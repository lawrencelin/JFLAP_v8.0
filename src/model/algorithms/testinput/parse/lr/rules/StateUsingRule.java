package model.algorithms.testinput.parse.lr.rules;

import model.algorithms.testinput.parse.lr.SLR1DFAState;
import model.automata.State;
import model.automata.acceptors.fsa.FSATransition;
import model.grammar.Terminal;

public abstract class StateUsingRule extends SLR1rule {

	private State myToState;

	public StateUsingRule(State to) {
		myToState = to;
	}

	public State getToState() {
		return myToState;
	}
	
}
