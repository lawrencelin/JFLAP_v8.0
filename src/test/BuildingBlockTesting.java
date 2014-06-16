package test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import model.algorithms.testinput.simulate.AutoSimulator;
import model.algorithms.testinput.simulate.ConfigurationChain;
import model.algorithms.testinput.simulate.SingleInputSimulator;
import model.automata.turing.BlankSymbol;
import model.automata.turing.TapeAlphabet;
import model.automata.turing.TuringMachineMove;
import model.automata.turing.buildingblock.Block;
import model.automata.turing.buildingblock.BlockTuringMachine;
import model.automata.turing.buildingblock.library.CopyBlock;
import model.symbols.Symbol;
import model.symbols.SymbolString;
import model.symbols.symbolizer.Symbolizers;
import universe.preferences.JFLAPPreferences;
import util.JFLAPConstants;
import file.xml.XMLCodec;

public class BuildingBlockTesting extends TestHarness implements JFLAPConstants{

	
	@Override
	public void runTest() {
		TapeAlphabet alph = new TapeAlphabet();
		Symbol a= new Symbol("a");
		Symbol b = new Symbol("b");
		Symbol c = new Symbol("c");
		alph.addAll(a,b,c);

		BlankSymbol blank = new BlankSymbol();
		alph.add(blank.getSymbol());
		//
		Block block = new CopyBlock(alph, 0);
		SymbolString input = new SymbolString(a,b,c,a,a,a);
		testBlock(block, input);

		////
		//				block = new FinalBlock(alph, blank, id);
		//				testBlock(block, input);
		//		////
		//				block = new MoveBlock(TuringMachineMove.RIGHT, alph, blank, id);
		//				testBlock(block, input);
		//		////
		//		block = new MoveUntilBlock(TuringMachineMove.RIGHT, c, alph, blank, id);
		//		testBlock(block, input);

		//		block = new MoveUntilNotBlock(TuringMachineMove.RIGHT, a, alph, blank, id);
		//		testBlock(block, input);
		//
		//		block = new WriteBlock(c, alph, blank, id);
		//		testBlock(block, input);

				String toSave = System.getProperties().getProperty("user.dir")
						+ "/filetest";
				File f = new File(toSave + "/blockTM_unaryAdd.jff");
		
				BlockTuringMachine blockTM = (BlockTuringMachine) new XMLCodec().decode(f);
				outPrintln("After import:\n" + blockTM);
		//
				SymbolString s = Symbolizers.symbolize("1111+1111", blockTM);
				AutoSimulator sim1 = new AutoSimulator(blockTM, SingleInputSimulator.DEFAULT);
				sim1.beginSimulation(s);
				List<ConfigurationChain> accept1 = sim1.getNextAccept();
				outPrintln("The result of a TM on "+ s +": \n" + 
						(accept1.isEmpty() ? "failed" : trimToResult(accept1.get(0))));


		//		ArrayList<State> states = new ArrayList<State>(blockTM.getStates());
		//		blockTM.getStates().removeAll(states);
		//		outPrintln("After remove:\n" + blockTM);
		//
		//		alph = blockTM.getTapeAlphabet();
		//		Block b1 = new MoveUntilNotBlock(TuringMachineMove.RIGHT, a, alph, blank, id++);
		//		Block b2 = new WriteBlock(a, alph, blank, id++);
		//		Block start = new StartBlock(alph, blank, id++);
		//		Block halt = new FinalBlock(alph, blank, id);
		//		BlockTransition t1 = new BlockTransition(b1, b2, 
		//				new SymbolString(new Symbol(NOT), JFLAPPreferences.getTMBlankSymbol()));
		//		BlockTransition t2 = new BlockTransition(b2, b1, new SymbolString(a));
		//		BlockTransition t3 = new BlockTransition(b1, halt, new SymbolString(JFLAPPreferences.getTMBlankSymbol()));
		//		BlockTransition t4 = new BlockTransition(start, b1, new SymbolString(new Symbol(TILDE)));
		//
		//		TransitionSet<BlockTransition> transitions = blockTM.getTransitions();
		//		blockTM.getFinalStateSet().add(halt);
		//		blockTM.setStartState(start);
		//
		//		transitions.add(t1);
		//		outPrintln("Transition " + t1 + " added:\n" + blockTM);
		//		transitions.add(t2);
		//		outPrintln("Transition " + t2 + " added:\n" + blockTM);
		//		transitions.add(t3);
		//		outPrintln("Transition " + t3 + " added:\n" + blockTM);
		//		transitions.add(t4);
		//		outPrintln("Transition " + t4 + " added:\n" + blockTM);
		//
		//		blockTM.getInputAlphabet().addAll(c, b);
		//		outPrintln("Input alphabet filled:\n" + blockTM);
		//
		//		AutoSimulator sim = new AutoSimulator(blockTM, 0);
		//		SymbolString input = new SymbolString(a,a,b,a,c,c,b);
		//		sim.beginSimulation(input);
		//		List<ConfigurationChain> accept = sim.getNextAccept();
		//		outPrintln("The result of a TM on "+ input +": \n" + 
		//				(accept.isEmpty() ? "failed" : trimToResult(accept.get(0))));
		//
		//		JFLAPDebug.print();














		//
		//		outPrintln("Block TM:\n" + blockTM);
		//
		//		SingleInputSimulator sim2 = new SingleInputSimulator(blockTM,
		//				SingleInputSimulator.DEFAULT);
		//		outPrintln("Input will be: " + s);
		//		sim.beginSimulation(s);
		//		List<ConfigurationChain> accept = null;
		//		while (sim.canStep()){
		//			JFLAPDebug.print(accept  = sim.step());
		//		}
		//		outPrintln("Accept chain: " + accept.get(0));
	}

	private void testBlock(Block block, SymbolString input) {
		outPrintln("\n\nTESTING: \n" + block.toDetailedString());
		SingleInputSimulator sim = new SingleInputSimulator(block.getTuringMachine(), 0);
		sim.beginSimulation(input);
		List<ConfigurationChain> accept = new ArrayList<ConfigurationChain>();
		while(sim.canStep()){
			sim.step();
			accept = new ArrayList<ConfigurationChain>(sim.getChains());
			outPrintln("" + accept);
		}
		outPrintln("The result of a "+ block.toString() + " on " + input + ": \n" + 
				(accept.isEmpty() || accept.get(0).isReject() ? "failed" : accept.get(0).getLast().getPositionForIndex(0) + " | "
						+ trimToResult(accept.get(0))));
	}


	private SymbolString trimToResult(ConfigurationChain chain) {
		SymbolString s = chain.getLast().getStringForIndex(0);
		Symbol b = JFLAPPreferences.getTMBlankSymbol();
		//		while (s.getFirst().equals(b)){
		//			s.removeFirst();
		//		}
		//		while (s.getLast().equals(b)){
		//			s.removeLast();
		//		}
		return s;
	}


	@Override
	public String getTestName() {
		return "Building Block test";
	}

}
