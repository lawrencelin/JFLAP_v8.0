package view.action.automata;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import model.automata.AutomatonException;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.graph.FSAEqualityChecker;
import universe.JFLAPUniverse;
import view.automata.editing.AutomatonEditorPanel;
import view.automata.views.FSAView;
import view.environment.JFLAPEnvironment;

public class DFAEqualityAction extends AutomatonAction {
	
	private FSAEqualityChecker myChecker; 

	public DFAEqualityAction(FSAView view) {
		super("Compare Equivalence", view);
		myChecker = new FSAEqualityChecker();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		AutomatonEditorPanel<FiniteStateAcceptor, FSATransition> panel = getEditorPanel();
		FiniteStateAcceptor auto = panel.getAutomaton();
		
		if(auto.getStartState() == null)
			throw new AutomatonException("This automaton has no initial state!");
		
		JFLAPEnvironment[] enviros = JFLAPUniverse.getRegistry().toArray(new JFLAPEnvironment[0]);
		JFLAPEnvironment active = JFLAPUniverse.getActiveEnvironment();
		JComboBox<JFLAPEnvironment> combo = new JComboBox<JFLAPEnvironment>();
		
		for(int i=0; i < enviros.length; i++){
			JFLAPEnvironment e = enviros[i];
			Component primary = e.getPrimaryView();
			
			if(!e.equals(active) && isValid(primary))
				combo.addItem(e);
		}
		
		if (combo.getItemCount() == 0)
			throw new AutomatonException("No other FAs around!");
		
		int result = JOptionPane.showOptionDialog(active, combo, "Compare against FA",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
				null, null, null);
		if (result != JOptionPane.OK_OPTION) return;
		
		FSAView otherView = (FSAView)((JFLAPEnvironment) combo.getSelectedItem()).getPrimaryView();
		FiniteStateAcceptor other = otherView.getDefinition();
		
		if(other.getStartState() == null)
			throw new AutomatonException("That automaton has no initial state!");
		//TODO: do we need these commented out removals?
//		other = (FiniteStateAutomaton) UselessStatesDetector
//				.cleanAutomaton(other);
//		automaton = (FiniteStateAutomaton) UselessStatesDetector
//				.cleanAutomaton(automaton);
	
		String check = myChecker.equals(auto, other) ? "They ARE equivalent!"
				: "They AREN'T equivalent!";
		JOptionPane.showMessageDialog(active, check);
	}
	
	private boolean isValid(Component c){
		return c instanceof FSAView;
	}

}
