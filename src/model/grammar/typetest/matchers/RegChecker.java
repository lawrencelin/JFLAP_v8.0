package model.grammar.typetest.matchers;

import model.grammar.Grammar;
import model.grammar.Production;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public class RegChecker extends CNFChecker{

		private LeftLinearChecker myLeftLinearChecker;
		private RightLinearChecker myRightLinearChecker;

		public RegChecker() {
			myLeftLinearChecker = new LeftLinearChecker();
			myRightLinearChecker = new RightLinearChecker();
		}

		@Override
		public boolean matchesGrammar(Grammar g) {
			return myLeftLinearChecker.matchesGrammar(g) || 
					myRightLinearChecker.matchesGrammar(g);
		}

		@Override
		public boolean matchesRHS(Symbol[] rhs) {
			return myLeftLinearChecker.matchesRHS(rhs) || 
					myRightLinearChecker.matchesRHS(rhs);
		}
		
		
		
	
		
		
	}