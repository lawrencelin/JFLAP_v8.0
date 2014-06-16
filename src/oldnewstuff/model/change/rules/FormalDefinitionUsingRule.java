package oldnewstuff.model.change.rules;

import model.formaldef.FormalDefinition;
import model.formaldef.components.alphabets.Alphabet;

public abstract class FormalDefinitionUsingRule<T extends Alphabet, S extends FormalDefinition> extends FormalDefinitionRule<T> {

	
	private S myFormalDefinition;
	
	public FormalDefinitionUsingRule(S fd){
		myFormalDefinition = fd;
	}
	
	public S getAssociatedDefiniton(){
		return myFormalDefinition;
	}
	
	
}
