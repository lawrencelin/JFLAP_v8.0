package view.algorithms.conversion.autotogram;

import javax.swing.JOptionPane;

import universe.JFLAPUniverse;
import view.automata.editing.AutomatonEditorPanel;
import model.algorithms.conversion.autotogram.AutomatonToGrammarConversion;
import model.algorithms.conversion.autotogram.FSAVariableMapping;
import model.algorithms.conversion.autotogram.FSAtoRegGrammarConversion;
import model.automata.State;
import model.automata.acceptors.Acceptor;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;

public class FSAtoRegGrammarPanel extends AutoToGramConversionPanel<FiniteStateAcceptor, FSATransition, FSAVariableMapping>{

	public FSAtoRegGrammarPanel(
			AutomatonEditorPanel<FiniteStateAcceptor, FSATransition> editor,
			AutomatonToGrammarConversion<FiniteStateAcceptor, FSAVariableMapping, FSATransition> convert) {
		super(editor, convert);
	}

	@Override
	public boolean addProductionForState(State clicked) {
		FSAtoRegGrammarConversion convert = (FSAtoRegGrammarConversion) getAlgorithm();
		AutomatonEditorPanel<FiniteStateAcceptor, FSATransition> panel = getEditorPanel();
		if(!Acceptor.isFinalState(panel.getAutomaton(), clicked)){
			JOptionPane.showMessageDialog(this,
					"There are no productions for that object!");
			return false;
		}
		if(!convert.addFinalStateProduction(clicked)){
			JOptionPane.showMessageDialog(
				JFLAPUniverse.getActiveEnvironment(),
				"This object has already been converted!");
			return false;
		}
		return true;
	}

	@Override
	public void addOtherProduction() {
		FSAtoRegGrammarConversion convert = (FSAtoRegGrammarConversion) getAlgorithm();
		
		State[] states = convert.getUnhandledStates().toArray(new State[0]);
		if(states.length > 0){
			int row = getMaxRow();
			if(addProductionForState(states[0]))
				updateObjectMap(states[0], row);
		}
	}
	
	@Override
	public void addAllOtherProductions() {
		FSAtoRegGrammarConversion convert = (FSAtoRegGrammarConversion) getAlgorithm();

		for(State s : convert.getUnhandledStates()){
			int row = getMaxRow();
			if(addProductionForState(s))
				updateObjectMap(s, row);
		}
	}

	@Override
	public void highlightOtherObjects() {
		AutomatonEditorPanel<FiniteStateAcceptor, FSATransition> panel = getEditorPanel();
		FSAtoRegGrammarConversion convert = (FSAtoRegGrammarConversion) getAlgorithm();
		
		for(State s : convert.getUnhandledStates())
			panel.selectObject(s);
	}

}
