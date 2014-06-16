package file.xml.graph;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import debug.JFLAPDebug;
import model.automata.Automaton;
import model.automata.State;
import model.automata.Transition;
import model.graph.TransitionGraph;
import view.automata.Note;
import view.automata.editing.AutomatonEditorPanel;

/**
 * Class for holding all of the relevant data of an AutomatonEditorPanel for
 * saving/loading without having to deal with the panel itself (so that the data
 * can be distributed wherever it is needed).
 * 
 * @author Ian McMahon
 */
public class AutomatonEditorData<T extends Automaton<S>, S extends Transition<S>> {

	private TransitionGraph<S> myGraph;
	private Map<Point2D, String> myLabels;
	private Map<Point2D, String> myNotes;

	public AutomatonEditorData(AutomatonEditorPanel<T, S> panel) {
		T auto = panel.getAutomaton();
		myGraph = panel.getGraph().copy();
		myLabels = new HashMap<Point2D, String>();
		myNotes = new HashMap<Point2D, String>();

		for (State s : auto.getStates()) {
			Note label = panel.getStateLabel(s);
			
			if (label != null && label.getText() != null)
				myLabels.put(panel.getPointForVertex(s), label.getText());
		}

		for (Note n : panel.getNotes()) {
			myNotes.put(n.getLocation(), n.getText());
		}
	}

	public AutomatonEditorData(TransitionGraph<S> graph,
			Map<Point2D, String> labels, Map<Point2D, String> notes) {
		myGraph = graph;
		myLabels = labels;
		myNotes = notes;
	}

	public TransitionGraph<S> getGraph() {
		return myGraph;
	}

	public Map<Point2D, String> getLabels() {
		return myLabels;
	}

	public Map<Point2D, String> getNotes() {
		return myNotes;
	}
}
