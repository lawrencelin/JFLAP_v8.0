/*
 *  JFLAP - Formal Languages and Automata Package
 * 
 * 
 *  Susan H. Rodger
 *  Computer Science Department
 *  Duke University
 *  August 27, 2009

 *  Copyright (c) 2002-2009
 *  All rights reserved.

 *  JFLAP is open source software. Please see the LICENSE for terms.
 *
 */




package test;

import java.util.Arrays;

import model.algorithms.testinput.parse.cyk.CYKParser;
import model.algorithms.transform.grammar.CNFConverter;
import model.automata.InputAlphabet;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.ProductionSet;
import model.grammar.StartVariable;
import model.grammar.Terminal;
import model.grammar.TerminalAlphabet;
import model.grammar.Variable;
import model.grammar.VariableAlphabet;
import model.regex.OperatorAlphabet;
import model.regex.RegularExpression;
import model.regex.RegularExpressionGrammar;
import model.symbols.Symbol;
import model.symbols.SymbolString;
import model.symbols.symbolizer.Symbolizers;

//import jflap.model.grammar.Grammar;
//import jflap.model.grammar.Production;
//import jflap.model.grammar.UnrestrictedGrammar;

/**
 * CYK Parser tester.
 * @author Kyung Min (Jason) Lee
 *
 */
public class CYKTester {

	public static void main(String[] args)
	{
//		VariableAlphabet variables = new VariableAlphabet();
//		variables.addAll(new Symbol("S"), new Symbol("D"), new Symbol("A"), new Symbol("C"), new Symbol("B"));
//		TerminalAlphabet terminals = new TerminalAlphabet();
//		terminals.addAll(new Symbol("a"), new Symbol("b"));
//		ProductionSet functions = new ProductionSet();
//		functions.add(new Production(new Variable("S"), new SymbolString(new Variable("B"), new Variable("A"))));
//		functions.add(new Production(new Variable("S"), new SymbolString(new Terminal("a"))));
//		functions.add(new Production(new Variable("A"), new SymbolString(new Variable("B"), new Variable("D"))));
//		functions.add(new Production(new Variable("D"), new SymbolString(new Variable("A"), new Variable("C"))));
//		functions.add(new Production(new Variable("A"), new SymbolString(new Variable("A"), new Variable("A"))));
//		functions.add(new Production(new Variable("A"), new SymbolString(new Variable("B"), new Variable("C"))));
//		functions.add(new Production(new Variable("C"), new Terminal("b")));
//		functions.add(new Production(new Variable("A"), new Terminal("a")));
//		functions.add(new Production(new Variable("A"), new SymbolString(new Variable("B"), new Variable("A"))));
//		functions.add(new Production(new Variable("B"), new Terminal("a")));
//		StartVariable startVar = new StartVariable("S");
//		Grammar g = new Grammar(variables, terminals, functions, startVar);
//		CYKParser parser = new CYKParser(g);
//		Symbol a = new Terminal("a");
//		Symbol b = new Terminal("b");
//		SymbolString target = new SymbolString(a,a,b,a,b);
//		System.out.println(parser.parse(target));
//		System.out.println(parser.getTrace());
	
		InputAlphabet input = new InputAlphabet();
//		for (char i = 'a'; i <= 'z'; i++){
//			input.add(new Symbol(Character.toString(i)));
//		}
		input.add(new Symbol(Character.toString('0')));
		input.add(new Symbol(Character.toString('1')));
		RegularExpressionGrammar gram = new RegularExpressionGrammar(input, new OperatorAlphabet());
		gram.trimAlphabets();
		System.out.println(gram.toString());
		
		CNFConverter conv = new CNFConverter(gram);		
		conv.stepToCompletion();
		Grammar CNFgram = conv.getTransformedGrammar();
//		System.out.println(CNFgram.toString());
		
		CYKParser parser = new CYKParser(CNFgram);
		parser.quickParse(Symbolizers.symbolize("(000)", CNFgram));
		System.out.println(parser.isAccept());
		System.out.println(Arrays.toString(parser.getDerivation().getResultArray()));
	}
}
