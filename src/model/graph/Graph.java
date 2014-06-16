/*
 *  JFLAP - Formal Languages and Automata Package
 * 
 * 
 *  Susan H. Rodger
 *  Computer Science Department
 *  Duke University
 *  August 27, 2009

 *  Copyright (c) 2002-2009
 *  All rights reserved.

 *  JFLAP is open source software. Please see the LICENSE for terms.
 *
 */

package model.graph;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import model.change.ChangingObject;
import util.JFLAPConstants;
import util.Point2DAdv;
import util.arrows.GeometryHelper;

/**
 * A graph data structure. The idea behind the graph data structure is that a
 * vertex is just some sort of data structure whose type is not important, and
 * associated with a point. There is therefore no explicit node structure.
 * 
 * @author Thomas Finley, Ian McMahon
 */

public abstract class Graph<T> extends ChangingObject {

	private Map<T, Map<T, Integer>> myEdgeIDs;
	private Map<Integer, ControlPoint> myCtrlPoints;
	private Map<T, Point2D> verticesToPoints = new HashMap<T, Point2D>();
	private Map<T, Set<T>> verticesToNeighbors = new HashMap<T, Set<T>>();

	public Graph() {
		myEdgeIDs = new HashMap<T, Map<T, Integer>>();
		myCtrlPoints = new TreeMap<Integer, ControlPoint>();
	}

	public abstract boolean isDirected();

	/** Returns a string description of the graph. */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(super.toString() + "\n");
		sb.append(verticesToPoints);
		sb.append(myCtrlPoints.values());
		return sb.toString();
	}

	/**
	 * Returns an index for the edge between from and to
	 * 
	 * @param from
	 *            Start vertex
	 * @param to
	 *            End vertex
	 * @return Index for edge.
	 */
	public int getID(T from, T to) {
		return myEdgeIDs.get(from).get(to);
	}

	/** Returns the degree of a vertex. */
	public int degree(T vertex) {
		return adjacent(vertex).size();
	}

	/** Returns the degree of the graph. */
	public int totalDegree() {
		int degree = 0;
		for (T v : vertices()) {
			degree += degree(v);
		}
		return degree;
	}

	/** Returns the number of vertices. */
	public int numberOfVertices() {
		return verticesToPoints.size();
	}

	/** Returns the set of vertices a vertex is adjacent to. */
	public Set<T> adjacent(T vertex) {
		return (Set<T>) myEdgeIDs.get(vertex).keySet();
	}

	/** Clears all vertices and edges. */
	public void clear() {
		verticesToPoints.clear();
		verticesToNeighbors.clear();
	}

	/** Returns the point for a given vertex. */
	public Point2D pointForVertex(T vertex) {
		return (Point2D) verticesToPoints.get(vertex);
	}

	public T vertexForPoint(Point2D point) {
		for (T vertex : verticesToPoints.keySet())
			if (verticesToPoints.get(vertex).equals(point))
				return vertex;
		return null;
	}

	/**
	 * Returns a copy of the set of vertex objects.
	 */
	public Set<T> vertices() {
		return new HashSet<T>(verticesToPoints.keySet());
	}

	/**
	 * Returns the list of vertex points. The order they appear is not
	 * necessarily the same as the vertices.
	 */
	public Point2D[] points() {
		return (Point2D[]) verticesToPoints.values().toArray(new Point2D[0]);
	}

	/** Returns whether the graph contains the vertex. */
	public boolean hasVertex(T v) {
		return vertices().contains(v);
	}

	/** Adds a vertex. */
	public boolean addVertex(T vertex, Point2D point) {
		if (this.hasVertex(vertex))
			return false;
		myEdgeIDs.put(vertex, new TreeMap<T, Integer>());
		verticesToPoints.put(vertex, new Point2DAdv(point));
		distributeChanged();
		return true;
	}

	/** Removes a vertex. */
	@SuppressWarnings("unchecked")
	public boolean removeVertex(T vertex) {
		if (!this.hasVertex(vertex))
			return false;

		for (Object to : myEdgeIDs.get(vertex).keySet().toArray(new Object[0]))
			removeEdge(vertex, (T) to);

		for (Entry<T, Map<T, Integer>> e : myEdgeIDs.entrySet().toArray(
				new Entry[0])) {
			Map<T, Integer> map = e.getValue();
			if (map.containsKey(vertex))
				removeEdge(e.getKey(), vertex);
		}
		myEdgeIDs.remove(vertex);
		verticesToNeighbors.remove(vertex);
		verticesToPoints.remove(vertex);
		distributeChanged();
		return true;
	}

	/** Moves a vertex to a new point. */
	public void moveVertex(T vertex, Point2D point) {
		ControlPoint ctrl;
		Point2D old = pointForVertex(vertex);
		double oldx = old.getX(), oldy = old.getY();
		double x = point.getX(), y = point.getY();

		this.pointForVertex(vertex).setLocation(point);

		for (Entry<T, Integer> e : myEdgeIDs.get(vertex).entrySet()) {
			T to = e.getKey();
			ctrl = myCtrlPoints.get(e.getValue());
			// Deal with loops here.
			if (vertex.equals(to)) {
				double dx = x - oldx, dy = y - oldy;
				ctrl.setAll(x, y);
				ctrl.setLocation(ctrl.getX() + dx, ctrl.getY() + dy);

			} else
				ctrl.setFrom(x, y);
			update(vertex, to);
		}
		for (T from : myEdgeIDs.keySet()) {
			Map<T, Integer> map = myEdgeIDs.get(from);
			// We have already dealt with loops.
			if (map.containsKey(vertex) && !vertex.equals(from)) {
				int id = map.get(vertex);
				ctrl = myCtrlPoints.get(id);
				ctrl.setTo(x, y);
				update(from, vertex);
			}
		}
		distributeChanged();
	}

	public void update(T vertex, T to) {
		
	}

	/** Returns if an edge exists between two vertices. */
	public boolean hasEdge(T vertex1, T vertex2) {
		return adjacent(vertex1).contains(vertex2);
	}

	/** Adds an edge between two vertices. */
	public boolean addEdge(T vertex1, T vertex2) {
		if (hasEdge(vertex1, vertex2))
			return false;
		Point2D pFrom = this.pointForVertex(vertex1), pTo = this
				.pointForVertex(vertex2);
		int newID = getNextEdgeID();

		// add control point so that autobend can be applied downstream
		ControlPoint ctrl = getDefaultControlPoint(vertex1, vertex2);
		myCtrlPoints.put(newID, ctrl);

		myEdgeIDs.get(vertex1).put(vertex2, newID);
		if (!isDirected())
			myEdgeIDs.get(vertex2).put(vertex1, newID);
		else if (!vertex1.equals(vertex2) && this.hasEdge(vertex2, vertex1)
				&& !hasBeenBent(vertex2, vertex1)) {
			applyAutoBend(ctrl, pFrom, pTo);
			applyAutoBend(getControlPt(vertex2, vertex1), pTo, pFrom);
		}

		distributeChanged();
		return true;
	}

	/** Removes an edge between two vertices. */
	public boolean removeEdge(T vertex1, T vertex2) {
		if (!hasEdge(vertex1, vertex2))
			return false;

		if (!isDirected()) {
			myCtrlPoints.remove(getID(vertex2, vertex1));
			myEdgeIDs.get(vertex2).remove(vertex1);
		} else if (hasEdge(vertex2, vertex1) && isAutoBent(vertex2, vertex1))
			undoAutoBend(vertex2, vertex1);

		myCtrlPoints.remove(getID(vertex1, vertex2));
		myEdgeIDs.get(vertex1).remove(vertex2);

		distributeChanged();
		return true;
	}

	/**
	 * Returns the ControlPoint for the edge between the two vertices
	 */
	public ControlPoint getControlPt(T from, T to) {
		int edgeID = getID(from, to);
		return myCtrlPoints.get(edgeID);
	}

	/**
	 * Moves the control point for the edge between from and to to the specified
	 * point.
	 */
	public void setControlPt(Point2D ctrl, T from, T to) {
		int edgeID = getID(from, to);
		myCtrlPoints.get(edgeID).setLocation(ctrl);
		distributeChanged();
	}

	/**
	 * Returns the default control point for a given edge, located at either the
	 * center of the two vertices or above the single vertex (if it is a loop).
	 */
	public ControlPoint getDefaultControlPoint(T from, T to) {
		Point2D pFrom = this.pointForVertex(from), pTo = this
				.pointForVertex(to), center = GeometryHelper.getCenterPoint(
				pFrom, pTo);
		ControlPoint ctrl = new ControlPoint(center, pFrom, pTo);
		if (from.equals(to)) {
			GeometryHelper.translate(ctrl, Math.PI / 2,
					-JFLAPConstants.INITAL_LOOP_HEIGHT);
		}
		return ctrl;
	}

	/** Returns the next available Integer value for indexing an edge. */
	private int getNextEdgeID() {
		int i = 0;
		Set<Integer> used = new HashSet<Integer>();
		for (Map<T, Integer> map : myEdgeIDs.values()) {
			used.addAll(map.values());
		}
		while (used.contains(i))
			i++;
		return i;
	}

	/**
	 * Applies a translate to ctrl such that the corresponding edge will be bent
	 * to a default curve
	 */
	private void applyAutoBend(Point2D ctrl, Point2D pFrom, Point2D pTo) {
		GeometryHelper.translatePerpendicular(ctrl,
				-JFLAPConstants.AUTO_BEND_HEIGHT, pFrom, pTo);
	}

	/**
	 * Translates the control point for the edge between from and to in the
	 * opposite direction as the default auto bend
	 */
	private void undoAutoBend(T from, T to) {
		GeometryHelper.translatePerpendicular(getControlPt(from, to),
				JFLAPConstants.AUTO_BEND_HEIGHT, pointForVertex(from),
				pointForVertex(to));
	}

	/** Returns true if the edge has not been modified by the user or autobending. */
	private boolean hasBeenBent(T from, T to) {
		Point2D pFrom = this.pointForVertex(from), pTo = this
				.pointForVertex(to), ctrl = this.getControlPt(from, to), center = GeometryHelper
				.getCenterPoint(pFrom, pTo);
		return !ctrl.equals(center) && !isAutoBent(from, to);
	}

	/**
	 * Returns true if the edge between from and to is equal to an auto-bent
	 * curve with the same vertices.
	 */
	protected boolean isAutoBent(T from, T to) {
		Point2D pFrom = this.pointForVertex(from), pTo = this
				.pointForVertex(to), ctrl = this.getControlPt(from, to), test = GeometryHelper
				.getCenterPoint(pFrom, pTo);
		applyAutoBend(test, pFrom, pTo);
		return test.equals(ctrl);
	}
}
