package view.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.JButton;

import model.algorithms.steppable.Steppable;

import universe.preferences.JFLAPPreferences;
import util.view.magnify.MagnifiableButton;

public class StepAction extends AbstractAction {

	private Steppable mySteppable;

	public StepAction(Steppable s) {
		super("Step");
		mySteppable = s;
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		mySteppable.step();
	}

}
