package view.automata.tools.algorithm;

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Set;

import javax.swing.JOptionPane;

import model.algorithms.transform.turing.StayOptionRemover;
import model.automata.State;
import model.automata.turing.MultiTapeTMTransition;
import model.automata.turing.MultiTapeTuringMachine;
import model.graph.TransitionGraph;
import util.arrows.GeometryHelper;
import view.automata.editing.AutomatonEditorPanel;

public class TransitionSelectionTool extends NonTransitionArrowTool<MultiTapeTuringMachine, MultiTapeTMTransition>{

	private StayOptionRemover myAlg;

	public TransitionSelectionTool(AutomatonEditorPanel<MultiTapeTuringMachine, MultiTapeTMTransition> panel,
			StayOptionRemover remove) {
		super(panel, panel.getAutomaton());
		myAlg = remove;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		super.mousePressed(e);
		
		AutomatonEditorPanel<MultiTapeTuringMachine, MultiTapeTMTransition> panel = getPanel();
		TransitionGraph<MultiTapeTMTransition> graph = panel.getGraph();
		Object clicked = panel.objectAtPoint(e.getPoint());
		
		if(clicked instanceof MultiTapeTMTransition){
			MultiTapeTuringMachine machine = panel.getAutomaton();
			Set<State> existing = machine.getStates().copy();
			MultiTapeTMTransition trans = (MultiTapeTMTransition) clicked;
			
			if(myAlg.replaceStayTransition(trans)){
				Set<State> added = machine.getStates().copy();
				added.removeAll(existing);

				for(State s : added){
					State from = trans.getFromState(), to = trans.getToState();
					Point2D p = graph.getDefaultControlPoint(from, to);
					panel.moveState(s, p);
				}
			}
			else
				JOptionPane.showMessageDialog(panel, "Not a valid transition!");
		}
	}

}
