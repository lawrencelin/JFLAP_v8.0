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


import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;





/**
 * This action will display a small about box that lists the tool version
 * number, and other version.
 * 
 * @author Thomas Finley
 */

public class AboutAction extends AbstractAction {
	/**
	 * Instantiates a new <CODE>AboutAction</CODE>.
	 */
	public AboutAction() {
		super("About...");
		this.setEnabled(false);
	}

	/**
	 * Shows the about box.
	 */
	public void actionPerformed(ActionEvent e) {
		BOX.displayBox();
	}

	private static final AboutBox BOX = new AboutBox();
}
