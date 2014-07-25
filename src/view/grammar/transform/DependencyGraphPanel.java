package view.grammar.transform;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;

import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import model.algorithms.conversion.gramtoauto.CFGtoFSAConverter;
import model.algorithms.conversion.gramtoauto.RGtoFSAConverter;
import model.algorithms.transform.grammar.ConstructDependencyGraph;
import model.algorithms.transform.grammar.DependencyGraph;
import model.algorithms.transform.grammar.UnitProductionRemover;
import model.automata.Automaton;
import model.automata.State;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.grammar.Grammar;
import model.grammar.Variable;
import model.symbols.Symbol;
import model.undo.UndoKeeper;
import view.EditingPanel;
import view.automata.AutomatonDisplayPanel;
import view.automata.AutomatonDrawer;
import view.automata.StateDrawer;
import view.automata.editing.AutomatonEditorPanel;
import view.automata.tools.ToolBar;
import view.automata.tools.algorithm.NonTransitionArrowTool;
import view.automata.tools.algorithm.REtoFATransitionTool;
import view.automata.tools.algorithm.VDGTransitionTool;
import view.graph.GraphDrawer;
import view.graph.VertexDrawer;

public class DependencyGraphPanel extends EditingPanel{

	private DependencyGraph myGraph;
	private UnitRemovalController myController;
	private Grammar myGrammar;
	private AutomatonEditorPanel<FiniteStateAcceptor, FSATransition> myEditorPanel;
	private int dependenciesAdded;
	private NonTransitionArrowTool<FiniteStateAcceptor, FSATransition> arrow;
	private VDGTransitionTool trans;
	private ToolBar toolBar;
	
	public DependencyGraphPanel(UndoKeeper keeper, boolean editable, 
			Grammar g, UnitProductionRemover alg, 
			UnitRemovalController control) {
		super(keeper, editable);
		this.setLayout(new BorderLayout());
		dependenciesAdded = 0;
		myGrammar = g;
		myController = control;
//		this.repaint();
//		myConstructor = new ConstructDependencyGraph(g);
//		myGraph = new DependencyGraph(g.getVariables());
//		myGraph = myConstructor.getDependencyGraph();
		myEditorPanel = initEditorPanel();
		myGraph = new DependencyGraph(myEditorPanel.getAutomaton().getStates(), g);
		this.add(createToolBar(), BorderLayout.NORTH);
		this.add(myEditorPanel, BorderLayout.CENTER);
		myEditorPanel.setSize(new Dimension(100, 100));
		
	}
	
	public void completeDependencyGraph() {
		for (State from : myEditorPanel.getAutomaton().getStates()) {
			if (myGraph.getDependency(from) != null) {
				for (State to : myGraph.getDependency(from)) {
					FSATransition trans = new FSATransition(from, to);
					if (!myEditorPanel.getAutomaton().getTransitions().contains(trans)) {
						myEditorPanel.getAutomaton().getTransitions().add(trans);
						addDependency();
					}
				}
			}
		}
	}
	
	public AutomatonEditorPanel<FiniteStateAcceptor, FSATransition> getEditorPanel() {
		return myEditorPanel;
	}
	
	public void addDependency() {
		dependenciesAdded++;
	}
	
	public boolean addTransition(State from, State to) {
		if (from.equals(to)) return false;
		FSATransition trans = new FSATransition(from, to);
		if (!myGraph.addDependency(from, to).state && !myEditorPanel.getAutomaton().getTransitions().contains(trans)) {
			myEditorPanel.getAutomaton().getTransitions().add(trans);
			return true;
		} else {
			JOptionPane.showMessageDialog(this, myGraph.addDependency(from, to).getMessage());
			return false;
		}
	}
	
	public DependencyGraph getDependencyGraph() {
		return myGraph;
	}
	
	
	public int getNumberDependenciesLeft() {
		return myGraph.getTotalDependencies() - dependenciesAdded;
	}
	
	private AutomatonEditorPanel<FiniteStateAcceptor, FSATransition> initEditorPanel() {
		CFGtoFSAConverter convert = new CFGtoFSAConverter(myGrammar);
		convert.removeFinalState();
//		AutomatonEditorPanel<Automaton<FSATransition>, FSATransition> editor = new AutomatonEditorPanel<Automaton<FSATransition>, FSATransition>(convert.getConvertedAutomaton(), new UndoKeeper(), true);
//		AutomatonDisplayPanel<Automaton<FSATransition>, FSATransition> display = new AutomatonDisplayPanel<Automaton<FSATransition>, FSATransition>(editor, editor.getAutomaton(), "Automaton haha");
		AutomatonEditorPanel<FiniteStateAcceptor, FSATransition> editor = new AutomatonEditorPanel<FiniteStateAcceptor, FSATransition>(convert.getConvertedAutomaton(), new UndoKeeper(), true);
		editor.setVDG();
		return editor;
	}
	
	private ToolBar createToolBar() {
		FiniteStateAcceptor fsa = myEditorPanel.getAutomaton();
//		System.out.println(fsa);
		// arrow tool
		arrow = new NonTransitionArrowTool<FiniteStateAcceptor, FSATransition>(myEditorPanel, fsa );
		// transition tool: connect two nodes
		trans = new VDGTransitionTool(this, myController);
		toolBar = new ToolBar(arrow, trans);
		toolBar.addToolListener(myEditorPanel);
		myEditorPanel.setTool(arrow);
		return toolBar;
	}
	
	public void resetToolBar() {
		myEditorPanel.setTool(arrow);
		toolBar.setActiveTool(0);
		toolBar.disableTool(1);
	}
	
//	@Override
//	public void paintComponent(Graphics g) {
////		super.paintComponent(g);
//		Graphics2D g2 = (Graphics2D) g;
//
//		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//				RenderingHints.VALUE_ANTIALIAS_ON);
//		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
//				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
//		setBackground(java.awt.Color.white);
////		Graphics2D g = (Graphics2D) gr.create();
////		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
////				RenderingHints.VALUE_ANTIALIAS_ON);
//		g.setColor(Color.white);
//		Dimension d = getSize();
//		g2.fillRect(0, 0, d.width, d.height);
////		g.transform(new AffineTransform());
//
////		updateBounds(g2);
//		myGraph.layout(d);
//		myDrawer.draw(myGraph, g2);
//
////		if (myTool != null)
////			myTool.draw(g2);
//	}
	
//	public Component createToolBar(T definition, )

}
