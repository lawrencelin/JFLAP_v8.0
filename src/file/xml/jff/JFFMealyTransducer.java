package file.xml.jff;

import java.util.List;

import org.w3c.dom.Element;

import file.xml.XMLHelper;
import model.automata.Automaton;
import model.automata.State;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.transducers.OutputFunctionSet;
import model.automata.transducers.mealy.MealyMachine;
import model.automata.transducers.mealy.MealyOutputFunction;
import model.symbols.Symbol;
import model.symbols.SymbolString;
import model.symbols.symbolizer.Symbolizers;

public class JFFMealyTransducer extends JFFFSATransducer {

    private static final String TRANSITION_OUTPUT_NAME = "transout";
    private static final String MEALY_TAG = "mealy";
    
	@Override
	public String getTag() {
		return MEALY_TAG;
	}
	
	@Override
	public Automaton<FSATransition> createAutomaton(Element root) {
		return new MealyMachine();
	}
	
	@Override
	public FSATransition createTransition(Automaton<FSATransition> auto,
			State from, State to, Element trans_elem) {
		List<Element> output_elems = XMLHelper.getChildrenWithTag(trans_elem, TRANSITION_OUTPUT_NAME);
		FSATransition trans = super.createTransition(auto, from, to, trans_elem);
		SymbolString output = new SymbolString();
		if(!output_elems.isEmpty()){
			String text = XMLHelper.containedText(output_elems.get(0));
			
			if(text != null)
				output = Symbolizers.symbolize(text, auto);
		}
		
		OutputFunctionSet<MealyOutputFunction> functions = ((MealyMachine) auto).getOutputFunctionSet();
		functions.add(new MealyOutputFunction(trans, output));
		return trans;
	}
}
