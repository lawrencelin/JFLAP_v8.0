package view.grammar.productions;

import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import debug.JFLAPDebug;

import universe.preferences.JFLAPPreferences;
import util.view.tables.SelectingEditor;

public class LambdaRemovingEditor extends SelectingEditor {

	public LambdaRemovingEditor() {
		super();
	}

	public LambdaRemovingEditor(JTextField textField) {
		super(textField);
	}

	@Override
	public Runnable createRunnable(JTextComponent jtc) {
		return new LambdaRemoveRunnable(jtc);
	}
	
	
	private class LambdaRemoveRunnable implements Runnable{

		private JTextComponent myJTC;

		public LambdaRemoveRunnable(JTextComponent jtc){
			myJTC = jtc;
			String s = jtc.getText();
			if (s.equals(JFLAPPreferences.getEmptyString())){
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
