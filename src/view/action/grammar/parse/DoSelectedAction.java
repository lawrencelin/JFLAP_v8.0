package view.action.grammar.parse;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import util.view.DoSelectable;

/**
 * Action called for doSelected method.
 * 
 * @author Ian McMahon
 *
 */
public class DoSelectedAction extends AbstractAction {

	private DoSelectable mySelectable;

	public DoSelectedAction(DoSelectable panel){
		super("Do Selected");
		mySelectable = panel;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		mySelectable.doSelected();
	}

}
