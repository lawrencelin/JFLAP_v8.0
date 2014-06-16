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




package view.action.automata;

import java.awt.Point;
import java.awt.event.ActionEvent;
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
import model.undo.IUndoRedo;
import util.Point2DAdv;
import view.automata.Note;
import view.automata.editing.AutomatonEditorPanel;
import view.automata.undoing.CompoundMoveEvent;
import view.automata.undoing.ControlMoveEvent;
import view.automata.undoing.NoteMoveEvent;
import view.automata.undoing.StateMoveEvent;
import view.automata.views.AutomatonView;

/**
 * Action that allows for the current automaton layout to be saved and possibly restored later.
 * This action itself will save the current layout of the automaton, and an action stored inside
 * this action will restore the automaton's saved layout.
 * 
 * @author Chris Morgan, Ian McMahon
 */
public class LayoutStorageAction extends AutomatonAction {
	/**
	 * Action that when invoked restores the automaton to the points stored in the
	 * graph.
	 */
	private AutomatonAction restoreAction;
	private Map<Object, Point2D> myObjectsToPoints;
	
	/**
	 * Constructor.
	 * 
	 * @param saveString the title of this action.
	 * @param restoreString the title of the action that restores the saved layout.
	 * @param automaton the automaton whose layout will be saved or restored.
	 */
	public LayoutStorageAction(String saveString, String restoreString, AutomatonView view){
		super(saveString, view);
		myObjectsToPoints = new HashMap<Object, Point2D>();
		
		restoreAction = new AutomatonAction(restoreString, view) {
			public void actionPerformed(ActionEvent e) {
				AutomatonEditorPanel panel = getEditorPanel();
				Automaton auto = panel.getAutomaton();	
				List<Note> notes = panel.getNotes();
				
				List<StateMoveEvent> sMove = new ArrayList<StateMoveEvent>();
				List<IUndoRedo> otherMove = new ArrayList<IUndoRedo>();
				Point2D current, prev;
				for(Object o : myObjectsToPoints.keySet()){
					if(o instanceof State){
						State s = (State) o;
						current = new Point2DAdv(panel.getPointForVertex(s));
						prev = myObjectsToPoints.get(s);
						
						if(!current.equals(prev)){
							sMove.add(new StateMoveEvent(panel, auto, s, current, prev));
						}
					} else if (o instanceof State[]){
						State[] edge = (State[]) o;
						current = new Point2DAdv(panel.getControlPoint(edge));
						prev = myObjectsToPoints.get(edge);
						
						if(!current.equals(prev)){
							otherMove.add(new ControlMoveEvent(panel, edge, current, prev));
						}
					} else{
						Note n = (Note) o;
						current = new Point(n.getLocation());
						prev = myObjectsToPoints.get(n);
						
						if(!current.equals(prev)){
							otherMove.add(new NoteMoveEvent(panel, n, (Point) current, (Point) prev));
						}
					}
				}
				CompoundMoveEvent event = new CompoundMoveEvent(panel, sMove);
				event.addEvents(otherMove);
				
				if(!event.isEmpty())
					panel.getKeeper().applyAndListen(event);
			}
		};
		restoreAction.setEnabled(false);			
	}
	
	/**
	 * Fetches the action used to restore the saved layout.
	 * 
	 * @return the action used to restore the saved layout.
	 */
	public AutomatonAction getRestoreAction() {
		return restoreAction;
	}		
	
	public void actionPerformed(ActionEvent e) {
		myObjectsToPoints.clear();
		AutomatonEditorPanel panel = getEditorPanel();
		Automaton auto = panel.getAutomaton();
		
		StateSet states = auto.getStates();		
		TransitionSet<? extends Transition<?>> transitions = auto.getTransitions();
		List<Note> notes = panel.getNotes();
		
		for(State s : states){
			myObjectsToPoints.put(s, new Point2DAdv(panel.getPointForVertex(s)));
		}
		for(Transition t : transitions){
			State[] edge = new State[]{t.getFromState(), t.getToState()};
			myObjectsToPoints.put(edge, new Point2DAdv(panel.getControlPoint(edge)));
		}
		for(Note n : notes){
			myObjectsToPoints.put(n, new Point(n.getLocation()));
		}
		restoreAction.setEnabled(true);
	}
}
