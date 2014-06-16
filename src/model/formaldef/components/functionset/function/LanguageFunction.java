package model.formaldef.components.functionset.function;

import java.util.List;
import java.util.Set;

import javax.swing.event.ChangeEvent;

import debug.JFLAPDebug;
import util.Copyable;
import model.change.events.SetToEvent;
import model.formaldef.Describable;
import model.formaldef.UsesSymbols;
import model.formaldef.components.SetSubComponent;
import model.formaldef.components.alphabets.Alphabet;
import model.symbols.Symbol;

public abstract class LanguageFunction<T extends LanguageFunction<T>> extends SetSubComponent<T>
																		implements UsesSymbols{


	@Override
	public boolean applySymbolMod(String from, String to) {
		List<Symbol> fromList = this.getAllSymbols();
		for (Symbol s: fromList){
			if (s.getString().equals(from))
				s.setString(to);
		}
		distributeChanged();
		return !fromList.isEmpty();
	}

	public abstract List<Symbol> getAllSymbols();


}
