/*
 *  JFLAP - Formal Languages and Automata Package
 * 
 * 
 *  Susan H. Rodger
 *  Computer Science Department
 *  Duke University
 *  August 27, 2009

 *  Copyright (c) 2002-2009
 *  All rights reserved.

 *  JFLAP is open source software. Please see the LICENSE for terms.
 *
 */





package util.view.tables;

import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import debug.JFLAPDebug;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * The <CODE>GrowableTableModel</CODE> is a table model that grows
 * automatically whenever the last line is edited. Basically, whenever the
 * <CODE>setValueAt</CODE> method is called on the last row, the table grows
 * itself by one line.
 * 
 * @author Thomas Finley
 */

public abstract class GrowableTableModel extends AbstractTableModel implements
		Cloneable {
	/**
	 * This instantiates a <CODE>GrowableTableModel</CODE>.
	 * 
	 * @param columns
	 *            the number of columns for this model
	 */
	public GrowableTableModel(int columns) {
		this(columns, new ArrayList<Object[]>());
	}

	/**
	 * This instantiates a <CODE>GrowableTableModel</CODE> with an initial set of data from
	 * another table or source.
	 * 
	 * @param columns
	 *            the number of columns for this model
	 */
	public GrowableTableModel(int columns, List<Object[]> newData) {
		this.columns = columns;
		data = newData;
		getData().add(createEmptyRow());
	}
	
	/**
	 * The copy constructor for this table model. This will do a shallow copy of
	 * all elements in the data.
	 * 
	 * @param model
	 *            the model to copy
	 */
	public GrowableTableModel(GrowableTableModel model) {
		copy(model);
	}

	/**
	 * This initializes the table so that it is completely blank except for
	 * having one row. The number of columns remains unchanged.
	 */
	public void clear() {
		getData().clear();
		getData().add(createEmptyRow());
		fireTableDataChanged();
	}

	/**
	 * This is a copy method to copy all of the data for a given table model to
	 * this table model.
	 * 
	 * @param model
	 *            the model to copy
	 */
	public void copy(GrowableTableModel model) {
		columns = model.getColumnCount();
		data = new ArrayList<Object[]>();
		Iterator it = model.getData().iterator();
		while (it.hasNext()) {
			Object[] oldRow = (Object[]) it.next();
			Object[] row = new Object[columns];
			for (int i = 0; i < oldRow.length; i++)
				row[i] = oldRow[i];
			getData().add(row);
		}
		fireTableDataChanged();
	}

	/**
	 * Initializes a new, empty row. This should not modify any data or any state
	 * whatsoever, but should simply return an initialized row.
	 * 
	 * @param row
	 *            the number of the row that's being initialized
	 * @return an object array that will hold, in column order, the contents of
	 *         that particular row; this by default simply returns an unmodified
	 *         <CODE>Object</CODE> array of size equal to the number of
	 *         columns with contents set to <CODE>null</CODE>
	 */
	protected Object[] createEmptyRow() {
		Object[] newRow = new Object[getColumnCount()];
		Arrays.fill(newRow, null);
		return newRow;
	}

	/**
	 * Returns the number of columns in this table.
	 * 
	 * @return the number of columns in this table
	 */
	public final int getColumnCount() {
		return columns;
	}

	/**
	 * Returns the number of rows currently in this table.
	 * 
	 * @return the number of rows currently in this table
	 */
	public final int getRowCount() {
		return getData().size();
	}

	/**
	 * Deletes a particular row.
	 * 
	 * @param row
	 *            the row index to delete
	 * @return if the row was able to be deleted
	 */
	public boolean deleteRow(int row) {
		if (row < 0 || row > getData().size() - 2)
			return false;
		fireTableRowsDeleted(row, row);
		return true;
	}

	/**
	 * Inserts data at a particular row.
	 * 
	 * @param newdata
	 *            the array of new data for a row
	 * @param row
	 *            the row index to insert the new data at
	 * @throws IllegalArgumentException
	 *             if the data array length differs from the number of columns
	 */
	public void insertRow(Object[] newData, int row) {
		if (newData.length != columns)
			throw new IllegalArgumentException("data length is "
					+ newData.length + ", should be " + columns + ".");
		getData().add(row, newData);
		fireTableRowsInserted(row, row);
	}

	/**
	 * Returns the object at a particular location in the model.
	 * 
	 * @param row
	 *            the row of the object to retrieve
	 * @param column
	 *            the column of the object to retrieve
	 * @return the object at that location
	 */
	@Override
	public Object getValueAt(int row, int column) {
		return getData().get(row)[column];
	}

	public void setValueAt(Object newdata, int row, int column) {
		getData().set(row, completeRow(newdata, row, column));
		if (this.getRowCount() > 1 && this.checkEmpty(row)){
			this.deleteRow(row);
			fireTableRowsDeleted(row, row);
			row--;
		}	
			if (row == getRowCount() - 1) {
				getData().add(createEmptyRow());
				fireTableRowsInserted(row, row);
			}
				fireTableRowsUpdated(row, row);
//        if (row  >= getRowCount()) {
//            getData().add(createEmptyRow());
//            fireTableRowsInserted(row, row);
//        }
		
		
	}

	private Object[] completeRow(Object newdata, int row, int column) {
		Object[] current = this.createEmptyRow();
		for (int c = 0; c < this.getColumnCount(); c++)
			current[c] = this.getValueAt(row, c);
		current[column] = newdata;
		fireTableCellUpdated(row, column);
		return current;
	}

	/**
	 * Checks to see if the given row is empty such that 
	 * empty rows will automatically be purged. Returns false every time
	 * by default. Override to add activity.
	 * @param row
	 * @return
	 */
	public boolean checkEmpty(int row) {
		return false;
	}


	public List<Object[]> getData(){
		return data;
	}
	
	/** This holds the number of columns. */
	protected int columns;
	
	/** The generic data structure used for tables. 
	 * Can be replaced by overriding the getData Method */
	protected List<Object[]> data;
	
}

