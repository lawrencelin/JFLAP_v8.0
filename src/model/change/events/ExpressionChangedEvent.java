package model.change.events;

import model.regex.ExpressionComponent;
import model.symbols.SymbolString;

public class ExpressionChangedEvent extends IndividualComponentChange<ExpressionComponent, SymbolString> {

	public ExpressionChangedEvent(ExpressionComponent source,
			SymbolString from, SymbolString to) {
		super(source, from, to);
	}

	@Override
	public boolean undo() {
		return getSource().setTo(getFrom());
	}

	@Override
	public boolean redo() {
		return getSource().setTo(getTo());
	}

}
