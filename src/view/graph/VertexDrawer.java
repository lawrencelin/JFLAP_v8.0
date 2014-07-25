package view.graph;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

import debug.JFLAPDebug;


import universe.preferences.JFLAPPreferences;
import util.ColoredStroke;
import util.JFLAPConstants;

public class VertexDrawer<T>{

	private int myRadius;
	private Font myFont;
	private Color myColor;
	private ColoredStroke myOutlineStroke;


	public VertexDrawer(){
		myOutlineStroke = new ColoredStroke(1, Color.black);
		setRadius(JFLAPConstants.STATE_RADIUS);
		setInnerColor(JFLAPPreferences.getStateColor());
		setBorderColor(Color.black);
		setBorderWidth(1);
		setFont(JFLAPConstants.DEFAULT_FONT);
	}
	
	private void setFont(Font f) {
		myFont = f;
	}

	private void setBorderWidth(int i) {
		myOutlineStroke = new ColoredStroke(i, myOutlineStroke.getColor());
	}

	public void setRadius(int r){
		myRadius = r;
	}

	public void setInnerColor(Color c){
		myColor=c;
	}

	public void setBorderColor(Color c){
		myOutlineStroke.setColor(c);
	}

	public void draw(Point2D p, T obj, Graphics g){
		this.draw(p.getX(), p.getY() , obj, g);
	}

	public void draw(double x, double y, T obj, Graphics g) {
		Rectangle r = createRectangle((int)x,(int)y, myRadius);
		Graphics g2 = g.create();
		// Draw the basic background of the state.
		g2.setColor(myColor);
		fillShape(g2, r);
		myOutlineStroke.apply((Graphics2D) g2);
		drawShape(g2, r);
//		drawVertexLabel(x, y, obj, g);

	}
	
	private void drawVertexLabel(double x, double y, T obj, Graphics g) {
		g.drawString(obj.toString(), (int)x, (int)y);
	}
	
	public void fillShape(Graphics g, Rectangle r){
		g.fillOval(r.x, r.y, r.width, r.height);
	}
	
	public void drawShape(Graphics g, Rectangle r){
		g.drawOval(r.x, r.y, r.width, r.height);
	}

	private Rectangle createRectangle(int x, int y, int radius) {
		return new Rectangle(x - radius, y - radius, 2 * radius,
				2 * radius);
	}

	public double getVertexRadius() {
		return myRadius;
	}
	
	/**
	 * Change the radius of the vertex. 
	 * Called when the magnification scrolling bar is changed. 
	 * @param mag
	 */
	public void changeRadius(double mag) {
		myRadius = (int) (JFLAPConstants.STATE_RADIUS * 2 * mag);
	}
	
	public void changeFontSize(double mag) {
			myFont = myFont.deriveFont((float) (JFLAPConstants.DEFAULT_FONT.getSize2D() * 2 * mag));
	}
	
	public Font getFont() {
		return myFont;
	}

}
