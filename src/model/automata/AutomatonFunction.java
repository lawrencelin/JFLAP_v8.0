package model.automata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.functionset.function.LanguageFunction;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public abstract class AutomatonFunction<T extends AutomatonFunction<T>> extends LanguageFunction<T>{

	public AutomatonFunction() {
		super();
	}

	@Override
	public Set<Symbol> getSymbolsUsedForAlphabet(Alphabet a) {
		TreeSet<Symbol> symbols = new TreeSet<Symbol>();
		for(SymbolString str: this.getPartsForAlphabet(a))
			symbols.addAll(str);
		return symbols;
	}

	@Override
	public boolean purgeOfSymbols(Alphabet a, Collection<Symbol> s) {
		boolean removed = false;
		for(SymbolString str: this.getPartsForAlphabet(a)){
			if(str.removeAll(s))
				removed = true;
		}
		return removed;
	}

	
	
	@Override
	public List<Symbol> getAllSymbols() {
		List<Symbol> symbols = new ArrayList<Symbol>();
		for (SymbolString ss: getAllParts()){
			symbols.addAll(ss);
		}
		return symbols;
	}

	//these methods could prove dangerous if some was beign especially annoying.
	
	public abstract SymbolString[] getPartsForAlphabet(Alphabet a);
	
	public abstract SymbolString[] getAllParts();

}