package model.graph;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import util.arrows.GeometryHelper;

public class ControlPoint extends java.awt.geom.Point2D {

	private Point2D myTo,myFrom;
	private double myX,myY,myHeight,myRatio;

	public ControlPoint(Point2D ctrl, Point2D from, Point2D to) {
		myX = ctrl.getX();
		myY = ctrl.getY();
		myTo=new Point2D.Double(to.getX(), to.getY());
		myFrom=new Point2D.Double(from.getX(), from.getY());
		updateConstants();
	}

	@Override
	public double getX() {
		return myX;
	}

	@Override
	public double getY() {
		return myY;
	}

	@Override
	public void setLocation(double x, double y) {
		myX=x;
		myY=y;
		updateConstants();
	}

	public void setFrom(double x, double y){
		myFrom.setLocation(x,y);
		updateLocation();
	}
	

	public void setTo(double x, double y){
		myTo.setLocation(x,y);
		updateLocation();
	}
	
	public void setAll(double x, double y){
		myFrom.setLocation(x, y);
		myTo.setLocation(x, y);
	}

	private void updateConstants() {
		//Preparation
		double fx = myFrom.getX();
		double fy = myFrom.getY();
		double tx = myTo.getX();
		double ty = myTo.getY();
		//Vector from from->ctrlpt
		double[] vecP = new double[]{myX-fx,myY-fy};
		//Vector from from->to
		double[] vecT = new double[]{tx-fx,ty-fy};
		//Magnitude of vecT
		double magT = Math.sqrt(Math.pow(vecT[0], 2) + Math.pow(vecT[1], 2) );
		//unit vector of vecT
		double[] unitVecT = new double[]{vecT[0]/magT, vecT[1]/magT};
		//transition length
		double transLength = myFrom.distance(myTo);
		//the magnitude of projection of vecP onto vecT
		double fromProjMag =  vecP[0]*unitVecT[0] + vecP[1]*unitVecT[1];
		//ratio of projection to total tansition length
		myRatio=fromProjMag/transLength;
		//prepping equadistant points
		Point above = new Point((int)((fx+tx)/2.0), (int) ((fy+ty)/2.0));
		Point below = new Point(above);
	
		//we know that VecT runs along the transition, thus the two points
		//are translated perpendicularly, one above, one below.
		above.translate((int)(-1*vecT[1]), (int) vecT[0]);
		below.translate((int)vecT[1], (int) (-1*vecT[0]));
	
		boolean isAbove = above.distance(myX,myY) < below.distance(myX, myY);
		//if above, remains the same. Otherwise, inverted
		int mul =  isAbove ? 1 : -1;
	
		myHeight= mul * Line2D.ptLineDist(fx, fy, tx, ty, myX, myY);
	}

	private void updateLocation() {
		double perp_angle = GeometryHelper.calculatePerpendicularAngle(myFrom,myTo);

		//retrieve the point on the line closest to the ctrl point
		double x1 = myFrom.getX();
		double y1 = myFrom.getY();
		double x2 = myTo.getX();
		double y2 = myTo.getY();

		double xClose = ((x2-x1)*myRatio) + x1;
		double yClose = ((y2-y1)*myRatio) + y1;

		double dx = myHeight * Math.cos(perp_angle);
		double dy = myHeight * Math.sin(perp_angle);

		myX = xClose+dx;
		myY = yClose+dy;
	}

	public Point2D toBasicPoint() {
		return new Point2D.Double(myX,myY);
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return new Point2D.Double(myX, myY).toString();
	}

}
