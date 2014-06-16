package view.action.file;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.KeyStroke;

import universe.JFLAPUniverse;
import universe.preferences.JFLAPPreferences;
import util.JFLAPConstants;

import file.XMLFileChooser;

public class OpenAction extends AbstractAction {

	public OpenAction() {
		super("Open...");
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O,
				JFLAPConstants.MAIN_MENU_MASK));
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		XMLFileChooser chooser = new XMLFileChooser(false);
		
		int n = chooser.showOpenDialog(null);
		if (n != JFileChooser.APPROVE_OPTION) return;

		File f = chooser.getSelectedFile();
		if (JFLAPUniverse.registerEnvironment(f))
			JFLAPPreferences.addRecentlyOpenend(f);
	}

}
