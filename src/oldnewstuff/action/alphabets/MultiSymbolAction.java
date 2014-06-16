package oldnewstuff.action.alphabets;


import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JOptionPane;

import jflap.actions.undo.UndoableAction;
import jflap.errors.BooleanWrapper;
import jflap.errors.JFLAPError;
import jflap.model.automaton.turing.TuringMachine;
import jflap.model.formaldef.FormalDefinition;
import jflap.model.formaldef.alphabets.Alphabet;
import jflap.model.formaldef.alphabets.specific.InputAlphabet;
import jflap.model.formaldef.alphabets.specific.TapeAlphabet;
import jflap.model.formaldef.symbols.Symbol;



public abstract class MultiSymbolAction extends UndoableAction {

	private Collection<Symbol> mySymbols;
	private FormalDefinition myDefinition;
	private Class<? extends Alphabet> myAlphType;

	public MultiSymbolAction(String s, FormalDefinition fd, 
			Class<? extends Alphabet> alph, 
			Collection<Symbol> symbols) {
		super(s);
		myDefinition = fd;
		myAlphType = alph;
		setSymbols(symbols);
	}

	public MultiSymbolAction(String s, FormalDefinition fd,
			Class<? extends Alphabet> alph) {
		this(s, fd, alph, (Collection<Symbol>) null);
	}
	public MultiSymbolAction(String s, FormalDefinition fd,
			Class<? extends Alphabet> alph, String text) {
		this(s, fd, alph, createSymbols(text));
	}

	private static Collection<Symbol> createSymbols(String text) {
		String[] split = text.split(" ");
		List<Symbol> symbols = new ArrayList<Symbol>();
		for (String s: split){
			symbols.add(new Symbol(s));
		}
		return symbols;
	}

	private void setSymbols(Collection<Symbol> symbols) {
		mySymbols = symbols;
	}

	@Override
	protected boolean setFields(ActionEvent e) {
		if (mySymbols == null){
			String text = JOptionPane.showInputDialog(null, "Input new symbol, click OK to complete.\n" +
					"To input multiple symbols, split by a space character");
			if (text == null) //If user input nothing/hit cancel
				return false;
			this.setSymbols(createSymbols(text));
		}
		return true;
	}

	protected BooleanWrapper doRemove() {
		return doGeneric(REMOVE);
	}


	protected BooleanWrapper doAdd() {
		return doGeneric(ADD);
	}

	private static final int ADD = 0,
			REMOVE = 1;

	private BooleanWrapper doGeneric(int type){
		String verb = "";
		switch(type){
		case ADD: verb = "added"; break;
		case REMOVE: verb = "removed"; break;
		}

		List<BooleanWrapper> errors = new ArrayList<BooleanWrapper>();
		boolean shouldshow = false;
		for (Symbol s: mySymbols){
			BooleanWrapper bw = new BooleanWrapper(true);
			switch(type){
			case ADD: bw = myDefinition.addSymbol(myAlphType,s); break;
			case REMOVE:bw = myDefinition.removeSymbol(myAlphType,s); break;
			}
			errors.add(bw);
			if (bw.isError()) shouldshow = true;
		}
		if (shouldshow)
			JFLAPError.show("The following symbols could not be " + verb +":\n" + 
					BooleanWrapper.createErrorLog(errors.toArray(new BooleanWrapper[0])), 
					"Pasting Errors");

		return new BooleanWrapper(true);
	}

	protected Class<? extends Alphabet> getAlphabetClass(){
		return myAlphType;
	}

	protected FormalDefinition getFormalDef(){
		return myDefinition;
	}

	protected Iterable<Symbol> getSymbols(){
		return mySymbols;
	}
}
