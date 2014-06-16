package oldnewstuff.model.change.rules.applied;

import oldnewstuff.model.change.ChangeEvent;
import oldnewstuff.model.change.rules.Rule;
import errors.BooleanWrapper;
import model.change.events.SetToEvent;
import model.formaldef.components.functionset.function.LanguageFunction;

public abstract class LanguageFunctionRule<T extends LanguageFunction<T>> extends Rule {

	public LanguageFunctionRule() {
		super(SET_TO);
	}

	@Override
	public BooleanWrapper checkRule(ChangeEvent event) {
		return checkRule(((SetToEvent<T, T>) event).getTo());
	}

	
	public abstract BooleanWrapper checkRule(T func);
}
