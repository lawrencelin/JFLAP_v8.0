package view.action.grammar.parse;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import util.view.Parsable;

public class DoParse extends AbstractAction {

	private Parsable mySelectable;

	public DoParse(Parsable panel){
		super("Parse");
		mySelectable = panel;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		mySelectable.Parse();
	}

}
