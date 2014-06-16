package view.action.automata;

import model.algorithms.conversion.autotogram.PDAVariableMapping;
import model.algorithms.conversion.autotogram.PDAtoCFGConverter;
import model.automata.acceptors.pda.PDATransition;
import model.automata.acceptors.pda.PushdownAutomaton;
import view.algorithms.conversion.autotogram.AutoToGramConversionPanel;
import view.algorithms.conversion.autotogram.PDAtoCFGPanel;
import view.automata.views.AutomatonView;
import view.automata.views.PDAView;

public class PDAtoCFGAction extends AutomatonToGrammarAction<PushdownAutomaton, PDATransition, PDAVariableMapping> {

	public PDAtoCFGAction(PDAView view) {
		super(view);
	}

	@Override
	public AutoToGramConversionPanel createConversionPanel() {

		PDAtoCFGConverter convert = new PDAtoCFGConverter((PushdownAutomaton) getAutomaton());
		return new PDAtoCFGPanel(getEditorPanel(), convert);
	}

}
