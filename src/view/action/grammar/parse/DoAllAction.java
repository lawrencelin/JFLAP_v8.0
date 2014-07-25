package view.action.grammar.parse;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import util.view.DoSelectable;

/**
 * Action called for doAll method.
 * 
 * @author John Godbey
 *
 */
public class DoAllAction extends AbstractAction {

	private DoSelectable mySelectable;

	public DoAllAction(DoSelectable panel){
		super("Do ALL");
		mySelectable = panel;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		//mySelectable.doAll();
	}

}
