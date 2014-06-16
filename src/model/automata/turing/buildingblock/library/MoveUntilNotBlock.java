package model.automata.turing.buildingblock.library;

import model.automata.State;
import model.automata.StateSet;
import model.automata.TransitionSet;
import model.automata.turing.MultiTapeTMTransition;
import model.automata.turing.MultiTapeTuringMachine;
import model.automata.turing.TapeAlphabet;
import model.automata.turing.TuringMachine;
import model.automata.turing.TuringMachineMove;
import model.symbols.Symbol;

/**
 * Building block used to move (minimum one move) either right or left,
 * modifying nothing, until the read head reaches something that is not
 * the specified Symbol.
 * @author Ian McMahon
 */
public class MoveUntilNotBlock extends MultiTapeUpdatingBlock{

	private MultiTapeTMTransition myNotTransition;
	private Symbol mySymbol;
	private TuringMachineMove myDirection;
	
	public MoveUntilNotBlock(TuringMachineMove direction, Symbol read,
			TapeAlphabet alph, int id) {
		super(alph, createName(direction, read), id, read, direction);
		
		
	}
	
	private static String createName(TuringMachineMove direction, Symbol read){
		if(direction.equals(TuringMachineMove.STAY)) throw new RuntimeException("Infinite loops are fun, but not allowed");
		String move = BlockLibrary.MOVE + BlockLibrary.UNDSCR +
				direction.char_abbr + BlockLibrary.UNDSCR + BlockLibrary.UNTIL +
				BlockLibrary.UNDSCR + BlockLibrary.NOT + BlockLibrary.UNDSCR+ read;
		return move;
	}

	@Override
	public void updateTuringMachine(TapeAlphabet tape) {
		MultiTapeTuringMachine tm = getTuringMachine();
		StateSet states = tm.getStates();
		
		TransitionSet<MultiTapeTMTransition> transitions = getTuringMachine().getTransitions();
		transitions.clear();
		transitions.add(myNotTransition);
		
		State start = tm.getStartState();
		State intermediate = states.getStateWithID(2);
		State finish = tm.getFinalStateSet().first();
		
		for(Symbol term : tape){
			transitions.add(new MultiTapeTMTransition(start, intermediate, term, term, myDirection));
			
			if(!term.equals(mySymbol)){
				transitions.add(new MultiTapeTMTransition(intermediate, finish, term, term, TuringMachineMove.STAY));
			}
		}
	}

	@Override
	public void constructFromBase(TapeAlphabet parentAlph,
			TuringMachine localTM, Object... args) {
		mySymbol = (Symbol) args[0];
		myDirection = (TuringMachineMove) args[1];
		addStartAndFinalStates(localTM);
		
		MultiTapeTuringMachine tm = getTuringMachine();
		StateSet states = tm.getStates();
		
		State intermediateState = states.createAndAddState();
		
		myNotTransition = new MultiTapeTMTransition(intermediateState, 
				intermediateState, mySymbol, mySymbol, myDirection);
		
		tm.getTransitions().add(myNotTransition);
	}

}
