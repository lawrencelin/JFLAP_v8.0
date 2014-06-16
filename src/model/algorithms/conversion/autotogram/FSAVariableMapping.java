package model.algorithms.conversion.autotogram;

import model.automata.State;
import model.symbols.SymbolString;

public class FSAVariableMapping extends VariableMapping {

	private State myState;

	public FSAVariableMapping(State state) {
		myState = state;
	}

	@Override
	public String toString() {
		return this.getState().getName();
	}

	public State getState() {
		return myState;
	}


	@Override
	public boolean equals(Object obj) {
		return this.getState().equals(((FSAVariableMapping) obj).getState());
	}

	@Override
	public int hashCode() {
		return this.getState().hashCode();
	}
	
	
	

}
