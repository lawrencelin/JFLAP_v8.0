package test;

import model.algorithms.testinput.parse.brute.RestrictedBruteParser;
import model.algorithms.testinput.parse.brute.UnrestrictedBruteParser;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.ProductionSet;
import model.grammar.Terminal;
import model.grammar.Variable;
import model.symbols.symbolizer.Symbolizers;

public class BruteParserTester {
	
	public static void main (String[] args) {
		
		Variable S = new Variable("S"), A = new Variable("A");
		Terminal a = new Terminal("a");
//		
//		VariableAlphabet v = new VariableAlphabet();
//		v.addAll(S, A, B);
//		
//		TerminalAlphabet t = new TerminalAlphabet();
//		t.addAll(a, b, c);
		Grammar gram = new Grammar();
		ProductionSet prods = gram.getProductionSet();
		
		prods.add(new Production(S, A,A));
		prods.add(new Production(A, A,A ));
		prods.add(new Production(A, a));
		
		gram.setStartVariable(S);
		
		
		UnrestrictedBruteParser parser = UnrestrictedBruteParser.createNewBruteParser(gram);
		
		
		//parser.stepParser();
		//System.out.println(parser.isAccept());
		
//		//parser.resetParserStateOnly();
//		parser.stepParser();
//		parser.stepParser();
//		System.out.println(parser.isAccept());
		System.out.println(parser.quickParse(Symbolizers.symbolize("aaaaaaaaaaaaaaa", gram)));
		System.out.println(parser.getNumberOfNodes());
	}

}
