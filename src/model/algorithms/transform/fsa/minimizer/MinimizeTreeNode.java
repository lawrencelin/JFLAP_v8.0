package model.algorithms.transform.fsa.minimizer;
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
import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import util.UtilFunctions;

import model.automata.State;
import model.automata.StateSet;
import model.automata.acceptors.FinalStateSet;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.symbols.Symbol;

/**
 * The Minimize Tree Node object is merely a default mutable tree node with an
 * additional field of data. This additional field is a String. You can set and
 * get this field. Other than that addition, Minimize Tree Nodes function
 * identically to Default Mutable Tree Nodes. You can insert them into
 * DefaultTreeModels, etc. This particular node is used in the tree of
 * distinguishable groups created during the DFA minimize operation.
 * 
 * @author Ryan Cavalcante
 */

public class MinimizeTreeNode extends DefaultMutableTreeNode implements Comparable<MinimizeTreeNode> {
	
	private Symbol mySplit;
	
	/**
	 * Creates a new <CODE>MinimizeTreeNode</CODE> with <CODE>userObject</CODE>
	 * as its user object and the empty string as its terminal.
	 * 
	 * @param userObject
	 *            the node's user object
	 */
	public MinimizeTreeNode(State ... group) {
		super(sortGroup(group));
	}
	


	private static State[] sortGroup(State[] group) {
		Arrays.sort(group);
		return group;
	}



	@Override
	public MinimizeTreeNode getChildAt(int index) {
		return (MinimizeTreeNode) super.getChildAt(index);
	}

	/**
	 * Sets the node's terminal field to <CODE>terminal</CODE>.
	 * 
	 * @param terminal
	 *            the node's terminal
	 */
	public void setSplittingSymbol(Symbol s) {
		mySplit = s;
	}

	/**
	 * Returns the node's terminal field
	 * 
	 * @return the node's terminal field
	 */
	public Symbol getSplittingSymbol() {
		return mySplit;
	}

	/**
	 * Returns the states on this node.
	 * 
	 * @return the array of states for this group
	 */
	public State[] getStateGroup() {
		return (State[]) getUserObject();
	}



	public boolean containsState(State state) {
		return Arrays.asList(getStateGroup()).contains(state);
	}



	public String createStateName() {
		return UtilFunctions.toDelimitedString(getStateGroup(), "");
	}



	@Override
	public int compareTo(MinimizeTreeNode o) {
		State[] mine = this.getStateGroup();
		State[] other = o.getStateGroup();
		
		int compare = new Integer(mine.length).compareTo(other.length);
		int i =0;
		while (compare == 0 && i < mine.length){
			compare = mine[i].compareTo(other[i]);
			i++;
		}
		
		return compare;
	}

}
