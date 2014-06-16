package model.automata.determinism;

import debug.JFLAPDebug;
import model.automata.turing.buildingblock.BlockTransition;
import model.symbols.Symbol;
import util.JFLAPConstants;

public class BlockTMDeterminismChecker extends
		DeterminismChecker<BlockTransition> {

	@Override
	protected boolean areNondeterministic(BlockTransition trans1,
			BlockTransition trans2) {
		Symbol[] s1 = trans1.getInput();
		Symbol[] s2 = trans2.getInput();
		String sym1 = s1[0].toString();
		String sym2 = s2[0].toString();
		if (sym1.equals(JFLAPConstants.TILDE)
				|| sym2.equals(JFLAPConstants.TILDE))
			return true;
		if (sym1.equals(JFLAPConstants.NOT)) {
			sym1 = s1[1].toString();
			if (!sym2.equals(sym1))
				return true;
		} else if (sym2.equals(JFLAPConstants.NOT)) {
			sym2 = s2[1].toString();
			if (!sym2.equals(sym1))
				return true;
		}
		return sym1.equals(sym2);
	}

}
