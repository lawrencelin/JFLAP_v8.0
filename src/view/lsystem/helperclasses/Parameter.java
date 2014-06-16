package view.lsystem.helperclasses;

/**
 * Special implementation of a <String, String> key value pair to allow for TransducerFactory to
 * work correctly (maps the proper transducer to each parameter, but not anything else).
 * 
 * @author Ian McMahon
 * 
 */
public class Parameter {
	private ParameterName name;
	private ParameterValue value;
	
	public Parameter(ParameterName name, ParameterValue value){
		this.name = name;
		this.value = value;
	}

	public Parameter(String name, String value) {
		this(new ParameterName(name), new ParameterValue(value));
	}

	public ParameterName getName(){
		return name;
	}
	
	public ParameterValue getValue(){
		return value;
	}
	
	public String toString(){
		return name.toString() + ": " + value.toString();
	}
	
	public boolean isEmpty(){
		return name.isEmpty() && value.isEmpty();
	}

	public Object[] toArray() {
		return new Object[]{name.toString(), value.toString()};
	}

	public boolean setTo(Parameter to) {
		if (to.name == null || to.value == null)
			return false;
		this.name = to.name;
		this.value = to.value;
		return true;
	}

	public Parameter copy() {
		return new Parameter(name, value);
	}
}
