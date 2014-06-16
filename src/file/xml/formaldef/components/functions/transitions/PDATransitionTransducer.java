package file.xml.formaldef.components.functions.transitions;

import java.util.Map;

import model.automata.InputAlphabet;
import model.automata.State;
import model.automata.acceptors.pda.PDATransition;
import model.automata.acceptors.pda.StackAlphabet;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.functionset.function.LanguageFunction;
import model.symbols.SymbolString;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import file.xml.formaldef.components.functions.FunctionTransducer;

public class PDATransitionTransducer extends InputTransitionTransducer<PDATransition> {

	public PDATransitionTransducer(InputAlphabet input, StackAlphabet stack) {
		super(input, stack);
	}

	@Override
	public Map<String, Object> addOtherLabelComponentsToMap(
			Map<String, Object> base, PDATransition t) {
		base.put(POP_TAG, t.getPop());
		base.put(PUSH_TAG, t.getPush());
		return super.addOtherLabelComponentsToMap(base, t);
	}

	@Override
	public PDATransition createTransition(State from, State to,
			SymbolString input, Map<String, Object> remaining) {
		SymbolString pop = (SymbolString) remaining.get(POP_TAG);
		SymbolString push = (SymbolString) remaining.get(PUSH_TAG);
		return new PDATransition(from, to, input, pop, push);
	}


}
