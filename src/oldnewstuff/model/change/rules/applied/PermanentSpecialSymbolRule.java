package oldnewstuff.model.change.rules.applied;

import oldnewstuff.model.change.rules.FormalDefinitionRule;
import errors.BooleanWrapper;
import model.automata.acceptors.pda.StackAlphabet;
import model.formaldef.components.alphabets.Alphabet;
import model.symbols.SpecialSymbol;
import model.symbols.Symbol;

public abstract class PermanentSpecialSymbolRule<T extends Alphabet, S extends SpecialSymbol> extends FormalDefinitionRule<T> {

	
	private S mySpecial;

	public PermanentSpecialSymbolRule(S specialSymbol){
		mySpecial = specialSymbol;
	}
	
	public S getSpecialSymbol(){
		return mySpecial;
	}
	
	private boolean checkDifferentFromSpecial(Symbol oldSymbol) {
		return this.getSpecialSymbol().isComplete().isError() || 
				!this.getSpecialSymbol().getSymbol().equals(oldSymbol);
	}

	@Override
	public BooleanWrapper canModify(T a, Symbol oldSymbol,
			Symbol newSymbol) {
		return new BooleanWrapper(checkDifferentFromSpecial(oldSymbol), "You may not modify the " 
																	+ this.getSpecialSymbol().getDescriptionName());
	}

	
	@Override
	public BooleanWrapper canRemove(T a, Symbol oldSymbol) {
		return new BooleanWrapper(checkDifferentFromSpecial(oldSymbol), "You may not remove the " +
															this.getSpecialSymbol().getDescriptionName() +
																		" from the " + a.getDescriptionName());
	}

	@Override
	public BooleanWrapper canAdd(T a, Symbol newSymbol) {
		return new BooleanWrapper(true);
	}



}
