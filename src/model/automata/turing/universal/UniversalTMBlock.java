package model.automata.turing.universal;

import model.automata.turing.buildingblock.Block;

/**
 * Universal TM as a building block.
 * @author Julian
 *
 */
public class UniversalTMBlock extends Block {

	public UniversalTMBlock(int id){
		super(new UniversalTuringMachine(true), "UnivTM" ,id);
	}
}
