package view.action.newactions;

import universe.preferences.JFLAPPreferences;
import model.formaldef.FormalDefinition;

public abstract class NewFormalDefinitionAction<T extends FormalDefinition>
		extends NewAction<T> {

	public NewFormalDefinitionAction(String name) {
		super(name);
	}

	@Override
	public T createNewModel() {
		T def = createDefinition();
		if (def != null)
			def.setMode(JFLAPPreferences.getDefaultMode());
		return def;
	}

	public abstract T createDefinition();

}
