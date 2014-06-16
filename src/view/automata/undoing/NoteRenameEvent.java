package view.automata.undoing;

import debug.JFLAPDebug;
import view.automata.Note;
import view.automata.editing.AutomatonEditorPanel;
import model.undo.IUndoRedo;

public class NoteRenameEvent extends SingleNoteEvent{

	private String myOldText;

	public NoteRenameEvent(AutomatonEditorPanel panel, Note n, String oldText){
		super(panel, n);
		myOldText = oldText;
	}
	
	@Override
	public boolean undo() {
		getPanel().setNoteText(getNote(), myOldText);
		return true;
		}

	@Override
	public boolean redo() {
		getPanel().setNoteText(getNote(), getToString());
		return true;
	}

	@Override
	public String getName() {
		return "Rename Note";
	}

}
