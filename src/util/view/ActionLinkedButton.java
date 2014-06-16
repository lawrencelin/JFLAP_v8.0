package util.view;

import javax.swing.Action;
import javax.swing.JButton;

import debug.JFLAPDebug;

public class ActionLinkedButton extends JButton{

	private Action myAction;

	public ActionLinkedButton(Action action) {
		super(action);
		myAction = action;
		updateEnabled();
		this.repaint();
	}

	public void updateEnabled() {
		if (myAction != null && myAction.isEnabled() != this.isEnabled()){
			super.setEnabled(myAction.isEnabled());
		}
	}


}