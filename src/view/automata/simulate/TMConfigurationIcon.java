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

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;

import model.algorithms.testinput.simulate.configurations.tm.TMConfiguration;
import model.automata.Transition;
import model.automata.turing.TuringMachine;
import model.symbols.SymbolString;

/**
 * This is a configuration icon for configurations related to finite state
 * automata. These sorts of configurations are defined only by the state that
 * the automata is current in, plus the input left.
 * 
 * @author Thomas Finley
 */

public class TMConfigurationIcon<T extends TuringMachine<S>, S extends Transition<S>> extends ConfigurationIcon<T, S> {

	public static final int SIZE_HEAD = 4;
	/**
	 * Instantiates a new <CODE>TMConfigurationIcon</CODE>.
	 * 
	 * @param configuration
	 *            the TM configuration that is represented
	 */
	public TMConfigurationIcon(TMConfiguration<T, S> configuration) {
		super(configuration);
	}

	/**
	 * Returns the height of this icon.
	 * 
	 * @return the height of this icon
	 */
	public int getIconHeight() {
		// Why not...
		return super.getIconHeight() + 25 * getConfiguration().getNumOfSecondary();
	}

	/**
	 * This will paint a sort of "torn tape" object that shows the current
	 * contents and position of the tape.
	 * 
	 * @param c
	 *            the component this icon is drawn on
	 * @param g
	 *            the <CODE>Graphics2D</CODE> object to draw on
	 */
	public void paintConfiguration(Component c, Graphics2D g, int width,
			int height) {
		if (c != null)
			super.paintConfiguration(c, g, width, height);
		float position = BELOW_STATE.y + 5.0f;
		int headx = BELOW_STATE.x + width / 2;
		int heady = BELOW_STATE.y + 5;

		TMConfiguration<T, S> config = (TMConfiguration<T, S>) getConfiguration();
		
		for (int i = 0; i < config.getNumOfSecondary(); i++) {
			SymbolString current = config.getStringForIndex(i);
			int primary = config.getPositionForIndex(i);
			
			float tornHeight = Torn.paintSymbolString(g, current, 
					BELOW_STATE.x, position, Torn.TOP, width, true,
					true, primary);
			g.setColor(Color.black);
			g.drawLine(headx, heady, headx - SIZE_HEAD, heady - SIZE_HEAD);
			g.drawLine(headx, heady, headx + SIZE_HEAD, heady - SIZE_HEAD);
			position += tornHeight + 8f;
		}
		position -= 8f;
	}
}
