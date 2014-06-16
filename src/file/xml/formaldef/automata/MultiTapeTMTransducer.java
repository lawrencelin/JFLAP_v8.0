package file.xml.formaldef.automata;

import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import debug.JFLAPDebug;

import util.UtilFunctions;

import model.automata.InputAlphabet;
import model.automata.StartState;
import model.automata.StateSet;
import model.automata.TransitionSet;
import model.automata.acceptors.FinalStateSet;
import model.automata.acceptors.pda.BottomOfStackSymbol;
import model.automata.acceptors.pda.PushdownAutomaton;
import model.automata.acceptors.pda.StackAlphabet;
import model.automata.turing.BlankSymbol;
import model.automata.turing.MultiTapeTMTransition;
import model.automata.turing.MultiTapeTuringMachine;
import model.automata.turing.TapeAlphabet;
import model.automata.turing.TuringMachine;
import model.formaldef.FormalDefinition;
import model.formaldef.components.alphabets.Alphabet;
import file.xml.XMLHelper;
import file.xml.formaldef.components.functions.transitions.TransitionSetTransducer;
import file.xml.formaldef.components.functions.transitions.tm.IntegerTransducer;
import file.xml.formaldef.components.functions.transitions.tm.MultiTapeTMTransitionTransducer;

public class MultiTapeTMTransducer extends AutomatonTransducer<MultiTapeTuringMachine> {

	@Override
	public String getTag() {
		return TM_TAG;
	}

	@Override
	public TransitionSetTransducer<MultiTapeTMTransition> createTransitionTransducer(
			List<Alphabet> alphs) {
		InputAlphabet input = retrieveAlphabet(alphs, InputAlphabet.class);
		TapeAlphabet tape = retrieveAlphabet(alphs, TapeAlphabet.class);
		TransitionSetTransducer<MultiTapeTMTransition> transducer = 
				new TransitionSetTransducer<MultiTapeTMTransition>(new MultiTapeTMTransitionTransducer(input, tape));
		return transducer;
	}
	
	@Override
	public Element appendComponentsToRoot(Document doc,
			MultiTapeTuringMachine structure, Element root) {
		Element e = new IntegerTransducer(TAPE_NUM).toXMLTree(doc, structure.getNumTapes());
		root.appendChild(e);
		return super.appendComponentsToRoot(doc, structure, root);
	}
	
	@Override
	public Object[] toSubStructureList(Element root) {
		Object[] comps = super.toSubStructureList(root);
		Element e = (Element) XMLHelper.getChildrenWithTag(root, TAPE_NUM).get(0);
		Integer tapes = new IntegerTransducer(TAPE_NUM).fromStructureRoot(e);
		return UtilFunctions.combine(comps, tapes);
	}
	@Override
	public MultiTapeTuringMachine buildStructure(Object[] subComp) {
		StateSet states = retrieveTarget(StateSet.class, subComp);
		states = states == null ? new StateSet() : states;
		
		TapeAlphabet tape = retrieveTarget(TapeAlphabet.class, subComp);
		tape = tape == null ? new TapeAlphabet() : tape;
		
		BlankSymbol blank = retrieveTarget(BlankSymbol.class, subComp);
		blank = blank == null ? new BlankSymbol() : blank;
		
		InputAlphabet input = retrieveTarget(InputAlphabet.class, subComp);
		input = input == null ? new InputAlphabet() : input;
		
		TransitionSet<MultiTapeTMTransition> transitions = retrieveTarget(TransitionSet.class, subComp);
		transitions = transitions == null ? new TransitionSet<MultiTapeTMTransition>() : transitions;
		
		StartState start = retrieveTarget(StartState.class, subComp);
		start = start == null ? new StartState() : start;
		
		FinalStateSet finals = retrieveTarget(FinalStateSet.class, subComp);
		finals = finals == null ? new FinalStateSet() : finals;
		
		int numTapes = retrieveTarget(Integer.class, subComp);
		
		return new MultiTapeTuringMachine(states, tape, blank, input, transitions, start, finals, numTapes);

	}
}
