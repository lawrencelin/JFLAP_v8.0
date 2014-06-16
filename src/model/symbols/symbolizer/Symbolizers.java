package model.symbols.symbolizer;

import model.formaldef.FormalDefinition;
import model.formaldef.components.alphabets.Alphabet;
import model.symbols.SymbolString;


public class Symbolizers {

	public static SymbolString symbolize(String in, FormalDefinition fd) {
		Symbolizer s = new AdvancedSymbolizer(fd);
		return s.symbolize(in);
	}
	
	public static SymbolString symbolize(String in, Alphabet ... alphs) {
		Symbolizer s = new SimpleSymbolizer(alphs);
		return s.symbolize(in);
	}

}
