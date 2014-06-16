package model.automata.transducers.mealy;

import debug.JFLAPDebug;
import util.UtilFunctions;
import model.automata.InputAlphabet;
import model.automata.State;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.transducers.OutputFunction;
import model.formaldef.components.alphabets.Alphabet;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public class MealyOutputFunction extends OutputFunction<MealyOutputFunction> {

	private SymbolString myInput;

	public MealyOutputFunction(State s,  SymbolString input, SymbolString output) {
		super(s, output);
		myInput = input;
	}

	public MealyOutputFunction(FSATransition t, SymbolString output) {
		this(t.getFromState(), new SymbolString(t.getInput()), output);
	}
	
	public MealyOutputFunction(FSATransition t) {
		this(t, new SymbolString());
	}

	@Override
	public String getDescriptionName() {
		return "Mealy Output Function";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	public Symbol[] getInput(){
		return myInput.toArray(new Symbol[0]);
	}
	
	public boolean setInput(SymbolString s){
		return myInput.setTo(s);
	}
	
	@Override
	public SymbolString[] getPartsForAlphabet(Alphabet a) {
		if (a instanceof InputAlphabet)
			return new SymbolString[]{myInput};
		return super.getPartsForAlphabet(a);
	}
	
	@Override
	public boolean matches(FSATransition trans) {
		return myInput.equals(new SymbolString(trans.getInput())) &&
				this.getState().equals(trans.getFromState());
	}

	@Override
	public int compareTo(OutputFunction o) {
		int compare = super.compareTo(o);
		if (compare != 0) return compare;
		return this.myInput.compareTo(((MealyOutputFunction) o).myInput);
	}

	@Override
	public boolean equals(Object o) {
		if(!(o instanceof OutputFunction)) return false;
		return compareTo((OutputFunction) o) == 0;
	}

	@Override
	public MealyOutputFunction copy() {
		return new MealyOutputFunction(getState(), 
				new SymbolString(myInput), 
				new SymbolString(getOutput()));
	}
	
	@Override
	public SymbolString[] getAllParts() {
		return UtilFunctions.combine(super.getAllParts(), myInput);
	}
	
	@Override
	protected void applySetTo(MealyOutputFunction other) {
		super.applySetTo(other);
		this.myInput.setTo(other.myInput);
	}
	
	@Override
	public String toString() {
		return "(" + getState() + ", " + myInput + ") --> " + new SymbolString(getOutput());
	}

}
