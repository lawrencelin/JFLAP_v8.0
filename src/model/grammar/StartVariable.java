package model.grammar;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import errors.BooleanWrapper;
import model.formaldef.UsesSymbols;
import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.alphabets.Alphabet;
import model.symbols.SpecialSymbol;
import model.symbols.Symbol;

public class StartVariable extends SpecialSymbol{

	public StartVariable(String s) {
		super(new Variable(s));
	}

	public StartVariable(Variable v) {
		super(v);
	}
	
	public StartVariable(){
		this ((Variable)null);
	}

	@Override
	public String getDescriptionName() {
		return "Start Variable";
	}

	@Override
	public String getDescription() {
		return "I am the start variable of the Grammar.";
	}

	@Override
	public Character getCharacterAbbr() {
		return 'S';
	}

	@Override
	public Variable getSymbol() {
		return (Variable) super.getSymbol();
	}

	@Override
	public boolean setSymbol(Symbol s) {
		if (s != null && !(s instanceof Variable))
			s = new Variable(s.getString());
		return super.setSymbol(s);
	}

	@Override
	public Class<? extends Alphabet> getAlphabetClass() {
		return VariableAlphabet.class;
	}

	
}
