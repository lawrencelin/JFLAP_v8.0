package view.automata;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

import model.automata.State;

public class BlockDrawer extends StateDrawer {

	@Override
	public void fillShape(Graphics g, Rectangle r) {
		g.fillRect(r.x, r.y, r.width, r.height);
	}
	
	@Override
	public void drawShape(Graphics g, Rectangle r) {
		g.drawRect(r.x, r.y, r.width, r.height);
	}
	
	@Override
	public void drawFinal(double x, double y, Graphics g, int radius) {
		g.drawRect((int) x, (int) y, radius, radius);
	}
	
	@Override
	public void draw(Point2D point2d, State obj, Graphics g, boolean isFinal,
			boolean isInitial) {
		// TODO Auto-generated method stub
		super.draw(point2d, obj, g, isFinal, isInitial);
	}
}
