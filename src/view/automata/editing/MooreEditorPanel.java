package view.automata.editing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;

import debug.JFLAPDebug;
import model.automata.State;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.transducers.OutputFunctionSet;
import model.automata.transducers.moore.MooreMachine;
import model.automata.transducers.moore.MooreOutputFunction;
import model.change.events.RemoveEvent;
import model.graph.TransitionGraph;
import model.symbols.SymbolString;
import model.undo.UndoKeeper;
import universe.preferences.JFLAPPreferences;
import util.JFLAPConstants;
import view.automata.Note;

public class MooreEditorPanel extends
		AutomatonEditorPanel<MooreMachine, FSATransition> {
	
	public Map<State, Note> myOutput;

	public MooreEditorPanel(MooreMachine m, UndoKeeper keeper, boolean editable) {
		super(m, keeper, editable);
		myOutput = new HashMap<State, Note>();
		OutputFunctionSet<MooreOutputFunction> funcs = m.getOutputFunctionSet();
		for(State s : m.getStates())
			addOutputFunction(s, funcs.getOutputForTransition(new FSATransition(s, s)));
	}
	
	@Override
	public void setGraph(TransitionGraph<FSATransition> graph) {
		super.setGraph(graph);
		for(State s : getAutomaton().getStates())
			moveOutputFunction(s);
	}
	
	@Override
	protected void paintChildren(Graphics g) {
		super.paintChildren(g);
		
		for(State s : myOutput.keySet()){
			Note label = myOutput.get(s);
			List<Object> selection = getSelection();
			Color bg = selection.contains(s) ? JFLAPPreferences
					.getSelectedStateColor() : JFLAPPreferences.getStateColor();
		label.setBackground(bg);
		}
	}
	
	@Override
	public void moveState(State s, Point2D p) {
		super.moveState(s, p);
		Note n = myOutput.get(s);
		if(n != null){
			moveOutputFunction(s);
		}
	}
	
	public MooreOutputFunction addOutputFunction(State s, SymbolString symbols) {
		OutputFunctionSet<MooreOutputFunction> output = getAutomaton().getOutputFunctionSet();
		MooreOutputFunction func = new MooreOutputFunction(s, symbols);
		output.add(func);
		
		Point2D p = getPointForVertex(s);
		p = new Point((int) p.getX(), (int) p.getY());
		Note n = new Note(this, (Point) p, symbols.toString());
		add(n);
		n.resetBounds();
		
		n.setEditable(false);
		n.setEnabled(false);
		n.setCaretColor(JFLAPConstants.BACKGROUND_CARET_COLOR);
		
		myOutput.put(s, n);
		moveOutputFunction(s);
		return func;
	}
	
	public Note getOutputNote(State s) {
		return myOutput.get(s);
	}
	
	public String editOutputFunction(State s) {
		String old = null;
		Note n = myOutput.get(s);
		
		if(n != null)
			old = n.getText();
		if(old == null || old.isEmpty())
			old = JFLAPPreferences.getEmptyString();
		String output = (String) JOptionPane.showInputDialog(this,
				"Enter output:", "Set Output",
				JOptionPane.QUESTION_MESSAGE, null, null,
				old);

		if (output == null
				|| output.equals(JFLAPPreferences.getEmptyString()))
			output = "";
		return output;
	}
	
	public void moveOutputFunction(State s){
		Point2D point = getPointForVertex(s);
		Note output = myOutput.get(s);
		
		if(output != null){
			Rectangle bounds = output.getBounds();
			int x = (int) (point.getX() + getStateRadius() - bounds.width/2);
			int y = (int) (point.getY() - getStateRadius() - bounds.height/2);
			output.setLocation(new Point(x, y));
		}
		repaint();
	}
	
	public void removeOutputFunction(State s){
		remove(myOutput.get(s));
		myOutput.remove(s);
		repaint();
	}
	
	@Override
	public CompoundRemoveEvent createCompoundRemoveEvent(
			State[] states, Set<FSATransition> transitions, Point2D[] points) {
		CompoundRemoveEvent ev  = super.createCompoundRemoveEvent(states, transitions, points);
		for(State s : myOutput.keySet())
			ev.addEvent(new MooreOutputRemoveEvent(s));
		return ev;
	}
	
	private MooreOutputFunction getFunction(State s){
		OutputFunctionSet<MooreOutputFunction> funcs = getAutomaton().getOutputFunctionSet();
		for(MooreOutputFunction f : funcs)
			if(f.matches(new FSATransition(s, s)))
				return f;
		return null;
	}
	
	private class MooreOutputRemoveEvent extends RemoveEvent<MooreOutputFunction> {

		private State myState;
		private MooreOutputFunction myFunc;

		public MooreOutputRemoveEvent(State s) {
			super(getAutomaton().getOutputFunctionSet(), getFunction(s));
			myFunc = getFunction(s);
			myState = s;
		}
		
		@Override
		public boolean undo() {
			boolean sup = super.undo();
			addOutputFunction(myState, new SymbolString(myFunc.getOutput()));
			return sup;
		}
		
		@Override
		public boolean redo() {
			boolean sup = super.redo();
			removeOutputFunction(myState);
			return sup;
		}
		
	}
}
