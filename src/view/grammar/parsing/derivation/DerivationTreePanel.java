package view.grammar.parsing.derivation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
		if (top != null)
			paintTree(g);
		g.dispose();
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

	private void paintTree(Graphics2D g) {
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
	
}
