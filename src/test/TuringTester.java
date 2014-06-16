package test;

import java.io.File;

import model.algorithms.testinput.simulate.AutoSimulator;
import model.algorithms.testinput.simulate.MultiSimulator;
import model.automata.State;
import model.automata.TransitionSet;
import model.automata.turing.MultiTapeTMTransition;
import model.automata.turing.MultiTapeTuringMachine;
import model.automata.turing.TapeAlphabet;
import model.automata.turing.TuringMachineMove;
import model.automata.turing.buildingblock.Block;
import model.automata.turing.buildingblock.BlockTransition;
import model.automata.turing.buildingblock.BlockTuringMachine;
import model.automata.turing.buildingblock.library.HaltBlock;
import model.automata.turing.buildingblock.library.MoveBlock;
import model.automata.turing.buildingblock.library.MoveUntilBlock;
import model.automata.turing.buildingblock.library.ShiftBlock;
import model.automata.turing.buildingblock.library.StartBlock;
import model.automata.turing.buildingblock.library.WriteBlock;
import model.automata.turing.universal.WriteRightBlock;
import model.symbols.Symbol;
import model.symbols.SymbolString;
import model.symbols.symbolizer.Symbolizers;
import util.JFLAPConstants;
import debug.JFLAPDebug;
import file.xml.XMLCodec;

public class TuringTester {
	
	public static void main(String[]args){
		String toSave = System.getProperties().getProperty("user.dir")
				+ "/filetest";
//		
		MultiTapeTuringMachine tm = createBinaryAdder();
//		
		File f = new File(toSave + "/ex9-adder.jff");
		JFLAPDebug.print("Before import:\n" + tm.toString());
		XMLCodec codec = new XMLCodec();
		codec.encode(tm, f, null);
		tm = (MultiTapeTuringMachine) codec.decode(f);
		JFLAPDebug.print("After import:\n" + tm.toString());
//		
//		f = new File(toSave + "/ex9.5langacc-b.jff");
//		tm = createLangacc_b();
//		JFLAPDebug.print("Before import:\n" + tm.toString());
//		codec.encode(tm, f, null);
//		tm = (MultiTapeTuringMachine) codec.decode(f);
//		JFLAPDebug.print("After import:\n" + tm.toString());
//		
//		f = new File(toSave + "/ex9.5trans-a.jff");
//		tm = createTrans_a();
//		JFLAPDebug.print("Before import:\n" + tm.toString());
//		codec.encode(tm, f, null);
//		tm = (MultiTapeTuringMachine) codec.decode(f);
//		JFLAPDebug.print("After import:\n" + tm.toString());
//		
//		f = new File(toSave + "/ex9.5ttm-whatlang-a.jff");
//		tm = createwhatlang_a();
//		JFLAPDebug.print("Before import:\n" + tm.toString());
//		codec.encode(tm, f, null);
//		tm = (MultiTapeTuringMachine) codec.decode(f);
//		JFLAPDebug.print("After import:\n" + tm.toString());
//		
//		f = new File(toSave + "/ex9.5ttm-whattran-a.jff");
//		tm = createwhattran_a();
//		JFLAPDebug.print("Before import:\n" + tm.toString());
//		codec.encode(tm, f, null);
//		tm = (MultiTapeTuringMachine) codec.decode(f);
//		JFLAPDebug.print("After import:\n" + tm.toString());
//		
//		f = new File(toSave + "/ex9.2tape-substring.jff");
//		tm = createTape_substring();
//		JFLAPDebug.print("Before import:\n" + tm.toString());
//		codec.encode(tm, f, null);
//		tm = (MultiTapeTuringMachine) codec.decode(f);
//		JFLAPDebug.print("After import:\n" + tm.toString());
	}

	private static MultiTapeTuringMachine createLangacc_a() {
		MultiTapeTuringMachine tm = new MultiTapeTuringMachine();
		TransitionSet<MultiTapeTMTransition> transitions = tm.getTransitions();
		
		Symbol a = new Symbol("a"), X = new Symbol("X"), b = new Symbol("b");
		TuringMachineMove R= TuringMachineMove.RIGHT, L=TuringMachineMove.LEFT;
		
		State[] q = new State[8];
		for(int i=0; i<8; i++){
			q[i] = new State("q"+i, i);
		}
		
		tm.setStartState(q[0]);
		addTransition(transitions, q[0], q[1], a, X, R);
		addTransition(transitions, q[1], q[1], a, a, R);
		addTransition(transitions, q[1], q[2], b, X, R);
		addTransition(transitions, q[1], q[4], X, X, R);
		addTransition(transitions, q[4], q[4], X, X, R);
		addTransition(transitions, q[4], q[2], b, X, R);
		addTransition(transitions, q[2], q[3], b, X, L);
		addTransition(transitions, q[3], q[3], X, X, L);
		addTransition(transitions, q[3], q[5], a, a, L);
		addTransition(transitions, q[5], q[5], a, a, L);
		addTransition(transitions, q[5], q[0], X, X, R);
		addTransition(transitions, q[3], q[6], tm.getBlankSymbol(), tm.getBlankSymbol(), R);
		addTransition(transitions, q[6], q[6], X, X, R);
		addTransition(transitions, q[6], q[7], tm.getBlankSymbol(), tm.getBlankSymbol(), R);
		tm.getFinalStateSet().add(q[7]);
		
		return tm;
	}
	
	private static MultiTapeTuringMachine createLangacc_b(){
		MultiTapeTuringMachine tm = new MultiTapeTuringMachine();
		TransitionSet<MultiTapeTMTransition> transitions = tm.getTransitions();
		
		Symbol a = new Symbol("a"), X = new Symbol("X"), b = new Symbol("b"),
				blank = tm.getBlankSymbol();
		TuringMachineMove R= TuringMachineMove.RIGHT, L=TuringMachineMove.LEFT;
		
		State[] q = new State[5];
		for(int i=0; i<5; i++){
			q[i] = new State("q"+i, i);
		}
		
		tm.setStartState(q[0]);
		tm.getFinalStateSet().add(q[3]);
		addTransition(transitions, q[0], q[0], a, a, R);
		addTransition(transitions, q[0], q[0], X, X, R);
		addTransition(transitions, q[0], q[1], b, X, R);
		addTransition(transitions, q[1], q[1], b, b, R);
		addTransition(transitions, q[1], q[1], X, X, R);
		addTransition(transitions, q[1], q[2], blank, blank, L);
		addTransition(transitions, q[1], q[4], a, X, L);
		addTransition(transitions, q[2], q[2], X, X, L);
		addTransition(transitions, q[2], q[2], b, b, L);
		addTransition(transitions, q[2], q[3], blank, blank, R);
		addTransition(transitions, q[2], q[4], a, X, L);
		addTransition(transitions, q[4], q[4], a, a, L);
		addTransition(transitions, q[4], q[4], b, b, L);
		addTransition(transitions, q[4], q[4], X, X, L);
		addTransition(transitions, q[4], q[0], blank, blank, R);
		
		return tm;
	}
	
	private static MultiTapeTuringMachine createTrans_a(){
		MultiTapeTuringMachine tm = new MultiTapeTuringMachine();
		TransitionSet<MultiTapeTMTransition> transitions = tm.getTransitions();
		
		Symbol a = new Symbol("a"), X = new Symbol("X"), b = new Symbol("b"),
				blank = tm.getBlankSymbol();
		TuringMachineMove R= TuringMachineMove.RIGHT, L=TuringMachineMove.LEFT;
		int id=0;
		
		State[] q = new State[4];
		for(int i=0; i<4; i++){
			q[i] = new State("q"+i, i);
		}
		
		tm.setStartState(q[0]);
		tm.getFinalStateSet().add(q[3]);
		
		addTransition(transitions, q[0], q[0], b, b, R);
		addTransition(transitions, q[0], q[1], a, a, R);
		addTransition(transitions, q[0], q[2], blank, blank, L);
		addTransition(transitions, q[1], q[0], a, b, R);
		addTransition(transitions, q[1], q[0], b, b, R);
		addTransition(transitions, q[1], q[2], blank, blank, L);
		addTransition(transitions, q[2], q[2], b, b, L);
		addTransition(transitions, q[2], q[2], a, a, L);
		addTransition(transitions, q[2], q[3], blank, blank, R);
		
		return tm;
	}
	
	private static MultiTapeTuringMachine createwhatlang_a(){
		MultiTapeTuringMachine tm = new MultiTapeTuringMachine(2);
		TransitionSet<MultiTapeTMTransition> transitions = tm.getTransitions();
		
		Symbol a = new Symbol("a"), b = new Symbol("b"),
				blank = tm.getBlankSymbol();
		TuringMachineMove R= TuringMachineMove.RIGHT, L=TuringMachineMove.LEFT,
				S = TuringMachineMove.STAY;
			
		State[] q = new State[4];
		for(int i=0; i<4; i++){
			q[i] = new State("q"+i, i);
		}
		
		tm.setStartState(q[0]);
		tm.getFinalStateSet().add(q[3]);
		add2TapeTrans(transitions, q[0], q[0], a, a, R, blank, a, R);
		add2TapeTrans(transitions, q[0], q[1], b, b, S, blank, blank, L);
		add2TapeTrans(transitions, q[1], q[2], b, b, R, a, a, S);
		add2TapeTrans(transitions, q[2], q[1], b, b, R, a, a, L);
		add2TapeTrans(transitions, q[1], q[3], blank, blank, R, blank, blank, R);

		return tm;
	}
	
	private static MultiTapeTuringMachine createwhattran_a(){
		MultiTapeTuringMachine tm = new MultiTapeTuringMachine(2);
		TransitionSet<MultiTapeTMTransition> transitions = tm.getTransitions();
		
		Symbol one = new Symbol("1"),
				blank = tm.getBlankSymbol();
		TuringMachineMove R= TuringMachineMove.RIGHT, L=TuringMachineMove.LEFT,
				S = TuringMachineMove.STAY;
			
		State[] q = new State[7];
		for(int i=0; i<7; i++){
			q[i] = new State("q"+i, i);
		}
		
		tm.setStartState(q[0]);
		tm.getFinalStateSet().add(q[6]);
		
		add2TapeTrans(transitions, q[0], q[1], one, one, S, blank, one, R);
		add2TapeTrans(transitions, q[1], q[2], one, one, S, blank, one, R);
		add2TapeTrans(transitions, q[2], q[3], one, one, S, blank, one, R);
		add2TapeTrans(transitions, q[3], q[0], one, one, R, blank, one, R);
		add2TapeTrans(transitions, q[0], q[4], blank, blank, R, blank, one, R);
		add2TapeTrans(transitions, q[4], q[5], blank, blank, R, blank, one, L);
		add2TapeTrans(transitions, q[5], q[5], blank, blank, S, one, one, L);
		add2TapeTrans(transitions, q[5], q[6], blank, blank, R, blank, blank, L);
		
		return tm;
		
	}
	
	private static MultiTapeTuringMachine createTape_substring(){
		MultiTapeTuringMachine tm = new MultiTapeTuringMachine(2);
		TransitionSet<MultiTapeTMTransition> transitions = tm.getTransitions();
		
		Symbol a = new Symbol("a"), b = new Symbol("b"),
				blank = tm.getBlankSymbol();
		TuringMachineMove R= TuringMachineMove.RIGHT, L=TuringMachineMove.LEFT,
				S = TuringMachineMove.STAY;
			
		State[] q = new State[3];
		for(int i=0; i<3; i++){
			q[i] = new State("q"+i, i);
		}
		
		tm.setStartState(q[0]);
		tm.getFinalStateSet().add(q[2]);
		
		add2TapeTrans(transitions, q[0], q[0], b, b, S, b, b, R);
		add2TapeTrans(transitions, q[0], q[0], a, a, S, b, b, R);
		add2TapeTrans(transitions, q[0], q[0], b, b, S, a, a, R);
		add2TapeTrans(transitions, q[0], q[0], a, a, S, a, a, R);
		add2TapeTrans(transitions, q[0], q[1], b, b, R, b, b, R);
		add2TapeTrans(transitions, q[0], q[1], a, a, R, a, a, R);
		add2TapeTrans(transitions, q[0], q[2], blank, blank, S, blank, blank, S);
		add2TapeTrans(transitions, q[0], q[2], blank, blank, S, a, a, R);
		add2TapeTrans(transitions, q[0], q[2], blank, blank, S, b, b, R);
		add2TapeTrans(transitions, q[1], q[1], b, b, R, b, b, R);
		add2TapeTrans(transitions, q[1], q[1], a, a, R, a, a, R);
		add2TapeTrans(transitions, q[1], q[2], blank, blank, S, blank, blank, S);
		add2TapeTrans(transitions, q[1], q[2], blank, blank, S, a, a, R);
		add2TapeTrans(transitions, q[1], q[2], blank, blank, S, b, b, R);
		
		return tm;
	
	}
	
	private static BlockTuringMachine createUnaryMultiply(){
		BlockTuringMachine tm = new BlockTuringMachine();
		TransitionSet<BlockTransition> transitions = tm.getTransitions();
		TapeAlphabet alph = tm.getTapeAlphabet();
		
		TuringMachineMove R = TuringMachineMove.RIGHT, L = TuringMachineMove.LEFT;
		Symbol blank = tm.getBlankSymbol(), one =new Symbol("1"), tilde = new Symbol(JFLAPConstants.TILDE),
				hash = new Symbol(JFLAPConstants.TM_MARKER), star = new Symbol("*");
		
		int id=0;
		
		Block b1 = new StartBlock(id++);
		tm.setStartState(b1);
		Block b2 = new MoveUntilBlock(R, blank, alph, id++);
		addBlockTransition(transitions, b1, b2, tilde);
		
		b1=b2;
		b2 = new WriteBlock(hash, alph, id++);
		addBlockTransition(transitions, b1, b2, tilde);
		
		b1=b2;
		Block backTrack = b2 = new MoveUntilBlock(L, blank, alph, id++);
		addBlockTransition(transitions, b1, b2, tilde);
		
		b1=b2;
		Block out = b2 = new MoveBlock(R, alph, id++);
		addBlockTransition(transitions, b1, b2, tilde);
		
		b1=b2;
		b2 = new WriteBlock(blank, alph, id++);
		addBlockTransition(transitions, b1, b2, one);
		
		b1=b2; 
		b2 = new MoveUntilBlock(R, star, alph, id++);
		addBlockTransition(transitions, b1, b2, tilde);
		
		b1=b2;
		b2 = new MoveBlock(R, alph, id++);
		addBlockTransition(transitions, b1, b2, tilde);
		addBlockTransition(transitions, b2, backTrack, hash);
		
		backTrack = b1 = b2;
		b2 = new WriteBlock(blank, alph, id++);
		addBlockTransition(transitions, b1, b2, one);
		
		b1=b2;
		b2 = new MoveUntilBlock(R, blank, alph, id++);
		addBlockTransition(transitions, b1, b2, tilde);
		
		b1=b2;
		b2 = new WriteBlock(one, alph, id++);
		addBlockTransition(transitions, b1, b2, tilde);
		
		b1=b2;
		b2 = new MoveUntilBlock(L, blank, alph, id++);
		addBlockTransition(transitions, b1, b2, tilde);
		
		b1=b2;
		b2 = new WriteBlock(one, alph, id++);
		addBlockTransition(transitions, b1, b2, tilde);
		addBlockTransition(transitions, b2, backTrack, tilde);
		
		b1 = new WriteRightBlock(alph, id++);
		b2 = new ShiftBlock(L, alph, id++);
		addBlockTransition(transitions, out, b1, star);
		addBlockTransition(transitions, b1, b1, one);
		addBlockTransition(transitions, b1, b2, hash);
		
		b1=b2;
		b2 = new HaltBlock(id);
		addBlockTransition(transitions, b1, b2, tilde);
		tm.getFinalStateSet().add(b2);
		
		return tm;
	}
	
	private static MultiTapeTuringMachine createBinaryAdder(){
		MultiTapeTuringMachine tm = new MultiTapeTuringMachine();
		TransitionSet<MultiTapeTMTransition> transitions = tm.getTransitions();
		
		Symbol one = new Symbol("1"), zero = new Symbol("0"), hash = new Symbol(JFLAPConstants.TM_MARKER),
				plus = new Symbol("+"), a = new Symbol("a"), b = new Symbol("b"),
				blank = tm.getBlankSymbol();
		TuringMachineMove R = TuringMachineMove.RIGHT, L = TuringMachineMove.LEFT;

		State[] q = new State[14];
		for(int i=0; i<14; i++){
			q[i] = new State("q"+ i, i);
		}
		tm.setStartState(q[0]);
		tm.getFinalStateSet().add(q[3]);
		addTransition(transitions, q[0], q[0], zero, zero, R);
		addTransition(transitions, q[0], q[0], hash, hash, R);
		addTransition(transitions, q[0], q[0], a, zero, R);
		addTransition(transitions, q[0], q[1], one, one, R);
		addTransition(transitions, q[0], q[1], b, one, R);
		addTransition(transitions, q[0], q[2], blank, blank, L);
		addTransition(transitions, q[0], q[4], plus, plus, R);
		addTransition(transitions, q[1], q[0], a, zero, R);
		addTransition(transitions, q[1], q[0], zero, zero, R);
		addTransition(transitions, q[1], q[1], b, one, R);
		addTransition(transitions, q[1], q[1], hash, hash, R);
		addTransition(transitions, q[1], q[1], one, one, R);
		addTransition(transitions, q[1], q[2], blank, blank, L);
		addTransition(transitions, q[1], q[5], plus, plus, R);
		addTransition(transitions, q[2], q[2], one, one, L);
		addTransition(transitions, q[2], q[2], zero, zero, L);
		addTransition(transitions, q[2], q[3], blank, blank, R);
		addTransition(transitions, q[4], q[4], zero, zero, R);
		addTransition(transitions, q[4], q[4], one, one, R);
		addTransition(transitions, q[4], q[6], b, b, L);
		addTransition(transitions, q[4], q[6], a, a, L);
		addTransition(transitions, q[4], q[6], plus, plus, L);
		addTransition(transitions, q[4], q[6], blank, blank, L);
		addTransition(transitions, q[5], q[5], one, one, R);
		addTransition(transitions, q[5], q[5], zero, zero, R);
		addTransition(transitions, q[5], q[7], b, b, L);
		addTransition(transitions, q[5], q[7], a, a, L);
		addTransition(transitions, q[5], q[7], blank, blank, L);
		addTransition(transitions, q[5], q[7], plus, plus, L);
		addTransition(transitions, q[6], q[8], zero, a, L);
		addTransition(transitions, q[6], q[8], one, b, L);
		addTransition(transitions, q[6], q[13], plus, a, L);
		addTransition(transitions, q[7], q[8], zero, b, L);
		addTransition(transitions, q[7], q[9], one, a, L);
		addTransition(transitions, q[7], q[13], plus, b, L);
		addTransition(transitions, q[8], q[8], zero, zero, L);
		addTransition(transitions, q[8], q[8], one, one, L);
		addTransition(transitions, q[8], q[10], plus, plus, L);
		addTransition(transitions, q[9], q[8], zero, one, L);
		addTransition(transitions, q[9], q[9], one, zero, L);
		addTransition(transitions, q[9], q[10], hash, one, L);
		addTransition(transitions, q[9], q[13], plus, one, L);
		addTransition(transitions, q[10], q[10], hash, hash, L);
		addTransition(transitions, q[10], q[11], zero, hash, L);
		addTransition(transitions, q[10], q[11], one, hash, L);
		addTransition(transitions, q[10], q[12], blank, blank, R);
		addTransition(transitions, q[11], q[11], zero, zero, L);
		addTransition(transitions, q[11], q[11], one, one, L);
		addTransition(transitions, q[11], q[12], blank, blank, R);
		addTransition(transitions, q[12], q[0], zero, zero, R);
		addTransition(transitions, q[12], q[0], a, zero, R);
		addTransition(transitions, q[12], q[1], b, one, R);
		addTransition(transitions, q[12], q[1], one, one, R);
		addTransition(transitions, q[12], q[12], hash, blank, R);
		addTransition(transitions, q[12], q[12], plus, blank, R);
		addTransition(transitions, q[13], q[10], hash, plus, L);
		addTransition(transitions, q[13], q[11], one, plus, L);
		addTransition(transitions, q[13], q[11], zero, plus, L);
		
		return tm;
	}

	private static void addTransition(TransitionSet<MultiTapeTMTransition> transitions, State from, State to, Symbol read, Symbol write, TuringMachineMove move){
		transitions.add(new MultiTapeTMTransition(from, to, read, write, move));
	}
	
	private static void add2TapeTrans(TransitionSet<MultiTapeTMTransition> transitions, State from, State to, Symbol r1, Symbol w1, TuringMachineMove m1, Symbol r2, Symbol w2, TuringMachineMove m2){
		transitions.add(new MultiTapeTMTransition(from, to, new Symbol[]{r1,r2}, new Symbol[]{w1,w2}, new TuringMachineMove[]{m1,m2}));
	}
	
	private static void addBlockTransition(TransitionSet<BlockTransition> transitions, Block from, Block to, Symbol read){
		transitions.add(new BlockTransition(from, to, read));
	}
}
