package view.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import view.environment.JFLAPEnvironment;

public abstract class EnvironmentAction extends AbstractAction {

	
	
	
	private JFLAPEnvironment myEnvironment;

	public EnvironmentAction(String name, JFLAPEnvironment e) {
		super(name);
		myEnvironment = e;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		actionPerformed(e, myEnvironment);
	}

	public abstract void actionPerformed(ActionEvent e, JFLAPEnvironment env);

	public JFLAPEnvironment getMyEnvironment() {
		return myEnvironment;
	}

}
