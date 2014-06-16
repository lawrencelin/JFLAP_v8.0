package model.grammar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import util.JFLAPConstants;



import model.change.events.SetToEvent;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.alphabets.AlphabetException;
import model.formaldef.components.functionset.function.LanguageFunction;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public class Production extends LanguageFunction<Production> implements JFLAPConstants{

	/** the left hand side of the production. */
	private SymbolString myLHS;

	/** the right hand side of the production. */
	private SymbolString myRHS;

	/**
	 * Creates an instance of <CODE>Production</CODE>.
	 * 
	 * @param lhs
	 *            the left hand side of the production rule.
	 * @param rhs
	 *            the right hand side of the production rule.
	 */
	public Production(SymbolString lhs, SymbolString rhs) {
		checkBadSymbols(lhs);
		checkBadSymbols(rhs);
		myLHS = lhs;
		myRHS = rhs;
	}

	public Production(){
		this(new SymbolString(), new SymbolString());
	}
	
	public Production(Symbol lhs, Symbol ... rhs) {
		this(new SymbolString(lhs), new SymbolString(rhs));
	}

	public Production(Symbol lhs, SymbolString rhs) {
		this(new SymbolString(lhs), rhs);

	}

	public Production(Symbol[] lhs, Symbol ... rhs) {
		this(new SymbolString(lhs), new SymbolString(rhs));
	}

	/**
	 * Sets the right hand side of production to <CODE>rhs</CODE>.
	 * 
	 * @param rhs
	 *            the right hand side
	 */
	public boolean setRHS(SymbolString rhs) {
		return setTo(myLHS, rhs);
	}

	/**
	 * Sets the left hand side of production to <CODE>lhs</CODE>.
	 * 
	 * @param lhs
	 *            the left hand side
	 */
	public boolean setLHS(SymbolString lhs) {
		return setTo(lhs, myRHS);
	}

	private void checkBadSymbols(SymbolString lhs) {
		if (containsBadSymbol(lhs))
			throw new ProductionException("The SymbolString set as the LHS or RHS " +
					"in a production cannot contain non-terminal/non-variable " +
					"symbols.");
	}

	private boolean containsBadSymbol(SymbolString side) {
		for (Symbol s: side){
			if (!(Grammar.isTerminal(s) ||
					Grammar.isVariable(s)))
				return true;
		}
		return false;
	}

	/**
	 * Returns a string representation of the left hand side of the production
	 * rule.
	 * 
	 * @return a string representation of the lhs.
	 */
	public Symbol[] getLHS() {
		return myLHS.toArray(new Symbol[0]);
	}

	/**
	 * Returns a string representation of the right hand side of the production
	 * rule.
	 * 
	 * @return a string representation of the rhs.
	 */
	public Symbol[] getRHS() {
		return myRHS.toArray(new Symbol[0]);
	}

	/**
	 * Returns all variables in the production.
	 * 
	 * @return all variables in the production.
	 */
	public Set<Variable> getVariablesUsed() {
		TreeSet<Variable> results = new TreeSet<Variable>(this.getVariablesOnLHS());
		results.addAll(this.getVariablesOnRHS());
		return results;
	}

	/**
	 * Returns all variables on the left hand side of the production.
	 * 
	 * @return all variables on the left hand side of the production.
	 */
	public Set<Variable> getVariablesOnLHS() {
		return myLHS.getSymbolsOfClass(Variable.class);
	}

	/**
	 * Returns all variables on the right hand side of the production.
	 * 
	 * @return all variables on the right hand side of the production.
	 */
	public Set<Variable> getVariablesOnRHS() {
		return myRHS.getSymbolsOfClass(Variable.class);
	}

	/**
	 * Returns all terminals in the production.
	 * 
	 * @return all terminals in the production.
	 */
	public Set<Terminal> getTerminalsUsed() {
		TreeSet<Terminal> results = new TreeSet<Terminal>(this.getTerminalsOnLHS());
		results.addAll(this.getTerminalsOnRHS());
		return results;
	}

	/**
	 * Returns all terminals on the right hand side of the production.
	 * 
	 * @return all terminals on the right hand side of the production.
	 */
	public Set<Terminal> getTerminalsOnRHS() {
		return myRHS.getSymbolsOfClass(Terminal.class);
	}

	/**
	 * Returns true if <CODE>production</CODE> is equivalent to this
	 * production (i.e. they have identical left and right hand sides).
	 * 
	 * @param production
	 *            the production being compared to this production
	 * @return true if <CODE>production</CODE> is equivalent to this
	 *         production (i.e. they have identical left and right hand sides).
	 */
	public boolean equals(Object production) {
		return this.compareTo((Production) production) == 0;
	}

	/**
	 * Returns a hashcode for this production.
	 * 
	 * @return the hashcode for this production
	 */
	public int hashCode() {
		return myRHS.hashCode() * myLHS.hashCode();
	}

	/**
	 * Returns all terminals on the left hand side of the production.
	 * 
	 * @return all terminals on the left hand side of the production.
	 */
	public Set<Terminal> getTerminalsOnLHS() {
		return myLHS.getSymbolsOfClass(Terminal.class);
	}

	/**
	 * Returns a string representation of the production object.
	 * 
	 * @return a string representation of the production object.
	 */
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(myLHS);
		 buffer.append("->");
//		buffer.append('\u2192');
		buffer.append(myRHS);
		// buffer.append('\n');
		return buffer.toString();
	}
	

	public boolean isStartProduction(Variable variable) {
		if (myLHS.isEmpty()) return false;
		return myLHS.getFirst().equals(variable);
//				&& this.getLHS().size() == 1;
	}
	
	public boolean containsSymbol(Symbol symbol) {
		return myLHS.contains(symbol) || myRHS.contains(symbol);
	}

	@Override
	public int compareTo(Production o) {
		
		int i = 0;
		if((i = myLHS.compareTo(o.myLHS)) == 0)
			i = this.myRHS.compareTo(o.myRHS);
		
		return i;
	}

	public boolean isEmpty() {
		return myLHS.isEmpty() && myRHS.isEmpty();
	}
	
	@Override
	public boolean purgeOfSymbols(Alphabet a, Collection<Symbol> s) {
		boolean lhs = myLHS.removeAll(s); 
		return this.myRHS.removeAll(s) || lhs;
	}

	
	public Object[] toArray() {
		return new Object[]{
				this.myLHS.toString(),
				PRODUCTION_ARROW,
				this.myRHS.toString()};
	}

	@Override
	public Set<Symbol> getSymbolsUsedForAlphabet(Alphabet a) {
		Set<Symbol> set = new TreeSet<Symbol>();
		if (a instanceof VariableAlphabet){
			set.addAll(getVariablesUsed());
		}
		else if (a instanceof TerminalAlphabet){
			set.addAll(getTerminalsUsed());
		}
		return set;
	}

	@Override
	public String getDescriptionName() {
		return "Production Rule";
	}

	@Override
	public String getDescription() {
		return "A production rule for a grammar.";
	}

	@Override
	public Production copy() {
		return new Production(myLHS.copy(), myRHS.copy());
	}
	
	public boolean isLambdaProduction() {
		return this.myRHS.isEmpty();
	}

	private boolean setTo(SymbolString lhs, SymbolString rhs) {
		return this.setTo(new Production(lhs, rhs));
	}

	public boolean containsSymbolOnLHS(Symbol s) {
		return myLHS.contains(s);
	}

	public boolean containsSymbolOnRHS(Symbol s) {
		return myRHS.contains(s);
	}

	@Override
	public List<Symbol> getAllSymbols() {
		List<Symbol> symbols = new ArrayList<Symbol>(myLHS);
		symbols.addAll(myRHS);
		return symbols;
	}

	@Override
	protected void applySetTo(Production other) {
		this.myLHS.setTo(other.myLHS);
		this.myRHS.setTo(other.myRHS);
	}

	public boolean equalsLHS(SymbolString lhs) {
		return myLHS.equals(lhs);
	}

	public boolean equalsRHS(SymbolString rhs) {
		return myRHS.equals(rhs);
	}

}
