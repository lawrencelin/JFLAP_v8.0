package model.algorithms.testinput.simulate;

import universe.preferences.JFLAPPreferences;
import model.algorithms.testinput.simulate.configurations.FSAConfiguration;
import model.algorithms.testinput.simulate.configurations.MealyConfiguration;
import model.algorithms.testinput.simulate.configurations.MooreConfiguration;
import model.algorithms.testinput.simulate.configurations.PDAConfiguration;
import model.algorithms.testinput.simulate.configurations.tm.BlockTMConfiguration;
import model.algorithms.testinput.simulate.configurations.tm.MultiTapeTMConfiguration;
import model.automata.Automaton;
import model.automata.State;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.automata.acceptors.pda.PushdownAutomaton;
import model.automata.transducers.mealy.MealyMachine;
import model.automata.transducers.moore.MooreMachine;
import model.automata.turing.MultiTapeTuringMachine;
import model.automata.turing.TuringMachine;
import model.automata.turing.buildingblock.BlockTuringMachine;
import model.symbols.Symbol;
import model.symbols.SymbolString;



public class ConfigurationFactory {

	private static final Class<?>[] CHAIN_TYPES = new Class<?>[]{FSAConfiguration.class,
		MealyConfiguration.class,
		MooreConfiguration.class,
		MultiTapeTMConfiguration.class,
		PDAConfiguration.class};

	private static final int MAX_TAPE_SIZE = 5;
	
	public static Configuration createInitialConfiguration(Automaton a, SymbolString...input){
		return createInitialConfiguration(a, a.getStartState(), input);
	}
	

	public static Configuration createInitialConfiguration(Automaton a, State s, SymbolString ... input) {
		
		if (a instanceof FiniteStateAcceptor){
			return new FSAConfiguration((FiniteStateAcceptor) a,s, 0, input[0]);
		}
		else if(a instanceof MooreMachine){
			return new MooreConfiguration((MooreMachine) a, s, 0, input[0], new SymbolString());
		}
		else if(a instanceof MealyMachine){
			return new MealyConfiguration((MealyMachine) a, s, 0, input[0], new SymbolString());
		}
		else if(a instanceof MultiTapeTuringMachine){
			MultiTapeTuringMachine tm = (MultiTapeTuringMachine) a;
			return new MultiTapeTMConfiguration(tm, s,
					createTMPosArray(tm.getNumTapes()), 
					createTMOutput(input));
		}
		else if(a instanceof BlockTuringMachine){
			BlockTuringMachine tm = (BlockTuringMachine) a;
			return new BlockTMConfiguration(tm, s,
					createTMPosArray(1)[0], 
					createTMOutput(input)[0]);
		}
		else if(a instanceof PushdownAutomaton){
			Symbol bos = ((PushdownAutomaton) a).getBottomOfStackSymbol();
			return new PDAConfiguration((PushdownAutomaton) a, s, 0, input[0], new SymbolString(bos));
		}
		else 
			return null;


	}


	private static int[] createTMPosArray(int tapes) {
		int[] positions = new int[tapes];
		for (int i = 0; i < positions.length; i++){
			positions[i] = MAX_TAPE_SIZE;
		}
		return positions;
	}


	private static SymbolString[] createTMOutput(SymbolString[] input) {
		SymbolString[] tapes = new SymbolString[input.length];
		for (int n = 0; n < tapes.length; n++){
			SymbolString tape = new SymbolString();
			tape.concat(createBlankString());
			tape.concat(input[n]);
			tape.concat(createBlankString());

			tapes[n] = tape;
		}
		return tapes;
	}


	private static SymbolString createBlankString() {
		SymbolString tape = new SymbolString();
		for (int n = 0; n < MAX_TAPE_SIZE; n++){
			tape.addLast(JFLAPPreferences.getTMBlankSymbol());
		}
		return tape;
	}

}
