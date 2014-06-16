package view.action.automata;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import debug.JFLAPDebug;
import file.xml.graph.AutomatonEditorData;
import model.automata.Automaton;
import model.automata.AutomatonException;
import model.automata.State;
import model.automata.Transition;
import model.automata.TransitionSet;
import model.automata.acceptors.Acceptor;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.transducers.moore.MooreMachine;
import model.automata.transducers.moore.MooreOutputFunction;
import model.automata.turing.MultiTapeTuringMachine;
import model.automata.turing.buildingblock.Block;
import model.automata.turing.buildingblock.BlockTuringMachine;
import model.graph.BlockTMGraph;
import model.graph.TransitionGraph;
import model.symbols.SymbolString;
import universe.JFLAPUniverse;
import util.Point2DAdv;
import view.ViewFactory;
import view.automata.AutomatonDrawer;
import view.automata.Note;
import view.automata.editing.AutomatonEditorPanel;
import view.automata.editing.BlockEditorPanel;
import view.automata.editing.MooreEditorPanel;
import view.automata.views.AutomatonView;
import view.environment.JFLAPEnvironment;

public class CombineAutomataAction extends AutomatonAction {

	public CombineAutomataAction(AutomatonView view) {
		super("Combine Automata", view);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		AutomatonEditorPanel panel = getEditorPanel();
		Automaton auto = panel.getAutomaton();

		JFLAPEnvironment[] enviros = JFLAPUniverse.getRegistry().toArray(
				new JFLAPEnvironment[0]);
		JFLAPEnvironment active = JFLAPUniverse.getActiveEnvironment();
		JComboBox<JFLAPEnvironment> combo = new JComboBox<JFLAPEnvironment>();

		for (int i = 0; i < enviros.length; i++) {
			JFLAPEnvironment env = enviros[i];
			Component primary = env.getPrimaryView();

			if (!env.equals(active) && isValid(auto, primary))
				combo.addItem(env);
		}

		if (combo.getItemCount() == 0)
			throw new AutomatonException(
					"No other automata of this type around!");

		// Prompt the user.
		int result = JOptionPane.showOptionDialog(active, combo, "Combine Two",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
				null, null, null);
		if (result != JOptionPane.OK_OPTION)
			return;

		AutomatonView otherView = (AutomatonView) ((JFLAPEnvironment) combo
				.getSelectedItem()).getPrimaryView();
		AutomatonEditorData other = new AutomatonEditorData(
				(AutomatonEditorPanel) otherView.getCentralPanel());
		AutomatonEditorData copy = new AutomatonEditorData(panel);

		add(copy, other);
	}

	/**
	 * Appends other to the <CODE>newOne</CODE> automaton.
	 * 
	 * @param newOne
	 */
	private void add(AutomatonEditorData newOne, AutomatonEditorData other) {
		AutomatonView view = ViewFactory.createAutomataView(newOne);
		AutomatonEditorPanel panel = (AutomatonEditorPanel) view
				.getCentralPanel();
		Automaton auto = panel.getAutomaton();

		TransitionGraph oGraph = other.getGraph();
		Automaton oAuto = oGraph.getAutomaton();

		Map<State, State> stateMapping = new TreeMap<State, State>();
		
		addStates(panel, oGraph, stateMapping);
		addTransitions(panel, oGraph, stateMapping);
		addNotes(other, panel, stateMapping);
		
		JFLAPUniverse.registerEnvironment(view);
	}

	public void addNotes(AutomatonEditorData other, AutomatonEditorPanel panel, Map<State, State> stateMapping) {
		Map<Point2D, String> oNotes = other.getNotes();

		for (Point2D p : oNotes.keySet()) {
			panel.addNote(new Note(panel, new Point((int) p.getX(), (int) p
					.getY()), oNotes.get(p)));
		}

		Map<Point2D, String> oLabels = other.getLabels();

		for (Point2D p : oLabels.keySet()) {
			State s = (State) other.getGraph().vertexForPoint(p);

			panel.addStateLabel(stateMapping.get(s), new Note(panel, new Point(
					(int) p.getX(), (int) p.getY())), oLabels.get(p));
		}
	}

	public void addTransitions(AutomatonEditorPanel panel, TransitionGraph oGraph, Map<State, State> stateMapping) {
		Automaton auto = panel.getAutomaton();
		Automaton oAuto = oGraph.getAutomaton();
		
		TransitionSet<? extends Transition<?>> oTransitions = oAuto
				.getTransitions();

		for (Transition t : oTransitions) {
			State from = stateMapping.get(t.getFromState());
			State to = stateMapping.get(t.getToState());
			auto.getTransitions().add(t.copy(from, to));
		}
		
		//For some reason, you can't adjust the control points until all transitions are added.
		for (Transition t : oTransitions){
			State from = stateMapping.get(t.getFromState());
			State to = stateMapping.get(t.getToState());
			Point2D ctrl = oGraph.getControlPt(t);
			panel.moveCtrlPoint(from, to, ctrl);
		}
	}

	public void addStates(AutomatonEditorPanel panel,
			TransitionGraph oGraph,
			Map<State, State> stateMapping) {
		Automaton auto = panel.getAutomaton();
		Automaton oAuto = oGraph.getAutomaton();
		
		for (State s : oAuto.getStates()) {
			Point2D p = oGraph.pointForVertex(s);
			State s2 = oAuto instanceof BlockTuringMachine ? ((BlockEditorPanel) panel)
					.addBlock((Block) s, p) : panel.createState(p);

			if (oAuto instanceof Acceptor
					&& Acceptor.isFinalState((Acceptor) oAuto, s))
				((Acceptor) auto).getFinalStateSet().add(s2);

			if(Automaton.isStartState(oAuto, s) && auto.getStartState() == null)
				auto.setStartState(s2);
			
			if (oAuto instanceof BlockTuringMachine) {
				BlockEditorPanel bPanel = (BlockEditorPanel) panel;
				BlockTMGraph bGraph = (BlockTMGraph) oGraph;
				bPanel.setGraph((Block) s2, bGraph.getGraph((Block) s).copy());
			}

			if (oAuto instanceof MooreMachine) {
				MooreEditorPanel mPanel = (MooreEditorPanel) panel;
				MooreMachine n = (MooreMachine) oAuto;
				SymbolString output = n.getOutputFunctionSet()
						.getOutputForTransition(new FSATransition(s, s));

				mPanel.addOutputFunction(s2, output);
			}

			s2.setName(s.getName());
			stateMapping.put(s, s2);
		}
	}

	private boolean isValid(Automaton auto, Component primary) {
		if (!(primary instanceof AutomatonView))
			return false;
		Automaton pAuto = ((AutomatonView) primary).getDefinition();
		if (pAuto instanceof MultiTapeTuringMachine
				&& auto instanceof MultiTapeTuringMachine)
			return ((MultiTapeTuringMachine) pAuto).getNumTapes() == ((MultiTapeTuringMachine) auto)
					.getNumTapes();
		return pAuto.getClass().equals(auto.getClass());
	}

}
