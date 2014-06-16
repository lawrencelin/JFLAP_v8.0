package view.action.automata;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import debug.JFLAPDebug;
import model.algorithms.transform.fsa.AddTrapStateAlgorithm;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.automata.determinism.FSADeterminismChecker;
import universe.JFLAPUniverse;
import view.algorithms.transform.TrapStatePanel;
import view.automata.views.FSAView;
import view.environment.JFLAPEnvironment;

public class TrapStateAction extends AutomatonAction{

	public TrapStateAction(FSAView view) {
		super("Add Trap State to DFA", view);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		FiniteStateAcceptor auto = (FiniteStateAcceptor) getAutomaton();
		JFLAPEnvironment env = JFLAPUniverse.getActiveEnvironment();
		
		if (auto.getStartState() == null) {
			JOptionPane.showMessageDialog(env,
					"The automaton should have " + "an initial state.");
			return;
		}
		
		FSADeterminismChecker checker = new FSADeterminismChecker();
		if (!checker.isDeterministic(auto)) {
			JOptionPane.showMessageDialog(env, "This isn't a DFA!");
			return;
		}
		
		boolean isNeeded = AddTrapStateAlgorithm.trapStateNeeded(auto);
		if (!isNeeded)
		{
			JOptionPane.showMessageDialog(env, "DFA is complete. No need for the Trap State");
			return;
		}
		AddTrapStateAlgorithm alg = new AddTrapStateAlgorithm(auto);
		TrapStatePanel trapPane = new TrapStatePanel(getEditorPanel(), alg);
		env.addSelectedComponent(trapPane);
	}
}
