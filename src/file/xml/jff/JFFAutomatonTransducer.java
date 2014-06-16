package file.xml.jff;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.automata.Automaton;
import model.automata.State;
import model.automata.StateSet;
import model.automata.Transition;
import model.automata.TransitionSet;
import model.automata.acceptors.Acceptor;
import model.automata.transducers.OutputFunctionSet;
import model.automata.transducers.moore.MooreMachine;
import model.automata.transducers.moore.MooreOutputFunction;
import model.graph.TransitionGraph;
import model.symbols.SymbolString;
import model.symbols.symbolizer.Symbolizers;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import debug.JFLAPDebug;
import universe.preferences.JFLAPPreferences;
import file.DataException;
import file.FileJFLAPException;
import file.xml.XMLHelper;
import file.xml.graph.AutomatonEditorData;

public abstract class JFFAutomatonTransducer<T extends Automaton<S>, S extends Transition<S>>
		extends JFFTransducer<AutomatonEditorData<T, S>> {

	private static final String FILE_NAME = "tag";
	public static final String BLOCK_NAME = "block";
	public static final String STATE_NAME = "state";
	public static final String STATE_ID_NAME = "id";
	public static final String STATE_X_COORD_NAME = "x";
	public static final String STATE_Y_COORD_NAME = "y";
	public static final String STATE_LABEL_NAME = "label";
	public static final String STATE_NAME_NAME = "name";
	public static final String STATE_FINAL_NAME = "final";
	public static final String STATE_INITIAL_NAME = "initial";
	public static final String TRANSITION_NAME = "transition";
	public static final String TRANSITION_FROM_NAME = "from";
	public static final String TRANSITION_TO_NAME = "to";
	public static final String TRANSITION_CONTROL_X = "controlx";
	public static final String TRANSITION_CONTROL_Y = "controly";
	public static final String NOTE_NAME = "note";
	public static final String NOTE_TEXT_NAME = "text";
	private static final String AUTOMATON_NAME = "automaton";


	@Override
	public AutomatonEditorData<T, S> fromStructureRoot(Element root) {
		Element auto_elem = root;
		List<Element> auto_elem_list = XMLHelper.getChildrenWithTag(root, AUTOMATON_NAME);
		if(!auto_elem_list.isEmpty())
			auto_elem = auto_elem_list.get(0);
		
		T auto = createAutomaton(auto_elem);
		TransitionGraph<S> graph = new TransitionGraph<S>(auto);
		Map<Point2D, String> labels = addStatesAndLabels(graph, auto_elem);
		addTransitions(graph, auto_elem);
		
		Map<Point2D, String> notes = getNotes(auto_elem);
		
		return new AutomatonEditorData<T, S>(graph, labels, notes);
	}

	public void addTransitions(TransitionGraph<S> graph, Element root) {
		List<Element> trans_elem_list = XMLHelper.getChildrenWithTag(root,
				TRANSITION_NAME);
		Automaton auto = graph.getAutomaton();
		StateSet states = auto.getStates();
		TransitionSet<S> transitions = auto.getTransitions();

		// Create the transitions.
		for (Element trans_elem : trans_elem_list) {
			// Get the from state.
			String isBlock = trans_elem.getAttribute("block");
			if (isBlock.equals("true")) {
				throw new FileJFLAPException(
						"Cannot import Turing Machines with blocks from old version files.");
			}
			List<Element> elem_list = XMLHelper.getChildrenWithTag(trans_elem,
					TRANSITION_FROM_NAME);
			if (elem_list.isEmpty())
				throw new DataException("A transition has no from state!");
			String fromName = XMLHelper.containedText(elem_list.get(0));

			int id = parseID(fromName).intValue();
			State from = states.getStateWithID(id);
			if (from == null)
				throw new DataException("A transition is defined from "
						+ "non-existent state " + id + "!");

			// Get the to state.
			elem_list = XMLHelper.getChildrenWithTag(trans_elem,
					TRANSITION_TO_NAME);
			if (elem_list.isEmpty())
				throw new DataException("A transition has no to state!");
			String toName = XMLHelper.containedText(elem_list.get(0));
			id = parseID(toName).intValue();
			State to = states.getStateWithID(id);
			if (to == null)
				throw new DataException("A transition is defined to "
						+ "non-existent state " + id + "!");

			// Now, make the transition.
			S transition = createTransition((T) auto, from, to, trans_elem);
			transitions.add(transition);

			// deal with the shapiness of the transition, if the file specifies
			// it. //add controlX and controlY
			elem_list = XMLHelper.getChildrenWithTag(trans_elem,
					TRANSITION_CONTROL_X);
			if (!elem_list.isEmpty()) {
				String controlX = XMLHelper.containedText(elem_list.get(0));
				elem_list = XMLHelper.getChildrenWithTag(trans_elem,
						TRANSITION_CONTROL_Y);

				if (!elem_list.isEmpty()) {
					String controlY = XMLHelper.containedText(elem_list.get(0));
					if (controlX != null && controlY != null) {
						Point p = new Point(Integer.parseInt(controlX),
								Integer.parseInt(controlY));
						graph.setControlPt(p, transition);
					}
				}
			}
		}
	};

	public abstract S createTransition(T auto, State from, State to,
			Element trans_elem);

	public abstract T createAutomaton(Element root);

	private Map<Point2D, String> addStatesAndLabels(TransitionGraph<S> graph,
			Element root) {
		Map<Point2D, String> labels = new HashMap<Point2D, String>();
		T auto = (T) graph.getAutomaton();
		StateSet states = auto.getStates();

		if (!XMLHelper.getChildArray(root, FILE_NAME).isEmpty())
			throw new FileJFLAPException(
					"Due to version compatibility, Turing Machines with blocks can't be opened from old files.");

		List<Element> stateElems = XMLHelper.getChildrenWithTag(root,
				STATE_NAME);
		for (Element state_elem : stateElems) {
			String idString = state_elem.getAttribute(STATE_ID_NAME);
			if (idString == null)
				throw new DataException(
						"State without id attribute encountered!");
			Integer id = parseID(idString);
			// Check for duplicates.
			if (states.getStateWithID(id) != null)
				throw new DataException("The state ID " + id
						+ " appears twice!");
			// Create the state.
			Point p = new Point();
			// Try to get the X coord.
			double x = 0, y = 0;
			try {
				Element x_elem = XMLHelper.getChildrenWithTag(state_elem,
						STATE_X_COORD_NAME).get(0);
				x = Double.parseDouble(XMLHelper.containedText(x_elem));
			} catch (NullPointerException e) {
				x = 0;
			} catch (NumberFormatException e) {
				throw new DataException("The x coordinate "
						+ "could not be read for state " + id + ".");
			}
			// Try to get the Y coord.
			try {
				Element y_elem = XMLHelper.getChildrenWithTag(state_elem,
						STATE_Y_COORD_NAME).get(0);
				y = Double.parseDouble(XMLHelper.containedText(y_elem));
			} catch (NullPointerException e) {
				y = 0;
			} catch (NumberFormatException e) {
				throw new DataException("The y coordinate"
						+ " could not be read for state " + id + ".");
			}
			p.setLocation(x, y);
			// Create the state.

			String name = state_elem.getAttribute(STATE_NAME_NAME);
			if (name.equals(""))
				name = JFLAPPreferences.getDefaultStateNameBase() + id;

			State state = new State(name, id);
			states.add(state);
			graph.moveVertex(state, p);

			// Add various attributes.
			List<Element> attr_list = XMLHelper.getChildrenWithTag(state_elem,
					STATE_LABEL_NAME);
			if (!attr_list.isEmpty())
				labels.put(p, XMLHelper.containedText(attr_list.get(0)));

			attr_list = XMLHelper.getChildrenWithTag(state_elem,
					STATE_FINAL_NAME);
			if (!attr_list.isEmpty())
				((Acceptor) auto).getFinalStateSet().add(state);

			attr_list = XMLHelper.getChildrenWithTag(state_elem,
					STATE_INITIAL_NAME);
			if (!attr_list.isEmpty())
				auto.setStartState(state);
			/*
			 * If it is a Moore machine, add state output.
			 */
			if (auto instanceof MooreMachine) {
				attr_list = XMLHelper.getChildrenWithTag(state_elem,
						JFFMooreTransducer.STATE_OUTPUT_NAME);
				if (!attr_list.isEmpty()) {
					OutputFunctionSet<MooreOutputFunction> funcs = ((MooreMachine) auto)
							.getOutputFunctionSet();
					String output = XMLHelper.containedText(attr_list.get(0));
					SymbolString outSymbols = new SymbolString();
					if(output != null)
						outSymbols = Symbolizers.symbolize(output, auto);
					funcs.add(new MooreOutputFunction(state, outSymbols));
				}
			}
		}
		return labels;
	}

	private Map<Point2D, String> getNotes(Element root) {
		Map<Point2D, String> notes = new HashMap<Point2D, String>();
		List<Element> note_list = XMLHelper.getChildrenWithTag(root, NOTE_NAME);

		for (Element note_elem : note_list) {
			Point p = new Point();
			boolean hasLocation = true;

			Element text_elem = XMLHelper.getChildrenWithTag(note_elem,
					NOTE_TEXT_NAME).get(0);
			if (text_elem == null)
				continue;
			String textString = XMLHelper.containedText(text_elem);

			// Try to get the X coord.
			double x = 0, y = 0;
			try {
				Element x_elem = XMLHelper.getChildrenWithTag(note_elem,
						STATE_X_COORD_NAME).get(0);
				x = Double.parseDouble(XMLHelper.containedText(x_elem));
			} catch (NullPointerException e) {
				hasLocation = false;
			} catch (NumberFormatException e) {
				throw new DataException("The x coordinate "
						+ " could not be read for the note with text "
						+ textString + ".");
			}
			// Try to get the Y coord.
			try {
				Element y_elem = XMLHelper.getChildrenWithTag(note_elem,
						STATE_Y_COORD_NAME).get(0);
				y = Double.parseDouble(XMLHelper.containedText(y_elem));
			} catch (NullPointerException e) {
				hasLocation = false;
			} catch (NumberFormatException e) {
				throw new DataException("The y coordinate "
						+ " could not be read for the note with text "
						+ textString + ".");
			}
			p.setLocation(x, y);
			notes.put(p, textString);
		}

		return notes;
	}

	private static Integer parseID(String string) {
		try {
			int num = Integer.parseInt(string);
			return new Integer(num);
		} catch (NumberFormatException e) {
			return new Integer(-1);
		}
	}
}
