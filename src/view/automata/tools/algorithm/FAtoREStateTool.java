package view.automata.tools.algorithm;

import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import model.automata.State;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import view.automata.editing.AutomatonEditorPanel;
import view.automata.tools.StateTool;
import view.regex.FAToREController;

public class FAtoREStateTool extends StateTool<FiniteStateAcceptor, FSATransition> {

	private FAToREController myController;

	public FAtoREStateTool(
			AutomatonEditorPanel<FiniteStateAcceptor, FSATransition> panel,
			FAToREController controller) {
		super(panel, panel.getAutomaton());
		myController = controller;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(SwingUtilities.isLeftMouseButton(e)){
			State s = myController.stateCreate();
			if(s != null){
				setState(s);
				getPanel().moveState(s, e.getPoint());
			}
		}
		else
			super.mousePressed(e);
	}
}
