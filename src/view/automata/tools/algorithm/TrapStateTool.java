package view.automata.tools.algorithm;

import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import model.algorithms.transform.fsa.AddTrapStateAlgorithm;
import model.automata.State;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import universe.JFLAPUniverse;
import view.automata.editing.AutomatonEditorPanel;
import view.automata.tools.StateTool;

public class TrapStateTool extends StateTool<FiniteStateAcceptor, FSATransition> {

	private AddTrapStateAlgorithm myAlg;

	public TrapStateTool(
			AutomatonEditorPanel<FiniteStateAcceptor, FSATransition> panel, AddTrapStateAlgorithm alg) {
		super(panel, alg.getDFAWithTrapState());
		myAlg = alg;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if(SwingUtilities.isRightMouseButton(e))
			super.mousePressed(e);
		else if(!myAlg.hasTrapState()){
			super.mousePressed(e);
			FiniteStateAcceptor dfa = myAlg.getDFAWithTrapState();
			State s = getState();
			myAlg.addStateAsTrapState(s);
		} else
			JOptionPane.showMessageDialog(JFLAPUniverse.getActiveEnvironment(),
				"That action is inappropriate for this step!", "Out of Order",
				JOptionPane.ERROR_MESSAGE);
	}


}
