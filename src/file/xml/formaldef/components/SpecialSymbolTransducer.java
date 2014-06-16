package file.xml.formaldef.components;

import model.symbols.SpecialSymbol;

public abstract class SpecialSymbolTransducer<T extends SpecialSymbol>
													extends SingleValueTransducer<T> {

	@Override
	public Object retrieveData(T structure) {
		return structure.getSymbol();
	}

	
}
