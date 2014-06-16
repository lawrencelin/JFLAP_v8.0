package file.xml.jff;

import java.util.List;

import model.automata.Automaton;
import model.automata.State;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.symbols.SymbolString;
import model.symbols.symbolizer.Symbolizers;

import org.w3c.dom.Element;

import debug.JFLAPDebug;
import file.xml.XMLHelper;

public class JFFFSATransducer extends
		JFFAutomatonTransducer<Automaton<FSATransition>, FSATransition> {

	private static final String FA_TAG = "fa";
	public static final String TRANSITION_READ_NAME = "read";

	@Override
	public String getTag() {
		return FA_TAG;
	}

	@Override
	public Automaton<FSATransition> createAutomaton(Element root) {
		return new FiniteStateAcceptor();
	}

	@Override
	public FSATransition createTransition(Automaton<FSATransition> auto,
			State from, State to, Element trans_elem) {
		List<Element> read_elems = XMLHelper.getChildrenWithTag(trans_elem,
				TRANSITION_READ_NAME);
		SymbolString s;
		String symbols = XMLHelper.containedText(read_elems.get(0));
		
		if (symbols == null)
			s = new SymbolString();
		else
			s = Symbolizers.symbolize(symbols, auto);
		return new FSATransition(from, to, s);
	}

}
