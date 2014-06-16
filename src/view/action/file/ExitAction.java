package view.action.file;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;

import universe.JFLAPUniverse;

public class ExitAction extends AbstractAction {

	
	public ExitAction() {
		super("Exit");
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		JFLAPUniverse.exit(true);
	}

}
