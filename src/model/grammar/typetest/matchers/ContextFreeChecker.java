package model.grammar.typetest.matchers;

import java.util.Arrays;

import model.grammar.Production;
import model.grammar.Terminal;
import model.grammar.Variable;
import model.symbols.Symbol;
import model.symbols.SymbolString;


public class ContextFreeChecker extends GrammarChecker{


		@Override
		public boolean matchesRHS(Symbol[] rhs) {
			return true;
		}

		@Override
		public boolean matchesLHS(Symbol[] lhs) {
			return lhs.length == 1 && lhs[0] instanceof Variable;
		}
		
		
		
		/***
		 * Checks if a symbol string is of the "linear" form.
		 * This means that is its of form AB* or A*B where
		 * A is of class first, B is of class second.
		 * 
		 * The boolean first_dominant is used to show whether the
		 * first symbol (true) is the one alone or if the last 
		 * symbol (false) is the one alone.
		 * 
		 * @param rhs
		 * @param first
		 * @param second
		 * @param first_dominant
		 * @return
		 */
		public static boolean checkLinear(Symbol[] rhs,
											Class<? extends Symbol> first,
											Class<? extends Symbol> second, 
											boolean first_dominant){
			
			if (rhs.length == 0){
				return false;
			}
			
			if ((first_dominant && 
					!(first.isAssignableFrom(rhs[0].getClass()))) ||
					!(second.isAssignableFrom(rhs[rhs.length-1].getClass()))){
				return false;
			}
			
			if (rhs.length == 1) return true;
			
			Class<? extends Symbol> repeated;
			Symbol[] subString;
			
			if(first_dominant){
				repeated = second;
				subString = Arrays.copyOfRange(rhs, 1, rhs.length);
			}
			else {
				repeated = first;
				subString = Arrays.copyOfRange(rhs, 0, rhs.length-1);
			}
			
			return containsOnly(subString, repeated);
			
		}

		public static boolean containsOnly(Symbol[] subString,
				Class<? extends Symbol> repeated) {
			
			for (Symbol symbol: subString){
				if (!(repeated.isAssignableFrom(symbol.getClass()))){ 
					return false;
				}
			}

			return true;
		}
}