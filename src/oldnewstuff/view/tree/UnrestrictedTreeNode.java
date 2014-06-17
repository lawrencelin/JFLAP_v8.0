package oldnewstuff.view.tree;
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






import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import universe.preferences.JFLAPPreferences;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public class UnrestrictedTreeNode extends DefaultMutableTreeNode {
	/** The text! */
	private SymbolString text;

	/** The weight. */
	public double weight = 1.0;

	/** The highest row. */
	public int highest = 0;

	/** The lowest row. */
	public int lowest = 0;
	
	/**
	 * Creates a new unrestricted tree node.
	 * 
	 * @param text
	 *            the label for this unrestricted tree node
	 */
	public UnrestrictedTreeNode(SymbolString text) {
		super(text);
		this.text = text;
		parent = null;
	}
	
	public UnrestrictedTreeNode(Symbol s) {
		this(new SymbolString(s));
	}
	
	public UnrestrictedTreeNode() {
		this(new SymbolString());
	}

	/**
	 * Returns the length of this node, which is the length of the text.
	 */
	public int length() {
		return text.size();
	}

	/**
	 * Returns the text.
	 * 
	 * @return the text
	 */
	public SymbolString getText() {
		return text;
	}

	/**
	 * Returns a string representation of the node.
	 * 
	 * @return a string representation of the node
	 */
	public String toString() {
		return super.toString();
		// return "("+text+", "+weight+")";
	}

}
