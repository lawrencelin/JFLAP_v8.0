package file.xml.jff;

import java.util.List;

import model.regex.RegularExpression;
import model.symbols.symbolizer.Symbolizers;

import org.w3c.dom.Element;

import file.FileJFLAPException;
import file.xml.XMLHelper;

public class JFFRETransducer extends JFFTransducer<RegularExpression> {

	private static final String REGULAR_EXPRESSION_TAG = "re";
	private static final String EXPRESSION_TAG = "expression";

	@Override
	public String getTag() {
		return REGULAR_EXPRESSION_TAG;
	}

	@Override
	public RegularExpression fromStructureRoot(Element root) {
		List<Element> list = XMLHelper.getChildrenWithTag(root, EXPRESSION_TAG);

		if (list.size() == 0)
			throw new FileJFLAPException("Regular expression structure has no "
					+ EXPRESSION_TAG + " tag!");
		
		String expression = XMLHelper.containedText(list.get(0));
		if (expression == null)
			expression = "";

		RegularExpression regex = new RegularExpression();
		regex.setTo(Symbolizers.symbolize(expression, regex));

		return regex;
	}

}
