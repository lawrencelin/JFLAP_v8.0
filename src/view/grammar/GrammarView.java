package view.grammar;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import model.formaldef.FormalDefinition;
import model.grammar.Grammar;
import model.undo.UndoKeeper;
import universe.preferences.JFLAPPreferences;
import universe.preferences.PreferenceChangeListener;
import view.formaldef.BasicFormalDefinitionView;
import view.formaldef.FormalDefinitionView;
import view.grammar.productions.ProductionTable;

public class GrammarView extends BasicFormalDefinitionView<Grammar>{

	private static final Dimension GRAMMAR_SIZE = new Dimension(500, 600);
	private ProductionTable myTable;

	public GrammarView(Grammar g){
		this(g, new UndoKeeper(), true);
	}
	
	public GrammarView(Grammar definition, UndoKeeper keeper,
			boolean editable) {
		super(definition, keeper, editable);
		setPreferredSize(GRAMMAR_SIZE);
		setMaximumSize(GRAMMAR_SIZE);
	}

	@Override
	public JComponent createCentralPanel(Grammar definition,
			UndoKeeper keeper, boolean editable) {

		return new ProductionTable(definition, 
										keeper, 
										editable);
	}
	
	@Override
	public String getName() {
		return "Grammar Editor";
	}

}
