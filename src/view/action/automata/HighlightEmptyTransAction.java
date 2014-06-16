package view.action.automata;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JLabel;
import javax.swing.JPanel;

import model.automata.Automaton;
import model.automata.State;
import model.automata.Transition;
import model.automata.TransitionSet;
import model.undo.UndoKeeper;
import universe.JFLAPUniverse;
import universe.preferences.JFLAPPreferences;
import util.JFLAPConstants;
import view.automata.AutomatonDisplayPanel;
import view.automata.editing.AutomatonEditorPanel;
import view.automata.views.AutomatonView;

public class HighlightEmptyTransAction extends AutomatonAction {

	public HighlightEmptyTransAction(AutomatonView view) {
		super("Highlight "+JFLAPPreferences.getEmptyString()+"-Transitions", view);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		AutomatonEditorPanel panel = getEditorPanel();
		Automaton auto = panel.getAutomaton();
		TransitionSet<? extends Transition> set = auto.getTransitions();
	
		Set<Transition> lambda = new TreeSet<Transition>();

		for(Transition t : set){
			if(t.isLambdaTransition())
				lambda.add(t);
		}
		
		JFLAPUniverse.getActiveEnvironment().addSelectedComponent(new LambdaPanel(panel, lambda));
	}

	private class LambdaPanel extends AutomatonDisplayPanel{

		public LambdaPanel(AutomatonEditorPanel editor, Set<Transition> lambdas){
			super(editor, editor.getAutomaton(), JFLAPPreferences.getEmptyString() + "-Transitions");
			add(new JLabel(JFLAPPreferences.getEmptyString()+"-transitions are highlighted."),
					BorderLayout.NORTH);
			
			AutomatonEditorPanel panel = getEditorPanel();
			panel.selectAll(lambdas);
			
			for(Transition t : lambdas)
				panel.selectObject(new State[]{t.getFromState(), t.getToState()});
			updateSize();
		}
	}
}
