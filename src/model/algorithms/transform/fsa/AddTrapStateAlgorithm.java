package model.algorithms.transform.fsa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import debug.JFLAPDebug;

import errors.BooleanWrapper;
import model.algorithms.AlgorithmException;
import model.algorithms.FormalDefinitionAlgorithm;
import model.algorithms.steppable.AlgorithmStep;
import model.automata.InputAlphabet;
import model.automata.State;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.automata.determinism.DeterminismChecker;
import model.automata.determinism.FSADeterminismChecker;
import model.formaldef.FormalDefinition;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public class AddTrapStateAlgorithm extends FormalDefinitionAlgorithm<FiniteStateAcceptor> {

	private static final String TRAP = "TRAP";
	private FiniteStateAcceptor myNewDFA;
	private State myTrapState;
	private Set<FSATransition> myTransitionsNeeded;

	public AddTrapStateAlgorithm(FiniteStateAcceptor fd) {
		super(fd);
	}

	@Override
	public String getDescriptionName() {
		return null;
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public BooleanWrapper[] checkOfProperForm(FiniteStateAcceptor dfa) {
		List<BooleanWrapper> errors = new ArrayList<BooleanWrapper>();
		FSADeterminismChecker check = new FSADeterminismChecker();
		if (!check.isDeterministic(dfa))
			errors.add(new BooleanWrapper(false,"You may not add a trap state to an NFA"));
		if(!FiniteStateAcceptor.hasAllSingleSymbolInput(dfa))
			errors.add(new BooleanWrapper(false, "The DFA to convert must have transitions " +
					"with either 1 or 0 input symbols."));
		return errors.toArray(new BooleanWrapper[0]);
	}

	@Override
	public AlgorithmStep[] initializeAllSteps() {
		return new AlgorithmStep[]{
				new AddStateStep(),
				new AddTransitionsStep()
		};
	}

	@Override
	public boolean reset() throws AlgorithmException {
		myNewDFA = (FiniteStateAcceptor) this.getDFA().copy();
		myTrapState = null;
		myTransitionsNeeded = new TreeSet<FSATransition>();
		return true;
	}

	private FiniteStateAcceptor getDFA() {
		return super.getOriginalDefinition();
	}
	
	private boolean AutoAddState() {
		State s = myNewDFA.getStates().createAndAddState();
		setupState(s);
		return true;
	}

	public boolean addStateAsTrapState(State s) {
		if (!myNewDFA.getStates().contains(s))
			return false;
		setupState(s);
		return true;
	}
	
	private void setupState(State s) {
		s.setName(TRAP);
		myTrapState = s;
		myTransitionsNeeded = getAllTransitionsNeeded();
		distributeChanged();
	}

	public boolean addTransition(State from, Symbol s){
		for (FSATransition trans: myTransitionsNeeded.toArray(new FSATransition[0])){
			
			if (trans.getFromState().equals(from) &&
					trans.getInput()[0].equals(s)){
				return addTransition(trans);
			}
		}
		return false;
	}

	public FiniteStateAcceptor getDFAWithTrapState() {
		return myNewDFA;
	}
	

	public boolean trapStateNeeded() {
		return !getAllTransitionsNeeded().isEmpty();
	}
	
	public int transitionsRemaining() {
		return getAllTransitionsNeeded().size();
	}

	private boolean addTransition(FSATransition trans) {
		if (!myNewDFA.getTransitions().add(trans))
			return false;
		boolean remove = myTransitionsNeeded.remove(trans);
		if(remove)
			distributeChanged();
		return remove;
	}

	private Set<FSATransition> getAllTransitionsNeeded() {
		Set<FSATransition> toAdd = new TreeSet<FSATransition>();
		for (State s: myNewDFA.getStates()){
			toAdd.addAll(getTransitionsNeededFor(s));
		}
		return toAdd;
	}

	private Collection<FSATransition> getTransitionsNeededFor(State from) {
		Collection<FSATransition> trans = myNewDFA.getTransitions().getTransitionsFromState(from);
		
		Set<FSATransition> needed = new TreeSet<FSATransition>();
		
		for (Symbol s: myNewDFA.getInputAlphabet()){
			boolean exists = false;
			for (FSATransition t: trans){
				if (t.getInput()[0].equals(s)){
					exists = true;
					break;
				}
			}
			if (!exists)
				needed.add(new FSATransition(from, myTrapState, new SymbolString(s)));
		}
		
		return needed;
	}

	private boolean addAllTransitionsNeeded() {
		for (FSATransition trans: myTransitionsNeeded.toArray(new FSATransition[0])){
			if (!addTransition(trans))
				return false;
		}
		return true;
	}

	
	public boolean hasTrapState() {
		return myTrapState != null;
	}
	
	public boolean isTrapState(State s){
		return hasTrapState() && myTrapState.equals(s);
	}

	public static boolean trapStateNeeded(FiniteStateAcceptor fsa) {
		FSADeterminismChecker check = new FSADeterminismChecker();
		if (!check.isDeterministic(fsa))
			return false;
		
		int transNum = fsa.getInputAlphabet().size();
		for (State s: fsa.getStates()){
			Set<FSATransition> fromSet = fsa.getTransitions().getTransitionsFromState(s);
			if (fromSet.size() != transNum)
				return true;
		}
		return false;
	}


	private class AddStateStep implements AlgorithmStep{

		@Override
		public String getDescriptionName() {
			return "Add Trap State";
		}

		@Override
		public String getDescription() {
			return null;
		}

		@Override
		public boolean execute() throws AlgorithmException {
			return AutoAddState();
		}

		@Override
		public boolean isComplete() {
			return !trapStateNeeded(myNewDFA) || hasTrapState();
		}
		
	}

	private class AddTransitionsStep implements AlgorithmStep{

		@Override
		public String getDescriptionName() {
			return "Add Transitions to Trap State";
		}

		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean execute() throws AlgorithmException {
			return addAllTransitionsNeeded();
		}

		@Override
		public boolean isComplete() {
			return myTransitionsNeeded.isEmpty();
		}
		
	}

}