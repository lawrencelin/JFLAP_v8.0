package test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;

import debug.JFLAPDebug;
import model.algorithms.testinput.simulate.AutomatonSimulator;
import model.algorithms.testinput.simulate.ConfigurationChain;
import model.algorithms.testinput.simulate.SingleInputSimulator;
import model.automata.Automaton;
import model.automata.State;
import model.automata.Transition;
import model.graph.TransitionGraph;
import model.symbols.symbolizer.Symbolizers;
import model.undo.UndoKeeper;
import util.JFLAPConstants;
import view.action.automata.SimulateAction;
import view.automata.AutomatonDrawer;
import view.automata.StateDrawer;
import view.automata.editing.AutomatonEditorPanel;
import view.automata.simulate.ConfigurationButton;
import view.automata.views.AutomatonView;
import view.graph.GraphDrawer;
import file.xml.XMLCodec;
import file.xml.graph.AutomatonEditorData;

public class AutomataDrawerTest extends TestHarness implements JFLAPConstants{

	@Override
	public void runTest() {
		String toSave = System.getProperties().getProperty("user.dir")
				+ "/filetest";
		File f = new File(toSave + "/ex1.6a.jff");
		AutomatonEditorData a = (AutomatonEditorData) new XMLCodec().decode(f);
		TransitionGraph graph = a.getGraph();
		outPrintln("After import:\n" + a.toString());
		JFrame frame =  new JFrame();
		AutomatonView panel = new AutomatonView(graph.getAutomaton(), new UndoKeeper(), true);
		((AutomatonEditorPanel) panel.getCentralPanel()).setGraph(graph);
		
//		Simu
//		panel.add(new ConfigurationButton(config))
		panel.setOpaque(true);
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
		panel.repaint();
	}

	@Override
	public String getTestName() {
		return "Automaton Drawer";
	}

	public static void main(String[] args) {
		new AutomataDrawerTest();
	}

	private class DrawPanel<T extends Transition<T>> extends JPanel{

		private TransitionGraph<T> myGraph;

		public DrawPanel(Automaton<T> a2){
			myGraph = new TransitionGraph<T>(a2);
		}
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			this.setBackground(Color.white);
			StateDrawer vDraw = new StateDrawer();
			((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			GraphDrawer<State> drawer = 
					new AutomatonDrawer(vDraw);

			drawer.draw(myGraph, g);
		}
	}

}
