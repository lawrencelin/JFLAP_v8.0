package view.regex;

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



import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import model.automata.State;
import model.automata.acceptors.fsa.FSATransition;

/**
 * This shows a bunch of transitions for the step of the conversion when the
 * states of the automaton are being removed one by one. A
 * {@link gui.regular.FSAToREController} object is reported back to when certain
 * actions happen in the window.
 * 
 * @see gui.regular.FSAToREController#finalizeStateRemove
 * @see gui.regular.FSAToREController#finalize
 * 
 * @author Thomas Finley
 */

public class TransitionWindow extends JFrame {

	private static final String[] COLUMN_NAMES = { "From", "To", "Label" };
	
	private List<FSATransition> transitions;
	private FAToREController myController;
	private State myState;

	private JTable table;

	/**
	 * Instantiates a new <CODE>TransitionWindow</CODE>.
	 * 
	 * @param controller
	 *            the FSA to RE controller object
	 */
	public TransitionWindow(State s, FAToREController controller) {
		super("Transitions");
		// Init the GUI.
		setSize(250, 400);
		myController = controller;
		myState = s;
		transitions = new ArrayList<FSATransition>();
		table = new JTable(new TransitionTableModel(transitions));
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(
				new JLabel("Select to see what transitions were combined."),
				BorderLayout.NORTH);
		getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);
		getContentPane().add(new JButton(new AbstractAction("Finalize") {
			public void actionPerformed(ActionEvent e) {
				myController.finalizeStateRemove(myState);
			}
		}), BorderLayout.SOUTH);
		// Have the listener to the transition.
		table.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent e) {
						if (table.getSelectedRowCount() != 1) {
							myController.tableTransitionSelected(myState, null);
							return;
						}
						FSATransition t = transitions.get(table.getSelectedRow());
						myController.tableTransitionSelected(myState, t);
					}
				});
	}

	public void setTransitions(Collection<FSATransition> trans) {
		transitions.clear();
		transitions.addAll(trans);
		table.setModel(new TransitionTableModel(transitions));
	}

	private class TransitionTableModel extends AbstractTableModel {

		private FSATransition[] transitions;
		
		/**
		 * Instantiates a new <CODE>TransitionTableModel</CODE>.
		 * 
		 * @param transitions
		 *            the array of transitions to create the table for
		 */
		public TransitionTableModel(Collection<FSATransition> transitions) {
			this.transitions = transitions.toArray(new FSATransition[0]);
		}

		public TransitionTableModel() {
			this(new TreeSet<FSATransition>());
		}

		public int getColumnCount() {
			return 3;
		}

		public int getRowCount() {
			return transitions.length;
		}

		public FSATransition getTransition(int row) {
			return transitions[row];
		}

		
		public Object getValueAt(int row, int column) {
			switch (column) {
			case 0:
				return "" + transitions[row].getFromState().getName();
			case 1:
				return "" + transitions[row].getToState().getName();
			case 2:
				return transitions[row].getLabelText();
			default:
				return null;
			}
		}

		public String getColumnName(int column) {
			return COLUMN_NAMES[column];
		}
	}

}
