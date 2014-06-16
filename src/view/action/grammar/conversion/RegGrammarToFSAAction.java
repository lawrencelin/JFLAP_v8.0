package view.action.grammar.conversion;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import model.algorithms.conversion.gramtoauto.GrammarToAutomatonConverter;
import model.algorithms.conversion.gramtoauto.RGtoFSAConverter;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.grammar.Grammar;
import model.undo.UndoKeeper;
import universe.JFLAPUniverse;
import view.algorithms.conversion.gramtoauto.GrammarToAutoConversionPanel;
import view.automata.editing.AutomatonEditorPanel;
import view.grammar.GrammarView;

public class RegGrammarToFSAAction extends GrammarToAutomatonAction<FiniteStateAcceptor, FSATransition, RGtoFSAConverter>{


	public RegGrammarToFSAAction(GrammarView view){
		super(view, "Convert Regular Grammar to FSA", "Convert to FA");
	}

	@Override
	public RGtoFSAConverter createConverter(Grammar g) {
		return new RGtoFSAConverter(g);
	}

}
