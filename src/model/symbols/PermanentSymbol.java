package model.symbols;

import java.io.ObjectInputStream.GetField;

public class PermanentSymbol extends Symbol{

	public PermanentSymbol(String s) {
		super(s);
	}
	
	@Override
	public boolean setTo(Symbol s) {
		throw new RuntimeException("A permanent symbol may not have its value changed.");
	}
	
}
