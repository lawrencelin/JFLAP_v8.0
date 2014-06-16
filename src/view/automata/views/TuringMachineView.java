package view.automata.views;

import javax.swing.JComponent;

import model.automata.Transition;
import model.automata.turing.TuringMachine;
import model.undo.UndoKeeper;

public abstract class TuringMachineView<T extends TuringMachine<S>, S extends Transition<S>> extends AcceptorView<T, S> {

	public TuringMachineView(T model) {
		super(model);
	}


}
