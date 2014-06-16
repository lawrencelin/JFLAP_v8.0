package view.regex;

import java.awt.Dimension;

import javax.swing.JComponent;

import model.regex.RegularExpression;
import model.undo.UndoKeeper;
import view.formaldef.BasicFormalDefinitionView;

public class RegexView extends BasicFormalDefinitionView<RegularExpression>{

	private static final Dimension REGEX_SIZE = new Dimension(700, 300);

	public RegexView(RegularExpression model, UndoKeeper keeper,
			boolean editable) {
		super(model, keeper, editable);
		setPreferredSize(REGEX_SIZE);
		setMaximumSize(REGEX_SIZE);
	}
	
	public RegexView(RegularExpression model){
		this(model, new UndoKeeper(), true);
	}

	@Override
	public JComponent createCentralPanel(RegularExpression model,
			UndoKeeper keeper, boolean editable) {
		return new RegexPanel(model, keeper, editable);
	}

	@Override
	public String getName() {
		return "Regular Expression Editor";
	}

}
