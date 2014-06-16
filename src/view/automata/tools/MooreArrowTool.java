package view.automata.tools;

import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import model.automata.State;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.transducers.moore.MooreMachine;
import view.automata.Note;
import view.automata.editing.MooreEditorPanel;
import view.automata.undoing.MooreOutputRenameEvent;

/**
 * Arrow tool specific to Moore panel (edits the output function of States when they are clicked).
 * 
 * @author Ian McMahon
 *
 */
public class MooreArrowTool extends ArrowTool<MooreMachine, FSATransition> {

	public MooreArrowTool(MooreEditorPanel panel, MooreMachine def) {
		super(panel, def);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Point p = e.getPoint();
		MooreEditorPanel panel = (MooreEditorPanel) getPanel();
		Object o = panel.objectAtPoint(p);
		
		if (o instanceof State && SwingUtilities.isLeftMouseButton(e)) {
			State s = (State) o;
			Note output = panel.getOutputNote(s);
			String newOutput = panel.editOutputFunction(s);
			
			if(!newOutput.equals(output.getText())){
				String old = output.getText();
				output.setText(newOutput);
				
				panel.moveOutputFunction(s);
				getKeeper().registerChange(new MooreOutputRenameEvent(panel, s, output, old));
			}
		} else
			super.mouseClicked(e);
	}

}
