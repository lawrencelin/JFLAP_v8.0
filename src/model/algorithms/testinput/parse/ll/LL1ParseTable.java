package model.algorithms.testinput.parse.ll;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;


import universe.preferences.JFLAPPreferences;
import util.UtilFunctions;


import model.algorithms.testinput.parse.FirstFollowTable;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.Terminal;
import model.grammar.Variable;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public class LL1ParseTable {

	private Set<Symbol[]> myTable[][];
	private FirstFollowTable myFirstFollow;
	private Grammar myGrammar;
	private Variable[] myRows;
	private Terminal[] myColumns;

	public LL1ParseTable(FirstFollowTable table, boolean complete) {
		myFirstFollow = table;
		myGrammar = myFirstFollow.getAssociatedGrammar();
		myRows = myGrammar.getVariables().toArray(new Variable[0]);
		
		myColumns = myGrammar.getTerminals().toArray(new Terminal[0]);
		myColumns = UtilFunctions.combine(myColumns,
				JFLAPPreferences.getEndOfStringMarker());
		myTable = new Set[myRows.length][myColumns.length];
		for (int i = 0; i< myRows.length;i++){
			for (int j = 0; j < myColumns.length; j++){
				myTable[i][j] = new TreeSet<Symbol[]>();
			}
		}
		if (complete)
			completeTable();
	}
	
	

	public LL1ParseTable(Grammar g){
		this (g, true);
	}



	public LL1ParseTable(Grammar g, boolean complete) {
		this(new FirstFollowTable(g), complete);
	}



	private void completeTable() {
		for (Production p: myGrammar.getProductionSet()){
			addEntryForProduction(p);
		}
	}


	public boolean addEntryForProduction(Production p) {
		Set<Terminal> terms = myFirstFollow.retrieveFirstSet(p.getRHS());
		Terminal empty = JFLAPPreferences.getSubForEmptyString();
		Variable A = (Variable) p.getLHS()[0];
		if (terms.contains(empty)){
			terms = myFirstFollow.getFollow(A);
		}
		
		boolean changed = false;
		
		for (Terminal t: terms){
			changed = setValue(p.getRHS(), A, t) || changed;
		}
		
		return changed;
		
	}

	public boolean setValue(Symbol[] symbols, Variable a, Terminal t) {
		int r = getRowForVar(a);
		int c = getColForTerm(t);
		return setValue(symbols, r, c);
	}
	


	private boolean setValue(Symbol[] rhs, int r, int c) {
		return myTable[r][c].add(rhs);
	}



	private int getColForTerm(Terminal t) {
		for(int i = 0; i < myColumns.length; i++){
			if (myColumns[i].equals(t))
				return i;
		}
		return -1;
	}



	private int getRowForVar(Variable v) {
		for(int i = 0; i < myRows.length; i++){
			if (myRows[i].equals(v))
				return i;
		}
		return -1;
	}



	public SymbolString get(Variable v, Terminal t) {
		return new SymbolString(get(getRowForVar(v), getColForTerm(t)));
	}



	private Symbol[] get(int r, int c) {
		Symbol[][] entry = myTable[r][c].toArray(new Symbol[0][]);
		return entry.length >= 1 ? entry[0]: null;
	}

}
