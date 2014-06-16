package util;

public interface Copyable {

	/**
	 * Creates a copy of this object. This is used to
	 * enforce a copy/clone method and circumvent
	 * the terrors of Object.clone().
	 * @return
	 */
	public Object copy();
	
}
