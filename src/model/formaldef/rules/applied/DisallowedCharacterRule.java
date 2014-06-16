package model.formaldef.rules.applied;

import util.UtilFunctions;
import model.formaldef.FormalDefinition;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.rules.FormalDefinitionUsingRule;
import model.symbols.Symbol;
import errors.BooleanWrapper;

public class DisallowedCharacterRule extends FormalDefinitionUsingRule<Alphabet, FormalDefinition> {

	public DisallowedCharacterRule(FormalDefinition fd) {
		super(fd);
	}

	@Override
	public String getDescriptionName() {
		return "Disallowed Character Rule";
	}

	@Override
	public String getDescription() {
		
		String chars = this.getAssociatedDefiniton().getDisallowedCharacters().toString();
		
		return "Prevents the characters disallowed by " + 
						this.getAssociatedDefiniton().getDescriptionName() + ": " +
						chars + " from being in any symbol in this Alphabet." ;
	}


	@Override
	public BooleanWrapper canModify(Alphabet a, Symbol oldSymbol,
			Symbol newSymbol) {
		return canAdd(a, newSymbol);
	}

	@Override
	public BooleanWrapper canRemove(Alphabet a, Symbol oldSymbol) {
		return new BooleanWrapper(true);
	}

	@Override
	public BooleanWrapper canAdd(Alphabet a, Symbol newSymbol) {
		for (Character c: this.getAssociatedDefiniton().getDisallowedCharacters()){
			if (newSymbol.containsCharacters(c))
				return new BooleanWrapper(false, "The character " + c + " is disallowed for this " + a.getDescriptionName() +
						". For more information on allowability rules, " +
						            "go to the Rules option in the Help Menu.");
		}
		return new BooleanWrapper(true);
	}

}
