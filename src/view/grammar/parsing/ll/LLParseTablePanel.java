package view.grammar.parsing.ll;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import model.algorithms.testinput.InputUsingAlgorithm;
import model.algorithms.testinput.parse.FirstFollowTable;
import model.algorithms.testinput.parse.Parser;
import model.algorithms.testinput.parse.brute.UnrestrictedBruteParser;
import model.algorithms.testinput.parse.cyk.CYKParser;
import model.algorithms.testinput.parse.ll.LL1Parser;
import model.change.events.AdvancedChangeEvent;
import model.grammar.Grammar;
import model.grammar.Variable;
import model.symbols.Symbol;
import model.symbols.SymbolString;
import util.view.DoSelectable;
import util.view.Parsable;
import util.view.tables.SelectingEditor;
import view.grammar.parsing.RunningView;
import view.grammar.parsing.cyk.SetCellEditor;
import view.grammar.parsing.ll.*;


public class LLParseTablePanel extends RunningView implements DoSelectable, Parsable{

	private LL1Parser myAlg;
	private SelectingEditor myEditor;
	private LLTableModel myModel;
	private LLParseTable targetTable;
	private boolean isComplete;

	public LLParseTablePanel(LL1Parser alg) {
		super("LL Parse Table", alg, true);
		myAlg = alg;
		isComplete = false;
		JTable table = getTable();

		table.setDefaultRenderer(Object.class, createRenderer());
		table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		TableColumnModel colModel = table.getColumnModel();

		TableColumn levels = colModel.getColumn(0);
		levels.setMinWidth(70);
		levels.setMaxWidth(70);

		TableColumn nodes = colModel.getColumn(1);
		nodes.setMinWidth(175);
		nodes.setMaxWidth(175);

		//myEditor = new SetCellEditor();

		JTable secondTable = new JTable(new LLParseTable(alg.getGrammar()));
		secondTable.setDefaultRenderer(Object.class, createRenderer());
		this.getSecondPane().add(secondTable);

		setPreferredSize(new Dimension(300, 180));

		targetTable = LLParseTableGenerator.generate(alg.getGrammar());
	}

	private DefaultTableCellRenderer createRenderer() {
		return new DefaultTableCellRenderer() {
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				JLabel label = (JLabel) super.getTableCellRendererComponent(
						table, value, isSelected, hasFocus, row, column);
				label.setHorizontalAlignment(SwingConstants.CENTER);
				return label;
			};
		};
	}

	@Override
	public AbstractTableModel createModel(Parser parser) {
		myModel = new LLTableModel((LL1Parser) parser);
		return myModel;
	}

	private class LLTableModel extends AbstractTableModel implements
	ChangeListener {

		private LL1Parser parser;
		private HashMap<Integer, Object[]> myData;
		private int myLevel;
		private FirstFollowTable myTable;

		public LLTableModel(LL1Parser parser) {
			this.parser = parser;
			myData = new HashMap<Integer, Object[]>();
			Grammar g = parser.getGrammar();
			myLevel = g.getVariables().size();

			for (int i = 0; i < myLevel; i++){
				SymbolString[] entry = new SymbolString[this.getColumnCount()];
				for (int j = 0; j < entry.length; j++){
					entry[j] = new SymbolString(new Symbol("{}"));
				}
				entry[0] = new SymbolString(new Symbol(g.getVariables().getSymbolStringArray()[i]));
				myData.put(i, entry);
			}

			myTable = parser.getTable().getFFTable();
			myTable.completeTable();
			//System.out.println(myTable.toString());

			//System.out.println(myTable.size());

			parser.addListener(this);

			/*
			FirstFollowTable fftable = new FirstFollowTable(g);
			fftable.completeTable();*/

			//System.out.println(g.getTerminals().toString());
			//System.out.println(g.getVariables().toString());


		}

		@Override
		public String getColumnName(int column) {
			String[] names = new String[] { "", "First",
			"Follow" };
			return names[column];
		}

		@Override
		public int getColumnCount() {
			return 3;
		}

		@Override
		public int getRowCount() {
			return myLevel;
		}

		@Override
		public Object getValueAt(int row, int column) {
			//System.out.println("row: "+row+ ", column: "+ column);
			int level = row;
			/*if (column == 0)
				return level;*/
			return myData.get(level)[column];
		}

		@Override
		public void stateChanged(ChangeEvent e) {
			if (e instanceof AdvancedChangeEvent) {
				AdvancedChangeEvent event = (AdvancedChangeEvent) e;
				System.out.println("getting stopped here");
			}

			TableColumnModel columnModel = getTable().getColumnModel();

			for (int i = 0; i < getColumnCount(); i++) {
				TableColumn column = columnModel.getColumn(i);
				column.setCellEditor(myEditor);
				System.out.println("making it here");
				//getTable().clearSelection();
			}
		}

		private Variable getVariable(int row, int column){
			return new Variable(parser.getGrammar().getVariables().getSymbolStringArray()[row]);
		}

		public void updateCell(int row, int column) {
			// TODO Auto-generated method stub
			if (column < 1 || column > 2 || row > myLevel || row < 0)
				return;

			Variable data = this.getVariable(row, column);

			SymbolString[] entry = (SymbolString[]) myData.get(row);
			if (column == 1)
				entry[column] = new SymbolString(new Symbol(myTable.getFirst(data).toString()));
			else
				entry[column] = new SymbolString(new Symbol(myTable.getFollow(data).toString()));
			myData.put(row, entry);
		}
	}

	public void step(){

	}

	@Override
	public void doSelected() {
		JTable table = getTable();

		int row = table.getSelectedRow();
		int column = table.getSelectedColumn();
		if (!(row < 0 || column < 0)){

			System.out.println(row + " " + column);
			table.clearSelection();
			myModel.updateCell(row, column);
		}

		JTable table2 = this.getSecondTable();
		row = table2.getSelectedRow();
		column = table2.getSelectedColumn();

		if (!(row < 0 || column < 0)){

			LLParseTable model = (LLParseTable) table2.getModel();
			model.setValueAt(targetTable.getValueAt(row, column), row, column);
			table2.clearSelection();
		}
		this.repaint();
	}

	public void complete() {
		for (int i = 0; i < myAlg.getGrammar().getVariables().size(); i++){
			myModel.updateCell(i, 1);
			myModel.updateCell(i, 2);
		}
		this.repaint();
	}

	@Override
	public void Parse() {
		// TODO Auto-generated method stub
		System.out.println("Parse here.");
	}

}
