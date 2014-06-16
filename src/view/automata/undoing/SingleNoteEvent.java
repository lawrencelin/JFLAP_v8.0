package view.automata.undoing;

import view.automata.Note;
import view.automata.editing.AutomatonEditorPanel;

public abstract class SingleNoteEvent extends EditingEvent {

	private Note myNote;
	private String myString;

	public SingleNoteEvent(AutomatonEditorPanel panel, Note n) {
		super(panel);
		myNote = n;
		myString = n.getText();
	}

	@Override
	public boolean redo() {
		return setText();
	}
	
	public Note getNote () {
		return myNote;
	}
	
	public String getToString() {
		return myString;
	}
	
	public boolean setText () {
		myNote.setText(myString);
		return true;
	}

}
