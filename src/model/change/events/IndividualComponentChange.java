package model.change.events;

import model.automata.StartState;
import model.automata.State;
import model.formaldef.Describable;
import model.formaldef.components.FormalDefinitionComponent;

public abstract class IndividualComponentChange<T extends FormalDefinitionComponent, S> extends AdvancedUndoableEvent {

	public IndividualComponentChange(T source, S from, S to) {
		super(source, SPECIAL_CHANGED, from, to);
	}

	@Override
	public String getName() {
		return "Set " + getSource().getDescriptionName(); 
	}
	
	@Override
	public T getSource(){
		return (T) super.getSource();
	}
	
	public S getFrom(){
		return (S) this.getArg(0);
	}
	
	public S getTo(){
		return (S) this.getArg(1);
	}

}
