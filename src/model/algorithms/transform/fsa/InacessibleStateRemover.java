package model.algorithms.transform.fsa;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import debug.JFLAPDebug;

import errors.BooleanWrapper;
import model.algorithms.AlgorithmException;
import model.algorithms.FormalDefinitionAlgorithm;
import model.algorithms.steppable.AlgorithmStep;
import model.automata.Automaton;
import model.automata.StartState;
import model.automata.State;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.formaldef.FormalDefinition;
import model.graph.PathFinder;

public class InacessibleStateRemover extends FormalDefinitionAlgorithm<Automaton> {

	private Automaton myNewAutomaton;
	private Set<State> myInaccessible;

	public InacessibleStateRemover(Automaton m) {
		super(m);
	}

	@Override
	public String getDescriptionName() {
		return "Remove Inaccessible States";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AlgorithmStep[] initializeAllSteps() {
		return new AlgorithmStep[]{new RemoveNextInaccesibleState()};
	}

	@Override
	public boolean reset() throws AlgorithmException {
		myNewAutomaton = (Automaton) this.getBaseAutomaton().copy();
		myInaccessible = new TreeSet<State>(Arrays.asList(findAllInacessibleStates(getBaseAutomaton())));
		return true;
	}

	@Override
	public BooleanWrapper[] checkOfProperForm(Automaton fd) {
		return new BooleanWrapper[0];
	}
	
	private Automaton getBaseAutomaton(){
		return getOriginalDefinition();
	}

	public Automaton getAdjustedAutomaton(){
		return myNewAutomaton;
	}

	private boolean autoRemoveState() {
		if (!inaccessibleStatesRemain())
			return false;
		return removeState((State)myInaccessible.toArray()[0]);
	}

	private boolean inaccessibleStatesRemain() {
		return !myInaccessible.isEmpty();
	}

	public boolean removeState(State state) {
		if (!myInaccessible.contains(state))
			throw new AlgorithmException("The state " + state.getName() + " is not " +
					"inacessible.");
		
		myInaccessible.remove(state);
		return myNewAutomaton.getStates().remove(state);
	}

	public static State[] findAllInacessibleStates(Automaton m) {
		State start = m.getStartState();
		Set<State> inaccessible = new TreeSet<State>();
		PathFinder finder = new PathFinder(m);
		for (State target: m.getStates()){
			if (finder.findPath(start, target) == null)
				inaccessible.add(target);
		}
		return inaccessible.toArray(new State[0]);
	}

	public static boolean hasUnreachableStates(Automaton m) {
		return findAllInacessibleStates(m).length > 0;
	}

	private class RemoveNextInaccesibleState implements AlgorithmStep{

		@Override
		public String getDescriptionName() {
			return "Remove Next Inacessible State";
		}

		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean execute() throws AlgorithmException {
			return autoRemoveState();
		}

		@Override
		public boolean isComplete() {
			return !inaccessibleStatesRemain();
		}
		
	}

}
