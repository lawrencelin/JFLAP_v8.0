package view.automata.transitiontable;

import model.automata.Automaton;
import model.automata.Transition;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.automata.acceptors.pda.PDATransition;
import model.automata.acceptors.pda.PushdownAutomaton;
import model.automata.transducers.mealy.MealyMachine;
import model.automata.transducers.moore.MooreMachine;
import model.automata.turing.MultiTapeTMTransition;
import model.automata.turing.MultiTapeTuringMachine;
import model.automata.turing.buildingblock.BlockTransition;
import model.automata.turing.buildingblock.BlockTuringMachine;
import view.automata.editing.AutomatonEditorPanel;

public class TransitionTableFactory {

	public static TransitionTable createTable(Transition trans, Automaton automaton, AutomatonEditorPanel panel){
		if(automaton instanceof FiniteStateAcceptor)
			return new FSATransitionTable<FiniteStateAcceptor>((FSATransition) trans, (FiniteStateAcceptor) automaton, panel);
		if(automaton instanceof PushdownAutomaton)
			return new PDATransitionTable((PDATransition) trans, (PushdownAutomaton) automaton, panel);
		if(automaton instanceof MultiTapeTuringMachine)
			return new MultiTapeTMTransitionTable((MultiTapeTMTransition) trans, (MultiTapeTuringMachine) automaton, panel);
		if(automaton instanceof MooreMachine)
			return new FSATransitionTable<MooreMachine>((FSATransition) trans, (MooreMachine) automaton, panel);
		if(automaton instanceof MealyMachine)
			return new MealyTransitionTable((FSATransition) trans, (MealyMachine) automaton, panel);
		if(automaton instanceof BlockTuringMachine)
			return new BlockTMTransitionTable((BlockTransition) trans, (BlockTuringMachine) automaton,panel);
		return null;
	}
}
