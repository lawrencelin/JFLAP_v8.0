package view.grammar.parsing;

import debug.JFLAPDebug;
import model.algorithms.testinput.InputUsingAlgorithm;
import model.algorithms.testinput.parse.Derivation;
import model.algorithms.testinput.parse.Parser;
import model.change.events.AdvancedChangeEvent;
import model.grammar.Grammar;
import util.view.DropDownMenuPanel;
import view.grammar.parsing.derivation.InteractiveDerivationView;

public abstract class FindFirstParserView<T extends RunningView> extends
		ParserView {
	private DropDownMenuPanel dropDownPanel;
	private T runningView;

	public FindFirstParserView(Parser alg) {
		super(alg);
	}

	@Override
	public DropDownMenuPanel createParseView(Parser alg) {
		Grammar g = alg.getGrammar();
		runningView = createRunningView(alg);
		dropDownPanel = new DropDownMenuPanel(runningView);
		return dropDownPanel;
	}

	public abstract T createRunningView(Parser alg);

	public T getRunningView() {
		return runningView;
	}

	@Override
	public void updateStatus(AdvancedChangeEvent e) {
		Parser alg = getAlgorithm();

		if (e.getType() == InputUsingAlgorithm.INPUT_SET) {
			resetDropDownPanel();
		}
		if (alg.getInput() == null) {
			setStatus(ParserView.SET_INPUT);
			return;
		}

		if (alg.isDone()) {
			if (alg.isReject())
				setStatus("Input rejected! Try another string!");
			else {
				resetDropDownPanel();
				Derivation d = alg.getDerivation();

				InteractiveDerivationView derivationView = new InteractiveDerivationView(
						d);
				dropDownPanel.addOption(derivationView);
				setStatus("Input accepted! Change view to see derivation!");
			}
		}
	}

	/**
	 * Resets the drop down menu and corresponding view to have the runningView
	 * as the only option.
	 */
	public void resetDropDownPanel() {
		dropDownPanel.removeAllOptions();
		dropDownPanel.addOption(runningView);
		dropDownPanel.setSize(runningView.getPreferredSize());
	}
}
