package model.formaldef.rules;

import errors.BooleanWrapper;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.alphabets.grouping.GroupingPair;
import model.symbols.Symbol;




public abstract class GroupingRule<T extends Alphabet> extends AlphabetRule<T> {

	private GroupingPair myGrouping;
	
	public GroupingRule(GroupingPair gp){
		myGrouping = gp;
	}

	public Character getOpenGroup() {
		return myGrouping.getOpenGroup();
	}
	
	public Character getCloseGroup() {
		return myGrouping.getCloseGroup();
	}

	protected GroupingPair getGroupingPair() {
		return myGrouping;
	}
	
	@Override
	public BooleanWrapper canRemove(T a, Symbol oldSymbol) {
		return new BooleanWrapper(true);
	};

}
