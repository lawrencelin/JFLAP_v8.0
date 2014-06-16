package oldnewstuff.model.change.rules.applied;

import oldnewstuff.model.change.rules.FormalDefinitionUsingRule;
import oldnewstuff.model.change.rules.SetComponentRule;
import util.UtilFunctions;
import model.change.events.SetComponentEvent;
import model.change.events.SetComponentModifyEvent;
import model.formaldef.FormalDefinition;
import model.formaldef.components.alphabets.Alphabet;
import model.symbols.Symbol;
import errors.BooleanWrapper;

public class DisallowedCharacterRule extends SetComponentRule<Symbol>{


	private FormalDefinition myDefinition;

	public DisallowedCharacterRule(int type, FormalDefinition fd) {
		super(type);
		myDefinition = fd;
	}

	@Override
	public String getDescriptionName() {
		return "Disallowed Character Rule";
	}

	@Override
	public String getDescription() {

		String chars = myDefinition.getDisallowedCharacters().toString();

		return "Prevents the characters disallowed by " + 
		myDefinition.getDescriptionName() + ": " +
		chars + " from being in any symbol in this Alphabet." ;
	}

	@Override
	public BooleanWrapper checkRemove(SetComponentEvent<Symbol> event) {
		return new BooleanWrapper(true);
	}

	@Override
	public BooleanWrapper checkAdd(SetComponentEvent<Symbol> event) {
		return checkSymbols(event.getItems().toArray(new Symbol[0]));
	}

	private BooleanWrapper checkSymbols(Symbol ... items) {
		for (Symbol item: items){
			for (Character c: myDefinition.getDisallowedCharacters()){
				if (item.containsCharacters(c))
					return new BooleanWrapper(false, "The character " + c + " is disallowed for this " + myDefinition.getDescriptionName() +
							". For more information on allowability rules, " +
							"go to the Rules option in the Help Menu.");
			}
		}
		return new BooleanWrapper(true);
	}

	@Override
	public BooleanWrapper checkModify(SetComponentModifyEvent<Symbol> event) {
		return checkSymbols(event.getModifiedItem());
	}

}
