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
 * Building block that moves Turing machine head (minimum once) either 
 * right or left, as specified by direction, until it reads the specified
 * Symbol.
 * 
 * @author Ian McMahon
 */
public class MoveUntilBlock extends MultiTapeUpdatingBlock {
	private MultiTapeTMTransition myFinalTransition;
	private Symbol mySymbol;
	private TuringMachineMove myMove;
	
	public MoveUntilBlock(TuringMachineMove direction, Symbol read,
			TapeAlphabet alph, int id) {
		super(alph, createName(direction, read), id, direction, read);
		
		
	}
	
	private static String createName(TuringMachineMove direction, Symbol read){
		if(direction.equals(TuringMachineMove.STAY)) throw new RuntimeException("Infinite loops are fun, but not allowed");
		String move = BlockLibrary.MOVE + BlockLibrary.UNDSCR +
				direction.char_abbr + BlockLibrary.UNDSCR + 
				BlockLibrary.UNTIL + BlockLibrary.UNDSCR + read;
		return move;
	}

	@Override
	public void updateTuringMachine(TapeAlphabet tape) {
		MultiTapeTuringMachine tm = getTuringMachine();
		StateSet states = tm.getStates();
		
		TransitionSet<MultiTapeTMTransition> transitions = tm.getTransitions();
		transitions.clear();
		transitions.add(myFinalTransition);
		
		State start = tm.getStartState();
		State intermediate = states.getStateWithID(2);
		
		for(Symbol term : tape){
			transitions.add(new MultiTapeTMTransition(start, intermediate, term, term, myMove));
			
			if(!term.equals(mySymbol)){
				transitions.add(new MultiTapeTMTransition(intermediate, intermediate, term, term, myMove));
			}
		}
	}

	@Override
	public void constructFromBase(TapeAlphabet parentAlph,
			TuringMachine localTM, Object... args) {
		myMove = (TuringMachineMove) args[0];
		mySymbol = (Symbol) args[1];

		addStartAndFinalStates(localTM);
		
		MultiTapeTuringMachine tm = getTuringMachine();
		StateSet states = tm.getStates();
		
		State intermediateState = states.createAndAddState();
		State finalState = tm.getFinalStateSet().first();
		
		myFinalTransition = new MultiTapeTMTransition(intermediateState, 
				finalState, mySymbol, mySymbol, TuringMachineMove.STAY);
		
		tm.getTransitions().add(myFinalTransition);
	}
}
