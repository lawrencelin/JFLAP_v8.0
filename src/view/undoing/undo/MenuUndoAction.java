package view.undoing.undo;

import java.awt.Component;

import view.EditingPanel;
import view.environment.JFLAPEnvironment;
import model.undo.UndoKeeper;

public class MenuUndoAction extends UndoAction {

	private JFLAPEnvironment myEnv;

	public MenuUndoAction(JFLAPEnvironment e) {
		super(null);
		myEnv = e;
	}
	
	
	@Override
	public UndoKeeper getKeeper() {
		Component current = myEnv.getCurrentView();
		if (current != null &&
				current instanceof EditingPanel)
			return ((EditingPanel) current).getKeeper();
		return null;
	}
	
	@Override
	public boolean isEnabled() {
		Component current = myEnv.getCurrentView();
		return getKeeper() != null && super.isEnabled();
	}

}
