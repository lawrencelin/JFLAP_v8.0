package model.algorithms.testinput.simulate.configurations;

import model.automata.State;
import model.automata.transducers.moore.MooreMachine;
import model.automata.transducers.moore.MooreOutputFunction;
import model.symbols.SymbolString;


public class MooreConfiguration extends
	InputOutputConfiguration<MooreMachine, MooreOutputFunction> {

	public MooreConfiguration(MooreMachine m, State s, int pos, SymbolString input, 
			SymbolString output) {
		super(m, s, pos, input, output);
	}

	@Override
	public String getName() {
		return "Moore Configuration";
	}



}