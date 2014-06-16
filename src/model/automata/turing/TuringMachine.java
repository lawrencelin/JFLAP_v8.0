package model.automata.turing;

import java.util.Arrays;
import java.util.Collection;

import debug.JFLAPDebug;

import model.automata.InputAlphabet;
import model.automata.StartState;
import model.automata.StateSet;
import model.automata.Transition;
import model.automata.TransitionSet;
import model.automata.acceptors.Acceptor;
import model.automata.acceptors.FinalStateSet;
import model.change.events.AdvancedChangeEvent;
import model.formaldef.rules.applied.TuringMachineBlankRule;
import model.symbols.Symbol;

public abstract class TuringMachine<T extends Transition<T>> extends Acceptor<T>{


	private BlankSymbol myBlank;

	public TuringMachine(StateSet states,
							TapeAlphabet tapeAlph,
							BlankSymbol blank,
							InputAlphabet inputAlph,
							TransitionSet<T> functions,
							StartState start,
							FinalStateSet finalStates) {
		super(states, tapeAlph, blank, inputAlph, functions, start, finalStates);
		setBlankSymbol(blank);
	}


	public Symbol getBlankSymbol() {
		BlankSymbol blank = getComponentOfClass(BlankSymbol.class);
		return blank == null ? null : blank.getSymbol();
	}


	public void setBlankSymbol(BlankSymbol blank) {
		myBlank = blank;
		this.getTapeAlphabet().add(blank.getSymbol());
		this.getTapeAlphabet().addRules(new TuringMachineBlankRule(myBlank));
	}


	public TapeAlphabet getTapeAlphabet() {
		return getComponentOfClass(TapeAlphabet.class);
	}



	@Override
	public void componentChanged(AdvancedChangeEvent event) {
		if (event.comesFrom(TapeAlphabet.class) && event.getType() == ITEM_REMOVED){
			InputAlphabet input = this.getInputAlphabet();
			Collection<Symbol> s = (Collection<Symbol>) event.getArg(0);
			input.removeAll(s);
		} else if (event.comesFrom(InputAlphabet.class) && event.getType() == ITEM_ADDED){
			TapeAlphabet tape = this.getTapeAlphabet();
			Collection<Symbol> s = (Collection<Symbol>) event.getArg(0);
			tape.addAll(s);
		}
		super.componentChanged(event);
	}
}
