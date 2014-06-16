package view.algorithms.transform;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.ViewFactory;

import debug.JFLAPDebug;
import file.xml.graph.AutomatonEditorData;
import model.algorithms.transform.fsa.AddTrapStateAlgorithm;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.change.events.AdvancedChangeEvent;
import model.symbols.Symbol;
import universe.JFLAPUniverse;
import view.automata.AutomatonDisplayPanel;
import view.automata.editing.AutomatonEditorPanel;
import view.automata.tools.ToolBar;
import view.automata.tools.algorithm.NonTransitionArrowTool;
import view.automata.tools.algorithm.TrapStateTool;
import view.automata.tools.algorithm.TrapTransitionTool;

public class TrapStatePanel extends
		AutomatonDisplayPanel<FiniteStateAcceptor, FSATransition>{

	private AddTrapStateAlgorithm myAlg;

	public TrapStatePanel(
			AutomatonEditorPanel<FiniteStateAcceptor, FSATransition> editor,
			AddTrapStateAlgorithm alg) {
		super(editor, alg.getDFAWithTrapState(), "Adding Trap State");
		myAlg = alg;
		updateSize();
		initView();
	}
	
	@Override
	public void updateSize() {
		super.updateSize();
		setPreferredSize(new Dimension(getPreferredSize().width, getPreferredSize().height + 100));
	}

	public void initView() {
		AutomatonEditorPanel<FiniteStateAcceptor, FSATransition> panel = getEditorPanel();
		FiniteStateAcceptor auto = panel.getAutomaton();

		JScrollPane scroller = new JScrollPane(panel);
		scroller.revalidate();

		JPanel north = new JPanel(new BorderLayout());

		JPanel labels = new JPanel(new BorderLayout());
		JLabel mainLabel = new JLabel();
		JLabel detailLabel = new JLabel();

		labels.add(mainLabel, BorderLayout.NORTH);
		labels.add(detailLabel, BorderLayout.SOUTH);

		NonTransitionArrowTool<FiniteStateAcceptor, FSATransition> arrow = new NonTransitionArrowTool<FiniteStateAcceptor, FSATransition>(
				panel, auto);
		TrapStateTool state = new TrapStateTool(panel, myAlg);
		TrapTransitionTool transition = new TrapTransitionTool(panel, myAlg);
		panel.setTool(arrow);
		ToolBar tools = new ToolBar(arrow, state, transition);
		
		tools.addToolListener(panel);
		tools.addSeparator();
		tools.add(new JButton(new AbstractAction("Do All") {
			public void actionPerformed(ActionEvent e) {
				if(!myAlg.hasTrapState())
					JOptionPane.showMessageDialog(TrapStatePanel.this,
							"Just create a state.\nI believe in you.",
							"Create the State", JOptionPane.ERROR_MESSAGE);
				else
					myAlg.stepToCompletion();
			}
		}));
		
		addListeners(mainLabel, detailLabel);

		north.add(labels, BorderLayout.NORTH);
		north.add(tools, BorderLayout.SOUTH);

		add(north, BorderLayout.NORTH);
		add(scroller, BorderLayout.CENTER);
	}

	private void addListeners( final JLabel main, final JLabel detail) {
		ChangeListener listen = new ChangeListener() {

			private Set<Symbol> myReadSets = new TreeSet<Symbol>(myAlg.getDFAWithTrapState().getAllSymbolsInAlphabets());
			private JLabel mainLabel = main;
			private JLabel detailLabel = detail;
			
			@Override
			public void stateChanged(ChangeEvent e) {
				if(e instanceof AdvancedChangeEvent) 
					return;
				update();
			}

			public void update() {
				if(!myAlg.hasTrapState()){
					mainLabel.setText("Make Single Trap State");
					detailLabel.setText("Create a new state to make a single trap state.");
				} else if (myAlg.isRunning()){
					int remaining = myAlg.transitionsRemaining();
					mainLabel.setText("Adding Transitions    Readable String : "+myReadSets);
					detailLabel.setText("Put transitions from all states to the trap state.   "+remaining+
							" transitions must be added");
				} else{
					mainLabel.setText("Adding a Trap State and Transitions is Finished!");
					detailLabel.setText("");
					JOptionPane.showMessageDialog(JFLAPUniverse.getActiveEnvironment(), "The DFA is now complete!\n"
							+ "It will now be placed in a new window.");
					AutomatonEditorData<FiniteStateAcceptor, FSATransition> data = new AutomatonEditorData<FiniteStateAcceptor, FSATransition>(getEditorPanel());
					JFLAPUniverse.registerEnvironment(view.ViewFactory.createAutomataView(data));
				}
			}
		};

		myAlg.addListener(listen);
		listen.stateChanged(null);
	}

}
