package view.automata;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import debug.JFLAPDebug;

import model.automata.State;
import model.automata.Transition;
import model.graph.Graph;
import model.graph.TransitionGraph;
import universe.preferences.JFLAPPreferences;
import util.JFLAPConstants;
import util.arrows.CurvedArrow;
import util.view.GraphHelper;

/**
 * Automaton Drawer that allows for and keeps track of selection of States,
 * Arrows, and Transitions and draws them accordingly.
 * 
 * @author Ian McMahon, Lawrence Lin
 */
public class SelectionAutomatonDrawer<T extends Transition<T>> extends
		AutomatonDrawer<T> {

	private Set<State> mySelectedStates;
	private Set<State[]> mySelectedEdges;
	private Set<T> mySelectedTrans;
	private Set<Note> mySelectedNotes;
	private Font font = JFLAPConstants.DEFAULT_FONT;

	public SelectionAutomatonDrawer(StateDrawer vDraw) {
		super(vDraw);
		mySelectedStates = new TreeSet<State>();
		mySelectedEdges = new HashSet<State[]>();
		mySelectedTrans = new TreeSet<T>();
		mySelectedNotes = new HashSet<Note>();
	}

	@Override
	public void drawVertex(State v, Graph<State> obj, Graphics g) {
		TransitionGraph<T> graph = (TransitionGraph<T>) obj;
		// This line will allow for color to change if preferences are modified.
		getVertexDrawer().setInnerColor(JFLAPPreferences.getStateColor());

		if (isSelected(v))
			drawSelectedVertex(v, obj, g);
		else
			super.drawVertex(v, obj, g);

	}

	@Override
	public void drawEdge(State from, State to, Graph<State> obj, Graphics g) {
		Color current = g.getColor();
		g.setColor(JFLAPPreferences.getTransitionColor());

		if (isSelected(new State[] { from, to }))
			drawSelectedEdge(from, to, obj, g);
		else
			super.drawEdge(from, to, obj, g);
		g.setColor(current);
	}

	@Override
	public void drawLabel(Graphics2D g2d, T t, TransitionGraph<T> graph,
			Point2D center) {
		g2d.setFont(font);
		if (isSelected(t)) {
			Color oldColor = g2d.getColor();
			g2d.setColor(JFLAPPreferences.getSelectedTransitionColor());
			
			LabelBounds bounds = GraphHelper.getLabelBounds(graph, t, g2d);
			bounds.fill(g2d);
			
			g2d.setColor(oldColor);
		} else
			super.drawLabel(g2d, t, graph, center);
	}
	
	/**
	 * Change the size of the labels.
	 * @param mag
	 */
	public void magnifyLabel(double mag) {
		font = font.deriveFont((float) (JFLAPConstants.DEFAULT_FONT.getSize2D() * 2 * mag));
	}

	/** Selects or deselects the given object based on <CODE>select</CODE> */
	public boolean setSelected(Object o, boolean select) {
		if (o instanceof State)
			return select ? mySelectedStates.add((State) o) : mySelectedStates
					.remove((State) o);
		else if (o instanceof State[]){
			State[] edge = (State[]) o;
			if(select) return mySelectedEdges.add(edge);
			
			for(State[] e : mySelectedEdges)
				if(edge[0].equals(e[0]) && edge[1].equals(e[1]))
					return mySelectedEdges.remove(e);
		}
		else if (o instanceof Transition)
			return select ? mySelectedTrans.add((T) o) : mySelectedTrans
					.remove((T) o);
			else if (o instanceof Note)
				return select ? mySelectedNotes.add((Note) o) : mySelectedNotes.remove((Note) o);
		return false;
	}

	/** Returns true if the given Object is selected. */
	public boolean isSelected(Object o) {
		if (o instanceof State)
			return mySelectedStates.contains((State) o);
		else if (o instanceof State[]) {
			for (State[] array : mySelectedEdges)
				if (array[0].equals(((State[]) o)[0])
						&& array[1].equals(((State[]) o)[1]))
					return true;
		} else if (o instanceof Transition)
			return mySelectedTrans.contains((T) o);
		else if (o instanceof Note)
			return mySelectedNotes.contains((Note) o);
		return false;
	}

	/** Deselects all objects. */
	public void clearSelection() {
		mySelectedStates.clear();
		mySelectedEdges.clear();
		mySelectedTrans.clear();
		mySelectedNotes.clear();
	}
	
	public Set<State> getSelectedStates(){
		return mySelectedStates;
	}
	
	public Set<T> getSelectedTransitions(){
		return mySelectedTrans;
	}
	
	public Set<State[]> getSelectedEdges(){
		return mySelectedEdges;
	}
	
	public Set<Note> getSelectedNotes() {
		return mySelectedNotes;
	}

	/** Draws the vertex as the selected state color, as set in Preferences. */
	private void drawSelectedVertex(State v, Graph<State> obj, Graphics g) {
		StateDrawer vDraw = (StateDrawer) getVertexDrawer();
		vDraw.setInnerColor(JFLAPPreferences.getSelectedStateColor());
		super.drawVertex(v, obj, g);
		vDraw.setInnerColor(JFLAPPreferences.getStateColor());
	}

	private void drawSelectedEdge(State from, State to, Graph<State> obj,
			Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setStroke(new BasicStroke(6.0f));
		g2.setColor(JFLAPPreferences.getSelectedTransitionColor());
		CurvedArrow arrow = GraphHelper.getArrow(from, to, obj);
		arrow.draw(g2);
		g2.dispose();
	}

//	/** Draws the control point p. */
//	private void drawPoint(Graphics g, Point2D p) {
//		g.drawOval((int) p.getX() - JFLAPConstants.CONTROL_POINT_RADIUS,
//				(int) p.getY() - JFLAPConstants.CONTROL_POINT_RADIUS,
//				JFLAPConstants.CONTROL_POINT_RADIUS * 2,
//				JFLAPConstants.CONTROL_POINT_RADIUS * 2);
//	}
}
