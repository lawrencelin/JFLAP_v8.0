package model.algorithms.testinput.simulate.configurations;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;

import model.automata.State;
import model.automata.SingleInputTransition;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.transducers.OutputFunction;
import model.automata.transducers.Transducer;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public abstract class InputOutputConfiguration<S extends Transducer<T>, T extends OutputFunction<T>> 
							extends SingleSecondaryConfiguration<S, FSATransition> {

	public InputOutputConfiguration(S a, State s, int pos, SymbolString input,
			SymbolString output) {
		super(a, s, pos, input, output);
	}

	public SymbolString getOutput() {
		return super.getSecondaryString();
	}

	@Override
	protected boolean shouldFindValidTransitions() {
		return true;
	}
//	
//	@Override
//	protected boolean isDone() {
//		return !hasNextState();
//	}
//	
	@Override
	protected boolean isInFinalState() {
		return !hasNextState();
	}

	@Override
	protected String getSecondaryName() {
		return "Output";
	}

	@Override
	protected int getPositionChange(FSATransition trans) {
		return getOutputForTransition(trans).size();
	}

	private SymbolString getOutputForTransition(
			FSATransition trans){
		return this.getAutomaton().getOutputFunctionSet().getOutputForTransition(trans);
	}

	@Override
	protected SymbolString createUpdatedSecondary(SymbolString secClone,
			FSATransition trans) {
		return secClone.concat(getOutputForTransition(trans));
	}

	
}
