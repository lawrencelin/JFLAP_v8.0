package view.automata.undoing;

import java.awt.Panel;
import java.awt.geom.Point2D;
import java.util.List;

import debug.JFLAPDebug;
import util.Point2DAdv;
import view.automata.editing.AutomatonEditorPanel;
import model.automata.State;
import model.graph.ControlPoint;
import model.undo.IUndoRedo;

/**
 * Undo event for moving the ControlPoint of an edge in a TransitionGraph.
 * @author Ian McMahon
 *
 */
public class ControlMoveEvent extends ClearSelectionEvent{

	private State myFrom;
	private State myTo;
	private Point2D pFrom;
	private Point2D pTo;

	public ControlMoveEvent(AutomatonEditorPanel panel, State[] states, Point2D origin, Point2D dest){
		super(panel);
		myFrom = states[0];
		myTo = states[1];
		pFrom = origin;
		pTo = dest;
	}
	
	@Override
	public boolean undo() {
		setLocation(pFrom);
		return super.undo();
	}

	@Override
	public boolean redo() {
		setLocation(pTo);
		return super.undo();
	}
	
	private void setLocation(Point2D point){
		getPanel().moveCtrlPoint(myFrom, myTo, point);
	}

	@Override
	public String getName() {
		return "Control Point Moved";
	}
	
}
