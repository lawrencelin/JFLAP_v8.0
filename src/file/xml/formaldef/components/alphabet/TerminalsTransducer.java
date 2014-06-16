package file.xml.formaldef.components.alphabet;

import model.formaldef.components.SetComponent;
import model.grammar.TerminalAlphabet;
import model.symbols.Symbol;

public class TerminalsTransducer extends AlphabetTransducer {


	@Override
	public String getTag() {
		return TERMINALS_TAG;
	}

	@Override
	public SetComponent<Symbol> createEmptyComponent() {
		return new TerminalAlphabet();
	}

}
