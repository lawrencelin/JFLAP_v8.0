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





package view.automata.simulate;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.Icon;

import model.algorithms.testinput.simulate.Configuration;
import model.automata.Automaton;
import model.automata.State;
import model.automata.Transition;
import model.automata.acceptors.Acceptor;
import view.automata.StateDrawer;

/**
 * The <CODE>ConfigurationIcon</CODE> is a general sort of icon that can be
 * used to draw transitions. The <CODE>Icon</CODE> can then be added as the
 * part of any sort of object.
 * 
 * @author Thomas Finley
 */

public abstract class ConfigurationIcon<T extends Automaton<S>, S extends Transition<S>> implements Icon {

	private static final int STATE_RADIUS = 13;
	private static final StateDrawer STATE_DRAWER = new StateDrawer();
	private static final Point STATE_POINT = new Point(STATE_RADIUS * 2,
			STATE_RADIUS);
	protected static final Point RIGHT_STATE = new Point(STATE_RADIUS * 3, 0);
	protected static final Point BELOW_STATE = new Point(0, STATE_RADIUS * 2);
//	/** If there is a building block, this is where you draw the current state */
//	private static final Point BB_POINT = new Point(
//			(int) (STATE_POINT.getX() * 3.5), (int) STATE_POINT.getY());
	
	private Configuration<T, S> myConfiguration;

	/**
	 * Instantiates a new <CODE>ConfigurationIcon</CODE>.
	 * 
	 * @param configuration
	 *            the configuration that is represented
	 */
	public ConfigurationIcon(Configuration<T, S> configuration) {
		myConfiguration = configuration;
		STATE_DRAWER.setRadius(STATE_RADIUS);
	}

	/**
	 * Returns the preferred width of the icon. Subclasses should attempt to
	 * draw within these bounds, and can override if they'd like a bit more
	 * space to play with.
	 * 
	 * @return the default preferred width is 150 pixels
	 */
	public int getIconWidth() {
		return 400;
	}

	/**
	 * Returns the preferred height of the icon, which is just enought to draw
	 * the state.
	 */
	public int getIconHeight() {
		return STATE_RADIUS * 2;
	}

	/**
	 * Returns the <CODE>Configuration</CODE> drawn by this icon.
	 * 
	 * @return the <CODE>Configuration</CODE> drawn by this icon
	 */
	public Configuration<T, S> getConfiguration() {
		return myConfiguration;
	}

	/**
	 * Paints the graphical representation of a configuration on this icon
	 * 
	 * @param c
	 *            the component this icon is drawn on
	 * @param g
	 *            the graphics object to draw to
	 * @param x
	 *            the start <CODE>x</CODE> coordinate to draw at
	 * @param y
	 *            the start <CODE>y</CODE> coordinate to draw at
	 */
	public void paintIcon(Component c, Graphics g, int x, int y) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.translate(x, y);
		// Draws the state.
		paintConfiguration(c, g2, getIconWidth(), getIconHeight());
		g2.translate(-x, -y);
	}

	/**
	 * The general method for painting the rest of the configuration. The
	 * subclasses should override to do whatever it is they do. At this point
	 * the state has already been drawn. The areas where the state has NOT
	 * already been drawn is defined by the static variables <CODE>RIGHT_STATE</CODE>
	 * and <CODE>BELOW_STATE</CODE>. By default this method paints the state.
	 * 
	 * @param c
	 *            the component this icon is drawn on
	 * @param g
	 *            the <CODE>Graphics2D</CODE> object to draw on
	 * @param width
	 *            the width to paint the configuration in
	 * @param height
	 *            the height to paint the configuration in
	 */
	public void paintConfiguration(Component c, Graphics2D g, int width,
			int height) {
			Automaton auto = myConfiguration.getAutomaton();
			State state = myConfiguration.getState();
			
			boolean isStartState = Automaton.isStartState(auto, state);
			boolean isFinal = auto instanceof Acceptor ? Acceptor.isFinalState((Acceptor) auto, state) : false;
			STATE_DRAWER.draw(STATE_POINT, state, g, isFinal, isStartState);
	}
}
