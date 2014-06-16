package view.action.sets;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import model.undo.UndoKeeper;
import universe.JFLAPUniverse;
import view.environment.JFLAPEnvironment;
import view.sets.SetsEditingPanel;

public class ActivateSetAction extends AbstractAction {

	private UndoKeeper myKeeper;

	public ActivateSetAction (UndoKeeper keeper) {
		super("Add/create new set");
		myKeeper = keeper;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		JFLAPEnvironment env = JFLAPUniverse.getActiveEnvironment();
		env.addSelectedComponent(new SetsEditingPanel(myKeeper, true));
	}

}
