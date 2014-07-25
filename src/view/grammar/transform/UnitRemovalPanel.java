package view.grammar.transform;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import errors.BooleanWrapper;

import model.algorithms.conversion.gramtoauto.RGtoFSAConverter;
import model.algorithms.transform.grammar.DependencyGraph;
import model.algorithms.transform.grammar.UnitProductionRemover;
import model.automata.Automaton;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.grammar.Grammar;
import model.grammar.Production;
import model.symbols.SymbolString;
import model.undo.UndoKeeper;
import universe.JFLAPUniverse;
import util.view.magnify.Magnifiable;
import util.view.magnify.MagnifiablePanel;
import util.view.magnify.MagnifiableScrollPane;
import util.view.tables.GrowableTableModel;
import view.action.grammar.conversion.CNFTransformAction;
import view.automata.AutomatonDisplayPanel;
import view.automata.editing.AutomatonEditorPanel;
import view.grammar.GrammarView;
import view.grammar.productions.ProductionTable;
import view.grammar.productions.ProductionTableModel;

public class UnitRemovalPanel extends GrammarTransformPanel {

	private UnitProductionRemover myAlg;
	private Grammar myGrammar;
	private UnitRemovalController myController;
	private AbstractAction completeSelectedAction;
	private AbstractAction deleteAction;
	private AbstractAction doStepAction;
	private AbstractAction doAllAction;
	private AbstractAction proceedAction;
	private Action exportAction;
	private int editingRow;
	private boolean editingColumn[];
	private boolean editingActive;
	private ProductionTable myEditingTable;
	private DependencyGraphPanel myDependencyGraphPanel;
	private JPanel myPanel;
	private int dependenciesAdded;

	public UnitRemovalPanel(Grammar g, UnitProductionRemover remover) {
		super(g, "Unit Removal");
		myGrammar = g;
		myAlg = remover;
		myAlg.setPanel(this);
		dependenciesAdded = 0;
		myController = new UnitRemovalController(this, remover);
//		myController = new UnitRemovalController(this, myAlg);
		initView();
	}
	
	public void completeDependencyGraph() {
		myDependencyGraphPanel.completeDependencyGraph();
		myController.initializeGrammarEditing();
	}
	
	public DependencyGraph getDependencyGraph() {
		return myDependencyGraphPanel.getDependencyGraph();
	}
	
	/**
	 * Add action listeners to the editing table. 
	 */
	public void addTableListeners() {
		ProductionTable grammarTable = getTable();
		grammarTable.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent event) {
//						myController.updateButtonEnabledness();
					}
				});

		myEditingTable.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent event) {
//						myController.updateButtonEnabledness();
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
				if (editingColumn[0] == true && editingColumn[1] == true) {
//					System.out.println(r);
//					System.out.println(model.getOrderedProductions()[r-1]);
					Production p = model.getOrderedProductions()[r];
					if (p == null)
						return;
					BooleanWrapper bw = myAlg.performAdd(p);
//					myController.checkForTableCompletion();
					myController.updateLabels();
					myController.updateButtonEnabledness();

					if (bw.isError()) {
						myEditingTable.deleteSelected();
						JOptionPane.showMessageDialog(myEditingTable,
								bw.getMessage());
					} else if (!myAlg.isRunning())
//						myController.updateStep();
					
//					myController.updateButtonEnabledness();
//					myController.updateDisplay();
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
	
	public ProductionTable getTable() {
		return myEditingTable;
	}

	@Override
	public void productionClicked(Production p) {
		
	}

	@Override
	public MagnifiablePanel initRightPanel() {
		MagnifiablePanel right = new MagnifiablePanel(new BorderLayout());
		myDependencyGraphPanel = new DependencyGraphPanel(new UndoKeeper(), true, myGrammar, myAlg, myController);
		right.add(initToolPanel(), BorderLayout.NORTH);
		SplitPane splitPane = new SplitPane(JSplitPane.VERTICAL_SPLIT,
				myDependencyGraphPanel, initTablePanel());
		right.add(splitPane, BorderLayout.CENTER);
//		right.add(myDependencyGraphPanel, BorderLayout.CENTER);
//		right.add(initTablePanel(), BorderLayout.SOUTH);
//		myController.addOriginalGrammarToTable();
		myController.updateButtonEnabledness();
		myController.updateLabels();
//		right.revalidate();
//		right.repaint();
		return right;
	}
	
	public Grammar getGrammar() {
		return myGrammar;
	}

	/**
	 * Disable the Transition tool of the variable dependency graph. 
	 */
	public void resetToolBar() {
		myDependencyGraphPanel.resetToolBar();
	}

	public int getNumberDependenciesLeft() {
		return myDependencyGraphPanel.getNumberDependenciesLeft();
	}
	
	private MagnifiablePanel initTablePanel() {
		MagnifiablePanel panel = new MagnifiablePanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		initEditingProductionTable();
		initEditingBar(panel);
		panel.add(new MagnifiableScrollPane(myEditingTable));
		return panel;
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
	
	public ProductionTable getEditingTable() {
		return myEditingTable;
	}
	
	private void proceed() {
		if (!myAlg.isRunning())
			CNFTransformAction
					.hypothesizeUnit(myAlg.getTransformedDefinition());
		else
			JOptionPane.showMessageDialog(this, "Unit removal is not complete!");
	}

	private void export() {
		if (myAlg.isRunning())
			JOptionPane.showMessageDialog(this, "Unit removal is not complete!");
		else {
			GrammarView view = new GrammarView(myAlg.getTransformedDefinition());
			JFLAPUniverse.registerEnvironment(view);
		}
	}
	
	private void performStep() {
		myAlg.step();
//		if (myAlg.canStep()){
//			cancelEditing();
//			
//			if(myAlg.getNumberUnidentifiedTargets() > 0){
//				myAlg.step();
////				myController.updateStep();
//			} else if(myAlg.getNumRemovesRemaining() > 0){
//				Production remove = myAlg.getFirstRemove();
//				ProductionTableModel model = (ProductionTableModel) myEditingTable.getModel();
//				
//				for(int i=0; i<myEditingTable.getRowCount(); i++){
//					if(remove.equals(model.getOrderedProductions()[i])){
//						myAlg.performRemove(remove);
//						model.remove(i);
//						break;
//					}
//				}
//			}else {
//				//Add
//				Production add = myAlg.getFirstAdd();
//				editingActive = false;
//				myAlg.performAdd(add);
//				addProduction(add);
//				editingActive = true;
//			}
////			myController.updateDisplay();
//		}
	}
	
	public void setEditable(boolean b) {
		editingActive = b;
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
	
	private void initEditingBar(MagnifiablePanel panel) {
		JToolBar editingBar = new JToolBar();
		editingBar.setAlignmentX(0.0f);
		editingBar.setFloatable(false);

		deleteAction = new AbstractAction("Delete") {
			public void actionPerformed(ActionEvent e) {
				myController.deleteHighlightedProduction();
			}
		};
		completeSelectedAction = new AbstractAction("Complete Selected") {
			public void actionPerformed(ActionEvent e) {
				cancelEditing();
				ProductionTable table = getTable();
//				for (int i = 0; i < table.getRowCount() - 1; i++)
//					if (table.isRowSelected(i))
//						myController.expandRowProduction(i);
			}
		};

		editingBar.add(deleteAction);
		editingBar.add(completeSelectedAction);
		panel.add(editingBar);
	}
	
	private void initEditingProductionTable() {
		Grammar g = new Grammar();
		UndoKeeper keeper = new UndoKeeper();
		editingRow = -1;
		editingColumn = new boolean[2];
//		editingActive = false;
		editingActive = true;

		ProductionTableModel model = new ProductionTableModel(g, keeper) {
			@Override
			public boolean isCellEditable(int row, int column) {
//				if (myAlg.getNumberUnidentifiedTargets() != 0
//						|| myAlg.getNumAddsRemaining() == 0
//						|| !myAlg.isRunning())
//					return false;
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
//				return true;
			}
				
		};

		myEditingTable = new ProductionTable(g, keeper, true, model);
//		System.out.println(((ProductionTableModel) myEditingTable.getModel());
	}

	public void addProduction(Production p) {
		String lhs = new SymbolString(p.getLHS()).toString();
		String rhs = new SymbolString(p.getRHS()).toString();
		int row = myEditingTable.getRowCount() - 1;

		myEditingTable.setValueAt(lhs, row, 0);
		myEditingTable.setValueAt(rhs, row, 2);
	}
	
	public MagnifiablePanel initToolPanel() {
		MagnifiablePanel p = new MagnifiablePanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.add(initToolbar());
		p.add(getMainLabel());
		p.add(getDetailLabel());
		return p;
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
	
	private class SplitPane extends JSplitPane implements Magnifiable {
		
		public SplitPane (int newOrientation,
                Component newLeftComponent,
                Component newRightComponent) {
			super(newOrientation, newLeftComponent, newRightComponent);
		}
		
		@Override
		public void setMagnification(double mag) {
			myDependencyGraphPanel.setMagnification(mag);
			myEditingTable.setMagnification(mag);
		}
		
	}

}
