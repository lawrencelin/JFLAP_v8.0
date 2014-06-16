package view.automata.tools.algorithm;

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Set;

import javax.swing.JOptionPane;

import model.algorithms.transform.fsa.NFAtoDFAConverter;
import model.automata.State;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import util.Point2DAdv;
import view.automata.editing.AutomatonEditorPanel;
import view.automata.tools.EditingTool;
import errors.BooleanWrapper;

public class StateExpanderTool extends EditingTool<FiniteStateAcceptor, FSATransition>{

	private NFAtoDFAConverter myAlg;

	public StateExpanderTool(
			AutomatonEditorPanel<FiniteStateAcceptor, FSATransition> panel, NFAtoDFAConverter convert) {
		super(panel);
		myAlg = convert;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		AutomatonEditorPanel<FiniteStateAcceptor, FSATransition> panel = getPanel();
		Object o = panel.objectAtPoint(e.getPoint());
		if(o instanceof State){
			Set<State> existing = myAlg.getDFA().getStates().copy();
			BooleanWrapper wrap = myAlg.expandState((State) o);
			
			if(wrap.isError())
				JOptionPane.showMessageDialog(panel, wrap.getMessage());
			else{
				layoutNewStates(panel, existing);
			}
		}
	}

	private void layoutNewStates(
			AutomatonEditorPanel<FiniteStateAcceptor, FSATransition> panel,
			Set<State> existing) {
		Set<State> newStates = myAlg.getDFA().getStates().copy();
		newStates.removeAll(existing);
		
		Point2D center = new Point2DAdv(panel.getWidth()/2, panel.getHeight()/2);
		for(State s : newStates)
			panel.moveState(s, center);
		panel.layoutGraph(existing);
	}
	@Override
	public String getToolTip() {
		return "State Expander";
	}

	@Override
	public char getActivatingKey() {
		return 's';
	}

	@Override
	public String getImageURLString() {
		return "/ICON/state_expander.gif";
	}

}
