package view;

import model.undo.UndoKeeper;
import util.view.magnify.MagnifiablePanel;

public abstract class EditingPanel extends MagnifiablePanel {

	private UndoKeeper myKeeper;
	private boolean amEditable;
	private boolean amDirty;
	
	public EditingPanel(UndoKeeper keeper, boolean editable){
		myKeeper = keeper;
		amEditable = editable;
	}
	
	public UndoKeeper getKeeper() {
		return myKeeper;
	}
	public boolean isEditable(){
		return amEditable;
	}
	
	public void setEditable(boolean editable){
		amEditable = editable;
	}

	public boolean isDirty() {
		return amDirty;
	}
	
	public void setDirty(boolean dirty){
		amDirty = dirty;
	}
	
}
