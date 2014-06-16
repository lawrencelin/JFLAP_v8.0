package model.algorithms;

import errors.BooleanWrapper;
import errors.JFLAPException;

public class AlgorithmException extends JFLAPException {

	public AlgorithmException() {
		super();
	}

	public AlgorithmException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public AlgorithmException(String arg0) {
		super(arg0);
	}

	public AlgorithmException(Throwable arg0) {
		super(arg0);
	}

	public AlgorithmException(BooleanWrapper ... bw) {
		super(BooleanWrapper.createErrorLog(bw));
	}

}
