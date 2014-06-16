package util;

import java.awt.geom.Point2D;

public class Point2DAdv extends Point2D.Double{

	public Point2DAdv() {
		this(0,0);
	}

	public Point2DAdv(double x, double y) {
		super(x, y);
	}

	public Point2DAdv(Point2D p){
		this(p.getX(), p.getY());
	}
	
	public void translate(double dx, double dy){
		this.setLocation(this.getX()+dx, this.getY()+dy);
	}
	
	
}
