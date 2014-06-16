package model.automata.turing.buildingblock.library;

import java.util.HashSet;
import java.util.Set;

import debug.JFLAPDebug;

import model.automata.TransitionSet;
import model.automata.turing.BlankSymbol;
import model.automata.turing.TapeAlphabet;
import model.automata.turing.TuringMachine;
import model.automata.turing.TuringMachineMove;
import model.automata.turing.buildingblock.Block;
import model.automata.turing.buildingblock.BlockSet;
import model.automata.turing.buildingblock.BlockTransition;
import model.automata.turing.buildingblock.BlockTuringMachine;
import model.symbols.Symbol;

/**
 * Block used to duplicate input from the tape once to the right.
 * 
 * @author Ian McMahon
 * 
 */
public class CopyBlock extends BlockTMUpdatingBlock {

	private Symbol marker, tilde;
	private Block myPivot;
	private Set<Block> myLoops;

	public CopyBlock(TapeAlphabet alph, int id) {
		super(alph, BlockLibrary.COPY, id);
	}

	@Override
	public void updateTuringMachine(TapeAlphabet tape) {
		BlockTuringMachine tm = (BlockTuringMachine) getTuringMachine();
		TapeAlphabet alph = tm.getTapeAlphabet();
		BlockSet blocks = tm.getStates();

		alph.retainAll(tape);
		alph.addAll(tape);
		alph.add(marker);

		removeOldLoops();
		int id = blocks.getNextUnusedID();
		for (Symbol term : alph) {
			if (!term.equals(marker)) {
				DuplicateCharBlock newBlock = new DuplicateCharBlock(alph, id,
						term);
				addTransition(myPivot, newBlock, term);
				addTransition(newBlock, myPivot, tilde);
				myLoops.add(newBlock);
				id = blocks.getNextUnusedID();
			}
		}
	}

	private void removeOldLoops() {
		TransitionSet<BlockTransition> transitions = getTuringMachine()
				.getTransitions();
		BlockSet blocks = getTuringMachine().getStates();

		for (BlockTransition transition : transitions.copy()) {
			Block to = transition.getToState(), from = transition
					.getFromState();

			if (myLoops.contains(to) || myLoops.contains(from)) {
				transitions.remove(transition);
				blocks.remove((myPivot.equals(to) ? from : to));
			}
		}
		myLoops.clear();
	}

	@Override
	public void constructFromBase(TapeAlphabet alph, TuringMachine localTM,
			Object... args) {
		myLoops = new HashSet<Block>();
		Symbol blank = new BlankSymbol().getSymbol();

		BlockTuringMachine tm = (BlockTuringMachine) getTuringMachine();

		TapeAlphabet tapeAlph = tm.getTapeAlphabet();

		int id = 0;
		tilde = new Symbol(TILDE);
		marker = new Symbol(TM_MARKER);

		Block b1 = new StartBlock(id++);
		tm.setStartState(b1);
		Block b2 = new MoveUntilBlock(TuringMachineMove.RIGHT, blank, tapeAlph,
				id++);
		addTransition(b1, b2, tilde);

		b1 = b2;
		b2 = new WriteBlock(marker, tapeAlph, id++);
		addTransition(b1, b2, tilde);

		b1 = b2;
		b2 = new MoveUntilBlock(TuringMachineMove.LEFT, blank, tapeAlph, id++);
		addTransition(b1, b2, tilde);

		b1 = b2;
		b2 = myPivot = new MoveBlock(TuringMachineMove.RIGHT, tapeAlph, id++);
		addTransition(b1, b2, tilde);

		// commented out as Prof. Rodger wants to leave a marker.
		// b1=b2;
		// b2 = new ShiftBlock(TuringMachineMove.LEFT, tapeAlph, id++);
		// addTransition(b1, b2, marker);

		b1 = b2;
		b2 = new MoveUntilBlock(TuringMachineMove.LEFT, blank, tapeAlph, id++);
		addTransition(b1, b2, marker);

		b1 = b2;
		b2 = new MoveBlock(TuringMachineMove.RIGHT, tapeAlph, id++);
		addTransition(b1, b2, tilde);

		b1 = b2;
		b2 = new HaltBlock(id++);
		addTransition(b1, b2, tilde);
		tm.getFinalStateSet().add(b2);
	}

}
