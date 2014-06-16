package model.regex.operators;

import model.formaldef.Describable;
import model.grammar.Terminal;
import model.symbols.Symbol;

public abstract class Operator extends Terminal implements Describable{

	public Operator(String s) {
		super(s);
	}
	
}
