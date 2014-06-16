package model.algorithms.transform.turing;

import java.util.ArrayList;
import java.util.List;

import model.algorithms.AlgorithmException;
import model.algorithms.steppable.AlgorithmStep;
import model.algorithms.transform.FormalDefinitionTransformAlgorithm;
import model.automata.State;
import model.automata.StateSet;
import model.automata.TransitionSet;
import model.automata.turing.MultiTapeTMTransition;
import model.automata.turing.MultiTapeTuringMachine;
import model.automata.turing.TuringMachineMove;
import model.symbols.Symbol;
import errors.BooleanWrapper;

/**
 * Transformation algorithm from Linz book on removing Stay transitions from a
 * single-tape Turing machine
 * 
 * @author Ian McMahon
 * 
 */
public class StayOptionRemover extends
		FormalDefinitionTransformAlgorithm<MultiTapeTuringMachine> {

	private List<MultiTapeTMTransition> stayTransitions;

	public StayOptionRemover(MultiTapeTuringMachine tm) {
		super(tm);
		initializeStayTransitions();
	}

	@Override
	public boolean reset() {
		super.reset();
		initializeStayTransitions();
		return true;
	}

	/**
	 * Compiles a list of all Stay transitions (that must be replaced).
	 */
	private void initializeStayTransitions() {
		stayTransitions = new ArrayList<MultiTapeTMTransition>();
		MultiTapeTuringMachine tm = getOriginalDefinition();

		for (MultiTapeTMTransition transition : tm.getTransitions()) {
			if (transition.getMove(0).equals(TuringMachineMove.STAY)) {
				stayTransitions.add(transition);
			}
		}
	}

	@Override
	public String getDescriptionName() {
		return "Stay Option Remover";
	}

	@Override
	public String getDescription() {
		return "Algorithm to remove Stay transitions from Turing Machines";
	}

	@Override
	public BooleanWrapper[] checkOfProperForm(MultiTapeTuringMachine tm) {
		if (tm.getNumTapes() != 1)
			return new BooleanWrapper[] { new BooleanWrapper(false,
					"The Turing machine has multiple tapes") };
		return new BooleanWrapper[0];
	}

	@Override
	public AlgorithmStep[] initializeAllSteps() {
		return new AlgorithmStep[] { new ProductionReplacementStep() };
	}

	private class ProductionReplacementStep implements AlgorithmStep {

		@Override
		public String getDescriptionName() {
			return "Production Replacement";
		}

		@Override
		public String getDescription() {
			return "Replaces a Stay Transition";
		}

		@Override
		public boolean execute() throws AlgorithmException {
			return replaceStayTransition();
		}

		@Override
		public boolean isComplete() {
			return stayTransitions.isEmpty();
		}

	}

	public boolean replaceStayTransition(MultiTapeTMTransition trans){
		MultiTapeTuringMachine tm = getTransformedDefinition();
		TransitionSet<MultiTapeTMTransition> transitionSet = tm
				.getTransitions();
		StateSet states = tm.getStates();
		if(!stayTransitions.contains(trans))
			return false;
		
		stayTransitions.remove(trans);
		transitionSet.remove(trans);

		State newState = states.createAndAddState();
		MultiTapeTMTransition rightReplacement = createRightReplacement(
				trans, newState);

		transitionSet.add(rightReplacement);

		for (Symbol c : getOriginalDefinition().getTapeAlphabet()) {
			MultiTapeTMTransition leftReplacement = createLeftReplacement(
					trans, newState, c);

			transitionSet.add(leftReplacement);
		}
		return true;
	}
	/**
	 * Removes a single Stay transition (one step in the algorithm) and replaces
	 * it with one right move on the previous read and write terminals, and a
	 * left transition for each terminal in the tape alphabet.
	 */
	public boolean replaceStayTransition() {
		return replaceStayTransition(stayTransitions.get(0));
	}

	public int getNumUnconverted() {
		return stayTransitions.size();
	}
	/**
	 * Helper method to create the single right replacement needed
	 */
	private MultiTapeTMTransition createRightReplacement(
			MultiTapeTMTransition transition, State newState) {
		State from = transition.getFromState();
		Symbol read = transition.getRead(0), write = transition.getWrite(0);
		TuringMachineMove move = TuringMachineMove.RIGHT;

		return new MultiTapeTMTransition(from, newState, read, write, move);
	}

	/**
	 * Helper method to create a left replacement on the given Symbol
	 */
	private MultiTapeTMTransition createLeftReplacement(
			MultiTapeTMTransition transition, State newState, Symbol c) {
		State to = transition.getToState();
		TuringMachineMove move = TuringMachineMove.LEFT;

		return new MultiTapeTMTransition(newState, to, c, c, move);
	}

	public MultiTapeTMTransition getFirstTransition() {
		return stayTransitions.isEmpty() ? null : stayTransitions.get(0);
	}
}
