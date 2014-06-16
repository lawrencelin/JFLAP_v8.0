package model.automata.acceptors.pda;

import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.alphabets.Alphabet;



public class StackAlphabet extends Alphabet{


	@Override
	public String getDescriptionName() {
		return "Stack Alphabet";
	}

	@Override
	public Character getCharacterAbbr() {
		return '\u0393';
	}

	@Override
	public String getDescription() {
		return "The finite set of symbols that can be used " +
				"on the PDA FIFO stack.";
	}

	@Override
	public String getSymbolName() {
		return "Stack Symbol";
	}
	

	@Override
	public StackAlphabet copy() {
		return (StackAlphabet) super.copy();
	}
}
