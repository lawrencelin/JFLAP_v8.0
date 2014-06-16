package model.automata.turing.buildingblock.library;

import model.automata.InputAlphabet;
import model.automata.StartState;
import model.automata.State;
import model.automata.TransitionSet;
import model.automata.acceptors.FinalStateSet;
import model.automata.turing.BlankSymbol;
import model.automata.turing.TapeAlphabet;
import model.automata.turing.buildingblock.Block;
import model.automata.turing.buildingblock.BlockSet;
import model.automata.turing.buildingblock.BlockTransition;
import model.automata.turing.buildingblock.BlockTuringMachine;
import model.automata.turing.buildingblock.UpdatingBlock;
import model.symbols.Symbol;

public abstract class BlockTMUpdatingBlock extends UpdatingBlock{

	
	
	public BlockTMUpdatingBlock(TapeAlphabet parentAlph,
			String name, int id, Object ... args) {
		super(createTuringMachine(parentAlph), parentAlph, name, id, args);
	}

	
	private static BlockTuringMachine createTuringMachine(TapeAlphabet alph) {

		BlockTuringMachine tm = new BlockTuringMachine(new BlockSet(), 
				alph.copy(),
				new BlankSymbol(), 
				new InputAlphabet(), 
				new TransitionSet<BlockTransition>(),
				new StartState(), 
				new FinalStateSet());
		return tm;
	}
	
	@Override
	public BlockTuringMachine getTuringMachine() {
		return (BlockTuringMachine) super.getTuringMachine();
	}
	
	@Override
	public void updateTuringMachine(TapeAlphabet tape) {
		BlockSet blocks = getTuringMachine().getStates();
		
		for(State block : blocks){
			if (block instanceof UpdatingBlock)
				((UpdatingBlock) block).updateTuringMachine(tape);
		}
	}
	
	public void addTransition(Block from, Block to, Symbol input){
		TransitionSet<BlockTransition> trans = this.getTuringMachine().getTransitions();
		trans.add(new BlockTransition(from, to, input));
	}
	
}
