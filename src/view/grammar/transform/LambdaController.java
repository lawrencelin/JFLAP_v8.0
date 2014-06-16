package view.grammar.transform;

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

import java.awt.event.MouseEvent;
import java.util.Set;

import javax.swing.JOptionPane;

import model.algorithms.transform.grammar.LambdaProductionRemover;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.ProductionSet;
import model.symbols.SymbolString;
import view.grammar.productions.ProductionTable;
import view.grammar.productions.ProductionTableModel;
import errors.BooleanWrapper;

/**
 * This is the controller for the lambda panel.
 * 
 * @author Thomas Finley
 */

public class LambdaController {
	private LambdaPanel pane;
	private LambdaProductionRemover remover;

	/**
	 * This instantiates a new lambda controller.
	 * 
	 * @param pane
	 *            the lambda panel
	 * @param grammar
	 *            the grammar to produce
	 */
	public LambdaController(LambdaPanel pane, LambdaProductionRemover remover) {
		this.pane = pane;
		this.remover = remover;
		updateStep();
	}

	/**
	 * This is called to move the lambda controller to the next step.
	 */
	public void updateStep() {

		if (remover.getNumberUnidentifiedTargets() > 0) {
			pane.setMainText("Select variables that derive lambda.");
			pane.setDetailText("Click productions; the LHS variable will be added.");

			pane.setDeleteEnabled(false);
			pane.setCompleteEnabled(false);

			pane.setStepEnabled(true);
			pane.setStepCompleteEnabled(true);
			pane.setProceedEnabled(false);
			pane.setExportEnabled(false);
		} else if (remover.getNumAddsRemaining() > 0
				|| remover.getNumRemovesRemaining() > 0) {
			updateButtonEnabledness();
			pane.setMainText("Modify the grammar to remove lambdas.");
			ProductionSet prods = remover.getTransformedGrammar()
					.getProductionSet();

			for (Production p : prods) {
				pane.addProduction(p);
			}
			pane.setEditable(true);
			updateDisplay();
		} else {
			pane.setDeleteEnabled(false);
			pane.setCompleteEnabled(false);
			pane.setMainText("Lambda removal complete.");
			pane.setDetailText("\"Proceed\" or \"Export\" available.");

			pane.setStepEnabled(false);
			pane.setStepCompleteEnabled(false);
			pane.setProceedEnabled(true);
			pane.setExportEnabled(true);
		}
	}

	/**
	 * Does the expansion of the production in the given row of the left grammar
	 * panel.
	 * 
	 * @param row
	 *            the row of the production to expand
	 */
	public void expandRowProduction(int row) {
		ProductionTableModel model = (ProductionTableModel) pane.getTable()
				.getModel();
		Production p = model.getOrderedProductions()[row];

		Set<Production> ps = remover.getProductionsToAddForRemoval(p);
		if (ps == null || ps.isEmpty())
			return;
		pane.setEditable(false);

		for (Production prod : ps) {
			BooleanWrapper bw = remover.performAdd(prod);
			if (bw.isTrue())
				pane.addProduction(prod);
		}
		pane.setEditable(true);
		updateDisplay();
	}

	/**
	 * Updates the detail display to show how many more removes, and additions
	 * are needed in the grammar modification step.
	 */
	void updateDisplay() {
		int toRemove = remover.getNumRemovesRemaining();
		int toAdd = remover.getNumAddsRemaining();
		
		pane.setDetailText(toRemove + " more remove(s), and " + toAdd
				+ " more addition(s) needed.");
		if (toAdd == 0 && toRemove == 0)
			updateStep();
	}
	
	public void updateButtonEnabledness() {
		if (remover.getNumberUnidentifiedTargets() > 0 || !remover.isRunning()) {
			pane.setCompleteEnabled(false);
			pane.setDeleteEnabled(false);
			return;
		}
		ProductionTable table = pane.getTable();
		ProductionTable editingTable = pane.getEditingTable();
		
		int lmin = table.getSelectionModel().getMinSelectionIndex();
		int rmin = editingTable.getSelectionModel().getMinSelectionIndex();

		pane.setCompleteEnabled(lmin != -1 && lmin < table.getRowCount() - 1);
		pane.setDeleteEnabled(rmin != -1 && rmin < editingTable.getRowCount() - 1);

	}


	public void deleteActivated() {
		if (remover.getNumberUnidentifiedTargets() != 0 || !remover.isRunning())
			return;
		pane.cancelEditing();
		ProductionTable editingTable = pane.getEditingTable();
		
		int deleted = 0, kept = 0;
		
		for (int i = editingTable.getRowCount() - 2; i >= 0; i--) {
			if (!editingTable.isRowSelected(i))
				continue;
			ProductionTableModel model = (ProductionTableModel) editingTable
					.getModel();
			Production p = model.getOrderedProductions()[i];
			if (remover.performRemove(p).isTrue()) {
				deleted++;
			} else {
				editingTable.removeRowSelectionInterval(i, i);
				kept++;
			}
		}
		editingTable.deleteSelected();
		
		if (kept != 0) {
			JOptionPane.showMessageDialog(pane, kept
					+ " production(s) selected should not be removed.\n"
					+ deleted + " production(s) were removed.",
					"Bad Selection", JOptionPane.ERROR_MESSAGE);
		}
		if (deleted != 0) {
			updateDisplay();
		}
	}
}
