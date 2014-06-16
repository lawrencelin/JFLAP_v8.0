package view.action;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import model.change.events.SpecialSymbolSetEvent;
import model.symbols.SpecialSymbol;
import model.symbols.Symbol;
import model.undo.IUndoRedo;
import model.undo.UndoKeeper;

public class SetSpecialSymbolAction extends UndoingAction {

	private SpecialSymbol mySpecialSymbol;

	public SetSpecialSymbolAction(SpecialSymbol ss, UndoKeeper keeper) {
		super("Set " + ss.getDescriptionName(), keeper);
		mySpecialSymbol = ss;
	}

	@Override
	public IUndoRedo createEvent(ActionEvent e) {
		String toString = JOptionPane.showInputDialog(null, 
				"Enter the new " + mySpecialSymbol.getDescriptionName() +
					" , click ok to complete",
				mySpecialSymbol.isComplete().isTrue() ?
						mySpecialSymbol.getSymbol().toString():
						"");
		if (toString == null) return null;
		Symbol from = mySpecialSymbol.getSymbol();
		from = from == null ? null : from.copy();
		Symbol to = new Symbol(toString);
		
		return new SpecialSymbolSetEvent(mySpecialSymbol, from, to);
	}

}
