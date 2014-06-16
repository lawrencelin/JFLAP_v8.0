package model.algorithms.testinput.simulate.configurations;

import model.algorithms.testinput.simulate.Configuration;
import model.automata.State;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.transducers.OutputFunctionSet;
import model.automata.transducers.mealy.MealyMachine;
import model.automata.transducers.mealy.MealyOutputFunction;
import model.symbols.SymbolString;


public class MealyConfiguration extends
		InputOutputConfiguration<MealyMachine, MealyOutputFunction> {

	public MealyConfiguration(MealyMachine m, State s, int pos, SymbolString input, 
			SymbolString output) {
		super(m, s, pos, input, output);
	}

	@Override
	public String getName() {
		return "Mealy Configuration";
	}

}
