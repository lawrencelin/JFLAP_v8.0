package view.action.grammar.parse;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import model.algorithms.testinput.parse.Parser;
import model.grammar.Grammar;
import universe.JFLAPUniverse;
import view.environment.JFLAPEnvironment;
import view.grammar.GrammarView;
import view.grammar.parsing.ParserView;

/**
 * Superclass for initializing ParserViews and making them the selected
 * tab of the open JFLAP environment.
 * 
 * @author Ian McMahon
 * 
 * @param <S>
 */
public abstract class ParseAction<S extends Parser> extends AbstractAction {

	private GrammarView myView;

	public ParseAction(String name, GrammarView view) {
		super(name);
		myView = view;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Grammar g = myView.getDefinition();
		if (g == null)
			return;
		
		ParserView<S> parserView = createParseView(g);
		JFLAPEnvironment environ = JFLAPUniverse.getActiveEnvironment();
		environ.addSelectedComponent(parserView);
	}

	public abstract ParserView<S> createParseView(Grammar g);

}
