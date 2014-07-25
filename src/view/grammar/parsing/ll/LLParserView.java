package view.grammar.parsing.ll;

import java.awt.Dimension;

import javax.swing.JComponent;

import model.algorithms.testinput.parse.Parser;
import model.algorithms.testinput.parse.StackUsingParser;
import model.algorithms.testinput.parse.brute.UnrestrictedBruteParser;
import model.algorithms.testinput.parse.ll.LL1Parser;
import model.undo.UndoKeeper;
import util.view.magnify.MagnifiableToolbar;
import view.algorithms.toolbar.StackUsingParserToolbar;
import view.algorithms.toolbar.SteppableToolbar;
import view.grammar.parsing.FindFirstParserView;
import view.grammar.parsing.brute.BruteParseTablePanel;

public class LLParserView extends FindFirstParserView<LLParseTablePanel>{
	
	public LLParserView(LL1Parser alg) {
		super(alg);
		setPreferredSize(new Dimension(800, 700));
	}

	@Override
	public LLParseTablePanel createRunningView(Parser alg) {
		LLParseTablePanel running = new LLParseTablePanel(
				(LL1Parser) alg);
		return running;
	}

	@Override
	public StackUsingParserToolbar createToolbar(Parser alg) {
		return new StackUsingParserToolbar(getRunningView(),(StackUsingParser) alg, false);
	}

}
