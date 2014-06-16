package model.grammar.typetest.matchers;

import model.grammar.Terminal;
import model.grammar.Variable;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public class RightLinearChecker extends ContextFreeChecker {

	@Override
	public boolean matchesRHS(Symbol[] rhs) {
		return rhs.length == 0 || 
				checkLinear(rhs, Terminal.class, Variable.class, false) ||
				containsOnly(rhs, Terminal.class);
	}

}
