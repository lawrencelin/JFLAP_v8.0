package file.xml.formaldef.components.alphabet;

import model.automata.turing.TapeAlphabet;
import model.formaldef.components.SetComponent;
import model.symbols.Symbol;

public class TapeAlphabetTransducer extends AlphabetTransducer {

	@Override
	public String getTag() {
		return TAPE_ALPH;
	}

	@Override
	public SetComponent<Symbol> createEmptyComponent() {
		return new TapeAlphabet();
	}

}
