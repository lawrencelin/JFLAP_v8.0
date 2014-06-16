package view.automata.views;

import model.automata.acceptors.fsa.FSATransition;
import model.automata.transducers.OutputFunction;
import model.automata.transducers.Transducer;

public abstract class TransducerView<T extends Transducer<S>, S extends OutputFunction<S>> extends AutomatonView<T, FSATransition>{

	public TransducerView(T model) {
		super(model);
		// TODO Auto-generated constructor stub
	}

}
