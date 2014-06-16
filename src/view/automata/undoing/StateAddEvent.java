package view.automata.undoing;

import java.awt.geom.Point2D;

import model.automata.Automaton;
import model.automata.State;
import model.change.events.AddEvent;
import util.JFLAPConstants;
import util.view.GraphHelper;
import view.automata.editing.AutomatonEditorPanel;

/**
 * Undoing Event for creating a state at a specific point so that when undone
 * and redone, the state returns to that point (or a boundary, if it was
 * initially out of bounds).
 * 
 * @author Ian McMahon
 * 
 */
public class StateAddEvent extends AddEvent<State> {

	private State myState;
	private Point2D myPoint;
	private AutomatonEditorPanel myPanel;
	private Automaton myDefinition;

	public StateAddEvent(AutomatonEditorPanel panel, Automaton definition,
			State vertex, Point2D p) {
		super(definition.getStates(), vertex);
		myPanel = panel;
		myDefinition = definition;
		myState = vertex;
		myPoint = p;
	}
	
	@Override
	public boolean undo() {
		myPanel.clearSelection();
		return super.undo();
	}

	@Override
	public boolean redo() {
		boolean sup = super.redo();
		myPoint = GraphHelper.getOnscreenPoint(myState.equals(myDefinition.getStartState()), myPoint);
		myPanel.clearSelection();
		myPanel.moveState(myState, myPoint);
		return sup;
	}
}
