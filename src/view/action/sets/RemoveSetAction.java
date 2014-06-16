package view.action.sets;

import java.awt.event.ActionEvent;

import model.sets.AbstractSet;
import model.sets.SetsManager;
import model.undo.IUndoRedo;
import model.undo.UndoKeeper;
import view.action.UndoingAction;

public class RemoveSetAction extends UndoingAction implements IUndoRedo {
	
	private AbstractSet mySet;
	
	public RemoveSetAction (UndoKeeper keeper, AbstractSet set) {
		super("Delete " + set.getName(), keeper);
		mySet = set;
	}

	@Override
	public IUndoRedo createEvent(ActionEvent e) {
		SetsManager.ACTIVE_REGISTRY.remove(mySet);
		
		return this;
	}

	@Override
	public boolean undo() {
		SetsManager.ACTIVE_REGISTRY.add(mySet);
		return true;
	}

	@Override
	public boolean redo() {
		SetsManager.ACTIVE_REGISTRY.remove(mySet);
		return true;
	}

	@Override
	public String getName() {
		return (String) super.getValue(NAME);
	}


}
