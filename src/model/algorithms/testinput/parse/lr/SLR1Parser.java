package model.algorithms.testinput.parse.lr;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import universe.preferences.JFLAPPreferences;


import debug.JFLAPDebug;


import model.algorithms.testinput.parse.Derivation;
import model.algorithms.testinput.parse.Parser;
import model.algorithms.testinput.parse.ParserException;
import model.algorithms.testinput.parse.StackUsingParser;
import model.algorithms.testinput.parse.lr.rules.AcceptRule;
import model.algorithms.testinput.parse.lr.rules.EndReduceRule;
import model.algorithms.testinput.parse.lr.rules.ReduceRule;
import model.algorithms.testinput.parse.lr.rules.SLR1rule;
import model.algorithms.testinput.parse.lr.rules.ShiftRule;
import model.algorithms.testinput.parse.lr.rules.StateUsingRule;
import model.automata.State;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.Terminal;
import model.grammar.typetest.GrammarType;
import model.symbols.Symbol;
import model.symbols.SymbolString;


public class SLR1Parser extends StackUsingParser {

	private SLR1ParseTable myTable;
	private List<Production> myDerivation;
	private LinkedList<Object> myStack;
	private SLR1DFA mySLR1DFA;
	private TableLocation myNextRule;
	private TableLocation myCurrentRule;

	public SLR1Parser(Grammar g) {
		this(new SLR1ParseTable(g));
	}

	public SLR1Parser(SLR1ParseTable parseTable) {
		super(parseTable.getGrammar());
		myTable = parseTable;
		mySLR1DFA = parseTable.getDFA();
	}

	@Override
	public String getDescriptionName() {
		return "SLR(1) Parser";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean resetInternalStateOnly() {
		myDerivation = new ArrayList<Production>();
		myDerivation.add(this.getGrammar().getStartProductions()[0]);
		myStack = new LinkedList<Object>();
		myNextRule = null;
		myCurrentRule = null;
		
		if (mySLR1DFA == null) return true;
		super.resetInternalStateOnly();
		SLR1DFAState start = (SLR1DFAState) mySLR1DFA.getStartState();
		myStack.addFirst(start.getID());

		myNextRule = new TableLocation(start, getUnprocessedInput().getFirst());
		return true;
	}

	@Override
	public boolean isAccept() {
		return isDone() && (retrieveCurrentRule() instanceof AcceptRule) ;
	}

	@Override
	public boolean isDone() {
		return myNextRule == null;
	}

	@Override
	public GrammarType getRequiredGrammarType() throws ParserException {
		return GrammarType.CONTEXT_FREE;
	}

	@Override
	public boolean stepParser() {
		myCurrentRule = myNextRule;
//		JFLAPDebug.print("Next rule: " + retrieveCurrentRule());
		applyCurrentRule();
		return true;
	}

	private void applyCurrentRule() {
		SLR1rule current = retrieveCurrentRule();
		State nextState = null;
		Symbol nextCol = null;
		if (current instanceof StateUsingRule){
			myStack.addFirst(myCurrentRule.symbol);
			nextState = ((StateUsingRule) current).getToState();
			myStack.addFirst(nextState.getID());
			if (current instanceof ShiftRule){
				getUnprocessedInput().pollFirst();
			}
			nextCol = getUnprocessedInput().peekFirst();
		}
		else if(current instanceof ReduceRule){
			int i = ((ReduceRule)current).getProductionIndex();
			Production p = myTable.getProductionForIndex(i);
			for (int k = 0; k< p.getRHS().length; k++){
				myStack.pollFirst();
				myStack.pollFirst();
			}
			nextState = getStateForID((Integer) myStack.peekFirst());
			nextCol = p.getLHS()[0];
			myDerivation.add(p);
		}
		else{
			myNextRule = null;
			return;
		}
		
		myNextRule = new TableLocation(nextState, nextCol);
	}

	private SLR1DFAState getStateForID(int id) {
		return (SLR1DFAState) this.mySLR1DFA.getStates().getStateWithID(id);
	}

	private SLR1rule retrieveCurrentRule() {
		return myTable.getRule(myCurrentRule.state, myCurrentRule.symbol);
	}

	@Override
	public Derivation getDerivation() {
		return Derivation.createRightmostDerivation(myDerivation, true);
	}
	
	private class TableLocation{
		public State state;
		public Symbol symbol;

		public TableLocation(State nextState, Symbol sym){
			state = nextState;
			symbol = sym;
		}
	}

	@Override
	public LinkedList getStack() {
		return myStack;
	}
	
}
