package view.lsystem.helperclasses;

import java.util.Map;
import java.util.TreeMap;

/**
 * Special implementation of a TreeMap<String, String> to allow for
 * TransducerFactory to work correctly (maps the proper transducer to the
 * ParameterMape, but not other instances of Map<String, String>).
 * 
 * @author Ian McMahon
 * 
 */
public class ParameterMap extends TreeMap<String, String> {

	public ParameterMap() {
		super();
	}

	public ParameterMap(Map<String, String> map) {
		super(map);
	}

	/**
	 * Helper function to allow for quick insertion of
	 * <code>ParameterName</code>, <code>ParameterValue</code> pairs.
	 * 
	 * @param name
	 * @param value
	 */
	public void put(ParameterName name, ParameterValue value) {
		if (name == null || value == null)
			return;
		put(name.toString(), value.toString());
	}
}
