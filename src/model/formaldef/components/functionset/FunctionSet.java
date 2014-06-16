package model.formaldef.components.functionset;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import util.UtilFunctions;

import errors.BooleanWrapper;

import model.formaldef.UsesSymbols;
import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.SetComponent;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.functionset.function.LanguageFunction;
import model.symbols.Symbol;

public abstract class FunctionSet<T extends LanguageFunction<T>> extends SetComponent<T> implements UsesSymbols{

	@Override
	public BooleanWrapper isComplete() {
		return new BooleanWrapper(true);
	}


	@Override
	public Set<Symbol> getSymbolsUsedForAlphabet(Alphabet a) {
		
		TreeSet<Symbol> used = new TreeSet<Symbol>();
		
		for (LanguageFunction<T> f: this){
			used.addAll(f.getSymbolsUsedForAlphabet(a));
		}
		return used;
	}
	
	@Override
	public boolean applySymbolMod(String from, String to) {
		boolean changed = false;
		for (LanguageFunction<T> f: this){
			changed = f.applySymbolMod(from,to) || changed;
		}
		return changed;
	}
	
	@Override
	public boolean purgeOfSymbols(Alphabet a, Collection<Symbol> s) {
		boolean result = false;
		for (LanguageFunction<T> f: this){
			result = f.purgeOfSymbols(a, s) || result;
		}
		return result;
	}
	
	@Override
	public String toString() {
		String str = this.getDescriptionName() + "\n\t\t";
		str += UtilFunctions.createDelimitedString(this, "\n\t\t");
		return str;
	}
	
	
}
