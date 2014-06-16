package view.automata.transitiontable;

import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import util.JFLAPConstants;
import util.view.tables.SelectingEditor;

/**
 * Editor for TuringMachineTransitionTables to make BlankSymbols respond as if there's nothing there.
 * 
 * @author Ian McMahon
 *
 */
public class BlankRemovingEditor extends SelectingEditor{
	
	public BlankRemovingEditor() {
		super();
	}

	public BlankRemovingEditor(JTextField textField) {
		super(textField);
	}

	@Override
	public Runnable createRunnable(JTextComponent jtc) {
		return new BlankRemoveRunnable(jtc);
	}
	
	
	private class BlankRemoveRunnable implements Runnable{

		private JTextComponent myJTC;

		public BlankRemoveRunnable(JTextComponent jtc){
			myJTC = jtc;
			String s = jtc.getText();
			if (s.equals(JFLAPConstants.BLANK)){
				jtc.setText("");
			}
		}

		@Override
		public void run() {
			int caret = myJTC.getText().length();
			myJTC.selectAll();
			myJTC.setCaretPosition(caret);
		}

	}
	

}
