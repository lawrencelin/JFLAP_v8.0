package model.languages;

/**
 * 
 * @author Peggy Li
 */

import java.util.Set;
import java.util.TreeSet;

import model.formaldef.components.alphabets.Alphabet;
import model.grammar.Grammar;
import model.grammar.ProductionSet;
import model.grammar.StartVariable;
import model.grammar.TerminalAlphabet;
import model.grammar.Variable;
import model.grammar.VariableAlphabet;
import model.grammar.typetest.matchers.ContextFreeChecker;
import model.symbols.SymbolString;

public abstract class Language {

	private TerminalAlphabet myAlphabet;
	private Set<SymbolString> myStrings;
	private Grammar myGrammar;

	public Language(TerminalAlphabet alpha) {
		myAlphabet = alpha;
	}

	public Language(Grammar g) {
		myAlphabet = g.getTerminals();
		myGrammar = g;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Language))
			return false;
		Language other = (Language) obj;
		if (!myAlphabet.equals(other.myAlphabet))
			return false;
		return myStrings.equals(other.myStrings);
	}

	public Grammar createGrammar() {
		VariableAlphabet v = new VariableAlphabet();
		TerminalAlphabet t = myAlphabet;
		ProductionSet p = new ProductionSet();
		createVariablesAndProductions(v, p);
		StartVariable s = new StartVariable("S");
		Grammar g = new Grammar(v, t, p, s);
		return g;
	}

	public abstract void createVariablesAndProductions(VariableAlphabet v,
			ProductionSet p);

	public TerminalAlphabet getAlphabet() {
		return myAlphabet.copy();
	}

	public Set<SymbolString> getStrings() {
		return getStrings(myAlphabet.size());
	}

	public Set<SymbolString> getStrings(int numberOfStrings) {
		if (myGrammar == null) {
			myGrammar = createGrammar();
		}
		if (myStrings == null) {
			LanguageGenerator generator = LanguageGenerator.createGenerator(myGrammar);

			myStrings = new TreeSet<SymbolString>(
					generator.getStrings(numberOfStrings));

		}
		Set<SymbolString> stringCopy = new TreeSet<SymbolString>(
				new SetComparator());
		stringCopy.addAll(myStrings);
		return stringCopy;
	}
}
