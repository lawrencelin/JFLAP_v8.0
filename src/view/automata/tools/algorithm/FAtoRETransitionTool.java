package view.automata.tools.algorithm;

import java.awt.event.MouseEvent;

import model.automata.State;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import view.automata.editing.AutomatonEditorPanel;
import view.automata.tools.TransitionTool;
import view.regex.FAToREController;

public class FAtoRETransitionTool extends TransitionTool<FiniteStateAcceptor, FSATransition> {

	private FAToREController myController;

	public FAtoRETransitionTool(
			AutomatonEditorPanel<FiniteStateAcceptor, FSATransition> panel, FAToREController controller) {
		super(panel);
		myController = controller;
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		if (hasFrom()) {
			AutomatonEditorPanel<FiniteStateAcceptor, FSATransition> panel = getPanel();
			Object obj = panel.objectAtPoint(e.getPoint());
			
			if (obj instanceof State) {
				myController.transitionCreate(getFrom(), (State) obj);
			}
			clear();
		}
	}

}
