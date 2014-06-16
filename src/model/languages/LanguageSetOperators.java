package model.languages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import model.automata.InputAlphabet;
import model.formaldef.components.alphabets.Alphabet;
import model.symbols.Symbol;
import model.symbols.SymbolString;
import model.symbols.symbolizer.SimpleSymbolizer;
import model.symbols.symbolizer.Symbolizer;

/**
 * Operators that may be performed on a language (set of strings) but not
 * necessarily on sets consisting of generic objects
 * 
 * Note: works only with sets of SymbolStrings. Strings, Symbols, etc. should be
 * converted to SymbolStrings first.
 * 
 * @author Peggy Li
 * 
 */


public class LanguageSetOperators {


	public static Collection<SymbolString> kleeneStar (Collection<SymbolString> strings, int count) {
		Collection<SymbolString> results = new ArrayList<SymbolString>(strings);
		ArrayList<SymbolString> prev = new ArrayList<SymbolString>();
		prev.addAll(strings);

		loop: while (true) {
			for (SymbolString s : strings) {

				ArrayList<SymbolString> temp = new ArrayList<SymbolString>();
				for (SymbolString p : prev) {
					temp.add(new SymbolString(s).concat(p));
				}

				if (results.size() >= count)
					break loop;

				results.addAll(temp);
				prev.clear();
				prev.addAll(temp);
			}
		}

		results.add(new SymbolString());
		return results;
	}

	/**
	 * 
	 * 
	 * @param symbols
	 *            the characters in an alphabet
	 * @param count
	 *            number of strings to generate
	 * @return <code>count</code> number of strings formed from Kleene Star
	 * 
	 */

	public static Collection<SymbolString> kleeneStar(int count, Collection<Symbol> symbols) {
		Collection<SymbolString> symbolsToStrings = new ArrayList<SymbolString>();
		for (Symbol sym : symbols) {
			symbolsToStrings.add(new SymbolString(sym));
		}

		return kleeneStar(symbolsToStrings, count);
	}


	public static Collection<SymbolString> homomorphism(Set<SymbolString> strings, Symbol original, Symbol replaceWith) {
		Collection<SymbolString> results = new ArrayList<SymbolString>();
		for (SymbolString s : strings) {
			SymbolString replaced = s.replaceAll(original, replaceWith);
			results.add(replaced);
		}
		return results;
	}
	
	public static Set<SymbolString> concatination(Set<SymbolString> set1, Set<SymbolString> set2){
		Set<SymbolString> combinedSet = new TreeSet<SymbolString>();
		for (SymbolString t1 : set1){
			for (SymbolString t2 : set2){
				SymbolString temp = (SymbolString) t1.copy().concat(t2);
				combinedSet.add(temp);
			}
		}
		return createSortedSet(combinedSet);
	}
	
	public static Set<SymbolString> reverse(Set<SymbolString> set){
		Set<SymbolString> reversedSet = new TreeSet<SymbolString>();
		for(SymbolString t : set){
			SymbolString temp = (SymbolString) t.copy().reverse();
			reversedSet.add(temp);
		}
		return createSortedSet(reversedSet);
	}
	
	public static Set<SymbolString> rightQuotient(Set<SymbolString> set1, Set<SymbolString> set2){
		Set<SymbolString> combinedSet = new TreeSet<SymbolString>();
		for(SymbolString t1 : set1){
			for(SymbolString t2 : set2){
				if(t1.endsWith(t2)){
					combinedSet.add((SymbolString) t1.subList(0,t1.size()-t2.size()));
				}
			}
		}
		return createSortedSet(combinedSet);
	}
	
	public static Set<SymbolString> selfConcatination(Set<SymbolString> set, int n){
		if(n==0) return new TreeSet<SymbolString>();
		Set<SymbolString> newSet = new TreeSet<SymbolString>(set);
		for(int i=1; i<n;i++){
			newSet = concatination(set, newSet);
		}
		return createSortedSet(newSet);
	}
	
	public static Set<SymbolString> starClosure(Set<SymbolString> language, int maxConcat){
		Set<SymbolString> emptySet = new TreeSet<SymbolString>();
		emptySet.add(new SymbolString());
		return createSortedSet((SetOperators.union(emptySet, positiveClosure(language,maxConcat))));
	}
	
	public static Set<SymbolString> positiveClosure(Set<SymbolString> language, int maxConcat){
		Set<SymbolString> tempSet = new TreeSet<SymbolString>();
		for(int i=1;i<=maxConcat;i++){
			tempSet = SetOperators.union(tempSet, selfConcatination(language, i));
		}
		return createSortedSet(tempSet);
	}
	
	public static Set<SymbolString> createSortedSet(Set<SymbolString> set){
		Set<SymbolString> sortedSet = new TreeSet<SymbolString>(new SetComparator());
		sortedSet.addAll(set);
		return sortedSet;
	}
	
	public static Set<SymbolString> truncate(Set<SymbolString> set){
		Set<SymbolString> truncatedSet = new TreeSet<SymbolString>(new SetComparator());
		for(SymbolString string : set){
			SymbolString temp = string.subList(0, string.size()-1);
			truncatedSet.add(temp);
		}
		return truncatedSet;
	}
	
	public static void main (String[] args) {
		Set<SymbolString> set = new TreeSet<SymbolString>();
		Alphabet alphs = new InputAlphabet();
		for(char i='a';i<='z';i++){
			alphs.add(new Symbol(i+""));
		}
		Symbolizer s = new SimpleSymbolizer(alphs);
		set.add(s.symbolize("acdc"));
		set.add(s.symbolize("bccd"));
		Set<SymbolString> set2 = new TreeSet<SymbolString>();
		set2.add(s.symbolize("ccd"));
		set2.add(s.symbolize("d"));
		set2.add(s.symbolize("c"));
		System.out.println(rightQuotient(set, set2));
	}
}
	