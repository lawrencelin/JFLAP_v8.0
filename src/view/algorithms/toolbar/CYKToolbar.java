package view.algorithms.toolbar;

import model.algorithms.steppable.SteppableAlgorithm;
import universe.preferences.JFLAPPreferences;
import util.view.magnify.MagnifiableButton;
import view.action.grammar.parse.CYKAnimateAction;
import view.action.grammar.parse.DoSelectedAction;
import view.grammar.parsing.cyk.CYKParseTablePanel;

public class CYKToolbar extends SteppableToolbar {

	private MagnifiableButton mySelectedButton;
	private MagnifiableButton myAnimateButton;
	private CYKParseTablePanel myPanel;

	public CYKToolbar(CYKParseTablePanel panel, SteppableAlgorithm alg, boolean floatable) {
		super(alg, floatable);
		int size = JFLAPPreferences.getDefaultTextSize();
		mySelectedButton = new MagnifiableButton(new DoSelectedAction(panel), size);
		myAnimateButton = new MagnifiableButton(new CYKAnimateAction(panel), size);
		myPanel = panel;
		
		add(mySelectedButton);
		add(myAnimateButton);
		updateButtons(alg);
	}
	
	@Override
	public void updateButtons(SteppableAlgorithm alg) {
		mySelectedButton.setEnabled(alg.canStep());
		myAnimateButton.setEnabled(alg.canStep());
		super.updateButtons(alg);
	}

}
