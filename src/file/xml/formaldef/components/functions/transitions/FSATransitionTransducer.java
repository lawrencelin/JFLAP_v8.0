package file.xml.formaldef.components.functions.transitions;

import java.util.Map;

import model.automata.InputAlphabet;
import model.automata.State;
import model.automata.acceptors.fsa.FSATransition;
import model.formaldef.components.SetComponent;
import model.formaldef.components.alphabets.Alphabet;
import model.symbols.SymbolString;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import util.Copyable;
import file.xml.XMLHelper;

public class FSATransitionTransducer extends InputTransitionTransducer<FSATransition> {


	public FSATransitionTransducer(InputAlphabet alph) {
		super(alph);
	}


	@Override
	public String getTag() {
		return FSA_TRANS;
	}


	@Override
	public FSATransition createTransition(State from, State to,
			SymbolString input, Map<String, Object> remaining) {
		return new FSATransition(from, to, input);
	}


	@Override
	public Map<String, Object> addOtherLabelComponentsToMap(
			Map<String, Object> base, FSATransition structure) {
		return super.addOtherLabelComponentsToMap(base, structure);
	}

}
