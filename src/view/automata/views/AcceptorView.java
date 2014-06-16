package view.automata.views;

import model.automata.Automaton;
import model.automata.Transition;
import model.automata.acceptors.Acceptor;

public abstract class AcceptorView<T extends Acceptor<S>, S extends Transition<S>> extends AutomatonView<T, S>{

	public AcceptorView(T model) {
		super(model);
	}
}
