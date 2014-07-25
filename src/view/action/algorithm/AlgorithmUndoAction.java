package view.action.algorithm;

import model.algorithms.steppable.SteppableAlgorithm;

public class AlgorithmUndoAction extends AlgorithmAction<SteppableAlgorithm> {

	public AlgorithmUndoAction(SteppableAlgorithm alg) {
		super("Undo", alg);
	}
	
	@Override
	public void actionPerformed(SteppableAlgorithm alg) {
		alg.undo();
	}

}
