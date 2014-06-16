package view.automata;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

import debug.JFLAPDebug;
import model.automata.Automaton;
import model.automata.State;
import model.automata.Transition;
import model.automata.TransitionSet;
import model.automata.transducers.moore.MooreMachine;
import model.undo.UndoKeeper;
import universe.JFLAPUniverse;
import util.JFLAPConstants;
import util.view.magnify.MagnifiablePanel;
import view.automata.editing.AutomatonEditorPanel;
import view.automata.editing.MooreEditorPanel;

public class AutomatonDisplayPanel<T extends Automaton<S>, S extends Transition<S>>
		extends MagnifiablePanel {
	
	private static final int MAX_HEIGHT = 400;
	private static final int MAX_WIDTH = 700;
	public static final int PADDING = 150;
	private AutomatonEditorPanel<T, S> myPanel;

	public AutomatonDisplayPanel(AutomatonEditorPanel<T, S> editor, T auto, String name) {
		super(new BorderLayout());
		setName(name);

		myPanel = (AutomatonEditorPanel<T, S>) (auto instanceof MooreMachine ? new MooreEditorPanel(
				(MooreMachine) auto, new UndoKeeper(), false)
				: new AutomatonEditorPanel<T, S>(auto, new UndoKeeper(), false));
		TransitionSet<S> tranSet = auto.getTransitions();

		for (State s : auto.getStates()) {
			if(editor.getStateLabel(s) != null)
				myPanel.addStateLabel(s, new Note(myPanel, new Point()), editor.getStateLabel(s).getText());
			myPanel.moveState(s, editor.getPointForVertex(s));
		}

		for (S t : tranSet) {
			State[] states = new State[] { t.getFromState(), t.getToState() };
			myPanel.moveCtrlPoint(states[0], states[1],
					editor.getControlPoint(states));
		}

		myPanel.getActionMap().put(AutomatonEditorPanel.DELETE, null);
		add(myPanel, BorderLayout.CENTER);
	}

	public void updateSize() {
		Graphics g = getGraphics();
		myPanel.updateBounds(g);
		
		Dimension panDim = myPanel.getPreferredSize();
		if(panDim.height > MAX_HEIGHT || panDim.width > MAX_WIDTH){
			if(panDim.height > MAX_HEIGHT)
				panDim = new Dimension(panDim.width, MAX_HEIGHT);
			if(panDim.width > MAX_WIDTH)
				panDim = new Dimension(MAX_WIDTH, panDim.height);
		}
		
		int width = Math.max(getViewSize().width, panDim.width)
				+ JFLAPConstants.STATE_RADIUS + 5;
		int height = panDim.height + PADDING;
		
		setPreferredSize(new Dimension(width, height));
		
		Point2D max = myPanel.getMaxPoint(g);
		if(max.getX() >= width || max.getY() > height)
			myPanel.resizeGraph(new Rectangle(width - (JFLAPConstants.STATE_RADIUS + 5), height - PADDING));
	}

	public AutomatonEditorPanel<T, S> getEditorPanel() {
		return myPanel;
	}

	private Dimension getViewSize() {
		return JFLAPUniverse.getActiveEnvironment().getPreferredSize();
	}

}
