package model.automata;

import java.awt.Point;
import java.util.Collection;

import errors.BooleanWrapper;
import model.change.events.StartStateSetEvent;
import model.formaldef.components.FormalDefinitionComponent;

public class StartState extends FormalDefinitionComponent {

	private State myState;

	public StartState(String name, int id) {
		this(new State(name, id));
	}

	
	
	public StartState() {
		this(null);
	}

	public StartState(State state) {
		this.setState(state);
	}



	@Override
	public String getDescription() {
		return "The Start State of the automaton.";
	}

	@Override
	public Character getCharacterAbbr() {
		return 'S';
	}

	@Override
	public BooleanWrapper isComplete() {
		return new BooleanWrapper(myState != null,
									"The Automaton requires a Start State.");
	}

	
	@Override
	public String getDescriptionName() {
		return "Start State";
	}
	
	
	public boolean setState(State start){
		StartStateSetEvent e = new StartStateSetEvent(this, myState, start);
		myState = start;
		this.distributeChange(e);
		return true;
	}
	
	
	public void clear(){
		this.setState(null);
	}
	
	@Override
	public String toString() {
		return getDescriptionName() +": " + (this.isComplete().isTrue() ? myState.getName() : "");
	}



	@Override
	public StartState copy() {
		return new StartState(myState.copy());
	}


	public State getState() {
		return myState;
	}



	public void checkAndRemove(Collection<State> removed) {
		if (this.getState() == null) return;
		for (State s: removed){
			if (this.getState().equals(s)){
				this.clear();
				return;
			}
		}
	}
}
