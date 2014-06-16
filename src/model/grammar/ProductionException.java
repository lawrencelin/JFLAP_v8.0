package model.grammar;

import errors.JFLAPException;

public class ProductionException extends JFLAPException {

	public ProductionException() {
		super();
	}

	public ProductionException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public ProductionException(String arg0) {
		super(arg0);
	}

	public ProductionException(Throwable arg0) {
		super(arg0);
	}

}
