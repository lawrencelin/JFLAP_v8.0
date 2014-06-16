package model.formaldef.rules;

import model.formaldef.components.alphabets.Alphabet;

public abstract class IndividualIdenticalSymbolRule<T extends Alphabet, S extends Alphabet> extends IdenticalSymbolRule<T> {

	
	
	
	public IndividualIdenticalSymbolRule(S alphabet) {
		super(alphabet);
	}

}
