package file.xml.jff;

import java.util.List;

import model.automata.State;
import model.automata.acceptors.pda.PDATransition;
import model.automata.acceptors.pda.PushdownAutomaton;
import model.symbols.SymbolString;
import model.symbols.symbolizer.Symbolizers;

import org.w3c.dom.Element;

import file.xml.XMLHelper;

public class JFFPDATransducer extends JFFAutomatonTransducer<PushdownAutomaton, PDATransition>{

	private static final String TRANSITION_READ_NAME = "read";
	private static final String TRANSITION_POP_NAME = "pop";
	private static final String TRANSITION_PUSH_NAME = "push";
	private static final String PDA_TAG = "pda";
	
	@Override
	public String getTag() {
		return PDA_TAG;
	}

	@Override
	public PDATransition createTransition(PushdownAutomaton auto, State from,
			State to, Element trans_elem) {
		SymbolString read = new SymbolString(), pop = new SymbolString(), push = new SymbolString();
		
		List<Element> symbol_elem_list = XMLHelper.getChildrenWithTag(trans_elem, TRANSITION_READ_NAME);
		String text;
		
		if(!symbol_elem_list.isEmpty()){
			text = XMLHelper.containedText(symbol_elem_list.get(0));
			if(text != null)
				read = Symbolizers.symbolize(text, auto);
		}
		
		symbol_elem_list = XMLHelper.getChildrenWithTag(trans_elem, TRANSITION_POP_NAME);
		if(!symbol_elem_list.isEmpty()){
			text = XMLHelper.containedText(symbol_elem_list.get(0));
			if(text != null)
				pop = Symbolizers.symbolize(text, auto);
		}
		
		symbol_elem_list = XMLHelper.getChildrenWithTag(trans_elem, TRANSITION_PUSH_NAME);
		if(!symbol_elem_list.isEmpty()){
			text = XMLHelper.containedText(symbol_elem_list.get(0));
			if(text != null)
				push = Symbolizers.symbolize(text, auto);
		}
		return new PDATransition(from, to, read, pop, push);
	}

	@Override
	public PushdownAutomaton createAutomaton(Element root) {
		return new PushdownAutomaton();
	}

}
