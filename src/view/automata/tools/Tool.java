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





package view.automata.tools;


import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import util.JFLAPConstants;

import debug.JFLAPDebug;



/**
 * The <CODE>Tool</CODE> abstract class is a type of input adapter for the
 * pane used to edit the view, and the automaton. The tool also has the ability
 * to draw on the view.
 */

public abstract class Tool extends MouseAdapter {

	/**
	 * Returns the tool tip for this tool, modified to have the tool tip
	 * shortcut highlighted.
	 * 
	 * @return the string from <CODE>getToolTip</CODE> slightly modified
	 */
	public String getShortcutToolTip() {
		String tip = getToolTip();
		int index = findDominant(tip, this.getActivatingKey());
		if (index == -1)
			return tip + "(" + Character.toUpperCase(this.getActivatingKey()) + ")";
		return tip.substring(0, index) + "(" + tip.substring(index, index + 1)
				+ ")" + tip.substring(index + 1, tip.length());
	}

	/**
	 * Returns the tool tip for this tool.
	 * 
	 * @return a string containing the tool tip
	 */
	public abstract String getToolTip();


	/**
	 * Returns the tool icon.
	 * 
	 * @return the default tool icon
	 */
	public Image getImage() {
		URL url = getClass().getResource(getImageURLString());
		return  Toolkit.getDefaultToolkit().getImage(url);
	}
	
	public Icon getIcon(){
		return new ImageIcon(getImage());
	}

	/**
	 * Returns the key stroke that will activate this tool.
	 * 
	 * @return the key stroke that will activate this tool, or <CODE>null</CODE>
	 *         if there is no shortcut keystroke for this tool
	 */
	public abstract char getActivatingKey();

	
	/**
	 * This automatically finds the index of a character in the string for which
	 * then given character is at its most prominant. The intended use is to
	 * automatically, given a tooltip and a key shortcut, find the key in the
	 * string that should be highlighted as the shortcut for that particular
	 * tool.
	 * 
	 * @param string
	 *            the string to search for a character
	 * @param c
	 *            the character to search for in the string
	 * @return the index of the character c "at its best", or -1 if the
	 *         indicated character is not in the string
	 */
	private int findDominant(String string, char c) {
		int index = string.indexOf(Character.toUpperCase(c));
		if (index != -1)
			return index;
		return string.indexOf(Character.toLowerCase(c));
	}

	public Cursor getCursor() {
		return new Cursor(Cursor.DEFAULT_CURSOR);
	}

	public  Point getCenterOfIcon(){
		Image i = this.getImage();
		return new Point(i.getWidth(null)/2, i.getHeight(null)/2);
	}

	public abstract String getImageURLString();



	public void draw(Graphics g) {
		
	}
}
