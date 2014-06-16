package view.automata.tools;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

import javax.swing.SwingUtilities;

import model.automata.Automaton;
import model.automata.AutomatonFunction;
import model.automata.State;
import model.automata.Transition;
import util.JFLAPConstants;
import util.arrows.GeometryHelper;
import view.automata.editing.AutomatonEditorPanel;

public class TransitionTool<T extends Automaton<S>, S extends Transition<S>>
		extends EditingTool<T, S> {

	private State from;
	private Point2D pCurrent;
	private Point2D pFrom;

	public TransitionTool(AutomatonEditorPanel<T, S> panel) {
		super(panel);
		from = null;
		pCurrent = pFrom = null;

	}

	@Override
	public String getToolTip() {
		return "Transition Creator";
	}

	@Override
	public char getActivatingKey() {
		return 't';
	}

	@Override
	public String getImageURLString() {
		return "/ICON/transition.gif";
	}

	@Override
	public void mousePressed(MouseEvent e) {
		AutomatonEditorPanel<T, S> panel = getPanel();
		Object obj = panel.objectAtPoint(e.getPoint());

		if (SwingUtilities.isLeftMouseButton(e)) {
			if (obj instanceof State) {
				from = (State) obj;
				pFrom = pCurrent = panel.getPointForVertex(from);
			}
		}
		super.mousePressed(e);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (hasFrom()) {
			pCurrent = e.getPoint();
			getPanel().repaint();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (hasFrom()) {
			AutomatonEditorPanel<T, S> panel = getPanel();
			Object obj = panel.objectAtPoint(e.getPoint());
			
			if (obj instanceof State) {
				S trans = createTransition(panel, obj);
				panel.editTransition(trans, true);
			}
			clear();
		}
	}

	public S createTransition(AutomatonEditorPanel<T, S> panel, Object obj) {
		return panel.createTransition(from, (State) obj);
	}

	public void clear() {
		from = null;
		getPanel().repaint();
	}

	@Override
	public void draw(Graphics g) {
		if (hasFrom()) {
			Graphics2D g2 = (Graphics2D) g;
			Stroke s = g2.getStroke();
			
			g2.setStroke(JFLAPConstants.DEFAULT_TRANSITION_STROKE);
			g2.setColor(JFLAPConstants.DEFAULT_TRANS_TOOL_COLOR);
			
			g2.drawLine((int) pFrom.getX(), (int) pFrom.getY(),
					(int) pCurrent.getX(), (int) pCurrent.getY());
			g2.setStroke(s);
		}
	}

	public boolean hasFrom() {
		return from != null;
	}
	
	public State getFrom() {
		return from;
	}
}
