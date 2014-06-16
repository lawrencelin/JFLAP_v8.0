package oldnewstuff.action.alphabets;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Collection;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

import util.view.undo.UndoableAction;

import model.formaldef.components.alphabets.Alphabet;
import model.symbols.Symbol;




public abstract class AbstractEditSymbolsAction extends UndoableAction{

	private Alphabet myAlphabet;
	private Symbol[] mySymbol;
	
	public AbstractEditSymbolsAction(String label, Alphabet alph, Symbol ... s) {
		super(label);
		setAlphabet(alph);
		setSymbols(s);
		this.putValue(MNEMONIC_KEY, 
				(Integer)(int)((String) this.getValue(NAME)).charAt(0));
	}

	public Symbol getSymbol(int i) {
		return mySymbol[i];
	}


	public Symbol[] getSymbols() {
		return mySymbol;
	}

	
	public void setSymbols(Symbol[] s) {
		mySymbol = s;
	}

	public Alphabet getAlphabet() {
		return myAlphabet;
	}

	public void setAlphabet(Alphabet alph) {
		this.myAlphabet = alph;
	}
	
}
