package model.automata.turing.buildingblock.library;

import model.automata.State;
import model.automata.TransitionSet;
import model.automata.turing.MultiTapeTMTransition;
import model.automata.turing.TapeAlphabet;
import model.automata.turing.TuringMachine;
import model.automata.turing.TuringMachineMove;
import model.symbols.Symbol;

/**
 * Building block used to write a single character in place (Stay) over any other character.
 * @author Ian McMahon
 *
 */
public class WriteBlock extends MultiTapeUpdatingBlock {
	private Symbol myWrite;

	public WriteBlock(Symbol write, TapeAlphabet alph, int id) {
		super(alph, BlockLibrary.WRITE + BlockLibrary.UNDSCR +write, id, write);
		
	}

	@Override
	public void updateTuringMachine(TapeAlphabet tape) {
		TransitionSet<MultiTapeTMTransition> transitions = getTuringMachine().getTransitions();
		transitions.clear();

		State start = getTuringMachine().getStartState();
		State finish = getTuringMachine().getFinalStateSet().first();
		
		for(Symbol term : tape){
			transitions.add(new MultiTapeTMTransition(start, finish, term, myWrite, TuringMachineMove.STAY));
		}

	}

	@Override
	public void constructFromBase(TapeAlphabet parentAlph,
			TuringMachine localTM, Object... args) {
		myWrite = (Symbol) args[0];
		
		addStartAndFinalStates(getTuringMachine());
		
		
	}

}
