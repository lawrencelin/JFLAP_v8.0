package view.action.automata;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import model.algorithms.transform.turing.StayOptionRemover;
import model.automata.TransitionSet;
import model.automata.turing.MultiTapeTMTransition;
import model.automata.turing.MultiTapeTuringMachine;
import model.automata.turing.TuringMachineMove;
import universe.JFLAPUniverse;
import view.algorithms.transform.StayOptionRemovalPanel;
import view.automata.views.AutomatonView;
import view.automata.views.MultiTapeTMView;
import view.environment.JFLAPEnvironment;

public class StayOptionRemoveAction extends AutomatonAction {

	public StayOptionRemoveAction(MultiTapeTMView view) {
		super("Remove Stay Transitions", view);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		MultiTapeTuringMachine machine = (MultiTapeTuringMachine) getAutomaton();
		StayOptionRemover remover = new StayOptionRemover(machine);
		JFLAPEnvironment env = JFLAPUniverse.getActiveEnvironment();

		TransitionSet<MultiTapeTMTransition> transitions = machine
				.getTransitions();
		boolean stay = false;

		for (MultiTapeTMTransition t : transitions)
			if (t.getMove(0) == TuringMachineMove.STAY)
				stay = true;
		if (!stay)
			JOptionPane.showMessageDialog(env,
					"This TM has no Stay transitions!");
		else {
			StayOptionRemovalPanel panel = new StayOptionRemovalPanel(
					getEditorPanel(), remover);
			env.addSelectedComponent(panel);
		}
	}

}
