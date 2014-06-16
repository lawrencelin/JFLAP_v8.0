package view.action.grammar.parse;

import model.grammar.Grammar;
import view.grammar.GrammarView;
import view.grammar.parsing.ParserView;

public class LRParseAction extends ParseAction {

	public LRParseAction(GrammarView view) {
		super("Build SLR(1) Parse Table", view);
		this.setEnabled(false);
	}

	@Override
	public ParserView createParseView(Grammar g) {
		// TODO Auto-generated method stub
		return null;
	}

}
