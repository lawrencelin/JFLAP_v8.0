package view.automata.undoing;

import view.automata.Note;
import view.automata.editing.AutomatonEditorPanel;

public class NoteAddEvent extends SingleNoteEvent{

	public NoteAddEvent(AutomatonEditorPanel panel, Note n){
		super(panel, n);
	}
	
	@Override
	public boolean undo() {
		AutomatonEditorPanel panel = getPanel();
		panel.removeNote(getNote());
		return true;
	}

	@Override
	public boolean redo() {
		AutomatonEditorPanel panel = getPanel();
		Note note = getNote();
		
		boolean sup = super.redo();
		panel.addNote(note);
		note.setSelectionStart(0);
		panel.editNote(note);
		return sup;
	}

	@Override
	public String getName() {
		return "Note creation event";
	}

}
