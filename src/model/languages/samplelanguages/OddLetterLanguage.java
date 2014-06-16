package model.languages.samplelanguages;

import model.grammar.Production;
import model.grammar.ProductionSet;
import model.grammar.Terminal;
import model.grammar.TerminalAlphabet;
import model.grammar.Variable;
import model.grammar.VariableAlphabet;
import model.languages.Language;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public class OddLetterLanguage extends Language {
	private Terminal myTerminal;
	
	public OddLetterLanguage(TerminalAlphabet alpha, Terminal terminal) {
		super(alpha);
		myTerminal = terminal;
	}

	@Override
	public void createVariablesAndProductions(VariableAlphabet v,
			ProductionSet p) {
		Variable S = new Variable("S"), A = new Variable("A");
		v.addAll(A,S);
		
		p.add(new Production(S,myTerminal,A));
		p.add(new Production(A, new SymbolString()));
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
