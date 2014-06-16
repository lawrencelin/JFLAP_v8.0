package view.action;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;

import universe.JFLAPUniverse;

public class QuickOpenAction extends AbstractAction {

	private File myFile;

	public QuickOpenAction(File f){
		super(f.getName());
		myFile = f;
	}



	@Override
	public void actionPerformed(ActionEvent arg0) {
		JFLAPUniverse.registerEnvironment(myFile);
	}

}
