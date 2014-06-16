package view.action.automata;

import javax.swing.AbstractAction;

import model.automata.Automaton;
import model.automata.Transition;
import view.automata.editing.AutomatonEditorPanel;
import view.automata.views.AutomatonView;

public abstract class AutomatonAction extends AbstractAction{

	public static final int PADDING = 150;
	private AutomatonView myView;

	public AutomatonAction(String name, AutomatonView view){
		super(name);
		myView = view;
	}
	
	public AutomatonView getView() {
		return myView;
	}
	
	public AutomatonEditorPanel getEditorPanel() {
		return (AutomatonEditorPanel) myView.getCentralPanel();
	}
	
	public Automaton getAutomaton() {
		return myView.getDefinition();
	}
}
