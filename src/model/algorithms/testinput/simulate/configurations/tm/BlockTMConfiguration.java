package model.algorithms.testinput.simulate.configurations.tm;

import java.util.Arrays;
import java.util.List;

import oldnewstuff.main.JFLAP;


import debug.JFLAPDebug;

import universe.preferences.JFLAPPreferences;
import util.JFLAPConstants;
import model.algorithms.testinput.simulate.AutoSimulator;
import model.algorithms.testinput.simulate.Configuration;
import model.algorithms.testinput.simulate.ConfigurationChain;
import model.algorithms.testinput.simulate.ConfigurationFactory;
import model.automata.Automaton;
import model.automata.State;
import model.automata.Transition;
import model.automata.turing.MultiTapeTuringMachine;
import model.automata.turing.TuringMachine;
import model.automata.turing.buildingblock.Block;
import model.automata.turing.buildingblock.BlockTransition;
import model.automata.turing.buildingblock.BlockTuringMachine;
import model.automata.turing.universal.UniversalTuringMachine;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public class BlockTMConfiguration extends TMConfiguration<BlockTuringMachine, BlockTransition> {

	private SymbolString myUpdatedTape;
	private int myUpdatedIndex;

	public BlockTMConfiguration(BlockTuringMachine tm, State s, int pos,
			SymbolString tape) {
		super(tm, s, new int[]{pos}, tape);
	}

	@Override
	protected Configuration<BlockTuringMachine, BlockTransition> createConfig(
			BlockTuringMachine a, State s, int ppos, SymbolString primary,
			int[] positions, SymbolString[] updatedClones) throws Exception {
		return new BlockTMConfiguration(a, s, positions[0], updatedClones[0]);
	}

	@Override
	protected boolean canMoveAlongTransition(BlockTransition trans) {
		Symbol read = this.getReadForTape(0);
		Symbol[] input = trans.getInput();

		if (input[0].getString().equals(JFLAPConstants.NOT) &&
				read.equals(input[1]))
			return false;
		if (input.length == 1 &&
				!input[0].getString().equals(JFLAPConstants.TILDE) &&
				!read.equals(input[0]))
			return false;
		
		TMConfiguration config = applyBlock(trans.getToState());

		if (config == null){
			return false;
		}
		
		myUpdatedTape = config.getStringForIndex(0);
		myUpdatedIndex = config.getPositionForIndex(0);
		return true;
	}

	/**
	 * Applies this block to the configuration
	 * @param toState
	 * @return
	 */
	private TMConfiguration applyBlock(Block toState) {
		TMConfiguration init = createInitialConfig(toState);
		
		AutoSimulator auto = new AutoSimulator(toState.getTuringMachine(),
				getSpecialCase());
		
		auto.beginSimulation(init);
		List<ConfigurationChain> chainList = auto.getFirstAccept();
		if (chainList.isEmpty()) return null;
		
		ConfigurationChain chain = chainList.get(0);
		return (TMConfiguration) chain.getLast();
	}

	private TMConfiguration createInitialConfig(Block toState) {
		TuringMachine tm = toState.getTuringMachine();
		SymbolString input = getStringForIndex(0);
		
		int pos = getPositionForIndex(0);
		if (tm instanceof MultiTapeTuringMachine){
			return ((MultiTapeTuringMachine)tm).createInitalConfig(input, pos);
		}
		else{
			return new BlockTMConfiguration((BlockTuringMachine) tm, 
					tm.getStartState(),
					pos, 
					input);
		}
	}

	@Override
	protected int getNextSecondaryPosition(int i, BlockTransition trans) {
		return myUpdatedIndex;
	}

	@Override
	protected SymbolString[] assembleUpdatedStrings(SymbolString[] clones,
			BlockTransition trans) {
		return new SymbolString[]{myUpdatedTape};
	}

}
