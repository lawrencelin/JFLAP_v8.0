package model.formaldef;

import java.util.Collection;
import java.util.Set;


import model.formaldef.components.alphabets.Alphabet;
import model.symbols.Symbol;

/**
 * A simple interface to give an object the quality of 
 * using symbols and therefore having some set of unique
 * symbols applied within that object. Involved with the
 * trimming of alphabets.
 * 
 * @author Julian Genkins
 *
 */
public interface UsesSymbols {

	public Set<Symbol> getSymbolsUsedForAlphabet(Alphabet a);
	
	public boolean applySymbolMod(String from, String to);
	
	public boolean purgeOfSymbols(Alphabet a, Collection<Symbol> s);
}
