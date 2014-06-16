package view.automata.tools.algorithm;

import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import model.automata.Automaton;
import model.automata.Transition;
import view.automata.editing.AutomatonEditorPanel;
import view.automata.tools.ArrowTool;

public class NonTransitionArrowTool<T extends Automaton<S>, S extends Transition<S>> extends ArrowTool<T, S> {

	public NonTransitionArrowTool(AutomatonEditorPanel<T, S> panel, T def) {
		super(panel, def);
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if(SwingUtilities.isLeftMouseButton(e))
			if(!(getPanel().objectAtPoint(e.getPoint()) instanceof Transition))
				super.mousePressed(e);
	}

}
