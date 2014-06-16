package file.xml;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import debug.JFLAPDebug;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.automata.acceptors.pda.PushdownAutomaton;
import model.automata.transducers.mealy.MealyMachine;
import model.automata.transducers.moore.MooreMachine;
import model.automata.turing.MultiTapeTuringMachine;
import model.grammar.Grammar;
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
import file.xml.graph.AutomatonEditorData;
import file.xml.jff.JFFFSATransducer;
import file.xml.jff.JFFGrammarTransducer;
import file.xml.jff.JFFLSystemTransducer;
import file.xml.jff.JFFMealyTransducer;
import file.xml.jff.JFFMooreTransducer;
import file.xml.jff.JFFPDATransducer;
import file.xml.jff.JFFRETransducer;
import file.xml.jff.JFFTMTransducer;
import file.xml.pumping.CFPumpingLemmaTransducer;
import file.xml.pumping.RegPumpingLemmaTransducer;

public class JFFTransducerFactory {

private static Map<Class, LinkedHashSet<XMLTransducer>> myClassToTransducerMap;
	
	static{
		myClassToTransducerMap = new HashMap<Class, LinkedHashSet<XMLTransducer>>();
		//FSA
		addMapping(AutomatonEditorData.class, new JFFFSATransducer(), 
											  new JFFPDATransducer(),
											  new JFFMooreTransducer(),
											  new JFFMealyTransducer(),
											  new JFFTMTransducer());
		
//		//TM Block stuff
//		addMapping(BlockTuringMachine.class, new BlockTMTransducer());
		
		//Grammar
		addMapping(Grammar.class, new JFFGrammarTransducer());
		
		//RegEx
		addMapping(RegularExpression.class, new JFFRETransducer());
		
		//LSystem
		addMapping(LSystem.class, new JFFLSystemTransducer());
		
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
		
	}

	public static void addMapping(Class c, XMLTransducer ... struct) {
		myClassToTransducerMap.put(c,
				new LinkedHashSet<XMLTransducer>(Arrays.asList(struct)));
	}
	
	public static XMLTransducer getTransducerForTag(String tag) {
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
