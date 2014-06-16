package view.algorithms.conversion.autotogram;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import debug.JFLAPDebug;
import model.algorithms.conversion.autotogram.AutomatonToGrammarConversion;
import model.algorithms.conversion.autotogram.VariableMapping;
import model.automata.Automaton;
import model.automata.State;
import model.automata.Transition;
import model.change.events.AddEvent;
import model.change.events.RemoveEvent;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.ProductionSet;
import model.undo.UndoKeeper;
import universe.JFLAPUniverse;
import util.view.magnify.MagnifiableScrollPane;
import util.view.magnify.SizeSlider;
import view.automata.AutomatonDisplayPanel;
import view.automata.editing.AutomatonEditorPanel;
import view.automata.simulate.TooltipAction;
import view.automata.tools.algorithm.ArrowDisplayOnlyTool;
import view.environment.JFLAPEnvironment;
import view.grammar.GrammarView;
import view.grammar.productions.ProductionTable;
import view.grammar.productions.ProductionTableModel;

public abstract class AutoToGramConversionPanel<T extends Automaton<S>, S extends Transition<S>, R extends VariableMapping>
		extends AutomatonDisplayPanel<T, S> {

	private AutomatonToGrammarConversion<T, R, S> myAlg;
	private ProductionTable myTable;
	private Map<Integer, Object> myObjectMap;

	public AutoToGramConversionPanel(AutomatonEditorPanel<T, S> editor,
			AutomatonToGrammarConversion<T, R, S> convert) {
		super(editor, convert.getAutomaton(), "Convert to Grammar");
		myAlg = convert;
		//This is temporary?
		myAlg.doAllAutomaticVariableMappings();
		
		myObjectMap = new TreeMap<Integer, Object>();
		updateSize();
		initView();
	}


	public abstract boolean addProductionForState(State clicked);

	public abstract void addOtherProduction();

	public abstract void highlightOtherObjects();
	
	public abstract void addAllOtherProductions();

	private void initView() {
		AutomatonEditorPanel<T, S> panel = getEditorPanel();
		T auto = panel.getAutomaton();

		initTable(myAlg.getConvertedGrammar(), new UndoKeeper());
		panel.setTool(createArrowTool(panel, auto));

		MagnifiableScrollPane scroll = new MagnifiableScrollPane(myTable);
		scroll.setMinimumSize(myTable.getMinimumSize());
		
		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panel,
				scroll);

		Dimension size = getPreferredSize(), 
					scrollSize = myTable.getMinimumSize();
		double width = size.width + scrollSize.width;
		split.setPreferredSize(new Dimension((int) width, size.height));

		double ratio = size.width / width;
		split.setDividerLocation(ratio);
		split.setResizeWeight(ratio);

		SizeSlider slider = new SizeSlider(scroll);
		JToolBar toolbar = initToolbar();

		add(split, BorderLayout.CENTER);
		add(slider, BorderLayout.SOUTH);
		add(toolbar, BorderLayout.NORTH);

		slider.distributeMagnification();

		size = new Dimension((int) width, size.height
				+ slider.getPreferredSize().height
				+ toolbar.getPreferredSize().height);
		setPreferredSize(size);
	}

	private void initTable(Grammar converted, UndoKeeper keeper) {
		final AutoProductionDataHelper helper = new AutoProductionDataHelper(
				converted, keeper);
		ProductionTableModel model = new ProductionTableModel(converted,
				keeper, helper) {
			public boolean isCellEditable(int row, int column) {
				return false;
			};
		};
		myTable = new ProductionTable(converted, keeper, true, model);

		myTable.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent e) {
						changeSelection();
					}
				});

		converted.getProductionSet().addListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				if (e instanceof AddEvent) {
					for (Object o : ((AddEvent) e).getToAdd()) {
						Production p = (Production) o;
						helper.add(getMaxRow(),
								new Object[] { p.getLHS(), null, p.getRHS() });
					}
				}
			}
		});
	}

	private ArrowDisplayOnlyTool<T, S> createArrowTool(AutomatonEditorPanel<T, S> panel, T auto) {
		return new ArrowDisplayOnlyTool<T, S>(panel, auto) {
			@Override
			public void mousePressed(MouseEvent e) {
				AutomatonEditorPanel<T, S> panel = getPanel();
				clearSelection();
				
				super.mousePressed(e);
				Object clicked = panel.objectAtPoint(e.getPoint());
				
				if (clicked instanceof State || clicked instanceof Transition) {
					int row = getMaxRow();
					
					if (addProductions(clicked)) {
						updateObjectMap(clicked, row);
					} else 
						panel.deselectObject(clicked);
				}
			}
		};
	}
	
	public void updateObjectMap(Object clicked, int row) {
		for (int i = row; i < myTable.getRowCount() - 1; i++)
			myObjectMap.put(i, clicked);
		myTable.setRowSelectionInterval(row, myTable.getRowCount() - 2);
	}

	private void changeSelection() {
		ListSelectionModel model = myTable.getSelectionModel();
		AutomatonEditorPanel<T, S> panel = getEditorPanel();
		panel.clearSelection();

		int min = model.getMinSelectionIndex(), max = model
				.getMaxSelectionIndex();
		if (min < 0)
			return;
		for (int i = min; i <= max; i++)
			if (model.isSelectedIndex(i) && myObjectMap.containsKey(i))
				panel.selectObject(myObjectMap.get(i));
	}

	private boolean addProductions(Object clicked) {
		if (clicked instanceof Transition) {
			if (!myAlg.convertAndAddTransition((S) clicked)) {
				JOptionPane.showMessageDialog(
						JFLAPUniverse.getActiveEnvironment(),
						"This object has already been converted!");
				return false;
			}
			return true;
		}

		else
			return addProductionForState((State) clicked);
	}

	/**
	 * This helper initializes a toolbar to do stuff with the automaton.
	 * 
	 * @param controller
	 *            the converter controller
	 */
	private JToolBar initToolbar() {
		JToolBar bar = new JToolBar();
		bar.add(new TooltipAction("Step",
				"Shows the productions for one object.") {
			public void actionPerformed(ActionEvent e) {
				clearSelection();
				S[] transitions = (S[]) myAlg.getUnconvertedTransitions()
						.toArray(new Transition[0]);
				if (transitions.length > 0) {
					int row = getMaxRow();
					if(addProductions(transitions[0]))
						updateObjectMap(transitions[0], row);
				} else
					addOtherProduction();
			}
		});
		bar.add(new TooltipAction("Step To Completion",
				"Shows all productions remaining.") {
			public void actionPerformed(ActionEvent e) {
				for(S trans : myAlg.getUnconvertedTransitions()){
					int row = getMaxRow();
					if(addProductions(trans))
						updateObjectMap(trans, row);
				}
				addAllOtherProductions();
				clearSelection();
			}
		});
		bar.addSeparator();
		bar.add(new TooltipAction("What's Left?",
				"Highlights remaining objects to convert.") {
			public void actionPerformed(ActionEvent e) {
				highlightUntransformed();
			}
		});
		bar.add(new TooltipAction("Export",
				"Exports a finished grammar to a new window.") {
			public void actionPerformed(ActionEvent e) {
				exportGrammar();
			}
		});
		return bar;
	}

	public void exportGrammar() {
		JFLAPEnvironment env = JFLAPUniverse.getActiveEnvironment();
		if (!myAlg.isComplete()) {
			highlightUntransformed();
			JOptionPane
					.showMessageDialog(
							env,
							"Conversion unfinished!  Objects to convert are highlighted.",
							"Conversion Unfinished", JOptionPane.ERROR_MESSAGE);
			changeSelection();
			return;
		}
		Grammar g = myAlg.getConvertedGrammar().copy();
		GrammarView view = new GrammarView(g);
		JFLAPUniverse.registerEnvironment(view);
	}

	private void highlightUntransformed() {
		AutomatonEditorPanel<T, S> panel = getEditorPanel();
		clearSelection();
		
		for (S trans : myAlg.getUnconvertedTransitions())
			panel.selectObject(trans);
		highlightOtherObjects();
	}


	private void clearSelection() {
		getEditorPanel().clearSelection();
		myTable.clearSelection();
	}

	public AutomatonToGrammarConversion<T, R, S> getAlgorithm() {
		return myAlg;
	}


	public int getMaxRow() {
		return myTable.getRowCount() - 1;
	}
}
