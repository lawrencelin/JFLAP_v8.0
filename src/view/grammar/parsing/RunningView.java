package view.grammar.parsing;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;

import model.algorithms.testinput.parse.Parser;
import universe.preferences.JFLAPPreferences;
import util.view.magnify.MagnifiablePanel;

/**
 * Main panel for FindFirstParsers (Brute force and CYK) that consists of a
 * table used for display/interaction.
 * 
 * @author Ian McMahon
 * 
 */
public abstract class RunningView extends MagnifiablePanel {

	private JTable myTable;

	public RunningView(String name, Parser parser) {
		setName(name);
		setLayout(new BorderLayout());

		myTable = new JTable(createModel(parser));
		JTableHeader header = myTable.getTableHeader();

		header.setReorderingAllowed(false);
		header.setResizingAllowed(false);

		JScrollPane panel = new JScrollPane(myTable);
		add(panel, BorderLayout.CENTER);
	}

	public abstract AbstractTableModel createModel(Parser parser);

	public JTable getTable() {
		return myTable;
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
