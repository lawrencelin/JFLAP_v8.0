package model.automata.acceptors.fsa;

import debug.JFLAPDebug;
import model.automata.InputAlphabet;
import model.automata.StartState;
import model.automata.State;
import model.automata.StateSet;
import model.automata.TransitionSet;
import model.automata.acceptors.Acceptor;
import model.automata.acceptors.FinalStateSet;
import model.formaldef.FormalDefinition;
import model.formaldef.components.FormalDefinitionComponent;

public class FiniteStateAcceptor extends Acceptor<FSATransition> {

	public FiniteStateAcceptor(StateSet states, 
									InputAlphabet langAlph,
									TransitionSet<FSATransition> functions, 
									StartState start,
									FinalStateSet finalStates) {
		super(states, langAlph, functions, start, finalStates);
	}

	public FiniteStateAcceptor() {
		this(new StateSet(),
				new InputAlphabet(),
				new TransitionSet<FSATransition>(),
				new StartState(),
				new FinalStateSet());
	}

	@Override
	public String getDescriptionName() {
		return "Finite State Automaton (FSA)";
	}

	@Override
	public String getDescription() {
		return "A finite state automaton, basic accepter with no memory.";
	}

	@Override
	public FiniteStateAcceptor alphabetAloneCopy() {
		return new FiniteStateAcceptor(new StateSet(), 
						this.getInputAlphabet().copy(), 
						new TransitionSet<FSATransition>(), 
						new StartState(), 
						new FinalStateSet());
	}

	public static boolean hasAllSingleSymbolInput(FiniteStateAcceptor dfa) {
		for (FSATransition trans : dfa.getTransitions()){
			if (trans.getInput().length > 1){
				return false;
			}
		}
		return true;
	}
	
	@Override
	public FiniteStateAcceptor copy() {
		StartState start = getStartState() == null ? new StartState() : new StartState(getStartState().copy());
		return new FiniteStateAcceptor(this.getStates().copy(),
				this.getInputAlphabet().copy(),
				this.getTransitions().copy(),
				start,
				this.getFinalStateSet().copy());
	}

	@Override
	public FSATransition createBlankTransition(State from, State to) {
		return new FSATransition(from, to);
	}

	

}
