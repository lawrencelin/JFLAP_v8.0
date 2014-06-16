package view.automata.tools;

import java.awt.event.ActionEvent;


import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;

import debug.JFLAPDebug;


public class ToolButton extends JToggleButton {

	private Tool myTool;

	public ToolButton(Tool tool) {
		super(tool.getIcon());
		myTool = tool;
		InputMap imap = this
				.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap amap = this.getActionMap();
		char key = myTool.getActivatingKey();
		imap.put(KeyStroke.getKeyStroke(key), this);
		key = Character.isUpperCase(key) ? Character.toLowerCase(key) : Character.toUpperCase(key);
		imap.put(KeyStroke.getKeyStroke(key), this);
		amap.put(this, new ButtonClicker(this));
	}

	public Tool getTool() {
		return myTool;
	}

	
	/**
	 * The action that clicks a button.
	 */
	private class ButtonClicker extends AbstractAction {
		public ButtonClicker(AbstractButton button) {
			this.button = button;
		}

		public void actionPerformed(ActionEvent e) {
			button.doClick();
		}

		AbstractButton button;
	}
	
}

