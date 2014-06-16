package view.algorithms.conversion.gramtoauto;

import java.awt.Dimension;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.table.DefaultTableModel;

import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.ProductionSet;
import util.view.magnify.MagnifiableTable;

public class GrammarConversionTable extends MagnifiableTable {

	private static final Dimension CONVERSION_TABLE_SIZE = new Dimension(200, 200);
	private Grammar myGrammar;
	private Object[][] myData;
	private Map<Production, Integer> productionToRow;

	public GrammarConversionTable(Grammar g){
		setModel(new GrammarTableModel());
		productionToRow = new TreeMap<Production, Integer>();
		myGrammar = g;
		
		ProductionSet prods = g.getProductionSet();
		myData = new Object[prods.size()][2];
		Object[] columnNames = { "Production", "Created" };
		
		int i=0;
		for (Production p : prods) {
			myData[i][0] = p;
			myData[i][1] = false;
			productionToRow.put(p, i);
			i++;
		}
		DefaultTableModel model = (DefaultTableModel) getModel();
		model.setDataVector(myData, columnNames);
		setMinimumSize(CONVERSION_TABLE_SIZE);
	}
	
	public Production[] getSelected() {
		int[] rows = getSelectedRows();
		Production[] selected = new Production[rows.length];
		for (int i = 0; i < rows.length; i++)
			selected[i] = (Production) myData[rows[i]][0];
		return selected;
	}
	
	public void setChecked(Production production, boolean checked) {
		Integer r = productionToRow.get(production);
		if (r == null)
			return;
		myData[r][1] = checked;
		((DefaultTableModel) getModel()).setValueAt(checked, r, 1);
	}
	
	/**
	 * The model for this table.
	 */
	private class GrammarTableModel extends DefaultTableModel {
		
		public boolean isCellEditable(int row, int column) {
			return false;
		}

		public Class getColumnClass(int columnIndex) {
			if (columnIndex == 1)
				return Boolean.class;
			return super.getColumnClass(columnIndex);
		}
	}
}
