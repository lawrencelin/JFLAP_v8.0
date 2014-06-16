package model.automata.turing.universal;

import model.algorithms.AlgorithmException;
import model.algorithms.testinput.simulate.configurations.tm.MultiTapeTMConfiguration;
import model.algorithms.testinput.simulate.configurations.tm.TMConfiguration;
import model.automata.State;
import model.automata.StateSet;
import model.automata.TransitionSet;
import model.automata.acceptors.FinalStateSet;
import model.automata.turing.MultiTapeTMTransition;
import model.automata.turing.MultiTapeTuringMachine;
import model.automata.turing.TuringMachineMove;
import model.symbols.Symbol;
import model.symbols.SymbolString;

/**
 * Three tape Universal TM, either assumes that input tapes are as follows: 
 * T1 = Input Encoding T2 = Transition Encoding T3 = State Encoding
 * (if flipForBlock is false) or single input encoded as 
 * TRANS00INPUT (if true). Taken from JFLAP guide.
 * 
 * @author Julian, Ian McMahon
 * 
 */
public class UniversalTuringMachine extends MultiTapeTuringMachine {

	private boolean shouldFlip;
	public static final SymbolString TAPE_DELIMITER = new SymbolString(
			new Symbol("0"), new Symbol("0"));

	public UniversalTuringMachine(boolean flipForBlock) {
		super(3);
		shouldFlip = flipForBlock;
		buildMachine();
	}

	@Override
	public TMConfiguration createInitalConfig(SymbolString input, int pos) {

		// input is: TRANS00INPUT
		SymbolString[] parts = input.split(TAPE_DELIMITER
				.toArray(new Symbol[0]));
		
		if (parts.length != 2)
			throw new AlgorithmException("An error occurred splitting " + input
					+ " while " + "preparing a Universal TM inital config");
		parts[0].add(new Symbol("0"));
		SymbolString stateEncoding = new SymbolString(new Symbol("1"));
		// Alternate definition of turing machine
		return new MultiTapeTMConfiguration(this, this.getStartState(),
				new int[] { pos, 0, 0 }, new SymbolString[] { parts[1],
						stateEncoding, parts[0] });
	}

	public void buildMachine() {
		StateSet states = this.getStates();
		State[] q = new State[34];

		for (int i = 0; i < 34; i++) {
			State qi = new State("q" + i, i);
			states.add(qi);
			q[i] = qi;
		}

		Symbol zero = new Symbol("0"), one = new Symbol("1"), blank = getBlankSymbol();

		TuringMachineMove R = TuringMachineMove.RIGHT, L = TuringMachineMove.LEFT, S = TuringMachineMove.STAY;

		TransitionSet<MultiTapeTMTransition> functions = this.getTransitions();

		addTransition(q, 0, 0, one, one, R, one, one, S, one, one, R);

		addTransition(q, 0, 1, zero, zero, R, one, one, S, blank, blank, L);

		addTransition(q, 11, 0, one, one, S, one, one, S, blank, blank, R);

		addTransition(q, 0, 6, zero, zero, L, one, one, S, one, one, S);

		addTransition(q, 0, 6, one, one, S, one, one, S, blank, blank, L);

		addTransition(q, 31, 0, one, one, S, one, one, S, blank, blank, L);

		addTransition(q, 1, 1, one, one, S, one, one, S, one, one, L);

		addTransition(q, 1, 2, one, one, S, one, one, S, blank, blank, R);

		addTransition(q, 2, 2, one, one, R, one, one, R, one, one, S);

		addTransition(q, 2, 3, zero, zero, R, zero, zero, L, one, one, S);

		addTransition(q, 2, 12, zero, zero, L, one, one, S, one, one, S);

		addTransition(q, 2, 12, one, one, S, zero, zero, L, one, one, S);

		addTransition(q, 3, 3, one, one, S, one, one, L, one, one, S);

		addTransition(q, 3, 4, one, one, S, blank, blank, R, one, one, S);

		addTransition(q, 3, 4, one, one, S, zero, zero, R, one, one, S);

		addTransition(q, 4, 4, one, one, R, one, one, S, one, one, R);

		addTransition(q, 4, 4, one, one, R, one, one, S, blank, one, R);

		addTransition(q, 4, 5, zero, zero, R, one, one, S, blank, blank, L);

		addTransition(q, 4, 5, zero, zero, R, one, one, S, one, blank, L);

		addTransition(q, 5, 5, one, one, S, one, one, S, one, one, L);

		addTransition(q, 5, 13, one, one, R, one, one, R, blank, blank, R);

		addTransition(q, 6, 6, one, one, R, one, one, S, one, one, S);

		addTransition(q, 6, 7, zero, zero, R, one, one, S, one, one, S);

		addTransition(q, 7, 7, one, one, R, one, one, S, one, one, S);

		addTransition(q, 7, 8, zero, zero, R, one, one, S, one, one, S);

		addTransition(q, 8, 8, one, one, R, one, one, S, one, one, S);

		addTransition(q, 8, 9, zero, zero, R, one, one, S, one, one, S);

		addTransition(q, 9, 9, one, one, R, one, one, S, one, one, S);

		addTransition(q, 9, 10, zero, zero, R, one, one, S, one, one, S);

		addTransition(q, 10, 10, one, one, R, one, one, S, one, one, S);

		addTransition(q, 10, 11, zero, zero, R, one, one, S, one, one, S);

		addTransition(q, 11, 11, one, one, S, one, one, S, one, one, L);

		addTransition(q, 32, 11, one, one, S, one, one, S, one, one, S);

		addTransition(q, 12, 12, one, one, S, one, one, L, one, one, S);

		addTransition(q, 12, 7, one, one, S, zero, zero, R, one, one, S);

		addTransition(q, 12, 7, one, one, S, blank, blank, R, one, one, S);

		addTransition(q, 13, 13, one, one, R, one, one, R, one, one, S);

		addTransition(q, 13, 14, one, one, S, zero, blank, R, one, one, S);

		addTransition(q, 14, 14, one, one, S, zero, zero, R, one, one, S);

		addTransition(q, 14, 15, one, one, S, one, zero, R, one, one, S);

		addTransition(q, 14, 16, one, one, S, blank, zero, S, one, one, S);

		addTransition(q, 15, 14, one, one, S, zero, one, R, one, one, S);

		addTransition(q, 15, 15, one, one, S, one, one, R, one, one, S);

		addTransition(q, 15, 16, one, one, S, blank, one, S, one, one, S);

		addTransition(q, 16, 16, one, one, S, zero, zero, L, one, one, S);

		addTransition(q, 16, 16, one, one, S, one, one, L, one, one, S);

		addTransition(q, 16, 13, one, one, R, blank, one, R, one, one, S);

		addTransition(q, 13, 18, zero, zero, S, one, blank, R, one, one, S);

		addTransition(q, 13, 17, zero, zero, R, zero, zero, L, one, one, S);

		addTransition(q, 18, 18, zero, zero, S, one, one, R, one, one, S);

		addTransition(q, 18, 18, zero, zero, S, zero, zero, R, one, one, S);

		addTransition(q, 18, 21, zero, zero, S, blank, blank, L, one, one, S);

		addTransition(q, 21, 19, zero, zero, S, zero, blank, L, one, one, S);

		addTransition(q, 21, 20, zero, zero, S, one, blank, L, one, one, S);

		addTransition(q, 19, 19, zero, zero, S, zero, zero, L, one, one, S);

		addTransition(q, 19, 20, zero, zero, S, one, zero, L, one, one, S);

		addTransition(q, 19, 13, zero, zero, S, blank, zero, S, one, one, S);

		addTransition(q, 20, 20, zero, zero, S, one, one, L, one, one, S);

		addTransition(q, 20, 19, zero, zero, S, zero, one, L, one, one, S);

		addTransition(q, 20, 13, zero, zero, S, blank, one, S, one, one, S);

		addTransition(q, 17, 17, one, one, S, one, one, L, one, one, S);

		addTransition(q, 17, 22, one, one, S, blank, zero, L, one, one, S);

		addTransition(q, 17, 23, one, one, S, zero, zero, L, one, one, S);

		addTransition(q, 22, 24, one, one, R, blank, one, S, one, one, S);

		addTransition(q, 23, 23, one, one, S, one, one, L, one, one, S);

		addTransition(q, 23, 24, one, one, R, zero, zero, R, one, one, S);

		addTransition(q, 23, 24, one, one, R, blank, blank, R, one, one, S);

		addTransition(q, 24, 26, one, one, S, one, one, R, one, one, S);

		addTransition(q, 24, 25, zero, zero, S, one, one, S, one, one, S);

		addTransition(q, 26, 26, one, one, S, one, one, R, one, one, S);

		addTransition(q, 26, 27, one, one, R, zero, zero, R, one, one, S);

		addTransition(q, 27, 28, one, one, S, one, one, R, one, one, S);

		addTransition(q, 27, 25, zero, zero, S, one, one, S, one, one, S);

		addTransition(q, 28, 28, one, one, S, one, one, R, one, one, S);

		addTransition(q, 28, 29, one, one, S, zero, zero, R, one, one, S);

		addTransition(q, 29, 25, one, one, S, one, one, S, one, one, S);

		addTransition(q, 29, 30, one, one, S, blank, one, R, one, one, S);

		addTransition(q, 30, 25, one, one, S, blank, zero, L, one, one, S);

		addTransition(q, 25, 25, one, one, L, one, one, S, one, one, S);

		addTransition(q, 25, 25, zero, zero, L, one, one, S, one, one, S);

		addTransition(q, 25, 31, blank, blank, R, one, one, S, one, one, R);

		addTransition(q, 31, 32, one, one, S, one, one, S, one, one, R);

		addTransition(q, 32, 33, one, one, S, one, one, S, blank, blank, L);

		setStartState(q[0]);

		FinalStateSet finalStates = this.getFinalStateSet();
		finalStates.add(q[33]);

	}

	private void addTransition(State[] q, int from, int to, Symbol r1, Symbol w1,
			TuringMachineMove m1, Symbol r2, Symbol w2, TuringMachineMove m2,
			Symbol r3, Symbol w3, TuringMachineMove m3) {
		MultiTapeTMTransition trans;
		if (shouldFlip) {
			trans = new MultiTapeTMTransition(q[from], q[to], new Symbol[] {
					r2, r3, r1 }, new Symbol[] { w2, w3, w1 },
					new TuringMachineMove[] { m2, m3, m1 });
		} else {
			trans = new MultiTapeTMTransition(q[from], q[to], new Symbol[] {
					r1, r2, r3 }, new Symbol[] { w1, w2, w3 },
					new TuringMachineMove[] { m1, m2, m3 });
		}
		getTransitions().add(trans);

	}
}
