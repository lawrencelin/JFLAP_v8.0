package test;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import debug.JFLAPDebug;

import file.xml.XMLCodec;
import file.xml.formaldef.components.functions.output.MooreOutputFuncTransducer;
import model.algorithms.testinput.simulate.AutoSimulator;
import model.automata.InputAlphabet;
import model.automata.StartState;
import model.automata.State;
import model.automata.StateSet;
import model.automata.TransitionSet;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.transducers.OutputAlphabet;
import model.automata.transducers.OutputFunctionSet;
import model.automata.transducers.mealy.MealyMachine;
import model.automata.transducers.moore.MooreMachine;
import model.automata.transducers.moore.MooreOutputFunction;
import model.grammar.Grammar;
import model.symbols.Symbol;
import model.symbols.SymbolString;
import model.symbols.symbolizer.Symbolizers;

public class MooreAndMealyTest extends TestHarness {

	@Override
	public void runTest() {
		
		String dir = System.getProperties().getProperty("user.dir") +"/filetest";
		File f;
		AutoSimulator sim;
		
		f = new File(dir + "/mooreTest.jff");
		MooreMachine moore = XMLCodec.decode(f, MooreMachine.class);
		
		outPrintln("Moore Machine :\n" + moore.toString());
		
		SymbolString input1 = Symbolizers.symbolize("AAAABBBBCCCC", moore);
		SymbolString input2 = Symbolizers.symbolize("AAABBBBABBBABAB", moore);

		sim = new AutoSimulator(moore, 0);
		run(sim, input1);
		run(sim, input2);
		
		f = new File(dir + "/mealyTest.jff");
		MealyMachine mealy = XMLCodec.decode(f, MealyMachine.class);
		sim = new AutoSimulator(mealy, 0);
		run(sim, input1);
		run(sim, input2);
	}


	public void run(AutoSimulator sim, SymbolString input1) {
		sim.beginSimulation(input1);
		
		outPrintln("Output for "+ input1 + ":\n" + 
				sim.getLastHalt().get(0).getLast().getStringForIndex(0));
	}


	@Override
	public String getTestName() {
		return "Transducer Test";
	}

}
