package model.algorithms.testinput.parse.lr;

import java.math.MathContext;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import universe.preferences.JFLAPPreferences;
import util.UtilFunctions;

import debug.JFLAPDebug;


import model.algorithms.testinput.parse.FirstFollowTable;
import model.algorithms.testinput.parse.ParseTable;
import model.algorithms.testinput.parse.lr.rules.AcceptRule;
import model.algorithms.testinput.parse.lr.rules.EndReduceRule;
import model.algorithms.testinput.parse.lr.rules.ReduceRule;
import model.algorithms.testinput.parse.lr.rules.SLR1rule;
import model.algorithms.testinput.parse.lr.rules.ShiftRule;
import model.algorithms.testinput.parse.lr.rules.StateUsingRule;
import model.automata.State;
import model.automata.acceptors.Acceptor;
import model.automata.acceptors.fsa.FSATransition;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.Terminal;
import model.grammar.Variable;
import model.symbols.Symbol;

public class SLR1ParseTable extends ParseTable{


	private FirstFollowTable myFirstFollow;
	private SLR1DFA mySLR1DFA;
	private Set<SLR1DFAState> myIncompleted;
	private Grammar myModifiedGrammar;
	private Symbol[] myColumns;
	private int myNumRows;
	private Set<SLR1rule> myTable[][];
	private ArrayList<Production> myOrderedProductions;
	private Grammar myOriginalGrammar;

	public SLR1ParseTable(Grammar g){
		this(new FirstFollowTable(g), new SLR1DFA(g), true);
	}

	public SLR1ParseTable(FirstFollowTable table, 
			SLR1DFA dfa, boolean complete) {
		Set<Symbol> col = new TreeSet<Symbol>(dfa.getInputAlphabet());
		col.add(JFLAPPreferences.getEndOfStringMarker());
		myColumns = col.toArray(new Symbol[0]);
		myNumRows = dfa.getStates().size();
		initTable();
		myFirstFollow = table;
		mySLR1DFA = dfa;
		myModifiedGrammar = mySLR1DFA.getGrammar();
		myOriginalGrammar = myFirstFollow.getAssociatedGrammar();
		myOrderedProductions = new ArrayList<Production>(myModifiedGrammar.getProductionSet());
		myIncompleted = new TreeSet<SLR1DFAState>();
		for (State s: dfa.getStates()){
			myIncompleted.add((SLR1DFAState) s);
		}
		if (complete)
			completeTable();
	}

	private void initTable() {
		myTable = new Set[myNumRows][myColumns.length];
		for (Set<SLR1rule>[] set: myTable){
			for (int i = 0; i<set.length;i++){
				set[i] = new TreeSet<SLR1rule>();
			}
		}
	}

	private void completeTable() {
		while (this.isIncomplete()){
			autoCompleteState();
		}
	}

	public SLR1DFAState autoCompleteState() {
		SLR1DFAState state = getFirstIncompleteState();
		if (state == null)
			return null;
		
		addAllRulesForState(state);
		
		
		return state;
	}

	public void addAllRulesForState(SLR1DFAState from) {
		Set<FSATransition> trans = mySLR1DFA.getTransitions().getTransitionsFromState(from);
		for (FSATransition t: trans){
			Symbol s = t.getInput()[0];
			SLR1DFAState to = (SLR1DFAState) t.getToState();
			SLR1rule newRule;
			if (Grammar.isTerminal(s)){
				newRule = new ShiftRule(to);
			}
			else {
				newRule = new EndReduceRule(to);
			}
			addRule(newRule, from, s);
		}
		
		if (Acceptor.isFinalState(mySLR1DFA, from)){
			for (SLR1Production p: from.getReduceProductions()){
				addReduceRules(from, p);
			}
		}
		myIncompleted.remove(from);
	}

	private void addReduceRules(SLR1DFAState from, SLR1Production p) {
		Variable A = (Variable) p.getLHS()[0];
		if (Grammar.isStartVariable(A, myModifiedGrammar )){
			addRule(new AcceptRule(), from, 
					JFLAPPreferences.getEndOfStringMarker());
			return;
		}
		
		Set<Terminal> followA = myFirstFollow.getFollow(A);
		for (Terminal t: followA){
			int pIndex = myOrderedProductions.indexOf(p.createNormalProduction());
			addRule(new ReduceRule(pIndex), from, t);
		}
		
	}

	public Production getProductionForIndex(int i) {
		return myOrderedProductions.get(i);
	}

	public void addRule(SLR1rule rule, SLR1DFAState s, Symbol sym) {
		int r = s.getID();
		int c = getColumnForSymbol(sym);
		myTable[r][c].add(rule);
	}

	private int getColumnForSymbol(Symbol sym) {
		for (int i =0; i< myColumns.length; i++){
			if (myColumns[i].equals(sym))
				return i;
		}
		return -1;
	}

	private SLR1DFAState getFirstIncompleteState() {
		if (!isIncomplete()) return null;
		return myIncompleted.toArray(new SLR1DFAState[0])[0];
	}

	private boolean isIncomplete() {
		return !myIncompleted.isEmpty();
	}

	public Grammar getGrammar() {
		return myModifiedGrammar;
	}

	public SLR1DFA getDFA() {
		return mySLR1DFA;
	}

	public SLR1rule getRule(State state, Symbol s) {
		int r = state.getID();
		int c = getColumnForSymbol(s);
		return getValueAt(r, c);
	}
	
	@Override
	public String toString() {
		String str = "\t" + UtilFunctions.toDelimitedString(myColumns, "\t") + "\n";
		for (int r = 0; r < myNumRows; r++){
		str += r + "\t" + UtilFunctions.toDelimitedString(myTable[r],"\t") +"\n";
		}
		return str;
	}

	@Override
	public int getColumnCount() {
		return myColumns.length;
	}

	@Override
	public int getRowCount() {
		return myNumRows;
	}

	@Override
	public SLR1rule getValueAt(int r, int c) {
		Set<SLR1rule> rules = myTable[r][c];
		if (rules.isEmpty())
			return null;
		return (SLR1rule) rules.toArray()[0];
	}
}
