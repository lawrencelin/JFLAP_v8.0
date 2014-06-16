package file.xml.formaldef.components.functions.output;

import java.util.HashMap;
import java.util.Map;

import model.automata.State;
import model.automata.transducers.OutputAlphabet;
import model.automata.transducers.OutputFunction;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.functionset.function.LanguageFunction;
import model.symbols.SymbolString;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import util.UtilFunctions;

import file.xml.formaldef.components.functions.FunctionTransducer;

public abstract class OutputFunctionTransducer<T extends OutputFunction> extends FunctionTransducer<T> {

	public OutputFunctionTransducer(OutputAlphabet alph, Alphabet ... others){
		super(UtilFunctions.combine(others, alph));
	}
	
	@Override
	public T createFunction(Map<String, Object> valueMap) {
		SymbolString output = (SymbolString) valueMap.remove(OUTPUT_TAG);
		State state = (State) valueMap.remove(STATE_TAG);
		return createOutputFunction(state, output, valueMap);
	}

	public abstract T createOutputFunction(State state, SymbolString output,
			Map<String, Object> valueMap);

	@Override
	public Map<String, Object> createTagToValueMap(T func) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(OUTPUT_TAG, func.getOutput());
		map.put(STATE_TAG, func.getState());
		return finishTagToValueMap(map, func);
	}

	@Override
	public String getTag() {
		return OUTPUT_FUNC_TAG;
	}
	
	public abstract Map<String, Object> finishTagToValueMap(Map<String, Object> map, T func);


	
}
