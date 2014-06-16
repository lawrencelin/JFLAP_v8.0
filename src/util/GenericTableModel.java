package util;

import javax.swing.table.AbstractTableModel;

public abstract class GenericTableModel<T> extends AbstractTableModel {

	private T[][] myTable;
	
	public GenericTableModel(int row, int column){
		myTable = createModel(row, column);
	}
	
	public abstract T[][] createModel(int row, int column);

	@Override
	public int getColumnCount() {
		if (getRowCount() > 0)
			return myTable[0].length;
		return 0;
	}

	@Override
	public int getRowCount() {
		return myTable.length;
	}

	@Override
	public T getValueAt(int r, int c) {
		
		return myTable[r][c];
	}
	
	@Override
	public void setValueAt(Object newVal, int r, int c) {
		myTable[r][c] = (T) newVal;
	}

}
