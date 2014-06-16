package view.algorithms.transform;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.JSplitPane;

import model.algorithms.transform.turing.StayOptionRemover;
import model.automata.State;
import model.automata.StateSet;
import model.automata.TransitionSet;
import model.automata.turing.MultiTapeTMTransition;
import model.automata.turing.MultiTapeTuringMachine;
import model.graph.TransitionGraph;
import model.undo.UndoKeeper;
import universe.JFLAPUniverse;
import util.view.magnify.MagnifiablePanel;
import util.view.magnify.MagnifiableScrollPane;
import view.ViewFactory;
import view.automata.AutomatonDisplayPanel;
import view.automata.editing.AutomatonEditorPanel;
import view.automata.simulate.TooltipAction;
import view.automata.tools.ToolBar;
import view.automata.tools.algorithm.TransitionSelectionTool;
import view.environment.JFLAPEnvironment;
import file.xml.graph.AutomatonEditorData;

public class StayOptionRemovalPanel extends AutomatonDisplayPanel<MultiTapeTuringMachine, MultiTapeTMTransition>{

	private StayOptionRemover myAlg;
	private AutomatonEditorPanel<MultiTapeTuringMachine, MultiTapeTMTransition> myNewTMpanel;

	public StayOptionRemovalPanel(
			AutomatonEditorPanel<MultiTapeTuringMachine, MultiTapeTMTransition> editor,
			StayOptionRemover remove) {
		super(editor, editor.getAutomaton(), "Stay Option Remover");
		myAlg = remove;
		updateSize();
		initView();
	}

	private void initView() {
		AutomatonEditorPanel<MultiTapeTuringMachine, MultiTapeTMTransition> oldTM = getEditorPanel();
		myNewTMpanel = new AutomatonEditorPanel<MultiTapeTuringMachine, MultiTapeTMTransition>(
				myAlg.getTransformedDefinition(), new UndoKeeper(), true);
		myNewTMpanel.getActionMap().put(AutomatonEditorPanel.DELETE, null);
		resetGraph(oldTM);

		MagnifiableScrollPane oldScroll = new MagnifiableScrollPane(oldTM), newScroll = new MagnifiableScrollPane(
				myNewTMpanel);
		Dimension oSize = oldTM.getMinimumSize();
		int padding = (int) (oldTM.getStateBounds() - oldTM.getStateRadius());
		oldScroll.setMinimumSize(new Dimension(oSize.width + padding,
				oSize.height));
		MagnifiablePanel right = new MagnifiablePanel(new BorderLayout());

		ToolBar tools = createTools();
		right.add(tools, BorderLayout.NORTH);
		right.add(newScroll, BorderLayout.CENTER);

		Dimension rSize = right.getMinimumSize();
		int width = (int) (rSize.width * 1.5);
		right.setMinimumSize(new Dimension(width, rSize.height));

		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, oldScroll,
				right);
		add(split, BorderLayout.CENTER);

		Dimension size = getPreferredSize();
		rSize = right.getMinimumSize();
		width = size.width + rSize.width;
		setPreferredSize(new Dimension(width, size.height));
	}

	private ToolBar createTools() {
		TransitionSelectionTool trans = new TransitionSelectionTool(myNewTMpanel, myAlg);
		ToolBar tools = new ToolBar(trans);
		tools.addToolListener(myNewTMpanel);
		myNewTMpanel.setTool(trans);
		
		tools.addSeparator();
		
		tools.add(new TooltipAction("Complete", "This will finish all replacements.") {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				TransitionGraph<MultiTapeTMTransition> graph = myNewTMpanel.getGraph();
				MultiTapeTuringMachine machine = myNewTMpanel.getAutomaton();
				
				while(myAlg.canStep()){
					Set<State> existing = machine.getStates().copy();
					MultiTapeTMTransition trans = myAlg.getFirstTransition();
					myAlg.step();
					
					Set<State> added = machine.getStates().copy();
					added.removeAll(existing);

					for(State s : added){
						State from = trans.getFromState(), to = trans.getToState();
						Point2D p = graph.getDefaultControlPoint(from, to);
						myNewTMpanel.moveState(s, p);
					}
				}
			}
		});
		
		tools.add(new TooltipAction("Done?", "Are we finished?") {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				done();
			}
		});
		
		tools.add(new TooltipAction("Export", "Display S-Option free TM in new window.") {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				export();
			}
		});
		
		return tools;
	}

	private void done() {
		int remaining = myAlg.getNumUnconverted();
		JFLAPEnvironment env = JFLAPUniverse.getActiveEnvironment();
		if(remaining > 0){
			String tRemaining = remaining +" more Stay transition" + (remaining == 1 ? "": "s")+ " must be converted.";
			String message = "The TM has not been completed\n"+tRemaining;
			JOptionPane.showMessageDialog(env, message);
		} else
			JOptionPane.showMessageDialog(env, "The TM is fully converted!");
	}

	private void export() {
		if(myAlg.isRunning())
			done();
		else{
			JOptionPane.showMessageDialog(JFLAPUniverse.getActiveEnvironment(),
					"The converted TM will now be placed in a new window.");
			AutomatonEditorData<MultiTapeTuringMachine, MultiTapeTMTransition> data = new AutomatonEditorData<MultiTapeTuringMachine, MultiTapeTMTransition>(myNewTMpanel);
			JFLAPUniverse.registerEnvironment(ViewFactory
					.createAutomataView(data));
		}
	}

	private void resetGraph(
			AutomatonEditorPanel<MultiTapeTuringMachine, MultiTapeTMTransition> oldTM) {
		MultiTapeTuringMachine tm = oldTM.getAutomaton();
		StateSet states = tm.getStates();
		TransitionSet<MultiTapeTMTransition> transitions = tm.getTransitions();
		
		for(State s : states)
			myNewTMpanel.moveState(s, oldTM.getPointForVertex(s));
		for(MultiTapeTMTransition trans : transitions){
			State[] edge = new State[]{trans.getFromState(), trans.getToState()};
			myNewTMpanel.moveCtrlPoint(edge[0], edge[1], oldTM.getControlPoint(edge));
		}	
	}

}
