package model.automata.turing.universal;

import model.automata.turing.BlankSymbol;
import model.automata.turing.TapeAlphabet;
import model.automata.turing.TuringMachine;
import model.automata.turing.TuringMachineMove;
import model.automata.turing.buildingblock.Block;
import model.automata.turing.buildingblock.BlockTuringMachine;
import model.automata.turing.buildingblock.library.BlockLibrary;
import model.automata.turing.buildingblock.library.BlockTMUpdatingBlock;
import model.automata.turing.buildingblock.library.HaltBlock;
import model.automata.turing.buildingblock.library.MoveBlock;
import model.automata.turing.buildingblock.library.MoveUntilBlock;
import model.automata.turing.buildingblock.library.StartBlock;
import model.automata.turing.buildingblock.library.WriteBlock;
import model.symbols.Symbol;
import model.symbols.SymbolString;

/**
 * Subblock used by ConvertInputBlock to replace a Symbol with its unary
 * encoding at the right end of the tape.
 * @author Ian McMahon
 *
 */
public class UnaryEncodingBlock extends BlockTMUpdatingBlock {
	int myLength;
	
	public UnaryEncodingBlock(SymbolString ones, TapeAlphabet alph, int id) {
		super(alph, "Unary"+BlockLibrary.UNDSCR+ones.size(), id, ones);
	}

	@Override
	public void constructFromBase(TapeAlphabet alph,
			TuringMachine localTM, Object... args) {
		SymbolString ones = (SymbolString) args[0];
		myLength = ones.size();
		
		Symbol blank = new BlankSymbol().getSymbol();
		Symbol tilde = new Symbol(TILDE);
		BlockTuringMachine tm = getTuringMachine();
		
		int id = 0;
		
		Block b1 = new StartBlock(id++);
		tm.setStartState(b1);
		Block b2 = new WriteBlock(blank, alph, id++);
		addTransition(b1, b2, tilde);
		
		b1=b2;
		b2 = new MoveUntilBlock(TuringMachineMove.RIGHT, blank, alph, id++);
		addTransition(b1,b2, tilde);
		
		alph.add(new Symbol("1"));
		
		for(int i=0; i<myLength;i++){
			b1=b2;
			b2 = new WriteBlock(new Symbol("1"), alph, id++);
			addTransition(b1, b2, tilde);
			
			b1=b2;
			b2 = new MoveBlock(TuringMachineMove.RIGHT, alph, id++);
			addTransition(b1, b2, tilde);
		}
		
		b1=b2;
		b2 = new WriteBlock(new Symbol("0"), alph, id++);
		addTransition(b1, b2, tilde);
		
		b1=b2;
		b2 = new HaltBlock(id++);
		addTransition(b1, b2, tilde);
		
		tm.getFinalStateSet().add(b2);
	}

}
