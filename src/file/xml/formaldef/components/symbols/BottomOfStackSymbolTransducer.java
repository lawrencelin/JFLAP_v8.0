package file.xml.formaldef.components.symbols;

import model.automata.acceptors.pda.BottomOfStackSymbol;
import file.xml.formaldef.components.SpecialSymbolTransducer;

public class BottomOfStackSymbolTransducer 
						extends SpecialSymbolTransducer<BottomOfStackSymbol> {

	@Override
	public String getTag() {
		return BOSS_TAG;
	}

	@Override
	public BottomOfStackSymbol createInstance(String s) {
		return new BottomOfStackSymbol(s);
	}

}
