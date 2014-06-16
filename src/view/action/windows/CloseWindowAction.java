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

package view.action.windows;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import debug.JFLAPDebug;

import universe.JFLAPUniverse;
import util.JFLAPConstants;
import view.environment.JFLAPEnvironment;

/**
 * The <CODE>CloseWindowAction</CODE> invokes the close method on the
 * <CODE>EnvironmentFrame</CODE> to which they belong.
 * 
 * @author Thomas Finley
 */

public class CloseWindowAction extends AbstractAction {
	private JFLAPEnvironment myEnvironment;

	/**
	 * Instantiates a <CODE>CloseWindowAction</CODE>.
	 * 
	 * @param frame
	 *            the <CODE>EnvironmentFrame</CODE> to dismiss when an action is
	 *            registered
	 */
	public CloseWindowAction(JFLAPEnvironment e) {
		super("Close", null);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_W,
				JFLAPConstants.MAIN_MENU_MASK));
		myEnvironment = e;
	}

	/**
	 * Handles the closing of the window.
	 */
	public void actionPerformed(ActionEvent event) {
		if (myEnvironment.close(true))
			JFLAPUniverse.deregisterEnvironment(myEnvironment);
	}
}
