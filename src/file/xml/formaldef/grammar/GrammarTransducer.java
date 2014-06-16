package file.xml.formaldef.grammar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.automata.StateSet;
import model.formaldef.FormalDefinition;
import model.formaldef.components.alphabets.Alphabet;
import model.grammar.Grammar;
import model.grammar.ProductionSet;
import model.grammar.StartVariable;
import model.grammar.TerminalAlphabet;
import model.grammar.VariableAlphabet;
import file.xml.XMLTransducer;
import file.xml.formaldef.FormalDefinitionTransducer;
import file.xml.formaldef.components.functions.productions.ProductionSetTransducer;
import file.xml.formaldef.components.functions.productions.ProductionTransducer;

public class GrammarTransducer extends FormalDefinitionTransducer<Grammar> {

	private static final String GRAMMAR_TAG = "grammar";

	@Override
	public String getTag() {
		return GRAMMAR_TAG;
	}

	@Override
	public XMLTransducer getTransducerForStructureNode(String string,
			List<Alphabet> alphs) {
		if (string.equals(PROD_SET_TAG))
			return createProductionSetTransducer(alphs);
		return null;
	}

	private ProductionSetTransducer createProductionSetTransducer(List<Alphabet> alphs) {
		VariableAlphabet vars = retrieveAlphabet(alphs, VariableAlphabet.class);
		TerminalAlphabet terms = retrieveAlphabet(alphs, TerminalAlphabet.class);
		
		ProductionTransducer trans = new ProductionTransducer(vars, terms);
		return new ProductionSetTransducer(trans);
	}

	@Override
	public void addFunctionSetsToMap(Map<Object, XMLTransducer> map,
			Grammar g) {
		ProductionSetTransducer ducer = 
				createProductionSetTransducer(new ArrayList<Alphabet>());
		ProductionSet set = g.getProductionSet();
		map.put(set, ducer);
	}

	@Override
	public Grammar buildStructure(Object[] subComp) {
		return new Grammar(retrieveTarget(VariableAlphabet.class,subComp),
				retrieveTarget(TerminalAlphabet.class,subComp),
				retrieveTarget(ProductionSet.class, subComp),
				retrieveTarget(StartVariable.class, subComp));
	}


}
