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

import java.awt.Color;
import java.awt.event.KeyEvent;

import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.TableModel;

import oldnewstuff.view.tree.InputTableModel;
import util.JFLAPConstants;
import util.view.magnify.MagnifiableTable;
import view.automata.views.TuringMachineView;
import view.grammar.productions.LambdaRemovingEditor;

/**
 * This is the action used for the simulation of multiple inputs on an automaton
 * with no interaction, and it also produces the output that a machine produces.
 * This is useful in situations where you are running input on a Turing machine
 * as a transducer. This is almost identical to its superclass except for a few
 * different names, and this one does not remove the columns corresponding to
 * the output.
 * 
 * @author Thomas Finley
 */

public class MultipleTransducerSimulateAction extends MultipleSimulateAction {
	/**
	 * Instantiates a new <CODE>MultipleOuptutSimulateAction</CODE>.
	 * 
	 * @param automaton
	 *            the automaton that input will be simulated on
	 * @param environment
	 *            the environment object that we shall add our simulator pane to
	 */
	public MultipleTransducerSimulateAction(TuringMachineView view) {
		super(view);
		putValue(NAME, "Multiple Run (Transducer)");
         putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_T,
                    JFLAPConstants.MAIN_MENU_MASK));
	}

	@Override
	public JTable initializeTable() {
		TableModel model = InputTableModel.getModel(getAutomaton(), false);
		
		MagnifiableTable table = new MagnifiableTable(model);
		
		LambdaRemovingEditor lambda = new LambdaRemovingEditor();
		for(int i=0; i < table.getColumnCount(); i++)
			table.getColumnModel().getColumn(i).setCellEditor(lambda);
		
		table.setShowGrid(true);
		table.setGridColor(Color.lightGray);
		
		return table;
	}
}
