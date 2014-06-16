package file.xml.formaldef.components.alphabet;

import model.automata.InputAlphabet;
import model.formaldef.components.SetComponent;
import model.symbols.Symbol;

public class InputAlphabetTransducer extends AlphabetTransducer{


	@Override
	public InputAlphabet createEmptyComponent() {
		return new InputAlphabet();
	}

	@Override
	public String getTag() {
		return INPUT_ALPH;
	}


}
