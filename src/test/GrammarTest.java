package test;

import java.io.File;

import debug.JFLAPDebug;
import errors.BooleanWrapper;
import file.xml.XMLCodec;
import model.algorithms.transform.grammar.CNFConverter;
import model.algorithms.transform.grammar.LambdaProductionRemover;
import model.algorithms.transform.grammar.UnitProductionRemover;
import model.algorithms.transform.grammar.UselessProductionRemover;
import model.automata.InputAlphabet;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.alphabets.AlphabetException;
import model.formaldef.components.alphabets.grouping.GroupingPair;
import model.formaldef.rules.AlphabetRule;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.ProductionSet;
import model.grammar.StartVariable;
import model.grammar.Terminal;
import model.grammar.TerminalAlphabet;
import model.grammar.Variable;
import model.grammar.VariableAlphabet;
import model.grammar.typetest.matchers.ContextFreeChecker;
import model.grammar.typetest.matchers.GrammarChecker;
import model.regex.OperatorAlphabet;
import model.regex.RegularExpression;
import model.regex.RegularExpressionGrammar;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public class GrammarTest extends TestHarness {

	@Override
	public void runTest() {
		String toSave = System.getProperties().getProperty("user.dir") +"/filetest";
		File f = new File(toSave + "/grammar.jff");
		Grammar g = (Grammar) new XMLCodec().decode(f);

		outPrintln("Initial Grammar:\n" + g.toString());

		GrammarChecker check = new ContextFreeChecker();
		boolean isCNF = check.matchesGrammar(g);
		Grammar g2;
		//Remove lambda productions
		if(isCNF){
			LambdaProductionRemover r1 = new LambdaProductionRemover(g);
			r1.stepToCompletion();
			g2 = r1.getTransformedGrammar();
			outPrintln("LAMBDA Productions removed:\n" + g2);

			UnitProductionRemover r2 = new UnitProductionRemover(g2);
			r2.stepToCompletion();
			g2 = r2.getTransformedGrammar();
			outPrintln("UNIT Productions removed:\n" + g2);
		}
		else g2=g;

		UselessProductionRemover r3 = new UselessProductionRemover(g2);
		r3.stepToCompletion();
		g2 = r3.getTransformedGrammar();
		outPrintln("USELESS Productions removed:\n" + g2);
		//
		if (isCNF){
			CNFConverter r4 = new CNFConverter(g);
			r4.stepToCompletion();
			g2 = r4.getTransformedGrammar();
			outPrintln("CNF Converted:\n" + g2);


			InputAlphabet alph = new InputAlphabet();
			alph.addAll(g.getTerminals());
			r4 = new CNFConverter(new RegularExpressionGrammar(alph,
					new OperatorAlphabet()));
			r4.stepToCompletion();
			g2 = r4.getTransformedGrammar();
			outPrintln("CNF Converted RegEx Grammar:\n" + g2);
		}
	}

	public static void addSymbols(Alphabet alph, Symbol ... sym) {
		for (Symbol s: sym){
			try {
				alph.add(s);
			} catch (AlphabetException e){
				System.err.println(e.getMessage());
			}
		}

	}

	public static String createRuleString(Alphabet alph) {
		String ruleString = alph.toString() + "\n" +
				"\tDescription: " + alph.getDescription() + "\n"+
				"\tRules: " + "\n";

		for (AlphabetRule rule: alph.getRules()){
			ruleString += "\t\t" + rule.toString() + "\n";
		}

		return ruleString;
	}

	@Override
	public String getTestName() {
		return "GRAMMAR TEST";
	}


}
