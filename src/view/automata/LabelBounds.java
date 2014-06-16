package view.automata;

import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import debug.JFLAPDebug;

import util.arrows.GeometryHelper;


public class LabelBounds implements Shape {

	private double myAngle;
	private Rectangle myRectangle;
	
	public LabelBounds(double angle, Rectangle r){
		myAngle = angle;
		myRectangle = r;
	}
	
	@Override
	public boolean contains(Point2D o) {
		Point2D rotated = rotatePoint(o);
		return myRectangle.contains(rotated);
	}

	public Point2D getCenter() {
		return new Point2D.Double(myRectangle.getCenterX(),myRectangle.getCenterY());
	}

	@Override
	public boolean contains(Rectangle2D r) {
		for (Point2D p : GeometryHelper.getCorners(r)){
			if (!this.contains(p)) return false;
		}
		
		return true;
	}

	@Override
	public boolean contains(double x, double y) {
		 return this.contains(new Point2D.Double(x, y));
	}

	@Override
	public boolean contains(double x, double y, double w, double h) {
		return this.contains(new Rectangle2D.Double(x,y,w,h));
	}

	@Override
	public Rectangle getBounds() {
		return getBounds2D();
	}

	@Override
	public Rectangle getBounds2D() {
		Point2D[] corners = this.getCorners();
		double maxX = GeometryHelper.getMaxX(corners),
				minX = GeometryHelper.getMinX(corners),
				maxY = GeometryHelper.getMaxY(corners),
				minY = GeometryHelper.getMinY(corners);
		return new Rectangle((int)minX,(int)minY,(int)(maxX-minX),(int)(maxY-minY));
	}

	public double getMinY() {
		return GeometryHelper.getMinY(this.getCorners());
	}

	public double getMaxY() {
		return GeometryHelper.getMaxY(this.getCorners());
	}

	public double getMinX() {
		return GeometryHelper.getMinX(this.getCorners());
	}

	public double getMaxX() {
		return GeometryHelper.getMaxX(this.getCorners());
	}

	private Point2D[] getCorners() {
		Point2D[] corners = new Point2D[4];
		Point2D[] temp = GeometryHelper.getCorners(myRectangle);
		for (int i = 0; i< corners.length; i++){
			corners[i]=this.rotatePoint(temp[i]);
		}
		return corners;
	}

	private Point2D rotatePoint(Point2D p) {
		return GeometryHelper.rotatePoint(p,getCenter(),myAngle);
	}
	

	@Override
	public PathIterator getPathIterator(AffineTransform arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PathIterator getPathIterator(AffineTransform arg0, double arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean intersects(Rectangle2D r) {
		for (Point2D p : GeometryHelper.getCorners(r)){
			if (this.contains(p)) return true;
		}
		return false;
	}

	@Override
	public boolean intersects(double x, double y, double w, double h) {
		return intersects(new Rectangle2D.Double(x, y, w, h));
	}
	
	public void draw(Graphics g){
		Point2D[] corners = GeometryHelper.getCorners(myRectangle);
		for (int i=0;i<4;i++){
			g.drawLine((int)corners[i].getX(), 
						(int)corners[i].getY(), 
						(int)corners[(i+1)%4].getX(), 
						(int)corners[(i+1)%4].getY());
			
		}
	}
	
	public void fill(Graphics g){
		Point2D[] corners = GeometryHelper.getCorners(myRectangle);
		int[] xs = new int[corners.length], ys = new int[corners.length];
		
		for(int i=0; i<corners.length; i++){
			xs[i] = (int) corners[i].getX();
			ys[i] = (int) corners[i].getY();
		}
		Polygon poly = new Polygon(xs, ys, xs.length);
		g.drawPolygon(poly);
		g.fillPolygon(poly);
	}

	public double getAngle() {
		return myAngle;
	}
	
	public Rectangle getRectangle() {
		return myRectangle;
	}
}
