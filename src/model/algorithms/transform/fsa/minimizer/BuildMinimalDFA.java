package model.algorithms.transform.fsa.minimizer;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import debug.JFLAPDebug;

import errors.BooleanWrapper;
import model.algorithms.AlgorithmException;
import model.algorithms.FormalDefinitionAlgorithm;
import model.algorithms.steppable.AlgorithmStep;
import model.algorithms.steppable.SteppableAlgorithm;
import model.automata.State;
import model.automata.StateSet;
import model.automata.acceptors.FinalStateSet;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.formaldef.FormalDefinition;
import model.symbols.SymbolString;

public class BuildMinimalDFA extends SteppableAlgorithm {

	private MinimizeTreeModel myTreeModel;
	private FiniteStateAcceptor myMinimalDFA;
	private Map<State, MinimizeTreeNode> myStateToNodeMap;
	private Set<FSATransition> myTransitionsNeeded;

	public BuildMinimalDFA(MinimizeTreeModel model) {
		myTreeModel = model;
		reset();
	}

	@Override
	public String getDescriptionName() {
		return "Build Minimal DFA";
	}

	@Override
	public String getDescription() {
		return "Use a minimize tree and the equivalence groups " +
				"it defines to construct a minmized DFA.";
	}

	@Override
	public AlgorithmStep[] initializeAllSteps() {
		return new AlgorithmStep[]{ new AddArbitraryTransition()};
	}

	@Override
	public boolean reset() throws AlgorithmException {
		myStateToNodeMap = new TreeMap<State, MinimizeTreeNode>();
		myMinimalDFA = initalizeMinimalDFA();
		myTransitionsNeeded = findAllTransitionsNeeded();
		return true;
	}
	
	private Set<FSATransition> findAllTransitionsNeeded() {
		Set<FSATransition> needed = new TreeSet<FSATransition>();
		for (FSATransition trans: getOriginalDFA().getTransitions()){
			State from = getGroupStateForState(trans.getFromState());
			State to = getGroupStateForState(trans.getToState());
			needed.add(new FSATransition(from, to, new SymbolString(trans.getInput())));
		}
		return needed;
	}

	private State getGroupStateForState(State state) {
		for (Entry<State, MinimizeTreeNode> entry: myStateToNodeMap.entrySet()){
			if (entry.getValue().containsState(state))
				return entry.getKey();
		}
		//uhoh
		return null;
	}

	public FiniteStateAcceptor getOriginalDFA(){
		return myTreeModel.getDFA();
	}
	
	public boolean addTransitionToDFA(FSATransition trans){
		if (!myTransitionsNeeded.remove(trans)){
			return false;
		}
		return myMinimalDFA.getTransitions().add(trans);
	}

	private FiniteStateAcceptor initalizeMinimalDFA() {
		FiniteStateAcceptor fsa = getOriginalDFA().alphabetAloneCopy();
		
		State oldStart = getOriginalDFA().getStartState();
		FinalStateSet oldFinal = getOriginalDFA().getFinalStateSet();
		
		StateSet newStates = fsa.getStates();
		FinalStateSet newFinal = fsa.getFinalStateSet();
		
		for (MinimizeTreeNode group : myTreeModel.getLeaves()){
			String name = group.createStateName();
			int id = newStates.getNextUnusedID();
			State groupState = new State(name, id);
			newStates.add(groupState);
			
			for (State s: group.getStateGroup()){
				if (oldFinal.contains(s))
					newFinal.add(groupState);
				if (s.equals(oldStart))
					fsa.setStartState(groupState);
			}
			myStateToNodeMap.put(groupState, group);
		}
		
		return fsa;
	}
	
	public FiniteStateAcceptor getMinimalDFA(){
		if (!myTransitionsNeeded.isEmpty())
			throw new AlgorithmException("You may not retrieve the minimal " +
					"dfa until it is fully minimized");
		return myMinimalDFA;
	}

	private class AddArbitraryTransition implements AlgorithmStep{

		@Override
		public String getDescriptionName() {
			return "Add Transition to Minimal DFA";
		}

		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean execute() throws AlgorithmException {
			FSATransition toAdd = (FSATransition) myTransitionsNeeded.toArray()[0];
			return addTransitionToDFA(toAdd);
		}

		@Override
		public boolean isComplete() {
			return myTransitionsNeeded.isEmpty();
		}
		
	}

}
