package oldnewstuff.model.change.rules.applied;

import oldnewstuff.model.change.ChangeEvent;
import oldnewstuff.model.change.rules.Rule;
import universe.preferences.JFLAPPreferences;
import errors.BooleanWrapper;
import model.change.events.SetToEvent;
import model.grammar.StartVariable;
import model.grammar.Variable;
import model.symbols.Symbol;


public class DefaultModeStartSymbolRule extends Rule {

	public DefaultModeStartSymbolRule() {
		super(SET_TO);
	}

	@Override
	public String getDescriptionName() {
		return "Default Mode Start Variable Rule";
	}

	@Override
	public String getDescription() {
		return "The Start Variable can only be set to the default start variable, " +
					JFLAPPreferences.getDefaultStartVariable() + ".";
	}

	@Override
	public BooleanWrapper checkRule(ChangeEvent event) {
		SetToEvent<StartVariable, Symbol> e = (SetToEvent<StartVariable, Symbol>) event;
		boolean ok = e.getTo().equals(JFLAPPreferences.getDefaultStartVariable());
		return new BooleanWrapper(ok, getDescription());
	}

}
