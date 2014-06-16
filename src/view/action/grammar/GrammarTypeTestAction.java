package view.action.grammar;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import model.grammar.Grammar;
import model.grammar.typetest.matchers.CNFChecker;
import model.grammar.typetest.matchers.ContextFreeChecker;
import model.grammar.typetest.matchers.ContextSensitiveChecker;
import model.grammar.typetest.matchers.GNFChecker;
import model.grammar.typetest.matchers.LeftLinearChecker;
import model.grammar.typetest.matchers.RightLinearChecker;
import universe.JFLAPUniverse;
import view.grammar.GrammarView;

public class GrammarTypeTestAction extends AbstractAction {

	private GrammarView myView;
	private static final String RIGHT = " right-linear Grammar",
			LEFT = " left-linear Grammar",
			REG_AND_CF = " (Regular Grammar and Context-Free Grammar)",
			ALSO_CF = " (also Context-Free Grammar)",
			CNF = " CNF Grammar",
			GNF = " GNF Grammar",
			CF = " Context-Free Grammar",
			CS = " Context-Sensitive Grammar (also Unrestricted Grammar)",
			UNR = "n Unrestricted Grammar";

	public GrammarTypeTestAction(GrammarView view) {
		super("Test for Grammar Type");
		myView = view;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		Grammar g = myView.getDefinition();
		String message = "This is a";

		if (new RightLinearChecker().matchesGrammar(g))
			message += RIGHT + REG_AND_CF;
		else if (new LeftLinearChecker().matchesGrammar(g))
			message += LEFT + REG_AND_CF;
		else if (new CNFChecker().matchesGrammar(g))
			message += CNF + ALSO_CF;
		else if (new GNFChecker().matchesGrammar(g))
			message += GNF + ALSO_CF;
		else if (new ContextFreeChecker().matchesGrammar(g))
			message += CF;
		else if (new ContextSensitiveChecker().matchesGrammar(g))
			message += CS;
		else
			message += UNR;
		JOptionPane.showMessageDialog(JFLAPUniverse.getActiveEnvironment(),
				message, "Grammar Type", JOptionPane.INFORMATION_MESSAGE, null);
	}

}
