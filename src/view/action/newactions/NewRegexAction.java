package view.action.newactions;

import model.regex.RegularExpression;

public class NewRegexAction extends NewFormalDefinitionAction<RegularExpression>{

	public NewRegexAction() {
		super("Regular Expression");
	}

	@Override
	public RegularExpression createDefinition() {
		return new RegularExpression();
	}

}
