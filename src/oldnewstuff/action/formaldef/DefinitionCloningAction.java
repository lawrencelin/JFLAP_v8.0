package oldnewstuff.action.formaldef;


import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import util.view.undo.UndoableAction;
import view.formaldef.UsesDefinition;

import model.formaldef.FormalDefinition;





public abstract class DefinitionCloningAction extends UndoableAction {

	private FormalDefinition myOldDef;
	private FormalDefinition myNewDef;
	private UsesDefinition myUsingDef;


	public DefinitionCloningAction(String name, FormalDefinition fd, UsesDefinition user) {
		super(name);
		myOldDef = fd;
		myUsingDef = user;
	}


	@Override
	public boolean redo() {
		myUsingDef.setDefintion(myNewDef);
		return true;
	}

	@Override
	public boolean undo() {
		myUsingDef.setDefintion(myOldDef);
		return true;
	}


	@Override
	protected boolean setFields(ActionEvent e) {
//		int n = 0;
//		if (getAlphabetClass().getClass().isAssignableFrom(TapeAlphabet.class)){
//			n = JOptionPane.showConfirmDialog(
//				    null,
//				    "Removing this symbol from the " + getAlphabetClass().getName() + "\n" +
//				    		" will also remove it from the Input Alphabet.", //CHEATING!!!
//				    "Warning",
//				    JOptionPane.OK_CANCEL_OPTION);
//		}
//		else{
//			n = JOptionPane.showConfirmDialog(
//				    null,
//				    "This symbol will be permanently removed from the " + getAlphabetClass().getName() + ".",
//				    "Warning",
//				    JOptionPane.OK_CANCEL_OPTION);
//		}
//		if (n != 0)
//			return false;
		
		myNewDef = myOldDef.clone();
		applyChange(myNewDef);
		return true;
	}


	protected abstract void applyChange(FormalDefinition newDef);

}
