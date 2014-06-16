package view.action.newactions;

import model.automata.turing.buildingblock.BlockTuringMachine;

public class NewBlockTMAction extends NewFormalDefinitionAction<BlockTuringMachine>{

	public NewBlockTMAction() {
		super("Block Turing Machine");
	}

	@Override
	public BlockTuringMachine createDefinition() {
		return new BlockTuringMachine();
	}

}
