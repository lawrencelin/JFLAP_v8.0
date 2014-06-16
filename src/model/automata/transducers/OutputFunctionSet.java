package model.automata.transducers;

import debug.JFLAPDebug;
import model.automata.acceptors.fsa.FSATransition;
import model.formaldef.components.functionset.FunctionSet;
import model.symbols.SymbolString;

public class OutputFunctionSet<T extends OutputFunction<T>> extends FunctionSet<T> {

	@Override
	public Character getCharacterAbbr() {
		return 'G';
	}

	@Override
	public String getDescriptionName() {
		return "Output Function Set";
	}

	@Override
	public String getDescription() {
		return "The set of functions that determine the output of a transducer.";
	}

	public SymbolString getOutputForTransition(FSATransition trans) {
		for (T func: this){
			if (func.matches(trans))
				return new SymbolString(func.getOutput());
		}
		
		return null;
	}

}
