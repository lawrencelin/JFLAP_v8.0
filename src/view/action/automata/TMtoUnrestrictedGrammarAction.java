package view.action.automata;

import view.algorithms.conversion.autotogram.AutoToGramConversionPanel;
import view.algorithms.conversion.autotogram.TMtoUGPanel;
import view.automata.views.AutomatonView;
import view.automata.views.MultiTapeTMView;
import model.algorithms.conversion.autotogram.TMVariableMapping;
import model.algorithms.conversion.autotogram.TMtoGrammarConversion;
import model.automata.turing.MultiTapeTMTransition;
import model.automata.turing.MultiTapeTuringMachine;

public class TMtoUnrestrictedGrammarAction extends AutomatonToGrammarAction<MultiTapeTuringMachine, MultiTapeTMTransition, TMVariableMapping>{

	public TMtoUnrestrictedGrammarAction(MultiTapeTMView view) {
		super(view);
	}

	@Override
	public AutoToGramConversionPanel<MultiTapeTuringMachine, MultiTapeTMTransition, TMVariableMapping> createConversionPanel() {
		TMtoGrammarConversion convert = new TMtoGrammarConversion((MultiTapeTuringMachine) getAutomaton());
		return new TMtoUGPanel(getEditorPanel(), convert);
	}

}
