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

import debug.JFLAPDebug;
import model.algorithms.transform.fsa.NFAtoDFAConverter;
import model.algorithms.transform.fsa.minimizer.MinimizeDFAAlgorithm;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.automata.determinism.FSADeterminismChecker;

/**
 * This determines if two FSAs accept the same language.
 * 
 * @author Thomas Finley
 */

public class FSAEqualityChecker {
	/**
	 * Checks if two FSAs accept the same language.
	 * 
	 * @param fsa1
	 *            the first finite state automaton
	 * @param fsa2
	 *            the second finite state automaton
	 * @return <CODE>true</CODE> if <CODE>fsa1</CODE> and <CODE>fsa2</CODE>
	 *         accept the same language, <CODE>false</CODE> if they they do
	 *         not
	 */
	public boolean equals(FiniteStateAcceptor fsa1, FiniteStateAcceptor fsa2) {
		// Clone for safety.
		fsa1 = fsa1.copy();
		fsa2 = fsa2.copy();

		// Make sure they're DFAs.
		FSADeterminismChecker check = new FSADeterminismChecker();
		if(!check.isDeterministic(fsa1))
			fsa1 = NFAtoDFAConverter.convertToDFA(fsa1);
		if(!check.isDeterministic(fsa2))
			fsa2 = NFAtoDFAConverter.convertToDFA(fsa2);
		// Minimize the DFAs.
		fsa1 = MinimizeDFAAlgorithm.minimize(fsa1);
		fsa2 = MinimizeDFAAlgorithm.minimize(fsa2);
		// Check the minimized DFAs to see if they are the same.
		return checker.equals(fsa1, fsa2);
	}

	/** The equality checker. */
	private static DFAEqualityChecker checker = new DFAEqualityChecker();

}
