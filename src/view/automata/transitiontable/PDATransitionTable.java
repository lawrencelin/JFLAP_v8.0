package view.automata.transitiontable;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import model.automata.State;
import model.automata.acceptors.pda.PDATransition;
import model.automata.acceptors.pda.PushdownAutomaton;
import model.symbols.SymbolString;
import model.symbols.symbolizer.Symbolizers;
import view.automata.editing.AutomatonEditorPanel;

/**
 * Transition Table for PushdownAutomata.
 * 
 * @author Ian McMahon
 *
 */
public class PDATransitionTable extends TransitionTable<PushdownAutomaton, PDATransition> {

	private static final String NAME[] = { "Read", "Pop", "Push" };
	
	public PDATransitionTable(PDATransition trans,
			PushdownAutomaton automaton,
			AutomatonEditorPanel<PushdownAutomaton, PDATransition> panel) {
		super(1, 3, trans, automaton, panel);
	}

	@Override
	public TableModel createModel() {
		PDATransition trans = getTransition();
		final SymbolString input = new SymbolString(trans.getInput());
		final SymbolString pop = new SymbolString(trans.getPop());
		final SymbolString push = new SymbolString(trans.getPush());
		
		
		return new AbstractTableModel() {
			private String[] s = new String[] { input.toString(), pop.toString(), push.toString()};
			
			public Object getValueAt(int row, int column) {
				return s[column];
			}

			public void setValueAt(Object o, int r, int c) {
				s[c] = (String) o;
			}

			public boolean isCellEditable(int r, int c) {
				return true;
			}

			public int getRowCount() {
				return 1;
			}

			public int getColumnCount() {
				return 3;
			}

			public String getColumnName(int c) {
				return NAME[c];
			}
		};
	}

	@Override
	public PDATransition modifyTransition() {
		TableModel model = getModel();
		PDATransition trans = getTransition();
		State from = trans.getFromState(), to = trans.getToState();
		PushdownAutomaton pda = getAutomaton();
		
		String iString = getValidString((String) model.getValueAt(0, 0));
		String popString = getValidString((String) model.getValueAt(0, 1));
		String pushString = getValidString((String) model.getValueAt(0, 2));
		
		SymbolString input = Symbolizers.symbolize(iString, pda);
		SymbolString pop = Symbolizers.symbolize(popString, pda);
		SymbolString push = Symbolizers.symbolize(pushString, pda);
		return new PDATransition(from, to, input, pop, push);
	}

	

}
