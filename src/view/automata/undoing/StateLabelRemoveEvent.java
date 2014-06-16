package view.automata.undoing;

import model.automata.State;
import model.undo.IUndoRedo;
import view.automata.Note;
import view.automata.editing.AutomatonEditorPanel;

public class StateLabelRemoveEvent extends EditingEvent{

	private State myState;
	private Note myNote;

	public StateLabelRemoveEvent (AutomatonEditorPanel panel, State s) {
		super(panel);
		myState = s;
		myNote = panel.getStateLabel(s);
	}
	
	@Override
	public boolean undo() {
		getPanel().addStateLabel(myState, myNote, myNote.getText());
		return true;
	}

	@Override
	public boolean redo() {
		getPanel().removeStateLabel(myState);
		return true;
	}

	@Override
	public String getName() {
		return "Remove State Label";
	}

}
