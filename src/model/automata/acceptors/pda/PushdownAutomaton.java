package model.automata.acceptors.pda;

import java.util.HashSet;

import debug.JFLAPDebug;
import model.automata.InputAlphabet;
import model.automata.StartState;
import model.automata.State;
import model.automata.StateSet;
import model.automata.TransitionSet;
import model.automata.acceptors.Acceptor;
import model.automata.acceptors.FinalStateSet;
import model.change.events.AdvancedChangeEvent;
import model.formaldef.FormalDefinition;
import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.alphabets.AlphabetException;
import model.formaldef.components.alphabets.grouping.SpecialSymbolFactory;
import model.formaldef.rules.applied.BottomOfStackSymbolRule;
import model.symbols.Symbol;

public class PushdownAutomaton extends Acceptor<PDATransition> {


	private BottomOfStackSymbol myBotOfStackSymbol;

	public PushdownAutomaton(StateSet states, 
								InputAlphabet inputAlph,
								StackAlphabet stackAlph,
								TransitionSet<PDATransition> functions, 
								StartState start,
								BottomOfStackSymbol bottom,
								FinalStateSet finalStates) {
		super(states, inputAlph, stackAlph, functions, start, bottom, finalStates);
		this.getStackAlphabet().addRules(new BottomOfStackSymbolRule(bottom));
		myBotOfStackSymbol = bottom;
		setBottomOfStackSymbol(bottom.getSymbol());
		
	}
	
	public PushdownAutomaton() {
		this(new StateSet(), 
				new InputAlphabet(),
				new StackAlphabet(),
				new TransitionSet<PDATransition>(), 
				new StartState(), 
				new BottomOfStackSymbol(SpecialSymbolFactory.getReccomendedBOSSymbol(new StackAlphabet())),
				new FinalStateSet());
		
	}

	@Override
	public String getDescriptionName() {
		return "Pushdown Automaton (PDA)";
	}

	@Override
	public String getDescription() {
		return "An variety of automaton, more complex than a finite state automaton. " +
				"Uses a single stack as memory, allowinf for more complex languages. " +
				"The kind of language defined by a PDA is a Context Free Language (CFL).";
	}

	@Override
	public PushdownAutomaton alphabetAloneCopy() {
		return new PushdownAutomaton(new StateSet(),
										this.getInputAlphabet().copy(), 
										this.getStackAlphabet().copy(), 
										new TransitionSet<PDATransition>(), 
										new StartState(), 
										new BottomOfStackSymbol(), 
										new FinalStateSet());
	}

	public Symbol getBottomOfStackSymbol() {
		return this.getComponentOfClass(BottomOfStackSymbol.class).getSymbol();
	}
	
	public void setBottomOfStackSymbol(Symbol s){
		StackAlphabet stackALph = this.getStackAlphabet();
	
		if (s != null && (!stackALph.contains(s) && !stackALph.add(s)))
			return;
		
		myBotOfStackSymbol.setSymbol(s);
	}

	public StackAlphabet getStackAlphabet() {
		return getComponentOfClass(StackAlphabet.class);
	}

	@Override
	public PushdownAutomaton copy() {
		return new PushdownAutomaton(this.getStates().copy(),
				this.getInputAlphabet().copy(), 
				this.getStackAlphabet().copy(), 
				this.getTransitions().copy(), 
				new StartState(getStartState() == null ? null : this.getStartState().copy()), 
				new BottomOfStackSymbol(this.getBottomOfStackSymbol()), 
				this.getFinalStateSet().copy());
	}

	@Override
	public PDATransition createBlankTransition(State from, State to) {
		return new PDATransition(from, to);
	}
	
	

}
