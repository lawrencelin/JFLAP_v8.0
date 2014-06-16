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


import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import oldnewstuff.controller.menus.MenuConstants;
import view.environment.JFLAPEnvironment;
import view.environment.TabChangeListener;
import view.environment.TabChangedEvent;



/**
 * The <CODE>CloseAction</CODE> is an action for removing tabs in an
 * environment. It automatically detects changes in the activation of panes in
 * the environment, and changes its enabledness whether or not a pane in the
 * environment is permanent (i.e. should not be closed).
 * 
 * @author Thomas Finley
 */

public class CloseTabAction extends AbstractAction implements TabChangeListener{
	
	public static final String SET_ENABLED = "enability";
	
	private JFLAPEnvironment myEnvironment;

	/**
	 * Instantiates a <CODE>CloseAction</CODE>.
	 * @param usingIcon 
	 * 
	 * @param environment
	 *            the environment to handle the closing for
	 */
	public CloseTabAction(JFLAPEnvironment e, boolean usingIcon) {
		super("Dismiss Tab");
		setIcon(usingIcon);
		putValue(ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, MenuConstants.getMainMenuMask()));
		myEnvironment = e;
		e.addTabListener(this);
	}


	private void setIcon(boolean usingIcon) {
		if(!usingIcon) return;
		URL url = getClass().getResource("/ICON/x.gif");
		Image img = Toolkit.getDefaultToolkit().getImage(url);
        putValue(Action.SMALL_ICON, new ImageIcon(img));
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		myEnvironment.closeActiveTab();
	}
	
	@Override
	public boolean isEnabled() {
		return myEnvironment.getTabCount() > 1;
	}


	@Override
	public void tabChanged(TabChangedEvent e) {
		firePropertyChange(SET_ENABLED, null, isEnabled());
	}

}
