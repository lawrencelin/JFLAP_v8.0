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




package model.graph.layout;


import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.JOptionPane;

import model.graph.Graph;
import model.graph.LayoutAlgorithm;
import model.graph.TransitionGraph;
import util.Point2DAdv;
import util.arrows.GeometryHelper;
import util.view.GraphHelper;
import debug.JFLAPDebug;
import errors.JFLAPError;




/**
 * This class contains a few simple vertex movement options that do not individually need a 
 * separate class.
 */
public class VertexMover extends LayoutAlgorithm {

	/**
	 * Code for reflecting across a horizontal line through the graph's center.
	 */
	public static final int HORIZONTAL_CENTER = -10;
	/**
	 * Code for reflecting across a vertical line through the graph's center.
	 */
	public static final int VERTICAL_CENTER = -11;
	/**
	 * Code for reflecting across a positive slope diagonal of slope 1.
	 */
	public static final int POSITIVE_SLOPE_DIAGONAL = -12;
	/**
	 * Code for reflecting across a negative slope diagonal of slope -1.
	 */
	public static final int NEGATIVE_SLOPE_DIAGONAL = -13;
	/**
	 * Code for rotating the graph.
	 */
	public static final int ROTATE = -14;
	/**
	 * Code for filling the editor window with the graph.
	 */
	public static final int FILL = -15;
	/**
	 * Represents which of the commands this class will process.
	 */
	private int command;
	
	/**
	 * Assigns some default values.  To have different values, use the other constructor.
	 */
	public VertexMover(int c) {
		super();
		command = c;	
	}
	
	/**
	 * Constructor allowing the user to customize certain values.
	 * 
	 * @param pSize 
	 *     value for <code>size</code>.
	 * @param vDim 
	 *     value for <code>vertexDim</code>.
	 * @param vBuffer 
	 *     value for <code>vertexBuffer</code>.
	 */
	public VertexMover(Dimension pSize, Dimension vDim, double vBuffer, int c) {
		super(pSize, vDim, vBuffer);
		command = c;	
	}	
	
	public void layout(Graph graph, Set notMoving) {  
		ArrayList vertices = getMovableVertices(graph, notMoving);
		//Check whether to fill the screen first, because other commands will call the other
		//shiftOntoScreen method.
		if (command == FILL) {
			shiftOntoScreen(graph, size, vertexDim, false);
			return;
		}
		
		Point2D point;
		//Reflecting across the given line
		if (command == HORIZONTAL_CENTER)
			for (int i=0; i<vertices.size(); i++) {
				point = graph.pointForVertex(vertices.get(i));
				graph.moveVertex(vertices.get(i), new Point2D.Double(point.getX(), size.getHeight() - point.getY()));
			}
		else if (command == VERTICAL_CENTER)
			for (int i=0; i<vertices.size(); i++) {
				point = graph.pointForVertex(vertices.get(i));
				graph.moveVertex(vertices.get(i), new Point2D.Double(size.getWidth() - point.getX(), point.getY()));
			}
		else if (command == POSITIVE_SLOPE_DIAGONAL)
			for (int i=0; i<vertices.size(); i++) {
				point = graph.pointForVertex(vertices.get(i));
				graph.moveVertex(vertices.get(i), new Point2D.Double(point.getY(), point.getX()));
			}
		else if (command == NEGATIVE_SLOPE_DIAGONAL)
			for (int i=0; i<vertices.size(); i++) {
				point = graph.pointForVertex(vertices.get(i));
				graph.moveVertex(vertices.get(i), new Point2D.Double(
					size.getWidth() - point.getY(), size.getHeight() - point.getX()));
			}		
		//Rotating the graph
		else if (command == ROTATE) {
			double theta = 0;
			try {
				//Prompt for the degree input
				String input = (String) JOptionPane.showInputDialog("How many degrees to turn the automaton? \n" +
					"(positive - clockwise, negative - counterclockwise)", new String("180"));
				theta = Double.parseDouble(input);
			} catch (NumberFormatException e) {
				JFLAPError.INVALID_DEGREE.show();
				return;
			} catch (NullPointerException e) {
				return;
			}
			Point2D pCenter = new Point2DAdv(size.getWidth()/2, size.getHeight()/2);
			double angle = theta * Math.PI / 180;

			for (int i=0; i<vertices.size(); i++) {
				point = graph.pointForVertex(vertices.get(i));		
				Point2D rotate = GeometryHelper.rotatePoint(point, pCenter, angle);
				graph.moveVertex(vertices.get(i),  rotate);
			}
		}
	}
}
