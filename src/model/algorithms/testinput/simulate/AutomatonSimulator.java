package model.algorithms.testinput.simulate;

import util.Copyable;
import model.automata.Automaton;
import model.formaldef.Describable;
import model.symbols.SymbolString;

public abstract class AutomatonSimulator implements Describable, Copyable{

	private Automaton myAutomaton;
	
	public AutomatonSimulator(Automaton a){
		myAutomaton = a;
	}
	
	public abstract void beginSimulation(SymbolString ... input);
	
	public abstract int getSpecialAcceptCase();
	
	public Automaton getAutomaton() {
		return myAutomaton;
	}

}
