package view.graph;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;

import util.JFLAPConstants;
import util.arrows.ArrowHead;
import util.arrows.CurvedArrow;
import util.arrows.GeometryHelper;
import util.view.GraphHelper;

import model.graph.Graph;

public class GraphDrawer<T> implements JFLAPConstants {

	private VertexDrawer<T> myVertexDrawer;

	
	public GraphDrawer(VertexDrawer<T> vDraw){
		myVertexDrawer = vDraw;
	}


	public void draw(Graph<T> obj, Graphics g) {
		drawEdges(obj,g);
		drawVertices(obj, g);
	}

	public void drawVertices(Graph<T> obj, Graphics g){
		for (T v : obj.vertices()){
			drawVertex(v, obj, g);
		}
	}
	
	public void drawVertex(T v, Graph<T> obj, Graphics g) {
		myVertexDrawer.draw(obj.pointForVertex(v), v,g);
	}

	public void drawEdges(Graph<T> obj, Graphics g) {
		for (T from : obj.vertices()){
			for (T to: obj.adjacent(from)){
				drawEdge(from, to, obj, g);
				drawLabel(from,to,obj,g);
			}
		}
	}

	public void drawLabel(T from, T to, Graph<T> obj, Graphics g) {
		// by default: do nothing
	}

	public void drawEdge(T from, T to, Graph<T> obj, Graphics g) {
		CurvedArrow curve = GraphHelper.getArrow(from, to, obj);
		curve.draw(g);
	}
		
	public VertexDrawer<T> getVertexDrawer(){
		return myVertexDrawer;
	}
	
}
