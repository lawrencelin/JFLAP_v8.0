package view.undoing;

import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;

import debug.JFLAPDebug;

import model.undo.UndoKeeper;
import model.undo.UndoKeeperListener;

public class UndoRelatedMenuItem extends JMenuItem implements UndoKeeperListener {

	public UndoRelatedMenuItem(UndoRelatedAction a) {
		super(a);
		a.getKeeper().addUndoListener(this);
		keeperStateChanged();
	}

	@Override
	public void keeperStateChanged() {
		this.setEnabled(this.getAction().isEnabled());
	}

}
