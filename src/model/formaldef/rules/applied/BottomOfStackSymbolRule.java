package model.formaldef.rules.applied;

import errors.BooleanWrapper;
import model.automata.acceptors.pda.BottomOfStackSymbol;
import model.automata.acceptors.pda.StackAlphabet;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.rules.AlphabetRule;
import model.symbols.Symbol;

public class BottomOfStackSymbolRule extends PermanentSpecialSymbolRule<StackAlphabet, BottomOfStackSymbol> {


	public BottomOfStackSymbolRule(BottomOfStackSymbol bottom) {
		super(bottom);
	}

	@Override
	public String getDescriptionName() {
		return "Bottom of Stack Symbol Rule";
	}

	@Override
	public String getDescription() {
		return "Ensures that, if there is a bottom of stack symbol, it cannot be" +
				"removed from the Stack Alphabet.";
	}

}
