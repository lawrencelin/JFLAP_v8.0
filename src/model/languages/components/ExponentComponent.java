package model.languages.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import model.symbols.Symbol;
import model.symbols.SymbolString;

public class ExponentComponent extends AbstractComponent {
	
	// a in a^n
	private Symbol mySymbol;
	
	// n in a^n
	private Symbol myExponentSymbol;

	
	public ExponentComponent (Symbol character, Symbol exponentName) {	
		mySymbol = character;
		myExponentSymbol = exponentName;
	}

	
	@Override
	public Collection<Symbol> getSymbolsUsed() {
		Set<Symbol> symbols = new HashSet<Symbol>();
		symbols.add(mySymbol);
		symbols.add(myExponentSymbol);
		return symbols;
	}
	
	
	public SymbolString expand (int n) {
		SymbolString str = new SymbolString();
		for (int i = 0; i < n; i++) {
			str.add(mySymbol);
		}
		System.out.printf("%s\t when %s = %d", str, myExponentSymbol, n);
		return str;
	}
	
	
	public Collection<SymbolString> deriveInRange (int min, int max) {
		Collection<SymbolString> ans = new ArrayList<SymbolString>();
		for (int i = min; i <= max; i++) {
			ans.add(expand(i));
		}
		return ans;
	}
	
	
	
	public String toString () {
		return String.format("%s^%s ", mySymbol.toString(), myExponentSymbol.toString());
	}


	@Override
	public SymbolString deriveString() {
		// TODO Auto-generated method stub
		return null;
	}

}
