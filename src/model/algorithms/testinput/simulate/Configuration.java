package model.algorithms.testinput.simulate;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import debug.JFLAPDebug;
import util.Copyable;
import model.automata.Automaton;
import model.automata.State;
import model.automata.SingleInputTransition;
import model.automata.Transition;
import model.automata.TransitionSet;
import model.automata.acceptors.Acceptor;
import model.symbols.SymbolString;


public abstract class Configuration<S extends Automaton<T>, T extends Transition<T>> implements Copyable{

	private State myState;
	private T myTransitionTo;
	private int myPrimaryPosition;
	private int[] myPositions;
	private SymbolString myPrimary;
	private SymbolString[] myStrings;
	boolean amAccept;
	private LinkedList<T> myValidTransitons;
	private int myNumSecondary;
	private boolean amReject;
	private S myAutomaton;

	//TODO: THIS CLASS NEEDS SIGNIFICANT COMMENTS TO EXPLAIN HOW TO USE IT

	public Configuration(S automaton, State s, int ppos, SymbolString primary, 
			int[] opos, SymbolString... strings){
		myAutomaton = automaton;
		myPrimaryPosition = ppos;
		myPositions = opos;
		myNumSecondary = strings.length;
		amAccept = amReject = false;
		myPrimary = primary;
		myStrings = strings;
		this.setState(s);

	}

	private LinkedList<T> findValidTransitions() {
		LinkedList<T> valid = new LinkedList<T>();
		TransitionSet<T>  transitions = this.getAutomaton().getTransitions();
		if (!this.shouldFindValidTransitions()) 
			return valid;
		for (T trans: transitions.getTransitionsFromState(this.getState())){
			if (this.canMoveAlongTransition(trans)){
				valid.add(trans);
			}
		}
		return valid;
	}

//	private void setToConfig(Configuration setTo) {
//		myPrimaryPosition = setTo.getPrimaryPosition();
//		myPositions = setTo.myPositions;
//		myPrimary = setTo.getPrimaryString();
//		myStrings = setTo.myStrings;
//	}

	protected boolean shouldFindValidTransitions() {
		return !this.isDone();
	}

	public S getAutomaton() {

		return myAutomaton;

	}

	protected boolean hasNextState(){
		return !myValidTransitons.isEmpty();
	}

	public LinkedList<Configuration<S,T>> getNextConfigurations() {
		LinkedList<Configuration<S,T>> configs = new LinkedList<Configuration<S,T>>();

		if (!this.hasNextState()&&
				!(this.isAccept() || this.isReject())){
			Configuration<S, T> clone = this.copy();
			configs.add(clone);
			clone.updateAccept();
			clone.updateReject();

		}
		else {
			for (T trans : myValidTransitons){
				Configuration c = createNextConfiguration(trans);
				configs.add(c);
				c.setTransitionTo(trans);
				c.updateAccept(); 
				
			}
		}
		

		return configs;
	}

	private boolean checkAccept() {
		return !this.hasNextState() && this.isDone() && this.isInFinalState();
	}

	protected boolean isInFinalState(){
		S auto = this.getAutomaton();
		if (!(auto instanceof Acceptor)) return true;
		return Acceptor.isFinalState((Acceptor) auto, this.getState());
	}

	private Configuration<S,T> createNextConfiguration(T trans) {
		State s = trans.getToState();
		int ppos = this.getNextPrimaryPosition(trans);
		int[] position = this.getNextSecondaryPositions(trans);
		SymbolString[] clones = new SymbolString[myStrings.length];
		for (int i = 0; i < clones.length; i++)
			clones[i] = myStrings[i].copy();
		clones = this.assembleUpdatedStrings(clones, trans);
		
		try {
			return createConfig( this.getAutomaton(), s, ppos, myPrimary, position, clones);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error creating next Config of class " + this.getClass());
		}

	}

	@Override
	public Configuration<S,T> copy(){
		LinkedList<SymbolString> clones = new LinkedList<SymbolString>();
		for (SymbolString string : myStrings){
			clones.add(string.copy());
		}
		try {
			Configuration<S,T> config = this.createConfig(this.getAutomaton(),
					myState, 
					myPrimaryPosition, 
					usingPrimary() ? myPrimary.copy(): null, 
					myPositions, 
					clones.toArray(new SymbolString[0]));
			config.setTransitionTo(this.getTransitionTo());
			return config;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error creating next Config of class " + this.getClass());
		}

	}

	private void setTransitionTo(T transitionTo) {
		myTransitionTo = transitionTo;
	}

	public T getTransitionTo(){
		return myTransitionTo;
	}

	private int[] getNextSecondaryPositions(T trans) {

		int[] positions = new int[this.getNumOfSecondary()];
		for (int i = 0; i < positions.length; i++){
			positions[i] = getNextSecondaryPosition(i, trans);
		}

		return positions;
	}

	public int getNumOfSecondary() {
		return myNumSecondary;
	}

	public State getState() {
		return myState;
	}

	public int getPrimaryPosition(){
		return myPrimaryPosition;
	}

	public SymbolString getPrimaryString(){
		return myPrimary;
	}

	public boolean usingPrimary(){
		return myPrimary != null;
	}

	public SymbolString getStringForIndex(int i){
		return myStrings[i];
	}

	public int getPositionForIndex(int i) {
		return myPositions[i];
	}


	protected int getSpecialCase(){
		return SingleInputSimulator.DEFAULT;
	}


	public boolean isReject(){
		return amReject;
	}


	/**
	 * Returns if this chain has been put into an accept state.
	 * DO NOT OVERRIDE THIS IN SUBCLASSES. Instead override <CODE>checkAccept()</CODE>
	 * @return
	 */
	public boolean isAccept(){
		return amAccept;
	}


	private void updateAccept(){
		amAccept = checkAccept();
	}

	private void updateReject(){
		amReject = !this.hasNextState() && !this.isAccept();
	}

	@Override
	public String toString() {

		String s = this.getName() + "\n" +
				"\t" + "State: " + this.getState().toString() + "\n";
		if(this.getPrimaryString()!=null){
				s+= "\t" + this.getPrimaryPresentationName() + ": " + getPrimaryString().toString() + "\n" +
				"\t" + "Primary Position: " + this.getPrimaryPosition() + "\n";
		}
		for (int i = 0; i < myStrings.length; i++){
			s += "\t" + this.getStringPresentationName(i) + ": " + myStrings[i] + "\n";
			s += "\t" + "Position: " + this.getPositionForIndex(i) + "\n";
		
		}
		return s;
	}


	public void setState(State s){
		myState = s;
		myValidTransitons = this.findValidTransitions();
	}

	protected abstract String getPrimaryPresentationName();

	protected abstract int getNextPrimaryPosition(T label);

	protected abstract Configuration<S, T> createConfig(S a, State s, int ppos, SymbolString primary, 
			int[] positions, SymbolString[] updatedClones) throws Exception;

	protected abstract boolean isDone();

	public abstract String getName();

	protected abstract String getStringPresentationName(int i);

	protected abstract boolean canMoveAlongTransition(T trans);

	protected abstract int getNextSecondaryPosition(int i, T trans);

	protected abstract SymbolString[] assembleUpdatedStrings(SymbolString[] clones, T trans);


}
