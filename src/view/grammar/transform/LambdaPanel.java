package view.grammar.transform;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import debug.JFLAPDebug;
import model.algorithms.transform.grammar.LambdaProductionRemover;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.ProductionSet;
import model.grammar.Variable;
import model.symbols.Symbol;
import model.symbols.SymbolString;
import model.undo.UndoKeeper;
import universe.JFLAPUniverse;
import universe.preferences.JFLAPPreferences;
import util.view.magnify.MagnifiablePanel;
import util.view.magnify.MagnifiableScrollPane;
import util.view.tables.GrowableTableModel;
import view.action.grammar.conversion.CNFTransformAction;
import view.grammar.GrammarView;
import view.grammar.productions.ProductionTable;
import view.grammar.productions.ProductionTableModel;
import errors.BooleanWrapper;

public class LambdaPanel extends GrammarTransformPanel {

	private LambdaProductionRemover myAlg;
	private JLabel lambdaDerivingLabel;
	private int editingRow;
	private boolean editingColumn[];
	private boolean editingActive;
	private ProductionTable myEditingTable;
	private AbstractAction completeSelectedAction;
	private AbstractAction deleteAction;
	private AbstractAction doStepAction;
	private AbstractAction doAllAction;
	private AbstractAction proceedAction;
	private Action exportAction;
	private LambdaController myController;

	public LambdaPanel(Grammar g, LambdaProductionRemover remover) {
		super(g, "Lambda Removal");
		myAlg = remover;
		initView();
		myController = new LambdaController(this, myAlg);
		addListeners();
	}

	@Override
	public void productionClicked(Production p) {
		if (myAlg.getNumberUnidentifiedTargets() > 0) {
			String empty = JFLAPPreferences.getEmptyString();

			if (myAlg.isOfTargetForm(p)) {
				BooleanWrapper bw = myAlg.identifyProductionToBeRemoved(p);

				if (bw.isTrue()) {
					setDetailText(p + " added!\n");
					Set<Production> identified = myAlg.getIdentifiedTargets();
					Set<Variable> lhs = new TreeSet<Variable>();

					for (Production prod : identified)
						lhs.add((Variable) prod.getLHS()[0]);
					lambdaDerivingLabel.setText("Set that derives " + empty
							+ ": " + lhs);
				} else
					setDetailText(p + " already selected!\n");

			} else
				setDetailText(p.toString() + " does not derive " + empty
						+ "!\n");

			setDetailText(" " + getDetailLabel().getText()
					+ myAlg.getNumberUnidentifiedTargets()
					+ " more production(s) needed.");
			if (myAlg.getNumberUnidentifiedTargets() == 0)
				myController.updateStep();
		}
	}

	@Override
	public MagnifiablePanel initRightPanel() {
		MagnifiablePanel right = new MagnifiablePanel(new BorderLayout());

		lambdaDerivingLabel = new JLabel(" ");
		lambdaDerivingLabel.setAlignmentX(0.0f);

		MagnifiablePanel panel = new MagnifiablePanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(getMainLabel());
		panel.add(getDetailLabel());
		panel.add(lambdaDerivingLabel);
		initEditingProductionTable();

		initEditingBar(panel);
		panel.add(new MagnifiableScrollPane(myEditingTable));

		JToolBar toolbar = initToolbar();
		right.add(toolbar, BorderLayout.NORTH);

		right.add(panel, BorderLayout.CENTER);

		return right;
	}

	private JToolBar initToolbar() {
		JToolBar toolbar = new JToolBar();
		toolbar.setAlignmentX(0.0f);

		doStepAction = new AbstractAction("Step") {

			@Override
			public void actionPerformed(ActionEvent e) {
				performStep();
			}
		};
		toolbar.add(doStepAction);

		doAllAction = new AbstractAction("Step to Completion") {

			@Override
			public void actionPerformed(ActionEvent e) {
				while (myAlg.canStep())
					performStep();
			}
		};
		toolbar.add(doAllAction);
		toolbar.addSeparator();

		proceedAction = new AbstractAction("Proceed") {

			@Override
			public void actionPerformed(ActionEvent e) {
				proceed();
			}
		};
		toolbar.add(proceedAction);

		exportAction = new AbstractAction("Export") {

			@Override
			public void actionPerformed(ActionEvent e) {
				export();
			}
		};
		toolbar.add(exportAction);
		return toolbar;
	}

	private void proceed() {
		if (!myAlg.isRunning())
			CNFTransformAction
					.hypothesizeUnit(myAlg.getTransformedDefinition());
		else
			JOptionPane.showMessageDialog(this, "Lamda removal is not complete!");
	}

	private void export() {
		if (myAlg.isRunning())
			JOptionPane.showMessageDialog(this, "Lamda removal is not complete!");
		else {
			GrammarView view = new GrammarView(myAlg.getTransformedDefinition());
			JFLAPUniverse.registerEnvironment(view);
		}
	}

	private void initEditingBar(MagnifiablePanel panel) {
		JToolBar editingBar = new JToolBar();
		editingBar.setAlignmentX(0.0f);
		editingBar.setFloatable(false);

		deleteAction = new AbstractAction("Delete") {
			public void actionPerformed(ActionEvent e) {
				myController.deleteActivated();
			}
		};
		completeSelectedAction = new AbstractAction("Complete Selected") {
			public void actionPerformed(ActionEvent e) {
				cancelEditing();
				ProductionTable table = getTable();
				for (int i = 0; i < table.getRowCount() - 1; i++)
					if (table.isRowSelected(i))
						myController.expandRowProduction(i);
			}
		};

		editingBar.add(deleteAction);
		editingBar.add(completeSelectedAction);
		panel.add(editingBar);
	}

	private void addListeners() {
		ProductionTable grammarTable = getTable();
		grammarTable.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent event) {
						myController.updateButtonEnabledness();
					}
				});

		myEditingTable.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent event) {
						myController.updateButtonEnabledness();
					}
				});

		final ProductionTableModel model = (ProductionTableModel) myEditingTable
				.getModel();
		model.addTableModelListener(new TableModelListener() {

			@Override
			public void tableChanged(TableModelEvent event) {
				if (!editingActive)
					return;
				int r = event.getFirstRow();
				if (event.getType() != TableModelEvent.UPDATE)
					return;
//				System.out.println("here");
				if (editingColumn[0] == true && editingColumn[1] == true) {
					Production p = model.getOrderedProductions()[r];
//					System.out.println(p.toString());
					if (p == null)
						return;
					BooleanWrapper bw = myAlg.performAdd(p);

					if (bw.isError()) {
						myEditingTable.deleteSelected();
						JOptionPane.showMessageDialog(LambdaPanel.this,
								bw.getMessage());
					} else if (!myAlg.isRunning())
						myController.updateStep();
					
					myController.updateButtonEnabledness();
					myController.updateDisplay();
					editingRow = -1;
				}
				if (event.getColumn() != TableModelEvent.ALL_COLUMNS)
					editingColumn[event.getColumn() >> 1] = true;
			}
		});

		Object o = new Object();
		myEditingTable.getInputMap().put(
				KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), o);
		myEditingTable.getInputMap().put(
				KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), o);
		myEditingTable.getActionMap().put(o, deleteAction);
	}

	private void initEditingProductionTable() {
		Grammar g = new Grammar();
		UndoKeeper keeper = new UndoKeeper();
		editingRow = -1;
		editingColumn = new boolean[2];
		editingActive = false;

		ProductionTableModel model = new ProductionTableModel(g, keeper) {
			@Override
			public boolean isCellEditable(int row, int column) {
				if (myAlg.getNumberUnidentifiedTargets() != 0
						|| myAlg.getNumAddsRemaining() == 0
						|| !myAlg.isRunning())
					return false;
				if (column == 1)
					return false;
				if (editingRow == -1) {
					if (row == getRowCount() - 1) {
						editingRow = row;
						editingColumn[0] = editingColumn[1] = false;
						return true;
					}
					return false;
				} else
					return editingRow == row;
			}
		};

		myEditingTable = new ProductionTable(g, keeper, true, model);
	}

	public void cancelEditing() {
		if (myEditingTable.getCellEditor() != null)
			myEditingTable.getCellEditor().stopCellEditing();
		if (editingRow != -1) {
			((GrowableTableModel) myEditingTable.getModel())
					.deleteRow(editingRow);
			editingRow = -1;
		}
	}
	
	public void setDeleteEnabled(boolean b) {
		deleteAction.setEnabled(b);
	}

	public void setCompleteEnabled(boolean b) {
		completeSelectedAction.setEnabled(b);
	}

	public void setStepEnabled(boolean b) {
		doStepAction.setEnabled(b);
	}

	public void setStepCompleteEnabled(boolean b) {
		doAllAction.setEnabled(b);
	}

	public void setProceedEnabled(boolean b) {
		proceedAction.setEnabled(b);
	}

	public void setExportEnabled(boolean b) {
		exportAction.setEnabled(b);
	}

	public void addProduction(Production p) {
		String lhs = new SymbolString(p.getLHS()).toString();
		String rhs = new SymbolString(p.getRHS()).toString();
		int row = myEditingTable.getRowCount() - 1;

		myEditingTable.setValueAt(lhs, row, 0);
		myEditingTable.setValueAt(rhs, row, 2);
	}

	public void setEditable(boolean b) {
		editingActive = b;
	}

	public ProductionTable getEditingTable() {
		return myEditingTable;
	}

	private void performStep() {
		if (myAlg.canStep()){
			cancelEditing();
			
			if(myAlg.getNumberUnidentifiedTargets() > 0){
				myAlg.step();
				myController.updateStep();
			} else if(myAlg.getNumRemovesRemaining() > 0){
				Production remove = myAlg.getFirstRemove();
				ProductionTableModel model = (ProductionTableModel) myEditingTable.getModel();
				
				for(int i=0; i<myEditingTable.getRowCount(); i++){
					if(remove.equals(model.getOrderedProductions()[i])){
						myAlg.performRemove(remove);
						model.remove(i);
						break;
					}
				}
			}else {
				//Add
				Production add = myAlg.getFirstAdd();
				editingActive = false;
				myAlg.performAdd(add);
				addProduction(add);
				editingActive = true;
			}
			myController.updateDisplay();
		}
	}
}
