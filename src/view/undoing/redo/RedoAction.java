package view.undoing.redo;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import universe.preferences.JFLAPPreferences;
import util.JFLAPConstants;
import view.undoing.UndoRelatedAction;


import model.undo.UndoKeeper;
import model.undo.UndoKeeperListener;

public class RedoAction extends UndoRelatedAction{


	public RedoAction(UndoKeeper keeper) {
		super("Redo", keeper);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Y,
				JFLAPConstants.MAIN_MENU_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		getKeeper().redoLast();
	}

	@Override
	public boolean checkKeeper() {
		return getKeeper().canRedo();
	}	


}
