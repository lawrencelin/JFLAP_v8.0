package util.view;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import model.automata.Automaton;
import model.automata.State;
import model.automata.Transition;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.transducers.OutputFunctionSet;
import model.automata.transducers.mealy.MealyMachine;
import model.automata.transducers.mealy.MealyOutputFunction;
import model.graph.Graph;
import model.graph.TransitionGraph;
import model.symbols.SymbolString;
import universe.preferences.JFLAPPreferences;
import util.JFLAPConstants;
import util.Point2DAdv;
import util.arrows.CurvedArrow;
import util.arrows.GeometryHelper;
import view.automata.AutomatonDrawer;
import view.automata.LabelBounds;

public class GraphHelper implements JFLAPConstants{
	private static double magnification = 0.5;
	
	public static void setMagnification(double mag) {
		magnification = mag;
	}
	/**
	 * Reforms the points so they are enclosed within a certain frame.
	 * 
	 * @param <T>
	 */
	public static <T> void moveWithinFrame(Graph<T> g, Rectangle2D bounds) {
		Object[] vertices = g.vertices().toArray();
		if (vertices.length == 0)
			return;
		Point2D p = g.pointForVertex((T) vertices[0]);
		double minx = p.getX(), miny = p.getY(), maxx = minx, maxy = miny;
		for (int i = 1; i < vertices.length; i++) {
			p = g.pointForVertex((T) vertices[i]);
			minx = Math.min(minx, p.getX());
			miny = Math.min(miny, p.getY());
			maxx = Math.max(maxx, p.getX());
			maxy = Math.max(maxy, p.getY());
		}

		minx -= STATE_RADIUS + 5;
		miny -= STATE_RADIUS + 5;
		maxx += STATE_RADIUS + 5;
		maxy += STATE_RADIUS + 5;
		// Now, scale them!
		for (int i = 0; i < vertices.length; i++) {
			p = g.pointForVertex((T) vertices[i]);
			p = new Point2D.Double((p.getX() - minx) * bounds.getWidth()
					/ (maxx - minx) + bounds.getX(), (p.getY() - miny)
					* bounds.getHeight() / (maxy - miny) + bounds.getY());
			g.moveVertex((T) vertices[i], p);
		}
	}

	public static <S extends Transition<S>> Point2D calculateCenterPoint(
			TransitionGraph<S> graph, S trans) {
		State from = trans.getFromState(), to = trans.getToState();

		if (graph.hasEdge(from, to)) {
			List<S> order = graph.getOrderedTransitions(from, to);
			return graph.getLabelCenterPoint(trans, order.size(), from, to);
		}

		Point2D pFrom = graph.pointForVertex(from), pTo = graph
				.pointForVertex(to);
		Point2D ctrl = graph.getDefaultControlPoint(from, to);
		return GeometryHelper.getCurveCenter(pFrom, ctrl, pTo);
	}

	/**
	 * Returns the bounding rectangle for the text component of the specified
	 * transition based off the font of the graphics.
	 */
	public static <S extends Transition<S>> LabelBounds getLabelBounds(
			TransitionGraph<S> graph, S trans, Graphics g) {
		Point2D pFrom = graph.pointForVertex(trans.getFromState());
		Point2D pTo = graph.pointForVertex(trans.getToState());
		Point2D center = graph.getLabelCenter(trans);

		double angle = pFrom.equals(pTo) ? 0 : GeometryHelper.calculateAngle(
				pFrom, pTo);
		// calculate bounds
		FontMetrics metrics = g.getFontMetrics();
		String label = getLabelText(graph, trans);
		int w = metrics.stringWidth(label);
		int h = metrics.getMaxAscent();
		int x = (int) (center.getX() - w / 2);
		int y = (int) (center.getY() - h / 2);

		return new LabelBounds(angle, new Rectangle(x, y, w, h));
	}
	
	public static <T extends Transition<T>> String getLabelText(TransitionGraph<T> obj, T t) {
		Automaton<T> auto = obj.getAutomaton();
		String label = t.getLabelText();
		if(auto instanceof MealyMachine){
			OutputFunctionSet<MealyOutputFunction> funcs = ((MealyMachine) auto).getOutputFunctionSet();
			SymbolString out = funcs.getOutputForTransition((FSATransition) t);
			label += " ; "+ (out == null ? JFLAPPreferences.getEmptyString() : out.toString());
		}
		return label;
	}

	/**
	 * Returns a point that is located at the max X and max Y value of either
	 * the given point or a State boundary.
	 */
	public static Point2D getOnscreenPoint(boolean isStartState, Point2D p) {
		double xBounds = STATE_RADIUS + 5;
		double yBounds = xBounds;

		if (isStartState)
			xBounds += STATE_RADIUS;

		double x = Math.max(p.getX(), xBounds);
		double y = Math.max(p.getY(), yBounds);
		return new Point2D.Double(x, y);
	}
	
	public static <T extends Transition<T>> Point2D getMinPoint(TransitionGraph<T> graph, Graphics g){
		Automaton<T> auto = graph.getAutomaton();
		double minx = 0, miny = 0;
		int radius = STATE_RADIUS;
		
		for(State vert : auto.getStates()){
			Point2D p = graph.pointForVertex(vert);
			minx = Math.min(minx, p.getX() - (Automaton.isStartState(auto, vert) ? 2*radius + 5 : radius + 5));
			miny = Math.min(miny, p.getY() - (radius + 5));
		}
		for(T trans : auto.getTransitions()){
			CurvedArrow arrow = getArrow(trans.getFromState(), trans.getToState(), graph);
			Rectangle2D arrowBounds = arrow.getCurveBounds();
			
			minx = Math.min(minx, arrowBounds.getMinX());
			miny = Math.min(miny, arrowBounds.getMinY());
			
			if(g != null){
				LabelBounds bounds = getLabelBounds(graph, trans, g);
				minx = Math.min(minx, bounds.getMinX());
				miny = Math.min(miny, bounds.getMinY());
			}
		}
		return new Point2DAdv(minx, miny);
	}
	
	public static <T extends Transition<T>> Point2D getMaxPoint(TransitionGraph<T> graph, Graphics g){
		Automaton<T> auto = graph.getAutomaton();
		double maxx = 0, maxy = 0;
		int radius = STATE_RADIUS;
		
		for(State vert : auto.getStates()){
			Point2D p = graph.pointForVertex(vert);
			maxx = Math.max(maxx, p.getX() + radius + 5);
			maxy = Math.max(maxy, p.getY() + radius + 5);
		}
		for(T trans : auto.getTransitions()){
			CurvedArrow arrow = getArrow(trans.getFromState(), trans.getToState(), graph);
			Rectangle2D arrowBounds = arrow.getCurveBounds();
			
			maxx = Math.max(maxx, arrowBounds.getMaxX());
			maxy = Math.max(maxy, arrowBounds.getMaxY());
			
			if(g != null){
				LabelBounds bounds = getLabelBounds(graph, trans, g);
				maxx = Math.max(maxx, bounds.getMaxX());
				maxy = Math.max(maxy, bounds.getMaxY());
			}
		}
		return new Point2DAdv(maxx, maxy);
	}
	
	public static <T> CurvedArrow getArrow(T from, T to, Graph<T> obj) {
		Point2D pFrom = obj.pointForVertex(from);
		Point2D pTo = obj.pointForVertex(to);
		Point2D ctrl = obj.getControlPt(from,to);
		
		double rad = STATE_RADIUS * 2 * magnification;
		double theta1 = GeometryHelper.calculateAngle(pFrom, pTo),
				theta2=GeometryHelper.calculateAngle(pTo, pFrom);
		if (from.equals(to)){
			theta1=-Math.PI/4;
			theta2=-3*Math.PI/4;
		}
			
		Point2D edgeFrom = GeometryHelper.pointOnCircle(pFrom,rad,theta1);
		Point2D edgeTo = GeometryHelper.pointOnCircle(pTo,rad,theta2);
		
		double arrowheadLen = 0;
		if (obj.isDirected()) arrowheadLen=ARROW_LENGTH * 2 * magnification;
		CurvedArrow curve = new CurvedArrow(arrowheadLen, ARROW_ANGLE);
		curve.setCurve(edgeFrom, ctrl, edgeTo);
		return curve;
	}

}
