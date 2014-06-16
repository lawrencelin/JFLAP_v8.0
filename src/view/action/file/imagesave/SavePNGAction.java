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

import view.action.EnvironmentAction;
import view.environment.JFLAPEnvironment;

/**
 * The <CODE>SaveGraphPNGAction</CODE> is an action to save the current view in window
 * to a PNG image file always using a dialog box.
 * 
 * @author Jonathan Su, Ian McMahon
 */

public class SavePNGAction extends EnvironmentAction {
	
	public SavePNGAction(JFLAPEnvironment e) {
		super("Save as PNG", e);
	}

	@Override
	public void actionPerformed(ActionEvent e, JFLAPEnvironment env) {
		Component view = env.getCurrentView();
		SaveImageUtility.saveImage(view, "PNG files", "png");
	}
}
