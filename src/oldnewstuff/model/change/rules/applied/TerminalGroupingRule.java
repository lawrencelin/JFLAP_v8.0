package oldnewstuff.model.change.rules.applied;

import oldnewstuff.model.change.rules.GroupingRule;
import model.change.events.SetComponentEvent;
import model.change.events.SetComponentModifyEvent;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.alphabets.grouping.GroupingPair;
import model.grammar.TerminalAlphabet;
import model.symbols.Symbol;
import errors.BooleanWrapper;

public class TerminalGroupingRule extends GroupingRule {

	public TerminalGroupingRule(int type, GroupingPair gp) {
		super(type, gp);
	}

	@Override
	public String getDescriptionName() {
		return "Restrictions on Terminals in Grouping case";
	}

	@Override
	public String getDescription() {
		return "This rule prevents any symbols from being added to the terminal alphabet if " +
				"they contain any characters used for group in the Variable Alphabet.";
	}

	@Override
	public BooleanWrapper checkAdd(SetComponentEvent<Symbol> event) {
		return check(event.getItems().toArray(new Symbol[0]));
	}

	private BooleanWrapper check(Symbol ... symbols) {
		for (Symbol s: symbols){
			if (containsGrouping(s))
				return new BooleanWrapper(false, 
						"A Terminal cannot contain the characters currently " +
								"used for grouping in the Variable Alphabet - " + this.getGroupingPair());
		}
		return new BooleanWrapper(true);
	}

	@Override
	public BooleanWrapper checkModify(SetComponentModifyEvent<Symbol> event) {
		return check(event.getModifiedItem());
	}

	@Override
	public BooleanWrapper checkRemove(SetComponentEvent<Symbol> event) {
		return new BooleanWrapper(true);
	}


}
