package model.automata.determinism;

import model.automata.Automaton;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.automata.acceptors.pda.PushdownAutomaton;
import model.automata.transducers.Transducer;
import model.automata.turing.MultiTapeTuringMachine;
import model.automata.turing.buildingblock.BlockTuringMachine;


public class DeterminismCheckerFactory {

	public static DeterminismChecker getChecker(Automaton auto){
		if(auto instanceof FiniteStateAcceptor || auto instanceof Transducer)
			return new FSADeterminismChecker();
		if(auto instanceof PushdownAutomaton)
			return new PDADeterminismChecker();
		if(auto instanceof MultiTapeTuringMachine)
			return new MultiTapeTMDeterminismChecker();
		if(auto instanceof BlockTuringMachine)
			return new BlockTMDeterminismChecker();
		return null;
	}
}
