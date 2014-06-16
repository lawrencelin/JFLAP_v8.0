package view.automata.undoing;

import view.automata.editing.AutomatonEditorPanel;

public class ClearSelectionEvent extends EditingEvent {

	public ClearSelectionEvent(AutomatonEditorPanel panel){
		super(panel);
	}
	
	@Override
	public boolean undo() {
		return clear();
	}

	@Override
	public boolean redo() {
		return clear();
	}

	@Override
	public String getName() {
		return "Clear selected objects";
	}

	private boolean clear(){
		getPanel().clearSelection();
		return true;
	}
}
