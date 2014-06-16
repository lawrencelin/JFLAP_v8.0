package model.symbols.symbolizer;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.management.RuntimeErrorException;

import universe.preferences.JFLAPPreferences;
import util.UtilFunctions;

import debug.JFLAPDebug;

import model.formaldef.FormalDefinition;
import model.formaldef.components.alphabets.Alphabet;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public class SimpleSymbolizer implements Symbolizer {

	
	private Map<String, Collection<Object>> myMap;
	private Set<String> myValid;
	private int myMax;
	private Alphabet[] myAlphabets;

	public SimpleSymbolizer(Alphabet... alphs){
		myAlphabets = alphs;
		myMap = new HashMap<String, Collection<Object>>();
		myValid = new HashSet<String>();
	}
	
	@Override
	public SymbolString symbolize(String in){
		myMap.clear();
		myValid.clear();
		myMax=getMaxSymbolLength(getParsingAlphabets());
		Collection<Object> extracted = new ArrayList<Object>();
		if (in==null) return null;
		for(String s:in.split(JFLAPPreferences.getSymbolizeDelimiter()))
			extracted.addAll(extractSymbols(s));
		SymbolString result = new SymbolString();
		for (Object o : extracted){
			if (o instanceof Symbol)
				result.add((Symbol) o);
			else{
				result.add(createSymbol((String) o));
			}
		}
		return result;
	}

	public Symbol createSymbol(String in) {
		return new Symbol(in);
	}

	public Alphabet[] getParsingAlphabets() {
		return myAlphabets;
	}

	public static int getMaxSymbolLength(Alphabet[] alphabets) {
		int max = 0;
		for (Alphabet a : alphabets)
			for (Symbol s: a){
				if (max<s.length()) max=s.length();
			}
		return max;
	}
	
	private Symbol isSymbol(String sub) {
		loop: for (Alphabet alph: getParsingAlphabets()){
			Symbol s = alph.getSymbolForString(sub);
			if (s != null) return s;
		}
		return null;
	}

	private Collection<Object> extractSymbols(String in) {

		//if this is not a valid string, return empty array
		if (in == null || in.length() == 0) 
			return new ArrayList<Object>();

		//if cached, use it!
		if (myMap.containsKey(in))
			return myMap.get(in);
		
		List<Object> temp = new ArrayList<Object>();
		
		//if this is a symbol already in alphabet, it is its own best option.
		if (in.length() <= myMax){	//improve performance with max
			Symbol newSym = isSymbol(in);
			if (newSym != null){
				temp.add(newSym);
				myMap.put(in, temp);
				return temp;
			}
		}
		
		List<List<Object>> options = new ArrayList<List<Object>>();

		//if this is a valid symbol, then lets add it as an option.
		if (myValid.contains(in) || isValidSymbol(in)){
			myValid.add(in);
			temp.add(in);
			options.add(temp);
		}

		for (int i = in.length()-1; i > 0; i--){
			temp = new ArrayList<Object>();
			String left = in.substring(0,i);
			String right = in.substring(i,in.length());
			temp.addAll(extractSymbols(left));
			temp.addAll(extractSymbols(right));
			options.add(temp);
		}
		if(options.isEmpty()) return new ArrayList<Object>();
		
		List<Object> best = getBest(options);
		myMap.put(in, best);

		return best;
	}

	public boolean isValidSymbol(String in) {
		return in.length()==1;
	}

	private List<Object> getBest(List<List<Object>> options) {
		List<Object> best = options.get(0);
		for (List<Object> option: options){
				best = getBest(best,option);
				
		}
		return best;
	}

	private List<Object> getBest(List<Object> best, List<Object> option) {
		int bestCount = stringCount(best);
		int optCount = stringCount(option);
		
		if (bestCount < optCount) return best;
		else if (bestCount == optCount){
			int bestSymbols = symbolCount(best);
			int optSymbols = symbolCount(option);
			if (bestSymbols <= optSymbols) return best;
		}

		return option;
	}

	private int symbolCount(List<Object> option) {
		int cnt=0;
		for (Object o: option){
			if (o instanceof Symbol) cnt++;
		}
		return cnt;
	}



	private int stringCount(List<Object> option) {
		int count = 0;
		for (Object o: option){
			if (o instanceof String)
				count+= ((String) o).length();
		}
		return count;
	}
}

