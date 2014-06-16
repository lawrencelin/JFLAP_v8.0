package model.automata.transducers.moore;

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

public class MooreMachine extends Transducer<MooreOutputFunction> {


	public MooreMachine(StateSet states, 
							InputAlphabet langAlph,
							OutputAlphabet outputAlph,
							TransitionSet<FSATransition> functions,
							StartState start,
							OutputFunctionSet<MooreOutputFunction> outputFunctions) {
		super(states, langAlph, outputAlph, functions, start, outputFunctions);
	}
	
	public MooreMachine(){
		super( new StateSet(),
				new InputAlphabet(),
				new OutputAlphabet(),
				new TransitionSet<FSATransition>(),
				new StartState(),
				new OutputFunctionSet<MooreOutputFunction>());
	}

	@Override
	public String getDescriptionName() {
		return "Moore Machine";
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public FSATransition createBlankTransition(State from, State to) {
		return new FSATransition(from, to);
	}



}
