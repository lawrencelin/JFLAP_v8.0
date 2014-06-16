package model.grammar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.alphabets.Alphabet;
import model.symbols.Symbol;




public class TerminalAlphabet extends Alphabet {


	@Override
	public String getDescriptionName() {
		return "Terminals";
	}

	@Override
	public Character getCharacterAbbr() {
		return 'T';
	}

	@Override
	public String getDescription() {
		return "The terminal alphabet.";
	}

	@Override
	public String getSymbolName() {
		return "Terminal";
	}

	@Override
	public boolean addAll(Collection<? extends Symbol> e) {
		for (Symbol s: e.toArray(new Symbol[0])){
			if (!(s instanceof Terminal)){
				e.remove(s);
				((Collection<Symbol>) e).add(new Terminal(s.getString()));
			}
		}
		return super.addAll(e);
	}


	@Override
	public TerminalAlphabet copy() {
		return (TerminalAlphabet) super.copy();
	}

}
