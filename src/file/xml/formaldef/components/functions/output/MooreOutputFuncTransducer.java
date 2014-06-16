package file.xml.formaldef.components.functions.output;

import java.util.Map;

import model.automata.State;
import model.automata.transducers.OutputAlphabet;
import model.automata.transducers.OutputFunction;
import model.automata.transducers.moore.MooreOutputFunction;
import model.formaldef.components.functionset.function.LanguageFunction;
import model.symbols.SymbolString;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MooreOutputFuncTransducer extends OutputFunctionTransducer<MooreOutputFunction> {

	public MooreOutputFuncTransducer(OutputAlphabet alph) {
		super(alph);
	}

	@Override
	public MooreOutputFunction createOutputFunction(State state,
			SymbolString output, Map<String, Object> valueMap) {
		return new MooreOutputFunction(state, output);
	}

	@Override
	public Map<String, Object> finishTagToValueMap(Map<String, Object> map,
			MooreOutputFunction func) {
		return map;
	}

}
