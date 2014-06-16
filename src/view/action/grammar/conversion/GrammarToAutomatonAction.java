package view.action.grammar.conversion;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import model.algorithms.conversion.gramtoauto.GrammarToAutomatonConverter;
import model.algorithms.conversion.gramtoauto.RGtoFSAConverter;
import model.automata.Automaton;
import model.automata.SingleInputTransition;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.grammar.Grammar;
import model.undo.UndoKeeper;
import universe.JFLAPUniverse;
import view.algorithms.conversion.gramtoauto.GrammarToAutoConversionPanel;
import view.automata.editing.AutomatonEditorPanel;
import view.grammar.GrammarView;

public abstract class GrammarToAutomatonAction<S extends Automaton<R>, R extends SingleInputTransition<R>, T extends GrammarToAutomatonConverter<S, R>> extends AbstractAction{

	private GrammarView myView;
	private String myPanelName;
	
	public GrammarToAutomatonAction(GrammarView v, String name, String panelName){
		super(name);
		myView = v;
		myPanelName = panelName;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		Grammar g = myView.getDefinition();
		T convert  = createConverter(g);
		AutomatonEditorPanel<S, R> editor = new AutomatonEditorPanel<S, R>(convert.getConvertedAutomaton(), new UndoKeeper(), true);
		GrammarToAutoConversionPanel<S, R> panel = new GrammarToAutoConversionPanel<S, R>(convert, editor, myPanelName);
		
		JFLAPUniverse.getActiveEnvironment().addSelectedComponent(panel);
	}
	
	public abstract T createConverter(Grammar g);

}
