package model.change.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import model.formaldef.components.SetComponent;
import model.formaldef.components.SetSubComponent;

public class AddEvent<T extends SetSubComponent<T>> extends AdvancedUndoableEvent {

	public AddEvent(SetComponent<T> source, T ...c ){
		this(source, Arrays.asList(c));
	}
	
	public AddEvent(SetComponent<T> source, Collection<? extends T> c) {
		super(source, ITEM_ADDED, new ArrayList<T>(c));
	}

	@Override
	public boolean undo() {
		return this.getSource().removeAll(getToAdd());
	}

	@Override
	public boolean redo() {
		return this.getSource().addAll(getToAdd());
	}

	@Override
	public String getName() {
		return "Add to " + this.getSource().getDescriptionName();
	}

	@Override
	public SetComponent<T> getSource() {
		return (SetComponent<T>) super.getSource();
	}
	
	public Collection<? extends T> getToAdd(){
		return (Collection<? extends T>) this.getArg(0);
	}
	
}
