package test;

import java.util.Arrays;

import util.UtilFunctions;

import debug.JFLAPDebug;

import model.algorithms.conversion.autotogram.PDAtoCFGConverter;
import model.algorithms.conversion.gramtoauto.CFGtoPDAConverterLL;
import model.algorithms.conversion.gramtoauto.CFGtoPDAConverterLR;
import model.algorithms.steppable.SteppableAlgorithm;
import model.algorithms.testinput.parse.brute.RestrictedBruteParser;
import model.algorithms.testinput.simulate.AutoSimulator;
import model.algorithms.testinput.simulate.SingleInputSimulator;
import model.algorithms.transform.grammar.GrammarTransformAlgorithm;
import model.algorithms.transform.grammar.UselessProductionRemover;
import model.automata.InputAlphabet;
import model.automata.StartState;
import model.automata.State;
import model.automata.StateSet;
import model.automata.TransitionSet;
import model.automata.acceptors.FinalStateSet;
import model.automata.acceptors.pda.BottomOfStackSymbol;
import model.automata.acceptors.pda.PDATransition;
import model.automata.acceptors.pda.PushdownAutomaton;
import model.automata.acceptors.pda.StackAlphabet;
import model.grammar.Grammar;
import model.grammar.typetest.GrammarType;
import model.symbols.Symbol;
import model.symbols.SymbolString;
import model.symbols.symbolizer.Symbolizers;

public class PDATest extends TestHarness{

	@Override
	public void runTest(){
		StateSet states = new StateSet();
		InputAlphabet input = new InputAlphabet();
		StackAlphabet stack = new StackAlphabet();
		TransitionSet<PDATransition> transitions = new TransitionSet<PDATransition>();
		StartState start = new StartState();
		FinalStateSet finalStates = new FinalStateSet();
		BottomOfStackSymbol bos = new BottomOfStackSymbol();
		PushdownAutomaton pda = new PushdownAutomaton(states, 
														input, 
														stack,
														transitions, 
														start, 
														bos,
														finalStates);

		errPrintln(UtilFunctions.createDelimitedString(Arrays.asList(pda.isComplete()),"\n"));
		
		errPrintln("");
		
		for (char i = 'a'; i <= 'z'; i++){
			pda.getInputAlphabet().add(new Symbol(Character.toString(i)));
			pda.getStackAlphabet().add(new Symbol(Character.toString(i)));
		}
		
		pda.setBottomOfStackSymbol(new Symbol("z"));
		
		State q0 = new State("Z0", 0);
		State q1 = new State("Z1", 1);
		State q2 = new State("Z2", 2);
		State q3 = new State("Z3", 3);

		pda.getStates().addAll(Arrays.asList(new State[]{q0,q1,q2,q3}));
		pda.setStartState(q0);
		pda.getFinalStateSet().add(q3.copy());
		
		Symbol A = 	new Symbol("a");
		Symbol B = new Symbol("b");
		
		
		PDATransition t0 = new PDATransition(q0, q1, new SymbolString(A), 
				new SymbolString(bos.getSymbol()), new SymbolString(A,bos.getSymbol()));
		PDATransition t1 = new PDATransition(q1, q1, new SymbolString(A), new SymbolString(A), new SymbolString(A,A));
		PDATransition t2 = new PDATransition(q1, q2, new SymbolString(B), new SymbolString(A), new SymbolString());
		PDATransition t3 = new PDATransition(q2, q2, new SymbolString(B), new SymbolString(A), new SymbolString());
		PDATransition t4 = new PDATransition(q2, q3, new SymbolString(), 
				new SymbolString(bos.getSymbol()), new SymbolString());
		pda.getTransitions().addAll((Arrays.asList(new PDATransition[]{t0,t1,t2,t3,t4})));
		pda.trimAlphabets();
		outPrintln(pda.toString());
		errPrintln(UtilFunctions.createDelimitedString(Arrays.asList(pda.isComplete()),"\n"));
		
		errPrintln("");

		//lets try some stuff...
				AutoSimulator sim = new AutoSimulator(pda, SingleInputSimulator.DEFAULT);
				String in = "aabb";
				sim.beginSimulation(Symbolizers.symbolize(in, pda));
				outPrintln("Run string: " + in + "\n\t In Language? " + !sim.getNextAccept().isEmpty());

		//convert PDA to CFG
		SteppableAlgorithm converter = new PDAtoCFGConverter(pda);
		converter.stepToCompletion();
		
		Grammar CFG = ((PDAtoCFGConverter) converter).getConvertedGrammar();
		
		outPrintln(CFG.toString());
		//Now trim
		CFG.trimAlphabets();
		outPrintln("Alphabets Trimmed: \n" + CFG.toString());
		
		//test Brute Force Parsing
		RestrictedBruteParser parser = new RestrictedBruteParser(CFG);
		SymbolString sad = Symbolizers.symbolize(in, CFG);
		boolean accept = parser.quickParse(sad);
		outPrintln("Parse string: " + sad + "\n\t In Language? " + accept);
	
		
		//TYPE TEST
		outPrintln(Arrays.toString(GrammarType.getType(CFG)));
		
		//Conversion to PDA - LL
		converter = new CFGtoPDAConverterLL(CFG);
		while (converter.step() != null){
		}
		pda = ((CFGtoPDAConverterLL) converter).getConvertedAutomaton();
		
		outPrintln("LL CONVERTED:\n" + pda.toString());

		//test LL converted PDA
		sim = new AutoSimulator(pda, SingleInputSimulator.DEFAULT);
		sim.beginSimulation(Symbolizers.symbolize(in, pda));
		outPrintln("Run string: " + in + "\n\t In Language? " + !sim.getNextAccept().isEmpty());
		
		//Conversion to PDA - LR
		converter = new CFGtoPDAConverterLR(CFG);
		while (converter.step() != null){
		}
		pda = ((CFGtoPDAConverterLR) converter).getConvertedAutomaton();
		
		outPrintln("LR CONVERTED:\n" + pda.toString());
		
		//test LR converted PDA
		sim = new AutoSimulator(pda, SingleInputSimulator.DEFAULT);
		sim.beginSimulation(Symbolizers.symbolize(in, pda));
		outPrintln("Run string: " + in + "\n\t In Language? " + !sim.getNextAccept().isEmpty());
		
		//test remove symbol
		pda.getStackAlphabet().remove(new Symbol("a"));
		
		outPrintln("\'a\' removed:\n" + pda.toString());

		//test remove state
		pda.getStates().remove(pda.getStates().getStateWithID(1));
		
		outPrintln("[q1] removed:\n" + pda.toString());
		
	}

	@Override
	public String getTestName() {
		return "PDAAAAA TEST";
	}

	
}