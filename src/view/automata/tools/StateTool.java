package view.automata.tools;

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import javax.swing.SwingUtilities;

import model.automata.Automaton;
import model.automata.State;
import model.automata.Transition;
import util.Point2DAdv;
import view.automata.editing.AutomatonEditorPanel;
import view.automata.undoing.StateAddEvent;
import view.automata.undoing.StateMoveEvent;

public class StateTool<T extends Automaton<S>, S extends Transition<S>> extends
		EditingTool<T, S> {

	private State myState;
	private Point2D myPoint;
	private T myDef;

	public StateTool(AutomatonEditorPanel<T, S> panel, T def) {
		super(panel);
		myDef = def;
		myState = null;
		myPoint = null;
	}

	@Override
	public String getToolTip() {
		return "State Creator";
	}

	@Override
	public char getActivatingKey() {
		return 's';
	}

	@Override
	public String getImageURLString() {
		return "/ICON/state.gif";
	}

	@Override
	public void mousePressed(MouseEvent e) {
		super.mousePressed(e);
		AutomatonEditorPanel<T, S> panel = getPanel();
		
		//Left click, create a state, move it to mouse's location
		if (SwingUtilities.isLeftMouseButton(e)){
			myState = panel.createState(e.getPoint());
		}
		//Right click, allow for dragging other states
		else if (SwingUtilities.isRightMouseButton(e)) {
			Object o = panel.objectAtPoint(e.getPoint());
			if (o instanceof State){
				myState = (State) o;
			}
		}
		if(myState != null){
			panel.selectObject(myState);
			myPoint = new Point2DAdv(panel.getPointForVertex(myState));
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (myState != null)
			getPanel().moveState(myState, e.getPoint());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(myState != null){
			AutomatonEditorPanel<T, S> panel = getPanel();
			panel.clearSelection();
			
			if(SwingUtilities.isLeftMouseButton(e))
				panel.getKeeper().registerChange(new StateAddEvent(panel, myDef, myState, e.getPoint()));
			else if(!myPoint.equals(e.getPoint()))
				panel.getKeeper().registerChange(new StateMoveEvent(panel, myDef, myState, myPoint, e.getPoint()));
		}
		clearValues();
	}
	
	public void clearValues() {
		myState = null;
		myPoint = null;
	}
	
	public State getState() {
		return myState;
	}
	
	public void setState(State s) {
		myState = s;
	}
	
	public T getDef() {
		return myDef;
	}
}
