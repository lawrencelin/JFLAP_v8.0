package view.grammar.parsing.derivation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

import debug.JFLAPDebug;
import model.algorithms.testinput.parse.Derivation;
import model.grammar.Production;
import model.symbols.Symbol;
import model.symbols.SymbolString;
import oldnewstuff.view.tree.DefaultNodeDrawer;
import oldnewstuff.view.tree.DefaultTreeDrawer;
import oldnewstuff.view.tree.TreeDrawer;
import oldnewstuff.view.tree.UnrestrictedTreeNode;
import universe.preferences.JFLAPPreferences;

public class DerivationTreePanel extends DerivationPanel {

	private static final Color INNER = new Color(100, 200, 120),
			LEAF = new Color(255, 255, 100),
			BRACKET = new Color(150, 150, 255), // purple
			BRACKET_OUT = BRACKET.darker().darker();
	// Minimum distance between levels of nodes
	private static final double LEVEL_SEPARATION = 50;
	private static final int MAX_DEPTH = Integer.MAX_VALUE;
	// Minimum separation desitance between sibling nodes
	private static final double SIBLING_SEPARATION = 100;
	// Minimum separation desitance between subtrees
	private static final double SUBTREE_SEPARATION = 200;
	// Size of the nodes
	private static final double MEAN_NODE_SIZE = DefaultNodeDrawer.NODE_RADIUS;
	private static final int CENTER_NODE_Y = 100;

	private boolean amInverted;
	private TreeDrawer treeDrawer;
	private DefaultNodeDrawer nodeDrawer;

	private UnrestrictedTreeNode[][][] top = null;
	private UnrestrictedTreeNode[][][] bottom = null;
	private double realWidth, realHeight, metaWidth = -1.0, metaHeight;
	private int myLevel = 0, group = 0, production = -1;

	private Map<UnrestrictedTreeNode, List<UnrestrictedTreeNode>> nodeToParentGroup;
	private Map<UnrestrictedTreeNode, Double> nodeToParentWeights;
	private Map<UnrestrictedTreeNode, Point2D> nodeToPoint;

	private UnrestrictedTreeNode root;
	private double xTopAdjustment;
	private double yTopAdjustment;
	private List<UnrestrictedTreeNode> prevList;

	private Derivation myAnswer;

	private boolean called;

	public DerivationTreePanel(TreeDrawer drawer, boolean inverted) {
		super((inverted ? "Inverted " : "") + "Derivation Tree");
		treeDrawer = drawer;
		nodeDrawer = new DefaultNodeDrawer();
		nodeToParentGroup = new HashMap<UnrestrictedTreeNode, List<UnrestrictedTreeNode>>();
		nodeToParentWeights = new HashMap<UnrestrictedTreeNode, Double>();
		amInverted = inverted;
	}

	public DerivationTreePanel(TreeModel tree, boolean inverted) {
		this(new DefaultTreeDrawer(tree), inverted);
	}

	public DerivationTreePanel(Derivation d, boolean inverted) {
		this(new DefaultTreeModel(new DefaultMutableTreeNode()), inverted);
		myAnswer = d;
		setAnswer(d);
		called = false;
	}

	/**
	 * Parse the derivation to initialize tree structure
	 */
	private void initTree() {
		List<UnrestrictedTreeNode> curNodes = new ArrayList<UnrestrictedTreeNode>();
		List<UnrestrictedTreeNode> childrenList = new ArrayList<UnrestrictedTreeNode>();
		List<UnrestrictedTreeNode> temp = new ArrayList<UnrestrictedTreeNode>();
		for (int i = 0; i < myAnswer.getProductionArray().length; i++) {
			Production currentProd = myAnswer.getProduction(i);
			int index = myAnswer.getSubstitution(i);
			// since it is restricted grammar, lhs should only have one symbol. right?
			Symbol lhs = currentProd.getLHS()[0];
			Symbol[] rhs = currentProd.getRHS();
			if (curNodes.isEmpty()) {
				root = new UnrestrictedTreeNode(lhs);
				curNodes.add(root);
			} 
			UnrestrictedTreeNode parent = curNodes.get(index);
			for (Symbol s : rhs) {
				UnrestrictedTreeNode node = new UnrestrictedTreeNode(s);
				parent.add(node);
				childrenList.add(node);
			}
			for (int j = 0; j < index; j++) {
				temp.add(curNodes.get(j));
			}
			// update current list of nodes
			temp.addAll(childrenList);
			temp.addAll(curNodes.subList(index+1, curNodes.size()));
			curNodes.clear();
			curNodes.addAll(temp);
			childrenList.clear();
			temp.clear();
		}
		
		// pre-set coordinates of the root: top center
		root.xCoord = realWidth / 2;
		root.yCoord = CENTER_NODE_Y;
	}

	/* (non-Javadoc)
	 * @see view.grammar.parsing.derivation.DerivationPanel#setDerivation(model.algorithms.testinput.parse.Derivation)
	 * Called when the "next" button is clicked.
	 */
	@Override
	public void setDerivation(Derivation d) {
		super.setDerivation(d);
		if (!called)
			called = true;
		else
		{
			next();
		}
	}

	@Override
	public void paintComponent(Graphics gr) {
		Graphics2D g = (Graphics2D) gr.create();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.white);
		Dimension d = getSize();
		g.fillRect(0, 0, d.width, d.height);
		g.setColor(Color.black);
		if (top != null) {
//			paintUnrestrictedTree(g);
			paintRestrictedTree(g, d);
		}
		g.dispose();
	}
	
	private void paintRestrictedTree(Graphics2D g, Dimension d) {
		realWidth = d.width;
		realHeight = d.height;
		initTree();
//		root = buildTestTree();
		positionTree(root);
		paintTest(g, root, new Point2D.Double(root.xCoord, root.yCoord));
	}

	public TreeNode nodeAtPoint(Point2D point) {
		return treeDrawer.nodeAtPoint(point, getSize());
	}

	private void setAnswer(Derivation answer) {

		if (answer == null) {
			top = null;
			return;
		}
		//initializeTree(answer);

		top = new UnrestrictedTreeNode[answer.length()+1][][];
		bottom = new UnrestrictedTreeNode[answer.length()+1][][];

		// Initialize the top of the top.
		top[0] = new UnrestrictedTreeNode[1][];
		top[0][0] = new UnrestrictedTreeNode[1];
		top[0][0][0] = new UnrestrictedTreeNode(answer.createResult(0));

		metaWidth = -1.0;

		List<UnrestrictedTreeNode> nodes = new ArrayList<UnrestrictedTreeNode>();
		nodes.add(top[0][0][0]);
		// Create the nodes.

		bridgeTo(nodes, 1);
		bottom[bottom.length - 1] = top[top.length - 1];

		// Assign the weights.
		boolean[] need = new boolean[top.length];
		for (int i = 0; i < need.length; i++)
			need[i] = true;
		boolean changed = true;
		for (int max = 0; changed && max < top.length * 2; max++) {
			changed = false;
			for (int i = 0; i < top.length - 1; i++)
				changed |= assignWeights(i, need);
		}
		myLevel = group = 0;
		return;
	}

	//	private void initializeTree(Derivation answer) {
	//		Derivation[] solutionSteps = answer.toStepArray();
	//		UnrestrictedTreeNode root = new UnrestrictedTreeNode(
	//				solutionSteps[0].createResult());
	//
	//		List<UnrestrictedTreeNode> prev = new ArrayList<UnrestrictedTreeNode>();
	//		prev.add(root);
	//		int height = 0;
	//
	//		for (int i = 1; i <= myAnswer.length(); i++) {
	//			Production prod = myAnswer.getProduction(i - 1);
	//			int sub = myAnswer.getSubstitution(i - 1);
	//			List<UnrestrictedTreeNode> current = new ArrayList<UnrestrictedTreeNode>(
	//					prev);
	//
	//			Symbol[] lhs = prod.getLHS();
	//			Symbol[] rhs = prod.getRHS();
	//			List<UnrestrictedTreeNode> parents = prev.subList(sub, sub
	//					+ lhs.length);
	//
	//			int maxLevel = 0;
	//			for (int j = 0; j < lhs.length; j++) {
	//				UnrestrictedTreeNode node = prev.get(sub + j);
	//				current.remove(sub);
	//				maxLevel = Math.max(maxLevel, node.highest);
	//			}
	//
	//			if (lhs.length > 1) {
	//				for (int j = 0; j < lhs.length; j++) {
	//					UnrestrictedTreeNode node = prev.get(sub + j);
	//					node.lowest = maxLevel;
	//				}
	//			}
	//
	//			for (int j = 0; j < rhs.length; j++) {
	//				UnrestrictedTreeNode node = new UnrestrictedTreeNode(rhs[j]);
	//				node.highest = node.lowest = maxLevel + 1;
	//				current.add(sub + j, node);
	//
	//				if (j == rhs.length - 1)
	//					nodeToParentGroup.put(node, parents);
	//			}
	//
	//			if (rhs.length == 0) {
	//				UnrestrictedTreeNode node = new UnrestrictedTreeNode();
	//				node.highest = node.lowest = maxLevel + 1;
	//
	//				nodeToParentGroup.put(node, parents);
	//				current.add(sub, node);
	//			}
	//			height = Math.max(height, maxLevel + 1);
	//			prev = current;
	//		}
	//		JFLAPDebug.print(height);
	//
	//	}

	private void bridgeTo(List<UnrestrictedTreeNode> prev, int level) {
		//		for (UnrestrictedTreeNode t : prev) {
		//			System.out.print(t.getText());
		//		}
		//		System.out.println();
		if (level > myAnswer.length())
			return;
		List<UnrestrictedTreeNode> current = new ArrayList<UnrestrictedTreeNode>();
		List<UnrestrictedTreeNode[]> bottomList = new ArrayList<UnrestrictedTreeNode[]>();
		List<UnrestrictedTreeNode[]> topList = new ArrayList<UnrestrictedTreeNode[]>();

		Production prod = myAnswer.getProduction(level - 1); // how are productions ordered?
		int sub = myAnswer.getSubstitution(level - 1);

		for (int i = 0; i < prev.size(); i++) {
			UnrestrictedTreeNode node = prev.get(i);
			SymbolString text = node.getText();

			if(text.isEmpty() && i <= sub)
				sub++;

			if (i == sub) { // if it is the correct, do the substitution
				Symbol[] lhs = prod.getLHS();
				Symbol[] rhs = prod.getRHS();
				List<UnrestrictedTreeNode> parents = prev.subList(i, i
						+ lhs.length);

				// int maxLevel = 0;
				//
				// for(int j=0; j<lhs.length; j++){
				// UnrestrictedTreeNode node = prev.get(i+j);
				// maxLevel = Math.max(maxLevel, node.highest);
				// }

				for (UnrestrictedTreeNode p : parents) {
					p.lowest = level - 1;
				}
				List<UnrestrictedTreeNode> tempTop = new ArrayList<UnrestrictedTreeNode>();

				for (int j = 0; j < rhs.length; j++) {
					node = new UnrestrictedTreeNode(rhs[j]);
					node.highest = node.lowest = level;
					tempTop.add(node);
					current.add(node);

					if (j == rhs.length - 1)
						nodeToParentGroup.put(node, parents);
				}

				if (rhs.length == 0) {
					node = new UnrestrictedTreeNode();
					node.highest = node.lowest = level;

					nodeToParentGroup.put(node, parents);
					tempTop.add(node);
					current.add(node);
				}
				topList.add(tempTop.toArray(new UnrestrictedTreeNode[0]));
				bottomList.add(parents.toArray(new UnrestrictedTreeNode[0]));
				i += lhs.length - 1;
			} 
			else {
				node.lowest = level;

				current.add(node);
				bottomList.add(new UnrestrictedTreeNode[] { node });
				topList.add(new UnrestrictedTreeNode[] { node });
			}
		}
		bottom[level - 1] = bottomList.toArray(new UnrestrictedTreeNode[0][]);
		top[level] = topList.toArray(new UnrestrictedTreeNode[0][]);

		bridgeTo(current, level + 1);
	}

	private UnrestrictedTreeNode[] levelNodes(int level) {
		List<UnrestrictedTreeNode> list = new ArrayList<UnrestrictedTreeNode>();
		if (top[level] != null) {
			for (int i = 0; i < top[level].length; i++)
				for (int j = 0; j < top[level][i].length; j++)
					list.add(top[level][i][j]);
		}
		return list.toArray(new UnrestrictedTreeNode[0]);
	}

	private boolean assignWeights(int level, boolean[] need) {
		if (!need[level])
			return false;

		need[level] = false;
		boolean changed = false;
		double total = 0.0;

		for (int i = 0; i < bottom[level].length; i++) {
			UnrestrictedTreeNode[] s = bottom[level][i];
			UnrestrictedTreeNode[] c = top[level + 1][i];
			double cSum = 0.0, sSum = 0.0;

			for (int j = 0; j < s.length; j++)
				sSum += s[j].weight;
			if (!ends(level, i)) {
				total += sSum;
				continue;
			}
			for (int j = 0; j < c.length; j++) {
				cSum += c[j].weight;
			}
			Double TOTAL = new Double(total + Math.max(sSum, cSum) / 2.0);
			for (int j = 0; j < c.length; j++) {
				nodeToParentWeights.put(c[j], TOTAL);
			}
			total += Math.max(sSum, cSum);

			if (cSum > sSum) {
				double ratio = cSum / sSum;

				for (int j = 0; j < s.length; j++)
					s[j].weight *= ratio;
				if (level != 0)
					need[level - 1] = true;
				changed = true;
			} else if (cSum < sSum) {
				double ratio = sSum / cSum;

				for (int j = 0; j < c.length; j++)
					c[j].weight *= ratio;
				if (level != 0)
					need[level + 1] = true;
				changed = true;
			}
		}
		return changed;
	}

	private boolean ends(int level, int group) {
		try {
			if (level == bottom.length - 1)
				return true; // Everything ends at last.
			return !Arrays.equals(bottom[level][group], top[level + 1][group]);
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Level " + level + ", group "
					+ group + " is out of range!");
		}
	}

	private void paintUnrestrictedTree(Graphics2D g) {
		Dimension d = getSize();
		realWidth = d.width;
		realHeight = d.height;
		if (metaWidth == -1.0)
			setMetaWidth();
		metaHeight = top.length;
		Point2D p = new Point2D.Double();
		nodeToPoint = new HashMap<UnrestrictedTreeNode, Point2D>();

		for (int l = 0; l <= myLevel; l++) {
			double total = 0.0;
			UnrestrictedTreeNode[][] GG = l < myLevel ? bottom[l] : top[l];

			for (int gr = 0; gr < GG.length && (myLevel != l || gr <= group); gr++) {
				UnrestrictedTreeNode[] G = GG[gr];

				if (l <= myLevel - 2 || (l == myLevel - 1 && gr <= group)) {
					// Want the node on the bottom level.
					for (int i = 0; i < G.length; i++) {
						if (l == G[i].lowest) {
							// This group is drawn on the bottom.
							// Draw the line.
							Point2D point = getPoint(G[i].lowest, total
									+ G[i].weight / 2.0, null);
							getPoint(G[i].highest, total + G[i].weight / 2.0, p);
							g.drawLine((int) point.getX(), (int) point.getY(),
									(int) p.getX(), (int) p.getY());
							// Make the mapping.
							nodeToPoint.put(G[i], point);
						}
						if (l == G[i].highest) {
							// This group is just starting.
							Point2D point = getPoint(G[i].highest, total
									+ G[i].weight / 2.0, null);
							Double D = (Double) nodeToParentWeights.get(G[i]);
							if (D != null) {
								double pweight = D.doubleValue();
								getPoint(l - 1, pweight, p);
								g.drawLine((int) point.getX(),
										(int) point.getY(), (int) p.getX(),
										(int) p.getY());
							}
							// Draw the brackets.
							List<UnrestrictedTreeNode> parent = nodeToParentGroup
									.get(G[i]);

							if (parent != null && parent.size() != 1) {
								Point2D alpha = (Point2D) nodeToPoint
										.get(parent.get(0));
								Point2D beta = (Point2D) nodeToPoint.get(parent
										.get(parent.size() - 1));
								g.setColor(BRACKET);
								int radius = (int) DefaultNodeDrawer.NODE_RADIUS;
								int ax = (int) (alpha.getX() - radius - 3);
								int ay = (int) (alpha.getY() - radius - 3);
								g.fillRoundRect(ax, ay, (int) (beta.getX()
										+ radius + 3)
										- ax, (int) (beta.getY() + radius + 3)
										- ay, 2 * radius + 6, 2 * radius + 6);
								g.setColor(BRACKET_OUT);
								g.drawRoundRect(ax, ay, (int) (beta.getX()
										+ radius + 3)
										- ax, (int) (beta.getY() + radius + 3)
										- ay, 2 * radius + 6, 2 * radius + 6);
								g.setColor(Color.black);
							}
							// Make the map.
							nodeToPoint.put(G[i], point);
						}
						total += G[i].weight;
					}
				} else if (l <= myLevel) {
					// We're going to get the top level.
					for (int i = 0; i < G.length; i++) {
						if (l == G[i].highest) {
							// This node is just starting too.
							Point2D point = getPoint(G[i].highest, total
									+ G[i].weight / 2.0, null);
							Double D = (Double) nodeToParentWeights.get(G[i]);
							if (D != null) {
								double pweight = D.doubleValue();
								getPoint(l - 1, pweight, p);
								g.drawLine((int) point.getX(),
										(int) point.getY(), (int) p.getX(),
										(int) p.getY());
							}
							// Draw the brackets.
							List<UnrestrictedTreeNode> parent = nodeToParentGroup
									.get(G[i]);
							if (parent != null && parent.size() != 1) {
								Point2D alpha = nodeToPoint.get(parent.get(0));
								Point2D beta = nodeToPoint.get(parent
										.get(parent.size() - 1));
								g.setColor(BRACKET);
								int radius = (int) DefaultNodeDrawer.NODE_RADIUS;
								int ax = (int) (alpha.getX() - radius - 3);
								int ay = (int) (alpha.getY() - radius - 3);
								g.fillRoundRect(ax, ay, (int) (beta.getX()
										+ radius + 3)
										- ax, (int) (beta.getY() + radius + 3)
										- ay, 2 * radius + 6, 2 * radius + 6);
								g.setColor(BRACKET_OUT);
								g.drawRoundRect(ax, ay, (int) (beta.getX()
										+ radius + 3)
										- ax, (int) (beta.getY() + radius + 3)
										- ay, 2 * radius + 6, 2 * radius + 6);
								g.setColor(Color.black);
							}
							// Make the map.
							nodeToPoint.put(G[i], point);
						}
						total += G[i].weight;
					}
				} else {
					System.err.println("Badness in the drawer!");
				}
			}
		}
		// Do the drawing of the nodes.
		for (UnrestrictedTreeNode n : nodeToPoint.keySet())
			paintNode(g, n, nodeToPoint.get(n));

	}

	private void setMetaWidth() {
		for (int i = 0; i < top.length; i++) {
			UnrestrictedTreeNode[] nodes = levelNodes(i);
			double total = 0.0;
			if (nodes != null) {
				for (int j = 0; j < nodes.length; j++)
					total += nodes[j].weight;
			}
			metaWidth = Math.max(total, metaWidth);
		}
	}

	private Point2D getPoint(int row, double weight, Point2D p) {
		if (p == null)
			p = new Point2D.Double();
		p.setLocation(realWidth * weight / metaWidth, realHeight
				* ((double) row + 0.5) / metaHeight);
		return p;
	}

	private void paintNode(Graphics2D g, UnrestrictedTreeNode node, Point2D p) {

		g.setColor(node.lowest == top.length - 1 ? LEAF : INNER);
		g.translate(p.getX(), p.getY());

		nodeDrawer.draw(g, node);
		g.translate(-p.getX(), -p.getY());
	}

	/*
	 * This method is called every time the "step" button is clicked. 
	 */
	private boolean next() { 
		Production p = null, ps[] = myAnswer.getSubDerivation(myLevel)
				.getProductionArray();
		production++;
		if (production >= ps.length) {
			production = 0;
			p = myAnswer.getSubDerivation(myLevel + 1).getProduction(0);
		} else {
			p = ps[production];
		}

		do {
			group++;
			if (group >= top[myLevel].length) {
				group = 0;
				myLevel++;
			}
			if (myLevel >= top.length) {
				myLevel = top.length - 1;
				group = top[myLevel].length - 1;
				break;
			}
			if (myLevel == top.length - 1 && group == top[myLevel].length - 1)
				break;
		} while (!begins(myLevel, group));

		String lhs = p.getRHS().toString();
		if (lhs.length() == 0)
			lhs = JFLAPPreferences.getEmptyString();

		if (myLevel == top.length - 1
				&& production == myAnswer.getSubDerivation(myLevel).length() - 1) {
			return true;
		}

		return false;
	}

	private boolean begins(int level, int group) {
		if (level == 0)
			return true; // Everything starts at beginning.
		return ends(level - 1, group);
	}
	
	/**
	 * This method, by calling other methods, determines the coordinates
	 * of each node. 
	 * 
	 * @param n
	 * @return
	 */
	private boolean positionTree(UnrestrictedTreeNode n) {
		if (n!=null) {
			initPrevNodeList();
			firstWalk(n, 0);
			xTopAdjustment = n.xCoord - n.prelimX; 
			yTopAdjustment = n.yCoord; 
//			xTopAdjustment = 0;
//			yTopAdjustment = 0;
			return secondWalk(n, 0 , 0);
		} 
		return true;
	}
	
	/**
	 * Initialize the list which contains a list of previous nodes
	 * at each level. 
	 */
	private void initPrevNodeList() {
		prevList = new LinkedList<UnrestrictedTreeNode>();
	}
	
	private UnrestrictedTreeNode getPrevNodeAtLevel(int level) {
		if (level > prevList.size()-1) {
			return null;
		}
		return prevList.get(level);
	}
	
	private void setPrevNodeAtLevel(int level, UnrestrictedTreeNode node) {
		if (level > prevList.size()-1) {
			prevList.add(node);
			return;
		}
		prevList.set(level, node);
	}
	
	/**
	 * Post-order traversal to determine the preliminary x coordinate and the 
	 * modifier value of each node. The values determined will be used in 
	 * the second walk to finalize position of the nodes. 
	 * 
	 * @param node
	 * @param level
	 */
	private void firstWalk(UnrestrictedTreeNode node, int level) {
		node.leftNeighbor = getPrevNodeAtLevel(level);
		setPrevNodeAtLevel(level, node);
		node.modifier = 0;
		if (node.isLeaf() || level == MAX_DEPTH) {
			if (node.getPreviousSibling() != null) {
				node.prelimX = ((UnrestrictedTreeNode) node.getPreviousSibling()).prelimX +
						SIBLING_SEPARATION + MEAN_NODE_SIZE;
			} else {
				node.prelimX = 0;
			}
		} else {
			UnrestrictedTreeNode leftMost = (UnrestrictedTreeNode) node.getFirstChild();
			UnrestrictedTreeNode rightMost = leftMost;
			firstWalk(leftMost, level+1);
			while (rightMost.getNextSibling() != null) {
				rightMost = (UnrestrictedTreeNode) rightMost.getNextSibling();
				firstWalk(rightMost, level+1);
			}
			double midPoint = (leftMost.prelimX + rightMost.prelimX) /2;
			if (node.getPreviousSibling() != null) {
				node.prelimX = ((UnrestrictedTreeNode) node.getPreviousSibling()).prelimX +
						SIBLING_SEPARATION + MEAN_NODE_SIZE;
				node.modifier = node.prelimX - midPoint;
				apportion(node, level);
			} else {
				node.prelimX = midPoint;
			}
		}
	}
	
	/**
	 * Pre-order traversal to finalize the coordinates of the nodes. 
	 * @param node
	 * @param level
	 * @param modSum
	 * @return
	 */
	private boolean secondWalk(UnrestrictedTreeNode node, int level, double modSum) {
		boolean result = true;
		if (level <= MAX_DEPTH) {
			double xTemp = xTopAdjustment + node.prelimX + modSum;
			double yTemp = yTopAdjustment + (level * LEVEL_SEPARATION);
			if (checkValidPosition(xTemp, yTemp)) {
				node.xCoord = xTemp;
				node.yCoord = yTemp;
				if (!node.isLeaf()) {
					result = secondWalk((UnrestrictedTreeNode)node.getFirstChild(), 
							level+1, 
							modSum+node.modifier);
				}
				if (result == true && node.getNextSibling()!=null) {
					result = secondWalk((UnrestrictedTreeNode)node.getNextSibling(), level, modSum);
				}
			} else {
				result = false;
			}
		}
		return result;
	}
	
	/**
	 * Adjust the position of the subtree.
	 * 
	 * @param node
	 * @param level
	 */
	private void apportion(UnrestrictedTreeNode node, int level) {
		UnrestrictedTreeNode leftMost = (UnrestrictedTreeNode) node.getFirstChild();
		UnrestrictedTreeNode neighbor = leftMost.leftNeighbor;
		int compareDepth = 1;
		int depthToStop = MAX_DEPTH - level;
		while (leftMost != null && 
				neighbor != null && 
				compareDepth <= depthToStop) {
			neighbor = leftMost.leftNeighbor;
			double leftModSum = 0;
			double rightModSum = 0;
			UnrestrictedTreeNode ancestorLeftMost = leftMost;
			UnrestrictedTreeNode ancestorNeighbor = neighbor;
			for (int i = 0; i < compareDepth; i++) {
				ancestorLeftMost = (UnrestrictedTreeNode) ancestorLeftMost.getParent();
				ancestorNeighbor = (UnrestrictedTreeNode) ancestorNeighbor.getParent();
				rightModSum += ancestorLeftMost.modifier;
				leftModSum += ancestorNeighbor.modifier;
			}
			
			double moveDistance = (neighbor.prelimX + leftModSum + SUBTREE_SEPARATION +
					MEAN_NODE_SIZE) - (leftMost.prelimX + rightModSum);
			
			if (moveDistance > 0) {
				UnrestrictedTreeNode temp = node;
				int leftSiblings = 0;
				while (temp != null && !temp.equals(ancestorNeighbor)) {
					leftSiblings++;
					temp = (UnrestrictedTreeNode) temp.getPreviousSibling();
				}
				if (temp != null) {
					double portion = moveDistance / leftSiblings;
					temp = node;
					while (!temp.equals(ancestorNeighbor)) {
						temp.prelimX+=moveDistance;
						temp.modifier+=moveDistance;
						moveDistance-=portion;
						temp = (UnrestrictedTreeNode) temp.getPreviousSibling();
					}
				} else {
					return;
				}
			}
			
			compareDepth++;
			if (leftMost.isLeaf()) {
				leftMost = getLeftMost(node, 0 ,compareDepth);
			} else {
				leftMost = (UnrestrictedTreeNode) leftMost.getFirstChild();
			}
		}
	}
	
	/**
	 * Check whether the coordinates are within the bounds of the canvas. 
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean checkValidPosition(double x, double y) {
//		return (x>=0 && 
//				x<=this.realWidth && 
//				y>=0 && y<=this.realHeight);
		return true;
	}
	
	/**
	 * Return the left-most descendant of a node at a given depth, implemented
	 * using a post-order traversal. 
	 * The level here is not the absolute level; it means the level below the node
	 * whose descendant is to be found. 
	 * @param node
	 * @param level
	 * @param depth
	 * @return
	 */
	private UnrestrictedTreeNode getLeftMost(UnrestrictedTreeNode node, int level, int depth) {
		if (level>=depth) return node;
		if (node.isLeaf()) return null;
		UnrestrictedTreeNode rightMost = (UnrestrictedTreeNode) node.getFirstChild();
		UnrestrictedTreeNode leftMost = getLeftMost(rightMost, level+1, depth);
		while (leftMost == null && rightMost.getNextSibling() != null) {
			rightMost = (UnrestrictedTreeNode) rightMost.getNextSibling();
			leftMost = getLeftMost(rightMost, level+1, depth);
		}
		return leftMost;
	}
	
	/**
	 * Draw out the tree on canvas. 
	 * @param g
	 * @param node
	 * @param p
	 */
	private void paintTest (Graphics2D g, UnrestrictedTreeNode node, Point2D p) {
		if (node == null) {
			return;
		}
		g.setColor(INNER);
		g.translate(p.getX(), p.getY());
		nodeDrawer.draw(g, node);
		g.translate(-p.getX(), -p.getY());
		nodeDrawer.draw(g, node);
		for (int i = 0; i < node.getChildCount(); i++) {
			UnrestrictedTreeNode temp = (UnrestrictedTreeNode) node.getChildAt(i);
			g.setColor(Color.black);
			g.drawLine((int)node.xCoord, (int)node.yCoord, (int)temp.xCoord, (int)temp.yCoord);
			paintTest(g, temp, new Point2D.Double(temp.xCoord, temp.yCoord));
		}
		
	}
	
	private UnrestrictedTreeNode buildTestTree() {
		UnrestrictedTreeNode root = new UnrestrictedTreeNode(new Symbol("O"));
		UnrestrictedTreeNode A = new UnrestrictedTreeNode(new Symbol("A"));
		UnrestrictedTreeNode B = new UnrestrictedTreeNode(new Symbol("B"));
		UnrestrictedTreeNode C = new UnrestrictedTreeNode(new Symbol("C"));
		UnrestrictedTreeNode D = new UnrestrictedTreeNode(new Symbol("D"));
		UnrestrictedTreeNode E = new UnrestrictedTreeNode(new Symbol("E"));
		UnrestrictedTreeNode F = new UnrestrictedTreeNode(new Symbol("F"));
		UnrestrictedTreeNode G = new UnrestrictedTreeNode(new Symbol("G"));
		UnrestrictedTreeNode H = new UnrestrictedTreeNode(new Symbol("H"));
		UnrestrictedTreeNode I = new UnrestrictedTreeNode(new Symbol("I"));
		UnrestrictedTreeNode J = new UnrestrictedTreeNode(new Symbol("J"));
		UnrestrictedTreeNode K = new UnrestrictedTreeNode(new Symbol("K"));
		UnrestrictedTreeNode L = new UnrestrictedTreeNode(new Symbol("L"));
		UnrestrictedTreeNode M = new UnrestrictedTreeNode(new Symbol("M"));
		UnrestrictedTreeNode N = new UnrestrictedTreeNode(new Symbol("N"));
		root.add(E);
		root.add(F);
		root.add(N);
		E.add(A);
		E.add(D);
		D.add(B);
		D.add(C);
		N.add(G);
		N.add(M);
		M.add(H);
		M.add(I);
		M.add(J);
		M.add(K);
		M.add(L);
		root.xCoord = realWidth/2;
		root.yCoord = 200;
		return root;
	}
}
