package model.automata.acceptors;

import java.util.Collection;

import javax.swing.event.ChangeEvent;

import debug.JFLAPDebug;

import model.automata.Automaton;
import model.automata.InputAlphabet;
import model.automata.StartState;
import model.automata.State;
import model.automata.StateSet;
import model.automata.SingleInputTransition;
import model.automata.Transition;
import model.automata.TransitionSet;
import model.change.events.AdvancedChangeEvent;
import model.formaldef.FormalDefinition;
import model.formaldef.FormalDefinitionException;
import model.formaldef.components.FormalDefinitionComponent;

public abstract class Acceptor<T extends Transition<T>> extends Automaton<T> {


	public Acceptor(FormalDefinitionComponent ...comps) {
		super(comps);
	}
	
	public FinalStateSet getFinalStateSet(){
		return getComponentOfClass(FinalStateSet.class);
	}
	
	public static boolean isFinalState(Acceptor a, State s){
		return ((Acceptor) a).getFinalStateSet().contains(s);
	}
	
	@Override
	public void componentChanged(AdvancedChangeEvent event) {
		if (event.comesFrom(FinalStateSet.class) &&
				event.getType() == ITEM_ADDED){
			Collection<State> added = (Collection<State>) event.getArg(0);
			StateSet states = this.getStates();
			for (State s: added){
				if (!states.contains(s))
					states.add(s);
			}
//			return;	
		}
		else if (event.comesFrom(this.getStates()) &&
				event.getType() == ITEM_REMOVED){
			Collection<State> removed = (Collection<State>) event.getArg(0);

			FinalStateSet finalStates = this.getFinalStateSet();
			for (State s: removed){
				if (finalStates.contains(s))
					finalStates.remove(s);
			}
				
		}
		
		super.componentChanged(event);
	}
}
