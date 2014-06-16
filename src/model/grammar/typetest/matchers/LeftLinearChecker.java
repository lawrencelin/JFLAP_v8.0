package model.grammar.typetest.matchers;

import model.grammar.Terminal;
import model.grammar.Variable;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public class LeftLinearChecker extends ContextFreeChecker {

	@Override
	public boolean matchesRHS(Symbol[] rhs) {
		return rhs.length == 0 || 
				checkLinear(rhs, Variable.class, Terminal.class, true) ||
				containsOnly(rhs, Terminal.class);
	}

}
