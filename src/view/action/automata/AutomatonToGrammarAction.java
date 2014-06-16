package view.action.automata;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import model.algorithms.conversion.autotogram.AutomatonToGrammarConversion;
import model.algorithms.conversion.autotogram.VariableMapping;
import model.automata.Automaton;
import model.automata.Transition;
import universe.JFLAPUniverse;
import view.algorithms.conversion.autotogram.AutoToGramConversionPanel;
import view.automata.views.AutomatonView;
import view.environment.JFLAPEnvironment;

public abstract class AutomatonToGrammarAction<T extends Automaton<S>, S extends Transition<S>, R extends VariableMapping>
		extends AutomatonAction {

	public AutomatonToGrammarAction(AutomatonView<T, S> view) {
		super("Convert to Grammar", view);
	}

	public abstract AutoToGramConversionPanel<T, S, R> createConversionPanel();

	@Override
	public void actionPerformed(ActionEvent e) {
		JFLAPEnvironment env = JFLAPUniverse.getActiveEnvironment();
		T auto = (T) getAutomaton();

		if (auto.getStartState() == null) {
			JOptionPane.showMessageDialog(env,
					"There must be an initial state!", "No Initial State",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		AutoToGramConversionPanel<T, S, R> convert = createConversionPanel();
		env.addSelectedComponent(convert);
	}

}
