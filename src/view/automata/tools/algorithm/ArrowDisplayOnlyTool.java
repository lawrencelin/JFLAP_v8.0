package view.automata.tools.algorithm;

import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import model.automata.Automaton;
import model.automata.State;
import model.automata.Transition;
import view.automata.editing.AutomatonEditorPanel;
import view.automata.tools.ArrowTool;

public class ArrowDisplayOnlyTool<T extends Automaton<S>, S extends Transition<S>> extends ArrowTool<T, S> {

	public ArrowDisplayOnlyTool(AutomatonEditorPanel<T, S> panel, T def) {
		super(panel, def);
	}
	
	
	@Override
	public void mousePressed(MouseEvent e) {
		AutomatonEditorPanel<T, S> panel = getPanel();
		panel.clearSelection();

		if(SwingUtilities.isLeftMouseButton(e) && !isModified(e)){
			Object clicked = panel.objectAtPoint(e.getPoint());
			if(clicked != null)
				panel.selectObject(clicked);
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
	}

	

}
