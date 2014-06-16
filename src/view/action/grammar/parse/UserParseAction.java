package view.action.grammar.parse;

import model.grammar.Grammar;
import view.grammar.GrammarView;
import view.grammar.parsing.ParserView;

public class UserParseAction extends ParseAction {

	public UserParseAction(GrammarView view) {
		super("User Control Parse", view);
		this.setEnabled(false);
	}

	@Override
	public ParserView createParseView(Grammar g) {
		// TODO Auto-generated method stub
		return null;
	}

}
