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

package view.lsystem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import debug.JFLAPDebug;

import model.change.events.AdvancedUndoableEvent;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.Terminal;
import model.lsystem.LSystem;
import model.lsystem.LSystemException;
import model.symbols.Symbol;
import model.undo.UndoKeeper;
import universe.preferences.JFLAPMode;
import universe.preferences.JFLAPPreferences;
import util.view.magnify.MagnifiableLabel;
import util.view.magnify.MagnifiablePanel;
import util.view.magnify.MagnifiableScrollPane;
import util.view.magnify.MagnifiableSplitPane;
import util.view.magnify.MagnifiableTextField;
import view.formaldef.BasicFormalDefinitionView;
import view.grammar.productions.ProductionDataHelper;
import view.grammar.productions.ProductionTable;
import view.grammar.productions.ProductionTableModel;
import view.lsystem.helperclasses.Axiom;
import view.lsystem.helperclasses.Parameter;
import view.lsystem.helperclasses.ParameterMap;
import view.lsystem.parameters.ParameterTable;
import view.lsystem.parameters.ParameterTableModel;

/**
 * The <CODE>LSystemInputPane</CODE> is a pane used to input and display the
 * textual representation of an L-system.
 * 
 * @author Thomas Finley, Ian McMahon
 */

public class LSystemInputView extends BasicFormalDefinitionView<LSystem> {

	private static final Dimension LSYSTEM_INPUT_SIZE = new Dimension(500, 650);

	private MagnifiableTextField axiomField;
	private ProductionTable myProdTable;
	private ParameterTableModel parameterModel;
	private ParameterTable parameterTable;
	private LSystem cachedSystem = null;
	private TableCellRenderer myRenderer = new NumberBoldingRenderer();

	/**
	 * Instantiates an empty <CODE>LSystemInputView</CODE>.
	 */
	public LSystemInputView() {
		this(new LSystem());
	}

	/**
	 * Instantiates an <CODE>LSystemInputView</CODE> for a given
	 * <CODE>LSystem</CODE>.
	 * 
	 * @param lsystem
	 *            the lsystem to display
	 */
	public LSystemInputView(LSystem lsystem) {
		super(lsystem, new UndoKeeper(), true);
		setPreferredSize(LSYSTEM_INPUT_SIZE);
		setMaximumSize(LSYSTEM_INPUT_SIZE);
	}

	@Override
	public String getName() {
		return "L System";
	}

	@Override
	public JComponent createCentralPanel(LSystem model, UndoKeeper keeper,
			boolean editable) {
		initializeStructures(model, keeper);
		MagnifiablePanel central = new MagnifiablePanel(new BorderLayout());

		MagnifiablePanel axiomView = createAxiomView();
		central.add(axiomView, BorderLayout.NORTH);

		MagnifiableScrollPane scroller = new MagnifiableScrollPane(
				parameterTable);
		scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setPreferredSize(new Dimension(400, 200));

		MagnifiableScrollPane prodScroller = createProductionScroller(model,
				keeper, new Dimension(400, 210));

		MagnifiableSplitPane split = new MagnifiableSplitPane(
				JSplitPane.VERTICAL_SPLIT, prodScroller, scroller);
		split.setDividerLocation(235);
		central.add(split, BorderLayout.CENTER);

		// Finally, show the grid.
		parameterTable.setShowGrid(true);
		parameterTable.setGridColor(Color.lightGray);

		JLabel c = createParameterMenu();

		scroller.setCorner(JScrollPane.UPPER_RIGHT_CORNER, c);
		initializeListener();
		return central;
	}

	/**
	 * Initializes the data structures and the subviews.
	 * 
	 * @param lsystem
	 *            the L-system to initialize the views on
	 */
	private void initializeStructures(LSystem lsystem, UndoKeeper keeper) {
		// Create the axiom text field.
		axiomField = new MagnifiableTextField(
				JFLAPPreferences.getDefaultTextSize());
		String axiom = lsystem.getAxiom().toString();
		axiomField
				.setText(axiom == JFLAPPreferences.getEmptyString() ? ""
						: axiom);

		// Create the parameter table model.
		parameterModel = new ParameterTableModel(lsystem, keeper);
		parameterTable = new ParameterTable(parameterModel);

		// We may as well use this as our cached system.
		cachedSystem = lsystem;
	}

	/**
	 * @return MagnifiablePanel for user to input Axiom data.
	 */
	private MagnifiablePanel createAxiomView() {
		MagnifiablePanel axiomView = new MagnifiablePanel(new BorderLayout());
		axiomView.add(
				new MagnifiableLabel("Axiom: ", JFLAPPreferences
						.getDefaultTextSize()), BorderLayout.WEST);
		axiomView.add(axiomField, BorderLayout.CENTER);
		return axiomView;
	}

	/**
	 * 
	 * @param model
	 *            The view's initial LSystem
	 * @param keeper
	 *            UndoKeeper for the view
	 * @param bestSize
	 *            Dimension defining the preferred size of the production table
	 * @return A ProductionTable representing the LSystem's production rules
	 *         contained in a MagnifiableScrollPane
	 */
	private MagnifiableScrollPane createProductionScroller(LSystem model,
			UndoKeeper keeper, Dimension bestSize) {
		Grammar g = model.getGrammar();
		LSystemDataHelper helper = new LSystemDataHelper(g, keeper);
		ProductionTableModel tableModel = new ProductionTableModel(g, keeper,
				helper);

		myProdTable = new ProductionTable(g, keeper, true, tableModel) {
			@Override
			public TableCellRenderer getCellRenderer(int row, int column) {
				if (column == 0)
					return myRenderer;
				return super.getCellRenderer(row, column);
			}
		};
		myProdTable.setPreferredSize(bestSize);

		MagnifiableScrollPane prodScroller = new MagnifiableScrollPane(
				myProdTable);
		prodScroller.setPreferredSize(bestSize);
		return prodScroller;
	}

	/**
	 * 
	 * @return JLabel for the area in which, when clicked, the popup menu
	 *         containing default Parameters to autofill the ParameterTable will
	 *         appear.
	 */
	private JLabel createParameterMenu() {
		JLabel c = new JLabel();
		final JPopupMenu menu = new JPopupMenu();

		ActionListener listener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setEditing(e.getActionCommand());
			}
		};

		String[] words = (String[]) Renderer.ASSIGN_WORDS
				.toArray(new String[0]);
		for (int i = 0; i < words.length; i++) {
			menu.add(words[i]).addActionListener(listener);
		}

		c.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				menu.show((Component) e.getSource(), e.getPoint().x,
						e.getPoint().y);
			}
		});
		c.setText(" P");
		return c;
	}

	/**
	 * Creates the listener to update the edited-ness.
	 */
	public void initializeListener() {
		//Simulates pressing enter when axiom field is tabbed/clicked off of
		axiomField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				super.focusLost(e);

				Object obj = e.getSource();
				if (obj.equals(axiomField))
					axiomField.dispatchEvent(new KeyEvent(axiomField,
							KeyEvent.KEY_PRESSED, System.currentTimeMillis(),
							0, KeyEvent.VK_ENTER, '0'));
			}

		});
		//The actual code to run on the axiomField
		axiomField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				fireAxiomInputEvent();
			}
		});

		TableModelListener paramTML = new TableModelListener() {
			public void tableChanged(TableModelEvent e) {
				fireParamInputEvent();
			}
		};

		TableModelListener prodTML = new TableModelListener() {
			public void tableChanged(TableModelEvent e) {
				fireGrammarInputEvent();
			}
		};
		parameterModel.addTableModelListener(paramTML);
		myProdTable.getModel().addTableModelListener(prodTML);
	}

	/**
	 * @return the L-system this view displays
	 */
	public LSystem getLSystem() {
		// Make sure we're not editing anything.
		if (myProdTable.getCellEditor() != null)
			myProdTable.getCellEditor().stopCellEditing();
		if (parameterTable.getCellEditor() != null)
			parameterTable.getCellEditor().stopCellEditing();
		// Do we already have a cached copy?
		try {
			if (cachedSystem == null) {
				Grammar g = ((ProductionTableModel) myProdTable.getModel())
						.getGrammar();
				cachedSystem = new LSystem(new Axiom(axiomField.getText()), g,
						parameterModel.getParameters());
			}
		} catch (IllegalArgumentException e) {
			throw new LSystemException(
					"Error in retrieving the L-System for rendering!");
		}
		return cachedSystem;
	}

	/**
	 * Signals that the L-system was changed by nullifying the cachedSystem.
	 */
	protected void fireLSystemInputEvent() {
		cachedSystem = null;
	}

	/**
	 * Changes the L-System's Axiom to match the newly input text in the
	 * axiomField.
	 */
	private void fireAxiomInputEvent() {
		String text = axiomField.getText();
		text = text.trim();
		text = text.replaceAll("\\s+", " ");
//		axiomField.setText(text);
		Axiom to = new Axiom(text);
		
		LSystem system = getDefinition();
		Axiom from = system.getAxiom();
//		system.setAxiom(to);
		getKeeper().applyAndListen(new SetAxiomEvent(from, from, to));
		
		fireLSystemInputEvent();
	}

	/**
	 * Changes the L-System's Parameters to match to newly input data in the
	 * parameterTable.
	 */
	private void fireParamInputEvent() {
		LSystem system = getDefinition();
		system.setParameters(parameterModel.getParameters());
		fireLSystemInputEvent();
	}

	/**
	 * Changes the L-System's Grammar to match to newly input data in the
	 * productionTable.
	 */
	private void fireGrammarInputEvent() {
		LSystem system = getDefinition();
		Grammar g = ((ProductionTableModel) myProdTable.getModel())
				.getGrammar();

		system.setGrammar(g);
		fireLSystemInputEvent();
	}

	/**
	 * This will edit the value for a particular parameter in the parameter
	 * table. If no such value exists yet it shall be created. The value field
	 * in the table shall be edited.
	 * 
	 * @param item
	 *            the key of the value we want to edit
	 */
	private void setEditing(String item) {
		int i;
		for (i = 0; i < parameterModel.getRowCount(); i++) {
			String value = (String) parameterModel.getValueAt(i, 0);
			if (value != null && value.equals(item))
				break;
		}
		if (i == parameterModel.getRowCount()) // We need to create it.
			parameterModel.setValueAt(item, --i, 0);
		int column = parameterTable.convertColumnIndexToView(1);

		parameterTable.editCellAt(i, column);
		parameterTable.requestFocus();
	}

	/**
	 * The modified table cell renderer. If a production is context-sensitive,
	 * the number will be hidden and the Symbol at that index will be bolded. As
	 * it renders using HTML, it will also replace all special HTML symbols with
	 * their HTML encoding.
	 * 
	 */
	private class NumberBoldingRenderer extends DefaultTableCellRenderer {
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			JLabel l = (JLabel) super.getTableCellRendererComponent(table,
					value, isSelected, hasFocus, row, column);
			if (l != null) {
				// Splits on whitespace.
				String[] lhs = l.getText().trim().split("\\s+");
				if (lhs.length > 1 && Character.isDigit(lhs[0].charAt(0))) {
					int index = lhs[0].charAt(0) - '0' + 1;

					if (index < lhs.length) {
						StringBuilder left = new StringBuilder(), right = new StringBuilder();
						for (int i = 1; i < lhs.length; i++) {
							lhs[i] = lhs[i].replaceAll("&", "&amp;");
							lhs[i] = lhs[i].replaceAll("\"", "&quot;");
							lhs[i] = lhs[i].replaceAll("<", "&lt;");
							lhs[i] = lhs[i].replaceAll(">", "&gt;");
							if (i < index)
								left.append(lhs[i] + " ");
							else if (i > index)
								right.append(lhs[i] + " ");
						}
						l.setText(String.format("<html>%s<b>%s</b>%s</html>",
								left.toString(), lhs[index] + " ",
								right.toString()).trim());
					}
				}
			}
			return l;
		}
	}

	/**
	 * Modified ProductionDataHelper, specific to L-Systems to fix an issue that
	 * arose with symbols that included Symbols already in the grammar not being
	 * parsed by the Symbolizer correctly. Any symbol in the production table
	 * that contains a valid command/terminal is added to the grammar prior to
	 * symbolizing.
	 * 
	 * @author Ian McMahon
	 * 
	 */
	private class LSystemDataHelper extends ProductionDataHelper {

		public LSystemDataHelper(Grammar model, UndoKeeper keeper) {
			super(model, keeper);
		}

		@Override
		protected Production objectToProduction(Object[] input) {
			if (isEmptyString((String) input[0]))
				input[0] = "";
			if (isEmptyString((String) input[2]))
				input[2] = "";
			String[] LHS = ((String) input[0]).trim().split("\\s+"), RHS = ((String) input[2])
					.trim().split("\\s+");

			for (String l : LHS)
				if (containsExistingSymbol(l))
					myGrammar.getLanguageAlphabet().add(new Terminal(l));
			for (String r : RHS)
				if (containsExistingSymbol(r))
					myGrammar.getLanguageAlphabet().add(new Terminal(r));
			return super.objectToProduction(input);
		}

		private boolean containsExistingSymbol(String s) {

			for (Symbol symbol : myGrammar.getTerminals()) {
				if (s.contains(symbol.toString()) && !s.equals(symbol.toString()))
					return true;
			}
			for (Symbol symbol : myGrammar.getVariables()) {
				if (s.contains(symbol.toString()) && !s.equals(symbol.toString()))
					return true;
			}			
			return false;
		}
	}

	
	private class SetAxiomEvent extends AdvancedUndoableEvent {

		public SetAxiomEvent(Axiom source, Axiom from, Axiom to) {
			super(source, ITEM_MODIFIED, from, to);
		}

		@Override
		public boolean undo() {
			axiomField.setText(getFrom().toString());
			return getDefinition().setAxiom(getFrom());
		}
		
		@Override
		public boolean redo() {
			axiomField.setText(getTo().toString());
			return getDefinition().setAxiom(getTo());
		}
		
		@Override
		public Axiom getSource() {
			return (Axiom) super.getSource();
		}
		
		public Axiom getFrom(){
			return (Axiom) getArg(0);
		}
		
		public Axiom getTo(){
			return (Axiom) getArg(1);
		}

		@Override
		public String getName() {
			return "Set " + getFrom() + " to " + getTo();
		}

	}
}
