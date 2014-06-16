package view.lsystem.helperclasses;

/**
 * Special implementation of a String to allow for TransducerFactory to work
 * correctly (maps the proper transducer to the values of Parameters, but not
 * instances of String).
 * 
 * @author Ian McMahon
 * 
 */
public class ParameterValue {

	private String value;

	public ParameterValue(String s) {
		value = s;
	}
	
	public String toString(){
		return value;
	}

	public boolean isEmpty() {
		return value.length() == 0;
	}
	
}
