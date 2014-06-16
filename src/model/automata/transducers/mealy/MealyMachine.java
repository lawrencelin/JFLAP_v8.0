package model.automata.transducers.mealy;

import model.automata.InputAlphabet;
import model.automata.StartState;
import model.automata.State;
import model.automata.StateSet;
import model.automata.TransitionSet;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.transducers.OutputAlphabet;
import model.automata.transducers.OutputFunctionSet;
import model.automata.transducers.Transducer;
import model.formaldef.components.FormalDefinitionComponent;

public class MealyMachine extends Transducer<MealyOutputFunction> {

	public MealyMachine(StateSet states, 
			InputAlphabet langAlph,
			OutputAlphabet outputAlph,
			TransitionSet<FSATransition> functions,
			StartState start,
			OutputFunctionSet<MealyOutputFunction> outputFunctions) {
		super(states, langAlph, outputAlph, functions, start, outputFunctions);
	}
	
	public MealyMachine(){
		super(new StateSet(),
				new InputAlphabet(),
				new OutputAlphabet(),
				new TransitionSet<FSATransition>(),
				new StartState(),
				new OutputFunctionSet<MealyOutputFunction>());
	}

	@Override
	public String getDescriptionName() {
		return "Mealy Machine";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FSATransition createBlankTransition(State from, State to) {
		return new FSATransition(from, to);
	}

	
}
