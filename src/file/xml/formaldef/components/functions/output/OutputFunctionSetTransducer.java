package file.xml.formaldef.components.functions.output;

import model.automata.transducers.OutputFunction;
import model.automata.transducers.OutputFunctionSet;
import model.formaldef.components.SetComponent;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import file.xml.formaldef.components.functions.FunctionSetTransducer;
import file.xml.formaldef.components.functions.FunctionTransducer;

import util.Copyable;

public class OutputFunctionSetTransducer<T extends OutputFunction<T>> extends FunctionSetTransducer<T> {


	public OutputFunctionSetTransducer(FunctionTransducer<T> trans) {
		super(trans);
	}

	@Override
	public String getTag() {
		return OUTPUT_FUNC_SET;
	}

	@Override
	public SetComponent<T> createEmptyComponent() {
		return new OutputFunctionSet<T>();
	}

}
