package model.automata.turing.buildingblock.library;

import debug.JFLAPDebug;
import model.automata.State;
import model.automata.TransitionSet;
import model.automata.turing.MultiTapeTMTransition;
import model.automata.turing.TapeAlphabet;
import model.automata.turing.TuringMachine;
import model.automata.turing.TuringMachineMove;
import model.symbols.Symbol;

/**
 * Building block that moves the Turing machine head either right or
 * left one move, as specified by direction.
 * @author Ian McMahon
 */
public class MoveBlock extends MultiTapeUpdatingBlock {
	private TuringMachineMove myDirection;
	
	public MoveBlock(TuringMachineMove direction, TapeAlphabet alph,int id) {
		super(alph, createName(direction), id, direction);
	}

	private static String createName(TuringMachineMove direction){
		String move = BlockLibrary.MOVE + BlockLibrary.UNDSCR +direction.char_abbr;
		return move;
	}
	
	@Override
	public void updateTuringMachine(TapeAlphabet tape) {
		TransitionSet<MultiTapeTMTransition> transitions = getTuringMachine().getTransitions();
		transitions.clear();
		State start = getTuringMachine().getStartState();
		State finish = getTuringMachine().getFinalStateSet().first();
		for(Symbol term : tape){
			transitions.add(new MultiTapeTMTransition(start, finish, term, term, myDirection));
		}
	}

	@Override
	public void constructFromBase(TapeAlphabet parentAlph,
			TuringMachine localTM, Object... args) {
		
		myDirection = (TuringMachineMove) args[0];

		addStartAndFinalStates(getTuringMachine());
	}
}
