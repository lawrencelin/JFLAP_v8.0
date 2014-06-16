package model.formaldef.rules.applied;

import errors.BooleanWrapper;
import model.formaldef.components.alphabets.grouping.GroupingPair;
import model.formaldef.rules.GroupingRule;
import model.grammar.Grammar;
import model.grammar.VariableAlphabet;
import model.symbols.Symbol;


public class VariableGroupingRule extends GroupingRule<VariableAlphabet> {





	private TerminalGroupingRule myInternalGroupingRule;

	public VariableGroupingRule(GroupingPair gp) {
		super(gp);
		myInternalGroupingRule = new TerminalGroupingRule(gp);
	}


	private BooleanWrapper checkExternalGrouping(String string) {
		
		boolean correct = checkExternalGrouping(string,this.getGroupingPair());
		
			return new BooleanWrapper(correct, "The Variable " + string + 
												" does not contain the necessary "+ 
												"grouping characters " + this.getGroupingPair().toShortString() + " flanking the symbol.");
	}



	private BooleanWrapper checkValid(Symbol newSymbol) {
		String toCheck = newSymbol.toString();
		
		//Check that grouping outside is correct
		BooleanWrapper bw = checkExternalGrouping(newSymbol.toString());
		
		if (bw.isTrue()){
			//Check that there are no grouping characters inside the symbol
			bw = checkInternalGrouping(toCheck);
		}
		
		return bw;
	}


	private BooleanWrapper checkInternalGrouping(String toCheck) {
		String inside = toCheck.substring(1, toCheck.length()-1);
		boolean contains = myInternalGroupingRule.canAdd(null, new Symbol(inside)).isTrue();
		return new BooleanWrapper(contains, "You may not create a Variable " +
												"with internal grouping characters. ");
	}


	@Override
	public BooleanWrapper canModify(VariableAlphabet a,
			Symbol oldSymbol, Symbol newSymbol) {
		return checkValid(newSymbol);
	}

	@Override
	public BooleanWrapper canAdd(VariableAlphabet a, Symbol newSymbol) {
		return checkValid(newSymbol);
	}
	

	@Override
	public String getDescription() {
		return "Variables are fun!! This rule dictates what is possible in the case of grammars using grouping.";
	}

	@Override
	public String getDescriptionName() {
		return "Variable Grouping Rule";
	}

	
	public static boolean checkExternalGrouping(String string, GroupingPair gp){
		return string.length() > 2 && gp.isGrouped(string);
	}

}
