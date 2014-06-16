package view.automata.undoing;

import view.automata.editing.AutomatonEditorPanel;
import model.automata.Automaton;
import model.automata.Transition;
import model.undo.IUndoRedo;

public abstract class EditingEvent<T extends Automaton<S>, S extends Transition<S>> implements IUndoRedo{

	private AutomatonEditorPanel<T, S> myPanel;
	
	public EditingEvent(AutomatonEditorPanel<T, S> panel){
		myPanel = panel;
	}
	
	public AutomatonEditorPanel<T, S> getPanel(){
		return myPanel;
	}
}
