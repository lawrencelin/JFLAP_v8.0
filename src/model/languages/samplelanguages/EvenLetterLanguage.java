package model.languages.samplelanguages;

import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.ProductionSet;
import model.grammar.StartVariable;
import model.grammar.Terminal;
import model.grammar.TerminalAlphabet;
import model.grammar.Variable;
import model.grammar.VariableAlphabet;
import model.languages.Language;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public class EvenLetterLanguage extends Language{
	private Terminal myTerminal;
	
	public EvenLetterLanguage(TerminalAlphabet alpha, Terminal letter) {
		super(alpha);
		myTerminal = letter;
	}


	@Override
	public void createVariablesAndProductions(VariableAlphabet v,
			ProductionSet p) {
		Variable S = new Variable("S"), A = new Variable("A");
		v.addAll(A,S);
		
		p.add(new Production(S,myTerminal,A));
		p.add(new Production(S, new SymbolString()));
		for(Symbol terminal : getAlphabet()){
			if(!terminal.equals(myTerminal)){
				p.add(new Production(S, terminal, S));
				p.add(new Production(A, terminal, A));
			}else{
				p.add(new Production(A, myTerminal,S));
			}
		}
	}

}
