package view.automata.undoing;

import model.automata.State;
import view.automata.Note;
import view.automata.editing.AutomatonEditorPanel;
import view.automata.editing.MooreEditorPanel;

public class MooreOutputRenameEvent extends NoteRenameEvent{

	private State myState;

	public MooreOutputRenameEvent(AutomatonEditorPanel panel, State s, Note n,
			String oldText) {
		super(panel, n, oldText);
		myState = s;
	}
	
	@Override
	public boolean undo() {
		boolean sup = super.undo();
		((MooreEditorPanel) getPanel()).moveOutputFunction(myState);
		return sup;
	}
	
	@Override
	public boolean redo() {
		boolean sup = super.redo();
		((MooreEditorPanel) getPanel()).moveOutputFunction(myState);
		return sup;
	}

}
