package view.action.automata;

import java.awt.event.ActionEvent;

import model.algorithms.conversion.fatoregex.DFAtoRegularExpressionConverter;
import model.automata.State;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.undo.UndoKeeper;
import universe.JFLAPUniverse;
import view.automata.editing.AutomatonEditorPanel;
import view.automata.views.FSAView;
import view.regex.FAToREPanel;

public class DFAtoREAction extends AutomatonAction{

	public DFAtoREAction(FSAView view) {
		super("Convert to RE", view);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		FSAView view = (FSAView) getView();
		AutomatonEditorPanel<FiniteStateAcceptor, FSATransition> panel = (AutomatonEditorPanel<FiniteStateAcceptor, FSATransition>) view.getCentralPanel();
		FiniteStateAcceptor fsa = view.getDefinition();
		
		DFAtoRegularExpressionConverter convert = new DFAtoRegularExpressionConverter(fsa);
		AutomatonEditorPanel<FiniteStateAcceptor, FSATransition> editor = new AutomatonEditorPanel<FiniteStateAcceptor, FSATransition>(convert.getGTG(), new UndoKeeper(), true);
		
		for(State s : fsa.getStates())
			editor.moveState(s, panel.getPointForVertex(s));
		for(FSATransition t : fsa.getTransitions()){
			State[] edge = new State[]{t.getFromState(), t.getToState()};
			editor.moveCtrlPoint(edge[0], edge[1], panel.getControlPoint(edge));
		}
		
		FAToREPanel convertPanel = new FAToREPanel(editor, convert);
		JFLAPUniverse.getActiveEnvironment().addSelectedComponent(convertPanel);
	}

}
