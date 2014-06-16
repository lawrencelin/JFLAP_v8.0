package view.grammar.parsing.cyk;

import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import util.view.tables.SelectingEditor;

/**
 * Editor used in CYK ParseTablePanel to remove brackets and commas, so that
 * user input can be parsed and displayed easier and correctly.
 * @author Ian McMahon
 *
 */
public class SetCellEditor extends SelectingEditor {

	public SetCellEditor() {
		super();
	}

	public SetCellEditor(JTextField textField) {
		super(textField);
	}

	@Override
	public Runnable createRunnable(JTextComponent jtc) {
		return new EmptySetRunnable(jtc);
	}
	
	private class EmptySetRunnable implements Runnable {

		private JTextComponent myJTC;

		public EmptySetRunnable(JTextComponent jtc) {
			myJTC = jtc;
			String s = jtc.getText();
			s = s.replaceAll("\\[", "");
			s = s.replaceAll("\\]", "");
			s = s.replaceAll(", ", "");
			jtc.setText(s);
		}

		@Override
		public void run() {
			int caret = myJTC.getText().length();
			myJTC.selectAll();
			myJTC.setCaretPosition(caret);
		}

	}
}
