package model.automata.acceptors;

import errors.BooleanWrapper;
import model.automata.StateSet;

public class FinalStateSet extends StateSet {

	@Override
	public String getDescriptionName() {
		return "Final State Set";
	}

	@Override
	public String getDescription() {
		return "The set of final states.";
	}

	@Override
	public Character getCharacterAbbr() {
		return 'F';
	}

	@Override
	public BooleanWrapper isComplete() {
		return new BooleanWrapper(!isEmpty(), 
				"Your Automaton requires a non-empty " + this.getDescriptionName());
	}
	
	@Override
	public FinalStateSet copy() {
		return (FinalStateSet) super.copy();
	}

	
	
}
