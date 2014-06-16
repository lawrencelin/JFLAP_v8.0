package view.automata.tools;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

import model.automata.Automaton;
import model.automata.State;
import model.automata.Transition;
import util.JFLAPConstants;
import view.automata.Note;
import view.automata.editing.AutomatonEditorPanel;
import view.automata.undoing.NoteRemoveEvent;

/**
 * Tool used in the deleting of States, Transitions, edges, and Notes in an
 * Automaton graph
 * 
 * @author Ian McMahon
 */
public class DeleteTool<T extends Automaton<S>, S extends Transition<S>>
		extends EditingTool<T, S> {

	public DeleteTool(AutomatonEditorPanel<T, S> panel) {
		super(panel);
	}

	@Override
	public String getToolTip() {
		return "Deleter";
	}

	@Override
	public char getActivatingKey() {
		return 'd';
	}

	@Override
	public String getImageURLString() {
		
		return "/ICON/delete.gif";
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		AutomatonEditorPanel<T, S> panel = getPanel();
		panel.stopAllEditing();
		
		if (SwingUtilities.isLeftMouseButton(e)) {
			Object o = panel.objectAtPoint(e.getPoint());
			
			if (e.getSource() instanceof Note){
				List<Note> notes = new ArrayList<Note>();
				notes.add((Note) e.getSource());
				panel.getKeeper().applyAndListen(new NoteRemoveEvent(panel, notes));
			}
			else if (o instanceof State)
				panel.removeState((State) o);
			else if (o instanceof Transition)
				panel.removeTransition((S) o);
			else if (o instanceof State[])
				panel.removeEdge(((State[]) o)[0], ((State[]) o)[1]);
			
			panel.repaint();
		}
	}
	
	@Override
	public void setActive(boolean active) {
		super.setActive(active);
		Cursor cursor = new Cursor(Cursor.DEFAULT_CURSOR);
		
		if(active){
			Toolkit toolkit = Toolkit.getDefaultToolkit();

			String del = "/ICON/deletecursor.gif";
			URL url = getClass().getResource(del);
			Image img = Toolkit.getDefaultToolkit().getImage(url);
			Point hotSpot = new Point(5, 5);
			cursor = toolkit.createCustomCursor(img, hotSpot, "Delete");
		}
		getPanel().setCursor(cursor);
	}
}
