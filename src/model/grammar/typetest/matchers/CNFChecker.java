package model.grammar.typetest.matchers;

import model.grammar.Production;
import model.grammar.Terminal;
import model.grammar.Variable;
import model.symbols.Symbol;
import model.symbols.SymbolString;


public class CNFChecker extends ContextFreeChecker{

	@Override
	public boolean matchesRHS(Symbol[] rhs) {
		return isSingleTerminal(rhs) || isDoubleVar(rhs);
	}

	private boolean isDoubleVar(Symbol[] rhs) {
		return rhs.length == 2 &&
				rhs[0] instanceof Variable &&
				rhs[1] instanceof Variable;
	}

	private boolean isSingleTerminal(Symbol[] rhs) {
		return rhs.length == 1 &&
				rhs[0] instanceof Terminal;
	}
	
}