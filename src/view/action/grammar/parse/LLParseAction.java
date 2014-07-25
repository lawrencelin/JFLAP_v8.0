package view.action.grammar.parse;

import model.algorithms.testinput.parse.brute.UnrestrictedBruteParser;
import model.algorithms.testinput.parse.ll.LL1Parser;
import model.grammar.Grammar;
import view.grammar.GrammarView;
import view.grammar.parsing.ParserView;
import view.grammar.parsing.ll.LLParserView;

public class LLParseAction extends ParseAction {

	public LLParseAction(GrammarView view) {
		super("Build LL(1) Parse Table", view);
		this.setEnabled(true);
	}

	@Override
	public ParserView createParseView(Grammar g) {
		LL1Parser parser = new LL1Parser(g);
		return new LLParserView(parser);
	}

}
