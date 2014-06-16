package view.action.sets;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import model.sets.AbstractSet;
import model.sets.SetsManager;
import model.undo.IUndoRedo;
import model.undo.UndoKeeper;
import universe.JFLAPUniverse;
import view.sets.state.State;

@SuppressWarnings("serial")
public class FinishConstructionAction extends AbstractAction implements IUndoRedo {
	
	private State myState;
	private UndoKeeper myKeeper;
	private AbstractSet mySet;
	

	public FinishConstructionAction(UndoKeeper keeper, State state) {
		super("Finish");
		myState = state;
		myKeeper = keeper;
	}
	

	@Override
	public void actionPerformed(ActionEvent arg0) {		
		try {
			mySet = myState.finish(myKeeper);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (mySet == null)	return;
		SetsManager.ACTIVE_REGISTRY.add(mySet);
		JFLAPUniverse.getActiveEnvironment().closeActiveTab();
	}


	
	@Override
	public boolean undo() {
		return myState.undo();
	}

	@Override
	public boolean redo() {
		return myState.redo();
	}

	@Override
	public String getName() {
		return "Finish/Save";
	}
	
	

}
