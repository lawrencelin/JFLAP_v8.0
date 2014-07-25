package view.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.JButton;

import model.algorithms.steppable.Steppable;

import universe.preferences.JFLAPPreferences;
import util.view.magnify.MagnifiableButton;
import view.grammar.parsing.ll.LLParseTablePanel;

public class StepAction extends AbstractAction {

	private Steppable mySteppable;
	private LLParseTablePanel myPanel;

	public StepAction(Steppable s) {
		super("Step");
		mySteppable = s;
		
	}

	public StepAction(LLParseTablePanel panel, Steppable s) {
		this(s);
		myPanel = panel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (myPanel != null)
			myPanel.step();
		mySteppable.step();
	}

}
