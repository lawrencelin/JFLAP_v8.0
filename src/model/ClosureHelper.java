package model;

import java.util.Set;
import java.util.TreeSet;

import util.JFLAPConstants;

import model.algorithms.testinput.parse.lr.SLR1Production;
import model.automata.Automaton;
import model.automata.State;
import model.automata.SingleInputTransition;
import model.automata.TransitionSet;
import model.grammar.Grammar;
import model.grammar.Production;
import model.symbols.Symbol;

public class ClosureHelper implements JFLAPConstants{

	public static TreeSet<State> takeClosure(State s, Automaton m){
		TreeSet<State> closure = new TreeSet<State>();

		TransitionSet<? extends SingleInputTransition> set = m.getTransitions();

		closure.add(s);
		boolean changed = true;
		
		while (changed){
			changed = false;
			for (State state: new TreeSet<State>(closure)){
				for (SingleInputTransition t: set.getTransitionsFromState(state)){
					if (t.isLambdaTransition()){
						changed = closure.add(t.getToState()) || changed;
					}
				}
			}
		}
		return closure;
	}
	
	/**
	 * Takes the closure of production p in the grammar g. This
	 * is based on the index of the current symbol with a marker
	 * to its left on the rhs of this production. Used specifically
	 * in the context of SLR1 parsing although could have applications
	 * elsewhere. Updates the marker location on productions.
	 * 
	 * @param p
	 * @return
	 */
	public static Set<SLR1Production> takeClosure(SLR1Production p, Grammar g){
		return recursiveTakeClosure(p, g, new TreeSet<SLR1Production>());
	}
	
	private static Set<SLR1Production> recursiveTakeClosure(SLR1Production p, Grammar g, Set<SLR1Production> closure){
		
		if (closure.contains(p))
			return closure;
		
		closure.add(p);
		
		Symbol s = p.getSymbolAfterMarker();
		
		if (s == null || Grammar.isTerminal(s))
			return closure;
		
		
		for (Production prod: g.getProductionSet().getProductionsWithSymbolOnLHS(s)){
			SLR1Production slr1P = new SLR1Production(prod);
			recursiveTakeClosure(slr1P, g, closure);
		}
		
		return closure;
			
	}

}
