package model.grammar.typetest;

import java.util.ArrayList;

import model.grammar.Grammar;
import model.grammar.typetest.matchers.CNFChecker;
import model.grammar.typetest.matchers.ContextFreeChecker;
import model.grammar.typetest.matchers.ContextSensitiveChecker;
import model.grammar.typetest.matchers.GNFChecker;
import model.grammar.typetest.matchers.GrammarChecker;
import model.grammar.typetest.matchers.LL1Checker;
import model.grammar.typetest.matchers.LeftLinearChecker;
import model.grammar.typetest.matchers.RegChecker;
import model.grammar.typetest.matchers.RightLinearChecker;
import model.grammar.typetest.matchers.UnrestrictedChecker;

public enum GrammarType{
	REGULAR("Regular Grammar", "RG", new RegChecker()),
	LEFT_LINEAR("Left-Linear Grammar", "LLG", new LeftLinearChecker()),
	RIGHT_LINEAR("Right-Linear Grammar", "RLG", new RightLinearChecker()),
	CONTEXT_FREE("Context Free Grammar", "CFG", new ContextFreeChecker()),
	CHOMSKY_NORMAL_FORM("Grammar in Chomsky Normal Form", 
							"CNF", 
							new CNFChecker()),
	GREIBACH_NORMAL_FORM("Grammar in Greibach Normal Form", 
							"GNF", 
							new GNFChecker()),
	CONTEXT_SENSITIVE("Context Sensitive Grammar", "CSG", new ContextSensitiveChecker()),
	UNRESTRICTED("Unrestricted Grammar","UG", new UnrestrictedChecker()),
	LL1("LL1 Grammar", "LL1", new LL1Checker())
	;

	
	public String abbreviation;
	public String name;
	private GrammarChecker myMatcher; 
	
	private GrammarType(String name, String abbr, GrammarChecker pm) {
		this.name = name;
		abbreviation = abbr;
		myMatcher = pm;
	}
	
	@Override
	public String toString() {
		return name + "(" + abbreviation + ")";
	}
	
	public static GrammarType[] getType(Grammar g){

		ArrayList<GrammarType> types = new ArrayList<GrammarType>();

		
		for (GrammarType type: GrammarType.values()){
			if (type.matches(g))
				types.add(type);
		}
		
		return types.toArray(new GrammarType[0]);
		
	}

	public boolean matches(Grammar g) {
		return myMatcher.matchesGrammar(g);
	}
	
	
	
	
	
	
	

	
	
	
	
	
	
	
}
