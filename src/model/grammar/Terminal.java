package model.grammar;

import model.symbols.Symbol;


public class Terminal extends Symbol {

	public Terminal(String s) {
		super(s);
	}
	
	@Override
	public boolean equals(Object o) {
		return !(o instanceof Variable) && super.equals(o);
	}
	
	@Override
	public String getDescriptionName() {
		return "Terminal";
	}

}
