package model.grammar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.event.ChangeEvent;

import universe.preferences.JFLAPMode;
import universe.preferences.JFLAPPreferences;
import util.UtilFunctions;
import debug.JFLAPDebug;
import errors.BooleanWrapper;
import model.change.events.AdvancedChangeEvent;
import model.formaldef.FormalDefinition;
import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.alphabets.grouping.GroupingPair;
import model.formaldef.rules.AlphabetRule;
import model.formaldef.rules.GroupingRule;
import model.formaldef.rules.applied.DisallowedCharacterRule;
import model.formaldef.rules.applied.TerminalGroupingRule;
import model.formaldef.rules.applied.TermsVersusVarsIdenticalRule;
import model.formaldef.rules.applied.VariableGroupingRule;
import model.formaldef.rules.applied.VarsVersusTermsIdenticalRule;
import model.grammar.typetest.GrammarType;
import model.symbols.Symbol;
import model.symbols.SymbolString;

/**
 * An object representing the formal 4-tuple that represents
 * a grammar. This includes the following {@link FormalDefinitionComponent}s:
 * 		{@link TerminalAlphabet}
 * 		{@link VariableAlphabet}
 * 		{@link ProductionSet}
 * 		{@link StartVariable}
 * 
 * Each of these is accessible from the 
 * 
 * @author Julian Genkins
 *
 */
public class Grammar extends FormalDefinition{

	private StartVariable myStartVariable;
	private GroupingPair myGrouping;

	/**
	 * Creates a {@link Grammar}with all of the necessary components.
	 * @param terminals = the initial {@link TerminalAlphabet}
	 * @param variables = the initial {@link VariableAlphabet}
	 * @param functions = set of {@link Production} rules
	 * @param startVar = the {@link StartVariable} of this grammar
	 */
	public Grammar(VariableAlphabet variables,
					TerminalAlphabet terminals,
					ProductionSet functions,
					StartVariable startVar) {
		super(variables, terminals, functions, startVar);
		myStartVariable = startVar;
		setUpRules();
	}
	
	public Grammar(){
		this(new VariableAlphabet(),
				new TerminalAlphabet(),
				new ProductionSet(),
				new StartVariable());
	}

	private void setUpRules() {
		DisallowedCharacterRule disallowed = new DisallowedCharacterRule(this);
		this.getVariables().addRules(disallowed, new VarsVersusTermsIdenticalRule(getTerminals()));
		this.getTerminals().addRules(disallowed, new TermsVersusVarsIdenticalRule(getVariables()));
	}

	private void addGroupingPairRules(GroupingPair gp) {
		if (gp == null) return;
		
		this.getVariables().addRules(new VariableGroupingRule(gp));
		this.getTerminals().addRules(new TerminalGroupingRule(gp));
		
	}

	private void clearGroupingPairRules() {
		GroupingRule varsRule = this.getVariables().getRuleOfClass(GroupingRule.class);
		GroupingRule termsRule = this.getTerminals().getRuleOfClass(GroupingRule.class);
		if (varsRule != null)
			this.getVariables().removeRule(varsRule);
		if (termsRule != null)
			this.getTerminals().removeRule(termsRule);
	}

	public void setVariableGrouping(GroupingPair gp) {
		clearVariableGrouping();
		addGroupingPairRules(gp);
		super.setMode(JFLAPMode.CUSTOM);
		myGrouping = gp;
	}
	
	public void clearVariableGrouping() {
		clearGroupingPairRules();
		myGrouping = null;
	}
	
	@Override
	public void setMode(JFLAPMode mode) {
		if (!usingGrouping() && mode==JFLAPMode.CUSTOM)
			this.setVariableGrouping(JFLAPPreferences.getDefaultGrouping());
		else {
			this.clearVariableGrouping();
			super.setMode(mode);
		}
	}
	
	/**
	 * Retrieves the open group of the grouping pair in use
	 * or null if grouping is not in use.
	 * 
	 * @return
	 */
	public Character getOpenGroup(){
		if (myGrouping != null)
			return myGrouping.getOpenGroup();
		return null;
	}
	
	/**
	 * Retrieves the close group of the grouping pair in use
	 * or null if grouping is not in use.
	 * @return
	 */
	public Character getCloseGroup(){
		if (this.usingGrouping())
			return myGrouping.getCloseGroup();
		return null;
	}
	

	
	/**
	 * Retrieves the {@link VariableAlphabet} of this grammar
	 * @return
	 */
	public VariableAlphabet getVariables() {
		return getComponentOfClass(VariableAlphabet.class);
	}

	@Override
	public String getDescriptionName() {
		return "Grammar";
	}

	@Override
	public String getDescription() {
		
		return "Hey look, its a Grammar!";
	}

	/**
	 * Returns the {@link TerminalAlphabet} of this grammar. The
	 * {@link Alphabet} returned is identical to that of the 
	 * {@link Alphabet.getLanguageAlphabet()} method.
	 * @return
	 */
	public TerminalAlphabet getTerminals() {
		return getComponentOfClass(TerminalAlphabet.class);
	}
	
	/**
	 * Returns the start variable for this grammar
	 * @return
	 */
	public Variable getStartVariable(){
		return getComponentOfClass(StartVariable.class).getSymbol();
	}
	
	/**
	 * Sets the StartVariable to the {@link Variable} v;
	 * @param s
	 */
	public void setStartVariable(Variable s){
		myStartVariable.setSymbol(s);
	}

	@Override
	public Grammar alphabetAloneCopy() {
		return new Grammar(this.getVariables().copy(),
							this.getTerminals().copy(), 
							new ProductionSet(),
							new StartVariable());
	}
	
	/**
	 * Returns the set of products in the from of a {@link ProductionSet} object.
	 * Calling this methods is equivalent to calling <code>getFunctionSet()</code>.
	 * 
	 * @return
	 */
	public ProductionSet getProductionSet(){
		return getComponentOfClass(ProductionSet.class);
	}

	/**
	 * Returns true if and only if the variable alphabet has a grouping rule,
	 * signifying that it is using a grouping pair!
	 * 
	 * @return
	 */
	@Override
	public boolean usingGrouping() {
		return (myGrouping != null);
	}

	public boolean isType(GrammarType type) {
		GrammarType[] myTypes = GrammarType.getType(this);
		for (GrammarType gt : myTypes){
			if (gt == type)
				return true;
		}
		return false;
	}

	public Production[] getStartProductions() {
		ProductionSet startProds = new ProductionSet();
		for (Production p : this.getProductionSet()){
			if (p.isStartProduction(this.getStartVariable()))
					startProds.add(p);
		}
		return startProds.toArray(new Production[0]);
	}

	public static boolean isVariable(Symbol first) {
		return first instanceof Variable;
	}

	public static boolean isTerminal(Symbol symbol) {
		return symbol instanceof Terminal;
	}

	@Override
	public Grammar copy() {
		VariableAlphabet vars = getVariables().copy();
		TerminalAlphabet terms = getTerminals().copy();
		ProductionSet p = getProductionSet().copy();
		
		Variable start = getStartVariable();
		StartVariable var = start == null ? new StartVariable() : new StartVariable((Variable) start.copy());
		
		Grammar g = new Grammar(vars, terms, p, var);
		if(usingGrouping())
			g.setVariableGrouping(myGrouping);
		return g;
	}

	public static boolean isStartVariable(Variable a, Grammar gram) {
		return gram.getStartVariable().equals(a);
	}
	
	public static boolean isStartProduction(Production p, Grammar g){
		return p.getLHS().length == 1
				&& p.containsSymbolOnLHS(g.getStartVariable());
	}

	@Override
	public TerminalAlphabet getLanguageAlphabet() {
		return getTerminals();
	}

	@Override
	public void componentChanged(AdvancedChangeEvent event) {
		if (event.comesFrom(VariableAlphabet.class) && 
				this.getStartVariable()==null &&
				event.getType() == ITEM_ADDED)
			this.setStartVariable(((ArrayList<Variable>) event.getArg(0)).get(0));
		super.componentChanged(event);
	}

	@Override
	public Symbol createSymbol(String sym) {
		JFLAPMode mode = getMode();
		switch(mode){
		case CUSTOM:
			if (VariableGroupingRule.checkExternalGrouping(sym, myGrouping) &&
					!TerminalGroupingRule.containsGrouping(sym.substring(1,sym.length()-1), myGrouping))
				return new Variable(sym);
//			else if (!TerminalGroupingRule.containsGrouping(sym, myGrouping))
				return new Terminal(sym);
//			else return null;
		case DEFAULT:
			if (sym.length()>1) return null;
		case MULTI_CHAR_DEFAULT:
			if (UtilFunctions.isAllUpperCase(sym)) return new Variable(sym);
			if (UtilFunctions.isAllNonUpperCase(sym)) return new Terminal(sym);
		}
		return null;
	}	

}
