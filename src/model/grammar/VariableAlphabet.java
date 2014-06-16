package model.grammar;

import java.util.Collection;
import java.util.List;

import errors.BooleanWrapper;
import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.rules.AlphabetRule;
import model.formaldef.rules.GroupingRule;
import model.symbols.Symbol;



public class VariableAlphabet extends Alphabet{


	@Override
	public String getDescriptionName() {
		return "Variables";
	}

	@Override
	public Character getCharacterAbbr() {
		return 'V';
	}

	@Override
	public String getDescription() {
		return "The Variable alphabet.";
	}

	@Override
	public String getSymbolName() {
		return "Variable";
	}

	@Override
	public boolean addAll(Collection<? extends Symbol> e) {
		for (Symbol s: e.toArray(new Symbol[0]))
		if (!(s instanceof Variable)){
			e.remove(s);
			((Collection<Symbol>) e).add(new Variable(s.getString()));
		}
		return super.addAll(e);
	}
	

	@Override
	public VariableAlphabet copy() {
		return (VariableAlphabet) super.copy();
	}
}
