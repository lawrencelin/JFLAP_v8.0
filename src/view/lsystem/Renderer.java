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

package view.lsystem;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

import model.symbols.Symbol;
import model.symbols.SymbolString;
import universe.preferences.JFLAPPreferences;

/**
 * A <CODE>Renderer</CODE> object allows a client to create an image of a string
 * of symbols generated, presumably, from an <CODE>LSystem</CODE>.
 * <P>
 * 
 * The following symbols have significance:
 * 
 * @see grammar.lsystem.Expander
 * @see grammar.lsystem.LSystem
 * 
 * @author Thomas Finley, Ian McMahon
 */

public class Renderer {
	public static Set<String> ASSIGN_WORDS;
	public static Set<String> NONASSIGN_WORDS;

	private Map<String, CommandHandler> handlers;
	private boolean areDrawing;
	private Stack<Turtle> turtleStack;
	private Turtle currentTurtle;
	private Graphics2D g;
	private GeneralPath polygon;
	private GeneralPath linePath;
	private int objectsDrawnSoFar;
	private int completedSymbols;

	static {
		Set<String> s = new TreeSet<String>();
		s.add("color");
		s.add("polygonColor");
		NONASSIGN_WORDS = Collections.unmodifiableSet(new HashSet<String>(s));
		s.add("angle");
		s.add("lineWidth");
		s.add("lineIncrement");
		s.add("distance");
		s.add("hueChange");
		ASSIGN_WORDS = Collections.unmodifiableSet(s);
	}

	/**
	 * Instantiates a renderer object.
	 */
	public Renderer() {
		initRenderer();
		initializeCommandHandlers();
		initializeAssignmentHandlers();
	}

	/**
	 * Given a list of symbols and a dictionary of parameters, this will render
	 * a representation of those symbols to either a graphics, or a returned
	 * image.
	 * 
	 * @param symbols
	 *            a list of symbols
	 * @param parameters
	 *            the parameters
	 * @param matrix
	 *            the initial transform matrix for the turtle, or if
	 *            <CODE>null</CODE> it is assumed to be the identity matrix
	 * @param graphics
	 *            If we want to render to a graphics, pass this in and the
	 *            L-system will be drawn in the graphic's clip bounds, or pass
	 *            in <CODE>null</CODE> to have this function return an image.
	 *            This graphics should have a clip area set!
	 * @param origin
	 *            stores in the passed in point the location where the turtle
	 *            started
	 * @return an image of a rendering of these symbols, or <CODE>null</CODE> if
	 *         there was a passed in graphics object
	 * @throws IllegalArgumentException
	 *             if there is a passed in graphics object and its clip area is
	 *             not set
	 */
	public Image render(SymbolString symbols, Map<String, String> parameters,
			Matrix matrix, Graphics2D graphics, Point2D origin) {
		if (graphics != null && graphics.getClip() == null)
			throw new IllegalArgumentException(
					"Graphics needs a non-null clip!");
		if (matrix == null)
			matrix = new Matrix();
		BufferedImage image = null;
		Rectangle2D bounds = new Rectangle2D.Double();

		completedSymbols = 0;

		for (int i = 0; i < 2; i++) {
			initializeConditions(matrix, i);

			image = createGraphicsObject(graphics, origin, bounds);

			// Do the initial parameters.
			for (String key : parameters.keySet()) {
				try {
					assign(key, parameters.get(key));
				} catch (Throwable e) {
					// We have an error in the handler!
				}
			}

			// Set the initial drawing state.
			g.setColor(currentTurtle.getColor());
			g.setStroke(currentTurtle.getStroke());
			capLinePath();

			handleAllSymbols(symbols);
			capLinePath();

			g.dispose();
			// We pop all the turtle stacks to make sure the bounds
			// are okay...
			while (!turtleStack.isEmpty())
				popTurtleStack();
			bounds = currentTurtle.getBounds();
		}
		areDrawing = false;
		return image;
	}

	/**
	 * Returns the progress in the current rendering.
	 * 
	 * @return the number of symbols processed, the max value of which is twice
	 *         the number of symbols passed into the <CODE>render</CODE> method
	 */
	public int getCompletedSymbols() {
		return completedSymbols;
	}

	/**
	 * Initializes private fields to be ready to function correctly.
	 */
	private void initRenderer() {
		handlers = new HashMap<String, CommandHandler>();
		areDrawing = false;
		turtleStack = new Stack<Turtle>();
		polygon = null;
		linePath = new GeneralPath();
	}

	/**
	 * Puts all handlers having to do with LSystem commands into the
	 * <CODE>handlers</CODE> map.
	 */
	private void initializeCommandHandlers() {
		handlers.put(JFLAPPreferences.getDrawCommand().toString(),
				new MoveHandler(true, true));
		handlers.put(JFLAPPreferences.getForwardCommand().toString(),
				new MoveHandler(false, true));
		handlers.put(JFLAPPreferences.getRightYawCommand().toString(),
				new TurnHandler(true));
		handlers.put(JFLAPPreferences.getLeftYawCommand().toString(),
				new TurnHandler(false));
		handlers.put(JFLAPPreferences.getPitchDownCommand().toString(),
				new PitchHandler(true));
		handlers.put(JFLAPPreferences.getPitchUpCommand().toString(),
				new PitchHandler(false));
		handlers.put(JFLAPPreferences.getRightRollCommand().toString(),
				new RollHandler(true));
		handlers.put(JFLAPPreferences.getLeftRollCommand().toString(),
				new RollHandler(false));
		handlers.put(JFLAPPreferences.getPushCommand().toString(),
				new PushTurtleHandler());
		handlers.put(JFLAPPreferences.getPopCommand().toString(),
				new PopTurtleHandler());
		handlers.put(JFLAPPreferences.getIWidthCommand().toString(),
				new WidthChangeHandler(true));
		handlers.put(JFLAPPreferences.getDWidthCommand().toString(),
				new WidthChangeHandler(false));
		handlers.put(JFLAPPreferences.getBeginPolygonCommand().toString(),
				new BeginPolygonHandler());
		handlers.put(JFLAPPreferences.getEndPolygonCommand().toString(),
				new ClosePolygonHandler());
		handlers.put(JFLAPPreferences.getYawCommand().toString(),
				new ReverseHandler());
		handlers.put(JFLAPPreferences.getIColorCommand().toString(),
				new HueChangeHandler(false, true));
		handlers.put(JFLAPPreferences.getDColorCommand().toString(),
				new HueChangeHandler(false, false));
		handlers.put(JFLAPPreferences.getIPolyColorCommand().toString(),
				new HueChangeHandler(true, true));
		handlers.put(JFLAPPreferences.getDPolyColorCommand().toString(),
				new HueChangeHandler(true, false));
	}

	/**
	 * Puts all handlers having to do with LSystem parameter assignments into
	 * the <CODE>handlers</CODE> map.
	 */
	private void initializeAssignmentHandlers() {
		handlers.put("color", new DrawColorHandler());
		handlers.put("polygonColor", new PolygonColorHandler());
		CommandHandler angleIncrement = new AngleIncrementHandler();
		handlers.put("angle", angleIncrement);
		handlers.put("angleIncrement", angleIncrement);
		handlers.put("lineWidth", new LineWidthHandler());
		handlers.put("lineIncrement", new LineWidthIncrementHandler());
		handlers.put("distance", new DistanceHandler());
		handlers.put("hueChange", new HueAngleIncrementHandler());
	}

	/**
	 * Prepares turtle for drawing, flags if we have entered drawing phase.
	 */
	private void initializeConditions(Matrix matrix, int i) {
		areDrawing = i == 1;
		objectsDrawnSoFar = 0;
		// Set up the initial conditions.
		turtleStack.clear();
		currentTurtle = new Turtle();
		currentTurtle.matrix = matrix;
		currentTurtle = currentTurtle.copy();
	}

	/**
	 * Returns an image initializes <i>g</i> for rendering, resizing as needed.
	 */
	private BufferedImage createGraphicsObject(Graphics2D graphics,
			Point2D origin, Rectangle2D bounds) {
		BufferedImage image;
		// Set up the graphics object.
		if (!areDrawing || graphics == null) {
			image = createGFromImage(origin, bounds);
		} else {
			image = null;
			g = (Graphics2D) graphics.create();
			Rectangle2D newBounds = new Rectangle2D.Double(bounds.getX() - 5.0,
					bounds.getY() - 5.0, bounds.getWidth() + 10.0,
					bounds.getHeight() + 10.0);
			Rectangle2D ourBounds = g.getClipBounds();

			double aRatio = newBounds.getWidth() / newBounds.getHeight();
			double vRatio = ourBounds.getWidth() / ourBounds.getHeight();

			if (aRatio > vRatio) {
				extendByHeight(newBounds, vRatio);
			} else {
				extendByWidth(newBounds, vRatio);
			}
			rescaleGraphics(origin, newBounds, ourBounds);
		}
		return image;
	}

	/**
	 * Creates a new image for rendering graphics.
	 */
	private BufferedImage createGFromImage(Point2D origin, Rectangle2D bounds) {
		BufferedImage image;
		image = new BufferedImage((int) bounds.getWidth() + 10,
				(int) bounds.getHeight() + 10, BufferedImage.TYPE_INT_ARGB);
		g = image.createGraphics();

		if (areDrawing) {
			g.translate(-bounds.getX() + 5.0, -bounds.getY() + 5.0);
			origin.setLocation(5.0 - bounds.getX(), 5.0 - bounds.getY());
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
		}
		return image;
	}

	/**
	 * The L-system is wider than the clip bounds.
	 * 
	 * @param newBounds
	 * @param vRatio
	 */
	private void extendByHeight(Rectangle2D newBounds, double vRatio) {
		double targetHeight = newBounds.getWidth() / vRatio;
		targetHeight -= newBounds.getHeight();

		newBounds.setRect(newBounds.getX(), newBounds.getY() - targetHeight
				/ 2.0, newBounds.getWidth(), newBounds.getHeight()
				+ targetHeight);
	}

	/**
	 * The L-system is taller than the clip bounds.
	 * 
	 * @param newBounds
	 * @param vRatio
	 */
	private void extendByWidth(Rectangle2D newBounds, double vRatio) {

		double targetWidth = newBounds.getHeight() * vRatio;
		targetWidth -= newBounds.getWidth();

		newBounds.setRect(newBounds.getX() - targetWidth / 2.0,
				newBounds.getY(), newBounds.getWidth() + targetWidth,
				newBounds.getHeight());
	}

	/**
	 * Given a new set of bounds, resizes and translates graphics and origin
	 * point.
	 */
	private void rescaleGraphics(Point2D origin, Rectangle2D newBounds,
			Rectangle2D ourBounds) {
		double scale = ourBounds.getWidth() / newBounds.getWidth();
		g.scale(scale, scale);
		g.translate(ourBounds.getX() - newBounds.getX(), ourBounds.getY()
				- newBounds.getY());
		origin.setLocation(ourBounds.getX() - newBounds.getX(),
				ourBounds.getY() - newBounds.getY());
	}

	/**
	 * Does an assignment from a key to a value, calling the handler as well as
	 * setting the value in the turtle.
	 * 
	 * @param key
	 *            the key
	 * @param value
	 *            the value, possibly a mathematical expression
	 */
	private void assign(String key, String value) {
		try {
			try {
				if (!NONASSIGN_WORDS.contains(key)) {
					currentTurtle.assign(key, value);
					value = currentTurtle.get(key).toString();
				}
			} catch (Throwable e) {

			}
			CommandHandler handler = getHandler(key);
			handler.handle(value);
		} catch (Throwable e) {

		}
	}

	/**
	 * Returns the command handler for a symbol.
	 * 
	 * @param symbol
	 *            the symbol
	 * @return the command handler for that symbol, or <CODE>null</CODE> if no
	 *         handler exists
	 */
	private CommandHandler getHandler(String symbol) {
		if (handlers.containsKey(symbol))
			return handlers.get(symbol);
		return null;
	}

	/**
	 * Dump path to the graphics, clear the path, move path to turtle's position
	 * and start anew!
	 */
	private final void capLinePath() {
		g.draw(linePath);
		linePath.reset();
		linePath.moveTo((float) currentTurtle.position.getX(),
				(float) currentTurtle.position.getY());
	}

	/**
	 * Repeatedly read symbols and call the appropriate command handler.
	 * 
	 * @param symbols
	 */
	private void handleAllSymbols(SymbolString symbols) {
		for (Symbol s : symbols) {
			completedSymbols++;
			String symbol = s.getString();
			CommandHandler handler = getHandler(symbol);
			if (handler != null) {
				try {
					handler.handle(null);
				} catch (Throwable e) {
					// We have an error!
				}
				continue;
			}
			// Perhaps this is an assignment?
			int equalsPosition = symbol.indexOf('=');
			if (equalsPosition != -1) {
				String key = symbol.substring(0, equalsPosition);
				String value = symbol.substring(equalsPosition + 1);
				// Get the assignment.
				assign(key, value);
			}
			// Perhaps this is a symbol with an argument.
			int leftParenPosition = symbol.indexOf('('), rightParenPosition = symbol
					.lastIndexOf(')');
			if (leftParenPosition != -1 && rightParenPosition != -1
					&& leftParenPosition < rightParenPosition) {
				String key = symbol.substring(0, leftParenPosition);
				String value = symbol.substring(leftParenPosition + 1,
						rightParenPosition);
				handler = getHandler(key);
				try {
					handler.handle(value);
				} catch (Throwable e) {
					// Another error. Whew.
				}
				continue;
			}
		}
	}

	/**
	 * This will pop the turtle stack.
	 */
	private void popTurtleStack() {
		try {
			Turtle lt = (Turtle) turtleStack.pop();
			lt.updateBounds(currentTurtle);
			currentTurtle = lt;
			g.setColor(currentTurtle.getColor());
			g.setStroke(currentTurtle.getStroke());
		} catch (EmptyStackException e) {
			// We just ignore it.
		}
	}

	// / THE COMMAND HANDLERS!

	/**
	 * This is a command handler. This is the object that responds to the
	 * command. This class is meant to alter the state of the
	 * <CODE>Renderer</CODE> object, so it is not a static class.
	 */
	private class CommandHandler {
		/**
		 * Handles the command.
		 * 
		 * @param symbol
		 *            an optional argument to the handler, but may be
		 *            <CODE>null</CODE>
		 */
		public void handle(String symbol) {
			// This does nothing, since the default behavior is to
			// just ignore commands. Subclasses will do something,
			// presumably.
		}
	}

	/**
	 * This handles moving the cursor.
	 */
	private class MoveHandler extends CommandHandler {
		private boolean pendown;
		private boolean forward;
		private Line2D line;
		
		public MoveHandler(boolean pendown, boolean forward) {
			this.pendown = pendown;
			this.forward = forward;
			line = new Line2D.Double();
		}

		public final void handle(String symbol) {
			// Evaluate if necessary.
			if (symbol == null)
				currentTurtle.go(forward);
			else {
				double d = currentTurtle.valueOf(symbol).doubleValue();
				currentTurtle.go(forward ? d : -d);
			}

			if (!areDrawing)
				return;
			if (pendown) {
				if (polygon == null) {
					// We're not adding to the polygon!
					linePath.lineTo((float) currentTurtle.position.getX(),
							(float) currentTurtle.position.getY());
				} else {
					// We are adding to the polygon!
					polygon.lineTo((float) currentTurtle.position.getX(),
							(float) currentTurtle.position.getY());
				}
			} else {
				linePath.moveTo((float) currentTurtle.position.getX(),
						(float) currentTurtle.position.getY());
			}

		}
	}

	/**
	 * This handles turning.
	 */
	private class TurnHandler extends CommandHandler {
		private boolean clockwise;
		
		public TurnHandler(boolean clockwise) {
			this.clockwise = clockwise;
		}

		public final void handle(String symbol) {
			// Evaluate if necessary.
			if (symbol == null)
				currentTurtle.turn(clockwise);
			else {
				double d = currentTurtle.valueOf(symbol).doubleValue();
				currentTurtle.turn(clockwise ? -d : d);
			}
		}
	}

	/**
	 * This handles pitching.
	 */
	private class PitchHandler extends CommandHandler {
		private boolean down;
		
		public PitchHandler(boolean down) {
			this.down = down;
		}

		public final void handle(String symbol) {
			if (symbol == null)
				currentTurtle.pitch(down);
			else {
				double d = currentTurtle.valueOf(symbol).doubleValue();
				currentTurtle.pitch(down ? d : -d);
			}
		}

	}

	/**
	 * This handles rolling.
	 */
	private class RollHandler extends CommandHandler {
		private boolean right;
		
		public RollHandler(boolean right) {
			this.right = right;
		}

		public final void handle(String symbol) {
			if (symbol == null)
				currentTurtle.roll(right);
			else {
				double d = currentTurtle.valueOf(symbol).doubleValue();
				currentTurtle.roll(right ? -d : d);
			}
		}

	}

	/**
	 * This handles pushing on the turtle stack.
	 */
	private class PushTurtleHandler extends CommandHandler {
		public final void handle(String symbol) {
			turtleStack.push(currentTurtle.clone());
		}
	}

	/**
	 * This handles popping the turtle stack.
	 */
	private class PopTurtleHandler extends CommandHandler {
		public final void handle(String symbol) {
			capLinePath();
			popTurtleStack();
			capLinePath();
		}
	}

	/**
	 * This handles changing the width of lines.
	 */
	private class WidthChangeHandler extends CommandHandler {
		private boolean increment;
		
		public WidthChangeHandler(boolean increment) {
			this.increment = increment;
		}

		public final void handle(String symbol) {
			capLinePath();
			if (symbol == null)
				currentTurtle.changeLineWidth(increment);
			else {
				double d = currentTurtle.valueOf(symbol).doubleValue();
				currentTurtle.changeLineWidth(increment ? d : -d);
			}
			g.setStroke(currentTurtle.getStroke());
		}

	}

	/**
	 * This handles change of the draw color.
	 */
	private class DrawColorHandler extends CommandHandler {
		public final void handle(String symbol) {
			if (!areDrawing)
				return;
			capLinePath();
			currentTurtle.setColor(symbol);
			g.setColor(currentTurtle.getColor());
		}
	}

	/**
	 * This handles change of the polygon color.
	 */
	private class PolygonColorHandler extends CommandHandler {
		public final void handle(String symbol) {
			if (!areDrawing)
				return;
			currentTurtle.setPolygonColor(symbol);
		}
	}

	/**
	 * This handles change of the angle increment.
	 */
	private class AngleIncrementHandler extends CommandHandler {
		public final void handle(String symbol) {
			currentTurtle.setAngleChange(Double.parseDouble(symbol));
		}
	}

	/**
	 * This handles change of the line width.
	 */
	private class LineWidthHandler extends CommandHandler {
		public final void handle(String symbol) {
			if (!areDrawing)
				return;
			capLinePath();
			currentTurtle.setLineWidth(Double.parseDouble(symbol));
			g.setStroke(currentTurtle.getStroke());
		}
	}

	/**
	 * This handles change of the line width increment.
	 */
	private class LineWidthIncrementHandler extends CommandHandler {
		public final void handle(String symbol) {
			currentTurtle.setLineIncrement(Double.parseDouble(symbol));
		}
	}

	/**
	 * This handles change of individual line lengths.
	 */
	private class DistanceHandler extends CommandHandler {
		public final void handle(String symbol) {
			currentTurtle.distance = Double.parseDouble(symbol);
		}
	}

	/**
	 * This handler begins a polygon.
	 */
	private class BeginPolygonHandler extends CommandHandler {
		public final void handle(String symbol) {
			if (!areDrawing || polygon != null)
				return; // Hrm.
			capLinePath();
			polygon = new GeneralPath();
			polygon.moveTo((float) currentTurtle.position.getX(),
					(float) currentTurtle.position.getY());
		}
	}

	/**
	 * This handler closes a polygon.
	 */
	private class ClosePolygonHandler extends CommandHandler {
		public final void handle(String symbol) {
			if (!areDrawing)
				return;
			capLinePath();
			polygon.closePath();
			g.setColor(currentTurtle.polygonColor);
			g.fill(polygon);
			polygon = null;
			g.setColor(currentTurtle.color);
			objectsDrawnSoFar++;
		}
	}

	/**
	 * The reverse handler.
	 */
	private class ReverseHandler extends CommandHandler {
		public final void handle(String symbol) {
			currentTurtle.turn(180.0);
		}
	}

	/**
	 * This handles change of the hue angle increment.
	 */
	private class HueAngleIncrementHandler extends CommandHandler {
		public final void handle(String symbol) {
			currentTurtle.setHueChange(Double.parseDouble(symbol));
		}
	}

	/**
	 * This handles changing the hue angle.
	 */
	private class HueChangeHandler extends CommandHandler {
		private boolean add;
		private boolean polygon;
		
		public HueChangeHandler(boolean polygon, boolean add) {
			this.polygon = polygon;
			this.add = add;
		}

		public final void handle(String symbol) {
			if (!areDrawing)
				return;
			capLinePath();
			if (symbol == null)
				if (polygon)
					currentTurtle.changePolygonHue(add);
				else
					currentTurtle.changeHue(add);
			else {
				double d = currentTurtle.valueOf(symbol).doubleValue();
				d = add ? d : -d;
				if (polygon)
					currentTurtle.changePolygonHue(d);
				else
					currentTurtle.changeHue(d);
			}
			g.setColor(currentTurtle.getColor());
		}

	}
}
