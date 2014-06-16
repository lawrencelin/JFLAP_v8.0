package model.automata.turing.buildingblock.library;

import model.automata.turing.BlankSymbol;
import model.automata.turing.TapeAlphabet;
import model.automata.turing.TuringMachine;
import model.automata.turing.TuringMachineMove;
import model.automata.turing.buildingblock.Block;
import model.automata.turing.buildingblock.BlockTuringMachine;
import model.symbols.Symbol;

/**
 * Block used in CopyBlock which duplicates a single Symbol at 
 * the next blank space to the right.
 * @author Ian McMahon
 *
 */
public class DuplicateCharBlock extends BlockTMUpdatingBlock {

	private Symbol mySymbol;

	public DuplicateCharBlock(TapeAlphabet alph, int id, Symbol character) {
		super(alph, BlockLibrary.DUPLICATE+ BlockLibrary.UNDSCR + character, id, character);
	}

	@Override
	public void constructFromBase(TapeAlphabet alph,
			TuringMachine localTM, Object... args) {
		
		mySymbol = (Symbol) args[0];
		BlockTuringMachine tm = (BlockTuringMachine) getTuringMachine();
		TapeAlphabet tapeAlph = tm.getTapeAlphabet();
		
		int id = 0;
		Symbol tilde = new Symbol(TILDE);
		Symbol blank = new BlankSymbol().getSymbol();
		
		Block b1 = new StartBlock(id++);
		tm.setStartState(b1);
		Block b2 = new WriteBlock(blank, alph, id++);
		addTransition(b1, b2, tilde);
		
		b1=b2;
		b2 = new MoveUntilBlock(TuringMachineMove.RIGHT, blank, tapeAlph, id++);
		addTransition(b1, b2, tilde);
		
		b1=b2;
		b2 = new WriteBlock(mySymbol, alph,  id++);
		addTransition(b1, b2, tilde);
		
		b1=b2;
		b2 = new MoveUntilBlock(TuringMachineMove.LEFT, blank, tapeAlph,  id++);
		addTransition(b1, b2, tilde);
		
		b1=b2;
		b2 = new WriteBlock(mySymbol, alph,  id++);
		addTransition(b1, b2, tilde);
		
		b1=b2;
		b2 = new HaltBlock(id++);
		addTransition(b1,b2, tilde);
		tm.getFinalStateSet().add(b2);
	}

}
