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

package view.action.file.imagesave;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JFileChooser;

import debug.JFLAPDebug;

import view.action.EnvironmentAction;
import view.environment.JFLAPEnvironment;
/**
 * The <CODE>SaveGraphJPGAction</CODE> is an action to save the current view in window
 * to a JPG image file always using a dialog box.
 * 
 * @author Jonathan Su, Henry Qin, Ian McMahon
 */

public class SaveJPGAction extends EnvironmentAction {
	/**
	 * Instantiates a new <CODE>SaveGraphBMPAction</CODE>.
	 * 
	 * @param environment
	 *            the environment that holds the action
	 * @param menu
	 *            the JMenu where the action is contained
	 */
	public SaveJPGAction(JFLAPEnvironment e) {
		super("Save as JPG", e);
	}

	@Override
	public void actionPerformed(ActionEvent e, JFLAPEnvironment env) {
		Component view = env.getCurrentView();
		SaveImageUtility.saveImage(view,"JPEG files", "jpg", "jpeg");
	}
}
