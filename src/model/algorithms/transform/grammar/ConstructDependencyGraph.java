package model.algorithms.transform.grammar;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.TreeSet;

import debug.JFLAPDebug;

import errors.BooleanWrapper;
import model.algorithms.AlgorithmException;
import model.algorithms.FormalDefinitionAlgorithm;
import model.algorithms.steppable.AlgorithmStep;
import model.formaldef.FormalDefinition;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.Variable;

public class ConstructDependencyGraph extends FormalDefinitionAlgorithm<Grammar> {

	private DependencyGraph myDependancyGraph;
	private Map<Variable, Set<Variable>> myTotalDependencies;

	public ConstructDependencyGraph(Grammar fd) {
		super(fd);
	}

	@Override
	public String getDescriptionName() {
		return "Construct a Dependency Graph";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BooleanWrapper[] checkOfProperForm(Grammar fd) {
		return new BooleanWrapper[0];
	}

	@Override
	public boolean reset() throws AlgorithmException {
		myDependancyGraph = new DependencyGraph(getOriginalGrammar().getVariables());
		myTotalDependencies = getAllDependecies(); 
		return true;
	}

	@Override
	public AlgorithmStep[] initializeAllSteps() {
		return new AlgorithmStep[]{new AddArbitraryDependency()};
	}

	/**
	 * Finds all dependencies from a given production. Dependency
	 * is defined by default as variable v1 being on the LHS of
	 * a production with variable v2 on the RHS. v1 is dependent
	 * on v2.
	 * 
	 * NOTE: This method should be overriden to redefine how the
	 * dependency for a given production is determined.
	 * 
	 * @param p
	 * @return
	 */
	public Map<Variable, Set<Variable>> getDependenciesFromProd(Production p) {
		Set<Variable> varsLHS = p.getVariablesOnLHS();
		Set<Variable> varsRHS = p.getVariablesOnRHS();

		Map<Variable, Set<Variable>> map = new TreeMap<Variable, Set<Variable>>();
		
		for (Variable lhs: varsLHS){
			Set<Variable> dep = new TreeSet<Variable>(varsRHS);
			dep.remove(lhs);
			map.put(lhs, dep);
		}

		return map;
	}

	public Grammar getOriginalGrammar() {
		return getOriginalDefinition();
	}

	private Map<Variable, Set<Variable>> getAllDependecies() {
		Map<Variable, Set<Variable>> all = new TreeMap<Variable, Set<Variable>>();
		for (Production p: this.getOriginalGrammar().getProductionSet()){
			
			Map<Variable, Set<Variable>> dep = getDependenciesFromProd(p);
			for (Entry<Variable, Set<Variable>> e : dep.entrySet()) {
				if (!all.containsKey(e.getKey())){
					all.put(e.getKey(), new TreeSet<Variable>());
				}
				all.get(e.getKey()).addAll(e.getValue());
			}
		}

		return all;
	}
	
	public BooleanWrapper addDependency(Variable from, Variable to){
		Set<Variable> dep = myTotalDependencies.get(from);
		if (dep == null || !dep.contains(to)){
			return new BooleanWrapper(false, "There is no valid " +
					"dependency between " + from + " and " + to);
		}
		return new BooleanWrapper(myDependancyGraph.addDependency(from, to), 
				"The dependency from " + from + " to " + to + " has already been added.");
	}

	public int getNumberDependenciesLeft() {
		return totalDependenciesNeeded() - 
				myDependancyGraph.getNumberDependencies();
	}

	private int totalDependenciesNeeded() {
		int size = 0;
		for (Set<Variable> set : myTotalDependencies.values()) {
			size += set.size();
		}
		return size;
	}

	public DependencyGraph getDependencyGraph(){
		return myDependancyGraph;
	}
	
	private Variable getFirstIncompleteVar() {
		for (Variable v: myTotalDependencies.keySet()){
			int needed = myTotalDependencies.get(v).size();
			int complete = myDependancyGraph.getAllDependencies(v).length;
			if (needed > complete)
				return v;
		}
		return null;
	}

	public void addAllDependencyFor(Variable from) {
		for (Variable to: myTotalDependencies.get(from)){
			BooleanWrapper added = addDependency(from, to);
			if(added.isError())
				throw new AlgorithmException(added.getMessage());
		}
	}

	private class AddArbitraryDependency implements AlgorithmStep{

		@Override
		public String getDescriptionName() {
			return "Add Dependencies for Variable";
		}

		@Override
		public String getDescription() {
			return null;
		}

		@Override
		public boolean execute() throws AlgorithmException {
			addAllDependencyFor(getFirstIncompleteVar());
			return true;
		}

		@Override
		public boolean isComplete() {
			return getFirstIncompleteVar() == null;
		}
		
	}

}
