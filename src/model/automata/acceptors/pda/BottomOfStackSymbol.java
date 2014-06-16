package model.automata.acceptors.pda;

import java.util.Set;
import java.util.TreeSet;

import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.alphabets.Alphabet;
import model.symbols.SpecialSymbol;
import model.symbols.Symbol;

public class BottomOfStackSymbol extends SpecialSymbol {

	public BottomOfStackSymbol(String s) {
		super(s);
	}

	public BottomOfStackSymbol(Symbol s) {
		super(s);
	}

	public BottomOfStackSymbol() {
		super();
	}

	@Override
	public Character getCharacterAbbr() {
		return 'z';
	}

	@Override
	public String getDescription() {
		return "The symbol at the bottom of the stack!";
	}

	@Override
	public String getDescriptionName() {
		return "Bottom of Stack Symbol";
	}

	@Override
	public Class<? extends Alphabet> getAlphabetClass() {
		return StackAlphabet.class;
	}
	
	
}
