package model.lsystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import view.lsystem.helperclasses.Axiom;
import view.lsystem.helperclasses.ParameterMap;

import model.formaldef.FormalDefinition;
import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.alphabets.Alphabet;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.ProductionSet;
import model.grammar.TerminalAlphabet;
import model.grammar.Variable;
import model.grammar.VariableAlphabet;
import model.lsystem.formaldef.FormalAlph;
import model.lsystem.formaldef.FormalAxiom;
import model.lsystem.formaldef.FormalRewritingRules;
import model.symbols.Symbol;
import model.symbols.SymbolString;

/**
 * The <CODE>LSystem</CODE> class represents L-systems. This does not do any
 * simulation of L-systems, but rather has the minimal mathematical definitions
 * required, i.e., the axiom, replacement rules, with some concession given to
 * define parameters for drawing.
 * 
 * @author Ian McMahon, Thomas Finley
 * 
 */
public class LSystem extends FormalDefinition {
	private Axiom myAxiom;
	private Grammar myGrammar;
	private ParameterMap myParameters;
	private Map<SymbolString, List<SymbolString>> myReplacements;

	public LSystem() {
		this(new Axiom(), new Grammar(), new ParameterMap());
	}

	public LSystem(Axiom axiom, Grammar g, ParameterMap parameters) {
		this(new FormalAlph(g.getVariables(), g.getTerminals()),
				new FormalAxiom(axiom), new FormalRewritingRules(
						g.getProductionSet()));
		myAxiom = axiom;
		setParameters(parameters);
		setGrammar(g);
	}

	public LSystem(FormalAlph alph, FormalAxiom ax, FormalRewritingRules rules) {
		super(ax, alph, rules);
	}

	@Override
	public String getDescription() {
		return "Lindenmayer System";
	}

	@Override
	public String getDescriptionName() {
		return "L-System";
	}

	@Override
	public LSystem copy() {
		return new LSystem(myAxiom, myGrammar, myParameters);
	}

	@Override
	public Alphabet getLanguageAlphabet() {
		return myGrammar.getLanguageAlphabet();
	}

	@Override
	public FormalDefinition alphabetAloneCopy() {
		return new LSystem(new Axiom(), myGrammar.alphabetAloneCopy(),
				new ParameterMap());
	}

	/**
	 * Sets the Parameters of the LSystem to the passed in ParameterMap
	 */
	public void setParameters(ParameterMap parameters) {
		myParameters = parameters;
	}

	/**
	 * Returns the map of parameters for the L-System.
	 */
	public ParameterMap getParameters() {
		return myParameters;
	}

	/**
	 * Sets the Grammar of the LSystem to the passed in Grammar, adding the
	 * Axiom and all LSystem Commands to the Terminal Alphabet of the Grammar.
	 */
	public void setGrammar(Grammar g) {
		myGrammar = g;
		// Dummy Start Variable, since simulation is done by the Expander but
		// Start Variables are required.
		myGrammar.setStartVariable(new Variable("`"));

		TerminalAlphabet term = myGrammar.getTerminals();
		CommandAlphabet command = new CommandAlphabet();

		if (!term.containsAll(command))
			term.addAll(command);
		addAxiomToAlphabet();
		initReplacements();
	}

	/**
	 * Returns an array of all SymbolStrings that <i>s</i> can be replaced by in
	 * a single Production. In other words, returns all SymbolStrings that occur
	 * on any Production's RHS that has <i>s</i> as the LHS.
	 */
	public SymbolString[] getReplacements(SymbolString s) {
		List<SymbolString> sList = myReplacements.get(s);
		SymbolString[] emptyArray = new SymbolString[0];
		return sList == null ? emptyArray : sList.toArray(emptyArray);
	}

	/**
	 * 
	 * @return Set of all SymbolStrings that can be replaced, ie. appear on the
	 *         LHS of a Production in the LSystem.
	 */
	public Set<SymbolString> getSymbolStringsWithReplacements() {
		return myReplacements.keySet();
	}

	/**
	 * @return True iff there exists at least one SymbolString that has more
	 *         than 1 replacement.
	 */
	public boolean isNondeterministic() {
		for (List<SymbolString> p : myReplacements.values()) {
			if (p.size() > 1)
				return true;
		}
		return false;
	}

	/**
	 * @return The L-System's Axiom
	 */
	public Axiom getAxiom() {
		return myAxiom;
	}

	/**
	 * @return The underlying Grammar of the L-System.
	 */
	public Grammar getGrammar() {
		return myGrammar;
	}

	/**
	 * Adds all Symbols in the Axiom to the proper alphabet in the Grammar.
	 */
	private void addAxiomToAlphabet() {
		if (myAxiom != null && !myAxiom.isEmpty()) {

			VariableAlphabet vars = myGrammar.getVariables();
			TerminalAlphabet terms = myGrammar.getTerminals();

			for (Symbol s : myAxiom) {
				if (!(vars.contains(s) || terms.contains(s))) {
					if (s instanceof Variable)
						myGrammar.getVariables().add(s);
					else
						myGrammar.getTerminals().add(s);
				}
			}
		}
	}

	/**
	 * Creates a map of all SymbolStrings on the LHS of Productions in the
	 * Grammar to their (possibly more than 1) RHSs.
	 */
	private void initReplacements() {
		myReplacements = new TreeMap<SymbolString, List<SymbolString>>();
		ProductionSet prods = myGrammar.getProductionSet();

		for (Production p : prods) {
			SymbolString lhs = new SymbolString(p.getLHS());
			if (!myReplacements.containsKey(lhs))
				myReplacements.put(lhs, new ArrayList<SymbolString>());
			SymbolString rhs = new SymbolString(p.getRHS());
			myReplacements.get(lhs).add(rhs);
		}
	}
	
	/**
	 * Sets the L-System's axiom to the given string, which will be converted to
	 * an <CODE>Axiom</CODE>.
	 * 
	 * @param axiom
	 *            The String to be set as the new Axiom.
	 */
	public boolean setAxiom(String axiom) {
		return setAxiom(new Axiom(axiom));
	}

	/**
	 * Sets the L-System's axiom to the given Axiom and adds it to the Grammar.
	 * 
	 * @param axiom
	 */
	public boolean setAxiom(Axiom axiom) {
		if(myAxiom.equals(axiom))
			return false;
		
		myAxiom = axiom;
		addAxiomToAlphabet();
		return true;
	}
}
