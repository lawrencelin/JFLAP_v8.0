package model.grammar;

import model.formaldef.FormalDefinitionException;

public class GrammarException extends FormalDefinitionException {

	public GrammarException() {
		super();
	}

	public GrammarException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public GrammarException(String arg0) {
		super(arg0);
	}

	public GrammarException(Throwable arg0) {
		super(arg0);
	}

}
