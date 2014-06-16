package view.automata.tools.algorithm;

import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;

import debug.JFLAPDebug;
import model.algorithms.transform.fsa.AddTrapStateAlgorithm;
import model.automata.State;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.symbols.Symbol;
import model.symbols.SymbolString;
import model.symbols.symbolizer.Symbolizers;
import universe.JFLAPUniverse;
import view.automata.editing.AutomatonEditorPanel;
import view.automata.tools.TransitionTool;
import view.environment.JFLAPEnvironment;

public class TrapTransitionTool extends TransitionTool<FiniteStateAcceptor, FSATransition> {

	private AddTrapStateAlgorithm myAlg;

	public TrapTransitionTool(
			AutomatonEditorPanel<FiniteStateAcceptor, FSATransition> panel, AddTrapStateAlgorithm alg) {
		super(panel);
		myAlg = alg;
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		JFLAPEnvironment env = JFLAPUniverse.getActiveEnvironment();
		
		if(hasFrom()){
			createTransition(e, env);
			clear();
		}
	}

	public void createTransition(MouseEvent e, JFLAPEnvironment env) {
		AutomatonEditorPanel<FiniteStateAcceptor, FSATransition> panel = getPanel();
		FiniteStateAcceptor dfa = myAlg.getDFAWithTrapState();
		Object obj = panel.objectAtPoint(e.getPoint());
		
		if (obj instanceof State) {
			FSATransition trans = createTransition(panel, obj);
			clear();
			
			if(!myAlg.hasTrapState() || !myAlg.isRunning()){
				showError("That action is inappropriate for this step!", "Out of Order");
				return;
			}
			
			if(!myAlg.isTrapState(trans.getToState())){
				showError("You have to make transition to the trap state!", "Error");
				return;
			}
			
			String terminal = JOptionPane.showInputDialog(env, "Transition on what terminal?");
			if (terminal == null) return;
			
			SymbolString symbols = Symbolizers.symbolize(terminal, dfa);
			if (symbols.size() != 1){
				showError("Terminal can only be a single letter", "Error");
				return;
			}
			Symbol read = symbols.get(0);
			
			if(!dfa.getAllSymbolsInAlphabets().contains(read)){
				showError("Terminal "+read+" is not in the DFA's Alphabet", "Incorrect input");
				return;
			}
			
			boolean added = myAlg.addTransition(trans.getFromState(), read);
			if(!added)
				showError("There is already a transition using Terminal "+terminal+" from this state", "Incorrect input");
		}
	}
	
	private void showError(String message, String title){
		JOptionPane.showMessageDialog(JFLAPUniverse.getActiveEnvironment(), message, title, JOptionPane.ERROR_MESSAGE);
	}
}
