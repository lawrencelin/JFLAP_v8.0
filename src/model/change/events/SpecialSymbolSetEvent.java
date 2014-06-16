package model.change.events;

import model.symbols.SpecialSymbol;
import model.symbols.Symbol;

public class SpecialSymbolSetEvent extends IndividualComponentChange<SpecialSymbol, Symbol> {

	public SpecialSymbolSetEvent(SpecialSymbol source, Symbol from, Symbol to) {
		super(source, from, to);
	}

	@Override
	public boolean undo() {
		return this.getSource().setSymbol(this.getFrom());
	}

	@Override
	public boolean redo() {
		return this.getSource().setSymbol(this.getTo());
	}


}
