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







import java.awt.Point;
import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.tree.DefaultTreeModel;

import debug.JFLAPDebug;

import errors.BooleanWrapper;

import model.algorithms.AlgorithmException;
import model.algorithms.FormalDefinitionAlgorithm;
import model.algorithms.steppable.AlgorithmStep;
import model.algorithms.transform.fsa.AddTrapStateAlgorithm;
import model.algorithms.transform.fsa.InacessibleStateRemover;
import model.automata.Automaton;
import model.automata.State;
import model.automata.StateSet;
import model.automata.acceptors.FinalStateSet;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.grammar.Terminal;
import model.symbols.Symbol;


public class BuildMinimizeTreeAlgorithm extends FormalDefinitionAlgorithm<FiniteStateAcceptor>{
	
	private MinimizeTreeModel myTree;
	
	/**
	 * Creates an instance of <CODE>Minimizer</CODE>.
	 */
	public BuildMinimizeTreeAlgorithm(FiniteStateAcceptor fsa) {
		super(fsa);
	}

	@Override
	public BooleanWrapper[] checkOfProperForm(FiniteStateAcceptor dfa) {
		List<BooleanWrapper> errors = new ArrayList<BooleanWrapper>();
		if (InacessibleStateRemover.hasUnreachableStates(dfa)){
			errors.add(new BooleanWrapper(false,
					"The DFA must have all accessible states removed before " +
					"a minimization tree can be constructed."));
		}
		if(AddTrapStateAlgorithm.trapStateNeeded(dfa)){
			errors.add(new BooleanWrapper(false,
					"The DFA must have all transitions defined, i.e. have a " +
					"trap state before a minimization tree can be constructed."));
		}
			
		return errors.toArray(new BooleanWrapper[0]);
	}

	@Override
	public boolean reset() throws AlgorithmException {
		myTree = new MinimizeTreeModel(getDFA());
		return true;
	}

	@Override
	public String getDescriptionName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AlgorithmStep[] initializeAllSteps() {
		return new AlgorithmStep[]{new SplitArbitraryNodeStep()};
	}

	public FiniteStateAcceptor getDFA(){
		return super.getOriginalDefinition();
	}
	
	/**
	 * Returns true if <CODE>group</CODE> can be further split.
	 * 
	 * @param group
	 *            the group
	 * @param automaton
	 *            the automaton
	 * @return true if <CODE>group</CODE> can be further split.
	 */
	public boolean isSplittable(MinimizeTreeNode group) {
		return getSymbolsToSplit(group).length > 0;
	}

	/**
	 * Returns the terminal that <CODE>group</CODE> can be split on.
	 * 
	 * @param group
	 *            the group being split
	 * @param fsa
	 *            the automaton
	 * @return the terminal that <CODE>group</CODE> can be split on.
	 */
	public Symbol[] getSymbolsToSplit(MinimizeTreeNode node) {
		List<Symbol> symbols = new ArrayList<Symbol>();
		for (Symbol s: getDFA().getInputAlphabet()) {
			if (isSplittableOnSymbol(node, s)) {
				symbols.add(s);
			}
		}
		return symbols.toArray(new Symbol[0]);
	}

	/**
	 * Returns true if <CODE>group</CODE> is splittable on <CODE>terminal</CODE>.
	 * 
	 * @param node
	 *            the group to split
	 * @param t
	 *            the terminal to split group on
	 * @param automaton
	 *            the automaton
	 * @return true if <CODE>group</CODE> is splittable on <CODE>terminal</CODE>.
	 */
	public boolean isSplittableOnSymbol(MinimizeTreeNode node, Symbol s) {
		return getGroupsFromGroupOnSymbol(node, s).size() > 1;
	}

	/**
	 * Returns true if <CODE>state</CODE> in <CODE>automaton</CODE> has a
	 * transition to one of the states in <CODE>group</CODE> on <CODE>terminal</CODE>.
	 * 
	 * @param state
	 *            the state.
	 * @param group
	 *            the set of states that state might have a transition to.
	 * @param terminal
	 *            the terminal
	 * @param automaton
	 *            the automaton.
	 * @return true if <CODE>state</CODE> in <CODE>automaton</CODE> has a
	 *         transition to one of the states in <CODE>group</CODE> on <CODE>terminal</CODE>.
	 */
	public boolean stateGoesToGroupOnTerminal(State state, State[] group, Symbol s) {
		return getGroupFromStateOnSymbol(state, s).equals(group);
	}

	/**
	 * Returns a list of groups (State[]) that <CODE>group</CODE> goes to on
	 * <CODE>terminal</CODE>.
	 * 
	 * @param node
	 *            the group
	 * @param t
	 *            the terminal
	 * @param automaton
	 *            the automaton
	 * @return a list of groups (State[]) that <CODE>group</CODE> goes to on
	 *         <CODE>terminal</CODE>.
	 */
	public Set<MinimizeTreeNode> getGroupsFromGroupOnSymbol(MinimizeTreeNode node, Symbol sym) {
		Set<MinimizeTreeNode> groups = new TreeSet<MinimizeTreeNode>();
		for (State s: node.getStateGroup()) {
			MinimizeTreeNode temp = getGroupFromStateOnSymbol(s, sym);
			if (temp != null)
				groups.add(temp);
		}
		return groups;
	}

	/**
	 * Retrieves the minimizing group that this state goes to on the 
	 * input sym. i.e. if State q1 has a transition to q2 on symbol a,
	 * and q1 is in the group {q1,q3,q4}, then this method will return
	 * {q1, q3, q4}.
	 * 
	 * @param s
	 * 		starting/from state
	 * @param sym
	 * 		splitting symbol
	 * @return
	 * 		group of states that this state goes to on sym
	 */
	public MinimizeTreeNode getGroupFromStateOnSymbol(State s, Symbol sym) {
		Set<FSATransition> fromSet = this.getDFA().getTransitions().getTransitionsFromState(s);
		for (FSATransition trans: fromSet) {
			if (trans.getInput()[0].equals(sym)) {
				return getGroupForState(trans.getToState());
			}
		}
		return null;
	}

	/**
	 * Returns an array of states that represents a group, of which <CODE>state</CODE>
	 * is a member.
	 * 
	 * @param state
	 *            the state.
	 * @return an array of states that represents a group, of which <CODE>state</CODE>
	 *         is a member.
	 */
	public MinimizeTreeNode getGroupForState(State state) {
		List<MinimizeTreeNode> leaves = myTree.getLeaves();
		for (MinimizeTreeNode node: leaves){
			if (node.containsState(state))
				return node;
			
		}
		return null;
	}

	/**
	 * Returns true if <CODE>automaton</CODE> is minimized (i.e. there are no
	 * distinguishable groups).
	 * 
	 * @param automaton
	 *            the automaton.
	 * @return true if <CODE>automaton</CODE> is minimized
	 */
	public boolean isFullyMinimized() {
		MinimizeTreeNode[] states = getAllDistinguishableGroups();
		return states.length == 0;
	}


	private MinimizeTreeNode[] getAllDistinguishableGroups() {
		Set<MinimizeTreeNode> groups = new TreeSet<MinimizeTreeNode>();
		for (MinimizeTreeNode leaf: myTree.getLeaves()){
			if (isSplittable(leaf))
				groups.add(leaf);
		}
		return groups.toArray(new MinimizeTreeNode[0]);
	}

	public MinimizeTreeModel getMinimizeTree() {
		return myTree;
	}

	/**
	 * Splits the input node based on the current state of the
	 * {@link MinimizeTreeModel}. If the node cannot be split on
	 * any terminal, the the method will return false.
	 * 
	 * @param toSplit
	 * @return
	 */
	public boolean split(MinimizeTreeNode toSplit) {
		
		for (Symbol s: this.getDFA().getInputAlphabet()){
			if (splitOnSymbol(toSplit, s)){
				return true;
			}
		}
	
		return false;
	}

	/**
	 * Attempts to split the node on the input symbol. If this is not
	 * possible, the method will return false. Otherwise the node
	 * will be split and the {@link MinimizeTreeModel} will be updated
	 * appropriately.
	 * 
	 * @param toSplit
	 * @param sym
	 * @return
	 */
	public boolean splitOnSymbol(MinimizeTreeNode toSplit, Symbol sym) {
		if (!isSplittableOnSymbol(toSplit, sym))
			return false;
		
		doSplit(toSplit, sym);
		return true;
	}

	/**
	 * Applies a split to the toSplit node with symbol s. This method 
	 * should only be called when it has been confirmed that the node
	 * is splittable on s.
	 * 
	 * @param toSplit
	 * @param s
	 */
	private void doSplit(MinimizeTreeNode toSplit, Symbol sym) {
		Map<MinimizeTreeNode, Set<State>> groupMap = new TreeMap<MinimizeTreeNode, Set<State>>();
		for (State s: toSplit.getStateGroup()){
			MinimizeTreeNode toGroup = getGroupFromStateOnSymbol(s, sym);
			if (!groupMap.containsKey(toGroup))
				groupMap.put(toGroup, new TreeSet<State>());
			
			groupMap.get(toGroup).add(s);
		}
		
		
		toSplit.setSplittingSymbol(sym);
		for (Set<State> child : groupMap.values()){
			toSplit.add(new MinimizeTreeNode(child.toArray(new State[0])));
		}
	}


	private class SplitArbitraryNodeStep implements AlgorithmStep{

		@Override
		public String getDescriptionName() {
			return "Split Arbitrary Node";
		}

		@Override
		public String getDescription() {
			return null;
		}

		@Override
		public boolean execute() throws AlgorithmException {
			MinimizeTreeNode toSplit = getAllDistinguishableGroups()[0];
			return split(toSplit);
		}

		@Override
		public boolean isComplete() {
			return isFullyMinimized();
		}
		
	}

}
