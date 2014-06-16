package view.automata.tools.algorithm;

import java.awt.event.MouseEvent;

import model.automata.acceptors.fsa.FSATransition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import view.automata.editing.AutomatonEditorPanel;
import view.automata.tools.EditingTool;
import view.regex.FAToREController;

public class TransitionCollapseTool extends FAtoRETool{

	public TransitionCollapseTool(
			AutomatonEditorPanel<FiniteStateAcceptor, FSATransition> panel,
			FAToREController controller) {
		super(panel, controller);
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		super.mousePressed(e);
		Object o = getPanel().objectAtPoint(e.getPoint());
		if(o instanceof FSATransition)
			getController().transitionCollapse((FSATransition) o);
	}

	@Override
	public String getToolTip() {
		return "Transition Collapser";
	}

	@Override
	public char getActivatingKey() {
		return 'c';
	}

	@Override
	public String getImageURLString() {
		return "/ICON/collapse.gif";
	}

}
