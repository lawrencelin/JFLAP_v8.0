package model.automata.determinism;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import model.automata.Automaton;
import model.automata.State;
import model.automata.Transition;

public abstract class DeterminismChecker<T extends Transition<T>> {

	
	public DeterminismChecker(){
	}

	
	public boolean isDeterministic(Automaton<T> m){
		return this.getNondeterministicStates(m).length == 0;
	}


	public State[] getNondeterministicStates(Automaton<T> m) {
		
		Collection<State> states = new ArrayList<State>();
		for (State s: m.getStates()){
			if (isNondeterministic(s, m)){
				states .add(s);
			}
		}
		
		return states.toArray(new State[0]);
	}


	private boolean isNondeterministic(State s, Automaton<T> m) {
		return !getNondeterministicTransitionsForState(s,m).isEmpty();
	}

	public Collection<T> getAllNondeterministicTransitions(Automaton<T> m){
		Collection<T> trans = new ArrayList<T>();
		for (State s: m.getStates()){
			trans.addAll(getNondeterministicTransitionsForState(s, m));
		}
		
		return trans;
	}
	
	private Collection<T> getNondeterministicTransitionsForState(State s,
			Automaton<T> m) {
		
		
		List<T> from = new ArrayList<T>(m.getTransitions().getTransitionsFromState(s));
		Set<T> nonDet = new TreeSet<T>();
		
		for (int i=0; i < from.size(); i++){
			T trans = from.get(i);
			
			if(trans.isLambdaTransition())
				nonDet.add(trans);
			else
				for(int j = i+1; j < from.size(); j++){
					T trans2 = from.get(j);
					if(areNondeterministic(trans, trans2)){
						nonDet.add(trans);
						nonDet.add(trans2);
					}
				}
		}
		
		return nonDet;
	}
	
	protected abstract boolean areNondeterministic(T trans1, T trans2);
}
