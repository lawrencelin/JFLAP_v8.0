package view.automata.tools;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

public class ToolBar extends JToolBar implements ActionListener {

	private Tool myActiveTool;
	private List<ToolButton> myButtons;
	private List<ToolListener> myListeners;

	public ToolBar(Tool... tools) {
		myButtons = new ArrayList<ToolButton>();
		myListeners = new ArrayList<ToolListener>();
		this.addAll(tools);
	}

	public void add(Tool tool) {
		ToolButton button = new ToolButton(tool);
		myButtons.add(button);
		this.add(button);
		button.setToolTipText(tool.getShortcutToolTip());
		button.addActionListener(this);
		if (myActiveTool == null)
			this.setActiveTool(button);
	}

	public void addAll(Tool... tools) {
		for (Tool t : tools)
			this.add(t);
	}

	/**
	 * If a tool is clicked, sets the new current tool.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof ToolButton)
			this.setActiveTool((ToolButton) e.getSource());
	}

	private void setActiveTool(ToolButton source) {
		this.toggleAllOff();
		source.setSelected(true);
		myActiveTool = source.getTool();
		this.broadcastToolChange();
	}
	
	public void setActiveTool(int i) {
		setActiveTool(myButtons.get(i));
	}

	private void broadcastToolChange() {
		for (ToolListener tcl : myListeners)
			tcl.toolActivated(myActiveTool);
	}

	private void toggleAllOff() {
		for (ToolButton tb : myButtons)
			tb.setSelected(false);
	}

	public void addToolListener(ToolListener tcl) {
		myListeners.add(tcl);
		tcl.toolActivated(myActiveTool);
	}
	
	public void disableTool(int i) {
		myButtons.get(i).setEnabled(false);
	}

}
