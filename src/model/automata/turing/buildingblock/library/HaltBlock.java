package model.automata.turing.buildingblock.library;

/**
 * Single state building block used to signify 
 * a halt/accept in a Block Turing machine.
 * @author Ian McMahon
 *
 */
public class HaltBlock extends StartHaltBlock {

	public HaltBlock(int id) {
		super(BlockLibrary.FINAL, id);
		
	}

}
