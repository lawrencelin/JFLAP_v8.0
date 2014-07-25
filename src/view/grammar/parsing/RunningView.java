package view.grammar.parsing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;

import model.algorithms.testinput.parse.Parser;
import universe.preferences.JFLAPPreferences;
import util.view.magnify.MagnifiablePanel;
import view.grammar.parsing.ll.LLParseTable;

/**
 * Main panel for FindFirstParsers (Brute force and CYK) that consists of a
 * table used for display/interaction.
 * 
 * @author Ian McMahon
 * 
 */
public abstract class RunningView extends MagnifiablePanel {

	private JTable myTable;
	private JTable mySecondTable;
	private JScrollPane myPanel;
	private JScrollPane mySecondPane;
	private LLParseTable llTable;

	public RunningView(String name, Parser parser) {
		this.initialize(name, parser);
		add(myPanel, BorderLayout.CENTER);
	}

	public RunningView(String name, Parser parser, boolean split){
		this.initialize(name,parser);
		
		llTable = new LLParseTable(parser.getGrammar());
		mySecondTable = new JTable(llTable);
		mySecondPane = new JScrollPane(mySecondTable);
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				myPanel, mySecondPane);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(150);

		Dimension minimumSize = new Dimension(100, 50);
		myPanel.setMinimumSize(minimumSize);
		mySecondPane.setMinimumSize(minimumSize);
		if (split){
			this.add(splitPane, BorderLayout.CENTER);
		}
		else
			System.out.println("messed up split panes in running view");
	}
	
	public void initialize(String name, Parser parser){
		setName(name);
		setLayout(new BorderLayout());

		myTable = new JTable(createModel(parser));
		JTableHeader header = myTable.getTableHeader();

		header.setReorderingAllowed(false);
		header.setResizingAllowed(false);

		myPanel = new JScrollPane(myTable);
	}

	public abstract AbstractTableModel createModel(Parser parser);

	public JTable getTable() {
		return myTable;
	}
	
	public LLParseTable getParseTable(){
		return llTable;
	}
	
	public JTable getSecondTable() {
		return mySecondTable;
	}
	
	public JScrollPane getSecondPane(){
		return mySecondPane;
	}

	@Override
	public void setMagnification(double mag) {
		super.setMagnification(mag);
		float size = (float) (mag * JFLAPPreferences.getDefaultTextSize());
		Font font = this.getFont().deriveFont(size);
		myTable.setFont(font);
		myTable.getTableHeader().setFont(font);
		myTable.setRowHeight((int) (size + 10));
	}

}
