package file.xml.jff;

import java.util.List;

import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.ProductionSet;
import model.grammar.Variable;
import model.symbols.symbolizer.Symbolizers;

import org.w3c.dom.Element;

import util.JFLAPConstants;
import file.xml.XMLHelper;

public class JFFGrammarTransducer extends JFFTransducer<Grammar>{

	private static final String GRAMMAR_TAG = "grammar";
	public static final String PRODUCTION_TAG = "production";
	public static final String LEFT_TAG = "left";
	public static final String RIGHT_TAG = "right";
	
	@Override
	public String getTag() {
		return GRAMMAR_TAG;
	}

	@Override
	public Grammar fromStructureRoot(Element root) {
		Grammar g = new Grammar();
		ProductionSet prods = g.getProductionSet();
		
		List<Element> list = XMLHelper.getChildrenWithTag(root,
				PRODUCTION_TAG);
		for (Element prod_elem : list) {
			Element left_elem = XMLHelper.getChildrenWithTag(prod_elem, LEFT_TAG).get(0);
			Element right_elem = XMLHelper.getChildrenWithTag(prod_elem, RIGHT_TAG).get(0);
			
			String left = XMLHelper.containedText(left_elem);
			String right = XMLHelper.containedText(right_elem);
			
			if (left == null)
				left = "";
			if (right == null)
				right = "";
			
			Production p = new Production(Symbolizers.symbolize(left, g), Symbolizers.symbolize(right, g));
			prods.add(p);
		}
		Variable s = JFLAPConstants.JFF_START_SYMBOL;
		if(g.getVariables().contains(s))
			g.setStartVariable(s);
		return g;
	}
	
}
