package view.automata.tools.algorithm;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Set;

import javax.swing.SwingUtilities;

import model.algorithms.conversion.regextofa.RegularExpressionToNFAConversion;
import model.automata.State;
import model.automata.Transition;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import util.Point2DAdv;
import view.algorithms.conversion.regextofa.RegularExpressionToFAPanel;
import view.automata.editing.AutomatonEditorPanel;
import view.automata.tools.EditingTool;

public class DeexpressionTransitionTool extends
		EditingTool<FiniteStateAcceptor, FSATransition> {


	private RegularExpressionToFAPanel myDisplay;

	public DeexpressionTransitionTool(RegularExpressionToFAPanel panel) {
		super(panel.getEditorPanel());
		myDisplay = panel;
	}

	@Override
	public String getToolTip() {
		return "De-expressionify Transition";
	}

	@Override
	public char getActivatingKey() {
		return 'd';
	}

	@Override
	public String getImageURLString() {
		return "/ICON/de-expressionify.gif";
	}

	@Override
	public void mousePressed(MouseEvent e) {
		super.mousePressed(e);
		if (SwingUtilities.isLeftMouseButton(e)) {
			AutomatonEditorPanel<FiniteStateAcceptor, FSATransition> panel = getPanel();
			
			Object o = panel.objectAtPoint(e.getPoint());
			if(o instanceof FSATransition){
				Set<FSATransition> existingTransitions = panel.getAutomaton().getTransitions().toCopiedSet();
				
				myDisplay.beginDeExpressionify((FSATransition) o);
				
				Set<FSATransition> addedT = panel.getAutomaton().getTransitions().toCopiedSet();
				addedT.removeAll(existingTransitions);
				
				myDisplay.replaceTransition((FSATransition) o, addedT);
			}
		}

	}
}
