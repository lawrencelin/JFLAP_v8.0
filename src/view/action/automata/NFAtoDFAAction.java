package view.action.automata;

import java.awt.event.ActionEvent;

import debug.JFLAPDebug;
import model.algorithms.transform.fsa.NFAtoDFAConverter;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import universe.JFLAPUniverse;
import view.algorithms.transform.NFAtoDFAPanel;
import view.automata.editing.AutomatonEditorPanel;
import view.automata.views.AutomatonView;
import view.automata.views.FSAView;

public class NFAtoDFAAction extends AutomatonAction{

	public NFAtoDFAAction(FSAView view) {
		super("Convert to DFA", view);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		NFAtoDFAConverter convert = new NFAtoDFAConverter((FiniteStateAcceptor) getAutomaton());
		NFAtoDFAPanel panel = new NFAtoDFAPanel(getEditorPanel(), convert);
		
		JFLAPUniverse.getActiveEnvironment().addSelectedComponent(panel);
	}

}
