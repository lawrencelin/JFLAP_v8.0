package util.view.tables;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

import debug.JFLAPDebug;


public class SelectingEditor extends DefaultCellEditor {

	public SelectingEditor() {
		this(new JTextField());
	}

	public SelectingEditor(JTextField textField) {
		super(textField);
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		Component c = super.getTableCellEditorComponent(table, value, isSelected, row, column);
		if ( c instanceof JTextComponent) {
			JTextComponent jtc = (JTextComponent)c;
			jtc.requestFocus();
			SwingUtilities.invokeLater(createRunnable(jtc));
		}
		return c;
	}

	public Runnable createRunnable(JTextComponent jtc) {
		return new SelectionRunnable(jtc);
	}

	private class SelectionRunnable implements Runnable{

		private JTextComponent myJTC;

		public SelectionRunnable(JTextComponent jtc){
			myJTC = jtc;
		}

		@Override
		public void run() {
			myJTC.selectAll();
		}

	}

}
