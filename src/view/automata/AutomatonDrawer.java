package view.automata;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.List;

import debug.JFLAPDebug;
import model.automata.Automaton;
import model.automata.State;
import model.automata.Transition;
import model.automata.acceptors.Acceptor;
import model.graph.Graph;
import model.graph.TransitionGraph;
import util.arrows.GeometryHelper;
import util.view.GraphHelper;
import view.graph.GraphDrawer;

public class AutomatonDrawer<T extends Transition<T>> extends
		GraphDrawer<State> {

	public AutomatonDrawer(StateDrawer vDraw) {
		super(vDraw);
	}

	@Override
	public void drawVertex(State v, Graph<State> obj, Graphics g) {
		TransitionGraph<T> graph = (TransitionGraph<T>) obj;
		Automaton<T> auto = graph.getAutomaton();

		boolean isFinal = false;
		if (auto instanceof Acceptor)
			isFinal = Acceptor.isFinalState((Acceptor) auto, v);
		boolean isInitial = v.equals(auto.getStartState());

		drawVertex(v, obj, g, isFinal, isInitial);
	}

	@Override
	public void drawLabel(State from, State to, Graph<State> obj, Graphics g) {
		TransitionGraph<T> graph = (TransitionGraph<T>) obj;
		List<T> transitions = graph.getOrderedTransitions(from, to);
		Point2D pFrom = graph.pointForVertex(from);
		Point2D pTo = graph.pointForVertex(to);
		drawLabels(transitions, g, graph, pFrom, pTo);
		// Point2D ctrl = graph.getControlPt(from, to);
		// drawPoint(g, ctrl);
	}

	public void drawLabel(Graphics2D g2d, T t, TransitionGraph<T> obj,
			Point2D center) {
		String label = GraphHelper.getLabelText(obj, t);
		FontMetrics metrics = g2d.getFontMetrics();
		int w = metrics.stringWidth(label);
		int h = metrics.getMaxAscent();
		int x = (int) (center.getX() - w / 2);
		int y = (int) (center.getY() + h / 2);
		g2d.drawString(label, x, y);
	}

	private void drawVertex(State v, Graph<State> obj, Graphics g,
			boolean isFinal, boolean isInitial) {
		StateDrawer sDraw = (StateDrawer) getVertexDrawer();
		sDraw.draw(obj.pointForVertex(v), v, g, isFinal, isInitial);
	}

	private void drawLabels(List<T> transitions, Graphics g,
			TransitionGraph<T> graph, Point2D pFrom, Point2D pTo) {
		// draw Labels
		Graphics2D g2d = (Graphics2D) g.create();
		AffineTransform oldTX = g2d.getTransform();
		for (int i = 0; i < transitions.size(); i++) {
			T t = transitions.get(i);
			// set up transform
			Point2D center = graph.getLabelCenter(t);

			g2d.setTransform(oldTX);
			if (!t.isLoop()) {
				AffineTransform tx = createLabelTransform(center, pFrom, pTo);
				g2d.transform(tx);
			}
			// drawLabel
			drawLabel(g2d, t, graph, center);
		}
	}

	private AffineTransform createLabelTransform(Point2D center, Point2D from,
			Point2D to) {
		AffineTransform at = new AffineTransform();
		double angle = (GeometryHelper.calculateAngle(from, to) - (GeometryHelper
				.getXDisplacement(from, to) < 0 ? Math.PI : 0)) % (2 * Math.PI);
		// The subtraction is due to the fact that calculateAngle needs it in
		// most cases,
		// but causes strange text rotation in this case.

		at.rotate(angle % Math.PI, center.getX(), center.getY());
		return at;
	}

}
