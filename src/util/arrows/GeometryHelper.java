package util.arrows;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.QuadCurve2D.Double;
import java.awt.geom.Rectangle2D;


import debug.JFLAPDebug;

import util.Point2DAdv;




public class GeometryHelper {

	public static Point2D rotatePoint(Point2D p, Point2D center, double angle){
		AffineTransform at = new AffineTransform();
		at.rotate(angle, center.getX(), center.getY());
		Point2D rotated = at.transform(p,null);
		return rotated;
	}

	public static Point2D[] getCorners(Rectangle2D r){
		double x = r.getX(), y = r.getY(), w = r.getWidth(), h = r.getHeight();
		Point2D[] corners = new Point2D[]{new Point2D.Double(x,y),
				new Point2D.Double(x+w,y),
				new Point2D.Double(x+w,y+h),
				new Point2D.Double(x,y+h)};
		return corners;
	}

	public static double calculateAngle(CurvedArrow arrow) {
		return calculateAngle(arrow.getP1(), arrow.getP2());
	}

	public static double getYDisplacement(CurvedArrow arrow) {
		return getYDisplacement(arrow.getP1(), arrow.getP2());
	}

	public static double getXDisplacement(CurvedArrow arrow) {
		return getXDisplacement(arrow.getP1(), arrow.getP2());
	}

	public static double calculateAngle(Point2D p1, Point2D p2) {
		double 	dy=getYDisplacement(p1, p2),
				dx=getXDisplacement(p1, p2);
		double theta = Math.atan(dy/dx) + (dx < 0 ? Math.PI :0);
		return theta;
	}

	public static double getYDisplacement(Point2D p1, Point2D p2) {
		return p2.getY() - p1.getY();
	}

	public static double getXDisplacement(Point2D p1, Point2D p2) {
		return p2.getX() - p1.getX();
	}

	public static Point2D getCenterPoint(CurvedArrow arrow){
		return getCenterPoint(arrow.getP1(), arrow.getP2());
	}


	public static Point2D getCenterPoint(Point2D p1, Point2D p2) {
		return cloneAndTranslatePoint(p1, getXDisplacement(p1, p2)/2, getYDisplacement(p1, p2)/2);
	}

	public static Point2D cloneAndTranslatePoint(Point2D point, double dx, double dy){
		Point2D newPoint = new Point2D.Double(point.getX()+dx, point.getY()+dy);
		return newPoint;
	}


	public static Point2D getCurveCenter(QuadCurve2D curve) {
		return getCurveCenter(curve.getP1(), curve.getCtrlPt(), curve.getP2());
	}

	public static Point2D getCurveCenter(Point2D p1, Point2D ctrlPt, Point2D p2) {
		Point2D center = GeometryHelper.getCenterPoint(p1, p2);
		center = GeometryHelper.getCenterPoint(center, ctrlPt);
		return center;
	}

	public static double getMinY(Point2D ... points) {
		double min = points[0].getY();
		for (int i = 1;i<points.length;i++){
			min = Math.min(min, points[i].getY());
		}
		return min;
	}

	public static double getMaxY(Point2D ... points) {
		double max = points[0].getY();
		for (int i = 1;i<points.length;i++){
			max = Math.max(max, points[i].getY());
		}
		return max;
	}

	public static double getMinX(Point2D ... points) {
		double min = points[0].getX();
		for (int i = 1;i<points.length;i++){
			min = Math.min(min, points[i].getX());
		}
		return min;
	}

	public static double getMaxX(Point2D ... points) {
		double max = points[0].getX();
		for (int i = 1;i<points.length;i++){
			max = Math.max(max, points[i].getX());
		}
		return max;
	}

	public static void translatePerpendicular(Point2D p,
			double d, Point2D from, Point2D to) {
		double theta = calculatePerpendicularAngle(from, to); 
		translate(p, theta, d);

	}

	public static Point2D pointOnCircle(Point2D center, double rad, double theta) {
		Point2D point = new Point2D.Double(center.getX(), center.getY());
		translate(point,theta, rad);
		return point;
	}

	public static void translateTowards(Point2D p, Point2D target, double d) {
		double theta = GeometryHelper.calculateAngle(p,target);
		translate(p, theta, d);
	}

	public static void translate(Point2D p, double theta, double d){
		double dx = d*Math.cos(theta), dy = d*Math.sin(theta);
		p.setLocation(p.getX()+dx,p.getY()+dy);
	}

	public static double calculatePerpendicularAngle(Point2D from,
			Point2D to) {

		return calculateAngle(from, to) + Math.PI/2;
	}


}
