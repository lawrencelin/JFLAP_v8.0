package model.automata.turing.buildingblock.library;

import debug.JFLAPDebug;
import universe.preferences.JFLAPPreferences;
import model.automata.State;
import model.automata.turing.TapeAlphabet;
import model.automata.turing.TuringMachine;
import model.automata.turing.buildingblock.Block;
import model.automata.turing.buildingblock.BlockSet;
import model.automata.turing.buildingblock.BlockTuringMachine;
import model.automata.turing.buildingblock.UpdatingBlock;

public class EmptyBlockTMUpdatingBlock extends UpdatingBlock {

	private TapeAlphabet myParentAlph;
	private Object[] myArg;

	public EmptyBlockTMUpdatingBlock(BlockTuringMachine tm,
			TapeAlphabet parentAlph, String name, int id, Object[] args) {
		super(tm, parentAlph, name, id, args);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void constructFromBase(TapeAlphabet parentAlph,
			TuringMachine localTM, Object... args) {
		myParentAlph = parentAlph;
		myArg = args;
	}

	@Override
	public void updateTuringMachine(TapeAlphabet tape) {
		BlockSet blocks = (BlockSet) getTuringMachine().getStates();
		for (State block : blocks) {
			if (block instanceof UpdatingBlock){
				((UpdatingBlock) block).updateTuringMachine(tape);
			}
		}
	}
	
	@Override
	public Block copy() {
		return new EmptyBlockTMUpdatingBlock((BlockTuringMachine) getTuringMachine().copy(), myParentAlph, getName(), getID(), myArg);
	}
	
	@Override
	public Block copy(int newID) {
		String name = getName();
		if(name.equals(JFLAPPreferences.getDefaultStateNameBase()+getID()))
			name = JFLAPPreferences.getDefaultStateNameBase() + newID;
		return new EmptyBlockTMUpdatingBlock((BlockTuringMachine) getTuringMachine().copy(), myParentAlph, getName(), newID, myArg);
	}

}
