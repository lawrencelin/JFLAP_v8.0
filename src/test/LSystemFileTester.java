package test;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import model.automata.State;
import model.automata.StateSet;
import model.automata.TransitionSet;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.graph.TransitionGraph;
import model.symbols.Symbol;
import util.Point2DAdv;
import debug.JFLAPDebug;
import file.xml.XMLCodec;

public class LSystemFileTester {

	public static void main(String[] args){
		String toSave = System.getProperties().getProperty("user.dir")
				+ "/filetest";
		
		File f = new File(toSave + "/test.jff");
		XMLCodec codec = new XMLCodec();
		
		FiniteStateAcceptor a = new FiniteStateAcceptor();
		StateSet states = a.getStates();
		State q0 = states.createAndAddState();
		State q1 = states.createAndAddState();
		State q2 = states.createAndAddState();
		
		TransitionSet<FSATransition> transitions = a.getTransitions();
		FSATransition t0 = new FSATransition(q0, q1);
		FSATransition t1 = new FSATransition(q1, q2, new Symbol("a"));
		FSATransition t2 = new FSATransition(q2, q0);
		transitions.add(t0);
		transitions.add(t1);
		transitions.add(t2);
		TransitionGraph<FSATransition> graph = new TransitionGraph<FSATransition>(a);
		graph.moveVertex(q0, new Point(142,  210));
		graph.moveVertex(q1, new Point(237, 104));
		graph.moveVertex(q2, new Point(328, 228));
		
		f = new File(toSave + "/auto.jff");
		JFLAPDebug.print("Before import:\n" + graph.toString());
		codec.encode(graph, f, null);
		graph = (TransitionGraph<FSATransition>) codec.decode(f);
		JFLAPDebug.print("After import:\n" + graph.toString());
	}
	
	
}
