package view.undoing;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import view.undoing.redo.RedoButton;
import view.undoing.undo.UndoButton;

import model.undo.UndoKeeper;
import model.undo.UndoKeeperListener;

public class UndoPanel extends JPanel{

	private UndoButton myUndoButton;
	private RedoButton myRedoButton;

	public UndoPanel(UndoKeeper keeper){
		myUndoButton = new UndoButton(keeper, true);
		myRedoButton = new RedoButton(keeper, true);
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		this.add(myUndoButton);
		this.add(myRedoButton);
	}

}
