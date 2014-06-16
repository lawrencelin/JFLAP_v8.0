package model.change.events;

import model.undo.IUndoRedo;

public abstract class AdvancedUndoableEvent extends AdvancedChangeEvent implements IUndoRedo {

	public AdvancedUndoableEvent(Object source, int type, Object ... args) {
		super(source, type, args);
	}

}
