package file.xml.formaldef.components.alphabet;

import model.automata.acceptors.pda.StackAlphabet;
import model.formaldef.components.SetComponent;
import model.symbols.Symbol;

public class StackAlphabetTransducer extends AlphabetTransducer {

	@Override
	public String getTag() {
		return STACK_ALPH;
	}

	@Override
	public SetComponent<Symbol> createEmptyComponent() {
		return new StackAlphabet();
	}

}
