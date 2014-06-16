package model.grammar;

import model.symbols.Symbol;


public class Variable extends Symbol {

	public Variable(String s) {
		super(s);
	}

	@Override
	public boolean equals(Object o) {
		return !(o instanceof Terminal) && super.equals(o);
	}
	
	@Override
	public String getDescriptionName() {
		return "Variable";
	}
	
	
	

}
