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





package oldnewstuff.view.tree;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.JComponent;

/**
 * The <CODE>TreePanel</CODE> is a graphical component that draws a tree using
 * a <CODE>TreeDrawer</CODE> object.
 * 
 * @see javax.swing.tree.TreeModel
 * 
 * @author Thomas Finley
 */

public class TreePanel extends JComponent {

	private TreeDrawer treeDrawer;
	
	public TreePanel(TreeModel tree) {
		treeDrawer = new DefaultTreeDrawer(tree);
	}

	public TreePanel(TreeDrawer drawer) {
		treeDrawer = drawer;
	}

	public TreeDrawer getTreeDrawer() {
		return treeDrawer;
	}

	public void setTreeDrawer(TreeDrawer drawer) {
		treeDrawer = drawer;
		repaint();
	}

	public void paintComponent(Graphics gr) {
		Graphics2D g = (Graphics2D) gr;
		super.paintComponent(g);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.white);
		Dimension d = getSize();
		g.fillRect(0, 0, d.width, d.height);
		g.setColor(Color.black);
		treeDrawer.draw((Graphics2D) g, d);
	}
	
	public TreeNode nodeAtPoint(Point2D point) {
		return treeDrawer.nodeAtPoint(point, getSize());
	}
}
