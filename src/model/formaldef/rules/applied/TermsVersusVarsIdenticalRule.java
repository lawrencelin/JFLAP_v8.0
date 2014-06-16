package model.formaldef.rules.applied;

import model.formaldef.rules.IndividualIdenticalSymbolRule;
import model.grammar.TerminalAlphabet;
import model.grammar.VariableAlphabet;

public class TermsVersusVarsIdenticalRule extends IndividualIdenticalSymbolRule<TerminalAlphabet, VariableAlphabet> {


	public TermsVersusVarsIdenticalRule(VariableAlphabet alphabet) {
		super(alphabet);
	}

	@Override
	public String getDescriptionName() {
		return "Terminal versus Variables Identical Rule";
	}

	@Override
	public String getDescription() {
		return "No terminal may be identical to any symbol in the Variable Alphabet";
	}

}
