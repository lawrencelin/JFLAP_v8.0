package model.algorithms.conversion.autotogram;

import model.automata.State;
import model.symbols.Symbol;

public class PDAVariableMapping extends VariableMapping {

	
	private State myState1;
	private Symbol mySymbol;
	private State myState2;

	public PDAVariableMapping(State s1, Symbol stackSym, State s2) {
		myState1 = s1;
		mySymbol = stackSym;
		myState2 = s2;
	}
	
	@Override
	public String toString() {
		return getFirstState().getName() + mySymbol + getSecondState().getName();
	}

	public State getSecondState() {
		return myState2;
	}

	public State getFirstState() {
		return myState1;
	}

	public Symbol getStackSymbol(){
		return mySymbol;
	}
	
	
	@Override
	public boolean equals(Object arg0) {
		PDAVariableMapping oMap = (PDAVariableMapping) arg0;
		
		
		
		return this.getSecondState().equals(oMap.getSecondState()) &&
					this.getFirstState().equals(oMap.getFirstState()) &&
					this.getStackSymbol().equals(getStackSymbol());
	}

	@Override
	public int hashCode() {
		return this.getSecondState().hashCode() * this.getFirstState().hashCode() * this.getStackSymbol().hashCode();
	}

}
