package view.automata.tools;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import debug.JFLAPDebug;
import model.automata.Automaton;
import model.automata.StartState;
import model.automata.State;
import model.automata.StateSet;
import model.automata.Transition;
import model.automata.acceptors.Acceptor;
import model.automata.acceptors.FinalStateSet;
import model.automata.acceptors.pda.PushdownAutomaton;
import model.automata.transducers.mealy.MealyMachine;
import model.automata.turing.MultiTapeTuringMachine;
import model.automata.turing.TuringMachine;
import model.automata.turing.buildingblock.Block;
import model.automata.turing.buildingblock.BlockTuringMachine;
import model.change.events.AddEvent;
import model.change.events.RemoveEvent;
import model.change.events.SetToEvent;
import model.change.events.StartStateSetEvent;
import model.graph.LayoutAlgorithm;
import model.graph.LayoutAlgorithmFactory;
import model.graph.TransitionGraph;
import model.graph.layout.VertexMover;
import model.undo.CompoundUndoRedo;
import model.undo.UndoKeeper;
import universe.JFLAPUniverse;
import universe.preferences.JFLAPPreferences;
import util.JFLAPConstants;
import util.Point2DAdv;
import util.view.GraphHelper;
import view.ViewFactory;
import view.automata.Note;
import view.automata.editing.AutomatonEditorPanel;
import view.automata.editing.BlockEditorPanel;
import view.automata.undoing.ClearSelectionEvent;
import view.automata.undoing.CompoundMoveEvent;
import view.automata.undoing.ControlMoveEvent;
import view.automata.undoing.NoteMoveEvent;
import view.automata.undoing.StateAddEvent;
import view.automata.undoing.StateLabelRemoveEvent;
import view.automata.undoing.StateMoveEvent;
import view.automata.views.AutomatonView;
import view.environment.JFLAPEnvironment;

/**
 * Tool for selection and editing of Automaton graphs.
 * 
 * @author Ian McMahon
 */
public class ArrowTool<T extends Automaton<S>, S extends Transition<S>> extends
		EditingTool<T, S> {
	private T myDef;

	private Object myObject;
	private Point2D myInitialPoint;
	private Point2D myInitialObjectPoint;
	private Point2D myMovingPoint;
	private Point2D myNoteMovingPoint;

	private StateMenu myStateMenu;
	private EmptyMenu myEmptyMenu;
	private Rectangle mySelectionBounds;

	public ArrowTool(AutomatonEditorPanel<T, S> panel, T def) {
		super(panel);
		myDef = def;
		myObject = null;
		myStateMenu = new StateMenu();
		myEmptyMenu = new EmptyMenu();
	}

	@Override
	public String getToolTip() {
		return "Attribute Editor";
	}

	@Override
	public char getActivatingKey() {
		return 'a';
	}

	@Override
	public String getImageURLString() {
		return "/ICON/arrow.gif";
	}

	@Override
	public void mousePressed(MouseEvent e) {
		AutomatonEditorPanel<T, S> panel = getPanel();
		myObject = panel.objectAtPoint(e.getPoint());

		if (e.getSource() instanceof Note) // Comes from a non-State-label Note
			myObject = e.getSource();

		if (SwingUtilities.isLeftMouseButton(e)) {
			myInitialPoint = e.getPoint();

			if (myObject != null) {
				boolean modifierDown = isModified(e);
				boolean isSelected = myObjectSelected();

				// If not selected and just a normal click, clear other
				// selections
				if (!isSelected && !modifierDown)
					panel.clearSelection();

				// If selected and shift/ctrl down, deselect object
				if (isSelected && modifierDown) {
					panel.deselectObject(myObject);
					return;
				}

				panel.selectObject(myObject);
				if (isTransitionClicked(e)) {
					if (e.getClickCount() == 2)
						panel.editTransition((S) myObject, false);
				} else {
					if (isStateClicked(e)) {
						myInitialObjectPoint = new Point2DAdv(
								panel.getPointForVertex((State) myObject));
					} else if (myObject instanceof State[]) {
						State[] edge = (State[]) myObject;
						myInitialObjectPoint = panel.getControlPoint(edge);
						panel.selectAll(myDef.getTransitions()
								.getTransitionsFromStateToState(edge[0],
										edge[1]));
					} else if (myObject instanceof Note) {
						myInitialObjectPoint = ((Note) myObject).getLocation();
						myNoteMovingPoint = (Point2D) myInitialObjectPoint
								.clone();
					}
					myMovingPoint = (Point2D) myInitialObjectPoint.clone();
				}
			} else {
				panel.stopAllEditing();
				panel.clearSelection();
			}
		} else if (SwingUtilities.isRightMouseButton(e)) {
			panel.stopAllEditing();
			panel.clearSelection();

			if (e.getSource().equals(panel)) {
				if (isStateClicked(e))
					showStateMenu(e.getPoint());
				else
					showEmptyMenu(e.getPoint());
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		AutomatonEditorPanel<T, S> panel = getPanel();
		List<Object> selectedObjs = panel.getSelection();

		if (SwingUtilities.isLeftMouseButton(e)) {

			if (myObject == null && myInitialPoint != null) {
				selectRectangle(e, panel);
				return;
			}
			if (myObjectSelected()) {
				Point drag = e.getPoint();

				if (myObject instanceof State) {
					stateDragged(e, panel, selectedObjs, drag);
				} else if (myObject instanceof State[]) {
					edgeDragged(panel, drag);
				} else if (myObject instanceof Note) {
					noteDragged(panel, drag);
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		AutomatonEditorPanel<T, S> panel = getPanel();
		UndoKeeper keeper = getKeeper();
		boolean modified = isModified(e);

		if (SwingUtilities.isLeftMouseButton(e) && isValidPoint(e)) {
			List<Object> selectedObjs = panel.getSelection();

			if (myObjectSelected()) {
				if (myObject instanceof State) {
					stateReleased(panel, keeper, modified, selectedObjs);
				} else if (myObject instanceof State[]) {
					edgeReleased(panel, keeper, modified, selectedObjs);
				} else if (myObject instanceof Note) {
					noteReleased(panel, keeper, modified, selectedObjs);
				}
				// Don't do anything if it was a transition,
				// we will have a TransitionTable open.
			}
		} else if (!(myObject instanceof Transition)) {
			panel.requestFocus();
			
			if(!modified)
				panel.clearSelection();
		}

		resetData();
		panel.repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() instanceof Note && !(isModified(e))) {
			Note n = (Note) e.getComponent();
			getPanel().editNote(n);
		}
	}

	@Override
	public void draw(Graphics g) {
		if (mySelectionBounds != null)
			g.drawRect(mySelectionBounds.x, mySelectionBounds.y,
					mySelectionBounds.width, mySelectionBounds.height);
	}

	@Override
	public void setActive(boolean active) {
		super.setActive(active);
		
		if (!active) {
			resetData();
			myObject = null;
		}
	}

	/** Returns true if o is equal to this tool's main object. */
	public boolean isMainObject(Object o) {
		return myObject != null && (myObject.equals(o) || isEdgeEqual(o));
	}

	/** Returns true if shift or ctrl was down. */
	public boolean isModified(MouseEvent e) {
		return e.isShiftDown() || e.isControlDown();
	}

	/** Returns true if myObject is currently selected. */
	private boolean myObjectSelected() {
		return getPanel().isSelected(myObject);
	}

	/** Clears all points, objects, and data. */
	private void resetData() {
		myInitialPoint = null;
		myInitialObjectPoint = null;
		myMovingPoint = null;
		myNoteMovingPoint = null;
		mySelectionBounds = null;
	}

	/**
	 * Returns true if the point is not equal to the initial point or if it is
	 * from a Note.
	 */
	private boolean isValidPoint(MouseEvent e) {
		return myInitialPoint != null
				&& (!myInitialPoint.equals(e.getPoint()) || e.getSource() instanceof Note);
	}

	/**
	 * Returns true if myObject (a State[]) is the only object in the list, or
	 * if it and its transitions are.
	 */
	private boolean isOnlyObject(List<Object> selectedObjs) {
		for (Object o : selectedObjs) {
			if (o instanceof State)
				return false;
			if (o instanceof State[] && !isEdgeEqual(o))
				return false;
			if (o instanceof Transition
					&& !isEdgeEqual(new State[] {
							((Transition) o).getFromState(),
							((Transition) o).getToState() }))
				return false;
		}
		return true;
	}

	/** Shows the state menu at the specific point. */
	private void showStateMenu(Point2D point) {
		myStateMenu.show(getPanel(), (int) point.getX(), (int) point.getY());
	}

	/** Shows the empty menu at the specific point. */
	private void showEmptyMenu(Point2D point) {
		myEmptyMenu.show(getPanel(), (int) point.getX(), (int) point.getY());
	}

	/**
	 * Returns true if the selected object is a State and the user clicked once.
	 */
	private boolean isStateClicked(MouseEvent e) {
		return myObject instanceof State;
	}

	/**
	 * Returns true if the selected object is a Transition
	 */
	public boolean isTransitionClicked(MouseEvent e) {
		return myObject instanceof Transition;
	}

	/**
	 * Returns true if myObject and o are both State[], and their from and to
	 * states are equal.
	 */
	private boolean isEdgeEqual(Object o) {
		if (!(o instanceof State[] && myObject instanceof State[]))
			return false;
		State[] o1 = (State[]) myObject, o2 = (State[]) o;
		return o1[0].equals(o2[0]) && o1[1].equals(o2[1]);
	}

	/** Drag a selection rectangle, selecting everything within its bounds. */
	private void selectRectangle(MouseEvent e, AutomatonEditorPanel<T, S> panel) {

		int nowX = e.getPoint().x;
		int nowY = e.getPoint().y;

		int leftX = (int) myInitialPoint.getX();
		int topY = (int) myInitialPoint.getY();

		if (nowX < leftX)
			leftX = nowX;
		if (nowY < topY)
			topY = nowY;
		mySelectionBounds = new Rectangle(leftX, topY, Math.abs(nowX
				- (int) myInitialPoint.getX()), Math.abs(nowY
				- (int) myInitialPoint.getY()));
		panel.selectAllInBounds(mySelectionBounds);
	}

	private void stateDragged(MouseEvent e, AutomatonEditorPanel<T, S> panel,
			List<Object> selectedObjs, Point drag) {
		// Move the state
		State s = (State) myObject;

		if (selectedObjs.size() > 1) {
			moveSelectedObjects(e, s, selectedObjs);
		}
		panel.moveState(s, drag);
		checkMin(panel);
	}

	private void edgeDragged(AutomatonEditorPanel<T, S> panel, Point drag) {
		// Move the control point
		State from = ((State[]) myObject)[0], to = ((State[]) myObject)[1];
		panel.moveCtrlPoint(from, to, new Point2DAdv(drag.getX(), drag.getY()));
		checkMin(panel);
	}

	private void noteDragged(AutomatonEditorPanel<T, S> panel, Point drag) {
		Note n = (Note) myObject;
		if (!n.isEditable()) {
			int diffX = (int) (drag.x - myInitialPoint.getX());
			int diffY = (int) (drag.y - myInitialPoint.getY());

			int nowAtX = (int) (myNoteMovingPoint.getX() + diffX);
			int nowAtY = (int) (myNoteMovingPoint.getY() + diffY);
			drag = new Point(nowAtX, nowAtY);
			panel.moveNote(n, drag);
			myNoteMovingPoint = n.getLocation();
		}
		checkMin(panel);
	}

	private void stateReleased(AutomatonEditorPanel<T, S> panel,
			UndoKeeper keeper, boolean modified, List<Object> selectedObjs) {
		State s = (State) myObject;

		Point2D pRelease = GraphHelper.getOnscreenPoint(
				Automaton.isStartState(myDef, s), panel.getPointForVertex(s));
		StateMoveEvent moveEvent = new StateMoveEvent(panel, myDef, s,
				myInitialObjectPoint, pRelease);

		// Clear the selection and notify the undo keeper
		if (!modified && selectedObjs.size() == 1)
			panel.clearSelection();
		CompoundMoveEvent comp = new CompoundMoveEvent(panel, moveEvent);
		addMoveEvents(comp, pRelease);

		keeper.registerChange(comp);
	}

	private void edgeReleased(AutomatonEditorPanel<T, S> panel,
			UndoKeeper keeper, boolean modified, List<Object> selectedObjs) {
		State[] edge = (State[]) myObject;
		Point2D ctrl = panel.getControlPoint(edge);
		ControlMoveEvent move = new ControlMoveEvent(panel, edge,
				myInitialObjectPoint, ctrl);

		// If this is the only object and this is a normal click,
		// clear selection
		if (isOnlyObject(selectedObjs) && !modified)
			panel.clearSelection();

		if (myMovingPoint.equals(myInitialObjectPoint))
			keeper.registerChange(move);

		else {
			CompoundMoveEvent comp = new CompoundMoveEvent(panel,
					new ArrayList<StateMoveEvent>());

			addMoveEvents(comp, ctrl);
			comp.addEvents(move);
			keeper.registerChange(comp);
		}
	}

	private void noteReleased(AutomatonEditorPanel<T, S> panel,
			UndoKeeper keeper, boolean modified, List<Object> selectedObjs) {
		Note n = (Note) myObject;
		if (selectedObjs.size() == 1 && !modified)
			panel.clearSelection();

		// If you have moved the note, notify undo keeper
		if (!myInitialObjectPoint.equals(myNoteMovingPoint)
				|| !myInitialObjectPoint.equals(myMovingPoint)) {
			NoteMoveEvent move = new NoteMoveEvent(panel, n,
					(Point) myInitialObjectPoint, n.getLocation());

			if (myInitialObjectPoint.equals(myMovingPoint))
				keeper.registerChange(move);
			else {
				CompoundMoveEvent comp = new CompoundMoveEvent(panel,
						new ArrayList<StateMoveEvent>());
				addMoveEvents(comp, myMovingPoint);
				comp.addEvents(move);
				keeper.registerChange(comp);
			}
		}
	}

	/**
	 * Moves myMovingPoint if the most recent drag placed objects offscreen top
	 * or left, as the rest of the objects will be moving that much. This is
	 * needed for graphically sound undo/redo.
	 * 
	 * @param panel
	 */
	private void checkMin(AutomatonEditorPanel<T, S> panel) {
		Point2D min = panel.getMinPoint(panel.getGraphics());
		double minx = min.getX(), miny = min.getY();

		if (minx < 0) {
			myMovingPoint = new Point2DAdv(myMovingPoint.getX() - (minx - 1),
					myMovingPoint.getY());
		}
		if (miny < 0) {
			myMovingPoint = new Point2DAdv(myMovingPoint.getX(),
					myMovingPoint.getY() - (miny - 1));
		}
	}

	/**
	 * Moves all objects in selectedObjs the same distance as the state that was
	 * dragged.
	 */
	private void moveSelectedObjects(MouseEvent e, State s,
			List<Object> selectedObjs) {
		AutomatonEditorPanel<T, S> panel = getPanel();
		Point2D pState = new Point2DAdv(panel.getPointForVertex(s));
		Point2D drag = e.getPoint();

		double dx = drag.getX() - pState.getX(), dy = drag.getY()
				- pState.getY();

		for (Object o : selectedObjs) {
			if (o instanceof State && !o.equals(s)) {
				Point2D current = panel.getPointForVertex((State) o);
				panel.moveState((State) o, new Point2DAdv(current.getX() + dx,
						current.getY() + dy));
			}
			if (o instanceof Note) {
				Note n = (Note) o;
				Point current = n.getLocation();
				n.setLocation(new Point((int) (current.x + dx),
						(int) (current.y + dy)));
			}
		}
	}

	/**
	 * Adds undo/redo events to the CompoundMoveEvent so that when moves are
	 * undone, they appear as they were before, even if the move shifting
	 * objects offscreen.
	 */
	private void addMoveEvents(CompoundMoveEvent comp, Point2D pRelease) {
		double dx = pRelease.getX() - myInitialObjectPoint.getX(), dy = pRelease
				.getY() - myInitialObjectPoint.getY();
		AutomatonEditorPanel<T, S> panel = getPanel();
		List<Object> selectedObjs = panel.getSelection();

		if (myMovingPoint.equals(myInitialObjectPoint))
			for (Object o : selectedObjs) {
				if (o instanceof State && !o.equals(myObject)) {
					Point2D pTo = new Point2DAdv(
							panel.getPointForVertex((State) o));
					Point2D pFrom = new Point2DAdv(pTo.getX() - dx, pTo.getY()
							- dy);

					comp.addEvents(new StateMoveEvent(panel, myDef, (State) o,
							pFrom, pTo));
				}
				if (o instanceof Note) {
					Note n = (Note) o;
					Point current = n.getLocation();
					Point old = new Point((int) (current.x - dx),
							(int) (current.y - dy));

					comp.addEvents(new NoteMoveEvent(panel, n, old, n
							.getLocation()));
				}
			}
		else {
			double dxSel = dx, dySel = dy;
			dx = myMovingPoint.getX() - myInitialObjectPoint.getX();
			dy = myMovingPoint.getY() - myInitialObjectPoint.getY();

			if (!(myObject instanceof State)) {
				dxSel = dx;
				dySel = dy;
			}
			for (State s : myDef.getStates())
				if (!s.equals(myObject)) {
					Point2D pTo = new Point2DAdv(panel.getPointForVertex(s));
					Point2D pFrom = panel.isSelected(s) ? new Point2DAdv(
							pTo.getX() - dxSel, pTo.getY() - dySel)
							: new Point2DAdv(pTo.getX() - dx, pTo.getY() - dy);

					comp.addEvents(new StateMoveEvent(panel, myDef, s, pFrom,
							pTo));
				}
			for (Note n : panel.getNotes()) {
				if (!n.equals(myObject)) {
					Point current = n.getLocation();
					Point old = panel.isSelected(n) ? new Point(
							(int) (current.x - dxSel),
							(int) (current.y - dySel)) : new Point(
							(int) (current.x - dx), (int) (current.y - dy));

					comp.addEvents(new NoteMoveEvent(panel, n, old,
							(Point) current.clone()));
				}
			}
		}
	}

	/**
	 * JPopupMenu with options specific to States.
	 * @author Ian McMahon
	 */
	private class StateMenu extends JPopupMenu {

		private JCheckBoxMenuItem makeFinal;
		private JCheckBoxMenuItem makeInitial;
		private JMenuItem changeLabel;
		private JMenuItem deleteLabel;
		private JMenuItem deleteAllLabels;
		private JMenuItem editBlock;
		private JMenuItem copyBlock;
		private JMenuItem setName;

		public StateMenu() {
			if (myDef instanceof Acceptor)
				addFinalButton();

			addInitialButton();
			addLabelButtons();

			if (myDef instanceof BlockTuringMachine)
				addBlockButtons();
			addSetNameButton();
		}

		private void addFinalButton() {
			makeFinal = new JCheckBoxMenuItem("Final");

			makeFinal.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					Acceptor<S> accept = (Acceptor<S>) myDef;
					FinalStateSet finalStates = accept.getFinalStateSet();
					UndoKeeper keeper = getKeeper();

					if (makeFinal.isSelected())
						keeper.applyAndListen(new AddEvent<State>(finalStates,
								(State) myObject));
					else
						keeper.applyAndListen(new RemoveEvent<State>(
								finalStates, (State) myObject));
				}
			});
			this.add(makeFinal);
		}

		private void addInitialButton() {
			makeInitial = new JCheckBoxMenuItem("Initial");

			makeInitial.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					UndoKeeper keeper = getKeeper();
					StartState start = myDef
							.getComponentOfClass(StartState.class);
					State internal = start.getState();

					if (makeInitial.isSelected())
						keeper.applyAndListen(new StartStateSetEvent(start,
								internal, (State) myObject));
					else
						keeper.applyAndListen(new StartStateSetEvent(start,
								internal, null));

				}
			});
			this.add(makeInitial);
		}

		private void addLabelButtons() {

			changeLabel = new JMenuItem("Change Label");
			changeLabel.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					AutomatonEditorPanel<T, S> panel = getPanel();
					State s = (State) myObject;

					Note oldLabel = panel.getStateLabel(s);
					String oldText = (oldLabel == null || oldLabel.getText() == null) ? ""
							: oldLabel.getText();

					String label = (String) JOptionPane.showInputDialog(panel,
							"Input a new label, or \n"
									+ "set blank to remove the label",
							"New Label", JOptionPane.QUESTION_MESSAGE, null,
							null, oldText);
					if (label == null)
						return;
					if (label.equals(""))
						label = null;
					panel.changeStateLabel((State) myObject, label);
				}
			});
			deleteLabel = new JMenuItem("Clear Label");
			deleteLabel.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					getKeeper().applyAndListen(
							new StateLabelRemoveEvent(getPanel(),
									(State) myObject));
				}
			});
			deleteAllLabels = new JMenuItem("Clear All Labels");
			deleteAllLabels.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					CompoundUndoRedo comp = null;
					for (State s : myDef.getStates()) {
						if (getPanel().getStateLabel(s) != null) {
							StateLabelRemoveEvent remove = new StateLabelRemoveEvent(
									getPanel(), s);
							if (comp == null)
								comp = new CompoundUndoRedo(remove);
							else
								comp.add(remove);
						}
					}
					if (comp != null)
						getKeeper().applyAndListen(comp);
				}
			});
			add(changeLabel);
			add(deleteLabel);
			add(deleteAllLabels);
		}

		private void addBlockButtons() {
			editBlock = new JMenuItem("Edit Block");
			editBlock.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					JFLAPEnvironment env = JFLAPUniverse.getActiveEnvironment();
					BlockEditorPanel panel = (BlockEditorPanel) getPanel();
					Block b = (Block) myObject;
					
					TuringMachine m = b.getTuringMachine();
					
					AutomatonView view = (AutomatonView) ViewFactory
							.createView(m);
					AutomatonEditorPanel central = (AutomatonEditorPanel) view.getCentralPanel();
					central.setGraph(panel.getGraph(b));
					
					Dimension size = env.getSize();

					env.addSelectedComponent(view);
					env.setSize(size);
					env.revalidate();
					env.update();

				}
			});
			copyBlock = new JMenuItem("Duplicate Block");
			copyBlock.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					Block b = (Block) myObject;
					TuringMachine m = b.getTuringMachine();
					BlockEditorPanel panel = (BlockEditorPanel) getPanel();
					TransitionGraph graph = panel.getGraph(b);
					
					Point2D p = panel.getPointForVertex(b);
					p = new Point((int) p.getX() + JFLAPConstants.STATE_RADIUS
							* 2 + 5, (int) p.getY());

					b = panel.addBlock(b, (Point) p);
					panel.setGraph(b, graph);
					
					getKeeper()
							.registerChange(
									new StateAddEvent(panel, panel
											.getAutomaton(), b, p));
				}
			});
			add(editBlock);
			add(copyBlock);
		}

		private void addSetNameButton() {
			setName = new JMenuItem("Set Name");
			setName.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					State state = (State) myObject;
					String oldName = state.getName();
					String name = (String) JOptionPane.showInputDialog(
							getPanel(), "Input a new name, or \n"
									+ "set blank to remove the name",
							"New Name", JOptionPane.QUESTION_MESSAGE, null,
							null, oldName);
					if (name == null || name.equals(oldName))
						return;
					State newState = new State(name, state.getID());
					getKeeper()
							.applyAndListen(
									new SetToEvent<State>(state, state.copy(),
											newState));
				}
			});
			add(setName);
		}

		@Override
		public void show(Component invoker, int x, int y) {
			super.show(invoker, x, y);
			if (makeFinal != null) // only happens when def instanceof Acceptor
				makeFinal.setSelected(Acceptor.isFinalState(
						(Acceptor<S>) myDef, (State) myObject));
			makeInitial.setSelected(Automaton.isStartState(myDef,
					(State) myObject));
			deleteLabel
					.setEnabled(getPanel().getStateLabel((State) myObject) != null);
		}
	}

	/**
	 * The contextual menu class for context clicks in blank space.
	 */
	private class EmptyMenu extends JPopupMenu {

		// private JCheckBoxMenuItem stateLabels;
		private JMenuItem layoutGraph;
		private JMenuItem renameStates;
		private JMenuItem createNote;
		private JMenuItem autoZoom;
		private Point myPoint;

		public EmptyMenu() {
			addLayoutGraph();
			if (!(myDef instanceof BlockTuringMachine))
				addRenameStates();
			addCreateNote();
			addAutoZoom();
		}

		private void addLayoutGraph() {
			layoutGraph = new JMenuItem("Layout Graph");
			layoutGraph.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					AutomatonEditorPanel<T, S> panel = getPanel();

					panel.layoutGraph();
				}
			});
			this.add(layoutGraph);
		}

		private void addRenameStates() {
			renameStates = new JMenuItem("Rename States");
			renameStates.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					CompoundUndoRedo comp = new CompoundUndoRedo(
							new ClearSelectionEvent(getPanel()));

					StateSet states = myDef.getStates();
					int maxId = states.size() - 1;
					TreeSet<Integer> untaken = new TreeSet<Integer>();
					Set<State> reassign = states.copy();

					for (int i = 0; i <= maxId; i++)
						untaken.add(new Integer(i));
					for (State s : states)
						if (untaken.remove(new Integer(s.getID()))) {
							reassign.remove(s);
							String basic = JFLAPPreferences
									.getDefaultStateNameBase() + s.getID();

							if (!s.getName().equals(basic))
								comp.add(new SetToEvent<State>(s, s.copy(),
										new State(basic, s.getID())));
						}
					// Now untaken has the untaken IDs, and reassign has the
					// states that need reassigning.
					for (State s : states) {
						if (reassign.contains(s)) {
							int newID = untaken.pollFirst();
							String basic = JFLAPPreferences
									.getDefaultStateNameBase() + newID;

							if (!s.getName().equals(basic))
								comp.add(new SetToEvent<State>(s, s.copy(),
										new State(basic, newID)));
						}
					}
					if (comp.size() > 1)
						getKeeper().applyAndListen(comp);
				}
			});
			this.add(renameStates);
		}

		private void addCreateNote() {
			createNote = new JMenuItem("Create Note");
			createNote.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					getPanel().createAndAddNote(myPoint);
				}
			});
			this.add(createNote);
		}

		private void addAutoZoom() {
			autoZoom = new JMenuItem("Fit to screen");
			autoZoom.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					AutomatonEditorPanel panel = getPanel();
					Rectangle visible = panel.getVisibleRect();
					double vertexBuffer;
					if (myDef instanceof MultiTapeTuringMachine)
						vertexBuffer = 80 * ((MultiTapeTuringMachine) myDef)
								.getNumTapes();
					else if (myDef instanceof PushdownAutomaton)
						vertexBuffer = 80;
					else if (myDef instanceof MealyMachine)
						vertexBuffer = 65;
					else
						vertexBuffer = JFLAPConstants.STATE_RADIUS * 2;

					LayoutAlgorithm layout = LayoutAlgorithmFactory
							.getLayoutAlgorithm(
									VertexMover.FILL,
									new Dimension(visible.width, visible.height),
									new Dimension(
											JFLAPConstants.STATE_RADIUS + 5,
											JFLAPConstants.STATE_RADIUS + 5),
									vertexBuffer);
					LayoutAlgorithm current = panel.getLayoutAlgorithm();
					panel.setLayoutAlgorithm(layout);
					panel.layoutGraph();
					panel.setLayoutAlgorithm(current);
				}
			});
			this.add(autoZoom);
		}

		public void show(Component comp, int x, int y) {
			// stateLabels.setSelected(getDrawer().doesDrawStateLabels());
			// adaptView.setSelected(getView().getAdapt());
			myPoint = new Point(x, y);
			super.show(comp, x, y);
		}
	}
}
