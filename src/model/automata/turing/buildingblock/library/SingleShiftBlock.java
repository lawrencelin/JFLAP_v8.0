package model.automata.turing.buildingblock.library;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import model.automata.AutomatonException;
import model.automata.turing.BlankSymbol;
import model.automata.turing.TapeAlphabet;
import model.automata.turing.TuringMachine;
import model.automata.turing.TuringMachineMove;
import model.automata.turing.buildingblock.Block;
import model.automata.turing.buildingblock.BlockSet;
import model.automata.turing.buildingblock.BlockTuringMachine;
import model.formaldef.components.alphabets.Alphabet;
import model.symbols.Symbol;

/**
 * This block is used as part of the ShiftBlock block. it will only shift if the
 * symbol passed into the constructor is currently being read.
 * 
 * @author Julian
 * 
 */
public class SingleShiftBlock extends BlockTMUpdatingBlock {

	private TuringMachineMove myShift;
	private TuringMachineMove myOpposite;
	private ArrayList<Loop> myLoops;
	private Block myPivot;
	private Symbol myMarker, tilde;

	public SingleShiftBlock(Symbol s, TuringMachineMove direction,
			TapeAlphabet tape, int id) {
		super(tape, BlockLibrary.SHIFT + BlockLibrary.UNDSCR
				+ direction.char_abbr + "_" + s, id, direction, s);
		if (direction == TuringMachineMove.STAY)
			throw new AutomatonException(
					"You may not shift with a stay option.");
	}

	@Override
	public Set<Symbol> getSymbolsUsedForAlphabet(Alphabet a) {
		Set<Symbol> sym = super.getSymbolsUsedForAlphabet(a);
		sym.remove(myMarker);
		return sym;
	}

	@Override
	public void updateTuringMachine(TapeAlphabet tape) {

		BlockTuringMachine tm = (BlockTuringMachine) getTuringMachine();
		TapeAlphabet alph = tm.getTapeAlphabet();
		
		alph.retainAll(tape);
		alph.addAll(tape);
		alph.add(myMarker);

		Set<Symbol> symbols = new TreeSet<Symbol>(tape);
		for (Loop loop : myLoops.toArray(new Loop[0])) {
			if (symbols.contains(loop.symbol))
				symbols.remove(loop.symbol);
			else
				removeLoop(loop);
		}

		symbols.remove(myMarker);
		symbols.remove(tm.getBlankSymbol());

		for (Symbol s : symbols) {
			Loop loop = createLoop(s, alph);
			myLoops.add(loop);
		}
	}

	private void removeLoop(Loop loop) {
		BlockSet blocks = this.getTuringMachine().getStates();
		for (Block b : loop.blocks) {
			blocks.remove(b);
		}
		myLoops.remove(loop);
	}

	private Loop createLoop(Symbol s, TapeAlphabet local) {
		BlockTuringMachine tm = (BlockTuringMachine) getTuringMachine();
		BlockSet blocks = tm.getStates();
		Symbol blank = new BlankSymbol().getSymbol();
		
		int id = blocks.getNextUnusedID();

		Block b1 = new WriteBlock(blank, local, id++);
		Block b2 = new MoveBlock(myShift, local, id++);
		Block b3 = new WriteBlock(s, local, id++);
		Block b4 = new MoveBlock(myOpposite, local, id++);

		addTransition(myPivot, b1, s);
		addTransition(b1, b2, tilde);
		addTransition(b2, b3, tilde);
		addTransition(b3, b4, tilde);
		addTransition(b4, myPivot, tilde);

		return new Loop(s, b1, b2, b3, b4);
	}

	private class Loop {
		private Block[] blocks;
		private Symbol symbol;

		public Loop(Symbol a, Block... blocks) {
			symbol = a;
			this.blocks = blocks;
		}
	}

	@Override
	public void constructFromBase(TapeAlphabet parentAlph,
			TuringMachine localTM, Object... args) {
		BlockTuringMachine tm = (BlockTuringMachine) localTM;

		Symbol blank = new BlankSymbol().getSymbol();
		TapeAlphabet alph = tm.getTapeAlphabet();

		myLoops = new ArrayList<Loop>();
		myMarker = new Symbol(TM_MARKER);
		tilde = new Symbol(TILDE);

		myShift = (TuringMachineMove) args[0];
		myOpposite = myShift == TuringMachineMove.RIGHT ? TuringMachineMove.LEFT
				: TuringMachineMove.RIGHT;
		int id = 0;
		Symbol s = (Symbol) args[1];
		Block b1 = new StartBlock(id++);
		tm.setStartState(b1);
		Block b2 = new MoveBlock(myShift, alph, id++);
		addTransition(b1, b2, tilde);

		b1 = b2;
		b2 = new WriteBlock(myMarker, alph, id++);
		addTransition(b1, b2, s);

		b1 = b2;
		b2 = new MoveBlock(myOpposite, alph, id++);
		addTransition(b1, b2, myMarker);

		b1 = b2;
		b2 = new WriteBlock(blank, alph, id++);
		addTransition(b1, b2, tilde);

		b1 = b2;
		myPivot = new MoveBlock(myOpposite, alph, id++);
		addTransition(b1, myPivot, tilde);

		b1 = myPivot;
		b2 = new MoveUntilBlock(myShift, myMarker, alph, id++);
		addTransition(b1, b2, blank);

		b1 = b2;
		b2 = new WriteBlock(s, alph, id++);
		addTransition(b1, b2, tilde);

		b1 = b2;
		b2 = new MoveBlock(myOpposite, alph, id++);
		addTransition(b1, b2, tilde);

		b1 = b2;
		b2 = new HaltBlock(id++);
		addTransition(b1, b2, tilde);

		tm.getFinalStateSet().add(b2);
	}

}
