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

package view.action.automata;

import java.awt.Component;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import model.algorithms.testinput.simulate.ConfigurationChain;
import model.algorithms.testinput.simulate.SingleInputSimulator;
import model.symbols.SymbolString;
import universe.JFLAPUniverse;
import view.automata.simulate.TraceWindow;
import view.automata.views.AutomatonView;
import view.environment.JFLAPEnvironment;
import debug.JFLAPDebug;

/**
 * This is the action used for the simulation of input on an automaton with no
 * interaction. This method can operate on any automaton. It uses a special
 * exception for the case of the multiple tape Turing machine.
 * 
 * @author Thomas Finley
 */

public class FastSimulateAction extends SimulateAction {

	/** The steps in warnings. */
	protected static final int WARNING_STEP = 500;
	private Object config;

	/**
	 * Instantiates a new <CODE>NoInteractionSimulateAction</CODE>.
	 * 
	 * @param auto
	 *            the automaton that input will be simulated on
	 * @param environment
	 *            the environment object that we shall add our simulator pane to
	 */
	public FastSimulateAction(AutomatonView view) {
		super(view, false);
		putValue(NAME, "Fast Run...");
		putValue(ACCELERATOR_KEY, null);
	}

	/**
	 * Reports a configuration that accepted.
	 * 
	 * @param configuration
	 *            the configuration that accepted
	 * @param component
	 *            the parent component of dialogs brought up
	 * @return <CODE>true</CODE> if we should continue searching, or
	 *         <CODE>false</CODE> if we should halt
	 */
	protected boolean reportConfiguration(ConfigurationChain configuration,
			Component component) {
		JComponent past = (JComponent) TraceWindow.getPastPane(configuration);
		past.setPreferredSize(new java.awt.Dimension(300, 400));
		String[] options = { "Keep looking", "I'm done" };
		int result = JOptionPane.showOptionDialog(component, past,
				"Accepting configuration found!", JOptionPane.YES_NO_OPTION,
				JOptionPane.INFORMATION_MESSAGE, null, options, null);
		return result == JOptionPane.YES_OPTION;
	}

	/**
	 * Confirms if the user wants to keep searching. This should be called
	 * periodically to give the user a chance to break out of infinite loops.
	 * 
	 * @param generated
	 *            the number of configurations generated sofar
	 * @param component
	 *            the parent component of dialogs brought up
	 * @return <CODE>true</CODE> if we should continue searching, or
	 *         <CODE>false</CODE> if we should halt
	 */
	protected boolean confirmContinue(int generated, Component component) {
		int result = JOptionPane.showConfirmDialog(component, generated
				+ " configurations have been generated.  "
				+ "Should we continue?");
		return result == JOptionPane.YES_OPTION;
	}

	@Override
	public void handleInteraction(SingleInputSimulator simulator,
			SymbolString... symbols) {
		JFLAPEnvironment env = JFLAPUniverse.getActiveEnvironment();
		int numberGenerated = 0;
		int warningGenerated = WARNING_STEP;
		int numberAccepted = 0;

		simulator.beginSimulation(symbols);
		ConfigurationChain[] configs = simulator.getChains().toArray(new ConfigurationChain[0]);

		while (configs.length > 0) {
			numberGenerated += configs.length;
			// Make sure we should continue.
			if (numberGenerated >= warningGenerated) {
				if (!confirmContinue(numberGenerated, env))
					return;
				while (numberGenerated >= warningGenerated)
					warningGenerated *= 2;
			}

			for (ConfigurationChain chain : configs) {
				if (chain.isAccept()) {
					numberAccepted++;
					if (!reportConfiguration(chain, env))
						return;
				}
			}
			configs = simulator.step();
		}
		if (numberAccepted == 0) {
			JOptionPane.showMessageDialog(env, "The input was rejected.");
			return;
		}
		JOptionPane.showMessageDialog(env, numberAccepted + " configuration"
				+ (numberAccepted == 1 ? "" : "s")
				+ " accepted, and\nother possibilities are exhausted.");
	}
}
