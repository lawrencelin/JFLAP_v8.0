package view.algorithms.conversion.autotogram;

import model.grammar.Grammar;
import model.grammar.Production;
import model.symbols.Symbol;
import model.undo.UndoKeeper;
import view.grammar.productions.ProductionDataHelper;

public class AutoProductionDataHelper extends ProductionDataHelper {

	public AutoProductionDataHelper(Grammar model, UndoKeeper keeper) {
		super(model, keeper);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void add(int index, Object[] input) {
		if (input[0] instanceof Symbol[] && input[2] instanceof Symbol[]) {
			Production p = new Production((Symbol[]) input[0],
					(Symbol[]) input[2]);
			if (isValid(p))
				addProduction(index, p);
		}
	}


}
