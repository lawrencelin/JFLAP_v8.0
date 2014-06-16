package model.automata.transducers;

import java.lang.reflect.InvocationTargetException;

import model.automata.Automaton;
import model.automata.InputAlphabet;
import model.automata.StartState;
import model.automata.StateSet;
import model.automata.TransitionSet;
import model.automata.acceptors.fsa.FSATransition;
import model.change.events.AdvancedChangeEvent;
import model.formaldef.FormalDefinition;
import model.formaldef.components.FormalDefinitionComponent;
import model.symbols.Symbol;

public abstract class Transducer<T extends OutputFunction<T>> extends Automaton<FSATransition> {


	public Transducer(StateSet states, 
					InputAlphabet langAlph,
					OutputAlphabet outputAlph,
					TransitionSet<FSATransition> functions, 
					StartState start,
					OutputFunctionSet<T> outputFunctions) {
		super(states, langAlph, outputAlph, functions, start, outputFunctions);
	}

	@Override
	public Transducer alphabetAloneCopy() {
		Class<Transducer> clz = (Class<Transducer>) this.getClass();
		try {
					return clz.cast(clz.getConstructors()[0].newInstance(new StateSet()	,																		this.getInputAlphabet(),
																			this.getOutputAlphabet(),
																			new TransitionSet<FSATransition>(),
																			new StartState(),
																			new OutputFunctionSet()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public OutputAlphabet getOutputAlphabet() {
		return this.getComponentOfClass(OutputAlphabet.class);
	}
	
	public OutputFunctionSet<T> getOutputFunctionSet(){
		return this.getComponentOfClass(OutputFunctionSet.class);
	}
	
	@Override
	public Transducer copy() {
		Class<Transducer> clz = (Class<Transducer>) this.getClass();
		try {StartState start = getStartState() == null ? new StartState() : new StartState(getStartState());
			
					return clz.cast(clz.getConstructors()[0].newInstance(this.getStates().copy(),
																			this.getInputAlphabet().copy(),
																			this.getOutputAlphabet().copy(),
																			this.getTransitions().copy(),
																			start,
																			this.getOutputFunctionSet().copy()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	

}
