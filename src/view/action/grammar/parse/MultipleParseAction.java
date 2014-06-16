package view.action.grammar.parse;

import model.grammar.Grammar;
import view.grammar.GrammarView;
import view.grammar.parsing.ParserView;

public class MultipleParseAction extends ParseAction {

	public MultipleParseAction(GrammarView view) {
		super("Multiple Brute Force Parse", view);
		this.setEnabled(false);
	}

	@Override
	public ParserView createParseView(Grammar g) {
		// TODO Auto-generated method stub
		return null;
	}

}
