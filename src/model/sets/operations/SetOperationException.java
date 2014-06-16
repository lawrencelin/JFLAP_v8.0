package model.sets.operations;

import errors.JFLAPException;

@SuppressWarnings("serial")
public class SetOperationException extends JFLAPException {
	
	public SetOperationException() {
		super();
	}

	public SetOperationException(String arg0) {
		super(arg0);
	}
	
	public SetOperationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
	
	public SetOperationException(Throwable arg1) {
		super(arg1);
	}
}
