package view.automata.undoing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import model.undo.IUndoRedo;
import view.automata.editing.AutomatonEditorPanel;

public class CompoundMoveEvent extends ClearSelectionEvent {

	private List<IUndoRedo> myEvents;

	public CompoundMoveEvent(AutomatonEditorPanel panel,
			StateMoveEvent... states) {
		this(panel, Arrays.asList(states));

	}

	public CompoundMoveEvent(AutomatonEditorPanel panel,
			Collection<StateMoveEvent> states) {
		super(panel);
		myEvents = new ArrayList<IUndoRedo>();
		if (!states.isEmpty())
			myEvents.addAll(states);
	}
	
	public void addEvents(Collection<IUndoRedo> events){
		addEvents(events.toArray(new IUndoRedo[0]));
	}

	public void addEvents(IUndoRedo... events) {
		if (events.length > 0)
			myEvents.addAll(Arrays.asList(events));
	}

	@Override
	public boolean undo() {
		for (IUndoRedo undo : myEvents)
			undo.undo();
		return super.undo();
	}

	@Override
	public boolean redo() {
		for (IUndoRedo redo : myEvents)
			redo.redo();
		return super.redo();
	}

	public boolean isEmpty() {
		return myEvents.isEmpty();
	}
}
