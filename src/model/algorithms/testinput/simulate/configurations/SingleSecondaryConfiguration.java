package model.algorithms.testinput.simulate.configurations;

import java.lang.reflect.Constructor;

import model.algorithms.testinput.simulate.Configuration;
import model.automata.Automaton;
import model.automata.State;
import model.automata.SingleInputTransition;
import model.symbols.SymbolString;


public abstract class SingleSecondaryConfiguration<S extends Automaton<T>, 
														T extends SingleInputTransition<T>> 
															  extends InputUsingConfiguration<S, T> {

	public SingleSecondaryConfiguration(S a, State s, int pos, SymbolString input,
			SymbolString output) {
		super(a, s, pos, input, output);
	}

	@Override
	protected Configuration<S,T> createInputConfig(S a, State s, int ppos,
			SymbolString input, SymbolString[] updatedClones) throws Exception {
		Constructor cons = this.getClass().getConstructors()[0];
		return (Configuration<S, T>) cons.newInstance(a, s, ppos, input, updatedClones[0]);
	}

	protected SymbolString getSecondaryString() {
		return getStringForIndex(0);
	}
	
	@Override
	protected String getStringPresentationName(int i) {
		return getSecondaryName();
	}

	@Override
	protected int getNextSecondaryPosition(int i, T trans) {
		return getPositionForIndex(i) + getPositionChange(trans);
	}

	@Override
	protected SymbolString[] assembleUpdatedStrings(SymbolString[] clones,
			T trans) {
		return new SymbolString[]{createUpdatedSecondary(clones[0], trans)};
	}

	protected abstract String getSecondaryName();

	protected abstract int getPositionChange(T trans);

	protected abstract SymbolString createUpdatedSecondary(SymbolString secClone,
			T trans);

}
