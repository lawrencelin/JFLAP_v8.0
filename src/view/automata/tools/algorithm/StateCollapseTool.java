package view.automata.tools.algorithm;


import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;

import model.algorithms.conversion.fatoregex.DFAtoRegularExpressionConverter;
import model.automata.State;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import view.automata.editing.AutomatonEditorPanel;
import view.automata.tools.EditingTool;
import view.regex.FAToREController;
import view.regex.TransitionWindow;

public class StateCollapseTool extends FAtoRETool{

	public StateCollapseTool(AutomatonEditorPanel<FiniteStateAcceptor, FSATransition> panel, FAToREController controller) {
		super(panel, controller);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		super.mousePressed(e);
		AutomatonEditorPanel<FiniteStateAcceptor, FSATransition> panel = getPanel();
		
		Object o = panel.objectAtPoint(e.getPoint());
		if(o instanceof State){
			getController().stateCollapse((State) o);
		}
	}
	@Override
	public String getToolTip() {
		return "State Collapser";
	}

	@Override
	public char getActivatingKey() {
		return 'o';
	}

	@Override
	public String getImageURLString() {
		return "/ICON/state_collapse.gif";
	}

}
