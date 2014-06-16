package model.automata;

import java.util.Arrays;
import java.util.Iterator;
import java.util.TreeSet;

import debug.JFLAPDebug;

import universe.preferences.JFLAPPreferences;


import errors.BooleanWrapper;

import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.SetComponent;

public class StateSet extends SetComponent<State> {

	@Override
	public String getDescriptionName() {
		return "Internal States";
	}

	@Override
	public String getDescription() {
		return "The set of internal states.";
	}

	@Override
	public Character getCharacterAbbr() {
		return 'Q';
	}

	@Override
	public BooleanWrapper isComplete() {
		return new BooleanWrapper(true);
	}

	public int getNextUnusedID() {
		
		int i = 0;
		
		while (this.getStateWithID(i) != null){
			i++;
		}
		return i;
	}

	public State getStateWithID(int id) {
		for (State s: this){
			if (s.getID() == id)
				return s;
		}
		
		return null;
	}

	public State createAndAddState() {
		int id = this.getNextUnusedID();
		State s = new State(JFLAPPreferences.getDefaultStateNameBase()+id, id);
		this.add(s);
		return s;
	}
	
	@Override
	public StateSet copy() {
		return (StateSet) super.copy();
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode(this.toArray(new State[0]));
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == this)
			return true;
		if(!(obj.getClass().equals(this.getClass())))
			return false;
		return((StateSet) obj).size() == this.size() && containsAll((StateSet) obj);
	}
	
}
