package view.action.newactions;

import java.awt.Component;

import universe.preferences.JFLAPMode;
import universe.preferences.JFLAPPreferences;

import model.grammar.Grammar;

public class NewGrammarAction extends NewFormalDefinitionAction<Grammar> {

	public NewGrammarAction() {
		super("Grammar");
	}

	@Override
	public Grammar createDefinition() {
		return new Grammar();
	}



}
