package view.algorithms.transform;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.JSplitPane;

import file.xml.graph.AutomatonEditorData;
import model.algorithms.transform.fsa.NFAtoDFAConverter;
import model.automata.State;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.undo.UndoKeeper;
import universe.JFLAPUniverse;
import util.Point2DAdv;
import util.view.magnify.MagnifiablePanel;
import util.view.magnify.MagnifiableScrollPane;
import view.ViewFactory;
import view.automata.AutomatonDisplayPanel;
import view.automata.editing.AutomatonEditorPanel;
import view.automata.simulate.TooltipAction;
import view.automata.tools.ToolBar;
import view.automata.tools.algorithm.NonTransitionArrowTool;
import view.automata.tools.algorithm.StateExpanderTool;
import view.automata.tools.algorithm.TransitionExpanderTool;
import view.automata.views.FSAView;

public class NFAtoDFAPanel extends
		AutomatonDisplayPanel<FiniteStateAcceptor, FSATransition> {

	private NFAtoDFAConverter myAlg;
	private AutomatonEditorPanel<FiniteStateAcceptor, FSATransition> myDFApanel;

	public NFAtoDFAPanel(
			AutomatonEditorPanel<FiniteStateAcceptor, FSATransition> nfa,
			NFAtoDFAConverter convert) {
		super(nfa, nfa.getAutomaton(), "NFA to DFA");
		myAlg = convert;
		updateSize();
		initView();
	}

	private void initView() {
		AutomatonEditorPanel<FiniteStateAcceptor, FSATransition> nfa = getEditorPanel();
		myDFApanel = new AutomatonEditorPanel<FiniteStateAcceptor, FSATransition>(
				myAlg.getDFA(), new UndoKeeper(), true);
		myDFApanel.getActionMap().put(AutomatonEditorPanel.DELETE, null);
		myDFApanel.updateBounds(getGraphics());

		MagnifiableScrollPane nScroll = new MagnifiableScrollPane(nfa), dScroll = new MagnifiableScrollPane(
				myDFApanel);
		Dimension nSize = nfa.getMinimumSize();
		int padding = (int) (nfa.getStateBounds() - nfa.getStateRadius());
		nScroll.setMinimumSize(new Dimension(nSize.width + padding,
				nSize.height));
		MagnifiablePanel right = new MagnifiablePanel(new BorderLayout());

		ToolBar tools = createTools();
		right.add(tools, BorderLayout.NORTH);
		right.add(dScroll, BorderLayout.CENTER);

		Dimension rSize = right.getMinimumSize();
		int width = (int) (rSize.width * 1.5);
		right.setMinimumSize(new Dimension(width, rSize.height));

		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, nScroll,
				right);
		add(split, BorderLayout.CENTER);

		Dimension size = getPreferredSize();
		rSize = right.getMinimumSize();
		width = size.width + rSize.width;
		setPreferredSize(new Dimension(width, size.height));
		moveStartState(tools.getPreferredSize().width);

	}

	private void moveStartState(int width) {
		FiniteStateAcceptor fsa = myDFApanel.getAutomaton();
		State start = fsa.getStartState();
		Point2D current = myDFApanel.getPointForVertex(start);

		myDFApanel.moveState(start, new Point2DAdv(width, current.getY()));
	}

	private ToolBar createTools() {
		NonTransitionArrowTool<FiniteStateAcceptor, FSATransition> arrow = new NonTransitionArrowTool<FiniteStateAcceptor, FSATransition>(
				myDFApanel, myDFApanel.getAutomaton());
		TransitionExpanderTool trans = new TransitionExpanderTool(myDFApanel,
				myAlg);
		StateExpanderTool state = new StateExpanderTool(myDFApanel, myAlg);

		ToolBar tools = new ToolBar(arrow, trans, state);
		tools.addToolListener(myDFApanel);
		myDFApanel.setTool(arrow);

		tools.addSeparator();
		tools.add(new TooltipAction("Complete",
				"This will finish all expansion.") {
			public void actionPerformed(ActionEvent e) {
				while (myAlg.canStep()) {
					myAlg.step();
					// without the following line, this action will crash JFLAP. No idea
					// why, something to do with FontMetrics when drawing
					// transition labels.
					myDFApanel.layoutGraph();
				}
			}
		});
		tools.add(new TooltipAction("Done?", "Are we finished?") {
			public void actionPerformed(ActionEvent e) {
				done();
			}
		});
		tools.add(new TooltipAction("Export",
				"Display complete DFA in new window.") {

			@Override
			public void actionPerformed(ActionEvent e) {
				export();
			}
		});

		return tools;
	}

	private void export() {
		if (myAlg.isRunning())
			done();
		else {
			JOptionPane.showMessageDialog(JFLAPUniverse.getActiveEnvironment(),
					"The DFA will now be placed in a new window.");
			AutomatonEditorData<FiniteStateAcceptor, FSATransition> data = new AutomatonEditorData<FiniteStateAcceptor, FSATransition>(
					myDFApanel);
			JFLAPUniverse.registerEnvironment(ViewFactory
					.createAutomataView(data));
		}
	}

	private void done() {
		Set<State> states = myAlg.getUnexpandedStates();
		int transitionsRemaining = myAlg.numTransitionsNeeded();
		int stateSize = states.size();

		if (stateSize + transitionsRemaining != 0) {
			String statesRemaining = "All the states are there.\n";
			if (stateSize != 0) {
				String stateString = states.toString();
				stateString = stateString
						.substring(1, stateString.length() - 1);
				statesRemaining = "State" + (stateSize == 1 ? " " : "s ")
						+ stateString + " must be expanded.\n";
			}
			String trans = transitionsRemaining == 0 ? "All the transitions are there.\n"
					: transitionsRemaining + " more transition"
							+ (transitionsRemaining == 1 ? "" : "s")
							+ " must be placed.\n";
			String message = "The DFA has not been completed.\n"
					+ statesRemaining + trans;
			JOptionPane.showMessageDialog(JFLAPUniverse.getActiveEnvironment(),
					message);
			return;
		} else
			JOptionPane.showMessageDialog(JFLAPUniverse.getActiveEnvironment(),
					"The DFA is fully built!");
	}
}
