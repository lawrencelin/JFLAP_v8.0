package view.grammar.transform;

import javax.swing.JOptionPane;

import view.grammar.productions.ProductionTable;
import view.grammar.productions.ProductionTableModel;

import model.algorithms.transform.grammar.UnitProductionRemover;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.ProductionSet;

/**
 * Controller for the unit removal panel
 * @author Lawrence Lin
 *
 */
public class UnitRemovalController {
	private static final String STEP_TWO = "Modify the grammar to remove unit productions.";
	private UnitRemovalPanel myPanel;
	private UnitProductionRemover myRemover;
	private int step;

	public UnitRemovalController (UnitRemovalPanel panel, UnitProductionRemover remover) {
		myPanel = panel;
		myRemover = remover;
		step = 0;
	}

	public void updateDependencies() {
		myPanel.setDetailText(myPanel.getNumberDependenciesLeft() + " more transition(s) needed. ");
		myPanel.repaint();
		if (myPanel.getNumberDependenciesLeft() == 0) {
			initializeGrammarEditing();
		}
	}

	public void initializeGrammarEditing() {
		myPanel.resetToolBar();
		step++;
		addOriginalGrammarToTable();
		myPanel.addTableListeners();
		updateLabels();
		updateButtonEnabledness();
	}

	/**
	 * Update the main and detail panels of the unit removal panels. 
	 * The labels typically contain the status and instructions. 
	 */
	public void updateLabels() {
		if (step == 0) {
			int dependencies = myPanel.getNumberDependenciesLeft();
			myPanel.setMainText("Complete unit production visualization.");
			myPanel.setDetailText(dependencies + " more transition(s) needed.");
		} else if (step == 1) {
			myPanel.setMainText(STEP_TWO);
			myPanel.setDetailText(myRemover.getNumRemovesRemaining() + " more removes, and " +
					myRemover.getNumAddsRemaining() + " more additions needed.");
		} else {
			myPanel.setMainText("Unit removal complete.");
			myPanel.setDetailText("\"Proceed\" or \"Expor\" available.");
		}
	}

	public void updateButtonEnabledness() {
		if (step == 0) {
			myPanel.setDeleteEnabled(false);
			myPanel.setCompleteEnabled(false);
			myPanel.setProceedEnabled(false);
			myPanel.setExportEnabled(false);
		} else if (step == 1) {
			myPanel.setEditable(true);
			myPanel.setDeleteEnabled(true);
			myPanel.setCompleteEnabled(true);
		} else {
			myPanel.setEditable(false);
			myPanel.setDeleteEnabled(false);
			myPanel.setCompleteEnabled(false);
			myPanel.setProceedEnabled(true);
			myPanel.setExportEnabled(true);
		}
	}
	
//	public void checkForTableCompletion() {
//		if (myRemover.getNumAddsRemaining() == 0 && myRemover.getNumRemovesRemaining() == 0) {
//			step++;
//		}
//	}

	/**
	 * Add the original grammar to be displayed in the production table. 
	 * This is called when the variable dependency graph is completed. 
	 */
	private void addOriginalGrammarToTable() {
		ProductionSet prods = myRemover.getTransformedGrammar()
				.getProductionSet();

		for (Production p : prods) {
			myPanel.addProduction(p);
		}
	}

	public void deleteHighlightedProduction() {
		//		if (remover.getNumberUnidentifiedTargets() != 0 || !remover.isRunning())
		//			return;
		myPanel.cancelEditing();
		ProductionTable editingTable = myPanel.getEditingTable();

		int deleted = 0, kept = 0;

		for (int i = editingTable.getRowCount() - 2; i >= 0; i--) {
			if (!editingTable.isRowSelected(i))
				continue;
			ProductionTableModel model = (ProductionTableModel) editingTable
					.getModel();
			Production p = model.getOrderedProductions()[i];
			if (myRemover.performRemove(p).isTrue()) {
				deleted++;
			} else {
				editingTable.removeRowSelectionInterval(i, i);
				kept++;
			}
		}
		editingTable.deleteSelected();

		if (kept != 0) {
			JOptionPane.showMessageDialog(myPanel, kept
					+ " production(s) selected should not be removed.\n"
					+ deleted + " production(s) were removed.",
					"Bad Selection", JOptionPane.ERROR_MESSAGE);
		}
		if (deleted != 0) {
			//			updateDisplay();
			updateLabels();
		}

	}


}
