package view.grammar.parsing.ll;

/*
 *  JFLAP - Formal Languages and Automata Package
 * 
 * 
 *  Susan H. Rodger
 *  Computer Science Department
 *  Duke University
 *  August 27, 2009

 *  Copyright (c) 2002-2009
 *  All rights reserved.

 *  JFLAP is open source software. Please see the LICENSE for terms.
 *
 */

import model.grammar.Grammar;
import model.grammar.Production;
import model.symbols.Symbol;

import java.util.*;

/**
 * This class generates {@link grammar.parse.LLParseTable}s for grammars.
 * 
 * @author Thomas Finley, edited by John Godbey
 */

public class LLParseTableGenerator {
	
	/** The cached first sets, maps from grammars to first sets. */
	private static WeakHashMap CACHED_FIRST = new WeakHashMap();

	/** The cached follow sets, maps from grammars to follow sets. */
	private static WeakHashMap CACHED_FOLLOW = new WeakHashMap();
	
	/**
	 * Can't instantiate this bad boy sparky.
	 */
	private LLParseTableGenerator() {
	}

	/**
	 * Generates a parse table for a particular grammar.
	 * 
	 * @param grammar
	 *            the grammar for which a complete parse table should be
	 *            generated
	 */
	public static LLParseTable generate(Grammar grammar) {
		LLParseTable table = new LLParseTable(grammar);
		Map first = first(grammar), follow = follow(grammar);
		Production[] productions = grammar.getProductionSet().toArray();
		for (int i = 0; i < productions.length; i++) {
			String alpha = properString(productions[i].getRHS());
			String A = properString(productions[i].getLHS());
			Set firsts = first(first, alpha);
			Iterator it = firsts.iterator();
			while (it.hasNext()) {
				String a = (String) it.next();
				if (!a.equals(""))
					table.addEntry(A, a, alpha);
			}
			if (!firsts.contains(""))
				continue;
			Set follows = (Set) follow.get(A);
			it = follows.iterator();
			while (it.hasNext())
				table.addEntry(A, (String) it.next(), alpha);
		}
		return table;
	}
	
	/**
	 * Calculate the first sets of a grammar.
	 * 
	 * @param grammar
	 *            the grammar to calculate first sets for
	 * @return a map of symbols in the grammar to the first sets of that symbol
	 *         for this grammar
	 */
	public static Map first(Grammar grammar) {
		if (CACHED_FIRST.containsKey(grammar))
			return (Map) CACHED_FIRST.get(grammar);
		Map first = new HashMap();
		// Put the terminals in the map.
		String[] terminals = grammar.getTerminals().getSymbolStringArray();
		for (int i = 0; i < terminals.length; i++) {
			Set termSet = new HashSet();
			termSet.add(terminals[i]);
			first.put(terminals[i], termSet);
		}
		// Put the variables in the map as empty sets.
		String[] variables = grammar.getVariables().getSymbolStringArray();
		for (int i = 0; i < variables.length; i++) {
			first.put(variables[i], new HashSet());
		}

		// Repeatedly go over the productions until there is no more
		// change.
		boolean hasChanged = true;
		Production[] productions = grammar.getProductionSet().toArray();
		while (hasChanged) {
			hasChanged = false;
			for (int i = 0; i < productions.length; i++) {
				String variable = properString(productions[i].getLHS());
				String rhs = properString(productions[i].getRHS());
				Set firstRhs = first(first, rhs);
				if (setForKey(first, variable).addAll(firstRhs))
					hasChanged = true;
			}
		}
		CACHED_FIRST.put(grammar, Collections.unmodifiableMap(first));
		return first(grammar);
	}
	
	/**
	 * Given a first map as returned by {@link #first(Grammar)} and a string
	 * containing some sequence of symbols, return the first for that sequence.
	 * 
	 * @param firstSets
	 *            the map of single symbols to a map
	 * @param sequence
	 *            a string of symbols
	 * @return the first set for that sequence of symbols
	 */
	public static Set first(Map firstSets, String sequence) {
		Set first = new HashSet();
		if (sequence.equals(""))
			first.add("");
		for (int j = 0; j < sequence.length(); j++) {
			Set s = setForKey(firstSets, sequence.substring(j, j + 1));
			if (!s.contains("")) {
				// Doesn't contain lambda. Add it and get the
				// hell out of dodge.
				first.addAll(s);
				break;
			}
			// Does contain lambda. Damn it.
			if (j != sequence.length() - 1)
				s.remove("");
			first.addAll(s);
			if (j != sequence.length() - 1)
				s.add("");
		}
		return first;
	}
	
	/**
	 * Given a grammar, this will return the follow mappings. This returns a map
	 * from the non-terminals in the grammar to the follow sets.
	 * 
	 * @param grammar
	 *            the grammar to calculate follow sets for
	 * @return the map of non-terminals to the follow sets
	 */
	public static Map follow(Grammar grammar) {
		if (CACHED_FOLLOW.containsKey(grammar))
			return (Map) CACHED_FOLLOW.get(grammar);
		Map follow = new HashMap();
		// Add the mapping from the initial variable to the end of
		// string character.
		Set initialSet = new HashSet();
		initialSet.add("$");
		follow.put(grammar.getStartVariable(), initialSet);
		// Make every follow mapping empty for now.
		String[] variables = grammar.getVariables().getSymbolStringArray();
		for (int i = 0; i < variables.length; i++)
			if (!variables[i].equals(grammar.getStartVariable()))
				follow.put(variables[i], new HashSet());
		// Get the first sets.
		Map firstSets = first(grammar);
		// Iterate repeatedly over the productions until we're
		// completely done.
		Production[] productions = grammar.getProductionSet().toArray();
		boolean hasChanged = true;
		while (hasChanged) {
			hasChanged = false;
			for (int i = 0; i < productions.length; i++) {
				String variable = properString(productions[i].getLHS());
				String rhs = properString(productions[i].getRHS());
				for (int j = 0; j < rhs.length(); j++) {
					String rhsVariable = rhs.substring(j, j + 1);
					if (!grammar.isVariable(new Symbol(rhsVariable)))
						continue;
					Set firstFollowing = first(firstSets, rhs.substring(j + 1));
					// Is lambda in that following the variable? For
					// A->aBb where lambda is in FIRST(b), everything
					// in FOLLOW(A) is in FOLLOW(B).
					if (firstFollowing.remove("")) {
						if (setForKey(follow, rhsVariable).addAll(
								setForKey(follow, variable)))
							hasChanged = true;
					}
					// For A->aBb, everything in FIRST(b) except
					// lambda is put in FOLLOW(B).
					if (setForKey(follow, rhsVariable).addAll(firstFollowing))
						hasChanged = true;
				}
			}
		}
		CACHED_FOLLOW.put(grammar, Collections.unmodifiableMap(follow));
		return follow(grammar);
	}
	
	/**
	 * Given a map to sets and a key, return the set.
	 */
	private static Set setForKey(Map map, Object key) {
		return (Set) map.get(key);
	}
	
	private static String properString(Symbol[] symbols){
		String val = "";
		for (Symbol symbol : symbols){
			val += symbol.toString();
		}
		return val;
	}
}