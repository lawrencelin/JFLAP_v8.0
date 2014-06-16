package view.automata.undoing;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import debug.JFLAPDebug;
import model.undo.UndoKeeper;
import util.JFLAPConstants;
import view.automata.editing.AutomatonEditorPanel;
import view.undoing.undo.UndoAction;

public class AutomataUndoAction extends UndoAction{

	private AutomatonEditorPanel myPanel;

	public AutomataUndoAction(UndoKeeper keeper, AutomatonEditorPanel panel) {
		super(keeper);
		myPanel = panel;
	}
	
	public void actionPerformed(ActionEvent arg0) {
		myPanel.stopAllEditing();
		super.actionPerformed(arg0);
	}

}
