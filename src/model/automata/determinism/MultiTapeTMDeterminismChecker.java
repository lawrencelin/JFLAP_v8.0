package model.automata.determinism;

import model.automata.turing.MultiTapeTMTransition;
import model.symbols.Symbol;

public class MultiTapeTMDeterminismChecker extends DeterminismChecker<MultiTapeTMTransition>{

	@Override
	protected boolean areNondeterministic(MultiTapeTMTransition trans1,
			MultiTapeTMTransition trans2) {
		for(int i=0; i < trans1.getNumTapes(); i++){
			Symbol s1 = trans1.getRead(i);
			Symbol s2 = trans2.getRead(i);
			
			if(!s1.equals(s2))
				return false;
		}
		return true;
	}

}
