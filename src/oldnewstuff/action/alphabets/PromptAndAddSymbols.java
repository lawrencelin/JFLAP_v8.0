package oldnewstuff.action.alphabets;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import model.formaldef.components.alphabets.Alphabet;
import model.symbols.Symbol;

public class PromptAndAddSymbols extends AddMultipleSymbolsAction {

	
	public PromptAndAddSymbols(Alphabet alph) {
		super(alph);
	}

	@Override
	protected boolean setFields(ActionEvent e){ 
		String text = JOptionPane.showInputDialog(null, "Input new symbol, click OK to complete.\n" +
				"To input multiple symbols, split by a space character");
		if (text == null) //If user input nothing/hit cancel
			return false;
		this.setSymbols(createSymbols(text));
		return true;
	}
	
	private static Symbol[] createSymbols(String text) {
		String[] split = text.split(" ");
		List<Symbol> symbols = new ArrayList<Symbol>();
		for (String s: split){
			symbols.add(new Symbol(s));
		}
		return symbols.toArray(new Symbol[0]);
	}
}
