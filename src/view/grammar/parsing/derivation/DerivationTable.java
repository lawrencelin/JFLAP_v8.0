package view.grammar.parsing.derivation;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import universe.preferences.JFLAPPreferences;

import model.algorithms.testinput.parse.Derivation;

public class DerivationTable extends DerivationPanel {

	private JTable myTable;

	public DerivationTable(Derivation d) {
		super("Derivation Table");
		this.setLayout(new BorderLayout());
		myTable = new JTable(new DerivationTableModel());
		myTable.setDragEnabled(false);
		
		setDerivation(d);
		
		JScrollPane pane = new JScrollPane(myTable);
		this.add(pane, BorderLayout.CENTER);
	}

	@Override
	public void setMagnification(double mag) {
		super.setMagnification(mag);
		float size = (float) (mag*JFLAPPreferences.getDefaultTextSize());
		myTable.setFont(this.getFont().deriveFont(size));
		myTable.setRowHeight((int) (size+10));
	}
	
	@Override
	public void setDerivation(Derivation d) {
		super.setDerivation(d);
		((AbstractTableModel) myTable.getModel()).fireTableDataChanged();
	}
	
	private class DerivationTableModel extends AbstractTableModel{

		@Override
		public int getColumnCount() {
			return 2;
		}
		
		@Override
		public String getColumnName(int column) {
			return new String[]{"Production", "Derivation"}[column];
		}

		@Override
		public int getRowCount() {
			return getDerivation().length()+1;
		}

		@Override
		public String getValueAt(int r, int c) {
			if (c == 0){
				if (r == 0) return "";
				return getDerivation().getProduction(r-1).toString();
			}
			else if (c == 1)
				return getDerivation().createResult(r).toString();
			
			return null;
		}
		
	}

}
