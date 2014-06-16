package file.xml.formaldef.components.alphabet;

import model.automata.transducers.OutputAlphabet;
import model.formaldef.components.SetComponent;
import model.symbols.Symbol;

public class OutputAlphabetTransducer extends AlphabetTransducer {


	@Override
	public String getTag() {
		return OUTPUT_ALPH_TAG;
	}

	@Override
	public SetComponent<Symbol> createEmptyComponent() {
		return new OutputAlphabet();
	}

}
