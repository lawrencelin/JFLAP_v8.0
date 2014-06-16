package model.automata.turing.universal;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import model.automata.TransitionSet;
import model.automata.turing.BlankSymbol;
import model.automata.turing.TapeAlphabet;
import model.automata.turing.TuringMachine;
import model.automata.turing.TuringMachineMove;
import model.automata.turing.buildingblock.Block;
import model.automata.turing.buildingblock.BlockSet;
import model.automata.turing.buildingblock.BlockTransition;
import model.automata.turing.buildingblock.BlockTuringMachine;
import model.automata.turing.buildingblock.library.HaltBlock;
import model.automata.turing.buildingblock.library.MoveBlock;
import model.automata.turing.buildingblock.library.MoveUntilBlock;
import model.automata.turing.buildingblock.library.ShiftBlock;
import model.automata.turing.buildingblock.library.StartBlock;
import model.automata.turing.buildingblock.library.WriteBlock;
import model.symbols.Symbol;
import model.symbols.SymbolString;

/**
 * Block used to decode the output of a Universal TM into the corresponding
 * Symbol encoding used by the TM being simulated.
 * @author Ian McMahon
 *
 */
public class RetrieveOutputBlock extends MappingBlock{
	
	private Block rightPivot, leftPivot, rightFromState, leftFromState;
	private Symbol tilde, hash, zero, one;
	private Set<Block> loops;
	
	public RetrieveOutputBlock(TapeAlphabet alph, int id) {
		super(alph, "Translate output", id);		
	}

	/**
	 * Initializes blocks whose order is not changed by a change in the TapeAlphabet.
	 */
	private void initTranslates(TapeAlphabet alph) {
		BlockTuringMachine tm = (BlockTuringMachine) getTuringMachine();
		BlockSet blocks = tm.getStates();
		Symbol blank = tm.getBlankSymbol();
		int id = blocks.getNextUnusedID();
		
		Block rightOut, lastBlock = blocks.getStateWithID(id-1);
		
		Block b1 = rightPivot = new MoveUntilBlock(TuringMachineMove.LEFT, blank, alph,id++);
		
		Block b2 = rightOut = new WriteRightBlock(alph,id++);
		addTransition(b1, b2, tilde);
		addTransition(lastBlock, b2, tilde);
		
		b1=b2;
		b2 = leftPivot = new MoveUntilBlock(TuringMachineMove.LEFT, zero, alph,id++);
		
		b1=b2;
		b2 = new WriteRightBlock(alph,id++);
		addTransition(b1, b2, tilde);
		
		b1=b2;
		b2 = leftFromState = new WriteRightBlock(alph,id++);
		addTransition(b1, b2, one);
		
		for(int i=0; i<3;i++){
			b1=b2;
			b2 = new MoveBlock(TuringMachineMove.LEFT, alph, id++);
			addTransition(b1, b2, blank);
		}
		
		b1=b2;
		b2 = new WriteBlock(blank, alph, id++);
		addTransition(b1, leftPivot, one);
		addTransition(b1, b2, hash);
		
		b1=b2;
		b2 = new MoveUntilBlock(TuringMachineMove.RIGHT, hash, alph,id++);
		addTransition(b1, b2, tilde);
		
		b1=b2;
		b2 = new ShiftBlock(TuringMachineMove.LEFT, alph,id++);
		addTransition(b1, b2, tilde);
		
		b1=b2;
		b2 = new HaltBlock(id++);
		addTransition(b1, b2, tilde);
		tm.getFinalStateSet().add(b2);
		
		rightFromState = new WriteRightBlock(alph,id++);
		addTransition(rightOut, rightFromState, one);
		addTransition(rightFromState, leftPivot, hash);
		
		//This block will take the first blank to the right of input and write over
		//anything to the right of it until the marker(ie. get rid of extra encoded blanks)
		b1 = new WriteRightBlock(alph, id++);
		addTransition(rightFromState, b1, zero);
		addTransition(b1, b1, zero);
		addTransition(b1, b1, one);
		addTransition(b1, leftPivot, hash);
	}

	/**
	 * Initializes the tape so that the right end is marked with a hash (with the farmost 0 deleted), the
	 * left end is marked by a hash (followed by a zero and a unary encoded blank), and the
	 * head is at the position the Universal TM halted at.
	 */
	private void initMarkers(TapeAlphabet alph, BlockTuringMachine tm, TransitionSet<BlockTransition> transitions) {
		int id = 0;
		Symbol blank = tm.getBlankSymbol();
		
		Block b1 = new StartBlock(id++);
		tm.setStartState(b1);
		Block b2 = new WriteBlock(hash, alph,id++);
		addTransition(b1, b2, tilde);
		
		b1=b2;
		b2 = new MoveUntilBlock(TuringMachineMove.RIGHT, blank, alph,id++);
		addTransition(b1, b2, tilde);
		
		b1=b2;
		b2 = new MoveBlock(TuringMachineMove.LEFT, alph, id++);
		addTransition(b1, b2, tilde);
		
		b1=b2; 
		b2 = new WriteBlock(hash, alph,id++);
		addTransition(b1, b2, tilde);
		
		b1=b2;
		b2 = new MoveUntilBlock(TuringMachineMove.LEFT, blank, alph,id++);
		addTransition(b1, b2, tilde);
		
		b1=b2;
		b2 = new WriteBlock(zero, alph,id++);
		addTransition(b1, b2, tilde);
		
		b1=b2;
		b2 = new MoveBlock(TuringMachineMove.LEFT, alph, id++);
		addTransition(b1, b2, tilde);
		
		b1=b2;
		b2 = new WriteBlock(hash, alph, id++);
		addTransition(b1, b2, tilde);
		
		b1=b2;
		b2 = new MoveUntilBlock(TuringMachineMove.RIGHT, hash, alph,id++);
		addTransition(b1, b2, tilde);
		
		b1=b2;
		b2 = new WriteBlock(one, alph,id++);
		addTransition(b1, b2, tilde);
		
		b1=b2;
		b2 = new MoveUntilBlock(TuringMachineMove.LEFT, zero, alph, id);
		addTransition(b1, b2, tilde);
	}

	@Override
	public void updateTuringMachine(Map<Symbol, SymbolString> encodingMap) {
		BlockTuringMachine tm = (BlockTuringMachine) getTuringMachine();
		BlockSet blocks = tm.getStates();
				
		for(Block state : loops){
			blocks.remove(state);
		}
		
		loops.clear();
		translateBothSides(encodingMap);
	}

	/**
	 * Creates blocks for both right and left side (in respect to the position the
	 * Universal TM halted at) which delete unary encodings and insert the corresponding
	 * Symbol at the correct position at the right of the tape. Deletes and remakes
	 * blocks if the TapeAlphabet changes.
	 */
	private void translateBothSides(
			Map<Symbol, SymbolString> encodingMap) {
		BlockTuringMachine tm = getTuringMachine();
		BlockSet blocks = tm.getStates();
		TapeAlphabet tape = tm.getTapeAlphabet();
		
		Symbol blank = new BlankSymbol().getSymbol();
		
		Block rightBlock1, rightBlock2 = rightFromState;
		Block leftBlock1, leftBlock2 = leftFromState;
		int id = blocks.getNextUnusedID();
		
		for(int i=2; i<=encodingMap.size();i++){
			rightBlock1=rightBlock2; 
			rightBlock2 = new WriteRightBlock(tape,id++);
			addTransition(rightBlock1, rightBlock2, one);
			
			leftBlock1=leftBlock2;
			leftBlock2 = new WriteRightBlock(tape,id++);
			addTransition(leftBlock1, leftBlock2, one);
			
			Symbol a = getKeyForValue(encodingMap, i);
			
			Block writeOverZero = new WriteBlock(blank, tape, id++);
			addTransition(rightBlock2, writeOverZero, zero);
			
			Block replaceZero = new ReplaceBlock(TuringMachineMove.RIGHT, a, tape,id++);
			addTransition(writeOverZero, replaceZero, tilde);
			
			Block ifHash = new MoveUntilBlock(TuringMachineMove.RIGHT, blank, tape, id++);
			addTransition(rightBlock2, ifHash, hash);
			
			Block replaceHash = new WriteBlock(a, tape, id++);
			addTransition(ifHash, replaceHash, tilde);
			addTransition(replaceHash, leftPivot, tilde);
			
			Block replaceLeft = new ReplaceBlock(TuringMachineMove.LEFT, a, tape,id++);
			addTransition(leftBlock2, replaceLeft, blank);
			
			addTransition(replaceZero, rightPivot, tilde);
			addTransition(replaceLeft, leftPivot, tilde);
			
			loops.add(writeOverZero);
			loops.add(replaceZero);
			loops.add(ifHash);
			loops.add(replaceHash);
			loops.add(rightBlock2);
			loops.add(replaceLeft);
			loops.add(leftBlock2);
		}

	}

	/**
	 * Helper method used to retrieve the Symbol mapped to the unary encoding of length
	 * <i>oneLength</i>
	 */
	private Symbol getKeyForValue(Map<Symbol, SymbolString> encodingMap, int oneLength){
		for(Symbol s : encodingMap.keySet()){
			if(encodingMap.get(s).size()==oneLength)
				return s;
		}
		return null;
	}

	@Override
	public void constructFromBase(TapeAlphabet parentAlph,
			TuringMachine localTM, Object... args) {

		loops = new TreeSet<Block>();
		tilde = new Symbol(TILDE);
		hash = new Symbol(TM_MARKER);
		zero = new Symbol("0");
		one = new Symbol("1");
		
		BlockTuringMachine tm = getTuringMachine();
		TapeAlphabet tape = tm.getTapeAlphabet();
		TransitionSet<BlockTransition> transitions = tm.getTransitions();
		
		initMarkers(tape, tm, transitions);
		initTranslates(tape);
		updateTuringMachine(tape);
		
	}

}
