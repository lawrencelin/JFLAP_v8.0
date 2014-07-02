package view.automata.editing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import debug.JFLAPDebug;
import model.automata.Automaton;
import model.automata.AutomatonException;
import model.automata.StartState;
import model.automata.State;
import model.automata.StateSet;
import model.automata.Transition;
import model.automata.TransitionSet;
import model.automata.acceptors.Acceptor;
import model.automata.turing.buildingblock.Block;
import model.automata.turing.buildingblock.BlockTuringMachine;
import model.change.events.RemoveEvent;
import model.change.events.StartStateSetEvent;
import model.graph.LayoutAlgorithm;
import model.graph.TransitionGraph;
import model.undo.CompoundUndoRedo;
import model.undo.IUndoRedo;
import model.undo.UndoKeeper;
import universe.preferences.JFLAPPreferences;
import util.JFLAPConstants;
import util.Point2DAdv;
import util.arrows.CurvedArrow;
import util.view.GraphHelper;
import view.EditingPanel;
import view.automata.BlockDrawer;
import view.automata.LabelBounds;
import view.automata.Note;
import view.automata.SelectionAutomatonDrawer;
import view.automata.StateDrawer;
import view.automata.tools.ArrowTool;
import view.automata.tools.EditingTool;
import view.automata.tools.Tool;
import view.automata.tools.ToolListener;
import view.automata.transitiontable.TransitionTable;
import view.automata.transitiontable.TransitionTableFactory;
import view.automata.undoing.CompoundMoveEvent;
import view.automata.undoing.ControlMoveEvent;
import view.automata.undoing.NoteAddEvent;
import view.automata.undoing.NoteRemoveEvent;
import view.automata.undoing.NoteRenameEvent;
import view.automata.undoing.StateLabelAddEvent;
import view.automata.undoing.StateLabelRemoveEvent;
import view.automata.undoing.StateLabelRenameEvent;
import view.automata.undoing.StateMoveEvent;

public class AutomatonEditorPanel<T extends Automaton<S>, S extends Transition<S>>
		extends EditingPanel implements ToolListener, ChangeListener {

	private static final int PADDING = 5;

	public static final String DELETE = "delete";
	
	private EditingTool<T, S> myTool;
	private T myAutomaton;
	private TransitionGraph<S> myGraph;
	private SelectionAutomatonDrawer<S> myDrawer;
	private AffineTransform transform;
	private Map<State, Note> myStateLabels;
	private Map<Note, String> myNotes;
	private TransitionTable<T, S> myEditingTable;

	public AutomatonEditorPanel(T m, UndoKeeper keeper, boolean editable) {
		super(keeper, editable);
		setLayout(null); // Needed to deal with Notes and Tables

		myAutomaton = m;
		myGraph = new TransitionGraph<S>(m);
		myGraph.addListener(this);
		StateDrawer vDraw = (m instanceof BlockTuringMachine ? new BlockDrawer()
				: new StateDrawer());
		myDrawer = new SelectionAutomatonDrawer<S>(vDraw);
		transform = new AffineTransform();
		myStateLabels = new HashMap<State, Note>();
		myNotes = new HashMap<Note, String>();
		
		InputMap iMap = getInputMap();
		ActionMap aMap = getActionMap();
		iMap.put(KeyStroke.getKeyStroke((char) KeyEvent.VK_DELETE), DELETE);
		aMap.put(DELETE, new DeleteAction());
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		setBackground(java.awt.Color.white);
		g2.transform(transform);

		updateBounds(g2);

		myDrawer.draw(myGraph, g2);

		if (myTool != null)
			myTool.draw(g2);
	}

	@Override
	protected void paintChildren(Graphics g) {
		super.paintChildren(g);
		for (Note n : myNotes.keySet()) {
			if (getSelection().contains(n))
				n.setBackground(JFLAPPreferences.getSelectedStateColor());
			else
				n.setBackground(JFLAPPreferences.getStateColor());
		}

		for (State s : myStateLabels.keySet()) {
			Note label = myStateLabels.get(s);
			Color bg = myDrawer.isSelected(s) ? JFLAPPreferences
					.getSelectedStateColor() : JFLAPPreferences.getStateColor();
			label.setBackground(bg);
		}
	}

	@Override
	public void setMagnification(double mag) {
		// TODO: Resize automata & notes, may actually want separate slider for
		// automaton. Issue with changing magnification and points.
//		transform.setToScale(mag * 2, mag * 2);
		super.setMagnification(mag);
		myDrawer.getVertexDrawer().changeRadius(mag);
		myDrawer.magnifyLabel(mag);
		myDrawer.getVertexDrawer().changeFontSize(mag);
		repaint();
	}

	@Override
	public void toolActivated(Tool e) {
		setTool((EditingTool) e);
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		repaint();
	}

	public void stopAllEditing() {
		// TODO: needs to be implemented so undo/redo doesn't create crazy
		// graphics with the TransitionTables
		// Also so you don't break things (like undoing a creation while you
		// still have the state selected)

		UndoKeeper keeper = getKeeper();
		for (Note n : myNotes.keySet()) {
			n.setEditable(false);
			n.setEnabled(false);
			n.setCaretColor(JFLAPConstants.BACKGROUND_CARET_COLOR);
			String text = n.getText(), oldText = myNotes.get(n);

			if (n.isEmpty()) {
				n.setText(oldText);
				keeper.applyAndListen(new NoteRemoveEvent(this, Arrays
						.asList(new Note[] { n })));
			} else if (!oldText.equals(text)) {
				myNotes.put(n, text);
				getKeeper().registerChange(
						new NoteRenameEvent(this, n, oldText));
			}
		}
		if (myEditingTable != null)
			myEditingTable.stopEditing(true);
	}

	/**
	 * Returns the State, Transition, Arrow (in the form of a State[]), or Note
	 * at the given point, or null if there is none of them.
	 */
	public Object objectAtPoint(Point2D p) {
		Object o = stateAtPoint(p);
		if (o == null)
			o = transitionAtPoint(p);
		if (o == null)
			o = arrowAtPoint(p);
		if (o == null)
			o = noteAtPoint(p);
		return o;
	}

	/**
	 * Removes the old tool and sets the new tool to listen for mouse and mouse
	 * motion events. Also will update the cursor (to an X if it is a delete
	 * tool, or a simple arrow otherwise).
	 */
	public void setTool(EditingTool<T, S> t) {
		stopAllEditing();
		if (myTool != null) {
			myTool.setActive(false);
			for (Note n : myNotes.keySet()) {
				n.removeMouseListener(myTool);
				n.removeMouseMotionListener(myTool);
			}
		}
		myTool = t;
		myTool.setActive(true);
		for (Note n : myNotes.keySet()) {
			n.addMouseListener(myTool);
			n.addMouseMotionListener(myTool);
		}
	}

	public void setGraph(TransitionGraph<S> graph) {
		myGraph.removeListener(this);
		myAutomaton.removeListener(myGraph);
		myGraph = graph;
		myGraph.addListener(this);
		repaint();
	}

	public TransitionGraph<S> getGraph() {
		return myGraph;
	}

	/** Sets the given object as selected in the AutomatonDrawer. */
	public void selectObject(Object o) {
		myDrawer.setSelected(o, true);
		repaint();
	}

	public void deselectObject(Object o) {
		myDrawer.setSelected(o, false);
		repaint();
	}

	public void selectAll(Collection<? extends Object> objs) {
		for (Object o : objs) {
			myDrawer.setSelected(o, true);
		}
		repaint();
	}

	public void selectAllInBounds(Rectangle bounds) {
		Set<S> tranSet = new TreeSet<S>();
		Set<State> stateSet = new TreeSet<State>();
		Set<State[]> edgeSet = new HashSet<State[]>();
		Set<Note> noteSet = new HashSet<Note>();

		for (State vertex : myAutomaton.getStates()) {
			Point2D current = myGraph.pointForVertex(vertex);
			if (bounds.contains(current))
				stateSet.add(vertex);
		}

		for (S trans : myAutomaton.getTransitions()) {
			LabelBounds label = GraphHelper.getLabelBounds(myGraph, trans,
					getGraphics());
			State from = trans.getFromState(), to = trans.getToState();
			CurvedArrow arrow = GraphHelper.getArrow(from, to, myGraph);

			if (bounds.intersects(label.getRectangle())
					|| bounds.contains(label.getRectangle()))
				tranSet.add(trans);
			if (arrow.intersects(bounds)) {
				tranSet.addAll(myGraph.getOrderedTransitions(from, to));
				edgeSet.add(new State[] { from, to });
			}
		}

		for (Note n : myNotes.keySet()) {
			Rectangle noteBound = n.getBounds();
			if (bounds.intersects(noteBound) || bounds.contains(noteBound))
				noteSet.add(n);
		}
		List<Object> list = new ArrayList<Object>(tranSet);
		list.addAll(stateSet);
		list.addAll(edgeSet);
		list.addAll(noteSet);

		clearSelection();
		selectAll(list);
	}
	
	public boolean isSelected (Object o){
		return myDrawer.isSelected(o);
	}

	public List<Object> getSelection() {
		List<Object> list = new ArrayList<Object>();
		list.addAll(myDrawer.getSelectedStates());
		list.addAll(myDrawer.getSelectedTransitions());
		list.addAll(myDrawer.getSelectedEdges());
		list.addAll(myDrawer.getSelectedNotes());

		return list;
	}

	/** Removes all selected objects in the AutomatonDrawer. */
	public void clearSelection() {
		myDrawer.clearSelection();
		repaint();
	}

	/**
	 * Creates and adds a new, default-named state to the automaton, and sets
	 * its location to the given point.
	 */
	public State createState(Point2D point) {

		StateSet states = myAutomaton.getStates();
		State vertex = states.createAndAddState();
		myGraph.moveVertex(vertex, new Point2DAdv(point));
		return vertex;
	}

	/** Moves the state to the specified point. */
	public void moveState(State s, Point2D p) {
		myGraph.moveVertex(s, p);
		if (myStateLabels.containsKey(s)) {
			Note label = myStateLabels.get(s);

			if (label != null)
				moveStateLabel(s);
		}
	}

	/**
	 * Deletes the given state from the Automaton, and notifies the UndoKeeper
	 * to allow for a compound event where all transitions to or from vertex
	 * will also be added or deleted.
	 */
	public void removeState(State vertex) {
		Point2D p = (Point2D) getPointForVertex(vertex).clone();
		TransitionSet<S> transitions = myAutomaton.getTransitions();

		Set<S> transFromOrTo = transitions.getTransitionsFromState(vertex);
		transFromOrTo.addAll(transitions.getTransitionsToState(vertex));

		getKeeper().applyAndListen(
				createCompoundRemoveEvent(new State[] { vertex },
						transFromOrTo, new Point2D[] { p }));
	}

	/** Returns a the location of vertex in the TransitionGraph. */
	public Point2D getPointForVertex(State vertex) {
		return myGraph.pointForVertex(vertex);
	}

	/**
	 * Initializes a blank transition to be edited prior to being added to the
	 * Automaton.
	 */
	public S createTransition(State from, State to) {
		StateSet states = myAutomaton.getStates();
		if (!(states.contains(from) && states.contains(to)))
			return null;

		return myAutomaton.createBlankTransition(from, to);
	}

	/**
	 * Initializes an editing table for the specified transition at the location
	 * of the transition's label center. If the transition is brand new (ie. not
	 * in the Automaton at the point this is called), the label center will be
	 * calculated based on default settings.
	 */
	public void editTransition(S trans, boolean isNew) {
		//Store the current visible rectangle to keep it after creating the table
		Rectangle visible = getVisibleRect();
		myEditingTable = TransitionTableFactory.createTable(trans, myAutomaton,
				this);
		myEditingTable.setCellSelectionEnabled(true);
		myEditingTable.changeSelection(0, 0, false, false);

		Dimension tableSize = myEditingTable.getPreferredSize();
		Point2D center = isNew ? GraphHelper.calculateCenterPoint(myGraph,
				trans) : myGraph.getLabelCenter(trans);
		Point tablePoint = new Point((int) center.getX() - tableSize.width / 2,
				(int) center.getY() - tableSize.height / 2);

		myEditingTable.setBounds(new Rectangle(tablePoint, tableSize));
		myEditingTable.requestFocusInWindow();
		scrollRectToVisible(visible);
	}

	public void clearTableInfo() {
		clearSelection();
		myEditingTable = null;
		repaint();
		requestFocus();
	}

	/** Removes the transition from the Automaton and notifies the UndoKeeper. */
	public void removeTransition(S trans) {
		getKeeper().applyAndListen(createTransitionRemove(trans));
	}

	/**
	 * Returns the location of the ControlPoint for the edge from states[0] to
	 * state[1].
	 */
	public Point2D getControlPoint(State[] states) {
		return myGraph.getControlPt(states[0], states[1]).toBasicPoint();
	}

	/**
	 * Moves the ControlPoint for the edge between from and to to the specified
	 * point.
	 */
	public void moveCtrlPoint(State from, State to, Point2D point) {
		myGraph.setControlPt(point, from, to);
	}

	/**
	 * Removes an edge and all transitions from <i>from</i> to <i>to</i> and
	 * notifies the UndoKeeper.
	 */
	public void removeEdge(State from, State to) {
		S[] temp = (S[]) myGraph.getOrderedTransitions(from, to).toArray(
				new Transition[0]);

		getKeeper().applyAndListen(createTransitionRemove(temp));
	}

	/** Creating of non-state-label notes. */
	public void createAndAddNote(Point p) {
		Note note = new Note(this, p);
		getKeeper().applyAndListen(new NoteAddEvent(this, note));
	}

	/** Adds a note to this panel. */
	public void addNote(Note n) {
		add(n);
		n.resetBounds();
		myNotes.put(n, n.getText());
		n.addMouseListener(myTool);
		n.addMouseMotionListener(myTool);
	}
	
	public void setNoteText(Note n, String text){
		n.setText(text);
		if(myNotes.containsKey(n))
			myNotes.put(n, text);
	}

	public List<Note> getNotes() {
		return new ArrayList<Note>(myNotes.keySet());
	}

	/** Enables the note and requests focus. */
	public void editNote(Note n) {
		n.setEnabled(true);
		n.setEditable(true);
		n.setCaretColor(null);
		n.requestFocus();
		myNotes.put(n, n.getText());
		repaint();
	}

	/** Moves the note to the specified point. */
	public void moveNote(Note n, Point p) {
		n.setLocation(p);
		repaint();
	}

	/** Deletes the note and all traces of it. */
	public void removeNote(Note n) {
		remove(n);
		myNotes.remove(n);
		n.removeMouseListener(myTool);
		n.removeMouseMotionListener(myTool);
		myDrawer.clearSelection();
		repaint();
	}

	public Note getStateLabel(State s) {
		if (myStateLabels.containsKey(s))
			return myStateLabels.get(s);
		return null;
	}

	public void addStateLabel(State s, Note n, String label) {
		add(n);
		n.setText(label);

		n.setEditable(false);
		n.setEnabled(false);
		n.setCaretColor(JFLAPConstants.BACKGROUND_CARET_COLOR);

		myStateLabels.put(s, n);
		moveStateLabel(s);
	}

	public void changeStateLabel(State s, String label) {
		Note n;
		UndoKeeper keeper = getKeeper();
		boolean contained = myStateLabels.containsKey(s);

		if (label == null) {
			if (contained)
				keeper.applyAndListen(new StateLabelRemoveEvent(this, s));
			return;
		}

		if (!myStateLabels.containsKey(s) || (n = myStateLabels.get(s)) == null) {
			keeper.applyAndListen(new StateLabelAddEvent(this, s, label));
		} else if (!label.equals(n.getText())) {
			String oldText = n.getText();
			n.setText(label);
			moveStateLabel(s);
			keeper.registerChange(new StateLabelRenameEvent(this, s, oldText));
		}
		repaint();
	}

	public void moveStateLabel(State s) {
		Point2D center = getPointForVertex(s);
		Note n = myStateLabels.get(s);
		if (n != null) {
			int x = (int) (center.getX() - n.getBounds().width / 2);
			int y = (int) (center.getY() + JFLAPConstants.STATE_LABEL_HEIGHT);
			n.setLocation(new Point(x, y));
		}
	}

	public void removeStateLabel(State s) {
		remove(myStateLabels.get(s));
		myStateLabels.remove(s);
		repaint();
	}

	public void setLayoutAlgorithm(LayoutAlgorithm alg) {
		myGraph.setLayoutAlgorithm(alg);
	}
	
	public LayoutAlgorithm getLayoutAlgorithm() {
		return myGraph.getLayoutAlgorithm();
	}
	
	public void layoutGraph(){
		layoutGraph(new HashSet<State>());
	}

	public void layoutGraph(Set<State> unmoving) {
		StateSet states = myAutomaton.getStates();
		TransitionSet<S> transitions = myAutomaton.getTransitions();

		Map<State, Point2D> oldStatePoints = new HashMap<State, Point2D>();
		Map<State[], Point2D> oldCtrlPoints = new HashMap<State[], Point2D>();

		for (State s : states) {
			oldStatePoints.put(s, new Point2DAdv(getPointForVertex(s)));
			for (S trans : transitions.getTransitionsFromState(s)) {
				State to = trans.getToState();
				State[] edge = new State[] { s, to };

				oldCtrlPoints.put(edge, getControlPoint(edge));
			}

		}
		myGraph.layout(unmoving);
		resizeGraph(getVisibleRect());

		List<StateMoveEvent> move = new ArrayList<StateMoveEvent>();

		for (State s : oldStatePoints.keySet()) {

			Point2D newPoint = new Point2DAdv(getPointForVertex(s)), oldPoint = oldStatePoints
					.get(s);
			if (!newPoint.equals(oldPoint))
				move.add(new StateMoveEvent(this, myAutomaton, s, oldPoint,
						newPoint));

		}
		CompoundMoveEvent comp = new CompoundMoveEvent(this, move);

		for (State[] edge : oldCtrlPoints.keySet()) {
			Point2D newPoint = new Point2DAdv(getControlPoint(edge)), oldPoint = oldCtrlPoints
					.get(edge);

			if (!newPoint.equals(oldPoint))
				comp.addEvents(new ControlMoveEvent(this, edge, oldPoint,
						newPoint));
		}
		if (!comp.isEmpty())
			getKeeper().registerChange(comp);
	}

	public void resizeGraph(Rectangle visible) {
		updateBounds(getGraphics());
		Rectangle b = new Rectangle(getPreferredSize());
//		JFLAPDebug.print(visible+" "+b);
		if (!visible.contains(b) && !visible.equals(b)) {
			int bounds = (int) getStateBounds();
			visible = new Rectangle(visible.x + bounds, visible.y + bounds,
					visible.width - 2 * bounds, visible.height - 2 * bounds);
			GraphHelper.moveWithinFrame(myGraph, visible);
		}
		for (State s : myStateLabels.keySet())
			moveStateLabel(s);
	}

	/**
	 * Updates the graph so that all parts (States, arrows, labels, notes) are
	 * moved to an accessible region (within the enclosing scroll pane), and the
	 * preferred size contains the entire graph (so the containing scroll pane
	 * knows to resize).
	 */
	public void updateBounds(Graphics g) {
		Point2D min = getMinPoint(g);
		Point2D max = getMaxPoint(g);
		double maxx = max.getX(), minx = min.getX();
		double maxy = max.getY(), miny = min.getY();
		
		if (minx < 0 || miny < 0) {
			// Adjust so they get off the boundary
			minx -= minx < 0 ? 1 : 0;
			miny -= miny < 0 ? 1 : 0;

			if(myTool instanceof ArrowTool)
			for(S trans : myAutomaton.getTransitions()){
				State from = trans.getFromState(), to = trans.getToState();
				Point2D current = myGraph.getControlPt(from, to);
				if(((ArrowTool) myTool).isMainObject(new State[]{from, to}))
					moveCtrlPoint(from, to, new Point2DAdv(current.getX() - minx, current.getY() - miny));
			}
			
			for (State vert : myAutomaton.getStates()) {
				Point2D current = myGraph.pointForVertex(vert);

				moveState(vert, new Point2D.Double(current.getX() - minx,
						current.getY() - miny));
			}
			
			for (Note n : myNotes.keySet()) {
				Point nPoint = n.getLocation();
				nPoint = new Point((int) (nPoint.x - minx),
						(int) (nPoint.y - miny));
				n.setLocation(nPoint);
			}
		}

		int x = (int) (Math.ceil(maxx)), y = (int) (Math.ceil(maxy));
		// TODO: may want to change when you actually set the size
		Dimension newSize = new Dimension(x, y);
		setPreferredSize(newSize);
		setMinimumSize(newSize);
		setMaximumSize(newSize);
		revalidate();
	}

	public CompoundRemoveEvent createCompoundRemoveEvent(State[] states,
			Set<S> transitions, Point2D[] points) {
		return new CompoundRemoveEvent(states, transitions, points);
	}

	public T getAutomaton() {
		return myAutomaton;
	}

	public IUndoRedo createTransitionRemove(Collection<S> trans) {
		return new TransitionRemoveEvent(trans);
	}

	public Point2D getMinPoint(Graphics g) {
		Point2D min = GraphHelper.getMinPoint(myGraph, g);
		double minx = min.getX(), miny = min.getY();

		for (State vert : myStateLabels.keySet()) {
			Note sLabel = myStateLabels.get(vert);
			if (sLabel != null) {
				Rectangle lBounds = sLabel.getBounds();
				minx = Math.min(minx, lBounds.getMinX());
				miny = Math.min(miny, lBounds.getMinY());
			}
		}
		for (Note n : myNotes.keySet()) {
			Rectangle r = n.getBounds();
			minx = Math.min(minx, r.getMinX());
			miny = Math.min(miny, r.getMinY());
		}
		return new Point2DAdv(minx, miny);
	}

	public Point2D getMaxPoint(Graphics g) {
		Point2D max = GraphHelper.getMaxPoint(myGraph, g);
		double maxx = max.getX(), maxy = max.getY();

		for (State vert : myStateLabels.keySet()) {
			Note sLabel = myStateLabels.get(vert);
			if (sLabel != null) {
				Rectangle lBounds = sLabel.getBounds();
				maxx = Math.max(maxx, lBounds.getMaxX());
				maxy = Math.max(maxy, lBounds.getMaxY());
			}
		}

		for (Note n : myNotes.keySet()) {
			Rectangle r = n.getBounds();
			maxx = Math.max(maxx, r.getMaxX());
			maxy = Math.max(maxy, r.getMaxY());
		}
		return new Point2DAdv(maxx, maxy);
	}

	/**
	 * Returns the state at the specified point, if one exists. If there are
	 * multiple, it will return the most recently created one (which is rendered
	 * as "on top" as well).
	 */
	private State stateAtPoint(Point2D p) {
		State[] states = myAutomaton.getStates().toArray(new State[0]);
		for (int i = states.length - 1; i >= 0; i--) {
			State s = states[i];
			Point2D point = myGraph.pointForVertex(s);
			if (p.distance(point) <= getStateRadius() || (isWithinBlock(s, p)))
				return s;
		}
		return null;
	}

	/**
	 * Returns the transition at the given point, which is calculated by seeing
	 * if the point is within the label bounds of that transition.
	 */
	private S transitionAtPoint(Point2D p) {
		for (S trans : myAutomaton.getTransitions()) {
			LabelBounds bounds = GraphHelper.getLabelBounds(myGraph, trans,
					getGraphics());
			bounds = new LabelBounds(-bounds.getAngle(), bounds.getRectangle());
			if (bounds.contains(p))
				return trans;
		}
		return null;
	}

	/**
	 * Returns the arrow at the specified point, if one exists, by checking if
	 * there is an edge that intersects that point.
	 */
	private State[] arrowAtPoint(Point2D p) {
		for (S trans : myAutomaton.getTransitions()) {
			State from = trans.getFromState(), to = trans.getToState();

			CurvedArrow edge = GraphHelper.getArrow(from, to, myGraph);
			if (CurvedArrow.intersects(p, 2, edge))
				return new State[] { from, to };
		}
		return null;
	}

	private Note noteAtPoint(Point2D p) {
		for (Note n : myNotes.keySet()) {
			if (n.getBounds().contains(p)) {
				return n;
			}
		}
		return null;
	}

	private boolean isWithinBlock(State s, Point2D p) {
		if (!(s instanceof Block))
			return false;
		Point2D center = myGraph.pointForVertex(s);
		int size = (int) getStateRadius();

		int x = (int) center.getX() - size;
		int y = (int) center.getY() - size;
		Rectangle r = new Rectangle(x, y, size * 2, size * 2);

		return r.contains(p);
	}

	/**
	 * Returns the radius of the vertex drawer.
	 */
	public double getStateRadius() {
		return myDrawer.getVertexDrawer().getVertexRadius();
	}

	/**
	 * Returns the radius of the vertex drawer with some additional padding for
	 * bounds.
	 */
	public double getStateBounds() {
		return getStateRadius() + PADDING;
	}

	private IUndoRedo createTransitionRemove(S... trans) {
		return createTransitionRemove(Arrays.asList(trans));
	}

	private class DeleteAction extends AbstractAction{

		@Override
		public void actionPerformed(ActionEvent e) {
			State[] states = myDrawer.getSelectedStates().toArray(
					new State[0]);
			Set<S> trans = new TreeSet<S>(myDrawer.getSelectedTransitions());
			Set<Note> notes = new HashSet<Note>(myDrawer.getSelectedNotes());

			myDrawer.clearSelection();

			TransitionSet<S> transitionSet = myAutomaton.getTransitions();
			UndoKeeper keeper = getKeeper();
			NoteRemoveEvent noteRemove = null;

			if (notes.size() > 0)
				noteRemove = new NoteRemoveEvent(AutomatonEditorPanel.this,
						notes);

			if (states.length > 0) {
				Point2D[] points = new Point2D[states.length];
				for (int i = 0; i < states.length; i++) {
					points[i] = GraphHelper.getOnscreenPoint(
							Automaton.isStartState(myAutomaton, states[i]),
							myGraph.pointForVertex(states[i]));

					trans.addAll(transitionSet
							.getTransitionsFromState(states[i]));
					trans.addAll(transitionSet
							.getTransitionsToState(states[i]));
				}
				CompoundRemoveEvent remove = createCompoundRemoveEvent(
						states, trans, points);
				if (noteRemove != null)
					remove.addEvent(noteRemove);
				keeper.applyAndListen(remove);
			} else if (noteRemove != null) {
				CompoundUndoRedo comp = new CompoundUndoRedo(noteRemove);
				if (!trans.isEmpty())
					comp.add(createTransitionRemove(trans));
				keeper.applyAndListen(comp);
			} else
				keeper.applyAndListen(createTransitionRemove(trans));
			
		}
	}

	/**
	 * Compound Event for undoing the deletion of a state and transitions.
	 */
	public class CompoundRemoveEvent implements IUndoRedo {

		private State[] myStates;
		private Point2D[] myPoints;
		private List<IUndoRedo> myEvents;

		public CompoundRemoveEvent(State[] states, Set<S> transitions,
				Point2D[] points) {
			if (states.length != points.length) {
				throw new AutomatonException("Error with State Deletion");
			}
			myStates = states;
			myPoints = points;
			myEvents = new ArrayList<IUndoRedo>();

			myEvents.add(new RemoveEvent<State>(myAutomaton.getStates(), states));
			for (State s : myStates) {
				if (Automaton.isStartState(myAutomaton, s))
					myEvents.add(new StartStateSetEvent(myAutomaton
							.getComponentOfClass(StartState.class), s, null));
				if (myAutomaton instanceof Acceptor
						&& Acceptor.isFinalState((Acceptor) myAutomaton, s))
					myEvents.add(new RemoveEvent<State>(
							((Acceptor) myAutomaton).getFinalStateSet(), s));
				if (myStateLabels.containsKey(s)
						&& myStateLabels.get(s) != null)
					myEvents.add(new StateLabelRemoveEvent(
							AutomatonEditorPanel.this, s));
			}
			if (!transitions.isEmpty())
				myEvents.add(new TransitionRemoveEvent(transitions));
		}

		public void addEvent(IUndoRedo event) {
			myEvents.add(event);
		}

		@Override
		public boolean undo() {
			boolean allUndone = true;
			for (int i = 0; i < myEvents.size(); i++) {
				if (!myEvents.get(i).undo())
					allUndone = false;
				// Move all vertexes to their original positions before messing
				// with them.
				if (i == 0)
					for (int j = 0; j < myStates.length; j++)
						myGraph.moveVertex(myStates[j], myPoints[j]);
			}
			clearSelection();
			return allUndone;
		}

		@Override
		public boolean redo() {
			boolean allRedone = true;
			for (int i = myEvents.size() - 1; i >= 0; i--)
				if (!myEvents.get(i).redo())
					allRedone = false;
			clearSelection();
			return allRedone;
		}

		@Override
		public String getName() {
			return "Remove State and all transitions";
		}
	}

	private class TransitionRemoveEvent extends RemoveEvent<S> {

		Map<S, Point2D> myPoints;

		public TransitionRemoveEvent(Collection<S> transitions) {
			this((S[]) transitions.toArray(new Transition[0]));
		}

		public TransitionRemoveEvent(S... transitions) {
			super(myAutomaton.getTransitions(), transitions);
			myPoints = new HashMap<S, Point2D>();

			for (S trans : transitions){
				myPoints.put(trans, myGraph.getControlPt(trans).toBasicPoint());
			}
		}

		@Override
		public boolean redo() {
			clearSelection();
			return super.redo();
		}

		@Override
		public boolean undo() {
			boolean undo = super.undo();

			for(S trans : myPoints.keySet()){
				moveCtrlPoint(trans.getFromState(), trans.getToState(), myPoints.get(trans));
			}
			clearSelection();
			return undo;
		}
	}
}
