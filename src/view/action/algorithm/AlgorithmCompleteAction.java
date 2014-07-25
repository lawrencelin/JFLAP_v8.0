package view.action.algorithm;

import view.grammar.parsing.ll.LLParseTablePanel;
import model.algorithms.steppable.SteppableAlgorithm;
import model.algorithms.steppable.SteppableAlgorithm;

public class AlgorithmCompleteAction extends AlgorithmAction {
	
	private LLParseTablePanel myPanel;

	public AlgorithmCompleteAction(SteppableAlgorithm alg) {
		super("Complete", alg);
	}

	public AlgorithmCompleteAction(LLParseTablePanel panel, SteppableAlgorithm alg) {
		this(alg);
		myPanel = panel;
	}

	@Override
	public void actionPerformed(SteppableAlgorithm alg) {
		if (myPanel != null)
			myPanel.complete();
		alg.stepToCompletion();
	}

}
