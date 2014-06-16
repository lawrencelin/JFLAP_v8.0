package view.automata.tools.algorithm;

import model.automata.acceptors.fsa.FSATransition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import view.automata.editing.AutomatonEditorPanel;
import view.automata.tools.EditingTool;
import view.regex.FAToREController;

public abstract class FAtoRETool extends EditingTool<FiniteStateAcceptor, FSATransition>{

	private FAToREController myController;

	public FAtoRETool(
			AutomatonEditorPanel<FiniteStateAcceptor, FSATransition> panel, FAToREController controller) {
		super(panel);
		myController = controller;
	}
	
	public FAToREController getController() {
		return myController;
	}

}
