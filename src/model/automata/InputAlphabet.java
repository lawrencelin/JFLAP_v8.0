package model.automata;


import javax.swing.JOptionPane;

import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.alphabets.Alphabet;


public class InputAlphabet extends Alphabet{

	@Override
	public String getDescriptionName() {
		return "Input Alphabet";
	}

	@Override
	public Character getCharacterAbbr() {
		return "\u03A3".charAt(0);
	}

	@Override
	public String getDescription() {
		return "The Input alphabet for all automata.";
	}

	@Override
	public String getSymbolName() {
		return "Input Symbol";
	}
	
	@Override
	public InputAlphabet copy() {
		return (InputAlphabet) super.copy();
	}
	
	

}
