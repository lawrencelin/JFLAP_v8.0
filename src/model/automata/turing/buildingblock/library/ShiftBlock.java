package model.automata.turing.buildingblock.library;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import model.automata.turing.TapeAlphabet;
import model.automata.turing.TuringMachine;
import model.automata.turing.TuringMachineMove;
import model.automata.turing.buildingblock.Block;
import model.automata.turing.buildingblock.BlockSet;
import model.automata.turing.buildingblock.BlockTuringMachine;
import model.symbols.Symbol;

/**
 * Block used to delete character currently under read head 
 * and shift entire tape either right or left (specified by shift)
 * 
 * @author Julian, Ian McMahon
 *
 */
public class ShiftBlock extends BlockTMUpdatingBlock {

	private StartBlock myStart;
	private Map<Symbol, Block> mySubBlocks;
	private TuringMachineMove myShift;

	public ShiftBlock(TuringMachineMove shift, 
						TapeAlphabet tape, 
						int id) {
		super(tape, BlockLibrary.SHIFT + BlockLibrary.UNDSCR+shift.char_abbr, id, shift);
	}
	

	@Override
	public void updateTuringMachine(TapeAlphabet tape) {
		BlockTuringMachine tm = getTuringMachine();
		BlockSet blocks = tm.getStates();
		Set<Symbol> symbols = new TreeSet<Symbol>(tape);
		
		removeSubblocks(blocks, symbols);
		
		for (Symbol s: symbols){
			Block block = new SingleShiftBlock(s, myShift, tape, blocks.getNextUnusedID());
			mySubBlocks.put(s, block);
			addTransition(myStart, block, new Symbol(TILDE));
			tm.getFinalStateSet().add(block);
		}
	}


	private void removeSubblocks(BlockSet blocks, Set<Symbol> symbols) {
		for (Symbol sym: mySubBlocks.keySet().toArray(new Symbol[0])){
			if (symbols.contains(sym))
				symbols.remove(sym);
			else{
				blocks.remove(mySubBlocks.get(sym));
				mySubBlocks.remove(sym);
			}
		}
	}


	@Override
	public void constructFromBase(TapeAlphabet parentAlph,
			TuringMachine localTM, Object... args) {
		mySubBlocks = new TreeMap<Symbol, Block>();
		myShift = (TuringMachineMove) args[0];
		myStart = new StartBlock(0);
		this.getTuringMachine().setStartState(myStart);		
	}


}
