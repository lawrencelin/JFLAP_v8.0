package view.action.automata;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.Collection;

import javax.swing.JLabel;

import model.automata.Automaton;
import model.automata.State;
import model.automata.Transition;
import model.automata.determinism.DeterminismChecker;
import model.automata.determinism.DeterminismCheckerFactory;
import universe.JFLAPUniverse;
import view.automata.AutomatonDisplayPanel;
import view.automata.editing.AutomatonEditorPanel;
import view.automata.views.AutomatonView;

public class HighlightNondeterminismAction extends AutomatonAction {

	private DeterminismChecker myChecker;

	public HighlightNondeterminismAction(AutomatonView view) {
		super("Highlight Nondeterminism", view);
		Automaton auto = (Automaton) view.getDefinition();
		myChecker = DeterminismCheckerFactory.getChecker(auto);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		AutomatonEditorPanel panel = getEditorPanel();		
		NondeterminismPanel np = new NondeterminismPanel(panel);
		JFLAPUniverse.getActiveEnvironment().addSelectedComponent(np);
		
	}
	
	private class NondeterminismPanel extends AutomatonDisplayPanel{

		public NondeterminismPanel(AutomatonEditorPanel editor) {
			super(editor, editor.getAutomaton(), "Nondeterminism");
			add(new JLabel("Nondeterministic states and transitions are highlighted."),
					BorderLayout.NORTH);
			
			Automaton auto = editor.getAutomaton();
			State[] states = myChecker.getNondeterministicStates(auto);
			Collection<Transition> trans = myChecker.getAllNondeterministicTransitions(auto);
			
			AutomatonEditorPanel panel = getEditorPanel();
			panel.selectAll(trans);
			
			for(Transition t : trans){
				panel.selectObject(new State[]{t.getFromState(), t.getToState()});
			}
			panel.selectAll(Arrays.asList(states));
			updateSize();
		}

		
	}

}
