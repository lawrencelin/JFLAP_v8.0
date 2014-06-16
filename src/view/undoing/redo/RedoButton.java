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





package view.undoing.redo;


import java.awt.event.MouseEvent;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.KeyStroke;

import util.JFLAPConstants;
import util.view.ActionLinkedButton;
import view.undoing.UndoRelatedButton;

import model.undo.UndoKeeper;
import model.undo.UndoKeeperListener;

import errors.BooleanWrapper;






/**
 * Redo time.
 * 
 * @author Henry Qin
 */

public class RedoButton extends UndoRelatedButton implements UndoKeeperListener {
	
	public RedoButton(UndoKeeper k, boolean useIcon) {
		this(new RedoAction(k), useIcon);
	}
	
	public RedoButton (RedoAction action, boolean useIcon) {
		super(action, useIcon);
	}

	@Override
	public String getIconFilename() {
		return "/ICON/redo.jpg";
	}

}
