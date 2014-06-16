package model.algorithms.testinput.parse.brute;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.event.ChangeEvent;

import model.algorithms.testinput.parse.Derivation;
import model.algorithms.testinput.parse.Parser;
import model.algorithms.testinput.parse.ParserException;
import model.algorithms.transform.grammar.UselessProductionRemover;
import model.change.events.AdvancedChangeEvent;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.ProductionSet;
import model.grammar.typetest.GrammarType;
import model.grammar.typetest.matchers.ContextFreeChecker;
import model.symbols.Symbol;
import model.symbols.SymbolString;

/**
 * Brute force parser used for unrestricted grammars.
 * 
 * Note: Brute force parser was re-implemented in Summer 2012 to conform with
 * the new parser hierarchy and new classes.
 * 
 * @author Ian McMahon
 * @author Peggy Li
 * @author Julian Genkins
 * 
 */

public class UnrestrictedBruteParser extends Parser {
	public static final int MAX_REACHED = 2, LEVEL_CHANGED = 4;
	private static final int MAX_INCREMENT = 100000;
	private int myCapacity;

	private LinkedList<Derivation> myDerivationsQueue;
	private int myNodesGenerated, maxLHSsize;
	private Set<SymbolString> mySententialsSeen;
	private Set<Symbol> mySmallerSet;

	/**
	 * Static constructor used to create the best matching brute force parser
	 * based on grammar type.
	 */
	public static UnrestrictedBruteParser createNewBruteParser(Grammar g) {
		if (new ContextFreeChecker().matchesGrammar(g)) {
			return new RestrictedBruteParser(g);
		}
		return new UnrestrictedBruteParser(g);
	}

	public UnrestrictedBruteParser(Grammar g) {
		super(g);
		myCapacity = 100000;
		mySmallerSet = Collections.unmodifiableSet(smallerSymbols(g));
		maxLHSsize = g.getProductionSet().getMaxLHSLength();
	}

	@Override
	public String getDescriptionName() {
		return "Brute Force Parser";
	}

	@Override
	public String getDescription() {
		return "Brute force parsing implementation";
	}

	@Override
	public boolean resetInternalStateOnly() {
		myNodesGenerated = 0;
		mySententialsSeen = new HashSet<SymbolString>();
		myDerivationsQueue = new LinkedList<Derivation>();
		return true;
	}

	@Override
	public boolean isAccept() {
		return getDerivation() != null;
	}

	@Override
	public boolean isDone() {
		return myNodesGenerated > 0
				&& (capacityReached() || myDerivationsQueue.isEmpty() || isAccept());
	}

	@Override
	public GrammarType getRequiredGrammarType() throws ParserException {
		return GrammarType.UNRESTRICTED;
	}

	@Override
	public Derivation getDerivation() {
		if (!myDerivationsQueue.isEmpty()) {
			for (Derivation d : myDerivationsQueue)
				if (d.createResult().equals(getInput())) {
					return d;
				}
		}
		return null;
	}

	private boolean capacityReached() {
		return myNodesGenerated >= myCapacity;
	}

	@Override
	public boolean stepParser() {
		if (myNodesGenerated == 0)
			initializeQueue();
		else
			makeNextReplacement();

		notifyNextLevel();
		if (capacityReached()) {
			distributeChange(new AdvancedChangeEvent(this, MAX_REACHED,
					myCapacity));
		}
		return true;
	}

	/**
	 * Updates listeners (most importantly BruteParseTablePanel's model) that
	 * the current step has been completed, passes on the level, number of
	 * nodes, and current sentential-form derivations.
	 */
	private void notifyNextLevel() {
		List<SymbolString> currentDerivs = new ArrayList<SymbolString>();
		for (Derivation d : myDerivationsQueue) {
			currentDerivs.add(d.createResult());
		}
		distributeChange(new AdvancedChangeEvent(this, LEVEL_CHANGED,
				getLevel(), getNumberOfNodes(), currentDerivs));
	}

	/**
	 * First step in the algorithm, puts each start production in the derivation
	 * queue and sets the minimum number of steps that the parser can do.
	 */
	private void initializeQueue() {
		Grammar g = getGrammar();

		for (Production p : g.getStartProductions()) {
			Derivation d = new Derivation(p);
			myDerivationsQueue.add(d);
			myNodesGenerated++;

		}
		// Allow for at least 8 steps of the parser
		raiseCapacity(8);
	}

	/**
	 * Does the next level of parsing, adding all possible steps to each current
	 * possible derivation, unless actual derivation is found, in which case it
	 * immediately stops.
	 */
	private boolean makeNextReplacement() {
		ArrayList<Derivation> nextLevel = new ArrayList<Derivation>();
		Grammar grammar = getGrammar();
		ProductionSet productions = grammar.getProductionSet();

		loop: while (!myDerivationsQueue.isEmpty()) {
			Derivation d = myDerivationsQueue.poll();
			SymbolString result = d.createResult();

			for (int i = 0; i < result.size(); i++) {
				for (int j = i; j < Math.min(maxLHSsize + i, result.size()); j++) {
					SymbolString LHS = result.subList(i, j + 1);

					Production[] productionsWithLHS = productions
							.getProductionsWithLHS(LHS);

					for (Production p : productionsWithLHS) {
						Derivation tempDerivation = d.copy();
						int replacementIndex = result.indexOf(LHS, i);

						tempDerivation.addStep(p, replacementIndex);

						// increment nodes generated.
						// Even if node=derivation is invalid, it is still
						// generated.
						myNodesGenerated++;

						SymbolString sentential = tempDerivation.createResult();
						if (isPossibleSententialForm(sentential)) {
							mySententialsSeen.add(sentential);
							nextLevel.add(tempDerivation);

							if (sentential.equals(getInput())) {
								// Not sure if this is good, but ensures that
								// only nodes on
								// the current level up to the first matching
								// derivation
								// are returned (for display purposes)
								myDerivationsQueue.clear();
								break loop;
							}
						}
					}
				}
			}
		}
		myDerivationsQueue.addAll(nextLevel);
		return true;
	}

	/**
	 * Increases the capacity of nodes the parser can generate, specified by
	 * numberOfSteps, the minimum number of steps it will be able to be run
	 * after the capacity is set.
	 */
	public boolean raiseCapacity(int numberOfSteps) {
		int levelSize = myDerivationsQueue.size();
		int numProductions = getGrammar().getProductionSet().size();
		int increment = (int) Math.pow(numProductions, numberOfSteps);
		increment = Math.min(increment, MAX_INCREMENT);
		
		myCapacity = myNodesGenerated + levelSize * increment;
		if(myCapacity < 0)
			myCapacity = Integer.MAX_VALUE;
		return true;
	}

	/**
	 * Total number of nodes created.
	 */
	public int getNumberOfNodes() {
		return this.myNodesGenerated;
	}

	/**
	 * Returns false if the derivation has already been encountered, or if the
	 * derivation can only derive strings larger than the input's size.
	 */
	public boolean isPossibleSententialForm(SymbolString sent) {
		if (mySententialsSeen.contains(sent))
			return false;
		int min = getMinimumLength(sent.toArray(new Symbol[0]));
		return min <= getInput().size();
	}

	public int getMinimumLength(Symbol[] sentential) {
		return minimumLength(sentential, mySmallerSet);
	}

	/**
	 * Given a string and a smaller set, this returns the minimum length that
	 * the string can derive as indicated by the smaller set.
	 * 
	 * @param right
	 *            the string to get the "smaller"
	 * @param smaller
	 *            the "smaller" set, as returned by {@link #smallerSymbols}
	 */
	private int minimumLength(Symbol[] right, Set<Symbol> smaller) {
		int length = 0;
		for (Symbol s : right)
			if (!smaller.contains(s))
				length++;
		return length;
	}

	/**
	 * Counts the number of characters in a given string.
	 * 
	 * @param left
	 *            the string
	 * @param c
	 *            the character
	 * @return the number of occurrences of the character in the string
	 */
	private int count(Symbol[] left, Symbol s) {
		int count = 0;
		for (int i = 0; i < left.length; i++)
			if (left[i].equals(s))
				count++;
		return count;
	}

	/**
	 * Returns a set of those symbols in the grammar that can derive some string
	 * smaller than it. For a normal grammar, of course, this would be just
	 * those variables with, but for an unrestricted grammar this can include
	 * the symbol <I>b</I> and <I>c</I> where <I>babca -> aa</I> is a rule.
	 * <I>a</I> is not included because there are <I>a</I> terminals in the
	 * result.
	 * 
	 * @param grammar
	 *            the grammar to find the "small" symbols for
	 */
	private Set<Symbol> smallerSymbols(Grammar grammar) {
		Set<Symbol> smaller = new HashSet<Symbol>();
		Production[] prods = grammar.getProductionSet().toArray();
		boolean added;

		do {
			added = false;
			for (int i = 0; i < prods.length; i++) {
				Symbol[] left = prods[i].getLHS();
				Symbol[] right = prods[i].getRHS();

				int rightLength = minimumLength(right, smaller);
				int leftLength = minimumLength(left, smaller);

				if (leftLength > rightLength) {
					for (int j = 0; j < left.length; j++) {
						Symbol symbol = left[j];

						if (smaller.contains(symbol)
								|| (count(left, symbol) <= count(right, symbol)))
							continue;

						smaller.add(symbol);
						added = true;
					}
				}
			}
		} while (added);
		return smaller;
	}

	/**
	 * Returns the "level" of the parser, how many steps have been taken so far.
	 */
	public int getLevel() {
		if (myDerivationsQueue.isEmpty())
			return 0;
		return myDerivationsQueue.getLast().length();
	}

	/**
	 * Removes useless productions for Unrestricted grammars, as a way to
	 * optimize the grammar as much as possible. //
	 */
	// private static Grammar optimize(Grammar g) {
	// UselessProductionRemover remover = new UselessProductionRemover(g);
	// remover.stepToCompletion();
	// return remover.getTransformedDefinition();
	// }

}
