package model.algorithms.testinput.parse.cyk;

import java.util.*;

import model.algorithms.testinput.parse.*;
import model.change.events.AdvancedChangeEvent;
import model.grammar.*;
import model.grammar.typetest.GrammarType;
import model.symbols.*;

/**
 * Implementation of the CYK (Cocke-Younger-Kasami) parsing algorithm to
 * determine whether a string is in the language of a given grammar and the
 * trace (set of productions) followed to derive the string
 * 
 * Note: CYKParser was redone in Summer 2012 with improved efficiency and
 * significant code redesign/refactoring. The grammar passed in must be in
 * Chomsky Normal Form (CNF) for the parser to work correctly.
 * 
 * @author Ian McMahon
 */

public class CYKParser extends Parser {

	private List<Production> myAnswerTrace;
	private Set<CYKParseNode> myNodeTable[][];
	private Set<Symbol> mySetTable[][];
	private int myIncrement;
	public static final int CELL_CHANGED = 4;

	public CYKParser(Grammar g) {
		super(g);
	}

	/**
	 * Resets the parser's tables to the length of the target input. Unused
	 * cells are left as null in myNodeTable.
	 */
	@SuppressWarnings("unchecked")
	private void initializeTable(int length) {
		myNodeTable = new Set[length][length];
		mySetTable = new Set[length][length];

		for (int i = 0; i < length; i++) {
			for (int j = i; j < length; j++) {
				myNodeTable[i][j] = new HashSet<CYKParseNode>();
				mySetTable[i][j] = null;
			}
		}
	}

	/**
	 * Adds the terminal productions for each terminal (index i) in the input
	 * string to the CYK parse table at cell [i][i]. Throws an exception if a
	 * terminal cannot be derived (should never happen, but should check just in
	 * case).
	 */
	private boolean addTerminalProductions() {
		ProductionSet productions = getGrammar().getProductionSet();

		for (int i = 0; i < getInput().size(); i++) {
			SymbolString current = getInput().subList(i, i + 1);

			for (Production p : productions) {
				if (p.equalsRHS(current)) {
					CYKParseNode node = new CYKParseNode(p, i);
					myNodeTable[i][i].add(node);
				}
			}
			if (myNodeTable[i][i].isEmpty())
				throw new ParserException(
						"There aren't valid terminal productions!");
		}
		return true;
	}

	/**
	 * Find and add to the parse table the productions in the grammar that can
	 * derive strings of the current increment size.
	 */
	private boolean addNonterminalProductions() {
		int size = getInput().size();
		if (myIncrement >= size)
			return false;
		
		for (int i = 0; i < size; i++) {
			int j =  i + myIncrement;
				// haven't filled out this cell
				if (j < size && myNodeTable[i][j].isEmpty())
					findAllProductions(i, j);
			
		}
		return true;
	}

	/**
	 * Adds all production that can derive the substring from i to j (inclusive)
	 * of the original input, by calculating all possible k: i <= k < j.
	 */
	private void findAllProductions(int i, int j) {
		Grammar g = getGrammar();
		ProductionSet prods = g.getProductionSet();
		
		for (int k = i; k < j; k++) {
			for (Symbol A : getLHSVariablesForNode(i, k)) {
				
				for (Symbol B : getLHSVariablesForNode(k + 1, j)) {
					SymbolString concat = new SymbolString(A, B);
					for (Production p : prods) {
						if (p.equalsRHS(concat)) {
							CYKParseNode node = new CYKParseNode(p, k);
							myNodeTable[i][j].add(node);
						}
					}
				}
			}
		}
	}

	/**
	 * Returns a LeftmostDerivation that can derive the target string.
	 * 
	 */
	public Derivation getDerivation() {
		myAnswerTrace = new ArrayList<Production>();
		Variable start = getGrammar().getStartVariable();

		getTrace(start, 0, getInput().size() - 1);

		Derivation answer = Derivation.createLeftmostDerivation(myAnswerTrace);
		return answer;
	}

	/**
	 * Helper function that modifies <CODE>myAnswerTrace</CODE> and will return
	 * true if and only if it finds a possible derivation of the string
	 * specified by the LHS variable and start and end indexes.
	 * 
	 * @param LHS
	 *            the variable to be checked for possibly being able to derive
	 *            the string.
	 * @param start
	 *            the index of first symbol in the string.
	 * @param end
	 *            the index of final symbol in the string.
	 */
	private boolean getTrace(Variable LHS, int start, int end) {
		ProductionSet productions = getGrammar().getProductionSet();
		
		if (start == end) {
			Terminal character = (Terminal) getInput().get(start);
			Production terminalProduction = new Production(LHS, character);

			for (Production p : productions) {
				if (p.equals(terminalProduction)) {
					myAnswerTrace.add(terminalProduction);
					return true;
				}
			}
			return false;
		}
		for (CYKParseNode node : myNodeTable[start][end]) {
			Production nodeProduction = new Production(LHS, node.getRHS());

			for (Production p : productions) {
				if (p.equals(nodeProduction)) {
					myAnswerTrace.add(nodeProduction);
					if (getTrace(node.getFirstRHSVariable(), start, node.getK())
							&& getTrace(node.getSecondRHSVariable(),
									node.getK() + 1, end)) {
						return true;
					}
					myAnswerTrace.remove(nodeProduction);
					return false;
				}
			}
		}
		return false;
	}

	@Override
	public String getDescriptionName() {
		return "CYK Parser";
	}

	@Override
	public String getDescription() {
		return "This is a CYK Parser!";
	}

	/**
	 * Return CNF enum from GrammarType class; CYK parser requires that the
	 * grammar be in Chomsky Normal Form
	 */
	@Override
	public GrammarType getRequiredGrammarType() throws ParserException {
		return GrammarType.CHOMSKY_NORMAL_FORM;
	}

	@Override
	public boolean isAccept() {
		return mySetTable[0][getInput().size() - 1].contains(getGrammar()
				.getStartVariable());
	}

	@Override
	public boolean isDone() {
		return getInput() != null && myIncrement > getInput().size();
	}

	/**
	 * Precalculates the next row/diagonal, returns true unless there are no
	 * Variables that can derive a terminal in the first step or it is trying to
	 * calculate a row that doesn't exist (myIncrement >= input.size()).
	 */
	private boolean calculateNextRow() {
		boolean row;
		if (myIncrement == 0)
			row = addTerminalProductions();
		else
			row = addNonterminalProductions();
		myIncrement++;
		return row;
	}

	@Override
	public boolean resetInternalStateOnly() {

		myAnswerTrace = new ArrayList<Production>();
		myIncrement = 0;

		if (getInput() != null) {
			initializeTable(getInput().size());
			return calculateNextRow();
		}

		return true;
	}

	@Override
	public boolean setInput(SymbolString string) {
		if (string != null && string.size() == 0) {
			throw new ParserException(
					"CNF Grammars cannot produce empty strings!");
		}
		return super.setInput(string);
	}

	/**
	 * Returns the set of Variables on the LHS of each cykParseNode at
	 * myNodeTable[row][col]
	 */
	private Set<Symbol> getLHSVariablesForNode(int row, int col) {
		Set<Symbol> set = new TreeSet<Symbol>();

		for (CYKParseNode node : myNodeTable[row][col]) {
			set.add(node.getLHS());
		}
		return set;
	}

	@Override
	public boolean stepParser() {
		int previousIncrement = myIncrement - 1;
		for (int i = 0; i + previousIncrement < getInput().size(); i++) {
			autofillCell(i, i + previousIncrement);
		}
		return true;
	}

	/**
	 * Completes the cell specified by row and col, inserting the correct set of
	 * Symbols at that location.
	 */
	public void autofillCell(int row, int col) {
		if (!isActive(row, col))
			return;
		Set<Symbol> selectedSet = getLHSVariablesForNode(row, col);
		insertSet(row, col, selectedSet);
	}

	/**
	 * Returns true if the set is equal to the LHS Variables at
	 * myNodeTable[row][col]
	 */
	private boolean validate(int row, int col, Set<Symbol> set) {
		Set<Symbol> valid = getLHSVariablesForNode(row, col);
		return set.equals(valid);
	}

	/**
	 * Changes the value of mySetTable[row][col] to <i>set</i>, increments the
	 * row/diagonal if the current row is complete, and returns whether or not
	 * the entered set is the correct one.
	 */
	public boolean insertSet(int row, int col, Set<Symbol> set) {
		boolean valid = validate(row, col, set);
		mySetTable[row][col] = set;
		if (rowIsComplete())
			calculateNextRow();
		distributeChange(new AdvancedChangeEvent(this, CELL_CHANGED,
				mySetTable[row][col]));

		return valid;
	}

	/**
	 * Returns true if all values in the current row/diagonal of mySetTable are
	 * the correct sets.
	 */
	private boolean rowIsComplete() {
		int previousIncrement = myIncrement - 1;
		for (int i = 0; i + previousIncrement < getInput().size(); i++) {
			if (!getLHSVariablesForNode(i, i + previousIncrement).equals(
					mySetTable[i][i + previousIncrement]))
				return false;
		}
		return true;
	}

	/**
	 * Returns the set of Symbols stored at mySetTable[row][col]
	 */
	public Set<Symbol> getValueAt(int row, int col) {
		return mySetTable[row][col];
	}

	/**
	 * Returns true if the cell is a member of the current diagonal/row.
	 */
	public boolean isActive(int row, int col) {
		return col - row == myIncrement - 1;
	}
}
