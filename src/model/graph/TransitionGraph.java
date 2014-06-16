/*
 *  JFLAP - Formal Languages and Automata Package
 * 
 * 
 *  Susan H. Rodger
 *  Computer Science Department
 *  Duke University
 *  August 27, 2009

 *  Copyright (c) 2002-2009
 *  All rights reserved.

 *  JFLAP is open source software. Please see the LICENSE for terms.
 *
 */

package model.graph;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.automata.Automaton;
import model.automata.State;
import model.automata.StateSet;
import model.automata.Transition;
import model.automata.acceptors.Acceptor;
import model.automata.acceptors.FinalStateSet;
import model.change.events.AddEvent;
import model.change.events.ModifyEvent;
import model.change.events.RemoveEvent;
import model.graph.layout.GEMLayoutAlgorithm;
import util.Copyable;
import util.JFLAPConstants;
import util.Point2DAdv;
import util.arrows.GeometryHelper;
import debug.JFLAPDebug;

/**
 * Constructs a transition graph associated with the passed in automaton this
 * graph will automatically update whenever the automaton registers a change.
 * 
 * @author Julian Genkins, Ian McMahon
 * 
 * @param <T>
 */
public class TransitionGraph<T extends Transition<T>> extends
		DirectedGraph<State> implements ChangeListener, Copyable {

	private Map<Integer, List<T>> myOrderedTransitions;
	private Map<T, Point2D> myCenterMap;
	private Automaton<T> myAutomaton;
	private LayoutAlgorithm myAlg;

	public TransitionGraph(Automaton<T> a) {
		this(a, new GEMLayoutAlgorithm());
	}

	public TransitionGraph(Automaton<T> a, LayoutAlgorithm alg) {
		myOrderedTransitions = new HashMap<Integer, List<T>>();
		myCenterMap = new TreeMap<T, Point2D>();
		myAutomaton = a;
		myAutomaton.addListener(this);
		myAlg = alg;

		for (State s : a.getStates()){
			this.addVertex(s, new Point2DAdv());
			//For some reason, when you use an existing automaton, the States may be "equal" but not "==", so
			//changes (ie. renaming) to a state may not affect the startstate, finalstates, transitions, etc.
			updateEquivalence(a, s);
		}
		alg.layout(this, new HashSet<State>());
		for (T t : a.getTransitions()) {
			addTransition(t);
		}
	}

	private void updateEquivalence(Automaton<T> a, State s) {
		if(a.getStartState() != null && a.getStartState().getID() == s.getID())
			a.setStartState(s);
		if(a instanceof Acceptor){
			Acceptor accept = (Acceptor) a;
			FinalStateSet fStates = accept.getFinalStateSet();
			
			for(State state : fStates.toCopiedSet()){
				if(s.getID() == state.getID() && s != state){
					fStates.remove(state);
					fStates.add(s);
				}
			}
		}
	}

	@Override
	public boolean removeEdge(State from, State to) {
		myOrderedTransitions.remove(getID(from, to));
		boolean needUpdate = hasEdge(to, from) && isAutoBent(to, from);
		boolean remove = super.removeEdge(from, to);
		if (needUpdate)
			updateLabelCenters(to, from);
		return remove;
	}

	@Override
	public void stateChanged(ChangeEvent event) {
		Collection col;
		Iterator it;
		if (event instanceof AddEvent) {
			col = ((AddEvent) event).getToAdd();
			if (col == null || col.isEmpty())
				return;

			it = col.iterator();
			while (it.hasNext()) {
				Object o = it.next();
				if (o instanceof State
						&& event.getSource().equals(myAutomaton.getStates())) {
					addVertex((State) o, new Point2DAdv());
				} else if (o instanceof Transition)
					addTransition((T) o);
			}
		} else if (event instanceof RemoveEvent) {
			col = ((RemoveEvent) event).getToRemove();
			if (col == null || col.isEmpty())
				return;

			it = col.iterator();
			while (it.hasNext()) {
				Object o = it.next();
				if (o instanceof State
						&& event.getSource().equals(myAutomaton.getStates())) {
					removeVertex((State) o);
				} else if (o instanceof Transition)
					removeTransition((T) o);
			}
		} else if (event instanceof ModifyEvent) {
			ModifyEvent e = (ModifyEvent) event;
			Object to = e.getArg(1);
			if (to instanceof Transition) {
				updateLabelCenter((T) to);
			}

			if (to instanceof State
					&& event.getSource().equals(myAutomaton.getStates())) {
				renameState((State) e.getEventSource(), (State) to);
			}

		}
		distributeChange(event);
	}

	private void renameState(State from, State to) {
		if (from != to) {
//			if (Automaton.isStartState(myAutomaton, from))
//				myAutomaton.setStartState(to);
//			if (myAutomaton instanceof Acceptor
//					&& Acceptor.isFinalState((Acceptor) myAutomaton, from)) {
//				((Acceptor) myAutomaton).getFinalStateSet().remove(from);
//				((Acceptor) myAutomaton).getFinalStateSet().add(to);
//			}
		}
	}

	@Override
	public void update(State from, State to) {
		updateLabelCenters(from, to);
	}

	@Override
	public void setControlPt(Point2D ctrl, State from, State to) {
		super.setControlPt(ctrl, from, to);
		update(from, to);
	}

	/** Helper function to simplify control point moving. */
	public void setControlPt(Point2D ctrl, T trans) {
		setControlPt(ctrl, trans.getFromState(), trans.getToState());
	}

	/**
	 * Returns a List of all transitions from the state <i>from</i> to state
	 * <i>to</i>.
	 */
	public List<T> getOrderedTransitions(State from, State to) {
		return myOrderedTransitions.get(getID(from, to));
	}

	public void setLayoutAlgorithm(LayoutAlgorithm layout) {
		myAlg = layout;
	}

	public LayoutAlgorithm getLayoutAlgorithm() {
		return myAlg;
	}

	/**
	 * Returns the location of the center point of the label of the given
	 * transition.
	 */
	public Point2D getLabelCenter(T t) {
		return myCenterMap.get(t);
	}

	/**
	 * Returns the ControlPoint for the edge specified by the given transition
	 */
	public ControlPoint getControlPt(T trans) {
		return super.getControlPt(trans.getFromState(), trans.getToState());
	}

	/**
	 * Returns the center point for the label specified by the transition based
	 * on when it was added (lvl).
	 */
	public Point2D getLabelCenterPoint(T t, int lvl, State from, State to) {
		double d = -(lvl + 1) * JFLAPConstants.EDITOR_CELL_HEIGHT;
		Point2D ctrl = getControlPt(from, to);
		Point2D pFrom = this.pointForVertex(from), pTo = this
				.pointForVertex(to), center = GeometryHelper.getCenterPoint(
				pFrom, pTo);
		center = GeometryHelper.getCenterPoint(center, ctrl);

		if (t.isLoop())
			GeometryHelper.translate(center, Math.PI / 2, d - 5);
		else
			GeometryHelper.translatePerpendicular(center, d, pFrom, pTo);
		return center;
	}

	public Automaton<T> getAutomaton() {
		return myAutomaton;
	}

	public void layout(Set<State> unmoving) {
		myAlg.layout(this, unmoving);
	}

	/**
	 * Adds the transition to the graph, adding an edge if it is the first
	 * transition between the given transition's two states.
	 */
	private void addTransition(T t) {
		State from = t.getFromState();
		State to = t.getToState();

		StateSet states = getAutomaton().getStates();
		from = states.getStateWithID(from.getID());
		to = states.getStateWithID(to.getID());
		t.setFromState(from);
		t.setToState(to);

		if (!this.hasEdge(from, to)) {
			boolean changed = addEdge(from, to);
			if (changed)
				myOrderedTransitions.put(getID(from, to), new ArrayList<T>());
		}

		int edgeID = getID(from, to);
		List<T> stack = myOrderedTransitions.get(edgeID);
		stack.add(t);

		updateLabelCenter(t, stack.size() - 1, from, to);

		if (hasEdge(to, from) && isAutoBent(from, to))
			updateLabelCenters(to, from);
	}

	/**
	 * Removes the given transition, removing the edge if it is the last one
	 * between the two states.
	 */
	private void removeTransition(T t) {
		State from = t.getFromState();
		State to = t.getToState();
		if (!this.hasEdge(from, to))
			return;

		int edgeID = getID(from, to);

		// update ordered transitions
		List<T> order = myOrderedTransitions.get(edgeID);
		order.remove(t);

		myCenterMap.remove(t);

		// if there are no more transition to/from these states
		if (order.isEmpty()) {
			removeEdge(from, to);
		} else {
			updateLabelCenters(from, to);
			super.distributeChanged();
		}
	}

	/** Updates the label center for the given transition. */
	private void updateLabelCenter(T t) {
		State from = t.getFromState(), to = t.getToState();
		int edgeID = getID(from, to);
		List<T> stack = myOrderedTransitions.get(edgeID);
		int lvl = stack.indexOf(t);

		updateLabelCenter(t, lvl, from, to);
	}

	/** Updates all label centers for the edge between from and to. */
	private void updateLabelCenters(State from, State to) {
		int i = 0;
		for (T t : myOrderedTransitions.get(getID(from, to))) {
			updateLabelCenter(t, i++, from, to);
		}
	}

	/**
	 * Updates the label center for the given transition based off when it was
	 * added (its lvl).
	 */
	private void updateLabelCenter(T t, int lvl, State from, State to) {
		Point2D center = getLabelCenterPoint(t, lvl, from, to);
		myCenterMap.put(t, center);
		distributeChanged();
	}

	@Override
	public TransitionGraph<T> copy() {
		TransitionGraph<T> clone = new TransitionGraph<T>(
				(Automaton<T>) myAutomaton.copy(), myAlg);

		for (State s : myAutomaton.getStates()) {
			clone.moveVertex(s, new Point2DAdv(pointForVertex(s)));
		}
		for (T trans : myAutomaton.getTransitions())
			clone.setControlPt(new Point2DAdv(getControlPt(trans)), trans);
		return clone;
	}
}
