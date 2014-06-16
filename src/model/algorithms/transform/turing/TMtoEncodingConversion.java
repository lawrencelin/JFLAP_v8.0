package model.algorithms.transform.turing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import model.algorithms.AlgorithmException;
import model.algorithms.FormalDefinitionAlgorithm;
import model.algorithms.steppable.AlgorithmStep;
import model.automata.State;
import model.automata.StateSet;
import model.automata.TransitionSet;
import model.automata.turing.MultiTapeTMTransition;
import model.automata.turing.MultiTapeTuringMachine;
import model.automata.turing.TapeAlphabet;
import model.automata.turing.TuringMachineMove;
import model.automata.turing.universal.MappingBlock;
import model.symbols.Symbol;
import model.symbols.SymbolString;
import errors.BooleanWrapper;

/**
 * Maps a single-tape, single-final state Turing machine to unary encoding 
 * to be simulated in a Universal TM. Start state is mapped to 1, final to 11,
 * all others are 111+ based on ID number. Final encoding is based off 
 * transitions as explained in <CODE>convertTM</CODE>.
 * @author Ian McMahon
 *
 */
public class TMtoEncodingConversion extends
		FormalDefinitionAlgorithm<MultiTapeTuringMachine> {
	private Symbol one, zero;
	
	private Map<TuringMachineMove, SymbolString> moveMap ;
	private Map<Symbol, SymbolString> alphabetMap;
	private Map<State, SymbolString> stateMap;
	private SymbolString myEncoding;
	private TransitionSet<MultiTapeTMTransition> transitionCopy;

	public TMtoEncodingConversion(MultiTapeTuringMachine fd) {
		super(fd);
	}

	@Override
	public String getDescriptionName() {
		return "TM to Unary Encoding";
	}

	@Override
	public String getDescription() {
		return "Conversion algorithm from Single-tape TM to Universal TM unary encoding";
	}

	@Override
	public BooleanWrapper[] checkOfProperForm(MultiTapeTuringMachine fd) {
		List<BooleanWrapper> bool = new ArrayList<BooleanWrapper>();
		if (fd.getNumTapes() != 1)
			bool.add(new BooleanWrapper(
					false,
					"The "
							+ "Turing machine must be single tape to be converted into a "
							+ "Universal TM encoding. "));
		if (fd.getFinalStateSet().size() > 1)
			bool.add(new BooleanWrapper(false,
					"The Turing Machine must have a single final state"));
		return bool.toArray(new BooleanWrapper[0]);
	}

	@Override
	public AlgorithmStep[] initializeAllSteps() {

		return new AlgorithmStep[] { new AlphabetConversionStep(),
				new StateConversionStep(), new TransitionConversionStep() };
	}

	@Override
	public boolean reset() throws AlgorithmException {
		constructMoveMap();
		alphabetMap = new TreeMap<Symbol, SymbolString>();
		stateMap = new TreeMap<State, SymbolString>();
		myEncoding = new SymbolString();
		transitionCopy = getOriginalDefinition().getTransitions().copy();
		return true;
	}

	public SymbolString getEncoding() {

		return myEncoding;
	}

	private class AlphabetConversionStep implements AlgorithmStep {

		@Override
		public String getDescriptionName() {
			return "Alphabet conversion";
		}

		@Override
		public String getDescription() {
			return "Convert an alphabet to its unary encoding";
		}

		@Override
		public boolean execute() throws AlgorithmException {
			return makeAlphabetMap();
		}

		@Override
		public boolean isComplete() {
			return alphabetMap.size() == getOriginalDefinition()
					.getTapeAlphabet().size();
		}

	}

	/**
	 * Unary maps blank symbol to 1, and the remainder of the TapeAlphabet to 11+ 
	 * in natural ascending order.
	 */
	private boolean makeAlphabetMap() {
		TapeAlphabet alph = getOriginalDefinition().getTapeAlphabet();
		
		alphabetMap = MappingBlock.createMap(alph);
		return true;
	}
	
	private class StateConversionStep implements AlgorithmStep{

		@Override
		public String getDescriptionName() {
			return "State Conversion Step";
		}

		@Override
		public String getDescription() {
			return null;
		}

		@Override
		public boolean execute() throws AlgorithmException {
			return convertStates();
		}

		@Override
		public boolean isComplete() {
			return stateMap.size() > 0;
		}
	}
	
	/**
	 * As explained in class description, states are mapped to unary encoding
	 * starting at 1. The start state is always 1, final state is always 11.
	 */
	private boolean convertStates(){
		stateMap.clear();
		
		MultiTapeTuringMachine tm = getOriginalDefinition();
		StateSet stateCopy = tm.getStates().copy();

		State start = tm.getStartState();
		State finalState = tm.getFinalStateSet().first();
		
		stateCopy.remove(finalState);
		stateCopy.remove(start);
		
		State[] states = stateCopy.toArray(new State[0]);
		
		for(int i=0; i<states.length;i++){
			SymbolString ones = new SymbolString();
			
			for(int j=0; j<i+3; j++){
				ones.add(one);
			}
			stateMap.put(states[i], ones);
		}
		stateMap.put(start, new SymbolString(one));
		stateMap.put(finalState, new SymbolString(one,one));
		return true;
	}
	
	private class TransitionConversionStep implements AlgorithmStep{

		@Override
		public String getDescriptionName() {
			return "Transition Conversion Step";
		}

		@Override
		public String getDescription() {
			return null;
		}

		@Override
		public boolean execute() throws AlgorithmException {
			return convertTM();
		}

		@Override
		public boolean isComplete() {
			return transitionCopy.isEmpty();
		}
		
	}
	
	/**
	 * Using unary encodings of the States, Symbols, and TuringMachineMoves (from earlier steps),
	 * encodes each transition to the unary mapping form: 
	 * <i>from 0 read 0 to 0 write 0 move 0 </i>
	 * and appends it to a single SymbolString until all transitions are converted.
	 */
	private boolean convertTM(){
		MultiTapeTuringMachine tm = getOriginalDefinition();
		
		MultiTapeTMTransition tran = transitionCopy.first();
		State from = tran.getFromState(), to = tran.getToState();
		Symbol read = tran.getRead(0), write = tran.getWrite(0);
		TuringMachineMove move = tran.getMove(0);
		
		SymbolString tranEncoding = new SymbolString();
		
		tranEncoding.concat(stateMap.get(from));
		tranEncoding.add(zero);
		
		tranEncoding.concat(alphabetMap.get(read));
		tranEncoding.add(zero);
		
		tranEncoding.concat(stateMap.get(to));
		tranEncoding.add(zero);
		
		tranEncoding.concat(alphabetMap.get(write));
		tranEncoding.add(zero);
		
		tranEncoding.concat(moveMap.get(move));
		tranEncoding.add(zero);
		
		myEncoding.concat(tranEncoding);
		transitionCopy.remove(tran);
		
		return true;
	}
	
	/**
	 * Maps (unary) the moves left to 1, stay to 11, and right to 111
	 */
	private void constructMoveMap(){
		one = new Symbol("1");
		zero = new Symbol("0");
		moveMap = new HashMap<TuringMachineMove, SymbolString>();
		
		moveMap.put(TuringMachineMove.LEFT, new SymbolString(one));
		moveMap.put(TuringMachineMove.STAY, new SymbolString(one,one));
		moveMap.put(TuringMachineMove.RIGHT, new SymbolString(one,one,one));
	}
}
