package model.automata.determinism;

import model.automata.acceptors.pda.PDATransition;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public class PDADeterminismChecker extends DeterminismChecker<PDATransition> {

	@Override
	protected boolean areNondeterministic(PDATransition trans1,
			PDATransition trans2) {
		Symbol[] input1 = trans1.getInput();
		Symbol[] input2 = trans2.getInput();
		Symbol[] toPop1 = trans1.getPop();
		Symbol[] toPop2 = trans2.getPop();

		return arePrefixesOfEachOther(input1, input2)
				&& arePrefixesOfEachOther(toPop1, toPop2);
	}

	private boolean arePrefixesOfEachOther(Symbol[] input1, Symbol[] input2) {
		String s1 = new SymbolString(input1).toString();
		String s2 = new SymbolString(input2).toString();
		
		return s1.startsWith(s2) || s2.startsWith(s1);
	}
	
	


}
