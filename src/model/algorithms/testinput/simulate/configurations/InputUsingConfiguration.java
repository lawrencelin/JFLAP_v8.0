package model.algorithms.testinput.simulate.configurations;

import debug.JFLAPDebug;
import model.algorithms.testinput.simulate.Configuration;
import model.automata.Automaton;
import model.automata.SingleInputTransition;
import model.automata.State;
import model.automata.TransitionSet;
import model.symbols.SymbolString;

public abstract class InputUsingConfiguration<S extends Automaton<T>, T extends SingleInputTransition<T>>
		extends Configuration<S, T> {

	public InputUsingConfiguration(S a, State s, int pos, SymbolString input,
			SymbolString... strings) {
		super(a, s, pos, input, null, strings);
	}

	@Override
	public int getPositionForIndex(int i) {
		return this.getStringForIndex(i).size();
	}

	@Override
	protected Configuration<S, T> createConfig(S a, State s, int ppos,
			SymbolString primary, int[] positions, SymbolString[] updatedClones)
			throws Exception {
		return createInputConfig(a, s, ppos, primary, updatedClones);
	}

	protected abstract Configuration<S, T> createInputConfig(S a, State s,
			int ppos, SymbolString input, SymbolString[] updatedClones)
			throws Exception;

	@Override
	protected boolean canMoveAlongTransition(T trans) {
		SymbolString remaining = getInput().subList(getPrimaryPosition());
		return remaining.startsWith(trans.getInput());
	}

	@Override
	protected int getNextPrimaryPosition(T trans) {
		return this.getPrimaryPosition() + trans.getInput().length;
	}

	@Override
	protected boolean isDone() {
		if (getInput().size() != getPrimaryPosition())
			return false;
		//You've parsed all input, now look for lambda transitions
		TransitionSet<T> transitions = getAutomaton().getTransitions();
		for (T trans : transitions.getTransitionsFromState(getState()))
			//if there is a lambda transition away from the state, take it.
			if (canMoveAlongTransition(trans) && !isInFinalState())
				return false;
		return true;
	}

	@Override
	protected String getPrimaryPresentationName() {
		return "Input";
	}

	private SymbolString getInput() {
		return this.getPrimaryString();
	}

}
