package model.languages.samplelanguages;

import model.formaldef.components.alphabets.AlphabetException;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.ProductionSet;
import model.grammar.StartVariable;
import model.grammar.TerminalAlphabet;
import model.grammar.Variable;
import model.grammar.VariableAlphabet;
import model.languages.Language;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public class ContainsSubstringLanguage extends Language{
	private SymbolString mySubstring;
	
	public ContainsSubstringLanguage(TerminalAlphabet alpha, SymbolString substring) {
		super(alpha);
		for(Symbol symbol : substring){
			if(!alpha.contains(symbol)) 
				throw new AlphabetException("The substring contains terminals not in the alphabet!");
		}
		mySubstring = substring;
	}
	
	@Override
	public void createVariablesAndProductions(VariableAlphabet v,
			ProductionSet p) {
		Variable S = new Variable("S"), A = new Variable("A");
		v.addAll(A,S);
		
		p.add(new Production(S,new SymbolString(A).concat(mySubstring).concat(new SymbolString(A))));
		p.add(new Production(A, new SymbolString()));
		for(Symbol terminal : getAlphabet()){
			p.add(new Production(A, terminal,A));
		}
		
	}

}
