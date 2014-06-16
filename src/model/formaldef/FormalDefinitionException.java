package model.formaldef;

import errors.JFLAPException;

/**
 * An Exception designed to be used with anything
 * associated with a Formal definition and the handling thereof.
 * @author Julian
 *
 */
public class FormalDefinitionException extends JFLAPException {

	public FormalDefinitionException() {
		super();
	}

	public FormalDefinitionException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public FormalDefinitionException(String arg0) {
		super(arg0);
	}

	public FormalDefinitionException(Throwable arg0) {
		super(arg0);
	}

}
