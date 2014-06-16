package model.automata.turing.universal;

import model.automata.turing.TapeAlphabet;
import model.automata.turing.TuringMachine;
import model.automata.turing.TuringMachineMove;
import model.automata.turing.buildingblock.Block;
import model.automata.turing.buildingblock.BlockTuringMachine;
import model.automata.turing.buildingblock.library.BlockTMUpdatingBlock;
import model.automata.turing.buildingblock.library.HaltBlock;
import model.automata.turing.buildingblock.library.MoveBlock;
import model.automata.turing.buildingblock.library.StartBlock;
import model.automata.turing.buildingblock.library.WriteBlock;
import model.symbols.Symbol;

/**
 * Sub-block used by RetrieveOutputBlock which writes a blank over the current Symbol
 * and moves once to the right.
 * 
 * @author Ian McMahon
 *
 */
public class WriteRightBlock extends BlockTMUpdatingBlock {

	public WriteRightBlock(TapeAlphabet tape, int id) {
		super(tape, "Translate", id);
	}

	@Override
	public void constructFromBase(TapeAlphabet parentAlph,
			TuringMachine localTM, Object... args) {
		
		BlockTuringMachine tm = getTuringMachine();
		TapeAlphabet alph = tm.getTapeAlphabet();
		Symbol blank = tm.getBlankSymbol();

		int id=0;
		Symbol tilde = new Symbol(TILDE);
		
		Block b1 = new StartBlock(id++);
		tm.setStartState(b1);
		
		Block b2 = new WriteBlock(blank, alph, id++);
		addTransition(b1,b2,tilde);
		
		b1=b2;
		b2 = new MoveBlock(TuringMachineMove.RIGHT, alph, id++);
		addTransition(b1, b2, tilde);
		
		b1=b2;
		b2 = new HaltBlock(id++);
		addTransition(b1, b2, tilde);
		
		tm.getFinalStateSet().add(b2);
	}

}
