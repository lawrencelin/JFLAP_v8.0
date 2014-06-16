package view.action.algorithm;

import model.algorithms.steppable.SteppableAlgorithm;
import model.algorithms.steppable.SteppableAlgorithm;

public class AlgorithmCompleteAction extends AlgorithmAction {

	public AlgorithmCompleteAction(SteppableAlgorithm alg) {
		super("Complete", alg);
	}

	@Override
	public void actionPerformed(SteppableAlgorithm alg) {
		alg.stepToCompletion();
	}

}
