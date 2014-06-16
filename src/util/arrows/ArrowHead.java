package util.arrows;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;


public class ArrowHead{

	private double myArrowAngle,
					 myLength,
					 myDirection;
	private Point2D myLocation;

	public ArrowHead(Point2D pTo, double direction, double headLength, double headAngle) {
		this(headLength, headAngle);
		setLocation(pTo);
		setDirection(direction);
	}

	public ArrowHead(double headLength, double headAngle) {
		setHeadLength(headLength);
		setArrowAngle(headAngle);
	}

	public void setHeadLength(double length){
		myLength = length;
	}
	
	public void setDirection(double dir){
		myDirection = dir;
	}
	
	public void setArrowAngle(double angle){
		myArrowAngle = angle;
	}
	public void setLocation(Point2D p2){
		myLocation = p2;
	}
	
	public void draw(Graphics g) {
		if (myLength<=0) return;
		drawPiece(g, -myDirection + myArrowAngle);
		drawPiece(g, -myDirection - myArrowAngle);
	}

	private void drawPiece(Graphics g, double angle) {
		int endX, endY;
		endX = (int) (Math.sin(angle) * myLength + myLocation.getX());
		endY = (int) (Math.cos(angle) * myLength + myLocation.getY());
		g.drawLine((int)myLocation.getX(), (int) myLocation.getY(), endX, endY);
	}

}
