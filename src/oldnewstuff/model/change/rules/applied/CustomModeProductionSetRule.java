package oldnewstuff.model.change.rules.applied;

import java.util.List;
import java.util.Set;

import oldnewstuff.model.change.rules.SetComponentRule;

import errors.BooleanWrapper;
import model.change.events.SetComponentEvent;
import model.change.events.SetComponentModifyEvent;
import model.grammar.Production;
import model.grammar.TerminalAlphabet;
import model.grammar.VariableAlphabet;
import model.symbols.Symbol;

public class CustomModeProductionSetRule extends SetComponentRule<Production> {

	private VariableAlphabet myVars;
	private TerminalAlphabet myTerms;

	public CustomModeProductionSetRule(int type, 
							VariableAlphabet var, TerminalAlphabet terms) {
		super(type);
		myVars= var;
		myTerms= terms;
	}

	@Override
	public String getDescriptionName() {
		return "Custom Mode Production Set Rule";
	}

	@Override
	public String getDescription() {
		return "Prevents a production from being added to the production set" +
				" unless it is entirely composed of variables and terminals. ";
	}

	@Override
	public BooleanWrapper checkRemove(SetComponentEvent<Production> event) {
		return new BooleanWrapper(true);
	}

	@Override
	public BooleanWrapper checkAdd(SetComponentEvent<Production> event) {
		return checkValid(event.getItems());
	}

	private BooleanWrapper checkValid(List<Production> items) {
		for (Production p: items){
			Set<Symbol> used = p.getSymbolsUsedForAlphabet(a);
			used.removeAll(myVars);
			used.removeAll(myTerms);
			if (!used.isEmpty()){
				return new BooleanWrapper(false, "The production " + p + 
									" contains illegal symbols: " + used);
			}
		}
		return new BooleanWrapper(true);
	}

	@Override
	public BooleanWrapper checkModify(SetComponentModifyEvent<Production> event) {
		return checkValid(event.getItems());
	}

}
