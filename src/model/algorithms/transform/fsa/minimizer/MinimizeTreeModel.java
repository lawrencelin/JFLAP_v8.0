package model.algorithms.transform.fsa.minimizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import model.automata.State;
import model.automata.StateSet;
import model.automata.acceptors.FinalStateSet;
import model.automata.acceptors.fsa.FiniteStateAcceptor;

public class MinimizeTreeModel extends DefaultTreeModel {

	private FiniteStateAcceptor myDFA;

	public MinimizeTreeModel(FiniteStateAcceptor dfa) {
		super(createRootNode(dfa));
		myDFA = dfa;
	}
	
	/**
	 * Creates the root of the Minimization tree with children defined
	 * by the inital non-final/final split.
	 * 
	 * @return
	 * 		the root of the minimization tree
	 */
	private static MinimizeTreeNode createRootNode(FiniteStateAcceptor dfa) {
		StateSet states = dfa.getStates();
		MinimizeTreeNode root = new MinimizeTreeNode(states.toArray(new State[0]));
		FinalStateSet finalStates = dfa.getFinalStateSet();
		Set<State> nonFinalStates = new TreeSet<State>(states);
		nonFinalStates.removeAll(finalStates);
		root.add(new MinimizeTreeNode(finalStates.toArray(new State[0])));
		root.add(new MinimizeTreeNode(nonFinalStates.toArray(new State[0])));
		return root;
	}
	
	public List<MinimizeTreeNode> getLeaves(){
		return findLeaves(this.getRoot());
	}
	
	@Override
	public MinimizeTreeNode getRoot() {
		return (MinimizeTreeNode) super.getRoot();
	}

	/**
	 * A function design to recurse in a DFS through the tree
	 * model in order to find all leaf nodes. Considering that these
	 * trees are never that large, the inefficiency of this
	 * alghgorithm should not be an issue.
	 * 
	 * @param node
	 * @return
	 */
	private List<MinimizeTreeNode> findLeaves(MinimizeTreeNode node) {
		List<MinimizeTreeNode> leaves = new ArrayList<MinimizeTreeNode>();
		for (int i = 0; i < node.getChildCount(); i++){
			MinimizeTreeNode child = node.getChildAt(i);
			if (isLeaf(child))
				leaves.add(child);
			else
				leaves.addAll(findLeaves(child));
		}
		return leaves;
	}

	public FiniteStateAcceptor getDFA() {
		return myDFA;
	}
}
