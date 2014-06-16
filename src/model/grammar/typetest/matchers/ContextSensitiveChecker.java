package model.grammar.typetest.matchers;

import java.util.Set;
import java.util.TreeSet;

import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.Variable;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public class ContextSensitiveChecker extends GrammarChecker {

	private Symbol[] myLHS;
	private Set<Variable> rhsVars;

	@Override
	public boolean matchesGrammar(Grammar g) {
		rhsVars = new TreeSet<Variable>();
		for (Production p : g.getProductionSet())
			rhsVars.addAll(p.getVariablesOnRHS());
		return super.matchesGrammar(g);
	}

	@Override
	public boolean matchesRHS(Symbol[] symbols) {
		return myLHS.length <= symbols.length
				|| (myLHS.length == 1 && !rhsVars.contains((Variable) myLHS[0]) && symbols.length == 0);
	}

	@Override
	public boolean matchesLHS(Symbol[] lhs) {
		myLHS = lhs;

		int numVars = 0;
		for (Symbol s : lhs)
			if (s instanceof Variable) {
				numVars++;
			}

		return numVars > 0;
	}

}
