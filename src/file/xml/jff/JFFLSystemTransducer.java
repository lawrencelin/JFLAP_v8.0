package file.xml.jff;

import java.util.List;

import model.formaldef.components.alphabets.Alphabet;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.ProductionSet;
import model.grammar.TerminalAlphabet;
import model.lsystem.CommandAlphabet;
import model.lsystem.LSystem;
import model.symbols.symbolizer.Symbolizers;

import org.w3c.dom.Element;

import view.lsystem.helperclasses.Axiom;
import view.lsystem.helperclasses.ParameterMap;
import file.FileJFLAPException;
import file.xml.XMLHelper;

public class JFFLSystemTransducer extends JFFTransducer<LSystem> {

	private static final String LSYSTEM_TAG = "lsystem";
	public static final String AXIOM_TAG = "axiom";
	public static final String RULE_TAG = "production";
	public static final String LEFT_TAG = "left";
	public static final String RIGHT_TAG = "right";
	public static final String PARAMETER_TAG = "parameter";
	public static final String NAME_TAG = "name";
	public static final String VALUE_TAG = "value";

	@Override
	public String getTag() {
		return LSYSTEM_TAG;
	}

	@Override
	public LSystem fromStructureRoot(Element root) {
		Axiom axiom = readAxiom(root);
		Grammar rules = readGrammar(root);
		ParameterMap parameters = readParameters(root);
		return new LSystem(axiom, rules, parameters);
	}

	private Axiom readAxiom(Element root) {
		List<Element> axiomList = XMLHelper
				.getChildrenWithTag(root, AXIOM_TAG);

		if (axiomList.size() < 1)
			throw new FileJFLAPException("No axiom specified in the document!");
		String axiom = XMLHelper.containedText(axiomList.get(0));
		if (axiom == null)
			axiom = "";
		return new Axiom(axiom);
	}

	private Grammar readGrammar(Element root) {
		Grammar g = new Grammar();
		TerminalAlphabet term = g.getTerminals();
		CommandAlphabet command = new CommandAlphabet();

		if (!term.containsAll(command))
			term.addAll(command);
		ProductionSet prods = g.getProductionSet();

		List<Element> productionList = XMLHelper.getChildrenWithTag(root,
				RULE_TAG);

		for (Element prod_elem : productionList) {
			Element left_elem = XMLHelper.getChildrenWithTag(prod_elem,
					LEFT_TAG).get(0);

			String left = XMLHelper.containedText(left_elem);
			if (left == null)
				left = "";

			List<Element> right_list = XMLHelper.getChildrenWithTag(prod_elem,
					RIGHT_TAG);
			for (Element right_elem : right_list) {

				String right = XMLHelper.containedText(right_elem);
				if (right == null)
					right = "";

				Production p = new Production(
						Symbolizers.symbolize(left, g),
						Symbolizers.symbolize(right, g));
				prods.add(p);
			}
		}
		return g;
	}

	private ParameterMap readParameters(Element document) {
		ParameterMap p = new ParameterMap();
		List<Element> list = XMLHelper.getChildrenWithTag(document, PARAMETER_TAG);
		
		for (Element parameter_elem : list) {
			Element name_elem = XMLHelper.getChildrenWithTag(parameter_elem, NAME_TAG).get(0);
			Element value_elem = XMLHelper.getChildrenWithTag(parameter_elem, VALUE_TAG).get(0);
			
			String name = XMLHelper.containedText(name_elem), 
					value = XMLHelper.containedText(value_elem);
			if (name == null)
				continue;
			if (value == null)
				value = "";
			p.put(name, value);
		}
		return p;
	}

}
