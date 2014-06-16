package view.undoing.undo;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import debug.JFLAPDebug;

import util.JFLAPConstants;
import view.undoing.UndoRelatedAction;


import model.undo.UndoKeeper;
import model.undo.UndoKeeperListener;

public class UndoAction extends UndoRelatedAction {


	public UndoAction(UndoKeeper keeper) {
		super("Undo", keeper);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Z,
				JFLAPConstants.MAIN_MENU_MASK));
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		getKeeper().undoLast();
	}

	@Override
	public boolean checkKeeper() {
		return getKeeper().canUndo();
	}	
	
	

}
