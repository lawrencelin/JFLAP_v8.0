package model.automata;

import java.awt.Point;
import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.util.Set;
import java.util.TreeSet;

import util.Copyable;



import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.functionset.function.LanguageFunction;
import model.symbols.Symbol;
import model.symbols.SymbolString;

/**
 * A <CODE>Transition</CODE> object is a simple abstract class representing a
 * transition between two state objects in an automaton. Subclasses of this
 * transition class will have additional fields containing the particulars
 * necessary for their transition.
 * 
 * @see jflap.model.automaton.State
 * @see jflap.model.automaton.Automaton
 * 
 * @author Thomas Finley, Henry Qin
 */

public abstract class SingleInputTransition<T extends SingleInputTransition<T>> extends Transition<T>{

	/** 
	 * the string of symbols that allows some input
	 * to move along this transition
	 */
	private SymbolString myInput;


	/**
	 * Instantiates a new <CODE>Transition</CODE>.
	 * 
	 * @param from
	 *            the state this transition is from
	 * @param to
	 *            the state this transition moves to
	 * @param input
	 * 			  the input to move along this transition
	 */
	public SingleInputTransition(State from, State to, SymbolString input) {
		super(from, to);
		myInput = input;
	}
	
	public SingleInputTransition(State from, State to, Symbol s) {
		this(from, to, new SymbolString(s));
	}
	
	public SingleInputTransition(State from, State to) {
		this(from,to, new SymbolString());
	}


	public Symbol[] getInput(){
		return myInput.toArray(new Symbol[0]);
	}


	public boolean setInput(SymbolString input){
		T copy = this.copy();
		copy.myInput = input;
		return setTo(copy);
	}

	@Override
	public SymbolString[] getPartsForAlphabet(Alphabet a) {
		if (a instanceof InputAlphabet)
			return new SymbolString[]{myInput};
		return new SymbolString[0];
	}
	
	@Override
	public SymbolString[] getAllParts() {
		return new SymbolString[]{myInput};
	}
	
	@Override
	public int compareTo(T o) {
		int compare = super.compareTo(o);
		if (compare == 0)
			compare = myInput.compareTo(o.myInput);
		return compare;
	}

	@Override
	public boolean isLambdaTransition() {
		return myInput.isEmpty();
	}
	
	protected void applySetTo(T other) {
		super.applySetTo(other);
		this.myInput.setTo(other.myInput);
	}
	
	@Override
	public String getLabelText() {
		return myInput.toString();
	}
	
}