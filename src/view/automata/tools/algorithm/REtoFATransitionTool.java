package view.automata.tools.algorithm;

import java.awt.event.MouseEvent;

import model.algorithms.conversion.regextofa.RegularExpressionToNFAConversion;
import model.automata.State;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import view.automata.editing.AutomatonEditorPanel;
import view.automata.tools.TransitionTool;

public class REtoFATransitionTool extends TransitionTool<FiniteStateAcceptor, FSATransition>{

	private RegularExpressionToNFAConversion myAlg;

	public REtoFATransitionTool(
			AutomatonEditorPanel<FiniteStateAcceptor, FSATransition> panel, RegularExpressionToNFAConversion convert) {
		super(panel);
		myAlg = convert;
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		if (getFrom() == null)
			return;
		Object o = getPanel().objectAtPoint(e.getPoint());
		if (o instanceof State) {
			myAlg.addLambdaTransition(getFrom(), (State) o);
		}
		clear();
	}
}
