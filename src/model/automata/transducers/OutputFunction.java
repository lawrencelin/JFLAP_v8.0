package model.automata.transducers;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.TreeSet;

import util.UtilFunctions;

import model.automata.AutomatonFunction;
import model.automata.State;
import model.automata.acceptors.fsa.FSATransition;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.functionset.function.LanguageFunction;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public abstract class OutputFunction<T extends OutputFunction<T>> extends AutomatonFunction<T> {

	private State myState;
	private SymbolString myOutput;

	public OutputFunction(State s, SymbolString output) {
		myState = s;
		myOutput = output;
	}

	public State getState(){
		return myState;
	}

	public Symbol[] getOutput(){
		return myOutput.toArray(new Symbol[0]);
	}

	/**
	 * Checks to see if this output function is associated
	 * with the transition passed in as an argument. This
	 * is simply a helper method.
	 * @param trans
	 * @return
	 */
	public abstract boolean matches(FSATransition trans);


	public void setState(State state) {
		myState = state;
	}

	public boolean setOutput(SymbolString out) {
		return myOutput.setTo(out);
	}

	@Override
	public SymbolString[] getPartsForAlphabet(Alphabet a) {
		if (a instanceof OutputAlphabet)
			return new SymbolString[]{myOutput};
		return new SymbolString[0];
	}

	@Override
	public int compareTo(OutputFunction o) {
		return UtilFunctions.metaCompare(
				new Comparable[]{this.myState, myOutput},
				new Comparable[]{o.myState, o.myOutput});
	}

	@Override
	public SymbolString[] getAllParts() {
		return new SymbolString[]{myOutput};
	}

	@Override
	protected void applySetTo(T other) {
		this.myState = other.myState;
		this.myOutput = other.myOutput;
		
	}
	
	@Override
	public String toString() {
		return myState + " --> " + myOutput;
	}
	
	
}

