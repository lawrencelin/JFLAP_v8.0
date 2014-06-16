package model.algorithms.testinput.parse.ll;

import universe.preferences.JFLAPPreferences;
import model.algorithms.AlgorithmException;
import model.algorithms.steppable.AlgorithmStep;
import model.algorithms.testinput.parse.Derivation;
import model.algorithms.testinput.parse.Parser;
import model.algorithms.testinput.parse.ParserException;
import model.algorithms.testinput.parse.StackUsingParser;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.Terminal;
import model.grammar.Variable;
import model.grammar.typetest.GrammarType;
import model.symbols.Symbol;
import model.symbols.SymbolString;


public class LL1Parser extends StackUsingParser {

	private LL1ParseTable myParseTable;
	private SymbolString myStack;
	private Derivation myDerivation;
	private SymbolString mySymbolsToAdd;

	public LL1Parser(Grammar g) {
		this(g, new LL1ParseTable(g));
	}

	public LL1Parser(Grammar g, LL1ParseTable table) {
		super(g);
		myParseTable = table;
	}

	@Override
	public String getDescriptionName() {
		return "LL1 parser";
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public AlgorithmStep[] initializeAllSteps() {
		AlgorithmStep[] steps = super.initializeAllSteps();
		return new AlgorithmStep[]{new AddSymbolToStackStep(),
				new RemoveMatchStep(),
				steps[0]};
	}
	
	@Override
	public boolean resetInternalStateOnly() {
		super.resetInternalStateOnly();
		myDerivation = new Derivation(createEmptyStart());
		myStack = new SymbolString(getGrammar().getStartVariable());
		mySymbolsToAdd = new SymbolString();
		return true;
	}

	private Production createEmptyStart() {
		return new Production(new SymbolString(),
							new SymbolString(getGrammar().getStartVariable()));
	}

	@Override
	public boolean isAccept() {
		Symbol eos = JFLAPPreferences.getEndOfStringMarker();
		return isDone() && 
				myStack.isEmpty() &&
				getUnprocessedInput().size() == 1 &&
				getUnprocessedInput().getFirst().equals(eos) ;
	}

	@Override
	public boolean isDone() {
		return mySymbolsToAdd.isEmpty() && 
				!hasMatchingTerminal() &&
				getCurrentEntry() == null;
	}

	@Override
	public GrammarType getRequiredGrammarType() throws ParserException {
		return GrammarType.LL1;
	}

	@Override
	public boolean stepParser() {

		SymbolString toAdd = getCurrentEntry();
		Variable v = (Variable) myStack.pollFirst();

		if (isEmptyString(toAdd))
			toAdd = new SymbolString();

		mySymbolsToAdd.addAll(toAdd);
		
		
		myDerivation.addLeftmostStep(new Production(v, toAdd));
		return true;
	}

	private boolean isEmptyString(SymbolString toAdd) {
		Terminal empty = JFLAPPreferences.getSubForEmptyString();
		return toAdd.startsWith(empty);
	}

	@Override
	public Derivation getDerivation() {
		return myDerivation;
	}
	
	public SymbolString getStack() {
		return new SymbolString(myStack);
	}

	public Terminal removeMatchingTerminal() {
		getUnprocessedInput().removeFirst();
		return (Terminal) myStack.pollFirst();
	}

	public boolean hasMatchingTerminal() {
		return myStack.size() > 0 && 
				getUnprocessedInput().getFirst().equals(myStack.peekFirst());
	}
	
	private SymbolString getCurrentEntry() {
		Symbol s = myStack.peekFirst();
		if (s == null || Grammar.isTerminal(s))
			return null;
		Terminal t = (Terminal) getUnprocessedInput().getFirst();
		Variable V = (Variable) s;
		
		return myParseTable.get(V,t);
	}

	public boolean addSymbolToStack() {
		myStack.addFirst(mySymbolsToAdd.pollLast());
		return true;
	}

	private class AddSymbolToStackStep implements AlgorithmStep{
	
		@Override
		public String getDescriptionName() {
			return "Add Symbol to Stack";
		}
	
		@Override
		public String getDescription() {
			return null;
		}
	
		@Override
		public boolean execute() throws AlgorithmException {
			return addSymbolToStack();
		}
	
		@Override
		public boolean isComplete() {
			return mySymbolsToAdd.isEmpty();
		}
		
	}

	private class RemoveMatchStep implements AlgorithmStep {

		@Override
		public String getDescriptionName() {
			return "Remove matching Terminals";
		}

		@Override
		public String getDescription() {
			return null;
		}

		@Override
		public boolean execute() throws AlgorithmException {
			return removeMatchingTerminal() != null;
		}

		@Override
		public boolean isComplete() {
			return !hasMatchingTerminal();
		}
		
	}
}
