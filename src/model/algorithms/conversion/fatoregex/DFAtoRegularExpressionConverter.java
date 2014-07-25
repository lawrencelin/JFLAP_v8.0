package model.algorithms.conversion.fatoregex;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import universe.preferences.JFLAPPreferences;
import debug.JFLAPDebug;
import errors.BooleanWrapper;
import model.algorithms.AlgorithmException;
import model.algorithms.FormalDefinitionAlgorithm;
import model.algorithms.steppable.AlgorithmStep;
import model.algorithms.steppable.SteppableAlgorithm;
import model.automata.InputAlphabet;
import model.automata.State;
import model.automata.StateSet;
import model.automata.TransitionSet;
import model.automata.acceptors.FinalStateSet;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.determinism.FSADeterminismChecker;
import model.formaldef.components.SetComponent;
import model.formaldef.components.alphabets.Alphabet;
import model.regex.EmptySub;
import model.regex.GeneralizedTransitionGraph;
import model.regex.OperatorAlphabet;
import model.regex.RegularExpression;
import model.regex.operators.KleeneStar;
import model.regex.operators.Operator;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public class DFAtoRegularExpressionConverter extends FormalDefinitionAlgorithm<FiniteStateAcceptor> {



	private GeneralizedTransitionGraph myGTG;
	private RegularExpression myRegEx;
	private List<FSATransition> newLambdaTransitions;
	private List<FSATransition> myCollapseList;
	public List<State> myStatesToCollapse;
	private State newFinal;


	public DFAtoRegularExpressionConverter (FiniteStateAcceptor fsa){
		super(fsa);
	}

	@Override
	public String getDescriptionName() {
		return "Finite Accepter to Regular Expression Converter";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AlgorithmStep[] initializeAllSteps() {
		
		return new AlgorithmStep[]{new DoSingleFinalState(),
				new CollapseTransitions(),
				new CreateEmptyTransitions(),
				new CollapseStates()};
	}

	@Override
	public boolean reset() throws AlgorithmException {
		myRegEx = new RegularExpression((InputAlphabet) getFA().getInputAlphabet().copy());
		myGTG = new GeneralizedTransitionGraph(getFA());
		newLambdaTransitions = new ArrayList<FSATransition>();
		myCollapseList = getTransitionCollapseList();
		myStatesToCollapse = getStatesToCollapse();
		return true;
	}


	@Override
	public BooleanWrapper[] checkOfProperForm(FiniteStateAcceptor fsa) {
		FSADeterminismChecker check = new FSADeterminismChecker();
		BooleanWrapper bw = new BooleanWrapper(check.isDeterministic(fsa), 
				"The FSA must be determinisitic in order to be converted into " +
				"a regular expression.");
		if (bw.isError())
			return new BooleanWrapper[]{bw};
		return new BooleanWrapper[0];
	}

	public RegularExpression getResultingRegEx(){
		if (this.canStep())
			throw new AlgorithmException("You may not retrieve the regular expression " +
					"until the GTG is fully simplified");
		
		if (myRegEx.isComplete().length == 0) return myRegEx;
		
		State start = getGTG().getStartState();
		FinalStateSet finalSet = getGTG().getFinalStateSet();
		
		State end = finalSet.first();
		
		TransitionSet<FSATransition> transitions = getGTG().getTransitions();
		
		FSATransition t1 = transitions.getTransitionsFromStateToState(start, start).iterator().next();
		FSATransition t2 = transitions.getTransitionsFromStateToState(start, end).iterator().next();
		FSATransition t3 = transitions.getTransitionsFromStateToState(end, end).iterator().next();
		FSATransition t4 = transitions.getTransitionsFromStateToState(end, start).iterator().next();
		
		SymbolString exp = new SymbolString(t2.getInput());
		if (!isEmptySetTransition(t1)){
			exp = concat(star(t1.getInput()), exp);
		}
		
		if (!isEmptySetTransition(t3)){
			exp = concat(exp, star(t3.getInput()));
		}
		
		if (!isEmptySetTransition(t4)){
			SymbolString expCopy = exp.copy();
			expCopy = concat(expCopy, new SymbolString(t4.getInput()));
			exp = concat(star(expCopy.toArray(new Symbol[0])), exp);
		}
		myRegEx.setTo(exp);
		return myRegEx;

	}
	
	/**
	 * Creates a single final state and sets up all of lamba transitions that
	 * must be added from the old final states. DOES NOT remove final states
	 * from final state set.
	 * @return
	 */
	public boolean createSingleFinalState() {

		newFinal = myGTG.getStates().createAndAddState();
		FinalStateSet finalStates = myGTG.getFinalStateSet();
		for(State s: finalStates){
			newLambdaTransitions.add(new FSATransition(s, newFinal, myRegEx.getOperators().getEmptySub()));
		}
		
		return finalStates.add(newFinal);
	}

	public boolean hasSingleFinal() {
		FinalStateSet finalStates = getGTG().getFinalStateSet();
		return finalStates.size() == 1 && !finalStates.contains(getGTG().getStartState()) ;
	}
	
	public State getSingleFinal() {
		return newFinal;
	}

	private FiniteStateAcceptor getFA() {
		return getOriginalDefinition();
	}
	
	public boolean needsTranstitionsCollapsed(){
		return !getTransitionCollapseList().isEmpty();
	}

	private List<FSATransition> getTransitionCollapseList() {
		StateSet states = getGTG().getStates();
		ArrayList<FSATransition> collapseTo = new ArrayList<FSATransition>();
		TransitionSet<FSATransition> trans = getGTG().getTransitions();
		for (State s1: states){
			for(State s2: states){
				Set<FSATransition> fromTo = trans.getTransitionsFromStateToState(s1, s2);
				if (fromTo.size() > 1){
					collapseTo.add(fromTo.iterator().next());
				}
			}
		}
		return collapseTo;
	}

	public boolean needsEmptyTransitions() {
		return !getEmptyTransitionsNeeded().isEmpty();
	}
	
	private List<FSATransition> getEmptyTransitionsNeeded() {
		StateSet states = getGTG().getStates();
		ArrayList<FSATransition> toAdd = new ArrayList<FSATransition>();
		TransitionSet<FSATransition> transSet = getGTG().getTransitions();
		for (State from: states){
			for(State to: states){
				if(transSet.getTransitionsFromStateToState(from, to).isEmpty())
					toAdd.add(new FSATransition(from, 
							to, 
							new SymbolString(JFLAPPreferences.getEmptySetSymbol())));
			}
		}
		return toAdd;
	}

	private boolean addAllEmptyTransitions(){
		List<FSATransition> emptyTrans = getEmptyTransitionsNeeded();
		if (emptyTrans.isEmpty()) return false;
		boolean added = getGTG().getTransitions().addAll(emptyTrans);
		if (added)
			emptyTrans.clear();
		return added;	
	}

	public FSATransition addEmptyTransition(State from, State to){
		for (FSATransition t: getEmptyTransitionsNeeded()){
			if (t.getToState().equals(to) && t.getFromState().equals(from)){
				getGTG().getTransitions().add(t);
				return t;
			}
		}
		return null;
	}


	public GeneralizedTransitionGraph getGTG() {
		return myGTG;
	}

	public boolean collapseAllStates() {
		for (State s: new ArrayList<State>(myStatesToCollapse)){
			boolean collapsed = collapseState(s);
			if (!collapsed)
				return false;
		}
		return true;
	}

	public boolean collapseState(State s) {
		if (!myStatesToCollapse.contains(s))
			return false;
		myStatesToCollapse.remove(s);
		Collection<FSATransition> toAdd = getTransitionsForCollapseState(s);

		return getGTG().getStates().remove(s) && doTransitionAdditions(toAdd);
		
	}
	
	public boolean needsStatesCollaped() {
		return !myStatesToCollapse.isEmpty();
	}

	private boolean doTransitionAdditions(Collection<FSATransition> toAdd) {
		
		TransitionSet<FSATransition> transSet = this.getGTG().getTransitions();
		
		for (FSATransition t: toAdd){
			Set<FSATransition> toRemove = 
					transSet.getTransitionsFromStateToState(t.getFromState(), t.getToState());
			transSet.removeAll(toRemove);
		}
		
		
		return getGTG().getTransitions().addAll(toAdd);
	}

	public Collection<FSATransition> getTransitionsForCollapseState(State k) {
		ArrayList<FSATransition> list = new ArrayList<FSATransition>();
		StateSet states = getGTG().getStates();
		for (State p : states) {
			
			if(!validTransitionForCollapse(p,k))
				continue;
			
			for (State q: states) {
				
				if (!validTransitionForCollapse(k,q))
					continue;
				
				SymbolString exp = getExpression(p, q, k);
				list.add(new FSATransition(p, q, exp));
			}
		}
		return list;
	}

	private boolean validTransitionForCollapse(State from, State to) {
		if (from.equals(to))
			return false;
		
		TransitionSet<FSATransition> transitions = getGTG().getTransitions();
		Set<FSATransition> pk = 
				transitions.getTransitionsFromStateToState(from, to);
		
		FSATransition test = (FSATransition) pk.toArray()[0];

		if (isEmptySetTransition(test))
			return false;
		
		return true;
		
	}

	private boolean isEmptySetTransition(FSATransition test) {
		return isEmptySetTransition(new SymbolString(test.getInput()));
	}

	private boolean isEmptySetTransition(SymbolString input) {
		return input.contains(JFLAPPreferences.getEmptySetSymbol());
	}

	/**
	 * Returns the expression obtained from evaluating the following equation:
	 * r(pq) = r(pq) + r(pk)r(kk)*r(kq), where p, q, and k represent the IDs of
	 * states in <CODE>automaton</CODE>.
	 * 
	 * @param p
	 *            the from state
	 * @param q
	 *            the to state
	 * @param k
	 *            the state being removed.
	 * @param automaton
	 *            the automaton.
	 * @return the expression obtained from evaluating the following equation:
	 *         r(pq) = r(pq) + r(pk)r(kk)*r(kq), where p, q, and k represent the
	 *         IDs of states in <CODE>automaton</CODE>.
	 */
	private SymbolString getExpression(State p, State q, State k) {

		SymbolString pq = getExpressionBetweenStates(p, q),
				pk = getExpressionBetweenStates(p, k),
				kk = getExpressionBetweenStates(k, k),
				kq = getExpressionBetweenStates(k, q);

		SymbolString exp = pk;
		if (!isEmptySetTransition(kk)){
			kk = star(kk.toArray(new Symbol[0]));
			exp = concat(exp, kk);
		}
		concat(exp,kq);
		if (!isEmptySetTransition(pq)){
			exp = union(exp, pq);
		}
		return pk;
	}

	private SymbolString union(SymbolString left, SymbolString right) {
		left.add(myRegEx.getOperators().getUnionOperator());
		left.addAll(right);
		return left;
	}

	private SymbolString concat(SymbolString left, SymbolString right) {
		OperatorAlphabet ops = myRegEx.getOperators();
		left = modifyForConcat(left);
		left.addAll(modifyForConcat(right));
		
		return left;
	}

	private SymbolString star(Symbol[] s) {
		SymbolString symbols = new SymbolString(s);
		OperatorAlphabet ops = myRegEx.getOperators();
		KleeneStar star = ops.getKleeneStar();
		SymbolString first = RegularExpression.getFirstOperand(symbols, ops);
		if (first.size() != symbols.size() ||
				symbols.endsWith(star)){
			symbols.addFirst(ops.getOpenGroup());
			symbols.addLast(ops.getCloseGroup());
		}
		
		symbols.add(star);
		
		return symbols;
	}

	private SymbolString modifyForConcat(SymbolString input) {
		OperatorAlphabet ops = myRegEx.getOperators();
		if (input.size() == 1 && input.startsWith(ops.getEmptySub()))
			return new SymbolString();
		boolean needsGrouping = false;
		for (int i = 0;;){
			SymbolString sub = input.subList(i);
			SymbolString first = RegularExpression.getFirstOperand(sub, ops);
			i = i + first.size();
			if (i >=  input.size()) break;
			if (input.get(i).equals(ops.getUnionOperator())){
				needsGrouping = true;
				break;
			}
		}
		
		if (needsGrouping){
			input.addFirst(ops.getOpenGroup());
			input.addLast(ops.getCloseGroup());
		}
		return input;
	}

	/**
	 * Returns the expression on the transition between <CODE>fromState</CODE>
	 * and <CODE>toState</CODE> in <CODE>automaton</CODE>.
	 * 
	 * @param fromState
	 *            the from state
	 * @param toState
	 *            the to state
	 * @param automaton
	 *            the automaton
	 * @return the expression on the transition between <CODE>fromState</CODE>
	 *         and <CODE>toState</CODE> in <CODE>automaton</CODE>.
	 */
	private SymbolString getExpressionBetweenStates(State fromState, State toState) {
		Set<FSATransition> transitions = getGTG().getTransitions()
				.getTransitionsFromStateToState(fromState, toState);
		FSATransition trans = transitions.toArray(new FSATransition[0])[0];
		return new SymbolString(trans.getInput());
	}

	private List<State> getStatesToCollapse() {
		List<State> toCollapse = new ArrayList<State>();
		toCollapse.addAll(getGTG().getStates());
		toCollapse.remove(getGTG().getStartState());
		if (hasSingleFinal()){
			toCollapse.removeAll(getGTG().getFinalStateSet());
		}
		return toCollapse;
	}

	private boolean addAllTransitionsToFinal() {
		boolean added = this.getGTG().getTransitions().addAll(newLambdaTransitions);

		if (added){
			for (FSATransition t: newLambdaTransitions){
				this.getGTG().getFinalStateSet().remove(t.getFromState());
			}
			newLambdaTransitions.clear();
		}

		return added;
	}
	
	public boolean addTransitionToFinal(State from, State to){
		GeneralizedTransitionGraph gtg = getGTG();
		boolean added = false;
		List<FSATransition> copy = new ArrayList<FSATransition>(newLambdaTransitions);
		
		for(FSATransition t : copy)
			if(t.getFromState().equals(from) && t.getToState().equals(to)){
				added = true;
				gtg.getTransitions().add(t);
				gtg.getFinalStateSet().remove(from);
				newLambdaTransitions.remove(t);
			}
		
		return added;
	}

	public boolean needsFinalTransitions() {
		return !newLambdaTransitions.isEmpty();
	}

	private boolean collapseAllTransitions() {
		if (myCollapseList.isEmpty()) return false;
		for (FSATransition trans : myCollapseList.toArray(new FSATransition[0]) ){
			this.collapseTransitionsOn(trans);
		}
		return true;
	}

	public boolean collapseTransitionsOn(FSATransition trans) {

		if (!shouldCollapse(trans)){
			return false;
		}

		removeCollapse(trans);

		TransitionSet<FSATransition> transSet = getGTG().getTransitions();
		Set<FSATransition> fromTo = 
				transSet.getTransitionsFromStateToState(trans.getFromState(), 
						trans.getToState());
		transSet.removeAll(fromTo);

		SymbolString regexLabel = new SymbolString();
		Symbol union = myRegEx.getOperators().getUnionOperator();
		for (FSATransition t: fromTo){
			if (isEmptySetTransition(t)) 
				continue;
			regexLabel.add(union);
			
			if (isLambdaTransition(t))
				regexLabel.add(myRegEx.getOperators().getEmptySub());
			regexLabel.addAll(t.getInput());
		}

		regexLabel = regexLabel.subList(1);

		FSATransition collapsed = new FSATransition(trans.getFromState(), 
				trans.getToState(),
				regexLabel);

		return transSet.add(collapsed);
	}

	public int numTransCollapsesNeeded() {
		return myCollapseList.size();
	}

	public int numEmptyNeeded() {
		return getEmptyTransitionsNeeded().size();
	}
	
	public int numStateCollapsesNeeded() {
		return myStatesToCollapse.size();
	}


	private boolean isLambdaTransition(FSATransition t) {
		return t.getInput().length == 0;
	}

	private void removeCollapse(FSATransition trans) {
		myCollapseList.remove(getCollapseTransition(trans));
	}

	private FSATransition getCollapseTransition(FSATransition trans) {
		for (FSATransition t: myCollapseList){
			if (t.getToState().equals(trans.getToState()) &&
					t.getFromState().equals(trans.getFromState())){
				return t;
			}
		}
		return null;
	}

	private boolean shouldCollapse(FSATransition trans) {
		return getCollapseTransition(trans) != null;
	}

	private class DoSingleFinalState implements AlgorithmStep{

		@Override
		public String getDescriptionName() {
			return "Make Single Final State";
		}

		@Override
		public String getDescription() {
			return null;
		}

		@Override
		public boolean execute() throws AlgorithmException {

			if (!hasSingleFinal() && getSingleFinal() == null)
				createSingleFinalState();

			return addAllTransitionsToFinal();
		}

		@Override
		public boolean isComplete() {
			return hasSingleFinal();
		}

	}

	private class CollapseTransitions implements AlgorithmStep{

		@Override
		public String getDescriptionName() {
			return "Collapse all transitions.";
		}

		@Override
		public String getDescription() {
			return "Transform multiple transitions with the same " +
					"from and to states into a single regular expression " +
					"transition.";
		}

		@Override
		public boolean execute() throws AlgorithmException {
			return collapseAllTransitions();
		}

		@Override
		public boolean isComplete() {
			return myCollapseList.isEmpty();
		}

	}

	private class CreateEmptyTransitions implements AlgorithmStep{


		@Override
		public String getDescriptionName() {
			return "Create empty set transitions.";
		}

		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean execute() throws AlgorithmException {
			return addAllEmptyTransitions();
		}

		@Override
		public boolean isComplete() {
			return getEmptyTransitionsNeeded().isEmpty();
		}

	}

	private class CollapseStates implements AlgorithmStep{

		@Override
		public String getDescriptionName() {
			return "Collapse all states";
		}

		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean execute() throws AlgorithmException {
			return collapseAllStates();
		}

		@Override
		public boolean isComplete() {
			return myStatesToCollapse.isEmpty();
		}

	}
}
