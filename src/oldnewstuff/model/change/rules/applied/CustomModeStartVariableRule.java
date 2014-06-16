package oldnewstuff.model.change.rules.applied;

import oldnewstuff.model.change.ChangeEvent;
import oldnewstuff.model.change.rules.Rule;
import universe.preferences.JFLAPPreferences;
import errors.BooleanWrapper;
import model.change.events.SetToEvent;
import model.grammar.StartVariable;
import model.grammar.VariableAlphabet;
import model.symbols.Symbol;

public class CustomModeStartVariableRule extends Rule {

	private VariableAlphabet myVars;

	public CustomModeStartVariableRule(VariableAlphabet alph) {
		super(SET_TO);
		myVars = alph;
	}

	@Override
	public String getDescriptionName() {
		return "Custom Mode Start Variable Rule";
	}

	@Override
	public String getDescription() {
		return "The Start Variable can only be set to a variable in the variable" +
				" alphabet.";
	}

	@Override
	public BooleanWrapper checkRule(ChangeEvent event) {
		SetToEvent<StartVariable, Symbol> e = (SetToEvent<StartVariable, Symbol>) event;
		boolean ok = myVars.contains(e.getTo());
		return new BooleanWrapper(ok, getDescription());
	}


}
