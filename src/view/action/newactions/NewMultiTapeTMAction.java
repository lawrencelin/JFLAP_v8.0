package view.action.newactions;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import model.automata.turing.MultiTapeTuringMachine;
import universe.JFLAPUniverse;

public class NewMultiTapeTMAction extends
		NewFormalDefinitionAction<MultiTapeTuringMachine> {

	public NewMultiTapeTMAction() {
		super("Multi-Tape Turing Machine");
	}

	@Override
	public MultiTapeTuringMachine createDefinition() {
		SpinnerModel model = new SpinnerNumberModel(1, 1, 15, 1);
		JSpinner spinner = new JSpinner(model);
		JPanel panel = new JPanel();
		panel.add(new JLabel("Enter number of tapes: "));
		panel.add(spinner);
		
		int value = JOptionPane.showOptionDialog(JFLAPUniverse.getActiveEnvironment(),
				panel, "Multi-Tape Turing Machine", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, null, null);
		if(value == JOptionPane.YES_OPTION)
			return new MultiTapeTuringMachine((Integer) spinner.getValue());
		return null;
	}

}
