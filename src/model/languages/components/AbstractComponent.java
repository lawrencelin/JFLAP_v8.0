package model.languages.components;

import java.util.Collection;

import model.symbols.Symbol;
import model.symbols.SymbolString;

public abstract class AbstractComponent {
	
	
	public abstract Collection<Symbol> getSymbolsUsed ();
	
	
	public abstract SymbolString deriveString ();
	
	

}
