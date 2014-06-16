package model.algorithms.testinput.simulate.configurations.tm;

import java.util.LinkedList;

import universe.preferences.JFLAPPreferences;

import debug.JFLAPDebug;

import model.algorithms.testinput.simulate.Configuration;
import model.automata.State;
import model.automata.turing.MultiTapeTuringMachine;
import model.automata.turing.TuringMachineMove;
import model.automata.turing.MultiTapeTMTransition;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public class MultiTapeTMConfiguration extends TMConfiguration<MultiTapeTuringMachine, MultiTapeTMTransition> {


	public MultiTapeTMConfiguration(MultiTapeTuringMachine tm, State s, int[] pos,
			SymbolString ... tapes) {
		super(tm, s, pos, tapes);
	}


	@Override
	protected Configuration<MultiTapeTuringMachine, MultiTapeTMTransition> createConfig(MultiTapeTuringMachine tm,
			State s, int ppos, SymbolString primary, int[] positions,
			SymbolString[] updatedClones) throws Exception {
		return new MultiTapeTMConfiguration(tm, s, positions, updatedClones);
	}

	@Override
	protected boolean canMoveAlongTransition(MultiTapeTMTransition trans) {
		for (int i = 0; i < super.getNumOfSecondary(); i++){
			if (!getReadForTape(i).equals(trans.getRead(i)))
				return false;
		}
		return true;
	}

	@Override
	protected int getNextSecondaryPosition(int i,
			MultiTapeTMTransition trans) {
		TuringMachineMove move = trans.getMove(i);
		return Math.max(this.getPositionForIndex(i) + move.int_move, 0);
	}

	@Override
	protected SymbolString[] assembleUpdatedStrings(SymbolString[] clones,
			MultiTapeTMTransition trans) {
		for (int i = 0; i < this.getNumOfSecondary(); i++){
			Symbol write = trans.getWrite(i);
			TuringMachineMove move = trans.getMove(i);
			int pos = this.getPositionForIndex(i);
			SymbolString tape = clones[i];
			
			int dPos = updateTape(move, pos, tape);
			
			clones[i] = clones[i].replace(this.getPositionForIndex(i)+dPos, write);
		}
		return clones;
	}



}
