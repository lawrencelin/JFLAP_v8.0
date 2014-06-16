package view.automata.transitiontable;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.DefaultCellEditor;
import javax.swing.InputMap;
import javax.swing.JComboBox;
import javax.swing.KeyStroke;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import debug.JFLAPDebug;
import model.automata.turing.MultiTapeTMTransition;
import model.automata.turing.MultiTapeTuringMachine;
import model.automata.turing.TuringMachineMove;
import model.symbols.Symbol;
import universe.preferences.JFLAPPreferences;
import util.JFLAPConstants;
import view.automata.editing.AutomatonEditorPanel;

/**
 * TransitionTable specific to non-Block Turing Machines
 * 
 * @author Ian McMahon
 * 
 */
public class MultiTapeTMTransitionTable extends
		TransitionTable<MultiTapeTuringMachine, MultiTapeTMTransition> {
	private static String[] DIRECTIONS = new String[] { "R", "L", "S" };

	public MultiTapeTMTransitionTable(
			MultiTapeTMTransition trans,
			MultiTapeTuringMachine automaton,
			AutomatonEditorPanel<MultiTapeTuringMachine, MultiTapeTMTransition> panel) {
		super(automaton.getNumTapes(), 3, trans, automaton, panel);

		TableColumnModel tcm = getColumnModel();
		BlankRemovingEditor blank = new BlankRemovingEditor();

		JComboBox<String> box = new JComboBox<String>(DIRECTIONS) {
			@Override
			protected boolean processKeyBinding(KeyStroke ks, KeyEvent e,
					int condition, boolean pressed) {
				// Will consume all keystrokes except Enter and ESC (as these
				// are the only non-null entries in the transition table's
				// inputmap.
				if (!super.processKeyBinding(ks, e, condition, pressed))
					return MultiTapeTMTransitionTable.this.getInputMap()
							.get(ks) == null;
				return true;
			}
		};

		TMMoveEditor move = new TMMoveEditor(box);

		tcm.getColumn(0).setCellEditor(blank);
		tcm.getColumn(1).setCellEditor(blank);
		tcm.getColumn(2).setCellEditor(move);
	}

	@Override
	public TableModel createModel() {
		return new MultiTapeTMTransTableModel();
	}

	public static String getValidTMString(String s) {
		s = getValidString(s);
		if (s.isEmpty())
			s = JFLAPConstants.BLANK;
		return s;
	}

	@Override
	public MultiTapeTMTransition modifyTransition() {
		MultiTapeTuringMachine machine = getAutomaton();
		MultiTapeTMTransition trans = getTransition();
		int numTapes = machine.getNumTapes();

		Symbol[] reads = new Symbol[numTapes];
		Symbol[] writes = new Symbol[numTapes];
		TuringMachineMove[] moves = new TuringMachineMove[numTapes];
		TableModel model = getModel();

		for (int i = 0; i < machine.getNumTapes(); i++) {
			String r = getValidTMString((String) model.getValueAt(i, 0));
			String w = getValidTMString((String) model.getValueAt(i, 1));

			reads[i] = new Symbol(r);
			writes[i] = new Symbol(w);
			moves[i] = TuringMachineMove.getMove((String) model
					.getValueAt(i, 2));
		}

		return new MultiTapeTMTransition(trans.getFromState(),
				trans.getToState(), reads, writes, moves);
	}

	private class MultiTapeTMTransTableModel extends AbstractTableModel {
		private String s[][];
		private String name[] = { "Read", "Write", "Direction" };

		public MultiTapeTMTransTableModel() {
			int numTapes = getAutomaton().getNumTapes();
			s = new String[numTapes][3];
			MultiTapeTMTransition trans = getTransition();

			for (int i = 0; i < numTapes; i++) {
				s[i][0] = trans.getRead(i).toString();
				s[i][1] = trans.getWrite(i).toString();
				s[i][2] = trans.getMove(i).toString();
			}
		}

		public Object getValueAt(int row, int column) {
			return s[row][column];
		}

		public void setValueAt(Object o, int r, int c) {
			s[r][c] = (String) o;
		}

		public boolean isCellEditable(int r, int c) {
			return true;
		}

		public int getRowCount() {
			return getAutomaton().getNumTapes();
		}

		public int getColumnCount() {
			return 3;
		}

		public String getColumnName(int c) {
			return name[c];
		}
	}

	private class TMMoveEditor extends DefaultCellEditor {
		public TMMoveEditor(JComboBox<String> box) {
			super(box);
			init(box);
		}

		private void init(final JComboBox<String> box) {
			InputMap imap = box.getInputMap();
			ActionMap amap = box.getActionMap();
			Object o = new Object();
			amap.put(o, new AbstractAction() {

				public void actionPerformed(ActionEvent e) {
					box.setSelectedItem(e.getActionCommand().toUpperCase());
				}
			});
			for (int i = 0; i < DIRECTIONS.length; i++) {
				char c = DIRECTIONS[i].charAt(0);
				imap.put(KeyStroke.getKeyStroke(c), o);

				c = DIRECTIONS[i].toLowerCase().charAt(0);
				imap.put(KeyStroke.getKeyStroke(c), o);
			}
		}
	}
}
