package model.automata.turing.universal;

import java.util.Set;

import debug.JFLAPDebug;

import model.algorithms.transform.turing.TMtoEncodingConversion;
import model.automata.TransitionSet;
import model.automata.turing.BlankSymbol;
import model.automata.turing.MultiTapeTuringMachine;
import model.automata.turing.TapeAlphabet;
import model.automata.turing.buildingblock.Block;
import model.automata.turing.buildingblock.BlockTransition;
import model.automata.turing.buildingblock.BlockTuringMachine;
import model.automata.turing.buildingblock.library.HaltBlock;
import model.automata.turing.buildingblock.library.StartBlock;
import model.symbols.Symbol;
import model.symbols.SymbolString;

/**
 * Block Turing machine that takes a single-tape TM, converts it to unary
 * Universal TM encoding, converts tape input to same encoding, simulates
 * original TM using a Universal TM, then (if accepted) translates tape 
 * back to original alphabet (still marking correct position).
 * @author Ian McMahon, Julian
 *
 */
public class ConvertedUniversalTM extends BlockTuringMachine {

	private SymbolString myEncoding;

	public ConvertedUniversalTM(MultiTapeTuringMachine tm){
		this(getEncoding(tm), tm.getTapeAlphabet());
	}

	public ConvertedUniversalTM(SymbolString encoding, TapeAlphabet tapeAlphabet) {
		this(encoding, tapeAlphabet.toCopiedSet());
	}

	private ConvertedUniversalTM(SymbolString encoding, Set<Symbol> copiedSet) {
		this.getTapeAlphabet().addAll(copiedSet);
		constructMachine(encoding);
	}

	private void constructMachine(SymbolString encoding) {
		TapeAlphabet alph = getTapeAlphabet();
		Block start = new StartBlock(0);
		Block convertTo = new ConvertInputBlock(encoding, alph, 1);
		Block univTM = new UniversalTMBlock(2);
		Block translateFrom = new RetrieveOutputBlock(alph, 3);
		Block halt = new HaltBlock(4);

		Symbol tilde = new Symbol(TILDE);
		
		BlockTransition t1 = new BlockTransition(start, convertTo, tilde);
		BlockTransition t2 = new BlockTransition(convertTo, univTM, tilde);
		BlockTransition t3 = new BlockTransition(univTM, translateFrom, tilde);
		BlockTransition t4 = new BlockTransition(translateFrom, halt, tilde);

		TransitionSet<BlockTransition> transitions = this.getTransitions();
		for (BlockTransition t: new BlockTransition[]{t1,t2,t3,t4}){
			transitions.add(t);
		}
		this.setStartState(start);
		this.getFinalStateSet().add(halt);
		

		
	}

	private static SymbolString getEncoding(MultiTapeTuringMachine tm) {
		TMtoEncodingConversion conv = new  TMtoEncodingConversion(tm);
		conv.stepToCompletion();
		return conv.getEncoding();
	
	}
	
}
