package file.xml.formaldef.automata;

import java.util.List;

import file.xml.formaldef.components.functions.output.MooreOutputFuncTransducer;
import file.xml.formaldef.components.functions.output.OutputFunctionSetTransducer;
import file.xml.formaldef.components.functions.transitions.TransitionSetTransducer;
import model.automata.InputAlphabet;
import model.automata.StartState;
import model.automata.StateSet;
import model.automata.TransitionSet;
import model.automata.acceptors.FinalStateSet;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.automata.transducers.OutputAlphabet;
import model.automata.transducers.OutputFunctionSet;
import model.automata.transducers.moore.MooreMachine;
import model.automata.transducers.moore.MooreOutputFunction;
import model.formaldef.components.alphabets.Alphabet;

public class MooreMachineTransducer extends TransducerTransducer<MooreMachine> {

	
	@Override
	public String getTag() {
		return MOORE_TAG;
	}

	@Override
	public OutputFunctionSetTransducer createOutputSetTransducer(
			List<Alphabet> alphs) {
		OutputAlphabet output = retrieveAlphabet(alphs, OutputAlphabet.class);
		MooreOutputFuncTransducer trans = new MooreOutputFuncTransducer(output);
		return new OutputFunctionSetTransducer<MooreOutputFunction>(trans);
	}

	@Override
	public TransitionSetTransducer createTransitionTransducer(
			List<Alphabet> alphs) {
		return FSATransducer.createFSATransitionTransducer(alphs);
	}

	@Override
	public MooreMachine buildStructure(Object[] subComp) {
		return new MooreMachine(retrieveTarget(StateSet.class,subComp), 
				retrieveTarget(InputAlphabet.class, subComp), 
				retrieveTarget(OutputAlphabet.class, subComp), 
				retrieveTarget(TransitionSet.class, subComp), 
				retrieveTarget(StartState.class, subComp), 
				retrieveTarget(OutputFunctionSet.class, subComp));
	}

}
