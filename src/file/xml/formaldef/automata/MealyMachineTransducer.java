package file.xml.formaldef.automata;

import java.util.List;
import java.util.Map;

import model.automata.InputAlphabet;
import model.automata.StartState;
import model.automata.StateSet;
import model.automata.TransitionSet;
import model.automata.transducers.OutputAlphabet;
import model.automata.transducers.OutputFunctionSet;
import model.automata.transducers.mealy.MealyMachine;
import model.automata.transducers.mealy.MealyOutputFunction;
import model.automata.transducers.moore.MooreMachine;
import model.automata.transducers.moore.MooreOutputFunction;
import model.formaldef.FormalDefinition;
import model.formaldef.components.alphabets.Alphabet;
import file.xml.formaldef.components.functions.output.MealyOutputFuncTransducer;
import file.xml.formaldef.components.functions.output.MooreOutputFuncTransducer;
import file.xml.formaldef.components.functions.output.OutputFunctionSetTransducer;
import file.xml.formaldef.components.functions.transitions.TransitionSetTransducer;

public class MealyMachineTransducer extends TransducerTransducer<MealyMachine> {


	@Override
	public String getTag() {
		return MEALY_TAG;
	}

	@Override
	public OutputFunctionSetTransducer<MealyOutputFunction> createOutputSetTransducer(
			List<Alphabet> alphs) {
		OutputAlphabet output = retrieveAlphabet(alphs, OutputAlphabet.class);
		InputAlphabet input	= retrieveAlphabet(alphs, InputAlphabet.class);
		MealyOutputFuncTransducer trans = new MealyOutputFuncTransducer(output, input);
		return new OutputFunctionSetTransducer<MealyOutputFunction>(trans);
	}

	@Override
	public TransitionSetTransducer createTransitionTransducer(
			List<Alphabet> alphs) {
		return FSATransducer.createFSATransitionTransducer(alphs);
	}

	@Override
	public MealyMachine buildStructure(Object[] subComp) {
		return new MealyMachine(retrieveTarget(StateSet.class,subComp), 
				retrieveTarget(InputAlphabet.class, subComp), 
				retrieveTarget(OutputAlphabet.class, subComp), 
				retrieveTarget(TransitionSet.class, subComp), 
				retrieveTarget(StartState.class, subComp), 
				retrieveTarget(OutputFunctionSet.class, subComp));
	}

}
