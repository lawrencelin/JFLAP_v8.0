package view.action.grammar.conversion;

import view.grammar.GrammarView;
import model.algorithms.conversion.gramtoauto.CFGtoPDAConverterLR;
import model.automata.acceptors.pda.PDATransition;
import model.automata.acceptors.pda.PushdownAutomaton;
import model.grammar.Grammar;

public class CFGtoPDALRAction extends GrammarToAutomatonAction<PushdownAutomaton, PDATransition, CFGtoPDAConverterLR>{

	public CFGtoPDALRAction(GrammarView v) {
		super(v, "Convert CFG to PDA (LR)", "Convert to PDA (LR)");
	}

	@Override
	public CFGtoPDAConverterLR createConverter(Grammar g) {
		return new CFGtoPDAConverterLR(g);
	}

}
