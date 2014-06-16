package file.xml.formaldef.components;

import java.util.List;

import model.symbols.SpecialSymbol;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import file.xml.StructureTransducer;
import file.xml.XMLHelper;

public abstract class SingleValueTransducer<T> extends StructureTransducer<T> {

	private static final String VALUE_TAG = "value";

	@Override
	public T fromStructureRoot(Element root) {
		List<Element> list = XMLHelper.getChildrenWithTag(root, VALUE_TAG);
		String s = null;
		if(list.size() > 0)
			s = XMLHelper.containedText(list.get(0));
		return this.createInstance(s);
	}

	public abstract T createInstance(String s);

	@Override
	public Element appendComponentsToRoot(Document doc, T structure, Element root) {
		Element e = XMLHelper.createElement(doc, VALUE_TAG, retrieveData(structure), null);
		root.appendChild(e);
		return root;
	}

	public abstract Object retrieveData(T structure);



}
