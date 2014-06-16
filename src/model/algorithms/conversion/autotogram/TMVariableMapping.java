package model.algorithms.conversion.autotogram;

import model.automata.State;
import model.symbols.Symbol;

/**
 * Variable mapping for single-tape Turing Machine to Unrestricted Grammar
 * conversion algorithm as defined in the Linz book. Variables are mapped to
 * form Vab or Vaib where a is a Symbol from InputAlphabet U {blank}, b is a
 * Symbol from the TapeAlphabet, i is the ID of State qi in the StateSet of the
 * Turing Machine.
 * 
 * @author Ian McMahon
 * 
 */
public class TMVariableMapping extends VariableMapping {
	private State myState;
	private Symbol myA;
	private Symbol myB;

	public TMVariableMapping(Symbol a, Symbol b) {
		this(a, null, b);
	}

	public TMVariableMapping(Symbol a, State i, Symbol b) {
		myA = a;
		myState = i;
		myB = b;
	}

	@Override
	public String toString() {
		return "V" + myA + (myState == null ? "" : myState.getID()) + myB;
	}

	@Override
	public boolean equals(Object arg0) {
		TMVariableMapping other = (TMVariableMapping) arg0;
		if ((myState == null && other.myState != null)
				|| (myState != null && other.myState == null))
			return false;
		if (myState == null && other.myState == null) {
			return myA.equals(other.myA) && myB.equals(other.myB);
		}
		return (myA.equals(other.myA) && myB.equals(other.myB) && myState
				.equals(other.myState));
	}

	@Override
	public int hashCode() {
		int hash = myA.hashCode() + myB.hashCode() * myB.hashCode()
				* myB.hashCode();
		if (myState != null) {
			hash += myState.hashCode() * myState.hashCode();
		}
		return hash;
	}

}
