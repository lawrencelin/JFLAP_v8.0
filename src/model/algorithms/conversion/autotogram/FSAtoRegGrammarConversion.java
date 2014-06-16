package model.algorithms.conversion.autotogram;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import errors.BooleanWrapper;
import model.algorithms.AlgorithmException;
import model.algorithms.steppable.AlgorithmStep;
import model.automata.Automaton;
import model.automata.State;
import model.automata.SingleInputTransition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.automata.acceptors.fsa.FSATransition;
import model.grammar.Production;
import model.grammar.Variable;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public class FSAtoRegGrammarConversion extends AutomatonToGrammarConversion<FiniteStateAcceptor, FSAVariableMapping, FSATransition> {

	private Set<State> finalStatesHandled;

	public FSAtoRegGrammarConversion(FiniteStateAcceptor automaton)
			throws AlgorithmException {
		super(automaton);
	}

	@Override
	public String getDescriptionName() {
		return "FSA to Regular Grammar Conversion";
	}

	@Override
	public String getDescription() {
		return "Converts a finite state automaton to a right-linear grammar.";
	}

	public boolean finishFinalStateProductions() {
		boolean added = true;
		for (State s: this.getAutomaton().getFinalStateSet()){
			added &= addFinalStateProduction(s);
		}
		return added;
	}

	public boolean addFinalStateProduction(State s) {
		Variable v = this.getVarForMapping(new FSAVariableMapping(s));
		SymbolString lhs = new SymbolString(v);
		Production p = new Production(lhs, new SymbolString());

		boolean added = this.getConvertedGrammar().getProductionSet().add(p);
		if (added) added &= finalStatesHandled.add(s);
		return added;
	}

	@Override
	public boolean isStartMapping(FSAVariableMapping mapping) {
		State s = mapping.getState();
		return s.equals(this.getAutomaton().getStartState());
	}

	@Override
	public Production[] convertTransition(FSATransition trans) {
		FSAVariableMapping from = new FSAVariableMapping(trans.getFromState());
		FSAVariableMapping to = new FSAVariableMapping(trans.getToState());
		SymbolString lhs = new SymbolString(this.getVarForMapping(from));
		SymbolString rhs = new SymbolString(convertToTerminals(trans.getInput()));
		rhs.add(this.getVarForMapping(to));
		Production p = new Production(lhs, rhs);
		return new Production[]{p};
	}

	@Override
	public Set<FSAVariableMapping> getAllNecessaryMappings() {
		Set<FSAVariableMapping> mappings = new HashSet<FSAVariableMapping>();
		for (State s: this.getAutomaton().getStates()){
			mappings.add(new FSAVariableMapping(s));
		}
		return mappings;
	}

	@Override
	public BooleanWrapper[] checkOfProperForm(FiniteStateAcceptor automaton) {
		return new BooleanWrapper[0];
	}

	@Override
	public boolean isComplete() {
		return super.isComplete() && allFinalStatesHandled();
	}

	public boolean allFinalStatesHandled() {
		Set<State> finalStates = new HashSet<State>(this.getAutomaton().getFinalStateSet());
		finalStates.removeAll(finalStatesHandled);
		return finalStates.isEmpty();
	}
	
	public Set<State> getUnhandledStates() {
		Set<State> finalStates = new HashSet<State>(this.getAutomaton().getFinalStateSet());
		finalStates.removeAll(finalStatesHandled);
		return finalStates;
	}

	@Override
	public boolean reset() throws AlgorithmException {
		finalStatesHandled = new HashSet<State>();
		return super.reset();
	}
	
	
	

	/////////////////////////////////////////////////
	////////////// Algorithm Steps //////////////////
	/////////////////////////////////////////////////
	
	
	@Override
	public AlgorithmStep[] initializeAllSteps() {
		List<AlgorithmStep> step = new ArrayList<AlgorithmStep>();
		step.addAll(Arrays.asList(super.initializeAllSteps()));
		step.add(new AddFinalStateProductions());
		return step.toArray(new AlgorithmStep[0]);
	}




	/**
	 * Creates productions for each final state in the grammar.
	 * These productions are of the form:
	 * 			A -> lambda 
	 * Where A  is the variable mapped to the final state.
	 * 	
	 * @author Julian
	 *
	 */
	private class AddFinalStateProductions implements AlgorithmStep{

		@Override
		public String getDescriptionName() {
			return "Create Productions for Final States";
		}

		@Override
		public String getDescription() {
			return "Create lambda productions for each individual final state.";
		}

		@Override
		public boolean execute() throws AlgorithmException {
			return finishFinalStateProductions();
		}

		@Override
		public boolean isComplete() {
			return allFinalStatesHandled();
		}
		
	}

}
