package view.action.newactions;

import model.automata.acceptors.pda.PushdownAutomaton;

public class NewPDAAction extends NewFormalDefinitionAction<PushdownAutomaton> {

	public NewPDAAction() {
		super("Pushdown Automaton");
	}

	@Override
	public PushdownAutomaton createDefinition() {
		return new PushdownAutomaton();
	}

}
