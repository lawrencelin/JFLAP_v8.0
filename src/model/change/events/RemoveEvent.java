package model.change.events;

import java.util.Arrays;
import java.util.Collection;

import debug.JFLAPDebug;

import model.formaldef.components.SetComponent;
import model.formaldef.components.SetSubComponent;
import model.grammar.Production;
import model.grammar.ProductionSet;

public class RemoveEvent<T extends SetSubComponent<T>> extends AdvancedUndoableEvent {

	public RemoveEvent(SetComponent<T> source, Collection<? extends T> c) {
		super(source, ITEM_REMOVED, c);
	}

	public RemoveEvent(SetComponent<T> source, T ... remove) {
		this (source, Arrays.asList(remove));
	}

	@Override
	public boolean undo() {
		return this.getSource().addAll(getToRemove());
	}

	@Override
	public boolean redo() {
		return this.getSource().removeAll(getToRemove());
	}

	@Override
	public String getName() {
		return "Remove from " + this.getSource().getDescriptionName();
	}

	@Override
	public SetComponent<T> getSource() {
		return (SetComponent<T>) super.getSource();
	}
	
	public Collection<? extends T> getToRemove(){
		return (Collection<? extends T>) this.getArg(0);
	}
	
}
