package file.xml.formaldef.components.alphabet;

import model.formaldef.components.SetComponent;
import model.grammar.VariableAlphabet;
import model.symbols.Symbol;

public class VariablesTransducer extends AlphabetTransducer {


	@Override
	public String getTag() {
		return VARIABLES_TAG;
	}

	@Override
	public SetComponent<Symbol> createEmptyComponent() {
		return new VariableAlphabet();
	}

}
