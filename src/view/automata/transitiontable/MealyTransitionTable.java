package view.automata.transitiontable;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import model.automata.State;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.transducers.OutputFunctionSet;
import model.automata.transducers.mealy.MealyMachine;
import model.automata.transducers.mealy.MealyOutputFunction;
import model.change.events.AddEvent;
import model.change.events.SetToEvent;
import model.symbols.SymbolString;
import model.symbols.symbolizer.Symbolizers;
import model.undo.CompoundUndoRedo;
import model.undo.IUndoRedo;
import universe.preferences.JFLAPPreferences;
import view.automata.editing.AutomatonEditorPanel;

/** 
 * Transition Table specific to MealyMachines, allows editing of output as well as the transition.
 * 
 * @author Ian McMahon
 *
 */
public class MealyTransitionTable extends
		TransitionTable<MealyMachine, FSATransition> {
	private static final String[] NAME = new String[] { "Label", "Output" };
	private MealyOutputFunction myFunc;

	public MealyTransitionTable(FSATransition trans, MealyMachine automaton,
			AutomatonEditorPanel<MealyMachine, FSATransition> panel) {
		super(1, 2, trans, automaton, panel);
	}

	@Override
	public TableModel createModel() {
		OutputFunctionSet<MealyOutputFunction> funcSet = getAutomaton()
				.getOutputFunctionSet();
		FSATransition trans = getTransition();
		for (MealyOutputFunction func : funcSet) {
			if (func.matches(trans)) {
				myFunc = func;
				break;
			}
		}
		
		final String input = getTransition().getLabelText();
		final String output = myFunc == null ? JFLAPPreferences
				.getEmptyString() : new SymbolString(myFunc.getOutput())
				.toString();

		return new AbstractTableModel() {
			String[] s = new String[] { input, output };

			@Override
			public Object getValueAt(int r, int c) {
				return s[c];
			}

			@Override
			public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
				s[columnIndex] = (String) aValue;
			}

			@Override
			public int getRowCount() {
				return 1;
			}

			@Override
			public int getColumnCount() {
				return 2;
			}

			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return true;
			}

			@Override
			public String getColumnName(int column) {
				return NAME[column];
			}
		};
	}

	@Override
	public IUndoRedo stopEditing(boolean cancel) {
		IUndoRedo sup = super.stopEditing(cancel);

		if (!cancel) {
			CompoundUndoRedo comp = null;
			MealyMachine mealy = getAutomaton();
			FSATransition trans = getTransition();

			String output = getValidString((String) getModel().getValueAt(0, 1));
			SymbolString outSymbols = Symbolizers.symbolize(output, mealy);

			if (sup != null)
				comp = new CompoundUndoRedo(sup);

			if (sup instanceof AddEvent) {
				OutputFunctionSet<MealyOutputFunction> funcSet = mealy.getOutputFunctionSet();
				funcSet.add(myFunc = new MealyOutputFunction(trans, outSymbols));
				
				AddEvent<MealyOutputFunction> mealAdd = new AddEvent<MealyOutputFunction>(
						funcSet, myFunc);
				if (comp == null)
					comp = new CompoundUndoRedo(mealAdd);
				else
					comp.add(mealAdd);
			} else if (myFunc != null){
				MealyOutputFunction temp = myFunc.copy();
				myFunc.setTo(new MealyOutputFunction(trans, outSymbols));
				
				//If the function changed, register a settoevent
				if (!myFunc.equals(temp)) {
					SetToEvent<MealyOutputFunction> mealSet = new SetToEvent<MealyOutputFunction>(
							myFunc, temp, myFunc.copy());
					if (comp == null)
						comp = new CompoundUndoRedo(mealSet);
					else
						comp.add(mealSet);
				}
			}
			return comp;
		}
		return null;
	}

	@Override
	public FSATransition modifyTransition() {
		String input = getValidString((String) getModel().getValueAt(0, 0));

		FSATransition trans = getTransition();
		State from = trans.getFromState(), to = trans.getToState();
		SymbolString inSymbols = Symbolizers.symbolize(input, getAutomaton());

		return new FSATransition(from, to, inSymbols);
	}

}
