package file.xml.formaldef.components.functions.transitions;

import java.util.Map;

import model.automata.InputAlphabet;
import model.automata.SingleInputTransition;
import model.automata.State;
import model.automata.Transition;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.functionset.function.LanguageFunction;
import model.symbols.SymbolString;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import debug.JFLAPDebug;

public abstract class InputTransitionTransducer<T extends SingleInputTransition<T>> extends TransitionTransducer<T> {

	public InputTransitionTransducer(InputAlphabet alph, Alphabet ... alphs) {
		super(alph, alphs);
	}

	@Override
	public Map<String, Object> addOtherLabelComponentsToMap(
			Map<String, Object> base, T structure) {
		base.put(getInputTag(), structure.getInput());
		return base;
	}
	
	public String getInputTag() {
		return TRANS_INPUT_TAG;
	}

	@Override
	public T createTransition(State from, State to,
			Map<String, Object> remaining) {
		SymbolString input = (SymbolString) remaining.remove(getInputTag());
		return createTransition(from, to, input, remaining);
	}
	
	public abstract T createTransition(State from, State to,
			SymbolString input, Map<String, Object> remaining);


}
