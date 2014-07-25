package model.automata;

import util.UtilFunctions;

public abstract class Transition<T extends Transition<T>> extends
		AutomatonFunction<T> {

	/** The states this transition goes between. */
	public State myFrom;
	/** The states this transition goes between. */
	public State myTo;

	public Transition(State from, State to) {
		myFrom = from;
		myTo = to;
	}

	/**
	 * Returns the state this transition eminates from.
	 * 
	 * @return the state this transition eminates from
	 */
	public State getFromState() {
		return this.myFrom;
	}

	/**
	 * Returns the state this transition travels to.
	 * 
	 * @return the state this transition travels to
	 */
	public State getToState() {
		return this.myTo;
	}

	/**
	 * Sets the state the transition starts at.
	 * 
	 * @param newFrom
	 *            the state the transition starts at
	 */
	public void setFromState(State newFrom) {
		this.myFrom = newFrom;
	}

	/**
	 * Sets the state the transition goes to.
	 * 
	 * @param newTo
	 *            the state the transition goes to
	 */
	public void setToState(State newTo) {
		this.myTo = newTo;
	}

	/**
	 * Checks if this transition is a loop, i.e. if its from state is the same
	 * as its to state
	 * 
	 * @return
	 */
	public boolean isLoop() {
		return this.myTo.equals(myFrom);
	}

	public abstract String getLabelText();
	
	public abstract T copy(State from, State to);

	/**
	 * Checks to see if this transition has a lambda input component. subclasses
	 * should override this method if the definition of a lambda transition is
	 * different, specifically in the case of closure.
	 * 
	 * @return
	 */
	public abstract boolean isLambdaTransition();

	/**
	 * Returns a string representation of this object. The string returned is
	 * the string representation of the first state, and the string
	 * representation of the second state.
	 * 
	 * @return a string representation of this object
	 */
	@Override
	public String toString() {
		return this.getFromState().getName() + "---" + this.getLabelText()
				+ "--->" + this.getToState().getName();
	}

	/**
	 * Returns if this transition equals another object.
	 * 
	 * @param object
	 *            the object to test against
	 * @return <CODE>true</CODE> if the two are equal, <CODE>false</CODE>
	 *         otherwise
	 */
	@Override
	public boolean equals(Object o) {
		if(o == null) return false;
		if(o.getClass().equals(this.getClass()))
			return this.compareTo((T) o) == 0;
		return false;
	}

	public int compareTo(T o) {
		return UtilFunctions.metaCompare(new Comparable[] {
				this.getFromState(), this.getToState() },
				new Comparable[] { o.getFromState(), o.getToState() });
	};

	/**
	 * Returns the hash code for this transition.
	 * 
	 * @return the hash code for this transition
	 */
	@Override
	public int hashCode() {
		return myFrom.hashCode() ^ myTo.hashCode();
	}
	
	@Override
	protected void applySetTo(T other) {
		this.myFrom = other.myFrom;
		this.myTo = other.myTo;
	};

}