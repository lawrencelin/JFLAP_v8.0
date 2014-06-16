package view.action.newactions;

import model.automata.transducers.moore.MooreMachine;

public class NewMooreAction extends NewFormalDefinitionAction<MooreMachine> {

	public NewMooreAction() {
		super("Moore Machine");
	}

	@Override
	public MooreMachine createDefinition() {
		return new MooreMachine();
	}

}
