package model.algorithms.steppable;

import model.algorithms.AlgorithmException;
import model.formaldef.Describable;

public interface AlgorithmStep extends Describable {

	public boolean execute() throws AlgorithmException;
	
	public boolean isComplete();
	
}
