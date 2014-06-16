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
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.swing.JPanel;

import debug.JFLAPDebug;
import model.algorithms.testinput.simulate.Configuration;

/**
 * The <CODE>ConfigurationPane</CODE> is the pane where the configurations are
 * displayed and selected.
 * 
 * @see automata.Configuration
 * 
 * @author Thomas Finley
 */

public class ConfigurationPanel extends JPanel implements ActionListener {
	private HashMap<Configuration, ConfigurationButton> configurationToButtonMap;
	private HashSet<Configuration> selected;

	/**
	 * Creates a <CODE>ConfigurationPane</CODE>. The instance as created has no
	 * configurations loaded into it yet.
	 * 
	 * @param automaton
	 *            the automaton that configurations will come from
	 */
	public ConfigurationPanel() {
		configurationToButtonMap = new HashMap<Configuration, ConfigurationButton>();
		selected = new HashSet<Configuration>();
		setLayout(new GridLayout(0, 4));
	}

	/**
	 * Adds a configuration to the configuration pane.
	 * 
	 * @param configuration
	 *            the configuration to add
	 */
	public void add(Configuration configuration) {
		add(configuration, ConfigurationButton.NORMAL);
	}

	/**
	 * Adds configurations with the given state.
	 * 
	 * @param configuration
	 *            the configuration to add q *
	 * @param state
	 *            the state of the configuration, either NORMAL, ACCEPT, REJECT,
	 *            or FREEZE
	 */
	public void add(Configuration configuration, int state) {
		if (contains(configuration))
			return;
		ConfigurationButton button = new ConfigurationButton(configuration,
				state);
		configurationToButtonMap.put(configuration, button);
		add(button);
		button.addActionListener(this);
	}

	/**
	 * Given a configuration, returns the state for that configuration.
	 * 
	 * @param configuration
	 *            the configuration
	 * @return the state for that configuration
	 */
	public int getState(Configuration configuration) {
		ConfigurationButton button = configurationToButtonMap
				.get(configuration);
		return button == null ? -1 : button.getState();
	}

	/**
	 * Determines if this pane already contains this configuration.
	 * 
	 * @param configuration
	 *            the configuration to test for membership
	 * @return <CODE>true</CODE> if the pane holds this transition,
	 *         <CODE>false</CODE> if it does not
	 */
	private boolean contains(Configuration configuration) {
		return configurationToButtonMap.containsKey(configuration);
	}

	/**
	 * Sets a configuration to be frozen. Only normal configurations can be
	 * frozen.
	 * 
	 * @param configuration
	 *            the configuration to freeze
	 */
	public void setFrozen(Configuration configuration) {
		ConfigurationButton button = (ConfigurationButton) configurationToButtonMap
				.get(configuration);
		if (button == null)
			return;
		button.setState(ConfigurationButton.FREEZE);
		button.doClick();
	}

	/**
	 * @param configuration
	 */
	public void defocus(Configuration configuration) {
		setNormal(configuration);
		// Configuration parent = configuration;
		// parent.
		// while (parent.getParent() != null) {
		// parent = parent.getParent();
		// parent.setFocused(false);
		// }
	}

	/**
	 * Sets a configuration to be normal.
	 * 
	 * @param configuration
	 *            the configuration to thaw or unfocus
	 */
	public void setNormal(Configuration configuration) {
		ConfigurationButton button = (ConfigurationButton) configurationToButtonMap
				.get(configuration);
		if (button == null)
			return;
		int state = button.getState();
		if (state == ConfigurationButton.FREEZE)
			button.setState(ConfigurationButton.NORMAL);
		button.doClick();
	}

	/**
	 * Removes a configuration from the configuration pane.
	 * 
	 * @param configuration
	 *            the configuration to remove
	 */
	public void remove(Configuration configuration) {
		Component comp = configurationToButtonMap.remove(configuration);
		if (comp == null)
			return;
		selected.remove(configuration);
		remove(comp);
	}

	/**
	 * Removes all configurations from this pane.
	 */
	public void clear() {
		configurationToButtonMap.clear();
		selected.clear();
		super.removeAll();
	}

	/**
	 * Renders all components deselected.
	 */
	public void deselectAll() {
		for(Configuration config : selected){
			ConfigurationButton button = configurationToButtonMap.get(config);
			if(button != null && button.isSelected())
				button.setSelected(false);
		}
		selected.clear();
	}

	/**
	 * Returns an array of selected configurations.
	 * 
	 * @return an array of selected configurations
	 */
	public List<Configuration> getSelected() {
		return new ArrayList<Configuration>(selected);
	}

	/**
	 * Returns an array of all configurations.
	 * 
	 * @return an array of all configurations
	 */
	public List<Configuration> getConfigurations() {
		return new ArrayList<Configuration>(configurationToButtonMap.keySet());
	}

	/**
	 * Returns an array of configurations that are, as far as is known, valid
	 * configurations for moving to other configurations.
	 * 
	 * @return an array of "valid" configurations
	 */
	public Configuration[] getValidConfigurations() {
		// A state is valid for return if it is normal.
		ArrayList<Configuration> list = new ArrayList<Configuration>();

		for (Configuration config : configurationToButtonMap.keySet()) {
			ConfigurationButton button = configurationToButtonMap.get(config);

			if (button != null
					&& button.getState() == ConfigurationButton.NORMAL)
				list.add(config);
		}
		return list.toArray(new Configuration[0]);
	}

	/**
	 * Clears out all configurations which are "final" configurations, i.e.,
	 * those that are marked either as accept or reject configurations.
	 */
	public void clearFinal() {
		// Avoid concurrent modification exceptions.
		ArrayList<ConfigurationButton> list = new ArrayList<ConfigurationButton>();
		list.addAll(configurationToButtonMap.values());

		for (ConfigurationButton button : list) {
			if (button.getState() == ConfigurationButton.ACCEPT
					|| button.getState() == ConfigurationButton.REJECT)
				remove(button.getConfiguration());
		}
	}

	/**
	 * Clears old all configurations which are not frozen.
	 */
	public void clearThawed() {
		// Avoid concurrent modification exceptions.
		ArrayList<ConfigurationButton> list = new ArrayList<ConfigurationButton>();
		list.addAll(configurationToButtonMap.values());

		for (ConfigurationButton button : list) {
			if (button.getState() != ConfigurationButton.FREEZE)
				remove(button.getConfiguration());
		}
	}

	/**
	 * Responds to actions, presumably generated by some button belonging to
	 * this view.
	 * 
	 * @param e
	 *            the action event generated
	 */
	public void actionPerformed(ActionEvent e) {
		ConfigurationButton button = null;
		try {
			button = (ConfigurationButton) e.getSource();
		} catch (ClassCastException ex) {
			return; // Then, we don't care.
		}
		Configuration config = button.getConfiguration();
		if (!configurationToButtonMap.containsKey(config))
			return;
		if (button.isSelected())
			selected.add(config);
		else
			selected.remove(config);

	}

}
