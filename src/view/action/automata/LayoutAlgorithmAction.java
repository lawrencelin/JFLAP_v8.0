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

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import model.automata.Automaton;
import model.automata.Transition;
import model.automata.acceptors.pda.PushdownAutomaton;
import model.automata.transducers.mealy.MealyMachine;
import model.automata.turing.MultiTapeTuringMachine;
import model.graph.LayoutAlgorithm;
import model.graph.LayoutAlgorithmFactory;
import util.JFLAPConstants;
import view.automata.editing.AutomatonEditorPanel;
import view.automata.views.AutomatonView;

/**
 * This action allows for a layout algorithm to be applied to an automaton.
 * 
 * @author Chris Morgan, Ian McMahon
 */
public class LayoutAlgorithmAction<T extends Automaton<S>, S extends Transition<S>> extends AutomatonAction {
	/**
	 * Used to specify the specific algorithm chosen.  For the list of algorithm identifiers,
	 * look in <code>automata.graph.LayoutAlgorithmFactory</code>.
	 */
	private int algorithm;		 
	
	/**
	 * Constructor.
	 * 
	 * @param s 
	 *     the title of this action.
	 * @param a 
	 *     the automaton this action will move.
	 * @param e 
	 *     the environment this automaton is in.
	 * @param algm 
	 *     a numerical identifier for the algorithm that will be used.  The constants
	 *     utilized are stored in automata.graph.LayoutAlgorithmFactory.
	 */
	public LayoutAlgorithmAction(String s, AutomatonView<T, S> view, int algm) {
		super(s, view);
		algorithm = algm;
	}
	
	public void actionPerformed(ActionEvent e) {		
		double vertexBuffer;
		AutomatonEditorPanel<T, S> panel = getEditorPanel();
		T automaton = panel.getAutomaton();
		
		if (automaton instanceof MultiTapeTuringMachine)
			vertexBuffer = 80 * ((MultiTapeTuringMachine) automaton).getNumTapes();
		else if (automaton instanceof PushdownAutomaton)
			vertexBuffer = 80;
		else if (automaton instanceof MealyMachine)
			vertexBuffer = 65;
		else
			vertexBuffer = JFLAPConstants.STATE_RADIUS * 2;
		
		Rectangle visible = panel.getVisibleRect();
		
		LayoutAlgorithm layout = LayoutAlgorithmFactory.getLayoutAlgorithm(algorithm, new Dimension(visible.width, visible.height), 
				new Dimension(JFLAPConstants.STATE_RADIUS+5, JFLAPConstants.STATE_RADIUS + 5), vertexBuffer);
		
		panel.setLayoutAlgorithm(layout);
		panel.layoutGraph();
	}
}
