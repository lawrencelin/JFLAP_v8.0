package model.automata.turing;


import universe.preferences.JFLAPPreferences;

import errors.BooleanWrapper;
import model.formaldef.components.alphabets.Alphabet;
import model.symbols.SpecialSymbol;
import model.symbols.Symbol;

public class BlankSymbol extends SpecialSymbol {

	public BlankSymbol() {
		super();
	}

	@Override
	public Symbol getSymbol() {
		return JFLAPPreferences.getTMBlankSymbol();
	}
	
	@Override
	public Character getCharacterAbbr() {
		return 'b';
	}

	@Override
	public String getDescriptionName() {
		return "Turing Machine Blank Symbol";
	}

	@Override
	public String getDescription() {
		return "The blank symbol used to represent the empty strin on a " +
				"Turing Machine tape.";
	}
	
	@Override
	public boolean isPermanent() {
		return true;
	}

	@Override
	public BlankSymbol copy() {
		return new BlankSymbol();
	}
	
	@Override
	public BooleanWrapper isComplete() {
		return new BooleanWrapper(true);
	}
	
	@Override
	public Class<? extends Alphabet> getAlphabetClass() {
		return TapeAlphabet.class;
	}
}
