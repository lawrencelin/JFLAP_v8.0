package test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import file.xml.XMLCodec;

import model.algorithms.testinput.simulate.AutoSimulator;
import model.automata.InputAlphabet;
import model.automata.StartState;
import model.automata.State;
import model.automata.StateSet;
import model.automata.TransitionSet;
import model.automata.acceptors.FinalStateSet;
import model.automata.turing.BlankSymbol;
import model.automata.turing.MultiTapeTMTransition;
import model.automata.turing.MultiTapeTuringMachine;
import model.automata.turing.TapeAlphabet;
import model.automata.turing.TuringMachineMove;
import model.symbols.Symbol;
import model.symbols.SymbolString;
import model.symbols.symbolizer.Symbolizers;

public class UniversalTMTest {
	private static State[] q;

	public static void main(String[] args){		
		StateSet states = new StateSet();
		q = new State[40];
		
		for(int i=0; i<40; i++){
			State qi = new State("q"+i, i);
			states.add(qi);
			q[i] = qi;
		}
		
		BlankSymbol blank = new BlankSymbol();
		Symbol zero = new Symbol("0"), one = new Symbol("1"), square = blank.getSymbol();
		TapeAlphabet tapeAlph = new TapeAlphabet();
		tapeAlph.addAll(zero, one);
		
		
		
		InputAlphabet inputAlph = new InputAlphabet();
		inputAlph.addAll(zero, one);
		
		TuringMachineMove R = TuringMachineMove.RIGHT, L = TuringMachineMove.LEFT, S = TuringMachineMove.STAY;
		
		
		TransitionSet<MultiTapeTMTransition> functions = new TransitionSet<MultiTapeTMTransition>();
		MultiTapeTMTransition trans = new MultiTapeTMTransition(
				q[0], q[0], new Symbol[]{one,one,one}, new Symbol[]{one,one,one}, new TuringMachineMove[]{R,S,R});
		functions.add(trans);
		
		trans = new MultiTapeTMTransition(
				q[0], q[1], new Symbol[]{zero,one,square}, new Symbol[]{zero,one,square}, new TuringMachineMove[]{R,S,L});
		functions.add(trans);
		
		trans = new MultiTapeTMTransition(
				q[11], q[0], new Symbol[]{one,one,square}, new Symbol[]{one,one,square}, new TuringMachineMove[]{S,S,R});
		functions.add(trans);

		trans = new MultiTapeTMTransition(
				q[0], q[6], new Symbol[]{zero,one,one}, new Symbol[]{zero,one,one}, new TuringMachineMove[]{L,S,S});
		functions.add(trans);
		
		trans = new MultiTapeTMTransition(
				q[0], q[6], new Symbol[]{one,one,square}, new Symbol[]{one,one,square}, new TuringMachineMove[]{S,S,L});
		functions.add(trans);
		
		trans = new MultiTapeTMTransition(
				q[31], q[0], new Symbol[]{one,one,square}, new Symbol[]{one,one,square}, new TuringMachineMove[]{S,S,L});
		functions.add(trans);
		
		trans = new MultiTapeTMTransition(
				q[1], q[1], new Symbol[]{one,one,one}, new Symbol[]{one,one,one}, new TuringMachineMove[]{S,S,L});
		functions.add(trans);
		
		trans = new MultiTapeTMTransition(
				q[1], q[2], new Symbol[]{one,one,square}, new Symbol[]{one,one,square}, new TuringMachineMove[]{S,S,R});
		functions.add(trans);
		
		trans = new MultiTapeTMTransition(
				q[2], q[2], new Symbol[]{one,one,one}, new Symbol[]{one,one,one}, new TuringMachineMove[]{R,R,S});
		functions.add(trans);
		
		trans = new MultiTapeTMTransition(
				q[2], q[3], new Symbol[]{zero,zero,one}, new Symbol[]{zero,zero,one}, new TuringMachineMove[]{R,L,S});
		functions.add(trans);
		
		trans = new MultiTapeTMTransition(
				q[2], q[12], new Symbol[]{zero,one,one}, new Symbol[]{zero,one,one}, new TuringMachineMove[]{L,S,S});
		functions.add(trans);

		trans = new MultiTapeTMTransition(
				q[2], q[12], new Symbol[]{one,zero,one}, new Symbol[]{one,zero,one}, new TuringMachineMove[]{S,L,S});
		functions.add(trans);
		
		trans = new MultiTapeTMTransition(
				q[3], q[3], new Symbol[]{one,one,one}, new Symbol[]{one,one,one}, new TuringMachineMove[]{S,L,S});
		functions.add(trans);
		
		trans = tri(q,3, 4,one,one, S,square,square, R,one,one, S);
		functions.add(trans);
		
		trans = tri(q,3, 4,one,one, S,zero,zero, R,one,one, S);
		functions.add(trans);
		
		trans = tri(q,4, 4,one,one, R,one,one, S,one,one, R);
		functions.add(trans);
		
		trans = tri(q,4, 4,one,one, R,one,one, S,square,one, R);
		functions.add(trans);
		
		trans = tri(q,4, 5,zero,zero, R,one,one, S,square,square, L);
		functions.add(trans);
		
		trans = tri(q,4, 5,zero,zero, R,one,one, S,one,square, L);
		functions.add(trans);
		
		trans = tri(q,5, 5,one,one, S,one,one, S,one,one, L);
		functions.add(trans);
		
		trans = tri(q,5, 13,one,one, R,one,one, R,square,square, R);
		functions.add(trans);

		trans = tri(q,6, 6,one,one, R,one,one, S,one,one, S);
		functions.add(trans);
		
		trans = tri(q,6, 7,zero,zero, R,one,one, S,one, one, S);
		functions.add(trans);
		
		trans = tri(q,7, 7,one,one, R,one,one, S,one,one, S);
		functions.add(trans);
		
		trans = tri(q,7, 8,zero,zero, R,one,one, S,one,one, S);
		functions.add(trans);
		
		trans = tri(q,8, 8,one,one, R,one,one, S,one,one, S);
		functions.add(trans);
		
		trans = tri(q,8, 9,zero,zero, R,one,one, S,one,one, S);
		functions.add(trans);
		
		trans = tri(q,9, 9,one,one, R,one,one, S,one,one, S);
		functions.add(trans);
		
		trans = tri(q,9, 10,zero,zero, R,one,one, S,one,one, S);
		functions.add(trans);
		
		trans = tri(q,10, 10,one,one, R,one,one, S,one,one, S);
		functions.add(trans);
		
		trans = tri(q,10, 11,zero,zero, R,one,one, S,one,one, S);
		functions.add(trans);
		
		trans = tri(q,11, 11,one,one, S,one,one, S,one,one, L);
		functions.add(trans);
		
		trans = tri(q,32, 11,one,one, S,one,one, S,one,one, S);
		functions.add(trans);
		
		trans = tri(q,12, 12,one,one, S,one,one, L, one,one, S);
		functions.add(trans);
		
		trans = tri(q,12, 7,one,one , S,zero,zero, R,one,one, S);
		functions.add(trans);
		
		trans = tri(q,12, 7,one,one , S,square,square, R,one,one, S);
		functions.add(trans);
		
		trans = tri(q,13, 13,one,one , R,one,one, R,one,one, S);
		functions.add(trans);

		trans = tri(q,13, 14,one,one , S,zero,square, R,one,one, S);
		functions.add(trans);
		
		trans = tri(q,14, 14,one,one , S,zero,zero, R,one,one, S);
		functions.add(trans);
		
		trans = tri(q,14, 15,one,one , S,one,zero, R,one,one, S);
		functions.add(trans);
		
		trans = tri(q,14, 16,one,one, S,square,zero, S,one,one, S);
		functions.add(trans);
		
		trans = tri(q,15, 14,one,one , S,zero,one, R,one,one, S);
		functions.add(trans);

		trans = tri(q,15, 15,one,one , S,one,one, R,one,one, S);
		functions.add(trans);
		
		trans = tri(q,15, 16,one,one , S,square,one, S,one,one, S);
		functions.add(trans);
		
		trans = tri(q,16, 16,one,one , S,zero,zero, L,one,one, S);
		functions.add(trans);
		
		trans = tri(q,16, 16,one,one , S,one,one, L,one,one, S);
		functions.add(trans);
		
		trans = tri(q,16, 13,one,one , R,square,one, R,one,one, S);
		functions.add(trans);
		
		trans = tri(q,13, 18,zero,zero, S,one,square, R,one,one, S);
		functions.add(trans);
		
		trans = tri(q,13, 17,zero,zero , R,zero,zero, L,one,one, S);
		functions.add(trans);
		
		trans = tri(q,18, 18,zero,zero , S,one,one, R,one,one, S);
		functions.add(trans);
		
		trans = tri(q,18, 18,zero,zero , S,zero,zero, R,one,one, S);
		functions.add(trans);
		
		trans = tri(q,18, 21,zero,zero , S,square,square, L,one,one, S);
		functions.add(trans);
		
		trans = tri(q,21, 19,zero,zero , S,zero,square, L,one,one, S);
		functions.add(trans);
		
		trans = tri(q,21, 20,zero,zero , S,one,square, L,one,one, S);
		functions.add(trans);
		
		trans = tri(q,19, 19,zero,zero , S,zero,zero, L,one,one, S);
		functions.add(trans);
		
		trans = tri(q,19, 20,zero,zero , S,one,zero, L,one,one, S);
		functions.add(trans);
		
		trans = tri(q,19, 13,zero,zero , S,square,zero, S,one,one, S);
		functions.add(trans);
		
		trans = tri(q,20, 20,zero,zero , S,one,one, L,one,one, S);
		functions.add(trans);
		
		trans = tri(q,20, 19,zero,zero , S,zero,one, L,one,one, S);
		functions.add(trans);
		
		trans = tri(q,20, 13,zero,zero , S,square,one, S,one,one, S);
		functions.add(trans);
		
		trans = tri(q,17, 17,one,one , S,one,one, L,one,one, S);
		functions.add(trans);
		
		trans = tri(q,17, 22,one,one , S,square,zero, L,one,one, S);
		functions.add(trans);
		
		trans = tri(q,17, 23,one,one , S,zero,zero, L,one,one, S);
		functions.add(trans);
		
		trans = tri(q,22, 24,one,one , R,square,one, S,one,one, S);
		functions.add(trans);
		
		trans = tri(q,23, 23,one,one , S,one,one, L,one,one, S);
		functions.add(trans);
		
		trans = tri(q,23, 24,one,one , R,zero,zero, R,one,one, S);
		functions.add(trans);
		
		trans = tri(q,23, 24,one,one , R,square,square, R,one,one, S);
		functions.add(trans);
		
		trans = tri(q,24, 26,one,one , S,one,one, R,one,one, S);
		functions.add(trans);
		
		trans = tri(q,24, 25,zero,zero , S,one,one, S,one,one, S);
		functions.add(trans);
		
		trans = tri(q,26, 26,one,one , S,one,one, R,one,one, S);
		functions.add(trans);
		
		trans = tri(q,26, 27,one,one , R,zero,zero, R,one,one, S);
		functions.add(trans);
		
		trans = tri(q,27, 28,one,one , S,one,one, R,one,one, S);
		functions.add(trans);
		
		trans = tri(q,27, 25,zero,zero , S,one,one, S,one,one, S);
		functions.add(trans);
		
		trans = tri(q,28, 28,one,one , S,one,one, R,one,one, S);
		functions.add(trans);
		
		trans = tri(q,28, 29,one,one , S,zero,zero, R,one,one, S);
		functions.add(trans);
		
		trans = tri(q,29, 25,one,one , S,one,one, S,one,one, S);
		functions.add(trans);
		
		trans = tri(q,29, 30,one,one , S,square,one, R,one,one, S);
		functions.add(trans);
		
		trans = tri(q,30, 25,one,one , S,square,zero, L,one,one, S);
		functions.add(trans);
		
		trans = tri(q,25, 25,one,one , L,one,one, S,one,one, S);
		functions.add(trans);
		
		trans = tri(q,25, 25,zero,zero , L,one,one, S,one,one, S);
		functions.add(trans);
		
		trans = tri(q,25, 31,square,square, R,one,one, S,one,one, R);
		functions.add(trans);
		
		trans = tri(q,31, 32,one,one , S,one,one, S,one,one, R);
		functions.add(trans);
		
		trans = tri(q,32, 33,one,one , S,one,one, S,square,square, L);
		functions.add(trans);
		
		trans = tri(q,33, 34,one,one, S, one,one, R, one, one, S);
		functions.add(trans);
		
		trans = tri(q,34, 35,one,one, S,zero,square, L, one, one, S);
		functions.add(trans);
		
		trans = tri(q,35, 36,one,one, S,one,square, L, one, one, S);
		functions.add(trans);
		
		trans = tri(q,36, 36,one,one, S, one,one, L, one, one, S);
		functions.add(trans);
		
		trans = tri(q,36, 36,one,one, S, zero,zero, L, one, one, S);
		functions.add(trans);
		
		trans = tri(q,36, 37,one,one, S, square,square, R, one, one, S);
		functions.add(trans);
		
		trans = tri(q,37, 38,one,one, S, one,square, R, one, one, S);
		functions.add(trans);
		
		trans = tri(q,38, 39,one,one, S, zero,square, R, one, one, S);
		functions.add(trans);
		
		StartState start = new StartState(states.getStateWithID(0));
		
		FinalStateSet finalStates = new FinalStateSet();
		finalStates.add(states.getStateWithID(39));
		
		int numTapes = 3;
		
		MultiTapeTuringMachine universal = new MultiTapeTuringMachine(states, tapeAlph, blank, inputAlph, functions, start, finalStates, numTapes);
		String toSave = System.getProperties().getProperty("user.dir")
				+ "/filetest/universal.jff";
		File f = new File(toSave);
		XMLCodec codec = new XMLCodec();
		codec.encode(universal, f, null);
		
		AutoSimulator simulator = new AutoSimulator(universal, 0);
		simulator.beginSimulation(Symbolizers.symbolize("101101011101110101011010110", universal), Symbolizers.symbolize("110110110", universal), new SymbolString(one));
		System.out.println(simulator.getNextAccept());
	}
	
	private static MultiTapeTMTransition tri(State[] q, int from, int to, Symbol r1, Symbol w1, TuringMachineMove m1, Symbol r2, Symbol w2, TuringMachineMove m2, Symbol r3, Symbol w3, TuringMachineMove m3 ){
		return new MultiTapeTMTransition(q[from], q[to], new Symbol[]{r1,r2,r3}, new Symbol[]{w1,w2,w3}, new TuringMachineMove[]{m1,m2,m3});
	}
}
