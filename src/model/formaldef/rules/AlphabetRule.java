package model.formaldef.rules;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import errors.BooleanWrapper;

import model.formaldef.Describable;
import model.formaldef.FormalDefinition;
import model.formaldef.components.alphabets.Alphabet;
import model.symbols.Symbol;




public abstract class AlphabetRule<S extends Alphabet> implements Describable, Comparable<AlphabetRule<S>>{
	
	public abstract BooleanWrapper canModify(S a, Symbol oldSymbol, Symbol newSymbol);

	public abstract BooleanWrapper canRemove(S a, Symbol oldSymbol);

	public abstract BooleanWrapper canAdd(S a, Symbol newSymbol);
	
	
	public String toString(){
		return this.getDescriptionName() + ": " + this.getDescription();
	}

	@Override
	public int compareTo(AlphabetRule<S> o) {
		return this.getDescriptionName().compareTo(o.getDescriptionName());
	}
	
	
}
