package model.languages;

/**
 * Class used for generating sentences
 * in a language given a grammar 
 * for the language
 * 
 * @author Ian McMahon
 */

import java.util.*;

import model.algorithms.testinput.parse.*;
import model.algorithms.testinput.parse.cyk.CYKParser;
import model.algorithms.transform.grammar.CNFConverter;
import model.grammar.*;
import model.symbols.*;

public class ContextFreeLanguageGenerator extends LanguageGenerator {
	private Grammar CNFGrammar;
	private Parser myParser;
	private Set<SymbolString> myPossibleStrings;

	public ContextFreeLanguageGenerator(Grammar g) {
		super(g);
	}

	@Override
	protected void initialize(Grammar g) {
		super.initialize(g);

		myPossibleStrings = new TreeSet<SymbolString>();

		CNFConverter converter = new CNFConverter(getGrammar());
		converter.stepToCompletion();
		CNFGrammar = converter.getTransformedGrammar();
		myParser = new CYKParser(CNFGrammar);
	}

	@Override
	protected void clear() {
		super.clear();
		myPossibleStrings.clear();
		
		for (Symbol terminal : getGrammar().getTerminals()) {
			myPossibleStrings.add(new SymbolString(terminal));
		}
		
		SymbolString lambda = new SymbolString();

		for (Production p : getGrammar().getStartProductions()) {
			if (getStringsInLanguage().contains(lambda))
				return;
			if (p.isLambdaProduction())
				addStringToLanguage(lambda);
		}
		
		
	}
	
	@Override
	public void generateStrings() {
		//Checks single symbol strings
		parsePossibleStrings();
		
		while (getStringsInLanguage().size() < getNumberToGenerate()) {
			getNextLengthStrings();
			parsePossibleStrings();
		}
	}

	@Override
	public void generateStringsOfLength(int length) {
		//ensures that all string of the length are generated.
		setNumberToGenerate(LARGE_NUMBER);
		SymbolString lambda = new SymbolString();
		
		if(length == 0 && getStringsInLanguage().contains(lambda))
			return;
		getStringsInLanguage().remove(lambda);
		
		for (int i = 1; i < length; i++) {
			getNextLengthStrings();
		}
		parsePossibleStrings();
	}

	/**
	 * Modifies <CODE>myPossibleStrings</CODE> to consist of every possible
	 * string of the next length by adding each terminal to the end of each
	 * existing possibility.
	 */
	private void getNextLengthStrings() {
		List<SymbolString> tempList = new ArrayList<SymbolString>();
		tempList.addAll(myPossibleStrings);
		
		for (SymbolString string : tempList) {
			for (Symbol terminal : getGrammar().getTerminals()) {
				SymbolString temp = string.copy();
				temp.add(terminal);
				myPossibleStrings.add(temp);
			}
			myPossibleStrings.remove(string);
		}
	}

	/**
	 * Using CYK parsing, checks every possible string and adds any strings that
	 * the language can produce to <CODE>myStringsInLanguage</CODE>.
	 */
	private void parsePossibleStrings() {
		for (SymbolString string : myPossibleStrings) {
			if (getStringsInLanguage().size() >= getNumberToGenerate())
				break;
			if (myParser.quickParse(string)) {
				addStringToLanguage(string);
			}
		}
	}

	

}
