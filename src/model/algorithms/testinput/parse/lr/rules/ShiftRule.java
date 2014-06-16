package model.algorithms.testinput.parse.lr.rules;

import model.automata.State;

public class ShiftRule extends StateUsingRule {

	public ShiftRule(State to) {
		super(to);
	}

	@Override
	public String getDescriptionName() {
		return "Shift Rule";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		return "s" + getToState().getID();
	}
}
