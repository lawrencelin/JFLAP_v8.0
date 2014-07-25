package view.grammar.transform;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import model.grammar.Grammar;
import model.grammar.Production;
import model.undo.UndoKeeper;
import util.view.magnify.MagnifiablePanel;
import util.view.magnify.MagnifiableSplitPane;
import util.view.magnify.SizeSlider;
import view.grammar.productions.ProductionTable;
import view.grammar.productions.ProductionTableModel;

public abstract class GrammarTransformPanel extends MagnifiablePanel {

	private Grammar myGrammar;
	private ProductionTable myProdTable;
	private JLabel mainLabel;
	private JLabel detailLabel;

	public GrammarTransformPanel(Grammar g, String name) {
		super(new BorderLayout());
		setName(name);
		myGrammar = g;
	}

	public void initView() {
		initProductionTable();
		JScrollPane lScroll = new JScrollPane(myProdTable);
		lScroll.setMinimumSize(myProdTable.getMinimumSize());
		

		mainLabel = new JLabel(" ");
		detailLabel = new JLabel(" ");

		mainLabel.setAlignmentX(0.0f);
		detailLabel.setAlignmentX(0.0f);
		
		MagnifiablePanel panel = initRightPanel();
		MagnifiableSplitPane split = new MagnifiableSplitPane(JSplitPane.HORIZONTAL_SPLIT, lScroll, panel);
		split.setDividerLocation(0.4);
		split.setResizeWeight(0.4);

		SizeSlider slide = new SizeSlider(myProdTable, panel);
		slide.distributeMagnification();
		
		add(split, BorderLayout.CENTER);
		add(slide, BorderLayout.SOUTH);
		
		Dimension rSize = panel.getMinimumSize();
		setPreferredSize(new Dimension(2*rSize.width, getPreferredSize().height));
	}

	private void initProductionTable() {
		UndoKeeper keeper = new UndoKeeper();
		ProductionTableModel model = new ProductionTableModel(myGrammar, keeper) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		myProdTable = new ProductionTable(myGrammar, keeper, true, model);
		myProdTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Point at = e.getPoint();
				int row = myProdTable.rowAtPoint(at);
				if (row == -1)
					return;
				if (row == myProdTable.getModel().getRowCount() - 1)
					return;
				Production[] prods = ((ProductionTableModel) myProdTable
						.getModel()).getOrderedProductions();
				Production p = prods[row];
				productionClicked(p);
			}
		});
	}
	
	public ProductionTable getTable() {
		return myProdTable;
	}

	public abstract void productionClicked(Production p);
	public abstract MagnifiablePanel initRightPanel();
	


	public void setMainText(String text) {
		mainLabel.setText(text);
	}

	public void setDetailText(String text) {
		detailLabel.setText(text);
	}

	public JLabel getMainLabel() {
		return mainLabel;
	}

	public JLabel getDetailLabel() {
		return detailLabel;
	}

}
