package model.change.events;

import javax.swing.event.ChangeEvent;

import model.undo.IUndoRedo;

public abstract class UndoableEvent extends ChangeEvent implements IUndoRedo {

	public UndoableEvent(Object source) {
		super(source);
	}
	


}
