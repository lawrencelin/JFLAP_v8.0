package view.automata;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import model.automata.State;
import util.ColoredStroke;
import util.JFLAPConstants;
import view.graph.VertexDrawer;

public class StateDrawer extends VertexDrawer<State> {

	private ColoredStroke myTextStroke;
	
	public StateDrawer(){
		myTextStroke = new ColoredStroke(1, Color.black);
	}
	
	public void setTextColor(Color c){
		myTextStroke.setColor(c);
	}
	
	@Override
	public void draw(double x, double y, State obj, Graphics g) {
		super.draw(x, y, obj, g);
		g = g.create();
		
		int dx = ((int) g.getFontMetrics().getStringBounds(obj.getName(), g)
				.getWidth()) >> 1;
		int dy = ((int) g.getFontMetrics().getAscent()) >> 1;
		
		myTextStroke.apply((Graphics2D) g);
		g.setFont(getFont());
		g.drawString(obj.getName(),(int) x - dx, (int) (y + dy));
	}
	
	/** Draws the State depending on whether it is the Start and/or a Final State in the graph. */
	public void draw (Point2D point2d, State obj, Graphics g, boolean isFinal, boolean isInitial) {
		this.draw(point2d.getX(), point2d.getY(), obj, g, isFinal, isInitial);
	}
	
	/** Draws the State depending on whether it is the Start and/or a Final State in the graph. */
	private void draw (double x, double y, State obj, Graphics g, boolean isFinal, boolean isInitial){
		this.draw(x, y, obj, g);
		int radius = (int) getVertexRadius();
		
		if (isFinal)
			drawFinal(x - radius + 3, y - radius + 3, g, (radius - 3) * 2);
		// If this is the initial state.
		if (isInitial) {
			int[] xPoly = { (int) x - radius, (int) x - (radius << 1),
					(int) x - (radius << 1) };
			int[] yPoly = { (int) y, (int) y - radius, (int) y + radius };
			g.setColor(Color.white);
			g.fillPolygon(xPoly, yPoly, 3);
			g.setColor(Color.black);
			g.drawPolygon(xPoly, yPoly, 3);
		}
	}

	public void drawFinal(double x, double y, Graphics g, int radius) {
		g.drawOval((int) x, (int) y,
				radius, radius);
	}
}
