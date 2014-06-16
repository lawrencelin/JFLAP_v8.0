package file.xml.formaldef.components.symbols;

import model.grammar.StartVariable;
import file.xml.formaldef.components.SpecialSymbolTransducer;

public class StartVariableTransducer extends SpecialSymbolTransducer<StartVariable>{

	@Override
	public String getTag() {
		return START_VAR_TAG;
	}

	@Override
	public StartVariable createInstance(String s) {
		return new StartVariable(s);
	}

}
