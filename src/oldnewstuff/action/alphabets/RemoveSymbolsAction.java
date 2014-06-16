package oldnewstuff.action.alphabets;


import java.awt.event.ActionEvent;
import java.util.Arrays;

import javax.swing.JOptionPane;

import oldnewstuff.action.formaldef.DefinitionCloningAction;
import view.formaldef.UsesDefinition;

import model.formaldef.FormalDefinition;
import model.formaldef.components.alphabets.Alphabet;
import model.symbols.Symbol;




public class RemoveSymbolsAction extends AbstractEditSymbolsAction {



	public RemoveSymbolsAction(Alphabet a, Symbol ... s) {
		super("Remove", a, s);
	}

	@Override
	protected boolean setFields(ActionEvent e) {
		
		return true;
	}

	@Override
	public boolean undo() {
		return this.getAlphabet().removeAll(Arrays.asList(this.getSymbols()));
	}

	@Override
	public boolean redo() {
		// TODO Auto-generated method stub
		return false;
	}




}
