package view.automata.views;

import javax.swing.JComponent;

import view.automata.editing.MealyEditorPanel;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.transducers.mealy.MealyMachine;
import model.automata.transducers.mealy.MealyOutputFunction;
import model.undo.UndoKeeper;

public class MealyView extends TransducerView<MealyMachine, MealyOutputFunction>{

	public MealyView(MealyMachine model) {
		super(model);
	}
	
	@Override
	public JComponent createCentralPanel(MealyMachine model, UndoKeeper keeper,
			boolean editable) {
		// TODO Auto-generated method stub
		return new MealyEditorPanel(model, keeper, editable);
	}

}
