package model.grammar.typetest.matchers;

import java.util.Set;
import java.util.TreeSet;

import universe.preferences.JFLAPPreferences;


import model.algorithms.testinput.parse.FirstFollowTable;
import model.algorithms.testinput.parse.ParserException;
import model.grammar.Grammar;
import model.grammar.GrammarException;
import model.grammar.Production;
import model.grammar.ProductionSet;
import model.grammar.Terminal;
import model.grammar.Variable;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public class LL1Checker extends GrammarChecker {

	@Override
	public boolean matchesRHS(Symbol[] rhs) {
		return throwError();
	}

	@Override
	public boolean matchesLHS(Symbol[] lhs) {
		return throwError();
	}

	@Override
	public boolean matchesProduction(Production p) {
		return throwError();
	}

	private boolean throwError() {
		throw new GrammarException("This method should never be called, " +
				"LL1 checking is only for the grammar as a whole");
	}

	@Override
	public boolean matchesGrammar(Grammar g) {
		FirstFollowTable table;
		try {
		table = new FirstFollowTable(g);
		} catch (ParserException e){
			return false;
		}
		ProductionSet p = g.getProductionSet();
		
		for (Symbol v: g.getVariables()){
			Set<Production> prods = p.getProductionsWithSymbolOnLHS(v);
			if (prods.size() < 2) continue;
			boolean isLL1 = isLL1((Variable) v, 
											prods.toArray(new Production[0]), 
											table);
			
			if (!isLL1)
				return false;
		}
		return true;
	}

	private boolean isLL1(Variable v, Production[] p,
			FirstFollowTable table) {
		for (int i = 0; i< p.length; i++){
			for (int j = i+1; j < p.length; j++){
				if (areAmbiguous(v,p[i],p[j],table))	
					return false;
			}
		}
		return true;
	}

	private boolean areAmbiguous(Variable v, Production A,
			Production B, FirstFollowTable table) {
		Set<Terminal> firstA = table.retrieveFirstSet(A.getRHS());
		Set<Terminal> firstB = table.retrieveFirstSet(B.getRHS());

//		check if they can derive the same first terminal or they
//		both derive the empty string. i.e. they have common elements
//		in their FIRST sets.
		if (!checkSharedContents(firstA, firstB))
			return true;
		
//		check if firstA does not contain empty and firstB share 
//		a terminal with FOLLOW(v) and vis-versa
		return !(noFollowConflict(table.getFollow(v), firstA, firstB) &&
				noFollowConflict(table.getFollow(v), firstB, firstA));
	}

	private boolean noFollowConflict(Set<Terminal> follow,
			Set<Terminal> firstA, Set<Terminal> firstB) {
		Terminal empty = JFLAPPreferences.getSubForEmptyString();
		
		if (!firstA.contains(empty))
			return true;
		
		return checkSharedContents(follow, firstB);
	}

	public boolean checkSharedContents(Set<Terminal> follow,
			Set<Terminal> firstB) {
		Set<Terminal> temp = new TreeSet<Terminal>(follow);
		temp.retainAll(firstB);
		
		return temp.isEmpty();
	}
	
	

}
