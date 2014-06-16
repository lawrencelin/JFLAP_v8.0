package model.formaldef.rules.applied;

import model.automata.turing.BlankSymbol;
import model.automata.turing.TapeAlphabet;

public class TuringMachineBlankRule extends PermanentSpecialSymbolRule<TapeAlphabet, BlankSymbol> {

	public TuringMachineBlankRule(BlankSymbol specialSymbol) {
		super(specialSymbol);
	}

	@Override
	public String getDescriptionName() {
		return "Turing Machine Blank Symbol Rule";
	}

	@Override
	public String getDescription() {
		return "This rule prevents the blank symbol from being modified or removed in " +
				"the Tape Alphabet.";
	}


}
