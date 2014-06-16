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





package view.help;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import util.JFLAPConstants;



import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.WeakHashMap;


/**
 * The <CODE>HelpAction</CODE> is an abstract action that is meant to bring up
 * a help page appropriate to whatever context. It also serves as a general sort
 * of database to relate types of objects to a particular URL to bring up in the
 * help web frame for the various subclasses that will implement this action.
 * The idea is that an object can store its help page in the <CODE>HelpAction</CODE>
 * via <CODE>setURL</CODE>
 * 
 * @see oldview.help.WebFrame
 * 
 * @author Thomas Finley
 */

public class HelpAction extends AbstractAction {
	
	
	private Object myTarget;

	public HelpAction(){
		this(null);
	}
	
	
	/**
	 * Instantiates a new <CODE>HelpAction</CODE>.
	 */
	public HelpAction(Object target) {
		super("Help...", null);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_SLASH,
				JFLAPConstants.MAIN_MENU_MASK));
		myTarget = target;
	}
	
	/**
	 * Displays help for this object. If there is no particular help for this
	 * object available according to <CODE>getURL</CODE>, then the URL
	 * indicated by <CODE>DEFAULT_HELP</CODE> will be displayed in a <CODE>WebFrame</CODE>.
	 * 
	 * @param object
	 *            the object to display help for
	 * @see #getURL(Object)
	 * @see oldview.help.WebFrame
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String url = HelpPageRegistry.getPageURL(myTarget);
		FRAME.gotoURL(url);
		FRAME.setVisible(true);
	}
	
	/** The web frame. */
	private static final WebFrame FRAME = new WebFrame("/DOCS/index.html");

}
