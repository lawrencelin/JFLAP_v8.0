package view.sets.state;

import model.sets.AbstractSet;
import model.undo.UndoKeeper;
import view.sets.SetDefinitionView;

public abstract class State {

	/**
	 * Called by FinishConstructionAction to effectively "save" the set
	 * once it has been created or modified
	 * @param keeper
	 * @throws Exception 
	 */
	public abstract AbstractSet finish(UndoKeeper keeper) throws Exception;
	
	
	public abstract boolean undo();
	
	public abstract boolean redo();

	public abstract SetDefinitionView createDefinitionView();
}
