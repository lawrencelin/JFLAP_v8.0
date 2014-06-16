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
import java.awt.Graphics2D;

import model.algorithms.testinput.simulate.configurations.PDAConfiguration;
import model.automata.acceptors.pda.PDATransition;
import model.automata.acceptors.pda.PushdownAutomaton;
import model.symbols.SymbolString;

/**
 * This is a configuration icon for configurations related to finite state
 * automata. These sorts of configurations are defined only by the state that
 * the automata is current in, plus the input left.
 * 
 * @author Thomas Finley
 */

public class PDAConfigurationIcon extends ConfigurationIcon<PushdownAutomaton, PDATransition> {
	/**
	 * Instantiates a new <CODE>PDAConfigurationIcon</CODE>.
	 * 
	 * @param configuration
	 *            the PDA configuration that is represented
	 */
	public PDAConfigurationIcon(PDAConfiguration configuration) {
		super(configuration);
	}

	/**
	 * Returns the height of this icon.
	 * 
	 * @return the height of this icon
	 */
	public int getIconHeight() {
		// Why not...
		return super.getIconHeight() + 25;
	}

	/**
	 * This will paint a sort of "torn tape" object that shows the rest of the
	 * input, as well as the stack.
	 * 
	 * @param c
	 *            the component this icon is drawn on
	 * @param g
	 *            the <CODE>Graphics2D</CODE> object to draw on
	 * @param width
	 *            the width to draw the configuration in
	 * @param height
	 *            the height to draw the configuration in
	 */
	public void paintConfiguration(Component c, Graphics2D g, int width,
			int height) {
		super.paintConfiguration(c, g, width, height);
		PDAConfiguration config = (PDAConfiguration) getConfiguration();
		// Draw the torn tape with the rest of the input.
		
		Torn.paintSymbolString((Graphics2D) g, config.getPrimaryString(),
				RIGHT_STATE.x + 5.0f, ((float) super.getIconHeight()) * 0.5f,
				Torn.MIDDLE, width - RIGHT_STATE.x - 5.0f, false, true, config.getPrimaryPosition());
		// Draw the stack.
		
		Torn.paintSymbolString((Graphics2D) g, config.getStack(),
				BELOW_STATE.x, BELOW_STATE.y + 5.0f, Torn.TOP, getIconWidth(),
				false, true, -1);
	}
}
