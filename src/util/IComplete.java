package util;

import errors.BooleanWrapper;


/**
 * Gives a class the ability to gauge completeness
 * @author Julian
 *
 */
public interface IComplete {

	/**
	 * Check if this object is complete.
	 * @return Boolean wrapper true if complete
	 * 		BooleanWrapper false if incomplete + informative message
	 */
	public BooleanWrapper isComplete();
	
}
