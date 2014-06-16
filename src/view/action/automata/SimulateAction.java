package view.action.automata;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import model.algorithms.testinput.simulate.SingleInputSimulator;
import model.automata.Automaton;
import model.automata.State;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.automata.acceptors.pda.PushdownAutomaton;
import model.automata.determinism.DeterminismChecker;
import model.automata.determinism.DeterminismCheckerFactory;
import model.automata.transducers.Transducer;
import model.automata.turing.MultiTapeTuringMachine;
import model.symbols.SymbolString;
import model.symbols.symbolizer.Symbolizers;
import universe.JFLAPUniverse;
import universe.preferences.JFLAPPreferences;
import util.JFLAPConstants;
import view.automata.simulate.SimulatorPanel;
import view.automata.views.AutomatonView;
import view.environment.JFLAPEnvironment;
import view.environment.TabChangeListener;
import view.environment.TabChangedEvent;
import file.BasicFileChooser;
import file.TxtFileFilter;
import file.XMLFileChooser;

public class SimulateAction extends AutomatonAction {

	private boolean myClosure;

	public SimulateAction(AutomatonView view, boolean closure) {
		super((closure ? "Step..." : "Step by State..."), view);
		Automaton auto = getAutomaton();
		myClosure = closure;
		
		if (closure && (auto instanceof FiniteStateAcceptor
				|| auto instanceof PushdownAutomaton))
			putValue(NAME, "Step with Closure...");
		int mask = JFLAPConstants.MAIN_MENU_MASK;
		if(!closure)
			mask |= KeyEvent.SHIFT_DOWN_MASK;
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_R,	mask));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Automaton auto = getAutomaton();
		if (actionPermissible()) {
			int tapes = auto instanceof MultiTapeTuringMachine ? ((MultiTapeTuringMachine) auto)
					.getNumTapes() : 0;
			Object input = openInputGUI((Component) e.getSource(), tapes);
			if (input == null)
				return;
			handleInput(input);
		}
	}

	/**
	 * Opens pop-up GUI for taking input. Now JFLAP can take file as an input.
	 * 
	 * @param component
	 * @return
	 */
	private Object openInputGUI(final Component component, final int tapes) {
		// TODO Auto-generated method stub
		JPanel panel;
		JTextField[] fields;

		// for FA, PDA
		if (tapes == 0) {
			panel = new JPanel(new GridLayout(3, 1));
			fields = new JTextField[1];
			panel.add(new JLabel("Input: "));
			panel.add(fields[0] = new JTextField());
		} else {
			panel = new JPanel(new GridLayout(tapes * 2 + 1, 2));
			fields = new JTextField[tapes];
			for (int i = 0; i < tapes; i++) {
				panel.add(new JLabel("Input " + (i + 1)));
				panel.add(fields[i] = new JTextField());
			}
		}
		JButton jb = new JButton("Click to Open Input File");
		jb.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JFileChooser ourChooser = new BasicFileChooser(new TxtFileFilter());
				int retval = ourChooser.showOpenDialog(null);
				File f = null;

				if (retval == JFileChooser.APPROVE_OPTION) {
					f = ourChooser.getSelectedFile();
					try {
						Scanner sc = new Scanner(f);
						if (tapes != 0) {
							String[] input = new String[tapes];
							for (int i = 0; i < tapes; i++) {
								if (sc.hasNext())
									input[i] = sc.next();
								else {
									JOptionPane
											.showMessageDialog(
													component,
													"Input file does not have enough input for all tapes",
													"File read error",
													JOptionPane.ERROR_MESSAGE);
									return;
								}
							}
							JOptionPane.getFrameForComponent(component)
									.dispose();
							handleInput(input);
						} else {
							String tt = sc.next();
							JOptionPane.getFrameForComponent(component)
									.dispose();
							handleInput(tt);
						}

					} catch (FileNotFoundException e1) {
						// TODO Auto-generate catch block
						e1.printStackTrace();
					}

				}

			}

		});
		panel.add(jb);
		int result = JOptionPane.showOptionDialog(component, panel, "Input",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
				null, null, null);
		if (result != JOptionPane.OK_OPTION)
			return null;
		if (tapes == 0) {
			String input = fields[0].getText();
			return input;
		} else {
			String[] input = new String[tapes];
			for (int i = 0; i < tapes; i++)
				input[i] = fields[i].getText();
			return input;
		}
	}

	public boolean actionPermissible() {
		Automaton auto = getAutomaton();
		JFLAPEnvironment env = JFLAPUniverse.getActiveEnvironment();

		if (auto.getStartState() == null) {
			JOptionPane.showMessageDialog(env,
					"Simulation requires an automaton\n"
							+ "with an initial state!", "No Initial State",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if (auto instanceof Transducer) {
			DeterminismChecker d = DeterminismCheckerFactory.getChecker(auto);
			State[] nd = d.getNondeterministicStates(auto);
			if (nd.length > 0) {
				JOptionPane
						.showMessageDialog(
								env,
								"Please remove nondeterminism for simulation.\n"
										+ "Select menu item Test : Highlight Nondeterminism\nto see nondeterministic states.",
								"Nondeterministic states detected",
								JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		// else if (automaton instanceof TuringMachine &&
		// !Universe.curProfile.transitionsFromTuringFinalStateAllowed()) {
		// TuringMachine turingMachine = (TuringMachine) automaton;
		// Object[] finalStates = turingMachine.getFinalStates();
		// AutomatonDirectedGraph graph = new
		// AutomatonDirectedGraph(turingMachine);
		// for (int i=0; i<finalStates.length; i++)
		// if (graph.fromDegree(finalStates[i], false) > 0) {
		// JOptionPane.showMessageDialog(source,
		// "There are transitions from final states.  Please remove them or change "
		// +
		// "\nthe preference in the \"Preferences\" menu in the JFLAP main menu.",
		// "Transitions From Final States", JOptionPane.ERROR_MESSAGE);
		// }
		// }
		//
		return true;
	}

	private void handleInput(Object input) {
		Automaton auto = getAutomaton();
		SingleInputSimulator simulator = new SingleInputSimulator(auto, myClosure);
		if (input == null)
			return;

		// Get the initial configurations.
		if (auto instanceof MultiTapeTuringMachine) {
			String[] s = (String[]) input;
			SymbolString[] symbols = new SymbolString[s.length];
			for (int i = 0; i < s.length; i++)
				symbols[i] = Symbolizers.symbolize(s[i], auto);
			handleInteraction(simulator, symbols);
		} else {
			String s = (String) input;
			SymbolString symbol = Symbolizers.symbolize(s, auto);
			handleInteraction(simulator, symbol);
		}
	}

	public void handleInteraction(SingleInputSimulator simulator, SymbolString... symbols) {
		final Color current = JFLAPPreferences.getSelectedStateColor();
		
		SimulatorPanel simpane = new SimulatorPanel(getEditorPanel(),
				simulator, symbols);
		final JFLAPEnvironment env = JFLAPUniverse.getActiveEnvironment();
		env.addSelectedComponent(simpane);
		env.addTabListener(new TabChangeListener() {

			@Override
			public void tabChanged(TabChangedEvent e) {
				if (e.getCurrentView().equals(getView())){
					JFLAPPreferences.setSelectedStateColor(current);
					env.removeTabListener(this);
				}
			}
		});

	}
}
