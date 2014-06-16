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





package model.algorithms.testinput.parse;

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




import java.util.Arrays;

import javax.swing.tree.DefaultMutableTreeNode;

import model.algorithms.testinput.parse.Derivation;
import model.grammar.Production;
import model.symbols.SymbolString;

/**
 * A parse node is used as an aide for brute force parsing. It contains a
 * derivation field showing a current derivation of a string, the grammar rules
 * that were substitute in to achieve this, and the position of those
 * substitutions.
 * <P>
 * 
 * For example, if the string "aACBb" were to derive "axxCyyb" based on the
 * rules "A->xx" and "B->yy", then the rules used array would hold those two
 * rules, and the substitution array would be {1,3} for the positions in the
 * original string that the substitutions happened in.
 * 
 * @author Thomas Finley
 */

public class ParseNode extends DefaultMutableTreeNode {

	private Derivation derivation;
	
	public ParseNode(Derivation derivation) {
		this.derivation = derivation;
//		if (productions.length != substitutions.length)
//			throw new IllegalArgumentException(
//					"Production and substitution array sizes mismatch!");
	}

	/**
	 * Instantiates a parse node based on an existing node.
	 * 
	 * @param node
	 *            the parse node to copy
	 */
	public ParseNode(ParseNode node) {
		this(node.derivation);
	}

	/**
	 * Returns the derivation string.
	 * 
	 * @return the derivation string
	 */
	public SymbolString getDerivation() {
		return derivation.createResult();
	}

	/**
	 * Returns the productions array for this node. For performance reasons this
	 * array could not be copied, and so must not be modified.
	 * 
	 * @return the productions that were substituted in to achieve the
	 *         derivation
	 */
	public Production[] getProductions() {
		return derivation.getProductionArray();
	}

	/**
	 * Returns the substitution positions. For performance reasons this array
	 * could not be copied, and so must not be modified.
	 * 
	 * @return the positions for the substitutions of the productions in the
	 *         parent derivation that led to this current derivation
	 */
	public Integer[] getSubstitutions() {
		return derivation.getSubstitutionArray();
	}

	/**
	 * Returns a string representation of those object.
	 * 
	 * @return a string representation of those object
	 */
	public String toString() {
		return derivation.toString();
	}
}
