package view.action;


import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import debug.JFLAPDebug;

import model.change.events.AdvancedUndoableEvent;
import model.change.events.SetToEvent;
import model.formaldef.components.alphabets.Alphabet;
import model.symbols.Symbol;
import model.undo.IUndoRedo;
import model.undo.UndoKeeper;



public class ModifySymbolAction extends UndoingAction {

	private Symbol mySymbol;

	public ModifySymbolAction(Symbol s, UndoKeeper keeper) {
		super("Modify " + s.getDescriptionName(), keeper);
		mySymbol = s;
	}


	

	@Override
	public IUndoRedo createEvent(ActionEvent e) {
		String from = mySymbol.getString(),
				to = JOptionPane.showInputDialog(null, 
				"Modify the symbol, click ok to complete",
				from);
		if (to == null)
			return null;
		return new SetToEvent<Symbol>(mySymbol, 
										  new Symbol(from), 
										  new Symbol(to));
	}

}
