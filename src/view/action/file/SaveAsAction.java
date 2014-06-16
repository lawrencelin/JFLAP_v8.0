package view.action.file;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import util.JFLAPConstants;
import view.action.EnvironmentAction;
import view.environment.JFLAPEnvironment;
import view.environment.TabChangeListener;
import view.environment.TabChangedEvent;

public class SaveAsAction extends EnvironmentAction implements TabChangeListener{

	public static final String SAVE_AS = "saveas-ability";

	public SaveAsAction(JFLAPEnvironment e) {
		super("Save As", e);
		e.addTabListener(this);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, JFLAPConstants.MAIN_MENU_MASK | KeyEvent.SHIFT_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e, JFLAPEnvironment env) {
		env.save(true);
	}
	
	@Override
	public boolean isEnabled() {
		return getMyEnvironment().getSavableObject() != null;
	}

	@Override
	public void tabChanged(TabChangedEvent e) {
		firePropertyChange(SAVE_AS, null, isEnabled());
	}

}
