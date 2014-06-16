package model.algorithms.testinput.parse;

import java.util.LinkedList;

import universe.preferences.JFLAPPreferences;

import model.grammar.Grammar;
import model.grammar.typetest.GrammarType;
import model.symbols.SymbolString;

public abstract class StackUsingParser extends Parser {

	private SymbolString myUnprocessedInput;
	
	public StackUsingParser(Grammar g) {
		super(g);
	}
	
	public abstract LinkedList getStack();
	
	public SymbolString getUnprocessedInput(){
		return myUnprocessedInput;
	}

	
	@Override
	public boolean resetInternalStateOnly() {
		myUnprocessedInput = initUnprocessedInput();
		return true;
	}

	private SymbolString initUnprocessedInput() {
		SymbolString input = getInput();
		if (input == null) return null;
		SymbolString unp = input.copy();
		unp.add(JFLAPPreferences.getEndOfStringMarker());
		return unp;
	}
	

}
