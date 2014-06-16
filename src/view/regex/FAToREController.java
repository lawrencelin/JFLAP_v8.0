package view.regex;

import java.awt.Point;
import java.util.Set;

import javax.swing.JOptionPane;

import model.algorithms.conversion.fatoregex.DFAtoRegularExpressionConverter;
import model.automata.State;
import model.automata.Transition;
import model.automata.TransitionSet;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.regex.RegularExpression;
import model.symbols.SymbolString;

import org.omg.CORBA.Environment;

import universe.JFLAPUniverse;
import universe.preferences.JFLAPPreferences;
import view.ViewFactory;
import view.environment.JFLAPEnvironment;

/**
 * This object monitors and guides the user actions in the conversion of an FSA
 * to a regular expression.
 * 
 * @author Thomas Finley
 */

public class FAToREController {

	private FAToREPanel myPanel;
	private DFAtoRegularExpressionConverter myAlg;
	private TransitionWindow transitionWindow;

	/**
	 * Instantiates a new <CODE>FSAToREController</CODE>.
	 * 
	 * @param automaton
	 *            the automaton that is in the process of being converted
	 * @param drawer
	 *            the selection drawer in the editor
	 * @param mainStep
	 *            the label holding the description of the main step
	 * @param detailStep
	 *            the label holding the detail description of whatever the user
	 *            must do now
	 * @param frame
	 *            the window that this is all happening in
	 */
	public FAToREController(FAToREPanel panel) {
		myPanel = panel;
		myAlg = panel.getAlgorithm();

		nextStep();
	}

	/**
	 * This method should be called when the user undertakes an action that is
	 * inappropriate for the current step. This merely displays a small dialog
	 * to the user informing him of this fact, and takes no further action.
	 */
	public void outOfOrder() {
		JOptionPane.showMessageDialog(JFLAPUniverse.getActiveEnvironment(),
				"That action is inappropriate for this step!", "Out of Order",
				JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Moves the converter controller to the next step. This will skip any
	 * unnecessary steps, and set the messages.
	 */
	private void nextStep() {
		if (myAlg.needsFinalTransitions()) {
			myPanel.setMainText("Reform Transitions");
			myPanel.setDetailText("Put " + JFLAPPreferences.getEmptyString()
					+ "-transitions from old final states to new.");
		} else if (!myAlg.hasSingleFinal()) {
			myPanel.setMainText("Make Single Noninitial Final State");
			myPanel.setDetailText("Create a new state to make a single final state.");
		} else if (myAlg.needsTranstitionsCollapsed()) {
			myPanel.setMainText("Reform Transitions");
			myPanel.setDetailText("Use the collapse tool to turn multiple transitions to one."
					+ " "
					+ myAlg.numTransCollapsesNeeded()
					+ " more collapses needed.");
		} else if (myAlg.needsEmptyTransitions()) {
			myPanel.setMainText("Reform Transitions");
			myPanel.setDetailText("Put empty transitions between states with no transitions."
					+ " "
					+ myAlg.numEmptyNeeded()
					+ " more empty transitions needed.");
		} else if (myAlg.needsStatesCollaped()) {
			myPanel.setMainText("Remove States");
			myPanel.setDetailText("Use the collapse state tool to remove nonfinal, noninitial "
					+ "states. "
					+ myAlg.numStateCollapsesNeeded()
					+ " more removals needed.");

			if (transitionWindow != null) {
				transitionWindow.setVisible(false);
				transitionWindow.dispose();
			}
			myPanel.clearSelection();
		} else if (!myAlg.isRunning()) {
			myPanel.setMainText("Generalized Transition Graph Finished!");
			myPanel.setDetailText(myAlg.getResultingRegEx()
					.getExpressionString());
		}

	}

	/**
	 * Creates a state at the specified location. This method is called by an
	 * external state creation tool for the purposes of creating a single final
	 * state only. If this is such an event, the state will be created, made
	 * final, all other final states will be made nonfinal, and selected, and
	 * the machine will move to the next phase.
	 * 
	 * @param point
	 *            the point that the state creation tool was clicked at
	 * @return the state that was created, or <CODE>null</CODE> if it is not the
	 *         time to create a state
	 */
	public State stateCreate() {
		if (myAlg.hasSingleFinal() || myAlg.getSingleFinal() != null) {
			outOfOrder();
			return null;
		}
		FiniteStateAcceptor automaton = myAlg.getGTG();
		Set<State> finals = automaton.getFinalStateSet().toCopiedSet();
		myPanel.clearSelection();

		if (myAlg.createSingleFinalState()) {

			Set<State> newState = automaton.getFinalStateSet().toCopiedSet();
			newState.removeAll(finals);
			myPanel.selectAll(finals.toArray());

			if (newState.size() == 1) {
				nextStep();
				return newState.toArray(new State[0])[0];
			}
		}
		return null;
	}

	/**
	 * Creates a new transition. There are two times when this would be
	 * appropriate: first, when creating the labmda transitions from previously
	 * final states to the new final state, and two, when creating the empty set
	 * transitions between states that do not have transitions between
	 * themselves already. Otherwise, this action should not be undertaken.
	 * These transition creations do not require any user input since in either
	 * case, what must go in the label is clear.
	 * 
	 * @param from
	 *            the from state
	 * @param to
	 *            the to state
	 * @return the newly created transition from <CODE>from</CODE> to
	 *         </CODE>to</CODE>, or <CODE>null</CODE> if a transition is
	 *         inappropriate for this circumstance
	 */
	public void transitionCreate(State from, State to) {
		FiniteStateAcceptor automaton = myAlg.getGTG();
		TransitionSet<FSATransition> transitions = automaton.getTransitions();
		JFLAPEnvironment env = JFLAPUniverse.getActiveEnvironment();

		if (myAlg.needsFinalTransitions()) {
			if (myAlg.getSingleFinal() != to) {
				JOptionPane.showMessageDialog(env,
						"Transitions must go to the new final state!",
						"Bad Destination", JOptionPane.ERROR_MESSAGE);
			} else if (!automaton.getFinalStateSet().contains(from)
					|| from.equals(myAlg.getSingleFinal())) {
				JOptionPane.showMessageDialog(env,
						"Transitions must come from an old final state!",
						"Bad Source", JOptionPane.ERROR_MESSAGE);
			} else {
				myPanel.deselect(from);
				myAlg.addTransitionToFinal(from, to);
				if (myAlg.hasSingleFinal()) {
					nextStep();
				}
			}
		} else if (myAlg.hasSingleFinal() && !myAlg.needsTranstitionsCollapsed() && myAlg.needsEmptyTransitions()) {
			if (!transitions.getTransitionsFromStateToState(from, to).isEmpty()) {
				JOptionPane.showMessageDialog(env,
						"Transitions must go between "
								+ "states with no transitions!",
						"Transition Already Exists", JOptionPane.ERROR_MESSAGE);
			}
			myAlg.addEmptyTransition(from, to);
			nextStep();
		} else
			outOfOrder();
	}

	/**
	 * This takes all the transitions from one state to another, and combines
	 * them into a single transition.
	 */
	public void transitionCollapse(FSATransition trans) {
		if (myAlg.hasSingleFinal() && myAlg.needsTranstitionsCollapsed()) {
			FiniteStateAcceptor automaton = myAlg.getGTG();
			TransitionSet<FSATransition> transitions = automaton
					.getTransitions();
			State from = trans.getFromState(), to = trans.getToState();

			Set<FSATransition> ts = transitions.getTransitionsFromStateToState(
					from, to);
			FSATransition first = ts.toArray(new FSATransition[0])[0];
			if (!myAlg.collapseTransitionsOn(first)) {
				JOptionPane.showMessageDialog(myPanel,
						"Collapse requires 2 or more transitions!",
						"Too Few Transitions", JOptionPane.ERROR_MESSAGE);
			} else
				nextStep();

		} else
			outOfOrder();
	}

	/**
	 * This takes a state, and prepares to remove it. Note that this does not
	 * actually remove the state, but notifies the user of what will appear.
	 * 
	 * @param state
	 *            the state that was selected for removal
	 * @return <CODE>false</CODE> if this state cannot be removed because it is
	 *         initial or final or because this is the wrong time for this
	 *         operation, <CODE>true</CODE> otherwise
	 */
	public boolean stateCollapse(State state) {
		FiniteStateAcceptor automaton = myAlg.getGTG();
		if (!myAlg.isRunning() || !myAlg.hasSingleFinal()
				|| myAlg.needsEmptyTransitions()
				|| myAlg.needsTranstitionsCollapsed()
				|| myAlg.needsFinalTransitions()) {
			outOfOrder();
			return false;
		} else if (automaton.getStartState().equals(state)) {
			JOptionPane.showMessageDialog(myPanel,
					"The initial state cannot be removed!",
					"Initial State Selected", JOptionPane.ERROR_MESSAGE);
			return false;
		} else if (automaton.getFinalStateSet().contains(state)) {
			JOptionPane.showMessageDialog(myPanel,
					"The final state cannot be removed!",
					"Final State Selected", JOptionPane.ERROR_MESSAGE);
			return false;
		} else {
			transitionWindow = new TransitionWindow(state, this);
			transitionWindow.setTransitions(myAlg
					.getTransitionsForCollapseState(state));
			transitionWindow.setVisible(true);

			return true;
		}
	}

	/**
	 * This finalizes a state remove. This will remove whatever state was
	 * selected.
	 */
	public void finalizeStateRemove(State collapseState) {
		if (collapseState == null) {
			JOptionPane.showMessageDialog(myPanel,
					"A valid state has not been selected yet!",
					"No State Selected", JOptionPane.ERROR_MESSAGE);
			return;
		}
		myAlg.collapseState(collapseState);
		nextStep();
		myPanel.clearSelection();

		transitionWindow.setVisible(false);
		transitionWindow.dispose();
	}

	/**
	 * If a transition is selected in the transition window, this method is told
	 * about it.
	 * 
	 * @param transition
	 *            the transition that was selected, or <CODE>null</CODE> if less
	 *            or more than one transition is selected
	 */
	public void tableTransitionSelected(State collapseState,
			FSATransition transition) {
		myPanel.clearSelection();
		if (transition != null && collapseState != null) {
			State from = transition.getFromState();
			State to = transition.getToState();

			FiniteStateAcceptor automaton = myAlg.getGTG();
			TransitionSet<FSATransition> ts = automaton.getTransitions();

			State[] a = new State[] { from, collapseState };
			State[] b = new State[] { from, to };
			State[] c = new State[] { collapseState, collapseState };
			State[] d = new State[] { collapseState, to };
			myPanel.selectAll(a, b, c, d);
		}
	}

	/**
	 * This will automatically perform the actions to move the conversion to the
	 * next step.
	 */
	public void step() {
		JFLAPEnvironment env = JFLAPUniverse.getActiveEnvironment();
		if (!(myAlg.hasSingleFinal() || myAlg.getSingleFinal() != null)) {
			JOptionPane.showMessageDialog(env,
					"Just create a state.\nI believe in you.",
					"Create the State", JOptionPane.ERROR_MESSAGE);
		} else if (myAlg.canStep()) {
			myAlg.step();
			nextStep();
		} else
			JOptionPane.showMessageDialog(env, "You're done.  Go away.",
					"You're Done!", JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * This will export the regular expression.
	 */
	public void export() {
		RegularExpression regex = myAlg.getResultingRegEx();
		JFLAPUniverse.registerEnvironment(ViewFactory.createView(regex.copy()));
	}

}
