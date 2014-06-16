package test;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;

import model.automata.InputAlphabet;
import model.automata.StartState;
import model.automata.State;
import model.automata.StateSet;
import model.automata.TransitionSet;
import model.automata.acceptors.FinalStateSet;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.automata.transducers.OutputAlphabet;
import model.automata.transducers.OutputFunctionSet;
import model.automata.transducers.mealy.MealyMachine;
import model.automata.transducers.mealy.MealyOutputFunction;
import model.automata.transducers.moore.MooreMachine;
import model.automata.transducers.moore.MooreOutputFunction;
import model.automata.turing.BlankSymbol;
import model.automata.turing.MultiTapeTMTransition;
import model.automata.turing.MultiTapeTuringMachine;
import model.automata.turing.TapeAlphabet;
import model.automata.turing.TuringMachineMove;
import model.automata.turing.buildingblock.Block;
import model.automata.turing.buildingblock.BlockSet;
import model.automata.turing.buildingblock.BlockTransition;
import model.automata.turing.buildingblock.BlockTuringMachine;
import model.automata.turing.buildingblock.library.HaltBlock;
import model.automata.turing.buildingblock.library.MoveBlock;
import model.automata.turing.buildingblock.library.MoveUntilBlock;
import model.automata.turing.buildingblock.library.ShiftBlock;
import model.automata.turing.buildingblock.library.StartBlock;
import model.automata.turing.buildingblock.library.WriteBlock;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.ProductionSet;
import model.grammar.StartVariable;
import model.grammar.Terminal;
import model.grammar.TerminalAlphabet;
import model.grammar.Variable;
import model.grammar.VariableAlphabet;
import model.pumping.ContextFreePumpingLemma;
import model.pumping.PumpingLemma;
import model.pumping.RegularPumpingLemma;
import model.pumping.cf.AiBjCk;
import model.pumping.reg.AB2n;
import model.regex.RegularExpression;
import model.symbols.Symbol;
import model.symbols.SymbolString;
import model.symbols.symbolizer.Symbolizers;
import universe.preferences.JFLAPMode;
import util.JFLAPConstants;
import util.UtilFunctions;
import file.xml.XMLCodec;

public class FileTester extends TestHarness implements JFLAPConstants {

	@Override
	public void runTest() {

		String toSave = System.getProperties().getProperty("user.dir")
				+ "/filetest";

		// SAVE AND LOAD FSA
		FiniteStateAcceptor fsa = createFSA();
		File f = new File(toSave + "/fsa.jff");
		outPrintln("Before import:\n" + fsa.toString());
		XMLCodec codec = new XMLCodec();
		codec.encode(fsa, f, null);
		fsa = (FiniteStateAcceptor) codec.decode(f);
		outPrintln("After import:\n" + fsa.toString());

		// SAVE AND LOAD CF PUMPING LEMMA
		ContextFreePumpingLemma lem = createCFLemma();
		f = new File(toSave + "/CFLemma.jff");
		outPrintln("Before import:\n" + lem.toString());
		codec.encode((ContextFreePumpingLemma) lem, f, null);
		lem = (ContextFreePumpingLemma) codec.decode(f);
		outPrintln("After import:\n" + lem.toString());

		// SAVE AND LOAD REG PUMPING LEMMA
		RegularPumpingLemma reg = createRegLemma();
		f = new File(toSave + "/RegLemma.jff");
		outPrintln("Before import:\n" + reg.toString());
		codec.encode((RegularPumpingLemma) reg, f, null);
		reg = (RegularPumpingLemma) codec.decode(f);
		outPrintln("After import:\n" + reg.toString());

		// SAVE AND LOAD GRAMMAR
		Grammar g = createGrammar();
		f = new File(toSave + "/UNRgrammar.jff");
		outPrintln("Before import:\n" + g.toString());
		codec.encode(g, f, null);
		g = (Grammar) codec.decode(f);
		outPrintln("After import:\n" + g.toString());

		// SAVE AND LOAD INPUT ALPHABET
		InputAlphabet inputAlph = fsa.getInputAlphabet();
		f = new File(toSave + "/inputAlph.jff");
		outPrintln("Before import:\n" + inputAlph.toString());
		codec.encode(inputAlph, f, null);
		inputAlph = (InputAlphabet) codec.decode(f);
		outPrintln("After import:\n" + inputAlph.toString());

		// SAVE AND LOAD REGEX
		RegularExpression regex = createRegex();
		f = new File(toSave + "/regEx.jff");
		outPrintln("Before import:\n" + regex.toString());
		codec.encode(regex, f, null);
		regex = (RegularExpression) codec.decode(f);
		outPrintln("After import:\n" + regex.toString());

		// SAVE AND LOAD TM
		MultiTapeTuringMachine tm = createTuringMachine();
		f = new File(toSave + "/tm_AnBnCn.jff");
		outPrintln("Before import:\n" + tm.toString());
		codec.encode(tm, f, null);
		tm = (MultiTapeTuringMachine) codec.decode(f);
		outPrintln("After import:\n" + tm.toString());

		// SAVE AND LOAD BLOCK TM
		BlockTuringMachine blockTM = createBlockTM();
		doSaveAndImport(blockTM, toSave + "/blockTM_unaryAdd.jff");

		// SAVE AND LOAD MOORE
		MealyMachine mealy = createMealyMachine();
		doSaveAndImport(mealy, toSave + "/mealyTest.jff");

		// SAVE AND LOAD MEALY
		MooreMachine moore = createMooreMachine();
		doSaveAndImport(moore, toSave + "/mooreTest.jff");

		// SAVE AND LOAD MEALY
		fsa = createFSA2();
		doSaveAndImport(fsa, toSave + "/fsa2.jff");

	}

	public File doSaveAndImport(Object o, String filename) {
		File f = new File(filename);
		outPrintln("Before import:\n" + o.toString());
		XMLCodec codec = new XMLCodec();
		codec.encode(o, f, null);
		o = codec.decode(f);
		outPrintln("After import:\n" + o.toString());
		return f;
	}

	private ContextFreePumpingLemma createCFLemma() {
		ContextFreePumpingLemma lem = new AiBjCk();
		lem.setFirstPlayer(PumpingLemma.HUMAN);
		lem.setM(3);
		lem.setDecomposition(new int[] { 2, 1, 1, 1, 5 });
		lem.chooseI();
		return lem;
	}
	
	private RegularPumpingLemma createRegLemma() {
		RegularPumpingLemma lem = new AB2n();
		lem.setFirstPlayer(PumpingLemma.COMPUTER);
		lem.chooseM();
		lem.setW("abababab");
		lem.chooseDecomposition();
		lem.setI(2);
		if (lem.isInLang(lem.createPumpedString()))
        	lem.addAttempt(lem.getDecompositionAsString()+"; I = "+lem.getI() + "; <i>Failed</i>");
        else
        	lem.addAttempt(lem.getDecompositionAsString()+"; I = "+lem.getI() + "; <i>Won</i>");
		return lem;
	}

	private FiniteStateAcceptor createFSA() {
		StateSet states = new StateSet();
		InputAlphabet input = new InputAlphabet();
		TransitionSet transitions = new TransitionSet();
		StartState start = new StartState();
		FinalStateSet finalStates = new FinalStateSet();

		FiniteStateAcceptor fsa = new FiniteStateAcceptor(states, input,
				transitions, start, finalStates);
		outPrintln("Testing error/definition completion printouts:");
		errPrintln(UtilFunctions.createDelimitedString(
				Arrays.asList(fsa.isComplete()), "\n")
				+ "\n");

		// for (char i = '0'; i <= '9'; i++){
		// fsa.getInputAlphabet().add(new Symbol(Character.toString(i)));
		// }

		// figure 2.18 from the linz book with minor adjustments for
		// non-determinism
		State q0 = new State("q0", 0);
		State q1 = new State("q1", 1);
		State q2 = new State("q2", 2);
		State q3 = new State("q3", 3);
		State q4 = new State("q4", 4);

		fsa.getStates().addAll(
				Arrays.asList(new State[] { q0, q1, q2, q3, q4 }));
		fsa.setStartState(q0);
		fsa.getFinalStateSet().addAll(Arrays.asList(new State[] { q2, q4 }));

		Symbol ONE = new Terminal("1");
		Symbol ZERO = new Terminal("0");

		FSATransition t0 = new FSATransition(q0, q1, new SymbolString(ZERO));
		FSATransition t1 = new FSATransition(q0, q3, new SymbolString(ONE));
		FSATransition t2 = new FSATransition(q1, q2, new SymbolString(ZERO));
		FSATransition t3 = new FSATransition(q1, q4, new SymbolString(ONE));
		FSATransition t4 = new FSATransition(q2, q1, new SymbolString(ZERO));
		FSATransition t5 = new FSATransition(q2, q4, new SymbolString(ONE));
		FSATransition t6 = new FSATransition(q3, q2, new SymbolString(ZERO));
		FSATransition t7 = new FSATransition(q3, q4, new SymbolString(ONE));
		FSATransition t8 = new FSATransition(q4, q4, new SymbolString(ONE));
		FSATransition t9 = new FSATransition(q4, q4, new SymbolString(ZERO));

		fsa.getTransitions().addAll(
				(Arrays.asList(new FSATransition[] { t0, t1, t2, t3, t4, t5,
						t6, t7, t8, t9 })));

		fsa.trimAlphabets();
		return fsa;

	}

	private FiniteStateAcceptor createFSA2() {
		StateSet states = new StateSet();
		InputAlphabet input = new InputAlphabet();
		TransitionSet transitions = new TransitionSet();
		StartState start = new StartState();
		FinalStateSet finalStates = new FinalStateSet();

		FiniteStateAcceptor fsa = new FiniteStateAcceptor(states, input,
				transitions, start, finalStates);
		outPrintln("Testing error/definition completion printouts:");
		errPrintln(UtilFunctions.createDelimitedString(
				Arrays.asList(fsa.isComplete()), "\n")
				+ "\n");

		// for (char i = '0'; i <= '9'; i++){
		// fsa.getInputAlphabet().add(new Symbol(Character.toString(i)));
		// }

		// figure 2.18 from the linz book with minor adjustments for
		// non-determinism
		State q0 = new State("q0", 0);
		State q1 = new State("q1", 1);

		fsa.getFinalStateSet().addAll(Arrays.asList(new State[] { q1 }));

		Symbol ONE = new Terminal("1");
		Symbol ZERO = new Terminal("0");

		FSATransition t0 = new FSATransition(q0, q1, new SymbolString(ZERO));
		FSATransition t1 = new FSATransition(q0, q1, new SymbolString(ONE));
		FSATransition t2 = new FSATransition(q0, q1,
				new SymbolString(ONE, ZERO));
		FSATransition t3 = new FSATransition(q0, q1, new SymbolString(ONE, ONE));
		fsa.getTransitions().addAll(
				(Arrays.asList(new FSATransition[] { t0, t1, t2, t3 })));

		return fsa;

	}

	private Grammar createGrammar() {
		TerminalAlphabet terms = new TerminalAlphabet();
		VariableAlphabet vars = new VariableAlphabet();
		ProductionSet prod = new ProductionSet();
		StartVariable var = new StartVariable();
		Grammar g = new Grammar(vars, terms, prod, var);

		Variable S = new Variable("S");
		Variable A = new Variable("A");
		Variable B = new Variable("B");
		Variable C = new Variable("C");
		Terminal a = new Terminal("a");
		Terminal b = new Terminal("b");
		Terminal c = new Terminal("c");
		Terminal d = new Terminal("d");

		// ex7.6cnf-a.jff
		// g.getVariables().addAll( S,A,B );
		// g.getTerminals().addAll(a,b,c,d);
		prod.add(new Production(S, a, b, c));
		prod.add(new Production(S, a, A, b, c));
		prod.add(new Production(new SymbolString(A, b), new SymbolString(b, A)));
		prod.add(new Production(new SymbolString(A, c), new SymbolString(B, b,
				c, c)));
		prod.add(new Production(new SymbolString(b, B), new SymbolString(B, b)));
		prod.add(new Production(new SymbolString(a, B), new SymbolString(a, a)));
		prod.add(new Production(new SymbolString(a, B), new SymbolString(a, a,
				A)));
		prod.add(new Production(new SymbolString(B, A), new SymbolString(C)));
		g.setStartVariable(S);

		return g;
	}

	private RegularExpression createRegex() {
		RegularExpression regex = new RegularExpression(new InputAlphabet());
		regex.setMode(JFLAPMode.DEFAULT);
		// set regex
		String in = "((a+b)*+c)";
		SymbolString expression = Symbolizers.symbolize(in, regex);
		regex.setTo(expression);
		outPrintln("RegEx set to " + in + ": \n" + regex.toString());
		// trim alphabets
		regex.trimAlphabets();
		return regex;
	}

	@Override
	public String getTestName() {
		return "File Test";
	}

	private MultiTapeTuringMachine createTuringMachine() {
		StateSet states = new StateSet();
		for (int i = 0; i < 5; i++) {
			states.createAndAddState();
		}

		Symbol a = new Symbol("a"), b = new Symbol("b"), c = new Symbol("c");
		TapeAlphabet tapeAlph = new TapeAlphabet();
		// tapeAlph.addAll(a, b, c);

		BlankSymbol blank = new BlankSymbol();
		Symbol square = blank.getSymbol();
		InputAlphabet inputAlph = new InputAlphabet();
		// inputAlph.addAll(a, b, c);

		TransitionSet<MultiTapeTMTransition> functions = new TransitionSet<MultiTapeTMTransition>();
		functions.add(new MultiTapeTMTransition(states.getStateWithID(0),
				states.getStateWithID(0), new Symbol[] { a, square, square },
				new Symbol[] { a, a, square }, new TuringMachineMove[] {
						TuringMachineMove.RIGHT, TuringMachineMove.RIGHT,
						TuringMachineMove.STAY }));
		functions.add(new MultiTapeTMTransition(states.getStateWithID(0),
				states.getStateWithID(1), new Symbol[] { b, square, square },
				new Symbol[] { b, square, b }, new TuringMachineMove[] {
						TuringMachineMove.RIGHT, TuringMachineMove.STAY,
						TuringMachineMove.RIGHT }));
		functions.add(new MultiTapeTMTransition(states.getStateWithID(0),
				states.getStateWithID(4),
				new Symbol[] { square, square, square }, new Symbol[] { square,
						square, square }, new TuringMachineMove[] {
						TuringMachineMove.STAY, TuringMachineMove.LEFT,
						TuringMachineMove.STAY }));
		functions.add(new MultiTapeTMTransition(states.getStateWithID(1),
				states.getStateWithID(1), new Symbol[] { b, square, square },
				new Symbol[] { b, square, b }, new TuringMachineMove[] {
						TuringMachineMove.RIGHT, TuringMachineMove.STAY,
						TuringMachineMove.RIGHT }));
		functions.add(new MultiTapeTMTransition(states.getStateWithID(1),
				states.getStateWithID(2), new Symbol[] { c, square, square },
				new Symbol[] { c, square, square }, new TuringMachineMove[] {
						TuringMachineMove.STAY, TuringMachineMove.LEFT,
						TuringMachineMove.LEFT }));
		functions.add(new MultiTapeTMTransition(states.getStateWithID(2),
				states.getStateWithID(2), new Symbol[] { c, a, b },
				new Symbol[] { c, a, b }, new TuringMachineMove[] {
						TuringMachineMove.RIGHT, TuringMachineMove.LEFT,
						TuringMachineMove.LEFT }));
		functions.add(new MultiTapeTMTransition(states.getStateWithID(2),
				states.getStateWithID(3),
				new Symbol[] { square, square, square }, new Symbol[] { square,
						square, square }, new TuringMachineMove[] {
						TuringMachineMove.STAY, TuringMachineMove.STAY,
						TuringMachineMove.RIGHT }));
		functions.add(new MultiTapeTMTransition(states.getStateWithID(4),
				states.getStateWithID(3),
				new Symbol[] { square, square, square }, new Symbol[] { square,
						square, square }, new TuringMachineMove[] {
						TuringMachineMove.STAY, TuringMachineMove.STAY,
						TuringMachineMove.STAY }));

		StartState start = new StartState(states.getStateWithID(0));
		FinalStateSet finalStates = new FinalStateSet();
		finalStates.add(states.getStateWithID(3));
		int numTapes = 3;

		MultiTapeTuringMachine tm = new MultiTapeTuringMachine(states,
				tapeAlph, blank, inputAlph, functions, start, finalStates,
				numTapes);

		return tm;
	}

	private BlockTuringMachine createBlockTM() {
		BlockSet blocks = new BlockSet();
		TapeAlphabet alph = new TapeAlphabet();
		BlankSymbol blank = new BlankSymbol();
		InputAlphabet inputAlph = new InputAlphabet();
		TransitionSet<BlockTransition> transitions = new TransitionSet<BlockTransition>();
		StartState startState = new StartState();
		FinalStateSet finalStates = new FinalStateSet();

		Symbol square = blank.getSymbol();
		Symbol ONE = new Symbol("1");
		Symbol PLUS = new Symbol("+");
		Symbol TILDE = new Symbol(JFLAPConstants.TILDE);

		int id = 0;

		Block start = new StartBlock(id++);
		startState.setState(start);
		blocks.add(start);

		Block shiftLeft = new ShiftBlock(TuringMachineMove.LEFT, alph, id++);
		Block rightToBlank = new MoveUntilBlock(TuringMachineMove.RIGHT,
				square, alph, id++);
		Block writeONE = new WriteBlock(ONE, alph, id++);
		Block leftToBlank = new MoveUntilBlock(TuringMachineMove.LEFT, square,
				alph, id++);
		Block moveRight1 = new MoveBlock(TuringMachineMove.RIGHT, alph, id++);
		Block writeBlank = new WriteBlock(square, alph, id++);
		Block moveRight2 = new MoveBlock(TuringMachineMove.RIGHT, alph, id++);
		Block halt = new HaltBlock(id);
		finalStates.add(halt);

		BlockTransition[] trans = new BlockTransition[9];

		trans[0] = new BlockTransition(start, shiftLeft, new SymbolString(ONE));
		trans[1] = new BlockTransition(shiftLeft, rightToBlank,
				new SymbolString(TILDE));
		trans[2] = new BlockTransition(rightToBlank, writeONE,
				new SymbolString(TILDE));
		trans[3] = new BlockTransition(writeONE, leftToBlank, new SymbolString(
				TILDE));
		trans[4] = new BlockTransition(leftToBlank, moveRight1,
				new SymbolString(TILDE));
		trans[5] = new BlockTransition(moveRight1, start, new SymbolString(
				TILDE));
		trans[6] = new BlockTransition(start, writeBlank,
				new SymbolString(PLUS));
		trans[7] = new BlockTransition(writeBlank, moveRight2,
				new SymbolString(TILDE));
		trans[8] = new BlockTransition(moveRight2, halt,
				new SymbolString(TILDE));

		for (Block b : new Block[] { shiftLeft, rightToBlank, writeONE,
				leftToBlank, moveRight1, writeBlank, moveRight2, halt }) {
			blocks.add(b);
		}

		for (BlockTransition t : trans) {
			transitions.add(t);
		}

		return new BlockTuringMachine(blocks, alph, blank, inputAlph,
				transitions, startState, finalStates);

	}

	private MooreMachine createMooreMachine() {
		StateSet states = new StateSet();
		InputAlphabet alph = new InputAlphabet();
		OutputAlphabet out = new OutputAlphabet();
		TransitionSet<FSATransition> transitions = new TransitionSet<FSATransition>();
		StartState start = new StartState();
		OutputFunctionSet<MooreOutputFunction> outputFunc = new OutputFunctionSet<MooreOutputFunction>();
		MooreMachine moore = new MooreMachine(states, alph, out, transitions,
				start, outputFunc);

		Symbol a = new Symbol("A");
		Symbol b = new Symbol("B");
		Symbol c = new Symbol("C");
		Symbol d = new Symbol("D");

		State s1 = new State("q1", 1);
		State s2 = new State("q2", 2);
		State s3 = new State("q3", 3);

		start.setState(s1);

		FSATransition t0 = new FSATransition(s1, s1, a);
		FSATransition t1 = new FSATransition(s1, s2, a);
		FSATransition t2 = new FSATransition(s2, s2, b);
		FSATransition t3 = new FSATransition(s2, s3, c);
		FSATransition t4 = new FSATransition(s3, s3, c);

		MooreOutputFunction o1 = new MooreOutputFunction(s1,
				new SymbolString(d));
		MooreOutputFunction o2 = new MooreOutputFunction(s2,
				new SymbolString(c));
		MooreOutputFunction o3 = new MooreOutputFunction(s3,
				new SymbolString(b));

		transitions.addAll(toCollection(t0, t1, t2, t3, t4));
		outputFunc.addAll(toCollection(o1, o2, o3));

		return moore;
	}

	private MealyMachine createMealyMachine() {
		StateSet states = new StateSet();
		InputAlphabet alph = new InputAlphabet();
		OutputAlphabet out = new OutputAlphabet();
		TransitionSet<FSATransition> transitions = new TransitionSet<FSATransition>();
		StartState start = new StartState();
		OutputFunctionSet<MealyOutputFunction> outputFunc = new OutputFunctionSet<MealyOutputFunction>();
		MealyMachine mealy = new MealyMachine(states, alph, out, transitions,
				start, outputFunc);

		Symbol a = new Symbol("A");
		Symbol b = new Symbol("B");
		Symbol c = new Symbol("C");
		Symbol d = new Symbol("D");

		State s1 = new State("q1", 1);
		State s2 = new State("q2", 2);
		State s3 = new State("q3", 3);

		start.setState(s1);

		FSATransition t0 = new FSATransition(s1, s1, a);
		FSATransition t1 = new FSATransition(s1, s2, a);
		FSATransition t2 = new FSATransition(s2, s2, b);
		FSATransition t3 = new FSATransition(s2, s3, c);
		FSATransition t4 = new FSATransition(s3, s3, c);

		MealyOutputFunction o0 = new MealyOutputFunction(t0,
				new SymbolString(d));
		MealyOutputFunction o1 = new MealyOutputFunction(t1,
				new SymbolString(d));
		MealyOutputFunction o2 = new MealyOutputFunction(t2,
				new SymbolString(c));
		MealyOutputFunction o3 = new MealyOutputFunction(t3,
				new SymbolString(b));
		MealyOutputFunction o4 = new MealyOutputFunction(t4,
				new SymbolString(b));

		transitions.addAll(toCollection(t0, t1, t2, t3, t4));
		outputFunc.addAll(toCollection(o0, o1, o2, o3, o4));

		return mealy;
	}

	private <T> Collection<T> toCollection(T... array) {
		return Arrays.asList(array);
	}

}
