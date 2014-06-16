package view.automata.undoing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import model.undo.IUndoRedo;
import view.automata.Note;
import view.automata.editing.AutomatonEditorPanel;

public class NoteRemoveEvent extends EditingEvent {

	private Collection<Note> myNotes;
	private List<String> myStrings;

	public NoteRemoveEvent(AutomatonEditorPanel panel, Collection<Note> notes) {
		super(panel);
		myNotes = notes;
		myStrings = new ArrayList<String>();
		
		for(Note n : myNotes)
			myStrings.add(n.getText());
	}

	@Override
	public boolean undo() {
		Note[] notes = myNotes.toArray(new Note[0]);
		AutomatonEditorPanel panel = getPanel();
		
		for (int i = 0; i < notes.length; i++) {
			Note n = notes[i];
			panel.addNote(n);
			n.setText(myStrings.get(i));
		}
		panel.repaint();
		return true;
	}

	@Override
	public boolean redo() {
		AutomatonEditorPanel panel = getPanel();
		for (Note n : myNotes)
			panel.removeNote(n);
		return true;
	}

	@Override
	public String getName() {
		return "Remove multiple notes";
	}

}
