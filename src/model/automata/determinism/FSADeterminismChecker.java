package model.automata.determinism;

import model.automata.acceptors.fsa.FSATransition;

public class FSADeterminismChecker extends DeterminismChecker<FSATransition> {

	
	@Override
	protected boolean areNondeterministic(FSATransition trans1,
			FSATransition trans2) {
		String label1 = trans1.getLabelText();
		String label2 = trans2.getLabelText();
		return label1.startsWith(label2) || label2.startsWith(label1);
	}


}
