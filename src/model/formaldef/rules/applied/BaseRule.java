package model.formaldef.rules.applied;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import errors.BooleanWrapper;

import model.formaldef.FormalDefinition;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.rules.AlphabetRule;
import model.symbols.Symbol;




public class BaseRule extends AlphabetRule {

	
	@Override
	public BooleanWrapper canModify(Alphabet a, Symbol oldSymbol, Symbol newSymbol) {
		BooleanWrapper canRemove = this.canRemove(a, oldSymbol);
		BooleanWrapper canAdd = this.canAdd(a, newSymbol);
		
		return BooleanWrapper.combineWrappers(canRemove, canAdd);
	}

	@Override
	public BooleanWrapper canRemove(Alphabet a, Symbol oldSymbol) {
//		return new BooleanWrapper(a.contains(oldSymbol), 
//						"This " + a.getDescriptionName() + " does not contain " + "the symbol " + oldSymbol.getString());
		return new BooleanWrapper(true);

	}

	@Override
	public BooleanWrapper canAdd(Alphabet a, Symbol newSymbol) {
		if (newSymbol.length() <= 0)
			return new BooleanWrapper(false, "You may not add a symbol of no length to any alphabet.");
		return new BooleanWrapper(true, "Symbol " + newSymbol.getString() + " can be added to the " + a.getDescriptionName() +" sucessfully");
	}

	@Override
	public String getDescriptionName() {
		return "Basic Rule";
	}

	@Override
	public String getDescription() {
		return "The basic rules that all alphabets are held to individually. These include...";
	}


}
