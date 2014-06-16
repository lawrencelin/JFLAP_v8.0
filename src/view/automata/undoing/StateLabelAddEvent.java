package view.automata.undoing;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

import model.automata.State;
import view.automata.Note;
import view.automata.editing.AutomatonEditorPanel;

public class StateLabelAddEvent extends EditingEvent{
	
	private Note myLabel;
	private String myText;
	private State myState;

	public StateLabelAddEvent(AutomatonEditorPanel panel, State s, String text){
		super(panel);
		myState = s;
		myText = text;
		
		Point2D center = panel.getPointForVertex(s);
		center = new Point((int) center.getX(), (int) center.getY());
		
		myLabel = new Note(panel, (Point) center);
		myLabel.setBounds(new Rectangle((Point) center, myLabel.getPreferredSize()));
	}
	
	@Override
	public boolean undo() {
		getPanel().removeStateLabel(myState);
		return true;
	}

	@Override
	public boolean redo() {
		getPanel().addStateLabel(myState, myLabel, myText);
		return true;
	}

	@Override
	public String getName() {
		return "Adds a state label";
	}

}
