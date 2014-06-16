package model.languages.components;

import model.symbols.Symbol;
import model.symbols.SymbolString;

public class ReverseComponent {

	public ReverseComponent () {
		
	}
	
	
	public static SymbolString reverse (SymbolString string) {
		SymbolString rev = new SymbolString();
		for (int i = string.size()-1; i >= 0; i--) {
			rev.add(string.get(i));
		}
		return rev;
	}
	
	
	public static void main (String[] args) {
		System.out.println(reverse(new SymbolString(new Symbol("a"), new Symbol("b"))));
	}
}
