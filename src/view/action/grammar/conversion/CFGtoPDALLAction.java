package view.action.grammar.conversion;

import view.grammar.GrammarView;
import model.algorithms.conversion.gramtoauto.CFGtoPDAConverterLL;
import model.automata.acceptors.pda.PDATransition;
import model.automata.acceptors.pda.PushdownAutomaton;
import model.grammar.Grammar;

public class CFGtoPDALLAction extends GrammarToAutomatonAction<PushdownAutomaton, PDATransition, CFGtoPDAConverterLL>{

	public CFGtoPDALLAction(GrammarView v) {
		super(v, "Convert CFG to PDA (LL)", "Convert to PDA (LL)");
	}

	@Override
	public CFGtoPDAConverterLL createConverter(Grammar g) {
		return new CFGtoPDAConverterLL(g);
	}

}
