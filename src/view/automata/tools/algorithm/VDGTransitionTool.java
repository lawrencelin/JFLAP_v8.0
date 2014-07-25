package view.automata.tools.algorithm;

import java.awt.event.MouseEvent;

import model.algorithms.conversion.regextofa.RegularExpressionToNFAConversion;
import model.algorithms.transform.grammar.UnitProductionRemover;
import model.automata.State;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.symbols.Symbol;
import model.symbols.SymbolString;
import view.automata.editing.AutomatonEditorPanel;
import view.automata.tools.TransitionTool;
import view.grammar.transform.DependencyGraphPanel;
import view.grammar.transform.UnitRemovalController;

public class VDGTransitionTool extends TransitionTool<FiniteStateAcceptor, FSATransition>{

//	private UnitProductionRemover myAlg;
	private UnitRemovalController myController;
//	private AutomatonEditorPanel<FiniteStateAcceptor, FSATransition> myPanel;
	private DependencyGraphPanel myPanel;
	
	public VDGTransitionTool(
			DependencyGraphPanel panel, UnitRemovalController control) {
		super(panel.getEditorPanel());
		myController = control;
		myPanel = panel;
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		if (getFrom() == null)
			return;
		Object o = getPanel().objectAtPoint(e.getPoint());
		if (o instanceof State) {
//			myPanel.getAutomaton().getTransitions().add(new FSATransition(getFrom(), (State) o));
			if (myPanel.addTransition(getFrom(), (State) o)) {
				myPanel.addDependency();
				myController.updateDependencies();
			}
		}
		clear();
	}

}
