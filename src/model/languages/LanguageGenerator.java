package model.languages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.algorithms.transform.grammar.ConstructDependencyGraph;
import model.algorithms.transform.grammar.DependencyGraph;
import model.formaldef.FormalDefinition;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.Variable;
import model.grammar.typetest.matchers.ContextFreeChecker;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public abstract class LanguageGenerator {

	public static int LARGE_NUMBER = 100000;
	
	private Grammar myGrammar;
	private Set<SymbolString> myStringsInLanguage;
	private int myNumberToGenerate;
	
	public static LanguageGenerator createGenerator(FormalDefinition formalDef){
		Grammar g = GrammarFactory.createGrammar(formalDef);
		if(g == null) return null;
		
		if(new ContextFreeChecker().matchesGrammar(g) && !isGrammarFinite(g))
			return new ContextFreeLanguageGenerator(g.copy());
		return new BruteLanguageGenerator(g);
	}
	
	public LanguageGenerator(Grammar g){
		initialize(g);
	}
	
	protected void initialize(Grammar g) {
		myGrammar = g;
		myStringsInLanguage = new HashSet<SymbolString>();
	}
	
	protected void clear() {
		myStringsInLanguage.clear();
	}
	
	/**
	 * Returns a sorted list consisting of every SymbolString that has been put
	 * into <CODE>myStringsInLanguage</CODE>. Sorts based on length, followed by
	 * the natural order of SymbolStrings.
	 */
	public List<SymbolString> getStrings(int numberToGenerate) {
		clear();
		setNumberToGenerate(numberToGenerate);
		generateStrings();
		
		ArrayList<SymbolString> stringsList = new ArrayList<SymbolString>(
				getStringsInLanguage());
		Collections.sort(stringsList, new StringComparator());
		return stringsList;
	}
	
	/**
	 * Generates every string of specified length that can be produced by the
	 * language. As the BruteLanguageGenerator handles Unrestricted and 
	 * finite grammars, this will only be used on ContextFreeLanguageGenerator
	 * 
	 * @param length
	 *            the length of the strings desired.
	 */
	public List<SymbolString> getStringsOfLength(int length){
			clear();
			generateStringsOfLength(length);
			
			ArrayList<SymbolString> stringsList = new ArrayList<SymbolString>(
					getStringsInLanguage());
			Collections.sort(stringsList, new StringComparator());
			return stringsList;
	}
	
	
	public abstract void generateStrings();
	public abstract void generateStringsOfLength(int length);
	
	
	public Grammar getGrammar() {
		return myGrammar;
	}

	public Set<SymbolString> getStringsInLanguage() {
		return myStringsInLanguage;
	}
	
	public boolean addStringToLanguage(SymbolString string){
		return myStringsInLanguage.add(string);
	}

	public int getNumberToGenerate() {
		return myNumberToGenerate;
	}

	public void setNumberToGenerate(int myNumberToGenerate) {
		this.myNumberToGenerate = myNumberToGenerate;
	}

	/**
	 * Comparator for sorting the returned ArrayList in the
	 * <CODE>generate()</CODE> method.
	 */
	private class StringComparator implements Comparator<SymbolString> {

		@Override
		public int compare(SymbolString s1, SymbolString s2) {
			if (s1.size() != s2.size())
				return s1.size() - s2.size();
			return s1.compareTo(s2);
		}

	}
	
	/**
	 * Checks a grammar to see if there are a finite number of
	 * strings that it will accept.
	 */
	private static boolean isGrammarFinite(Grammar g) {
		// checks loops from each variable to itself
		for (Production p : g.getProductionSet())
			if (p.getVariablesOnRHS().contains(p.getLHS()[0]))
				return false;
		ConstructDependencyGraph construct = new ConstructDependencyGraph(
				g);
		construct.stepToCompletion();
		DependencyGraph graph = construct.getDependencyGraph();

		for (Symbol v : g.getVariables()) {
			if (loopExists(graph, v, new ArrayList<Symbol>()))
				return false;
		}
		return true;
	}

	private static boolean loopExists(DependencyGraph graph, Symbol v,
			List<Symbol> history) {
		if (history.contains(v))
			return true;
		history.add(v);
		Variable[] dependents = graph.getAllDependencies((Variable) v);
		for (Variable depend : dependents) {
			if (loopExists(graph, depend, history))
				return true;
		}
		history.remove(v);
		return false;
	}
}
