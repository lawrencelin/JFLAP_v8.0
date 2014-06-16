package model.automata.transducers.moore;

import model.automata.State;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.transducers.OutputFunction;
import model.symbols.SymbolString;

public class MooreOutputFunction extends OutputFunction<MooreOutputFunction> {

	public MooreOutputFunction(State s,SymbolString output) {
		super(s, output);
	}

	@Override
	public String getDescriptionName() {
		return "Moore OutputFunction";
	}


	
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean matches(FSATransition trans) {
		return trans.getToState().equals(this.getState());
	}


	@Override
	public MooreOutputFunction copy() {
		return new MooreOutputFunction(getState(), new SymbolString(getOutput()));
	}

}
