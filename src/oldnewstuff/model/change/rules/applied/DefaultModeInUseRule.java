package oldnewstuff.model.change.rules.applied;

import java.util.Arrays;
import java.util.Set;

import oldnewstuff.model.change.rules.SetComponentRule;

import debug.JFLAPDebug;

import errors.BooleanWrapper;
import model.change.events.SetComponentEvent;
import model.change.events.SetComponentModifyEvent;
import model.formaldef.FormalDefinition;
import model.formaldef.UsesSymbols;
import model.formaldef.components.alphabets.Alphabet;
import model.symbols.Symbol;

public class DefaultModeInUseRule extends SetComponentRule<Symbol> {

	private FormalDefinition myDefinition;
	private Alphabet mySource;

	public DefaultModeInUseRule(int type, Alphabet source, FormalDefinition fd) {
		super(type);
		mySource = source;
		myDefinition = fd;
	}

	@Override
	public String getDescriptionName() {
		return "Symbol in Use Rule";
	}

	@Override
	public String getDescription() {
		return "Check if a symbol is already in used before adding it to the " +
				"target alphabet.";
	}

	@Override
	public BooleanWrapper checkRemove(SetComponentEvent<Symbol> event) {
		Set<Symbol> inUse = myDefinition.getSymbolsUsedForAlphabet(a);
		inUse.retainAll(event.getItems());
		return new BooleanWrapper(inUse.isEmpty(), 
									"In Default Mode, you may not remove symbols that" +
									" are already in use.");
	}

	@Override
	public BooleanWrapper checkAdd(SetComponentEvent<Symbol> event) {
		Set<? extends Symbol> inUse = myDefinition.getUniqueSymbolsUsed(mySource);
		boolean allUsed = inUse.containsAll(event.getItems());
		return new BooleanWrapper(allUsed, "Can only add a symbol to an alphabet if is " +
				"already in used somewhere. ");
	}

	@Override
	public BooleanWrapper checkModify(SetComponentModifyEvent<Symbol> event) {
		return new BooleanWrapper(false, "You may not modify symbols in Default Mode.");
	}

}
