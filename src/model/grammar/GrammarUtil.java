package model.grammar;

import java.util.Set;
import java.util.TreeSet;

import model.symbols.Symbol;

public class GrammarUtil {

	public static boolean isLambdaProduction(Production p){
		return p.getRHS().length == 0;
	}
	

	
////////////////// DERIVES LAMBDA STUFF /////////////////////////
	private static Set<Production> myMemory;

	public static boolean derivesLambda(Symbol s, Grammar g){
		if (Grammar.isTerminal(s))
			return false;
		if (!Grammar.isVariable(s))
			throw new GrammarException("You may not check if a non-Terminal/Variable " +
					"derives Lambda");
		myMemory = new TreeSet<Production>();
		return recursiveDerivesLambda((Variable) s, g, new TreeSet<Production>());
	}
	
	public static boolean derivesLambda(Production p, Grammar g){
		myMemory = new TreeSet<Production>();
		return recursiveDerivesLambda(p, g, new TreeSet<Production>());
	}
	
	private static boolean recursiveDerivesLambda(Variable v,Grammar g, Set<Production> history) {
		ProductionSet prodSet = g.getProductionSet();
		//if one prod with v on lhs derives lambda then v derives lambda
		for (Production p: prodSet.getProductionsWithSymbolOnLHS(v)){
			if (recursiveDerivesLambda(p, g, history)){
				myMemory.add(p);
				return true;
			}
		}
		return false;
	}

	private static boolean recursiveDerivesLambda(Production p,Grammar g, Set<Production> history) {
		//check already determined lambda productions.
		if (myMemory.contains(p))
			return true;
		//check if lambda production
		if(p.isLambdaProduction())
			return true;
		//check if we have already looked at this prod
		if (history.contains(p))
			return false;
		//check if it has any terminals on RHS
		if (!p.getTerminalsOnRHS().isEmpty())
			return false;
		
		history = new TreeSet<Production>(history);
		history.add(p);
		//check if each variable on rhs derives lambda
		for (Variable v: p.getVariablesOnRHS()){
			if (!recursiveDerivesLambda(v, g, history))
				return false;
		}
		return true;
	}
	
}
