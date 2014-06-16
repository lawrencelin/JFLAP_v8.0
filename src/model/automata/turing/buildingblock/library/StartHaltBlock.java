package model.automata.turing.buildingblock.library;

import model.automata.State;
import model.automata.turing.MultiTapeTuringMachine;
import model.automata.turing.buildingblock.Block;

/**
 * Superclass of start and halt building blocks which are single state, no transition
 * Turing machines used to signal starting and halting of a Block Turing Machine.
 * @author Ian McMahon
 *
 */
public class StartHaltBlock extends Block {


	public StartHaltBlock(String name, int id) {
		super(new MultiTapeTuringMachine(1), name, id);
		
		MultiTapeTuringMachine tm = (MultiTapeTuringMachine) getTuringMachine();
		
		State start = tm.getStates().createAndAddState();
		tm.setStartState(start);
		tm.getFinalStateSet().add(start);
		
	}


}
