package oldnewstuff.view;

import java.util.Map;

import model.undo.UndoKeeper;

import view.EditingPanel;


public abstract class PrimaryEditingPane extends EditingPanel {

	public PrimaryEditingPane(boolean editable) {
		super(new UndoKeeper(), editable);
	}
	
}
