package file.xml.formaldef.automata;

import java.util.List;
import java.util.Map;

import model.automata.InputAlphabet;
import model.automata.StartState;
import model.automata.StateSet;
import model.automata.TransitionSet;
import model.automata.acceptors.FinalStateSet;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.automata.acceptors.pda.BottomOfStackSymbol;
import model.automata.acceptors.pda.PDATransition;
import model.automata.acceptors.pda.PushdownAutomaton;
import model.automata.acceptors.pda.StackAlphabet;
import model.automata.transducers.OutputAlphabet;
import model.formaldef.FormalDefinition;
import model.formaldef.components.alphabets.Alphabet;
import model.grammar.TerminalAlphabet;
import model.grammar.VariableAlphabet;
import file.xml.formaldef.components.functions.productions.ProductionTransducer;
import file.xml.formaldef.components.functions.transitions.PDATransitionTransducer;
import file.xml.formaldef.components.functions.transitions.TransitionSetTransducer;

public class PDATransducer extends AutomatonTransducer<PushdownAutomaton> {

	@Override
	public String getTag() {
		return "pda";
	}

	@Override
	public TransitionSetTransducer createTransitionTransducer(
			List<Alphabet> alphs) {
		InputAlphabet input = retrieveAlphabet(alphs, InputAlphabet.class);
		StackAlphabet stack = retrieveAlphabet(alphs, StackAlphabet.class);
		
		PDATransitionTransducer trans = new PDATransitionTransducer(input, stack);
		return new TransitionSetTransducer<PDATransition>(trans);
	}

	@Override
	public PushdownAutomaton buildStructure(Object[] subComp) {
		return new PushdownAutomaton(retrieveTarget(StateSet.class,subComp), 
				retrieveTarget(InputAlphabet.class, subComp),
				retrieveTarget(StackAlphabet.class, subComp),
				retrieveTarget(TransitionSet.class, subComp),
				retrieveTarget(StartState.class, subComp),
				retrieveTarget(BottomOfStackSymbol.class, subComp), 
				retrieveTarget(FinalStateSet.class, subComp));
	}


}
