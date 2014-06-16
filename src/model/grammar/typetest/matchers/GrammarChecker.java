package model.grammar.typetest.matchers;

import model.formaldef.Describable;
import model.grammar.Grammar;
import model.grammar.Production;
import model.symbols.Symbol;
import model.symbols.SymbolString;



public abstract class GrammarChecker{
	
		public  boolean matchesGrammar(Grammar g){
			for (Production p: g.getProductionSet()){
				if (!this.matchesProduction(p))
					return false;
			}
			return true;
		}

		public boolean matchesProduction(Production p) {
			return this.matchesLHS(p.getLHS()) && this.matchesRHS(p.getRHS());
		}

		public abstract boolean matchesRHS(Symbol[] symbols);

		public abstract boolean matchesLHS(Symbol[] lhs);
		
}