package oldnewstuff.action.alphabets;


import java.awt.Container;
import java.awt.event.ActionEvent;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.swing.JOptionPane;

import errors.BooleanWrapper;

import model.formaldef.components.alphabets.Alphabet;
import model.symbols.Symbol;



public class AddMultipleSymbolsAction extends AbstractEditSymbolsAction {

	public AddMultipleSymbolsAction(Alphabet alph, Symbol ... symbols) {
		super("Add Symbols",alph, symbols);
	}


	@Override
	public boolean undo() {
		return this.getAlphabet().removeAll(Arrays.asList(this.getSymbols()));
	}
	@Override
	public boolean redo() {
		return this.getAlphabet().addAll(Arrays.asList(this.getSymbols()));
	}


	@Override
	protected boolean setFields(ActionEvent e) {
		return true;
	}

}
