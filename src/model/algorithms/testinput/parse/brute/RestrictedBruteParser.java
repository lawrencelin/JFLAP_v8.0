package model.algorithms.testinput.parse.brute;

import java.util.ArrayList;

import model.algorithms.testinput.parse.ParserException;
import model.grammar.Grammar;
import model.grammar.typetest.GrammarType;
import model.symbols.Symbol;
import model.symbols.SymbolString;

/**
 * Brute parser for Context Free grammars, more restricted in that we can check
 * more closely whether or not a derivation can possibly derive the target
 * string.
 * 
 * Code from JFLAP 7.0
 * 
 * @author Thomas Finley, Ian McMahon
 * 
 */
public class RestrictedBruteParser extends UnrestrictedBruteParser {

	public RestrictedBruteParser(Grammar g) {
		super(g);
	}

	/**
	 * Checks if the derivation starts or ends with terminals, and if so,
	 * compares to target input's start and/or end. Alsochecks if the derivation
	 * contains any terminals not in the input string, ruling out some
	 * derivations that would still be allowed in Unrestricted grammars.
	 */
	@Override
	public boolean isPossibleSententialForm(SymbolString derivation) {
		if (!super.isPossibleSententialForm(derivation))
			return false;

		boolean startBookend = false, endBookend = false;
		ArrayList<SymbolString> discrete = new ArrayList<SymbolString>();

		/*
		 * Set the start and end "bookeneds", that is, the derivation is padded
		 * with terminals on either it's left or right sides.
		 */
		if (derivation.isEmpty()) {
			startBookend = endBookend = false;
		} else {
			startBookend = !Grammar.isVariable(derivation.getFirst());
			endBookend = !Grammar.isVariable(derivation.getLast());
		}

		/* Break up groups of terminals into the "discrete" array. */
		if (startBookend)
			discrete.add(new SymbolString());

		for (Symbol s : derivation) {
			if (Grammar.isVariable(s))
				discrete.add(new SymbolString());
			if (Grammar.isTerminal(s))
				discrete.get(discrete.size() - 1).add(s);
		}
		if (!endBookend && !discrete.isEmpty())
			discrete.remove(discrete.size() - 1);

		int cp = 0;
		for (int i = 0; i < discrete.size(); i++) {
			SymbolString e = discrete.get(i);
			if (startBookend && i == 0) {
				if (!getInput().startsWith(e)) {
					return false;
				}
				cp = e.size();
			} else if (endBookend && i == discrete.size() - 1) {
				if (!getInput().endsWith(e)) {
					return false;
				}
			} else {
				if (cp >= discrete.size()) {
					continue;
				}
				cp = getInput().indexOf(e, cp);
				if (cp == -1) {
					return false;
				}
				cp += e.size();
			}
		}
		return true;
	}
	
	@Override
	public GrammarType getRequiredGrammarType() throws ParserException {
		return GrammarType.CONTEXT_FREE;
	}
}
