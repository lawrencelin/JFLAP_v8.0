package model.algorithms.conversion.autotogram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import debug.JFLAPDebug;

import errors.BooleanWrapper;
import model.algorithms.AlgorithmException;
import model.algorithms.steppable.AlgorithmExecutingStep;
import model.algorithms.steppable.AlgorithmStep;
import model.algorithms.transform.grammar.UselessProductionRemover;
import model.automata.StartState;
import model.automata.State;
import model.automata.acceptors.FinalStateSet;
import model.automata.acceptors.pda.PDATransition;
import model.automata.acceptors.pda.PushdownAutomaton;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.Variable;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public class PDAtoCFGConverter extends AutomatonToGrammarConversion<PushdownAutomaton,PDAVariableMapping, PDATransition>{


	public PDAtoCFGConverter(PushdownAutomaton automaton)
			throws AlgorithmException {
		super(automaton);
	}

	@Override
	public String getDescriptionName() {
		return "PDA to CFG Converter";
	}

	@Override
	public String getDescription() {
		return "Converts a PDA into a CFG";
	}

	@Override
	public boolean isStartMapping(PDAVariableMapping mapping) {
		State s = this.getAutomaton().getStartState();
		FinalStateSet fs = this.getAutomaton().getFinalStateSet();
		Symbol bos = this.getAutomaton().getBottomOfStackSymbol();		
		return mapping.getFirstState().equals(s) && 
				fs.contains(mapping.getSecondState()) &&
				mapping.getStackSymbol().equals(bos);
	}

	@Override
	public Production[] convertTransition(PDATransition trans) {
		SymbolString lhs = new SymbolString();
		SymbolString rhs = new SymbolString();

		State from = trans.getFromState();
		State to = trans.getToState();

		Symbol pop = trans.getPop()[0];
		
		SymbolString input = convertToTerminals(trans.getInput());
		Symbol[] push = trans.getPush();
		
		PDAVariableMapping map;
		
		if (push.length == 0){
			rhs.addAll(input);
			map = new PDAVariableMapping(from, pop, to);
			Variable var = this.getVarForMapping(map);
			lhs.add(var);
			return new Production[]{new Production(lhs, rhs)};
		}
		
		// push size = 2
		ArrayList<Production> productions = new ArrayList<Production>();
		for (State qk: this.getAutomaton().getStates()){
			for (State ql : this.getAutomaton().getStates()){
				lhs = new SymbolString();
				rhs = new SymbolString();
				Variable var1,var2;
				map = new PDAVariableMapping(from, pop, qk);
				var1 = this.getVarForMapping(map);
				lhs.add(var1);
				
				map = new PDAVariableMapping(to, push[0], ql);
				var1 = this.getVarForMapping(map);
				
				map = new PDAVariableMapping(ql, push[1], qk);
				var2 = this.getVarForMapping(map);
				
				rhs.addAll(input);
				rhs.add(var1);
				rhs.add(var2);
				productions.add(new Production(lhs, rhs));
			}
		}
		
		return productions.toArray(new Production[0]);
	}

	@Override
	public Set<PDAVariableMapping> getAllNecessaryMappings() {
		
		Set<PDAVariableMapping> possible = new HashSet<PDAVariableMapping>();
		
		for (State qi: this.getAutomaton().getStates()){
			for (State qj : this.getAutomaton().getStates()){
				for (Symbol s: this.getAutomaton().getStackAlphabet()){
					possible.add(new PDAVariableMapping(qi, s, qj));
				}
			}
		}
		
		return possible;
	}

	@Override
	public BooleanWrapper[] checkOfProperForm(PushdownAutomaton pda) {
		
		ArrayList<BooleanWrapper> errors = new ArrayList<BooleanWrapper>();
		
		if (pda.getFinalStateSet().size() != 1){
			errors.add(new BooleanWrapper(false, 
						"You may only convert a PDA if it has a single final state."));
		}
		
		for (PDATransition trans : pda.getTransitions()){
			Symbol[] input = trans.getInput();
			Symbol[] pop = trans.getPop();
			Symbol[] push = trans.getPush();
			
			if (pop.length == 0 || pop.length > 1)
				errors.add(new BooleanWrapper(false, 
						"Every transition must pop 1 symbol: " + trans.toString()));
			if (input.length > 1)
				errors.add(new BooleanWrapper(false, 
							"Every transition must read 1 or 0 input symbols: " + trans.toString()));
			if(push.length > 2 || push.length == 1)
				errors.add(new BooleanWrapper(false, 
							"A transition must push 2 or 0 symbols: " + trans.toString()));
			if (pda.getFinalStateSet().contains(trans.getToState()) &&
					(pop.length > 0 && (!pop[0].equals(pda.getBottomOfStackSymbol()) ||
							push.length != 0))){
				errors.add(new BooleanWrapper(false, 
												"Upon entering a final state, the stack must be empty." +
												" Therefore, you must pop the bottom of stack symbol" +
												" and push on the empty string."));
			}
		}
		
		
		return errors.toArray(new BooleanWrapper[0]);
	}

	@Override
	public AlgorithmStep[] initializeAllSteps() {
		LinkedList<AlgorithmStep> steps = new LinkedList<AlgorithmStep>();
		steps.addAll(Arrays.asList(super.initializeAllSteps()));
		steps.add(new ThinProductionsAndAlphabets());
		return steps.toArray(new AlgorithmStep[0]);
	}
	
	@Override
	public boolean isComplete() {
		JFLAPDebug.print(variableMappingsComplete());
		JFLAPDebug.print(allTransitionsConverted());
		JFLAPDebug.print(getConvertedGrammar().isComplete()[0]);
		return super.isComplete();
	}
	
	
	
	/**
	 * Thins out the converted grammar based on the 
	 * {@link UselessProductionRemover} algorithm.
	 * This makes the resulting CFG far more manageable.
	 * @author Julian
	 *
	 */
	private class ThinProductionsAndAlphabets extends AlgorithmExecutingStep<UselessProductionRemover>{

		
		@Override
		public UselessProductionRemover initializeAlgorithm() {
			return new UselessProductionRemover(getConvertedGrammar());
		}
		
		@Override
		public void updateDataInMetaAlgorithm() {
			Grammar g = this.getAlgorithm().getTransformedGrammar();
			getConvertedGrammar().getProductionSet().retainAll(g.getProductionSet());
			getConvertedGrammar().trimAlphabets();
		}
		
	}
	
}
