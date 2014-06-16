package view.grammar.productions;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.EventObject;

import javax.swing.CellEditor;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;


import debug.JFLAPDebug;

import model.formaldef.components.SetComponent;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.ProductionSet;
import model.undo.UndoKeeper;
import universe.preferences.JFLAPPreferences;
import util.ISelector;
import util.JFLAPConstants;
import util.view.magnify.Magnifiable;
import util.view.tables.HighlightTable;
import util.view.tables.SelectingEditor;

public class ProductionTable extends HighlightTable 
						implements JFLAPConstants, Magnifiable, ChangeListener, ISelector{

	private UndoKeeper myKeeper;


	/**
	 * Instantiates a <CODE>GrammarTable</CODE> with a given table model.
	 * @param model 
	 * 
	 * @param model
	 *            the table model for the new grammar table
	 * @param keeper 
	 * @param editable 
	 */
	public ProductionTable(Grammar g, UndoKeeper keeper, boolean editable) {
		this(g, keeper, editable, new ProductionTableModel(g, keeper));
	}

	public ProductionTable(Grammar g, UndoKeeper keeper, boolean editable, ProductionTableModel model){
		super(model);
		setEnabled(editable);
		g.getProductionSet().addListener(this);
		initView();
		myKeeper = keeper;
	}



	/**
	 * This constructor helper function customizes the view of the table.
	 */
	private void initView() {
		setTableHeader(new JTableHeader(getColumnModel()));
		getTableHeader().setReorderingAllowed(false);
		getTableHeader().setResizingAllowed(true);

		//set up LHS column
		TableColumn lhs = getColumnModel().getColumn(0);
		lhs.setPreferredWidth(70);
		lhs.setMinWidth(70);
		lhs.setMaxWidth(200);
		lhs.setCellEditor(createEditor());

		//set up arrow column
		TableColumn arrows = getColumnModel().getColumn(1);
		arrows.setMaxWidth(30);
		arrows.setMinWidth(30);
		arrows.setPreferredWidth(30);

		//set up RHS column
		TableColumn rhs = getColumnModel().getColumn(2);
		rhs.setCellEditor(createEditor());
		rhs.setMinWidth(70);


		setShowGrid(true);
		setGridColor(Color.lightGray);
		this.rowHeight = 30;
		this.setFont(DEFAULT_FONT);
		getColumnModel().getColumn(2).setCellRenderer(RENDERER);
	}

	private TableCellEditor createEditor() {
		return new LambdaRemovingEditor(){
			@Override
			public boolean isCellEditable(EventObject e) {
				if (e instanceof KeyEvent){
					int mod = ((KeyEvent) e).getModifiers();
					if (mod == JFLAPConstants.MAIN_MENU_MASK)
						return false;
				}
				return super.isCellEditable(e);
			}
		};
	}

	/** Modified to use the set renderer highlighter. */
	public void highlight(int row, int column) {
		highlight(row, column, THRG);
	}


	/** The built in highlight renderer generator, modified. */
	private TableHighlighterRendererGenerator THRG = new TableHighlighterRendererGenerator() {
		public TableCellRenderer getRenderer(int row, int column) {
			if (renderer == null) {
				renderer = new DefaultTableCellRenderer();
				renderer.setBackground(new Color(255, 150, 150));
			}
			return renderer;
		}

		private DefaultTableCellRenderer renderer = null;
	};

	/** The lambda cell renderer. */
	private TableCellRenderer RENDERER = new DefaultTableCellRenderer();


	@Override
	public TableCellRenderer getCellRenderer(int row, int column) {
		return super.getCellRenderer(row, column);
	}




	@Override
	public void setMagnification(double mag) {
		float size = (float) (mag*JFLAPPreferences.getDefaultTextSize());
        this.setFont(this.getFont().deriveFont(size));
        this.setRowHeight((int) (size+10));
	}




	@Override
	public void stateChanged(ChangeEvent e) {
		this.revalidate();
		this.repaint();
	}




	@Override
	public boolean deleteSelected() {
		int[] rows = getSelectedRows();
		myKeeper.beginCombine();
		boolean shouldAdd = false;
		for (int i = 0; i < rows.length; i++){
			int index = rows[i]-i;
			shouldAdd = ((ProductionTableModel) getModel()).remove(index);
			if (!shouldAdd) break;
		}
		myKeeper.endCombine(shouldAdd);

		setRowSelectionInterval(0, 0);
		updateUI();
		return shouldAdd;
	}

}
