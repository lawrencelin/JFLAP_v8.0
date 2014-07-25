package model.algorithms.conversion.gramtoauto;

import model.grammar.Grammar;
import model.grammar.typetest.GrammarType;

/**
 * Context-free grammar to automaton converter used for grammar transformations
 * i.e. unit production removal and useless production removal
 * @author Lawrence Lin
 *
 */
public class CFGtoFSAConverter extends RGtoFSAConverter {

	public CFGtoFSAConverter(Grammar g) {
		super(g);
	}
	
	@Override
	public GrammarType[] getValidTypes() {
		return new GrammarType[]{GrammarType.CONTEXT_FREE};
	}
	
	public void removeFinalState() {
		this.getConvertedAutomaton().getStates().remove(getFinalState());
	}

}
