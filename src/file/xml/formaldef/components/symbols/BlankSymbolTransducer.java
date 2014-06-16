package file.xml.formaldef.components.symbols;

import model.automata.turing.BlankSymbol;
import file.xml.formaldef.components.SpecialSymbolTransducer;

public class BlankSymbolTransducer extends SpecialSymbolTransducer<BlankSymbol> {

	@Override
	public String getTag() {
		return TM_BLANK_TAG;
	}

	@Override
	public BlankSymbol createInstance(String s) {
		return new BlankSymbol();
	}

}
