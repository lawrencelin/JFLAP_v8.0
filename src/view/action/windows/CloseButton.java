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

import java.awt.Dimension;
import util.view.ActionLinkedButton;
import view.environment.JFLAPEnvironment;







/**
 * The <code>CloseButton</code> is a button for removing tabs in
 * an environment. It automatically detects changes in the activation 
 * of panes in the environment, and changes its enabledness whether 
 * a pane in the environment is permanent (i.e. should not be closed).
 * 
 * @see CloseTabAction
 * @author Jinghui Lim
 *
 */
public class CloseButton extends ActionLinkedButton 
{
    
	public CloseButton(JFLAPEnvironment e) {
		super(new CloseTabAction(e, true));
        this.setText("");
        setPreferredSize(new Dimension(22, 22));
	}
	

}
