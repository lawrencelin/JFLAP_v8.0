package model.regex;

import universe.preferences.JFLAPPreferences;
import debug.JFLAPDebug;
import model.automata.InputAlphabet;
import model.automata.StartState;
import model.automata.State;
import model.automata.StateSet;
import model.automata.TransitionSet;
import model.automata.acceptors.FinalStateSet;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.automata.acceptors.fsa.FSATransition;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public class GeneralizedTransitionGraph extends FiniteStateAcceptor {

	private RegularExpression myRegEx;

	public GeneralizedTransitionGraph(RegularExpression regex) {
		myRegEx = regex;
		this.getInputAlphabet().addAll(myRegEx.getInputAlphabet());
		this.getInputAlphabet().addAll(myRegEx.getOperators());
		this.getInputAlphabet().add(JFLAPPreferences.getEmptySetSymbol());
		State start = this.getStates().createAndAddState();
		State end = this.getStates().createAndAddState();

		this.setStartState(start);
		this.getFinalStateSet().add(end);

		FSATransition trans = new FSATransition(start, 
				end, 
				myRegEx.getExpression());
		this.getTransitions().add(trans);
	}

	public GeneralizedTransitionGraph(FiniteStateAcceptor fa) {
		super(fa.getStates().copy(),
				fa.getInputAlphabet().copy(),
				fa.getTransitions().copy(),
				new StartState(),
				fa.getFinalStateSet().copy());
		this.setStartState(fa.getStartState().copy());
	}

	@Override
	public String getDescriptionName() {
		return "Generalized Transition Graph";
	}

	public FiniteStateAcceptor createNFAFromGTG(){
		StateSet states = (StateSet) this.getStates().copy();
		InputAlphabet inputAlph = (InputAlphabet) myRegEx.getInputAlphabet().copy();
		TransitionSet<FSATransition> transitions = 
				(TransitionSet<FSATransition>) this.getTransitions().copy();
		
		for (FSATransition t: transitions){
			if (isLambaTransition(t)){
				t.setInput(new SymbolString());
			}
		}
		
		StartState start = new StartState(stateHelper(states, 
				this.getStartState().getID()));
		FinalStateSet finalStates = new FinalStateSet();
		for (State s: this.getFinalStateSet()){
			finalStates.add(stateHelper(states, s.getID()));
		}
		FiniteStateAcceptor nfa = new FiniteStateAcceptor(states, 
				inputAlph, 
				transitions, 
				start, 
				finalStates);

		return nfa;
	}

	private boolean isLambaTransition(FSATransition t) {
		Symbol[] input = t.getInput();
		return input.length > 0 && input[0].equals(myRegEx.getOperators().getEmptySub());
	}

	public State stateHelper(StateSet states, int id) {
		return states.getStateWithID(id);
	}
}
