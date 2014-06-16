package model.automata;

import java.lang.reflect.Constructor;
import java.util.Collection;

import model.formaldef.components.SetSubComponent;

import util.Copyable;
import util.JFLAPConstants;




public class State extends SetSubComponent<State> implements JFLAPConstants {

	private int myID;

	private String myName;

	public State(String name, int id){
		myName= name;
		myID = id;
	}



	/**
	 * Sets the name for this state. A parameter of <CODE>null</CODE> will
	 * reset this to the default.
	 * 
	 * @param name
	 *            the new name for the state
	 */
	public void setName(String name) {
		setTo(new State(name, myID));
	}

	/**
	 * Returns the simple "name" for this state.
	 * 
	 * @return the name for this state
	 */
	public String getName() {
		return myName;
	}


	public int getID(){
		return myID;
	}


	@Override 
	public State copy(){
		return new State(this.getName(), this.getID());
	}


	@Override
	public String toString() {
		return this.getName();
	}

	public String toDetailedString() {
		return this.getName() + "|id:" + this.getID();
	}

	@Override
	public boolean equals(Object o){
		if(o instanceof State)
			return this.compareTo((State) o) == 0;
		return false;
	}

	@Override
	public int compareTo(State toState) {
		int compare = this.myID - toState.myID;
		if (compare == 0)
			compare = this.myName.compareTo(toState.myName);
		return compare;
	}

	@Override
	public int hashCode() {
		return (int) (Math.pow(2, this.getID()));
	}



	@Override
	public String getDescriptionName() {
		return "State";
	}



	@Override
	public String getDescription() {
		return "A State in an automaton!";
	}



	@Override
	protected void applySetTo(State other) {
		// this will only set the name. that is ALL. ID's are immutable.
		this.myName = other.myName;
	}





}
