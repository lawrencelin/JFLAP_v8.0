package model.grammar.typetest.matchers;

import model.grammar.Production;
import model.grammar.Terminal;
import model.grammar.Variable;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public class GNFChecker extends ContextFreeChecker{

	@Override
	public boolean matchesRHS(Symbol[] rhs) {
		return checkLinear(rhs, 
				Terminal.class, 
				Variable.class, 
				true);
	}


		
	}