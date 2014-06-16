package model.algorithms.conversion.gramtoauto;

import java.security.AllPermission;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import debug.JFLAPDebug;

import util.UtilFunctions;

import errors.BooleanWrapper;

import model.algorithms.AlgorithmException;
import model.algorithms.FormalDefinitionAlgorithm;
import model.algorithms.conversion.ConversionAlgorithm;
import model.algorithms.steppable.AlgorithmStep;
import model.algorithms.steppable.SteppableAlgorithm;
import model.automata.Automaton;
import model.automata.InputAlphabet;
import model.automata.State;
import model.automata.StateSet;
import model.automata.SingleInputTransition;
import model.automata.turing.TuringMachineMove;
import model.formaldef.components.alphabets.Alphabet;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.ProductionSet;
import model.grammar.Terminal;
import model.grammar.Variable;
import model.grammar.typetest.GrammarType;
import model.symbols.Symbol;

public abstract class GrammarToAutomatonConverter<T extends Automaton<S>, S extends SingleInputTransition<S>> 
																		extends ConversionAlgorithm<Grammar, T> {

	private ProductionSet myConvertedProductions;

	private Set<State> myAutoStates;

	
	public GrammarToAutomatonConverter(Grammar g){
		super(g);
	}
	
	@Override
	public BooleanWrapper[] checkOfProperForm(Grammar g) {
		
		List<GrammarType> valid = Arrays.asList(this.getValidTypes());
		
		for (GrammarType gt: GrammarType.getType(g)){
			if (valid.contains(gt))
					return new BooleanWrapper[0];
		}
		
		BooleanWrapper error = new BooleanWrapper(false, "The grammar must be of one of " +
				"the following forms for the " + this.getDescriptionName() + ": " +
				UtilFunctions.createDelimitedString(valid, ","));
		
		return new BooleanWrapper[]{error};
	}

	public boolean convertAlphabets() {
		Symbol[] terms = this.getGrammar().getTerminals().toArray(new Symbol[0]);
		InputAlphabet inAlph = getConvertedAutomaton().getInputAlphabet();
		return Alphabet.addCopiedSymbols(inAlph,terms);
	}
	
	public boolean alphabetsConverted() {
		return this.getGrammar().getTerminals().size() == 
				this.getConvertedAutomaton().getInputAlphabet().size();
	}
	
	
	public Grammar getGrammar() {
		return super.getOriginalDefinition();
	}

	public T getConvertedAutomaton() {
		return getConvertedDefinition();
	}

	public boolean convertAllProductions(){
		for (Production p : this.getUnconvertedProductions()){
			if(!this.convertAndAddProduction(p))
				return false;
		}
		return true;
	}
	
	public boolean convertAndAddProduction(Production p) {
		S trans = this.convertProduction(p);
		boolean success = trans != null && 
				this.getConvertedAutomaton().getTransitions().add(trans);
		if (success)
			success = success && myConvertedProductions.add(p);
		return success;
	}
	
	public boolean addConvertedProduction(Production p) {
		S trans = this.convertProduction(p);
		boolean success = trans != null && getConvertedAutomaton().getTransitions().contains(trans);
		if(success) 
			success = success && myConvertedProductions.add(p);
		return success;
	}

	public Set<Production> getUnconvertedProductions(){
		TreeSet<Production> all = new TreeSet<Production>(this.getGrammar().getProductionSet());
		all.removeAll(this.getConvertedProductions());
		return all;
	}
	
	
	public Set<Production> getConvertedProductions() {
		return myConvertedProductions;
	}

	public boolean allProductionsConverted() {
		return getUnconvertedProductions().isEmpty();
	}

	public boolean autoStatesAdded() {
		StateSet states = this.getConvertedAutomaton().getStates();
		return myAutoStates != null && states.containsAll(myAutoStates);
	}

	@Override
	public AlgorithmStep[] initializeAllSteps() {
		return new AlgorithmStep[]{
				new ConvertProductions()};
	}


	@Override
	public boolean reset() throws AlgorithmException {
		myConvertedProductions = new ProductionSet();
		super.reset();
		return doSetup();
	}



	public abstract boolean doSetup();

	public abstract GrammarType[] getValidTypes();
	
	public abstract S convertProduction(Production p);
	
	
	
	/////////////////////////////////////////////////
	////////////// Algorithm Steps //////////////////
	/////////////////////////////////////////////////
	
//	/**
//	 *  Convert all of the terminals in the terminal alphabet
//	 *  of the grammar into symbols in the input alphabet of
//	 *  the automaton.
//	 * @author Julian
//	 *
//	 */
//	private class ConvertAlphabets implements AlgorithmStep{
//
//		@Override
//		public String getDescriptionName() {
//			return "Convert Terminals to Input Alphabet";
//		}
//
//		@Override
//		public String getDescription() {
//			return "Convert all of the terminals in the terminal alphabet" +
//					" of the grammar into symbols in the input alphabet of" +
//					" the automaton";
//		}
//
//		@Override
//		public boolean execute() throws AlgorithmException {
//			return convertAlphabets();
//		}
//
//		@Override
//		public boolean isComplete() {
//			return alphabetsConverted();
//		}
//		
//	}

	
	/**
	 * Converts all productions i nthe grammar into transition
	 * functions in the automaton. This is executed via the 
	 * specific implementation in the {@link GrammarToAutomatonConverter}
	 * subclass.
	 * 
	 * @author Julian
	 *
	 */
	private class ConvertProductions implements AlgorithmStep{

		@Override
		public String getDescriptionName() {
			return "Convert Productions";
		}

		@Override
		public String getDescription() {
			return "Converts all productions into transitions in" +
					" the automaton.";
		}

		@Override
		public boolean execute() throws AlgorithmException {
			return convertAllProductions();
		}

		@Override
		public boolean isComplete() {
			return allProductionsConverted();
		}
		
	}
	
}

