package model.algorithms.transform;

import debug.JFLAPDebug;
import errors.BooleanWrapper;
import model.algorithms.AlgorithmException;
import model.algorithms.FormalDefinitionAlgorithm;
import model.algorithms.steppable.AlgorithmStep;
import model.formaldef.FormalDefinition;
import model.grammar.Grammar;

public abstract class FormalDefinitionTransformAlgorithm<T extends FormalDefinition> extends
		FormalDefinitionAlgorithm<T> {

	
	public FormalDefinitionTransformAlgorithm(T fd) {
		super(fd);
	}

	private T myNewDefinition;

	
	public T getTransformedDefinition(){
		return myNewDefinition;
	}
	
	@Override
	public boolean reset() throws AlgorithmException {
		myNewDefinition = (T) getOriginalDefinition().copy();
		return true;
	}



}
