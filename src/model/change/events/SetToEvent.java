package model.change.events;

import debug.JFLAPDebug;
import model.formaldef.components.Settable;
import model.symbols.SpecialSymbol;
import model.symbols.Symbol;

public class SetToEvent<T extends Settable<T>> extends AdvancedUndoableEvent {

	private T myBase;

	public SetToEvent(T source, T from, T to) {
		super(source, ITEM_MODIFIED, from, to);
		myBase = source;
	}

	@Override
	public boolean undo() {
		return myBase.setTo(getFrom());
	}
	
	@Override
	public boolean redo() {
		return myBase.setTo(getTo());
	}
	
	@Override
	public T getSource() {
		return (T) super.getSource();
	}
	
	public T getFrom(){
		return (T) getArg(0);
	}
	
	public T getTo(){
		return (T) getArg(1);
	}

	@Override
	public String getName() {
		return "Set " + getFrom() + " to " + getTo();
	}

}
