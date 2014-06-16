package file.xml;

import java.lang.Thread.State;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import model.automata.InputAlphabet;
import model.automata.StartState;
import model.automata.StateSet;
import model.automata.acceptors.FinalStateSet;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.automata.acceptors.pda.BottomOfStackSymbol;
import model.automata.acceptors.pda.PushdownAutomaton;
import model.automata.acceptors.pda.StackAlphabet;
import model.automata.transducers.OutputAlphabet;
import model.automata.transducers.mealy.MealyMachine;
import model.automata.transducers.moore.MooreMachine;
import model.automata.turing.BlankSymbol;
import model.automata.turing.MultiTapeTuringMachine;
import model.automata.turing.TapeAlphabet;
import model.automata.turing.buildingblock.BlockSet;
import model.automata.turing.buildingblock.BlockTuringMachine;
import model.grammar.Grammar;
import model.grammar.StartVariable;
import model.grammar.TerminalAlphabet;
import model.grammar.VariableAlphabet;
import model.graph.BlockTMGraph;
import model.graph.TransitionGraph;
import model.lsystem.LSystem;
import model.pumping.cf.AiBjCk;
import model.pumping.cf.AkBnCnDj;
import model.pumping.cf.AnBjAnBj;
import model.pumping.cf.AnBn;
import model.pumping.cf.AnBnCn;
import model.pumping.cf.NagNbeNc;
import model.pumping.cf.W1BnW2;
import model.pumping.cf.W1CW2CW3CW4;
import model.pumping.cf.W1VVrW2;
import model.pumping.cf.WW;
import model.pumping.cf.WW1WrEquals;
import model.pumping.cf.WW1WrGrtrThanEq;
import model.pumping.reg.AB2n;
import model.pumping.reg.ABnAk;
import model.pumping.reg.AnBk;
import model.pumping.reg.AnBkCnk;
import model.pumping.reg.AnBlAk;
import model.pumping.reg.AnEven;
import model.pumping.reg.B5W;
import model.pumping.reg.B5Wmod;
import model.pumping.reg.BBABAnAn;
import model.pumping.reg.BkABnBAn;
import model.pumping.reg.NaNb;
import model.pumping.reg.Palindrome;
import model.regex.RegularExpression;
import view.lsystem.helperclasses.Axiom;
import view.lsystem.helperclasses.Parameter;
import view.lsystem.helperclasses.ParameterMap;
import view.lsystem.helperclasses.ParameterName;
import view.lsystem.helperclasses.ParameterValue;
import file.xml.formaldef.automata.BlockTMTransducer;
import file.xml.formaldef.automata.FSATransducer;
import file.xml.formaldef.automata.MealyMachineTransducer;
import file.xml.formaldef.automata.MooreMachineTransducer;
import file.xml.formaldef.automata.MultiTapeTMTransducer;
import file.xml.formaldef.automata.PDATransducer;
import file.xml.formaldef.components.alphabet.InputAlphabetTransducer;
import file.xml.formaldef.components.alphabet.OutputAlphabetTransducer;
import file.xml.formaldef.components.alphabet.StackAlphabetTransducer;
import file.xml.formaldef.components.alphabet.TapeAlphabetTransducer;
import file.xml.formaldef.components.alphabet.TerminalsTransducer;
import file.xml.formaldef.components.alphabet.VariablesTransducer;
import file.xml.formaldef.components.states.BlockSetTransducer;
import file.xml.formaldef.components.states.FinalStateSetTransducer;
import file.xml.formaldef.components.states.FromStateTransducer;
import file.xml.formaldef.components.states.StartStateTransducer;
import file.xml.formaldef.components.states.StateSetTransducer;
import file.xml.formaldef.components.states.StateTransducer;
import file.xml.formaldef.components.states.ToStateTransducer;
import file.xml.formaldef.components.symbols.BlankSymbolTransducer;
import file.xml.formaldef.components.symbols.BottomOfStackSymbolTransducer;
import file.xml.formaldef.components.symbols.StartVariableTransducer;
import file.xml.formaldef.grammar.GrammarTransducer;
import file.xml.formaldef.lsystem.AxiomTransducer;
import file.xml.formaldef.lsystem.LSystemTransducer;
import file.xml.formaldef.lsystem.ParameterMapTransducer;
import file.xml.formaldef.lsystem.ParameterNameTransducer;
import file.xml.formaldef.lsystem.ParameterTransducer;
import file.xml.formaldef.lsystem.ParameterValueTransducer;
import file.xml.formaldef.regex.RegExTransducer;
import file.xml.graph.AutomatonEditorData;
import file.xml.graph.AutomatonEditorTransducer;
import file.xml.graph.BlockTMGraphTransducer;
import file.xml.graph.TransitionGraphTransducer;
import file.xml.pumping.CFPumpingLemmaTransducer;
import file.xml.pumping.RegPumpingLemmaTransducer;

public class TransducerFactory{

	private static Map<Class, LinkedHashSet<XMLTransducer>> myClassToTransducerMap;
	
	static{
		myClassToTransducerMap = new HashMap<Class, LinkedHashSet<XMLTransducer>>();
		//FSA
		addMapping(FiniteStateAcceptor.class, new FSATransducer());
		addMapping(FinalStateSet.class, new FinalStateSetTransducer());
		addMapping(InputAlphabet.class, new InputAlphabetTransducer());
		addMapping(StateSet.class, new StateSetTransducer());
		addMapping(StartState.class, new StartStateTransducer());
		addMapping(State.class, new StateTransducer(),
								new FromStateTransducer(), 
								new ToStateTransducer());
		//PDA
		addMapping(PushdownAutomaton.class, new PDATransducer());
		addMapping(StackAlphabet.class, new StackAlphabetTransducer());
		addMapping(BottomOfStackSymbol.class, new BottomOfStackSymbolTransducer());
		
		//Moore and Mealy
		addMapping(MooreMachine.class, new MooreMachineTransducer());
		addMapping(MealyMachine.class, new MealyMachineTransducer());
		addMapping(OutputAlphabet.class, new OutputAlphabetTransducer());
		
		//TM 
		addMapping(MultiTapeTuringMachine.class, new MultiTapeTMTransducer());
		addMapping(BlankSymbol.class, new BlankSymbolTransducer());
		addMapping(TapeAlphabet.class, new TapeAlphabetTransducer());
		
		//TM Block stuff
		addMapping(BlockTuringMachine.class, new BlockTMTransducer());
		addMapping(BlockSet.class, new BlockSetTransducer());
		
		//Grammar
		addMapping(Grammar.class, new GrammarTransducer());
		addMapping(TerminalAlphabet.class, new TerminalsTransducer());
		addMapping(VariableAlphabet.class, new VariablesTransducer());
		addMapping(StartVariable.class, new StartVariableTransducer());
		
		//RegEx
		addMapping(RegularExpression.class, new RegExTransducer());
		
		//LSystem
		addMapping(LSystem.class, new LSystemTransducer());
		addMapping(Parameter.class, new ParameterTransducer());
		addMapping(ParameterMap.class, new ParameterMapTransducer());
		addMapping(ParameterName.class, new ParameterNameTransducer());
		addMapping(ParameterValue.class, new ParameterValueTransducer());
		addMapping(Axiom.class, new AxiomTransducer());
		
		//Context-Free Pumping Lemma
		addMapping(AiBjCk.class, new CFPumpingLemmaTransducer());
		addMapping(AkBnCnDj.class, new CFPumpingLemmaTransducer());
		addMapping(AnBjAnBj.class, new CFPumpingLemmaTransducer());
		addMapping(AnBn.class, new CFPumpingLemmaTransducer());
		addMapping(AnBnCn.class, new CFPumpingLemmaTransducer());
		addMapping(NagNbeNc.class, new CFPumpingLemmaTransducer());
		addMapping(W1BnW2.class, new CFPumpingLemmaTransducer());
		addMapping(W1CW2CW3CW4.class, new CFPumpingLemmaTransducer());
		addMapping(W1VVrW2.class, new CFPumpingLemmaTransducer());
		addMapping(WW.class, new CFPumpingLemmaTransducer());
		addMapping(WW1WrEquals.class, new CFPumpingLemmaTransducer());
		addMapping(WW1WrGrtrThanEq.class, new CFPumpingLemmaTransducer());
		
		//Regular Pumping Lemma
		addMapping(AB2n.class, new RegPumpingLemmaTransducer());
		addMapping(ABnAk.class, new RegPumpingLemmaTransducer());
		addMapping(AnBk.class, new RegPumpingLemmaTransducer());
		addMapping(AnBkCnk.class, new RegPumpingLemmaTransducer());
		addMapping(AnBlAk.class, new RegPumpingLemmaTransducer());
		addMapping(model.pumping.reg.AnBn.class, new RegPumpingLemmaTransducer());
		addMapping(AnEven.class, new RegPumpingLemmaTransducer());
		addMapping(B5W.class, new RegPumpingLemmaTransducer());
		addMapping(B5Wmod.class, new RegPumpingLemmaTransducer());
		addMapping(BBABAnAn.class, new RegPumpingLemmaTransducer());
		addMapping(BkABnBAn.class, new RegPumpingLemmaTransducer());
		addMapping(NaNb.class, new RegPumpingLemmaTransducer());
		addMapping(Palindrome.class, new RegPumpingLemmaTransducer());
		
		addMapping(AutomatonEditorData.class, new AutomatonEditorTransducer());
		addMapping(BlockTMGraph.class, new BlockTMGraphTransducer());
		addMapping(TransitionGraph.class, new TransitionGraphTransducer());
	}

	public static void addMapping(Class c, XMLTransducer ... struct) {
		myClassToTransducerMap.put(c,
				new LinkedHashSet<XMLTransducer>(Arrays.asList(struct)));
	}
	
	public static <T> XMLTransducer<T> getTransducerForStructure(T object){
		LinkedHashSet<XMLTransducer> set = myClassToTransducerMap.get(object.getClass());
		if (set == null) return null;
		return (XMLTransducer<T>) set.toArray()[0];
	}
	
	public static XMLTransducer getTransducerForTag(String tag){
		for (LinkedHashSet<XMLTransducer> set: myClassToTransducerMap.values())
		{
			for (XMLTransducer trans:set){
				if (trans.matchesTag(tag))
					return trans;
			}
		}
		return null;
	}
	

}
