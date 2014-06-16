package model.algorithms.testinput.parse.lr.rules;

import model.algorithms.testinput.parse.lr.SLR1DFAState;
import model.automata.State;
import model.automata.acceptors.fsa.FSATransition;
import model.grammar.Variable;

public class EndReduceRule extends StateUsingRule {


	public EndReduceRule(State to) {
		super(to);
	}

	@Override
	public String getDescriptionName() {
		return "Finish Reduce Rule";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		return ""+ getToState().getID();
	}

}
