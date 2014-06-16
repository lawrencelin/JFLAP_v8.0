package oldnewstuff.model.change.rules.applied;

import oldnewstuff.model.change.rules.IndividualIdenticalSymbolRule;
import model.grammar.TerminalAlphabet;
import model.grammar.VariableAlphabet;

public class VarsVersusTermsIdenticalRule extends IndividualIdenticalSymbolRule<VariableAlphabet, TerminalAlphabet> {

	public VarsVersusTermsIdenticalRule(TerminalAlphabet alphabet) {
		super(alphabet);
	}

	@Override
	public String getDescriptionName() {
		return "Variable versus Terminal Identical Rule";
	}

	@Override
	public String getDescription() {
		return "No variable may be identical to any symbol in the Terminal Alphabet";
	}

}
