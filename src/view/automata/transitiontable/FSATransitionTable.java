package view.automata.transitiontable;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import model.automata.Automaton;
import model.automata.State;
import model.automata.acceptors.fsa.FSATransition;
import model.symbols.SymbolString;
import model.symbols.symbolizer.Symbolizers;
import view.automata.editing.AutomatonEditorPanel;

/**
 * TransitionTable specific to FSAs and MooreMachines (though technically, you
 * could use it for Mealy, output wouldn't be available).
 * 
 * @author Ian McMahon
 */
public class FSATransitionTable<T extends Automaton<FSATransition>> extends
		TransitionTable<T, FSATransition> {

	public FSATransitionTable(FSATransition trans, T automaton,
			AutomatonEditorPanel<T, FSATransition> panel) {
		super(1, 1, trans, automaton, panel);
	}

	@Override
	public TableModel createModel() {
		return new AbstractTableModel() {
			private String s = getTransition().getLabelText();

			public Object getValueAt(int row, int column) {
				return s;
			}

			public void setValueAt(Object o, int r, int c) {
				s = (String) o;
			}

			public boolean isCellEditable(int r, int c) {
				return true;
			}

			public int getRowCount() {
				return 1;
			}

			public int getColumnCount() {
				return 1;
			}

			public String getColumnName(int c) {
				return "Label";
			}
		};
	}

	@Override
	public FSATransition modifyTransition() {
		String s = getValidString((String) getModel().getValueAt(0, 0));

		FSATransition trans = getTransition();
		State from = trans.getFromState(), to = trans.getToState();
		SymbolString symbols = Symbolizers.symbolize(s, getAutomaton());

		return new FSATransition(from, to, symbols);
	}

}
