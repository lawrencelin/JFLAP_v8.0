package model.algorithms.conversion.regextofa;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import debug.JFLAPDebug;
import errors.BooleanWrapper;

import model.algorithms.AlgorithmException;
import model.algorithms.FormalDefinitionAlgorithm;
import model.algorithms.conversion.ConversionAlgorithm;
import model.algorithms.conversion.regextofa.deexpressionifying.ConcatDeX;
import model.algorithms.conversion.regextofa.deexpressionifying.GroupingDeX;
import model.algorithms.conversion.regextofa.deexpressionifying.KleeneStarDeX;
import model.algorithms.conversion.regextofa.deexpressionifying.UnionDeX;
import model.algorithms.steppable.AlgorithmStep;
import model.algorithms.steppable.SteppableAlgorithm;
import model.automata.Automaton;
import model.automata.InputAlphabet;
import model.automata.StartState;
import model.automata.State;
import model.automata.StateSet;
import model.automata.TransitionSet;
import model.automata.acceptors.FinalStateSet;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.automata.acceptors.fsa.FSATransition;
import model.regex.GeneralizedTransitionGraph;
import model.regex.OperatorAlphabet;
import model.regex.RegularExpression;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public class RegularExpressionToNFAConversion extends ConversionAlgorithm<RegularExpression, FiniteStateAcceptor> {

	private List<FSATransition> myExpressionTransitions;
	private List<FSATransition> myRemainingLambaTransitions;
	private List<DeExpressionifier> myDeExpressionifiers;

	public RegularExpressionToNFAConversion(RegularExpression re) {
		super(re);
		initDeExpressionifiers();
		reset();
	}
	
	private void initDeExpressionifiers() {
		myDeExpressionifiers = new ArrayList<DeExpressionifier>();
		OperatorAlphabet ops = getRE().getOperators();
		myDeExpressionifiers.add(new KleeneStarDeX(ops));
		myDeExpressionifiers.add(new GroupingDeX(ops));
		myDeExpressionifiers.add(new UnionDeX(ops));
		myDeExpressionifiers.add(new ConcatDeX(ops));
	}

	@Override
	public String getDescriptionName() {
		return "RE to NFA converter";
	}

	public RegularExpression getRE(){
		return super.getOriginalDefinition();
	}
	
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AlgorithmStep[] initializeAllSteps() {
		return new AlgorithmStep[]{new BeginDeExpressionifyStep(),
				new CompleteDeExpressionifyStep()};
	}

	@Override
	public boolean reset() throws AlgorithmException {
		super.reset();
		myRemainingLambaTransitions = new ArrayList<FSATransition>();
		updateExpressionTransitions();
		return true;
	}
	
	////// Algorithm Steps //////
	
	@Override
	public BooleanWrapper[] checkOfProperForm(RegularExpression fd) {
		return new BooleanWrapper[0];
	}

	private void updateExpressionTransitions() {
		myExpressionTransitions = new ArrayList<FSATransition>();
		for (FSATransition t: getGTG().getTransitions()){
			if (isExpressionTransition(t))
				myExpressionTransitions.add(t);
		}
	}

	public GeneralizedTransitionGraph getGTG() {
		return (GeneralizedTransitionGraph) super.getConvertedDefinition();
	}

	private boolean isExpressionTransition(FSATransition t) {
		SymbolString input = new SymbolString(t.getInput());
		
		// This deals with the cases where operators are present
		if (input.containsAny(this.getRE().getOperators().toArray(new Symbol[0]))) {
			return true;
		}
		// This deals with the concatenation cases
		if (input.size() > 1) {
			return true;
		}
		return false;
//		return input.containsAny(this.getRE().getOperators().toArray(new Symbol[0]));
	}

	
	public void addLambdaTransition(State from, State to){
		for(FSATransition trans: myRemainingLambaTransitions){
			if (trans.getFromState().equals(from) &&
					trans.getToState().equals(to)){
				getGTG().getTransitions().add(trans);
				myRemainingLambaTransitions.remove(trans);
				return;
			}
		}
		
		throw new AlgorithmException("A lambda transition is not needed between " +
				from + " and " + to);
	}
	
	public void addAllRemainingLambdaTransitions() {
		getGTG().getTransitions().addAll(myRemainingLambaTransitions);
		myRemainingLambaTransitions.clear();
	}

	public void beginDeExpressionify(FSATransition t) {
		checkCanBeginDeExpressionify(t);
		
		for (DeExpressionifier dex: myDeExpressionifiers){
			if (dex.isApplicable(t)){
				myRemainingLambaTransitions.addAll(dex.adjustTransitionSet(t, getGTG()));
				updateExpressionTransitions();
				return;
			}
		}

		throw new AlgorithmException("Unable to dexpressionify anything.");
		
	}

	private void checkCanBeginDeExpressionify(FSATransition t) {
		if (this.isDeExpressingifying())
			throw new AlgorithmException("You are already de-Expressionizing an expression.");
		else if (!myExpressionTransitions.contains(t)){
			throw new AlgorithmException("You may not de-expressionify a transition " +
					"that is not a part of the generalized transition graph");
		}
	}


	private boolean isDeExpressingifying() {
		return !myRemainingLambaTransitions.isEmpty();
	}

	public List<FSATransition> getExpressionTransitions() {
		return myExpressionTransitions;
	}
	
	@Override
	public FiniteStateAcceptor getConvertedDefinition(){
//		if (this.isRunning())
//			throw new AlgorithmException("You may not retrieve the NFA until all " +
//					"transitions in the GTG have been de-expressionified.");

		return getGTG().createNFAFromGTG();
	}

	
	////// Algorithm Steps //////
	
	private class BeginDeExpressionifyStep implements AlgorithmStep{

		@Override
		public String getDescriptionName() {
			return "Begin DeExpressionify";
		}

		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean execute() throws AlgorithmException {
			FSATransition t = getExpressionTransitions().get(0);
			beginDeExpressionify(t);
			
			return true;
		}

		@Override
		public boolean isComplete() {
			return getExpressionTransitions().isEmpty() || 
					isDeExpressingifying();
		}
		
	}

	private class CompleteDeExpressionifyStep implements AlgorithmStep{
		@Override
		public String getDescriptionName() {
			return "Complete DeExpressionify";
		}

		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean execute() throws AlgorithmException {

			addAllRemainingLambdaTransitions();
			
			return true;
		}

		@Override
		public boolean isComplete() {
			return !isDeExpressingifying();
		}
		
	}

	@Override
	public FiniteStateAcceptor createBaseConverted() {
		return new GeneralizedTransitionGraph(this.getRE());
	}

	public FiniteStateAcceptor getCompletedNFA() {
		return getConvertedDefinition();
	}
	
}
