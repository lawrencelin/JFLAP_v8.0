package model.change.events;

import model.formaldef.components.SetComponent;
import model.formaldef.components.SetSubComponent;

public class ModifyEvent<T extends SetSubComponent<T>> extends AdvancedUndoableEvent{

	private SetToEvent<T> myEvent;

	public ModifyEvent(Object source, SetToEvent<T> event) {
		super(source, event.getType());
		myEvent = event;
	}

	@Override
	public boolean undo() {
		return myEvent.undo();
	}

	@Override
	public boolean redo() {
		return myEvent.redo();
	}
	
	public Object getEventSource() {
		return myEvent.getSource();
	}

	@Override
	public Object getArg(int i) {
		return myEvent.getArg(i);
	}

	@Override
	public String getName() {
		return myEvent.getName();
	}

}