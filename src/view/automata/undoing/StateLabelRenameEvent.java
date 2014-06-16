package view.automata.undoing;

import model.automata.State;
import view.automata.Note;
import view.automata.editing.AutomatonEditorPanel;

public class StateLabelRenameEvent extends NoteRenameEvent {
	
	private State myState;

	public StateLabelRenameEvent(AutomatonEditorPanel panel, State s, String oldText) {
		super(panel, panel.getStateLabel(s), oldText);
		myState = s;
	}
	
	@Override
	public boolean redo() {
		boolean sup = super.redo();
		getPanel().moveStateLabel(myState);
		return sup;
	}
	
	@Override
	public boolean undo() {
		boolean sup = super.undo();
		getPanel().moveStateLabel(myState);
		return sup;
	}

}
