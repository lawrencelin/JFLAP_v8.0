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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.table.TableColumnModel;

import model.algorithms.testinput.simulate.Configuration;
import model.algorithms.testinput.simulate.ConfigurationChain;
import model.algorithms.testinput.simulate.SingleInputSimulator;
import model.algorithms.testinput.simulate.configurations.InputOutputConfiguration;
import model.automata.Automaton;
import model.automata.transducers.Transducer;
import model.automata.turing.MultiTapeTuringMachine;
import model.symbols.SymbolString;
import model.symbols.symbolizer.Symbolizers;
import oldnewstuff.view.tree.InputTableModel;
import universe.JFLAPUniverse;
import universe.preferences.JFLAPPreferences;
import util.JFLAPConstants;
import util.view.magnify.MagnifiableScrollPane;
import util.view.magnify.MagnifiableTable;
import util.view.magnify.SizeSlider;
import view.automata.AutomatonDisplayPanel;
import view.automata.editing.AutomatonEditorPanel;
import view.automata.simulate.TraceWindow;
import view.automata.views.AutomatonView;
import view.environment.JFLAPEnvironment;
import view.grammar.productions.LambdaRemovingEditor;
import file.XMLFileChooser;

/**
 * This is the action used for the simulation of multiple inputs on an automaton
 * with no interaction. This method can operate on any automaton.
 * 
 * @author Thomas Finley
 * @modified by Kyung Min (Jason) Lee
 */

public class MultipleSimulateAction extends FastSimulateAction {

	protected JTable table;
	private static String[] RESULT = { "Accept", "Reject", "Cancelled" };
	private static Color[] RESULT_COLOR = { Color.green, Color.red, Color.black };

	/**
	 * Instantiates a new <CODE>MultipleSimulateAction</CODE>.
	 * 
	 * @param automaton
	 *            the automaton that input will be simulated on
	 * @param environment
	 *            the environment object that we shall add our simulator pane to
	 */
	public MultipleSimulateAction(AutomatonView view) {
		super(view);
		putValue(NAME, "Multiple Run");
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_M,
				JFLAPConstants.MAIN_MENU_MASK));
	}

	/**
	 * Handles the creation of the multiple input pane.
	 * 
	 * @param e
	 *            the action event
	 */
	public void actionPerformed(ActionEvent e) {
		if (actionPermissible())
			performAction();
	}

	/**
	 * Returns the title for the type of compontent we will add to the
	 * environment.
	 * 
	 * @return in this base class, returns "Multiple Inputs"
	 */
	public String getComponentTitle() {
		return "Multiple Run";
	}

	public JTable initializeTable() {
		InputTableModel model = InputTableModel.getModel(getAutomaton(), false);

		MagnifiableTable table = new MagnifiableTable(model);
		TableColumnModel tcmodel = table.getColumnModel();
		for (int i = model.getInputCount(); i > 0; i--) {
			tcmodel.removeColumn(tcmodel.getColumn(model.getInputCount()));
		}

		LambdaRemovingEditor lambda = new LambdaRemovingEditor();
		for (int i = 0; i < table.getColumnCount(); i++)
			table.getColumnModel().getColumn(i).setCellEditor(lambda);

		table.setShowGrid(true);
		table.setGridColor(Color.lightGray);
		//TODO: don't allow for dragging of columns
		return table;
	}

	public void performAction() {
		Automaton auto = getAutomaton();

		table = initializeTable();
		
		JPanel panel = new JPanel(new BorderLayout());
		JToolBar bar = new JToolBar();
		MagnifiableScrollPane tableScroller = new MagnifiableScrollPane(table);
		SizeSlider slider = new SizeSlider(tableScroller);
		
		
		panel.add(tableScroller, BorderLayout.CENTER);
		panel.add(bar, BorderLayout.SOUTH);
		panel.add(slider, BorderLayout.NORTH);
		
		slider.distributeMagnification();

		// Load inputs
		bar.add(new AbstractAction("Load Inputs") {

			public void actionPerformed(ActionEvent e) {
				load();
			}

		});
		// Add the running input thing.
		bar.add(new AbstractAction("Run Inputs") {
			public void actionPerformed(ActionEvent e) {
				run();
			}
		});

		// Add the clear button.
		bar.add(new AbstractAction("Clear") {
			public void actionPerformed(ActionEvent e) {
				clear();
			}
		});
		String empty = JFLAPPreferences.getEmptyString();

		bar.add(new AbstractAction("Enter " + empty) {
			public void actionPerformed(ActionEvent e) {
				int row = table.getSelectedRow();
				if (row == -1)
					return;
				for (int column = 0; column < table.getColumnCount() - 1; column++)
					table.getModel().setValueAt("", row, column);
			}
		});

		bar.add(new AbstractAction("View Trace") {
			public void actionPerformed(ActionEvent e) {
				showTrace(e);
			}

		});
		MultiplePane mp = new MultiplePane(getEditorPanel(), panel);

		JFLAPUniverse.getActiveEnvironment().addSelectedComponent(mp);
	}

	private void load() {
		try {
			// Make sure any recent changes are registered.
			table.getCellEditor().stopCellEditing();
		} catch (NullPointerException exception) {
			// We weren't editing anything, so we're OK.
		}
		InputTableModel model = (InputTableModel) table.getModel();
		JFileChooser ourChooser = new XMLFileChooser(false);

		int retval = ourChooser.showOpenDialog(null);

		if (retval == JFileChooser.APPROVE_OPTION) {
			File f = ourChooser.getSelectedFile();
			try {
				Scanner sc = new Scanner(f);
				int last = model.getRowCount() - 1;

				while (sc.hasNext()) {
					// System.out.println(temp);
					String temp = sc.next();
					model.setValueAt(temp, last, 0);
					last++;
				}
			} catch (FileNotFoundException e1) {
				// TODO Auto-generate catch block
				e1.printStackTrace();
			}
		}
	}

	private void run() {
		try {
			// Make sure any recent changes are registered.
			table.getCellEditor().stopCellEditing();
		} catch (NullPointerException exception) {
			// We weren't editing anything, so we're OK.
		}
		InputTableModel model = (InputTableModel) table.getModel();
		Automaton auto = getAutomaton();

		String[][] inputs = model.getInputs();
		int tapes = 1;

		if (auto instanceof MultiTapeTuringMachine)
			tapes = ((MultiTapeTuringMachine) auto).getNumTapes();

		SingleInputSimulator sim = new SingleInputSimulator(auto, false);

		for (int i = 0; i < inputs.length; i++) {
			SymbolString[] symbols = new SymbolString[tapes];

			for (int j = 0; j < tapes; j++) {
				String in = inputs[i][j];
				if (in == null || in.equals(JFLAPPreferences.getEmptyString()))
					in = "";
				symbols[j] = Symbolizers.symbolize(in, auto);
			}
			sim.beginSimulation(symbols);

			JFLAPEnvironment env = JFLAPUniverse.getActiveEnvironment();
			int numberGenerated = 0;
			int warningGenerated = WARNING_STEP;
			int result = 1;
			ConfigurationChain returnChain = null;

			Set<ConfigurationChain> configs;

			loop: while (!(configs = sim.getChains()).isEmpty()) {
				numberGenerated += configs.size();
				// Make sure we should continue.
				if (numberGenerated >= warningGenerated) {
					if (!confirmContinue(numberGenerated, env)) {
						result = 2;
						break loop;
					}
					while (numberGenerated >= warningGenerated)
						warningGenerated *= 2;
				}

				for (ConfigurationChain chain : configs) {
					if (chain.isAccept()) {
						result = 0;
						returnChain = chain;
						break loop;
					}
					if (chain.isReject() && returnChain == null)
						returnChain = chain;
				}
				sim.step();
			}
			if (result == 0) {
				Configuration config = returnChain.getCurrentConfiguration();

				if (auto instanceof Transducer) {
					InputOutputConfiguration output = (InputOutputConfiguration) config;
					model.setResult(i, output.getOutput().toString(),
							returnChain);
				} else
					model.setResult(i, RESULT[result], returnChain);
			} else if (result == 2)
				model.setResult(i, RESULT[result], null);

			else
				model.setResult(i, RESULT[result], returnChain);

		}
	}

	private void clear() {
		try {
			// Make sure any recent changes are registered.
			table.getCellEditor().stopCellEditing();
		} catch (NullPointerException exception) {
			// We weren't editing anything, so we're OK.
		}
		InputTableModel model = (InputTableModel) table.getModel();
		model.clear();
	}

	private void showTrace(ActionEvent e) {
		int[] rows = table.getSelectedRows();
		InputTableModel tm = (InputTableModel) table.getModel();
		List nonassociatedRows = new ArrayList();

		for (int i = 0; i < rows.length; i++) {
			if (rows[i] == tm.getRowCount() - 1)
				continue;
			ConfigurationChain chain = tm
					.getAssociatedConfigurationForRow(rows[i]);
			if (chain == null) {
				nonassociatedRows.add(new Integer(rows[i] + 1));
				continue;
			}
			TraceWindow window = new TraceWindow(chain);
			window.setVisible(true);
			window.toFront();
		}
		// Print the warning message about rows without
		// configurations we could display.setValueAt
		if (nonassociatedRows.size() > 0) {
			StringBuffer sb = new StringBuffer("Row");
			if (nonassociatedRows.size() > 1)
				sb.append("s");
			sb.append(" ");
			sb.append(nonassociatedRows.get(0));
			for (int i = 1; i < nonassociatedRows.size(); i++) {
				if (i == nonassociatedRows.size() - 1) {
					// Last one!
					sb.append(" and ");
				} else {
					sb.append(", ");
				}
				sb.append(nonassociatedRows.get(i));
			}
			sb.append("\ndo");
			if (nonassociatedRows.size() == 1)
				sb.append("es");
			sb.append(" not have end configurations.");
			JOptionPane.showMessageDialog((Component) e.getSource(),
					sb.toString(), "Bad Rows Selected",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private class MultiplePane extends AutomatonDisplayPanel {
		private JSplitPane split;

		public MultiplePane(AutomatonEditorPanel editor, JPanel info) {
			super(editor, editor.getAutomaton(), "Multiple Run");
			updateSize();

			Dimension size = getPreferredSize(), infoSize = info
					.getMinimumSize();
			split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
					getEditorPanel(), info);
			setPreferredSize(new Dimension(size.width + infoSize.width,
					size.height));
			double ratio = ((double) (size.width))
					/ (size.width + infoSize.width);

			split.setResizeWeight(ratio);

			add(split, BorderLayout.CENTER);
		}
	}

}
