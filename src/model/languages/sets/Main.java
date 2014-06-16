package model.languages.sets;

import model.automata.InputAlphabet;
import model.formaldef.components.alphabets.Alphabet;
import model.symbols.Symbol;
import model.symbols.SymbolString;
import model.symbols.symbolizer.SimpleSymbolizer;
import model.symbols.symbolizer.Symbolizer;

public class Main {
	
	public static void main (String[] args) {
		Alphabet alpha = new InputAlphabet();
		alpha.addAll(new Symbol("a"), new Symbol("ab"));
		Symbolizer s = new SimpleSymbolizer(alpha);
		SymbolString a = s.symbolize("a");
		SymbolString ab = s.symbolize("ab");
		
		System.out.println(a.getFirst().getString());
		System.out.println(ab.getFirst().getString());
		System.out.println(a.compareTo(ab));
		
	}

}
