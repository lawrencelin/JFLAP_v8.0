package view.automata.simulate;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import debug.JFLAPDebug;
import model.algorithms.testinput.simulate.SingleInputSimulator;
import model.automata.Automaton;
import model.automata.Transition;
import model.symbols.SymbolString;
import universe.JFLAPUniverse;
import universe.preferences.JFLAPPreferences;
import util.view.SplitFactory;
import view.automata.AutomatonDisplayPanel;
import view.automata.editing.AutomatonEditorPanel;

public class SimulatorPanel<T extends Automaton<S>, S extends Transition<S>>
		extends AutomatonDisplayPanel<T, S> {
	
	private SingleInputSimulator mySimulator;
	private SymbolString[] myInput;
	private JScrollPane scroller;
	private JPanel lower;

	public SimulatorPanel(AutomatonEditorPanel<T, S> editor,
			SingleInputSimulator simulator, SymbolString... input) {
		super(editor, editor.getAutomaton(), "Simulate");
		mySimulator = simulator;
		myInput = input;
		JFLAPPreferences.setSelectedStateColor(JFLAPPreferences.getStateColor()
				.darker().darker());
		simulator.beginSimulation(input);

		super.updateSize();
		initView();
		updateSize();
	}

	@Override
	public void updateSize() {
		super.updateSize();
		if (scroller != null) {
			scroller.revalidate();
			
			Dimension size = getPreferredSize();
			int height = size.height + lower.getPreferredSize().height;
			setPreferredSize(new Dimension(size.width, height));
		}
	}
	
	private void initView() {
		// Initialize the lower display.
		lower = new JPanel();
		lower.setLayout(new BorderLayout());

		scroller = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		ConfigurationPanel configPane = new ConfigurationPanel();
		ConfigurationController controller = new ConfigurationController(
				configPane, this);

		scroller.getViewport().setView(configPane);
		lower.add(scroller, BorderLayout.CENTER);
		lower.add(controller, BorderLayout.SOUTH);

		int height = configPane.getPreferredSize().height;
		Dimension lSize = lower.getPreferredSize();
		lower.setPreferredSize(new Dimension(lSize.width, height + controller.getPreferredSize().height));
		lSize = lower.getPreferredSize();
		
		JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, getEditorPanel(), lower);
		
		Dimension size  = getPreferredSize();
		split.setPreferredSize(new Dimension(size.width, lSize.height + size.height));
		
		double ratio = size.getHeight() / (size.height + lSize.height);
		split.setDividerLocation(ratio);
		split.setResizeWeight(ratio);
		
		add(split, BorderLayout.CENTER);
	}

	public SingleInputSimulator getSimulator() {
		return mySimulator;
	}

}
