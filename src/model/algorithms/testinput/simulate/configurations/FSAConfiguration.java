package model.algorithms.testinput.simulate.configurations;

import model.algorithms.testinput.simulate.Configuration;
import model.automata.State;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.automata.acceptors.fsa.FSATransition;
import model.symbols.SymbolString;


public class FSAConfiguration extends InputUsingConfiguration<FiniteStateAcceptor, 
																FSATransition> {

	public FSAConfiguration(FiniteStateAcceptor a, State s, int pos, SymbolString input) {
		super(a, s, pos, input);
	}

	@Override
	public String getName() {
		return "FSA Configuration";
	}

	@Override
	protected Configuration<FiniteStateAcceptor,FSATransition> createInputConfig(
			FiniteStateAcceptor a, 
			State s, 
			int ppos, 
			SymbolString input, 
			SymbolString[] updatedClones)
			throws Exception {
		return new FSAConfiguration(a, s, ppos, input);
	}

	@Override
	protected String getStringPresentationName(int i) {
		//return nothing, this will never be called
		return null;
	}

	@Override
	protected int getNextSecondaryPosition(int i,
			FSATransition trans) {
		//return nothing, this will never be called
		return 0;
	}

	@Override
	protected SymbolString[] assembleUpdatedStrings(SymbolString[] clones,
			FSATransition trans) {
		//return nothing, this will never be called
		return null;
	}




}
