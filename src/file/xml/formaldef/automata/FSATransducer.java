package file.xml.formaldef.automata;

import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;

import file.xml.XMLTransducer;
import file.xml.formaldef.FormalDefinitionTransducer;
import file.xml.formaldef.components.functions.FunctionSetTransducer;
import file.xml.formaldef.components.functions.transitions.FSATransitionTransducer;
import file.xml.formaldef.components.functions.transitions.TransitionSetTransducer;
import file.xml.formaldef.components.functions.transitions.TransitionTransducer;

import model.automata.InputAlphabet;
import model.automata.StartState;
import model.automata.StateSet;
import model.automata.TransitionSet;
import model.automata.acceptors.FinalStateSet;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.formaldef.components.alphabets.Alphabet;

public class FSATransducer extends AutomatonTransducer<FiniteStateAcceptor> {

	@Override
	public FiniteStateAcceptor buildStructure(Object[] subComp) {
		return new FiniteStateAcceptor(retrieveTarget(StateSet.class,subComp), 
				retrieveTarget(InputAlphabet.class, subComp), 
				retrieveTarget(TransitionSet.class, subComp), 
				retrieveTarget(StartState.class, subComp), 
				retrieveTarget(FinalStateSet.class, subComp));
	}

	@Override
	public String getTag() {
		return FSA_TAG;
	}

	@Override
	public TransitionSetTransducer createTransitionTransducer(
			List<Alphabet> alphs) {
		return createFSATransitionTransducer(alphs);
	}

	public static TransitionSetTransducer createFSATransitionTransducer(
			List<Alphabet> alphs) {
		InputAlphabet inputAlph = retrieveAlphabet(alphs, InputAlphabet.class);
		FSATransitionTransducer single = 
				new FSATransitionTransducer(inputAlph);
		TransitionSetTransducer<FSATransition> ducer = 
				new TransitionSetTransducer<FSATransition>(single);
		return ducer;
	}

	
}
