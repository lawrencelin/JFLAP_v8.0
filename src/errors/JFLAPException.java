package errors;

import model.algorithms.AlgorithmException;
import model.formaldef.FormalDefinition;

/**
 * This class is designed as a global exception to be
 * thrown in any situation where the error should be
 * caught and expressed as an actual JFLAP error message,
 * i.e. the program should continue to run once the
 * error has resolved.
 * 
 * Should be implemented by different subclasses with 
 * module-specific descriptive names.
 * 
 * @See {@link AlgorithmException}
 * @See FormalDefinitionException
 * 
 * @author Julian
 *
 */
public abstract class JFLAPException extends RuntimeException {

	public JFLAPException() {
		super();
	}

	public JFLAPException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public JFLAPException(String arg0) {
		super(arg0);
	}

	public JFLAPException(Throwable arg0) {
		super(arg0);
	}

	
	
}
