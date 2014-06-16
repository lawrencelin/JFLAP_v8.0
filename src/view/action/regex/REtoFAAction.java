package view.action.regex;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import model.algorithms.conversion.regextofa.RegularExpressionToNFAConversion;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.regex.RegularExpression;
import model.undo.UndoKeeper;
import universe.JFLAPUniverse;
import view.algorithms.conversion.regextofa.RegularExpressionToFAPanel;
import view.automata.editing.AutomatonEditorPanel;
import view.regex.RegexView;

public class REtoFAAction extends AbstractAction{

	private RegexView myView;

	public REtoFAAction(RegexView view){
		super("RE to FA");
		myView = view;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		RegularExpression regex = myView.getDefinition();
		RegularExpressionToNFAConversion convert = new RegularExpressionToNFAConversion(regex);
		
		AutomatonEditorPanel<FiniteStateAcceptor, FSATransition> editor = new AutomatonEditorPanel<FiniteStateAcceptor, FSATransition>(convert.getGTG(), new UndoKeeper(), true);
		RegularExpressionToFAPanel panel = new RegularExpressionToFAPanel(editor, convert);
		JFLAPUniverse.getActiveEnvironment().addSelectedComponent(panel);
	}

}
