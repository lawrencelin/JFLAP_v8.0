package model.algorithms.transform.grammar;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

import debug.JFLAPDebug;

import errors.BooleanWrapper;

import model.algorithms.AlgorithmException;
import model.algorithms.steppable.AlgorithmExecutingStep;
import model.algorithms.steppable.AlgorithmStep;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.ProductionSet;
import model.grammar.Variable;
import model.symbols.SymbolString;

public class UselessProductionRemover extends GrammarTransformAlgorithm {

	private ProductionSet myDeriveTerms;
	private ProductionSet myFullDerivesTerminals;
	private ProductionSet myProcessedProductions;
	private Set<Variable> myVarsDeriveTerms;
	private ConstructDependencyGraphStep myConstructDependencyGraphStep;
	
	public UselessProductionRemover(Grammar g) {
		super(g);
	}

	@Override
	public String getDescription() {
		return "Useless Production Remover";
	}

	@Override
	public AlgorithmStep[] initializeAllSteps() {
		myConstructDependencyGraphStep = new ConstructDependencyGraphStep();
		return new AlgorithmStep[]{new checkDerivesTerminals(),
				myConstructDependencyGraphStep,
				new RemoveAllUnreachableProductions()
				};
	}

	@Override
	public String getDescriptionName() {
		return "Removes all useless productions in a grammar.";
	}

	@Override
	public boolean reset() throws AlgorithmException {
		super.reset();
		myDeriveTerms = new ProductionSet();
		myProcessedProductions = new ProductionSet();
		myVarsDeriveTerms = new TreeSet<Variable>();
		myFullDerivesTerminals = new ProductionSet();

		constructTerminalDerivationSet();

		//check if is last start production and none derived 
		if (noStartProductionsDeriveTerms())
			throw new AlgorithmException("No start productions derive terminals." +
												" Therefore this grammar cannot derive any strings " +
												"and cannot be transformed further.");
		this.getTransformedGrammar().getProductionSet().clear();
		this.getTransformedGrammar().setStartVariable(this.getOriginalGrammar().getStartVariable());
		return true;
	}

	
	private void constructTerminalDerivationSet() {
		
		for (Production p : this.getOriginalGrammar().getProductionSet()){
			if(this.checkDerivesTerminals(p)){
				myFullDerivesTerminals.add(p);
			}
		}
	}
	
	private boolean checkDerivesTerminals(Production p) {
		return checkDerivesTerminals(p, new TreeSet<Production>());
	}

	private boolean checkDerivesTerminals(Production p,
			Set<Production> history) {
		//if this production has already been seen, i.e. we are in a loop
		// then it cannot derive a terminal on this path
		if (history.contains(p)) return false;
		
		history.add(p);
		
		//loop through all variables on the RHS.
		// if each variable derives terminals,
		// then so does p!
		for (Variable v: p.getVariablesOnRHS()){
			
			if (!checkDerivesTerminals(v, history)){
				return false;
			}
		}
		myVarsDeriveTerms.addAll(p.getVariablesOnLHS());
		return true;
	}

	private boolean checkDerivesTerminals(Variable v,
			Set<Production> history) {
		//memoizing!
		if (myVarsDeriveTerms.contains(v)){
			return true;
		}
		
		ProductionSet productions = this.getOriginalGrammar().getProductionSet();
		for (Production prod: productions.getProductionsWithSymbolOnLHS(v)){
			Set<Production> temp = new TreeSet<Production>(history);
			if (checkDerivesTerminals(prod, temp)){
				return true;
			}
		}

		return false;
	}

	private boolean noStartProductionsDeriveTerms() {
		Variable var = this.getOriginalGrammar().getStartVariable();
		return !myVarsDeriveTerms.contains(var);
	}

	private Production[] getUncheckedProductions() {
		Set<Production> all = new TreeSet<Production>(this.getOriginalGrammar().getProductionSet());
		all.removeAll(myProcessedProductions);
		return all.toArray(new Production[0]);
	}

	public boolean checkRemainingProductions(){
		for (Production p: this.getUncheckedProductions()){
			checkAndAddDerivesTerminals(p);
		}
		return true;
	}

	public boolean allProductionsChecked(){
		ProductionSet p = this.getOriginalGrammar().getProductionSet();
		return myProcessedProductions.size() == p.size();
	}

	public BooleanWrapper checkAndAddDerivesTerminals(Production p){
		//check in Production set
		ProductionSet prod = this.getOriginalGrammar().getProductionSet();
		if (!prod.contains(p))
			return new BooleanWrapper(false, "The production " + p + " is not a part of this grammar.");
		
		//check already checked
		if (this.hasProcessed(p))
			return new BooleanWrapper(false, "The production " + p + " has already been checked.");
		
		
		myProcessedProductions.add(p);
		
		//check derives terminals
		if (!myFullDerivesTerminals.contains(p))
			return new BooleanWrapper(false, "The production " + p + " does not derive terminals.");

		myDeriveTerms.add(p);
		this.getTransformedGrammar().getProductionSet().add(p);
		
		return new BooleanWrapper(true);
	}
	
	public boolean hasProcessed(Production p) {
		return myProcessedProductions.contains(p);
	}
	
	
	
	//////////////////////////////////////////////
	////////////// Algorithm Steps ///////////////
	//////////////////////////////////////////////
	
	private void removeUnreachableProductions() {
		ProductionSet prods = this.getTransformedGrammar().getProductionSet();
		for (Production p: prods.toArray(new Production[0])){
			removeUnreachableProduction(p);
		}
	}

	public BooleanWrapper removeUnreachableProduction(Production p) {
		if (!isUnreachable(p))
			return new BooleanWrapper(false, "The production " + p + " can be reached " +
					"from the start variable. Therefore it is not unreachable.");
		boolean removed = this.getTransformedGrammar().getProductionSet().remove(p);
		return new BooleanWrapper(removed, "There was an error removing the production "+ p +
				"from the grammar.");
	}

	public Production[] getRemainingInaccessibleProductions(){
		Set<Production> unreach = new TreeSet<Production>();
		for (Production p: this.getTransformedGrammar().getProductionSet()){
			if (isUnreachable(p))
				unreach.add(p);
		}
		return unreach.toArray(new Production[0]);
	}
	
	private boolean isUnreachable(Production p) {
		if (p.isStartProduction(this.getTransformedGrammar().getStartVariable()))
			return false;
		DependencyGraph graph = myConstructDependencyGraphStep.getAlgorithm().getDependencyGraph();
		Set<Variable> lhs = p.getVariablesOnLHS();
		Variable start = this.getTransformedGrammar().getStartVariable();
		Variable[] startDependencies = graph.getAllDependencies(start);
		if (Arrays.asList(startDependencies).containsAll(lhs))
			return false;
		return true;
	}

	public int getNumberInaccessibleProductionsLeft() {
		return getRemainingInaccessibleProductions().length;
	}
	
	@Override
	public BooleanWrapper[] checkOfProperForm(Grammar g) {
		return new BooleanWrapper[0];
	}



	private class checkDerivesTerminals implements AlgorithmStep {

		@Override
		public String getDescriptionName() {
			return "Check if Variable derives Terminal string.";
		}

		@Override
		public String getDescription() {
			return "Checks if, starting wit the variable in question," +
					"one can apply a sequence of productions such that" +
					"the result is all terminals";
		}

		@Override
		public boolean execute() throws AlgorithmException {
			return checkRemainingProductions();
		}

		@Override
		public boolean isComplete() {
			return allProductionsChecked();
		}
		
	}

	private class ConstructDependencyGraphStep extends AlgorithmExecutingStep<ConstructDependencyGraph>{

		@Override
		public ConstructDependencyGraph initializeAlgorithm() {
			return new ConstructDependencyGraph(getTransformedGrammar());
		}
		
	}
	
	private class RemoveAllUnreachableProductions implements AlgorithmStep{

		@Override
		public String getDescriptionName() {
			return "Remove Unreachable Productions";
		}

		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean execute() throws AlgorithmException {
			removeUnreachableProductions();
			return true;
		}

		@Override
		public boolean isComplete() {
			return getNumberInaccessibleProductionsLeft() == 0;
		}
		
	}
	
}
