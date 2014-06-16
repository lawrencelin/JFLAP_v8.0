/*
 *  JFLAP - Formal Languages and Automata Package
 * 
 * 
 *  Susan H. Rodger
 *  Computer Science Department
 *  Duke University
 *  August 27, 2009

 *  Copyright (c) 2002-2009
 *  All rights reserved.

 *  JFLAP is open source software. Please see the LICENSE for terms.
 *
 */





package model.graph;

import java.util.HashMap;
import java.util.Map;

import debug.JFLAPDebug;

import model.automata.State;
import model.automata.acceptors.Acceptor;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.symbols.SymbolString;


/**
 * This is an object that checks if two deterministic finite state automatons
 * are equal, that is, you could rearrange the states in one and (except for
 * state names) have exactly the same automaton looking back at you from the
 * screen. This does not compare two DFAs to see if they accept the same
 * language!
 * 
 * @author Thomas Finley
 */

public class DFAEqualityChecker {
	/**
	 * "Hypothesize" that two states are equal in the graph isomorphism for this
	 * automaton. This function tests to see if that assumption is justifiable.
	 * 
	 * @param state1
	 *            the state in the first automaton
	 * @param state2
	 *            the state in the second automaton
	 * @param matching
	 *            the matchings of states in the first automaton to states in
	 *            the second automaton
	 */
	private boolean hypothesize(State state1, FiniteStateAcceptor a1,
								State state2, FiniteStateAcceptor a2,
								Map<State,State> matching) {
		{
			// Does state one already have a counterpart?
			State counterpart = (State) matching.get(state1);
			// If it does, is it the same?
			if (counterpart != null)
				return counterpart == state2;
			// We haven't visited this node yet.
			// Does "finality" match up?
			if (Acceptor.isFinalState(a1, state1)
					^ Acceptor.isFinalState(a2,state2))
				return false;
		}

		Map<SymbolString, FSATransition> labelToTrans1 = new HashMap<SymbolString, FSATransition>(), labelToTrans2 = new HashMap();
		FSATransition[] t1 = a1.getTransitions().getTransitionsFromState(state1).toArray(new FSATransition[0]);
		FSATransition[] t2 = a2.getTransitions().getTransitionsFromState(state2).toArray(new FSATransition[0]);
		// If they're not even the same length...
		if (t1.length != t2.length)
			return false;
		for (int i = 0; i < t1.length; i++) {
			labelToTrans1.put(new SymbolString(t1[i].getInput()), t1[i]);
			labelToTrans2.put(new SymbolString(t2[i].getInput()), t2[i]);
		}
		// Now, for each transition from state1, we can find the
		// corresponding transition in state2, if it exists.
		for (int i = 0; i < t1.length; i++) {
			SymbolString label = new SymbolString(t1[i].getInput());
			FSATransition counterpart = labelToTrans2.get(label);
			// Does the same transition exist in the other automaton?
			if (counterpart == null)
				return false;
			matching.put(state1, state2);
			boolean equal = hypothesize(t1[i].getToState(), a1,
						counterpart.getToState(),a2,
						matching);
			if (!equal) {
				matching.remove(state1);
				return false;
			}
		}
		return true;
	}

	/**
	 * Compares two DFAs for equality. The precondition is that these objects be
	 * instances of <CODE>FiniteStateAutomaton</CODE>, both are
	 * deterministic, and both have an initial state. Results are undefined
	 * otherwise.
	 * 
	 * @param one
	 *            the first dfa
	 * @param two
	 *            the second dfa
	 * @return <CODE>true</CODE> if the two DFAs are equal, <CODE>false</CODE>
	 *         if they are not
	 */
	public boolean equals(FiniteStateAcceptor a1, FiniteStateAcceptor a2) {
		// Make sure they have the same number of states.
		if (!checkStates(a1,a2) || !checkTransitions(a1,a2) || !checkAlphabet(a1,a2))
			return false;
		return hypothesize(a1.getStartState(),a1, 
							a2.getStartState(),a2,
								new HashMap<State,State>());
	}

	private boolean checkAlphabet(FiniteStateAcceptor a1, FiniteStateAcceptor a2) {
		return a1.getInputAlphabet().equals(a2.getInputAlphabet());
	}

	private boolean checkTransitions(FiniteStateAcceptor a1,
			FiniteStateAcceptor a2) {
		return a1.getTransitions().size() == a2.getTransitions().size();
	}

	private boolean checkStates(FiniteStateAcceptor a1, FiniteStateAcceptor a2) {
		return a1.getStates().size() == a2.getStates().size() &&
				a1.getFinalStateSet().size() == a2.getFinalStateSet().size();
	}
}
