package model.algorithms.conversion;

import model.algorithms.AlgorithmException;
import model.algorithms.FormalDefinitionAlgorithm;
import model.formaldef.FormalDefinition;

public abstract class ConversionAlgorithm<T extends FormalDefinition, S extends FormalDefinition> 
								extends FormalDefinitionAlgorithm<T> {

	
	
	private S myConvertedDefinition;


	public ConversionAlgorithm(T fd) {
		super(fd);
	}
	
	
	@Override
	public boolean reset() throws AlgorithmException {
		myConvertedDefinition = this.createBaseConverted();
		return true;
	}

	public S getConvertedDefinition(){
		return myConvertedDefinition;
	}

	public abstract S createBaseConverted();

	
}
