package view.automata.transitiontable;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import model.automata.turing.buildingblock.Block;
import model.automata.turing.buildingblock.BlockTransition;
import model.automata.turing.buildingblock.BlockTuringMachine;
import model.symbols.SymbolString;
import model.symbols.symbolizer.Symbolizers;
import view.automata.editing.AutomatonEditorPanel;

/**
 * Transition Table for BlockTuringMachines.
 * 
 * @author Ian McMahon
 *
 */
public class BlockTMTransitionTable extends
		TransitionTable<BlockTuringMachine, BlockTransition> {

	public BlockTMTransitionTable(BlockTransition trans,
			BlockTuringMachine automaton,
			AutomatonEditorPanel<BlockTuringMachine, BlockTransition> panel) {
		super(1, 1, trans, automaton, panel);

		getColumnModel().getColumn(0).setCellEditor(new BlankRemovingEditor());
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
	public BlockTransition modifyTransition() {
		String s = MultiTapeTMTransitionTable.getValidTMString((String) getModel().getValueAt(0, 0));

		BlockTransition trans = getTransition();
		Block from = trans.getFromState(), to = trans.getToState();
		SymbolString symbols = Symbolizers.symbolize(s, getAutomaton());

		return new BlockTransition(from, to, symbols);
	}

}
