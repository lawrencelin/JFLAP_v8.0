package view.algorithms.conversion.autotogram;

import javax.swing.JOptionPane;

import view.automata.editing.AutomatonEditorPanel;
import model.algorithms.conversion.autotogram.AutomatonToGrammarConversion;
import model.algorithms.conversion.autotogram.PDAVariableMapping;
import model.algorithms.conversion.autotogram.PDAtoCFGConverter;
import model.automata.State;
import model.automata.acceptors.pda.PDATransition;
import model.automata.acceptors.pda.PushdownAutomaton;

public class PDAtoCFGPanel extends AutoToGramConversionPanel<PushdownAutomaton, PDATransition, PDAVariableMapping>{

	public PDAtoCFGPanel(
			AutomatonEditorPanel<PushdownAutomaton, PDATransition> editor, PDAtoCFGConverter convert) {
		super(editor, convert);
	}

	@Override
	public boolean addProductionForState(State clicked) {
		JOptionPane.showMessageDialog(this,
				"There are no productions for that object!");
		return false;
	}
	
	@Override
	public void exportGrammar() {
		PDAtoCFGConverter convert = (PDAtoCFGConverter) getAlgorithm();
		if(convert.getUnconvertedTransitions().isEmpty()){
			JOptionPane.showMessageDialog(this, "Your list of rules will be trimmed of useless productions and placed in a new window.");
			convert.stepToCompletion();
		}
		super.exportGrammar();
	}

	@Override
	public void addOtherProduction() {
	}

	@Override
	public void highlightOtherObjects() {
	}

	@Override
	public void addAllOtherProductions() {
	}

}
