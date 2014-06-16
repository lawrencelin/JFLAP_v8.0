package file.xml.formaldef.regex;

import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import debug.JFLAPDebug;

import file.xml.XMLHelper;
import file.xml.XMLTransducer;
import file.xml.formaldef.FormalDefinitionTransducer;

import model.automata.InputAlphabet;
import model.formaldef.components.alphabets.Alphabet;
import model.regex.ExpressionComponent;
import model.regex.RegularExpression;
import model.symbols.SymbolString;

public class RegExTransducer extends FormalDefinitionTransducer<RegularExpression> {

	
	@Override
	public RegularExpression buildStructure(Object[] subComp) {
		InputAlphabet alph = retrieveTarget(InputAlphabet.class, subComp);
		ExpressionComponent exp = retrieveTarget(ExpressionComponent.class, subComp);
		return new RegularExpression(alph, exp);
	}

	@Override
	public String getTag() {
		return REGEX;
	}

	@Override
	public XMLTransducer getTransducerForStructureNode(String string,
			List<Alphabet> alphs) {
		if (string.equals(EXPRESSION_TAG))
			return new ExpressionStringTransducer((InputAlphabet) alphs.get(0));
		return null;
	}

	@Override
	public void addFunctionSetsToMap(Map<Object, XMLTransducer> map,
			RegularExpression regex) {
		map.put(regex.getComponentOfClass(ExpressionComponent.class), 
							new ExpressionStringTransducer(null));
	}


}
