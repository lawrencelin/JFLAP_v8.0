package view.action.grammar.parse;

import model.grammar.Grammar;
import view.grammar.GrammarView;
import view.grammar.parsing.ParserView;

public class LLParseAction extends ParseAction {

	public LLParseAction(GrammarView view) {
		super("Build LL(1) Parse Table", view);
		this.setEnabled(false);
	}

	@Override
	public ParserView createParseView(Grammar g) {
		// TODO Auto-generated method stub
		return null;
	}

}
