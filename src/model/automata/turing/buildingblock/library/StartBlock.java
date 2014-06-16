package model.automata.turing.buildingblock.library;

/**
 * Marks the start of any Block Turing Machine
 * @author Ian McMahon
 *
 */
public class StartBlock extends StartHaltBlock {

	public StartBlock(int id) {
		super(BlockLibrary.START, id);
	}

}
