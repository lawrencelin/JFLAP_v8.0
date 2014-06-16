package model.algorithms.transform.fsa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import util.UtilFunctions;

import debug.JFLAPDebug;

import errors.BooleanWrapper;
import model.ClosureHelper;
import model.algorithms.AlgorithmException;
import model.algorithms.FormalDefinitionAlgorithm;
import model.algorithms.steppable.AlgorithmStep;
import model.automata.State;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.automata.determinism.FSADeterminismChecker;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public class NFAtoDFAConverter extends FormalDefinitionAlgorithm<FiniteStateAcceptor>{


	private TreeMap<State, State[]> myStateToStatesMap;
	private TreeMap<State, MappingWrapper> myStatesToSymbolsMap;
	private FiniteStateAcceptor myDFA;

	public NFAtoDFAConverter(FiniteStateAcceptor dfa){
		super(dfa);

	}

	@Override
	public String getDescriptionName() {
		return "NFA to DFA converter";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BooleanWrapper[] checkOfProperForm(FiniteStateAcceptor dfa) {
		List<BooleanWrapper> errors = new ArrayList<BooleanWrapper>();
		FSADeterminismChecker check = new FSADeterminismChecker();
		if (check.isDeterministic(dfa))
			errors.add(new BooleanWrapper(false,"This FSA is a DFA already!"));
		if(!FiniteStateAcceptor.hasAllSingleSymbolInput(dfa))
			errors.add(new BooleanWrapper(false, "The NFA to convert must have transitions " +
					"with either 1 or 0 input symbols."));
		return errors.toArray(new BooleanWrapper[0]);
	}

	@Override
	public AlgorithmStep[] initializeAllSteps() {
		return new AlgorithmStep[]{new ExpandStateStep()};
	}

	@Override
	public boolean reset() throws AlgorithmException {
		myDFA = this.getNFA().alphabetAloneCopy();
		myStateToStatesMap = new TreeMap<State, State[]>();
		myStatesToSymbolsMap = new TreeMap<State, MappingWrapper>();
		createAndAddInitialState();
		return true;
	}

	public BooleanWrapper expandState(State from){
		if (myStatesToSymbolsMap.get(from).isFullyExpanded())
			return new BooleanWrapper(false, "The state " + from.getName() + " is fully expanded.");

		for (Symbol sym: getNFA().getInputAlphabet()){
			State[] expand = myStatesToSymbolsMap.get(from).getStatesForSymbol(sym);
			if (expand.length > 0)
				executeExpandFromState(from, sym, expand);
		}

		
		return new BooleanWrapper(true);
	}

	public State[] getExpansionForState(State from, Symbol sym){
		return myStatesToSymbolsMap.get(from).getStatesForSymbol(sym);
	}


	public BooleanWrapper expandFromState(State from, Symbol sym, State ... array) {
		
		State[] expand = getExpansionForState(from, sym);
		ArrayList<State> temp1 = new ArrayList<State>(Arrays.asList(expand));
		temp1.removeAll(Arrays.asList(array));
		if (!temp1.isEmpty() || expand.length != array.length){
			return new BooleanWrapper(false, "This is not the correct set of states that moving " +
					"along this transition expands to.");
		}
		
		return executeExpandFromState(from, sym, array);
	}

	private BooleanWrapper executeExpandFromState(State from, Symbol sym,
			State... array) {
		State to = getDFAStateForNFAStates(array);
		if (to == null)
			to = createAndAddDFAState(array);

		FSATransition trans = new FSATransition(from, to, new SymbolString(sym));
		this.getDFA().getTransitions().add(trans);
		
		myStatesToSymbolsMap.get(from).expansionComplete(sym);
		
		return new BooleanWrapper(true);
	}

	public FiniteStateAcceptor getDFA() {
		return myDFA;
	}

	public State getDFAStateForNFAStates(State[] array) {
		for (Entry<State, State[]> entry: myStateToStatesMap.entrySet()){
			List<State> temp1 = new ArrayList<State>(Arrays.asList(entry.getValue()));
			if (temp1.size() == array.length && 
					temp1.containsAll(Arrays.asList(array)))
				return entry.getKey();
		}
		return null;
	}

	private State createAndAddDFAState(State[] array) {
		State s = createStateForStates(array);
		myStateToStatesMap.put(s, array);
		myDFA.getStates().add(s);
		myStatesToSymbolsMap.put(s, new MappingWrapper(s));
		for (State state: array){
			if (this.getNFA().getFinalStateSet().contains(state)){
				myDFA.getFinalStateSet().add(s);
				break;
			}
		}
		return s;
	}

	public Collection<State> getStatesToExpandTo(State s, Symbol sym) {
		State[] linkedStates = getLinkedStates(s);
		Set<State> toStates = new TreeSet<State>();
		for (State state: linkedStates){
			for (FSATransition tran: findTransitionsFromStateOnSym(state, sym)){
				State to = tran.getToState();
				Set<State> closure = ClosureHelper.takeClosure(to, getNFA());
				toStates.addAll(closure);
			}
		}
		return toStates;
	}

	private List<FSATransition> findTransitionsFromStateOnSym(State state, Symbol sym) {
		List<FSATransition> list = new ArrayList<FSATransition>();
		for (State s: ClosureHelper.takeClosure(state, getNFA())){
			for (FSATransition trans : getNFA().getTransitions().getTransitionsFromState(s)){
				if (!trans.isLambdaTransition() && trans.getInput()[0].equals(sym)){
					list.add(trans);
				}

			}
		}
		return list;
	}

	private State[] getLinkedStates(State s) {
		return myStateToStatesMap.get(s);
	}

	public FiniteStateAcceptor getNFA(){
		return super.getOriginalDefinition();
	}

	private void createAndAddInitialState() {
		State NFAstart = getNFA().getStartState();
		TreeSet<State> closure = ClosureHelper.takeClosure(NFAstart, getNFA());
		State DFAstart = createAndAddDFAState(closure.toArray(new State[0]));
		this.myDFA.setStartState(DFAstart);
	}

	private State createStateForStates(State ... states) {
		int id = this.getDFA().getStates().getNextUnusedID();
		State s = new State(createName(states), id);
		return s;
	}


	private String createName(State...states) {
		String name = UtilFunctions.toDelimitedString(states, ",");
		name =  "{" + name + "}";
		return name;
	}
	
	public State getFirstUnexpandedState() {
		for (Entry<State, MappingWrapper> entry : myStatesToSymbolsMap.entrySet()){
			if (!entry.getValue().isFullyExpanded()){
				return entry.getKey();
			}
		}
		return null;
	}
	
	public Set<State> getUnexpandedStates() {
		Set<State> unexpanded = new TreeSet<State>();
		for (Entry<State, MappingWrapper> entry : myStatesToSymbolsMap.entrySet()){
			if (!entry.getValue().isFullyExpanded()){
				unexpanded.add(entry.getKey());
			}
		}
		return unexpanded;
	}
	
	public int numTransitionsNeeded() {
		int n = 0;
		for (Entry<State, MappingWrapper> entry : myStatesToSymbolsMap.entrySet()){
			if (!entry.getValue().isFullyExpanded()){
				State from = entry.getKey();
				
				for (Symbol sym: getNFA().getInputAlphabet()){
					State[] expand = myStatesToSymbolsMap.get(from).getStatesForSymbol(sym);
					if (expand.length > 0)
						n++;
				}
						
			}
		}
		return n;
	}
	
	
	private class MappingWrapper{
		private Map<Symbol, State[]> myMap;
		public MappingWrapper(State DFAstate) {
			myMap = new TreeMap<Symbol, State[]>();
			for (Symbol s: getNFA().getInputAlphabet()){
				Collection<State> states = getStatesToExpandTo(DFAstate, s);
				myMap.put(s, states.toArray(new State[0]));
			}
		}
		
		public State[] getStatesForSymbol(Symbol s){
			return myMap.get(s);
		}
		
		public void expansionComplete(Symbol s){
			myMap.put(s, new State[0]);
		}
		
		public boolean isFullyExpanded(){
			for (State[] states: myMap.values()){
				if (states.length > 0)
					return false;
			}
			return true;
		}
		
	}
	
	//////// Algorithm Steps ///////
	
	private class ExpandStateStep implements AlgorithmStep {

		@Override
		public String getDescriptionName() {
			return "Expand Next State";
		}

		@Override
		public String getDescription() {
			return null;
		}

		@Override
		public boolean execute() throws AlgorithmException {
			BooleanWrapper bw = expandState(getFirstUnexpandedState());
			
			if (bw.isError())
				throw new AlgorithmException(bw.getMessage());
			return true;
		}

		@Override
		public boolean isComplete() {
			return getFirstUnexpandedState() == null;
		}
		
	}

	public static FiniteStateAcceptor convertToDFA(FiniteStateAcceptor nfa) {
		NFAtoDFAConverter converter = new NFAtoDFAConverter(nfa);
		converter.stepToCompletion();
		return converter.getDFA();
	}

	

}
