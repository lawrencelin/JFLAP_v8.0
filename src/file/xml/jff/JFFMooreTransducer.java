package file.xml.jff;

import java.util.List;

import model.automata.Automaton;
import model.automata.State;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.transducers.OutputFunctionSet;
import model.automata.transducers.moore.MooreMachine;
import model.automata.transducers.moore.MooreOutputFunction;

import org.w3c.dom.Element;

import file.xml.XMLHelper;

public class JFFMooreTransducer extends JFFFSATransducer{

    public static final String STATE_OUTPUT_NAME = "output";
    private static final String MOORE_TAG = "moore";
   
    @Override
    public String getTag() {
    	return MOORE_TAG;
    }
    
    @Override
    public Automaton<FSATransition> createAutomaton(Element root) {
    	return new MooreMachine();
    }
}
