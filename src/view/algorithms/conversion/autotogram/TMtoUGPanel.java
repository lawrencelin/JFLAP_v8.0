package view.algorithms.conversion.autotogram;

import java.util.TreeSet;

import javax.swing.JOptionPane;

import debug.JFLAPDebug;
import model.algorithms.conversion.autotogram.TMVariableMapping;
import model.algorithms.conversion.autotogram.TMtoGrammarConversion;
import model.automata.Automaton;
import model.automata.State;
import model.automata.acceptors.Acceptor;
import model.automata.turing.MultiTapeTMTransition;
import model.automata.turing.MultiTapeTuringMachine;
import universe.JFLAPUniverse;
import view.automata.editing.AutomatonEditorPanel;

public class TMtoUGPanel extends AutoToGramConversionPanel<MultiTapeTuringMachine, MultiTapeTMTransition, TMVariableMapping>{

	public TMtoUGPanel(AutomatonEditorPanel<MultiTapeTuringMachine, MultiTapeTMTransition> editor,
			TMtoGrammarConversion convert) {
		super(editor, convert);
	}

	@Override
	public boolean addProductionForState(State clicked) {
		AutomatonEditorPanel<MultiTapeTuringMachine, MultiTapeTMTransition> panel = getEditorPanel();
		MultiTapeTuringMachine tm = panel.getAutomaton();
		boolean startState = Automaton.isStartState(tm, clicked);
		boolean finalState = Acceptor.isFinalState(tm, clicked);
		
		if(startState || finalState){
			TMtoGrammarConversion convert = (TMtoGrammarConversion) getAlgorithm();
			int row = getMaxRow();
			boolean added = false;
			
			if(startState)
				if(convert.addAllExtraProductions())
					added = true;
			if(finalState)
				if(convert.addFinalTransition(clicked))
					added = true;
			if(added){
				updateObjectMap(clicked, row);
				return true;
			}
			JOptionPane.showMessageDialog(JFLAPUniverse.getActiveEnvironment(),
					"This object has already been converted!");
			return false;
		}

		JOptionPane.showMessageDialog(this,
				"There are no productions for that object!");
		return false;
	}

	@Override
	public void addOtherProduction() {
		TMtoGrammarConversion convert = (TMtoGrammarConversion) getAlgorithm();
		MultiTapeTuringMachine tm = getEditorPanel().getAutomaton();
		
		int row = getMaxRow();
		TreeSet<State> finalStates = convert.getUnhandledFinalStates();
		
		if(!convert.hasAllAdditionProductions()){
			convert.addAllExtraProductions();
			State start = tm.getStartState();
			
			if(finalStates.contains(start))
				convert.addFinalTransition(start);
			updateObjectMap(start, row);
			return;
		} 
		
		if(!finalStates.isEmpty()){
			State s = finalStates.pollFirst();
			if(convert.addFinalTransition(s))
				updateObjectMap(s, row);
		}
		
	}

	@Override
	public void highlightOtherObjects() {
		AutomatonEditorPanel<MultiTapeTuringMachine, MultiTapeTMTransition> panel = getEditorPanel();
		TMtoGrammarConversion convert = (TMtoGrammarConversion) getAlgorithm();
		
		TreeSet<State> finalStates = convert.getUnhandledFinalStates();
		for(State s : finalStates)
			panel.selectObject(s);
		
		if(!convert.hasAllAdditionProductions())
			panel.selectObject(panel.getAutomaton().getStartState());
	}

	@Override
	public void addAllOtherProductions() {
		while(!getAlgorithm().isComplete())
			addOtherProduction();
	}

}
