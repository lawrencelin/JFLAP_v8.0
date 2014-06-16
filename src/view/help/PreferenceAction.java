package view.help;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JWindow;

import debug.JFLAPDebug;

import universe.preferences.JFLAPPreferences;

public class PreferenceAction extends AbstractAction{
	
	public PreferenceAction() {
		super("Preferences...");
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		JFLAPPreferences.showPreferenceDisplay();
	}

}
