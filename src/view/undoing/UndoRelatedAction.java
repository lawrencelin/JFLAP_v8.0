package view.undoing;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import model.undo.UndoKeeper;

public abstract class UndoRelatedAction extends AbstractAction {

	private UndoKeeper myKeeper;

	public UndoRelatedAction(String name, UndoKeeper keeper) {
		super(name);
		myKeeper = keeper;
	}
	
	public UndoKeeper getKeeper() {
		return myKeeper;
	}
	
	@Override
	public boolean isEnabled() {
		return myKeeper != null && checkKeeper();
	}

	public abstract boolean checkKeeper();

}
