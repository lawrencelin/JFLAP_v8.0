package view.action.grammar.parse;

import model.algorithms.testinput.parse.ParserException;
import model.algorithms.testinput.parse.cyk.CYKParser;
import model.grammar.Grammar;
import model.grammar.typetest.matchers.CNFChecker;

import universe.JFLAPUniverse;
import universe.preferences.JFLAPPreferences;
import view.environment.JFLAPEnvironment;
import view.grammar.GrammarView;
import view.grammar.parsing.ParserView;
import view.grammar.parsing.cyk.CYKParseView;

/**
 * Initializes a CYK Parser for the given CNF grammar, throws exception if
 * grammar is not in CNF (I imagine in the future, this will instead bring up a
 * dialog to start a CNF conversion)
 * 
 * @author Ian McMahon
 * 
 */
public class CYKParseAction extends ParseAction<CYKParser> {

	public CYKParseAction(GrammarView view) {
		super("CYK Parse", view);
	}

	@Override
	public ParserView<CYKParser> createParseView(Grammar g) {
		if (!new CNFChecker().matchesGrammar(g))
			throw new ParserException(
					"The grammar must be in CNF form to be parsed!");
		CYKParser parser = new CYKParser(g);
		JFLAPEnvironment env = JFLAPUniverse.getActiveEnvironment();
		return new CYKParseView(parser);
	}

}
