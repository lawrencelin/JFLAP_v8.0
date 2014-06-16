package model.formaldef.rules.applied;

import model.automata.InputAlphabet;
import model.automata.turing.TapeAlphabet;
import model.automata.turing.TuringMachine;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.rules.FormalDefinitionUsingRule;
import model.symbols.Symbol;
import errors.BooleanWrapper;

public class TuringMachineRule extends FormalDefinitionUsingRule<InputAlphabet, TuringMachine> {

	public TuringMachineRule(TuringMachine fd) {
		super(fd);
	}

	@Override
	public String getDescriptionName() {
		return "Turing Machine Rule";
	}

	@Override
	public String getDescription() {
		return "A symbol can be added to the InputAlphabet  if and only if " +
				"it is already present in the Tape Alphabet. This is because the " +
				"Input Alphabet is a subset of the Tape Alphabet.";
	}

	@Override
	public BooleanWrapper canModify(InputAlphabet a, Symbol oldSymbol,
			Symbol newSymbol) {
		return this.canAdd(a, newSymbol);
	}

	@Override
	public BooleanWrapper canRemove(InputAlphabet a, Symbol oldSymbol) {
		return new BooleanWrapper(true);
	}

	@Override
	public BooleanWrapper canAdd(InputAlphabet a, Symbol newSymbol) {
		TapeAlphabet tapeAlph = this.getAssociatedDefiniton().getTapeAlphabet();
		boolean valid = tapeAlph.contains(newSymbol);
		return new BooleanWrapper(valid, "A symbol can be added to the " + a.getDescriptionName() + " if and only if " +
				"it is already present in the " + tapeAlph.getDescriptionName() + ".");
	}


}
