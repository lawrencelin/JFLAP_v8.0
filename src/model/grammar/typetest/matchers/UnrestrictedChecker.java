package model.grammar.typetest.matchers;

import model.grammar.Production;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public class UnrestrictedChecker extends GrammarChecker{

		@Override
		public boolean matchesRHS(Symbol[] rhs) {
			return true;
		}

		@Override
		public boolean matchesLHS(Symbol[] lhs) {
			return lhs.length != 0;
		}

	}