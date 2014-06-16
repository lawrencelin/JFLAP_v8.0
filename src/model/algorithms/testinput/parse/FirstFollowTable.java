package model.algorithms.testinput.parse;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import universe.preferences.JFLAPPreferences;

import debug.JFLAPDebug;


import errors.BooleanWrapper;

import model.algorithms.AlgorithmException;
import model.grammar.Grammar;
import model.grammar.GrammarUtil;
import model.grammar.Production;
import model.grammar.Terminal;
import model.grammar.Variable;
import model.grammar.VariableAlphabet;
import model.grammar.typetest.matchers.ContextFreeChecker;
import model.grammar.typetest.matchers.GrammarChecker;
import model.grammar.typetest.matchers.UnrestrictedChecker;
import model.regex.EmptySub;
import model.symbols.Symbol;
import model.symbols.SymbolString;

/**
 * An object representing the First/Follow table for
 * a grammar. This class provides both the non-static
 * option to create the parse table or the static option
 * to retrieve the first set of a given variable in a
 * given grammar. One can also choose to instantiate with
 * or without auto-completion of the table.
 * 
 * @author Julian Genkins
 *
 */
public class FirstFollowTable {

	private FirstFollowMapping[] myTable;
	private Grammar myGrammar;

	/**
	 * Creates a new {@link FirstFollowTable} and completes
	 * it automatically, i.e. calls the other constructor
	 * with a populate boolean = true
	 * @param g
	 */
	public FirstFollowTable(Grammar g){
		this(g, true);
	}

	/**
	 * Creates a new {@link FirstFollowTable}. This is guaranteed
	 * to set up the variable entries, but will only automatically
	 * calculate the FIRST and FOLLOW sets if the populate boolean
	 * is true.
	 * 
	 * @param g
	 * 		The {@link Grammar} that will be used for this table
	 * @param populate
	 * 		true if the table should be automatically populated
	 */
	public FirstFollowTable(Grammar g, boolean populate) {
		GrammarChecker check = new ContextFreeChecker();
		if (!check.matchesGrammar(g))
			throw new ParserException("This grammar is not context free, " +
					" therefore, you may not build a FIRST/FOLLOW table with it.");
		myGrammar = g;
		Variable[] alph = g.getVariables().toArray(new Variable[0]);
		myTable = new FirstFollowMapping[alph.length];
		for (int i = 0; i<alph.length; i++){
			myTable[i] = new FirstFollowMapping(alph[i]);
		}
		if (populate){
			completeTable();
		}
	}

	/**
	 * Completes the rest of the {@link FirstFollowTable}. This
	 * method is called in the constructor automatically
	 * when the populate boolean is true.
	 */
	public void completeTable() {
		for (int i = 0; i < this.size(); i++){
			populateForIndex(i);
		}
	}

	/**
	 * Automatically constructs the first and follow sets for
	 * the Variable v. Will return true if something changed, 
	 * otherwise will return false.
	 * 
	 * @param v
	 * 		The variable target
	 */
	public void populateForVariable(Variable v){
		int i = getIndexForVar(v);
		populateForIndex(i);
	}

	/**
	 * Completes the FIRST set for the Variable v. Returns
	 * that set if there were changes made, otherwise returns
	 * null if the set was already complete.
	 * 
	 * @param v
	 * 		Variable target.
	 * @return
	 * 		The FIRST set of v 
	 */
	public Set<Terminal> populateFirstForVar(Variable v){
		int i = getIndexForVar(v);
		return populateFirstForIndex(i);
	}

	/**
	 * Completes the FOLLOW set for the Variable v. Returns
	 * that set if there were changes made, otherwise returns
	 * null if the set was already complete.
	 * 
	 * @param v
	 * 		Variable target.
	 * @return
	 * 		The FOLLOW set of v or null if the set was complete
	 */
	public Set<Terminal> populateFollowForVar(Variable v){
		int i = getIndexForVar(v);
		return populateFollowForIndex(i);
	}

	private int getIndexForVar(Variable v){
		for (int i = 0; i<this.size(); i++){
			FirstFollowMapping map = myTable[i];
			if (map.var.equals(v))
				return i;
		}

		throw new ParserException("Error with first/follow mapping. This" +
				" should not happen.");
	}

	/**
	 * Populate the FIRST and FOLLOW sets for the variable
	 * at index i. Will return true if something changed, 
	 * otherwise returns false.
	 * 
	 * @param i
	 * 		target index
	 * @return
	 * 		true if changed.
	 */
	public void populateForIndex(int i) {
		Set<Terminal> first = populateFirstForIndex(i);
		Set<Terminal> follow = populateFollowForIndex(i);
	}


	/**
	 * Completes the FIRST set for the variable at index i.
	 * This method will return the FIRST set if the table
	 * changed, otherwise it will return null signifying
	 * that the FIRST set for the variable at i is already
	 * complete.
	 * 
	 * @param i
	 * 		The target index
	 * @return
	 * 		the FIRST set at i
	 */
	public Set<Terminal> populateFirstForIndex(int i) {
		Variable v = myTable[i].var;
		Set<Terminal> newFirst = findFirstSet(v, myGrammar);
		Set<Terminal> oldFirst = myTable[i].first;
		boolean changed = !oldFirst.equals(newFirst);
		if (changed){
			oldFirst.clear();
			oldFirst.addAll(newFirst);
		}
		return oldFirst;
	}

	/**
	 * Adds all of the terminals to the FIRST set of this
	 * variable. If a terminal is not in the first set of v,
	 * it will not be added to the first set.
	 * 
	 * @param v
	 * 		the variable target
	 * @param terms
	 * 		the terminals to add to the FIRST set
	 * @return
	 * 		the resulting FIRST set currently in the table
	 */
	public Set<Terminal> addFirstSymbols(Variable v, Terminal ... terms){
		return addFirstSymbols(getIndexForVar(v), terms);
	}

	/**
	 * Adds all of the terminals to the FIRST set of the 
	 * variable v at this index. If a terminal is not in the 
	 * first set of v it will not be added to the first set.
	 * 
	 * @param i
	 * 		the index of the variable target
	 * @param terms
	 * 		the terminals to add to the FIRST set
	 * @return
	 * 		the resulting FIRST set currently in the table at i
	 */
	public Set<Terminal> addFirstSymbols(int i, Terminal ... terms) {
		Variable v = myTable[i].var;
		Set<Terminal> idealFirst = findFirstSet(v, myGrammar);
		Set<Terminal> curFirst = myTable[i].first;

		for (Terminal t: terms){
			if (idealFirst.contains(t))
				curFirst.add(t);
		}

		return curFirst;
	}

	/**
	 * Completes the FOLLOW set for the variable at index i.
	 * This method will return the FOLLOW set if the table
	 * changed, otherwise it will return null signifying
	 * that the FOLLOW set for the variable at i is already
	 * complete.
	 * 
	 * @param i
	 * 		The target index
	 * @return
	 * 		the FOLLOW set at i
	 */
	public Set<Terminal> populateFollowForIndex(int i) {
		Variable v = myTable[i].var;
		Set<Terminal> newFollow = findFollowSet(v, myGrammar);
		Set<Terminal> oldFollow = myTable[i].follow;
		boolean changed = !oldFollow.equals(newFollow);
		if (changed){
			oldFollow.clear();
			oldFollow.addAll(newFollow);
		}
		return oldFollow;
	}

	/**
	 * Adds all of the terminals to the FOLLOW set of this
	 * variable. If a terminal is not in the first set of v,
	 * it will not be added to the first set.
	 * 
	 * @param v
	 * 		the variable target
	 * @param terms
	 * 		the terminals to add to the FOLLOW set
	 * @return
	 * 		the resulting FOLLOW set currently in the table
	 */
	public Set<Terminal> addFollowSymbols(Variable v, Terminal ... terms){
		return addFirstSymbols(getIndexForVar(v), terms);
	}

	/**
	 * Adds all of the terminals to the FOLLOW set of the 
	 * variable v at this index. If a terminal is not in the 
	 * first set of v it will not be added to the first set.
	 * 
	 * @param i
	 * 		the index of the variable target
	 * @param terms
	 * 		the terminals to add to the FOLLOW set
	 * @return
	 * 		the resulting FOLLOW set currently in the table at i
	 */
	public Set<Terminal> addFollowSymbols(int i, Terminal ... terms) {
		Variable v = myTable[i].var;
		Set<Terminal> idealFollow = findFollowSet(v, myGrammar);
		Set<Terminal> curFollow = myTable[i].follow;

		for (Terminal t: terms){
			if (idealFollow.contains(t))
				curFollow.add(t);
		}

		return curFollow;
	}

	/**
	 * Retrieves the first set for the input {@link SymbolString} based
	 * on this first/follow table. NOTE: this should only be
	 * called once the table is complete and will throw an
	 * error otherwise.
	 * 
	 * @param rhs
	 * @return
	 */
	public Set<Terminal> retrieveFirstSet(Symbol[] rhs) {
		if (!this.isComplete())
			throw new ParserException("The FIRST/FOLLOW table must be " +
					"complete before you may use it to retrieve info.");
		Set<Terminal> first = new TreeSet<Terminal>();
		Terminal empty = JFLAPPreferences.getSubForEmptyString();
		first.add(empty);
		for (int i = 0; i< rhs.length; i++){
			first.remove(empty);
			Symbol s = rhs[i];
			if (Grammar.isTerminal(s)){
				first.add((Terminal) s);
				break;
			}
			first.addAll(this.getFirst((Variable) s));
			if (!first.contains(empty))
				break;
		}
		return first;
	}

	public boolean isFirstComplete(Variable v){
		int i = getIndexForVar(v);
		return isFirstComplete(i);
	}
	
	public boolean isFirstComplete(int i){
		Variable v = myTable[i].var;
		Set<Terminal> idealFirst = findFirstSet(v, myGrammar);
		Set<Terminal> curFirst = myTable[i].first;
		return idealFirst.equals(curFirst);
	}
	
	public boolean isFollowComplete(Variable v){
		int i = getIndexForVar(v);
		return isFollowComplete(i);
	}
	
	public boolean isFollowComplete(int i){
		Variable v = myTable[i].var;
		Set<Terminal> idealFollow = findFollowSet(v, myGrammar);
		Set<Terminal> curFollow = myTable[i].follow;
		return idealFollow.equals(curFollow);
	}
	
	public Variable getFirstIncompleteFollow(){
		for (FirstFollowMapping map: myTable){
			if (!isFollowComplete(map.var))
				return map.var;
		}
		return null;
	}
	
	public Variable getFirstIncompleteFirst(){
		for (FirstFollowMapping map: myTable){
			if (!isFirstComplete(map.var))
				return map.var;
		}
		return null;
	}
	
	public boolean allFirstComplete(){
		return getFirstIncompleteFirst() == null;
	}
	
	public boolean allFollowComplete(){
		return getFirstIncompleteFollow() == null;
	}
	
	public boolean isComplete() {
		return allFirstComplete() && allFollowComplete();
	}

	public int size() {
		return myTable.length;
	}

	public Grammar getAssociatedGrammar() {
		return myGrammar;
	}

	/**
	 * Retrieve the FIRST set for the {@link Variable} v
	 * in the {@link Grammar} g. 
	 * 
	 * @param v
	 * @param g
	 * @return
	 */
	public static Set<Terminal> findFirstSet(Variable v, Grammar g){
		if (!g.getVariables().contains(v))
			throw new AlgorithmException("The variable " + v + "is not in the " +
					g.getDescriptionName());
		return recursiveFirst(v, g, new TreeSet<Variable>());
	}
	
	private static Set<Terminal> recursiveFirst(Symbol[] symbols, Grammar g, Set<Variable> history){
		//Otherwise, check variables on RHS
		Set<Terminal> first = new TreeSet<Terminal>();
		Terminal empty = JFLAPPreferences.getSubForEmptyString();
		
//		by default, add the empty string. if ss is not empty, then it will
//		be removed below anyway
		first.add(empty);
		
		for (int i = 0; i < symbols.length; i++){
			first.remove(empty);
			Symbol sym = symbols[i];
			first.addAll(recursiveFirst(sym, g, history));
//			if the symbol does not derives lambda, then break out of the loop.
			if (!GrammarUtil.derivesLambda(sym, g))
				break;
			
//			else update history and recurse...you know sym must be a var
			history = new TreeSet<Variable>(history);
			history.add((Variable) sym); //i+1
			first.addAll(
					recursiveFirst(Arrays.copyOfRange(symbols, i+1, symbols.length),
					g, 
					history));
		}
		return first;
	}

	private static Set<Terminal> recursiveFirst(Symbol s, Grammar g, Set<Variable> history){
		//if s is a terminal, then FIRST(s) = s
		if (Grammar.isTerminal(s)){
			TreeSet<Terminal> first = new TreeSet<Terminal>();
			first.add((Terminal) s);
			return first;
		}
		Variable v = (Variable) s;
		
		//if we ave already recursed through this variable, it is already include
		if (history.contains(v))
			return new TreeSet<Terminal>();

		//update history
		history = new TreeSet<Variable>(history);
		history.add(v);
		Set<Production> prods = g.getProductionSet().getProductionsWithSymbolOnLHS(v);

		Set<Terminal> first = new TreeSet<Terminal>();
		Terminal empty = JFLAPPreferences.getSubForEmptyString();
		for (Production p: prods){
			//FIRST(lambda) = lambda (in this case the EmptySub symbol
			first.addAll(recursiveFirst(p.getRHS(), g, history));
		}

		return first;
	}

	public static Set<Terminal> findFollowSet(Variable v, Grammar g){
		return recursiveFollow(v, g, new TreeSet<Variable>());
	}

	private static Set<Terminal> recursiveFollow(Variable v, Grammar g, Set<Variable> history){
		
		//if we ave already recursed through this variable, it is already include
		if (history.contains(v))
			return new TreeSet<Terminal>();

		//update history
		history = new TreeSet<Variable>(history);
		history.add(v);

		Set<Terminal> follow = new TreeSet<Terminal>();
		Terminal eosMarker = JFLAPPreferences.getEndOfStringMarker();
		
		//if v is te start variable, add the EOS marker
		if (g.getStartVariable().equals(v))
			follow.add(eosMarker);
		
		Set<Production> prods = g.getProductionSet().getProductionsWithSymbolOnRHS(v);
		Terminal empty = JFLAPPreferences.getSubForEmptyString();
		
		for (Production p: prods){
			Set<Terminal> toAdd = new TreeSet<Terminal>();
			Symbol[] rhs = p.getRHS();
			
			//find all instances of V on the RHS of p
			for (int i = 0; i < rhs.length; i++){
				Symbol s = rhs[i];
				if (!s.equals(v)) continue;
				
				//Assume of the form A -> vBw
				
				Symbol[] w = Arrays.copyOfRange(rhs, i+1, rhs.length);
				
				Set<Terminal> firstW = recursiveFirst(w, g, new TreeSet<Variable>());
				toAdd.addAll(firstW);
				
				//if FIRST(w) contains lambda, then FOLLOW(A) is in FOLLOW(B)
				if (firstW.contains(empty)){
					toAdd.remove(empty);
					Variable A = (Variable) p.getLHS()[0];
					toAdd.addAll(recursiveFollow(A,g,history));
					//also at this point we have exhausted the production
					break;
				}
			}
			follow.addAll(toAdd);
		}
		return follow;
	}

	public Set<Terminal> getFirst(Variable v) {
		return new TreeSet<Terminal>(myTable[getIndexForVar(v)].first);
	}

	public Set<Terminal> getFollow(Variable v) {
		return new TreeSet<Terminal>(myTable[getIndexForVar(v)].follow);
	}
	
	@Override
	public String toString() {
		String toString = "";
		for (int i = 0; i < this.size(); i++){
			toString += myTable[i].toString() +"\n";
		}
		return toString;
	}

	private class FirstFollowMapping{
		public Variable var;
		public Set<Terminal> first;
		public Set<Terminal> follow;
	
		public FirstFollowMapping(Variable v) {
			var = v;
			first = new TreeSet<Terminal>();
			follow = new TreeSet<Terminal>();
		}
		
		@Override
		public String toString() {
			return var + ": FIRST = " + first + " | FOLLOW = " + follow;
		}
	}

}
