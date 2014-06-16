package model.formaldef;

public interface Describable {

	/**
	 * The full string name for this component. For example,
	 * the variable alphabet in a grammar would return the 
	 * {@link String} "Variable Alphabet"
	 * 
	 * @return {@link String} name of this component
	 */
	public abstract String getDescriptionName();

	/**
	 * Provides a simple text description of this component,
	 * can be used for tooltips, printouts, help with understanding
	 * what a given component represents, etc.
	 * 
	 * @return a {@link String} description of this component.
	 */
	public abstract String getDescription();

}