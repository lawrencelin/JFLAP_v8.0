package model.algorithms.testinput.parse;

import java.lang.Character.Subset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import util.Copyable;
import util.UtilFunctions;

import debug.JFLAPDebug;

import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.Variable;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public class Derivation implements Copyable {

	private LinkedList<Production> myProductions;
	private LinkedList<Integer> mySubstitutions;
	private SymbolString myInitial;

	public Derivation(Production start) {
		this(new SymbolString(start.getLHS()));
		addStep(start, 0);

	}

	/**
	 * Creates a derivation which starts with the {@link SymbolString} startLHS
	 * 
	 * @param startLHS
	 */
	public Derivation(Variable startVar) {
		this(new SymbolString(startVar));
	}

	private Derivation(SymbolString initial) {
		myInitial = initial;
		myProductions = new LinkedList<Production>();
		mySubstitutions = new LinkedList<Integer>();
	}

	public void addAll(Production[] productions, Integer[] subs) {
		if (productions.length != subs.length)
			throw new ParserException("The number of productions and "
					+ "substituations in the derivation must be equal.");
		for (int i = 0; i < productions.length; i++) {
			addStep(productions[i], subs[i]);
		}
	}

	public boolean addLeftmostStep(Production p) {
		SymbolString current = createResult();
		for (int i = 0; i < current.size(); i++) {
			if (Grammar.isVariable(current.get(i))) {
				return this.addStep(p, i);
			}

		}
		return false;
	}

	public boolean addRightmostStep(Production p) {
		SymbolString current = createResult();
		for (int i = current.size() - 1; i >= 0; i--) {
			if (Grammar.isVariable(current.get(i))) {
				return this.addStep(p, i);
			}

		}
		return false;
	}

	public boolean addStep(Production p, int subIndex) {
		return myProductions.add(p) && mySubstitutions.add(subIndex);
	}

	public SymbolString createResult() {
		return createResult(this.length());
	}

	/**
	 * Returns the number of productions in this derivation.
	 * 
	 * @return
	 */
	public int length() {
		return myProductions.size();
	}

	public SymbolString createResult(int n) {
		SymbolString result = new SymbolString();
		if (length() < n) {
			throw new ParserException("This derivation does not have " + n
					+ " steps.");
		}

		if (n == 0)
			return myInitial;

		result.addAll(myInitial);
		for (int i = 0; i < n; i++) {
			Symbol[] sub = myProductions.get(i).getRHS();
			int start = mySubstitutions.get(i);
			int end = start + myProductions.get(i).getLHS().length;
			result = result.replace(start, end, sub);
		}
		return result;
	}

	public SymbolString[] getResultArray() {
		SymbolString[] steps = new SymbolString[this.length()];
		for (int i = 1; i < steps.length + 1; i++) {
			steps[i - 1] = createResult(i);
		}
		return steps;
	}

	@Override
	public String toString() {
		return myProductions.toString()+"\n"+mySubstitutions.toString();
	}

	public Production getProduction(int i) {
		return myProductions.get(i);
	}
	
	public Production[] getProductionArray() {
		return myProductions.toArray(new Production[0]);
	}

	public int getSubstitution(int i) {
		return mySubstitutions.get(i);
	}
	
	public Integer[] getSubstitutionArray() {
		return mySubstitutions.toArray(new Integer[0]);
	}
	
	@Override
	public Derivation copy() {
		Derivation copy = new Derivation(myProductions.get(0));
		for (int i = 1; i < this.length(); i++) {
			copy.addStep(myProductions.get(i), mySubstitutions.get(i));
		}
		return copy;
	}

	public Derivation[] toStepArray() {
		Derivation[] steps = new Derivation[this.length() + 1];
		for (int i = 0; i < steps.length; i++) {
			steps[i] = getSubDerivation(i);
		}
		return steps;
	}

	public Derivation getSubDerivation(int n) {
		Derivation d = new Derivation(myInitial);
		for (int i = 0; i < n; i++)
			d.addStep(getProduction(i), mySubstitutions.get(i));
		return d;
	}

	@Override
	public boolean equals(Object arg0) {
		if (!(arg0 instanceof Derivation))
			return false;
		Derivation other = (Derivation) arg0;
		if (myInitial.equals(other.myInitial)) {
			if (length() == other.length()) {
				for (int i = 0; i < length(); i++) {
					if (!(myProductions.get(i).equals(
							other.myProductions.get(i)) && mySubstitutions.get(
							i).equals(other.mySubstitutions.get(i))))
						return false;
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * Helper method to create a Rightmost Derivation from the trace.
	 * 
	 * @return the corresponding derivation or null if there is no derivation.
	 */
	public static Derivation createRightmostDerivation(List<Production> trace,
			boolean flip) {
		if (trace.isEmpty())
			return null;
		if (flip) {
			trace = new ArrayList<Production>(trace);
			Collections.reverse(trace);
		}
		Derivation d = new Derivation(trace.get(0));
		for (int i = 1; i < trace.size(); i++) {
			d.addRightmostStep(trace.get(i));
		}
		return d;
	}

	public static Derivation createLeftmostDerivation(List<Production> trace) {
		if (trace.isEmpty())
			return null;
		Derivation d = new Derivation(trace.get(0));
		for (int i = 1; i < trace.size(); i++) {
			d.addLeftmostStep(trace.get(i));
		}
		return d;
	}
}
