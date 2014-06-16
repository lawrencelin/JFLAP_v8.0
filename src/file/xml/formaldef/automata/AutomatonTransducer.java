package file.xml.formaldef.automata;

import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;

import model.automata.Automaton;
import model.automata.StateSet;
import model.automata.TransitionSet;
import model.formaldef.FormalDefinition;
import model.formaldef.components.alphabets.Alphabet;
import file.xml.XMLTransducer;
import file.xml.TransducerFactory;
import file.xml.XMLHelper;
import file.xml.formaldef.FormalDefinitionTransducer;
import file.xml.formaldef.components.functions.FunctionSetTransducer;
import file.xml.formaldef.components.functions.transitions.TransitionSetTransducer;

public abstract class AutomatonTransducer<T extends Automaton> extends FormalDefinitionTransducer<T> {

	
	@Override
	public XMLTransducer getTransducerForStructureNode(String s,
			List<Alphabet> alphs) {
		if (s.equals(TRANS_SET)){
			return createTransitionTransducer(alphs);
		}
		return null;
	}

	@Override
	public void addFunctionSetsToMap(Map<Object, XMLTransducer> map, T structure) {
		XMLTransducer func = createTransitionTransducer(null);
		map.put(structure.getTransitions(), func);
	}

	public abstract TransitionSetTransducer createTransitionTransducer(List<Alphabet> alphs);

}
