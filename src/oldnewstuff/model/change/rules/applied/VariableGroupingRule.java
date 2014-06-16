package oldnewstuff.model.change.rules.applied;

import oldnewstuff.model.change.rules.GroupingRule;
import errors.BooleanWrapper;
import model.change.events.SetComponentEvent;
import model.change.events.SetComponentModifyEvent;
import model.formaldef.components.alphabets.grouping.GroupingPair;
import model.grammar.Grammar;
import model.grammar.VariableAlphabet;
import model.symbols.Symbol;


public class VariableGroupingRule extends GroupingRule {

	public VariableGroupingRule(int type,GroupingPair gp) {
		super(type, gp);
	}


	private BooleanWrapper checkExternalGrouping(String string) {

		boolean correct = string.length() > 2 && 
				string.indexOf(this.getOpenGroup()) == 0 && 
				string.indexOf(this.getCloseGroup()) == string.length()-1;

		return new BooleanWrapper(correct, "The Variable " + string + 
				" does not contain the necessary "+ 
				"grouping characters " + this.getGroupingPair().toShortString() + " flanking the symbol.");
	}



	private BooleanWrapper checkValid(Symbol ... symbols) {
		BooleanWrapper bw = new BooleanWrapper(true);
		for (Symbol s: symbols){
			String toCheck = s.toString();

			//Check that grouping outside is correct
			bw = checkExternalGrouping(s.toString());

			if (bw.isTrue()){
				//Check that there are no grouping characters inside the symbol
				bw = checkInternalGrouping(toCheck);
			}
			if (bw.isError())
				return bw;
		}
		return bw;
	}


	private BooleanWrapper checkInternalGrouping(String toCheck) {
		String inside = toCheck.substring(1, toCheck.length()-1);
		boolean contains = containsGrouping(new Symbol(inside));
		return new BooleanWrapper(contains, "You may not create a Variable " +
				"with internal grouping characters. ");
	}


	@Override
	public String getDescription() {
		return "Variables are fun!! This rule dictates what is possible in the case of grammars using grouping.";
	}

	@Override
	public String getDescriptionName() {
		return "Variable Grouping Rule";
	}


	@Override
	public BooleanWrapper checkAdd(SetComponentEvent<Symbol> event) {
		return checkValid(event.getItems().toArray(new Symbol[0]));
	}


	@Override
	public BooleanWrapper checkModify(SetComponentModifyEvent<Symbol> event) {
		return checkValid(event.getModifiedItem());
	}


	@Override
	public BooleanWrapper checkRemove(SetComponentEvent<Symbol> event) {
		return new BooleanWrapper(true);
	}


}
