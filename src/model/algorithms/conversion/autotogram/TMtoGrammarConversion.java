package model.algorithms.conversion.autotogram;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import debug.JFLAPDebug;
import model.algorithms.AlgorithmException;
import model.algorithms.steppable.AlgorithmStep;
import model.automata.InputAlphabet;
import model.automata.State;
import model.automata.TransitionSet;
import model.automata.turing.MultiTapeTMTransition;
import model.automata.turing.MultiTapeTuringMachine;
import model.automata.turing.TuringMachineMove;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.ProductionSet;
import model.grammar.Terminal;
import model.grammar.Variable;
import model.grammar.VariableAlphabet;
import model.symbols.Symbol;
import model.symbols.SymbolString;
import errors.BooleanWrapper;

/**
 * Conversion algorithm from single-tape Turing Machine (non-block) with no Stay
 * transitions to an unrestricted grammar.
 * 
 * @author Ian McMahon
 * 
 */
public class TMtoGrammarConversion
		extends
		AutomatonToGrammarConversion<MultiTapeTuringMachine, TMVariableMapping, MultiTapeTMTransition> {
	private Set<State> finalCopy;

	public TMtoGrammarConversion(MultiTapeTuringMachine automaton)
			throws AlgorithmException {
		super(automaton);
		finalCopy = getOriginalDefinition().getFinalStateSet().toCopiedSet();
	}

	@Override
	public String getDescriptionName() {
		return "Turing machine to Grammar conversion";
	}

	@Override
	public String getDescription() {
		return "Algorithm (from Linz book) to convert TM to Unrestricted Grammar";
	}

	@Override
	public boolean isStartMapping(TMVariableMapping mapping) {
		return false;
	}
	
	@Override
	public boolean isComplete() {
		// TODO Auto-generated method stub
		return super.isComplete() && hasAllAdditionProductions() && finalCopy.isEmpty();
	}

	@Override
	public Production[] convertTransition(MultiTapeTMTransition trans) {
		Set<Production> productionSet = new HashSet<Production>();

		State i = trans.getFromState();
		State j = trans.getToState();
		Symbol c = trans.getRead(0);
		Symbol d = trans.getWrite(0);

		InputAlphabet sigmaAndBlank = createSigmaAndBlank();

		for (Symbol a : sigmaAndBlank) {
			for (Symbol p : sigmaAndBlank) {
				for (Symbol q : getAutomaton().getTapeAlphabet()) {

					Variable Vaic = getVariable(a, i, c);
					Variable Vpq = getVariable(p, q);
					Variable Vad = getVariable(a, d);
					Variable Vpjq = getVariable(p, j, q);

					Terminal aTerminal = new Terminal(a.getString()), pTerminal = new Terminal(
							p.getString());

					SymbolString LHS = new SymbolString(Vpq, Vaic);
					SymbolString RHS = new SymbolString(Vpjq, Vad);

					if (trans.getMove(0) == TuringMachineMove.RIGHT) {
						LHS = LHS.reverse();
						RHS = RHS.reverse();
					}

					Production doubleVariableProduction = new Production(LHS,
							RHS);
					Production leftTerminalProduction = createTwoToTwoProduction(
							aTerminal, Vpq, aTerminal, pTerminal);
					Production rightTerminalProduction = createTwoToTwoProduction(
							Vpq, aTerminal, pTerminal, aTerminal);

					addAll(productionSet, doubleVariableProduction,
							leftTerminalProduction, rightTerminalProduction);
				}
			}
		}
		return productionSet.toArray(new Production[0]);
	}

	@Override
	public Set<TMVariableMapping> getAllNecessaryMappings() {
		Set<TMVariableMapping> mappingSet = new HashSet<TMVariableMapping>();
		MultiTapeTuringMachine tm = getAutomaton();

		InputAlphabet sigmaAndBlank = createSigmaAndBlank();
		for (MultiTapeTMTransition transition : tm.getTransitions()) {

			for (Symbol a : sigmaAndBlank) {
				for (Symbol p : sigmaAndBlank) {
					for (Symbol q : tm.getTapeAlphabet()) {
						 addVariableMappings(mappingSet, transition, a, p, q);
					}
				}
			}
		}
		addExtraMappings(mappingSet);
		return mappingSet;
	}
	
	/**
	 * Add Vaa and Va0a for all a, in case addVariableMappings doesn't catch them.
	 */
	private void addExtraMappings(Set<TMVariableMapping> mappingSet) {
		MultiTapeTuringMachine tm = getAutomaton();
		InputAlphabet alph = tm.getInputAlphabet();
		State q0 = tm.getStartState();
		
		for(Symbol a : alph){
			TMVariableMapping Vaa = new TMVariableMapping(a, a);
			TMVariableMapping Va0a = new TMVariableMapping(a, q0, a);
			addAll(mappingSet, Vaa, Va0a);
		}
	}

	/**
	 * Helper method for adding Vaic, Vpq, Vad, Vpjq, and (if j is a final state) Vajq 
	 * to the mapping set for each transition. i and j are the to and from States (respectively)
	 * of transition, c and d are the read and write Symbols (respectively).
	 * @param a
	 * 		symbol in InputAlphabet U {blank}
	 * @param p
	 * 		symbol in InputAlphabet U {blank}
	 * @param q
	 * 		symbol in TapeAlphabet
	 */
	private void addVariableMappings(Set<TMVariableMapping> mappingSet, MultiTapeTMTransition transition, Symbol a, Symbol p, Symbol q){
		State i = transition.getFromState();
		State j = transition.getToState();
		Symbol c = transition.getRead(0);
		Symbol d = transition.getWrite(0);
		
		TMVariableMapping Vaic = new TMVariableMapping(a, i, c);
		TMVariableMapping Vpq = new TMVariableMapping(p, q);
		TMVariableMapping Vad = new TMVariableMapping(a, d);
		TMVariableMapping Vpjq = new TMVariableMapping(p, j, q);

		addAll(mappingSet, Vaic, Vpq, Vad, Vpjq);

		if (getAutomaton().getFinalStateSet().contains(j)) {
			TMVariableMapping Vajq = new TMVariableMapping(a,
					j, q);

			mappingSet.add(Vajq);
		}
	}

	/**
	 * 	Creates productions of form Vajq->a for each State j in the FinalStateSet
	 * @param j
	 *		the final state for which terminal productions are needed.
	 */
	public boolean addFinalTransition(State j){
		ProductionSet productionSet = getConvertedGrammar().getProductionSet();
		if(!finalCopy.contains(j))
			return false;
		MultiTapeTuringMachine tm = getAutomaton();
		TransitionSet<MultiTapeTMTransition> transitions = tm.getTransitions();
		
		InputAlphabet sigmaAndBlank = createSigmaAndBlank();
		
		for(MultiTapeTMTransition trans : transitions){
			if(!trans.getToState().equals(j)) continue;
			for(Symbol a : sigmaAndBlank){
				for(Symbol q : tm.getTapeAlphabet()){
					Variable Vajq = getVariable(a, j, q);
					Terminal aTerminal = new Terminal(a.getString());
					
					Production terminalProduction = new Production(Vajq, aTerminal);
					productionSet.add(terminalProduction);
						
				}
			}
		}
		finalCopy.remove(j);
		return true;
	}

	@Override
	public BooleanWrapper[] checkOfProperForm(MultiTapeTuringMachine fd) {
		List<BooleanWrapper> bw = new ArrayList<BooleanWrapper>();

		if (fd.getNumTapes() != 1)
			bw.add(new BooleanWrapper(false,
					"The Turing machine has multiple tapes"));

		for (MultiTapeTMTransition trans : fd.getTransitions()) {
			if (trans.getMove(0) == TuringMachineMove.STAY) {

				bw.add(new BooleanWrapper(false,
						"The Turing machine has Stay transitions"));
				break;
			}
		}

		return bw.toArray(new BooleanWrapper[0]);
	}
	
	public boolean hasAllAdditionProductions() {
		Symbol blank = getOriginalDefinition().getBlankSymbol();
		Terminal square = new Terminal(blank.getString());
		Production squareToLambda = new Production(square, new SymbolString());
		return getConvertedGrammar().getProductionSet().contains(squareToLambda);
	}
	
	public TreeSet<State> getUnhandledFinalStates() {
		return new TreeSet<State>(finalCopy);
	}

	@Override
	public AlgorithmStep[] initializeAllSteps() {
		AlgorithmStep[] old = super.initializeAllSteps();
		return new AlgorithmStep[] { old[0], new CreateAdditionalProductions(),
				old[1], new createFinalProductions() };
	}

	private class CreateAdditionalProductions implements AlgorithmStep {

		@Override
		public String getDescriptionName() {
			return "Create Additional Productions";
		}

		@Override
		public String getDescription() {
			return "Creates productions unrelated to transitions for TM to Grammar conversion";
		}

		@Override
		public boolean execute() throws AlgorithmException {
			return addAllExtraProductions();
		}

		@Override
		public boolean isComplete() {
			return hasAllAdditionProductions();
		}

	}

	private class createFinalProductions implements AlgorithmStep {

		@Override
		public String getDescriptionName() {
			return "Create final productions";
		}

		@Override
		public String getDescription() {
			return "Adds all terminal productions for each final state";
		}

		@Override
		public boolean execute() throws AlgorithmException {
			List<State> finalList = new ArrayList<State>(finalCopy);
			State j = finalList.remove(0);
			return addFinalTransition(j);
		}

		@Override
		public boolean isComplete() {
			return finalCopy.isEmpty();
		}

	}

	/**
	 * Adds productions unrelated to transitions as dictated by the algorithm
	 * from the Linz book. S->T|V__S|SV__, T->TVaa|Va0a for all a in
	 * <i>inputAlphabet U {square}</i>, square -> lambda
	 */
	public boolean addAllExtraProductions() {
		Grammar gram = getConvertedGrammar();
		ProductionSet prods = gram.getProductionSet();

		Variable S = getNextAvailableVariable("S");
		Variable T = getNextAvailableVariable("T");

		gram.setStartVariable(S);

		Symbol blank = getOriginalDefinition().getBlankSymbol();
		Terminal square = new Terminal(blank.getString());
		Variable Vblankblank = getVariable(blank, blank);

		Production startToT = new Production(S, T);
		Production startToBlankStart = new Production(S, Vblankblank, S);
		Production startToStartBlank = new Production(S, S, Vblankblank);
		Production squareToLambda = new Production(square, new SymbolString());

		boolean added = addAll(prods, startToT, startToBlankStart, startToStartBlank,
				squareToLambda);
		State q0 = getOriginalDefinition().getStartState();

		for (Symbol a : getAutomaton().getInputAlphabet()) {
			Production tToTVaa = new Production(T, T, getVariable(a, a));
			Production tToVa0a = new Production(T, getVariable(a, q0, a));
			added = added && addAll(prods, tToTVaa, tToVa0a);
		}

		return added;
	}

	/**
	 * Copies original input alphabet and adds the blank symbol to the copy
	 */
	private InputAlphabet createSigmaAndBlank() {
		MultiTapeTuringMachine tm = getAutomaton();
		InputAlphabet sigma = tm.getInputAlphabet().copy();
		Symbol blank = tm.getBlankSymbol().copy();

		sigma.add(blank);
		return sigma;
	}

	/**
	 * Returns new Production of form LHSa LHSb -> RHSa RHSb
	 */
	private Production createTwoToTwoProduction(Symbol LHSa, Symbol LHSb,
			Symbol RHSa, Symbol RHSb) {
		SymbolString LHS = new SymbolString(LHSa, LHSb);
		SymbolString RHS = new SymbolString(RHSa, RHSb);

		return new Production(LHS, RHS);
	}

	/**
	 * returns the Variable mapped to ab (default (Vab))
	 */
	private Variable getVariable(Symbol a, Symbol b) {
		return getVariable(a, null, b);
	}

	/**
	 * returns the Variable mapped to aib (default (Vaib))
	 */
	private Variable getVariable(Symbol a, State i, Symbol b) {
		TMVariableMapping mapping = new TMVariableMapping(a, i, b);
		return getVarForMapping(mapping);
	}

	/**
	 * helper method to add multiple items of the same type to one set
	 */
	private <T> boolean addAll(Set<T> set, T... toAdd) {
		boolean added = true;
		for (T t : toAdd) {
			if(!set.add(t))
				added = false;
		}
		return added;
	}

	/**
	 * Given a desired string representation var, will return a Variable with
	 * correct open and closed group, with zero or more <b>'</b> characters
	 * following it, depending on what is already in the VariableAlphabet.
	 */
	private Variable getNextAvailableVariable(String var) {
		Grammar gram = getConvertedGrammar();
		VariableAlphabet variables = gram.getVariables();
		char open = gram.getOpenGroup();
		char close = gram.getCloseGroup();

		while (variables.containsSymbolWithString(var)) {
			var += "'";
		}

		return new Variable(open + var + close);
	}
}
