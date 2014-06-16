package view.automata.tools.algorithm;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import debug.JFLAPDebug;
import model.algorithms.transform.fsa.NFAtoDFAConverter;
import model.automata.State;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.symbols.Symbol;
import view.automata.editing.AutomatonEditorPanel;
import view.automata.tools.TransitionTool;

public class TransitionExpanderTool extends
		TransitionTool<FiniteStateAcceptor, FSATransition> {

	private NFAtoDFAConverter myAlg;

	public TransitionExpanderTool(
			AutomatonEditorPanel<FiniteStateAcceptor, FSATransition> panel,
			NFAtoDFAConverter convert) {
		super(panel);
		myAlg = convert;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (hasFrom()) {
			AutomatonEditorPanel<FiniteStateAcceptor, FSATransition> panel = getPanel();
			Object obj = panel.objectAtPoint(e.getPoint());

			expandFromState(getFrom(), obj, e.getPoint());
			clear();
		}
	}

	private void expandFromState(State from, Object obj, Point p) {
		AutomatonEditorPanel<FiniteStateAcceptor, FSATransition> panel = getPanel();
		String terminal = JOptionPane.showInputDialog(panel,
				"Expand on what terminal?");
		if (terminal == null) return;
		if (terminal.equals("")) {
			JOptionPane.showMessageDialog(panel,
					"One can't have lambda in the DFA!", "Improper terminal",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		State[] expand = myAlg.getExpansionForState(from, new Symbol(terminal));

		if (expand == null || expand.length == 0) {
			JOptionPane.showMessageDialog(panel, "The group " + from.getName()
					+ " does not expand on the terminal " + terminal + "!",
					"Improper expansion", JOptionPane.ERROR_MESSAGE);
			return;
		}
		String userEnd = "";
		State[] userEndStates = expand;
		if (!(obj instanceof State)) {
			userEnd = JOptionPane.showInputDialog(panel,
					"Which group of NFA states will that go to on " + terminal
							+ "?");
			if (userEnd == null)
				return;
			try {
				userEndStates = getStatesForString(userEnd);
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(panel,
						"The list of states is not formatted correctly!",
						"Format error", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}

		if(!containSameStates(expand, userEndStates)){
			JOptionPane.showMessageDialog(panel,
					"That list of states is incorrect!", "Wrong set",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		State to = obj instanceof State ? (State) obj : myAlg.getDFAStateForNFAStates(userEndStates);
		State dfaState = myAlg.getDFAStateForNFAStates(userEndStates);
		
		if(to != dfaState){
			JOptionPane.showMessageDialog(panel, "The group "
					+ from.getName()+" does not go to\n" + "group "
					+ to.getName() + " on terminal " + terminal + "!",
					"Improper transition", JOptionPane.ERROR_MESSAGE);
			return;
		}
		myAlg.expandFromState(from, new Symbol(terminal), userEndStates);
		if(dfaState == null)
			panel.moveState(myAlg.getDFAStateForNFAStates(userEndStates), p);
		
	}

	@Override
	public String getImageURLString() {
		return "/ICON/expand_group.gif";
	}

	@Override
	public String getToolTip() {
		return "Expand Group on Terminal";
	}

	private State[] getStatesForString(String label) {
		StringTokenizer tokenizer = new StringTokenizer(label, " \t\n\r\f,q");
		ArrayList<State> states = new ArrayList<State>();
		FiniteStateAcceptor auto = myAlg.getNFA();

		while (tokenizer.hasMoreTokens()){
			State s = auto.getStates().getStateWithID(
					Integer.parseInt(tokenizer.nextToken()));
			states.add(s);
		}
		states.remove(null);
		return (State[]) states.toArray(new State[0]);
	}

	private boolean containSameStates(State[] states1, State[] states2) {
		int len1 = states1.length;
		int len2 = states2.length;
		if (len1 != len2)
			return false;

		Arrays.sort(states1);
		Arrays.sort(states2);

		for (int k = 0; k < states1.length; k++) {
			if (!states1[k].equals(states2[k]))
				return false;
		}
		return true;
	}
}
