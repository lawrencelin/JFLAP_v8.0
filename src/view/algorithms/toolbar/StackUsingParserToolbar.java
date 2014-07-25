package view.algorithms.toolbar;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.algorithms.steppable.SteppableAlgorithm;
import model.algorithms.testinput.parse.StackUsingParser;
import model.change.events.AdvancedChangeEvent;
import universe.preferences.JFLAPPreferences;
import util.view.magnify.MagnifiableButton;
import util.view.magnify.MagnifiableToolbar;
import view.action.StepAction;
import view.action.algorithm.AlgorithmCompleteAction;
import view.action.grammar.parse.CYKAnimateAction;
import view.action.grammar.parse.DoSelectedAction;
import view.action.grammar.parse.DoParse;
import view.grammar.parsing.cyk.CYKParseTablePanel;
import view.grammar.parsing.ll.LLParseTablePanel;

public class StackUsingParserToolbar extends  MagnifiableToolbar implements ChangeListener {

	private MagnifiableButton myStepButton;
	private MagnifiableButton mySelectedButton;
	private MagnifiableButton myCompleteButton;
	private MagnifiableButton myParseButton;
	private LLParseTablePanel myPanel;

	public StackUsingParserToolbar(LLParseTablePanel panel, StackUsingParser alg, boolean floatable) {
		setFloatable(floatable);
		int size = JFLAPPreferences.getDefaultTextSize();
		myStepButton = new MagnifiableButton(new StepAction(panel, alg), size);
		mySelectedButton = new MagnifiableButton(new DoSelectedAction(panel), size);
		myCompleteButton = new MagnifiableButton(new AlgorithmCompleteAction(panel, alg), size);
		myParseButton = new MagnifiableButton(new DoParse(panel), size);
		myPanel = panel;
		
		this.add(myStepButton);
		this.add(mySelectedButton);
		this.add(myCompleteButton);
		this.add(myParseButton);
		updateButtons(alg);
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		if (e instanceof AdvancedChangeEvent)
			updateButtons((StackUsingParser) e.getSource());
	}
	
	public void updateButtons(StackUsingParser alg) {
		mySelectedButton.setEnabled(alg.canStep());
		myStepButton.setEnabled(alg.canStep());
		myCompleteButton.setEnabled(alg.canStep());
		myParseButton.setEnabled(false);
	}

}
