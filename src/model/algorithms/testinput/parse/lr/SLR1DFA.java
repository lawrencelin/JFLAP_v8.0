package model.algorithms.testinput.parse.lr;

import java.util.Set;
import java.util.TreeSet;

import debug.JFLAPDebug;

import model.ClosureHelper;
import model.automata.State;
import model.automata.StateSet;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.Terminal;
import model.grammar.Variable;
import model.symbols.Symbol;

public class SLR1DFA extends FiniteStateAcceptor {

	private Grammar myGrammar;

	public SLR1DFA(Grammar g) {
		this(g, true);
	}

	public SLR1DFA(Grammar g, boolean complete) {
		myGrammar = createModifiedGrammar(g);
		this.getInputAlphabet().addAll(g.getTerminals());
		this.getInputAlphabet().addAll(g.getVariables());

		createAndSetStartState();
		if (complete){
			completeDFA();
		}
			
	}

	private Grammar createModifiedGrammar(Grammar g) {
		Grammar modified = g.copy();
		String s_prime = "S'";
		while (modified.getVariables().containsSymbolWithString(s_prime)){
			s_prime += "'";
		}
		Variable newStart = new Variable(s_prime);
		Production start = new Production(newStart, modified.getStartVariable());
		modified.getVariables().add(newStart);
		modified.getProductionSet().add(start);
		modified.setStartVariable(newStart);
		return modified;
	}

	private void createAndSetStartState() {
		SLR1Production p = new SLR1Production(myGrammar.getStartProductions()[0]);
		Set<SLR1Production> closure = ClosureHelper.takeClosure(p, myGrammar);
		SLR1DFAState s = createAndAddSLR1state(closure);
		this.setStartState(s);
	}

	private SLR1DFAState createAndAddSLR1state(Set<SLR1Production> closure) {
		StateSet states = this.getStates();
		State s = states.createAndAddState();
		states.remove(s);
		SLR1DFAState sNew = new SLR1DFAState(s, closure);
		states.add(sNew);
		if (shouldBeFinal(sNew))
			this.getFinalStateSet().add(sNew);
		return sNew;
	}

	private boolean shouldBeFinal(SLR1DFAState sNew) {
		return !sNew.getReduceProductions().isEmpty();
	}

	public void completeDFA() {
		SLR1DFAState s;
		while((s = this.getIncompleteState()) != null){
			expandState(s);
		}
		
	}

	private void expandState(SLR1DFAState state) {
		Set<Symbol> needed = getSymbolsNeeded(state);
		for (Symbol s: needed){
			addToStateAndTrans(state, s);
		}
	}

	private SLR1DFAState addToStateAndTrans(SLR1DFAState from, Symbol s) {
		Set<SLR1Production> marked = from.getProductionsWithMarkBefore(s);
		Set<SLR1Production> closure = new TreeSet<SLR1Production>();
		for (SLR1Production p : marked){
			p = p.copy();
			p.shiftMarker();
			closure.addAll(ClosureHelper.takeClosure(p, myGrammar));
		}
		return addToStateAndTrans(from, s, closure);
		
	}

	public SLR1DFAState addToStateAndTrans(SLR1DFAState from, Symbol s,
			Set<SLR1Production> toSet) {
		SLR1DFAState to = getSLR1stateForSet(toSet);
		FSATransition trans = new FSATransition(from, to, s);
		this.getTransitions().add(trans);
		return to;
	}

	private SLR1DFAState getSLR1stateForSet(Set<SLR1Production> toSet) {
		SLR1DFAState to = getAlreadyAddedState(toSet);
		if (to == null)
			to = createAndAddSLR1state(toSet);
		return to;
	}

	private SLR1DFAState getAlreadyAddedState(Set<SLR1Production> toSet) {
		for (State s: this.getStates()){
			SLR1DFAState state = (SLR1DFAState) s;
			if (state.matchesSet(toSet)){
				return state;
			}
		}
		return null;
	}

	private Set<Symbol> getSymbolsNeeded(SLR1DFAState s) {
		Set<FSATransition> trans = this.getTransitions().getTransitionsFromState(s);
		Set<Symbol> needed = s.getSymbolsForTransition();
		for (FSATransition t: trans){
			needed.remove(t.getInput()[0]);
		}
		return needed;
	}

	private SLR1DFAState getIncompleteState() {
		for (State s: this.getStates()){
			SLR1DFAState state = (SLR1DFAState) s;
			if (!getSymbolsNeeded(state).isEmpty())
				return state;
		}
		return null;
	}

	public Grammar getGrammar() {
		return myGrammar;
	}

	
}
