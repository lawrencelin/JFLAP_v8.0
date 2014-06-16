package view.action.algorithm;

import model.algorithms.steppable.SteppableAlgorithm;
import model.algorithms.steppable.SteppableAlgorithm;

public class AlgorithmResetAction extends AlgorithmAction<SteppableAlgorithm> {

	public AlgorithmResetAction(SteppableAlgorithm alg) {
		super("Reset", alg);
	}

	@Override
	public void actionPerformed(SteppableAlgorithm alg) {
		alg.reset();
	}

}
