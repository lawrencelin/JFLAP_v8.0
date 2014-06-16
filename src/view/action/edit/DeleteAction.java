package view.action.edit;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import util.ISelector;
import util.JFLAPConstants;

public class DeleteAction extends AbstractAction {

	private ISelector mySource;

	public DeleteAction(ISelector source){
		super("Delete Selection");
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_D,
				JFLAPConstants.MAIN_MENU_MASK));
		mySource = source;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		mySource.deleteSelected();
	}


}
