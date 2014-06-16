package view.automata.undoing;

import java.awt.event.ActionEvent;

import model.undo.UndoKeeper;
import view.automata.editing.AutomatonEditorPanel;
import view.undoing.redo.RedoAction;

public class AutomataRedoAction extends RedoAction {

	private AutomatonEditorPanel myPanel;

	public AutomataRedoAction(UndoKeeper keeper, AutomatonEditorPanel panel) {
		super(keeper);
		myPanel = panel;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		myPanel.stopAllEditing();
		super.actionPerformed(arg0);
	}

}
