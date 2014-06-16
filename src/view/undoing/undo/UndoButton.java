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





package view.undoing.undo;


import model.undo.UndoKeeper;
import model.undo.UndoKeeperListener;
import view.undoing.UndoRelatedAction;
import view.undoing.UndoRelatedButton;



/**
 * First, let's make it work, then we'll make the interface so you don't have to click undo and then click randomly.
 * 
 * @author Henry Qin
 */

public class UndoButton extends UndoRelatedButton implements UndoKeeperListener{

	public UndoButton(UndoKeeper k, boolean useIcon) {
		this(new UndoAction(k), useIcon);
	}
	
	public UndoButton(UndoRelatedAction action, boolean useIcon) {
		super(action, useIcon);
	}

	@Override
	public String getIconFilename() {
		return "/ICON/undo2.jpg";
	}



}
