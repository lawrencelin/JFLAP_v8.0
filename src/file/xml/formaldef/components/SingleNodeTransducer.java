package file.xml.formaldef.components;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import debug.JFLAPDebug;

import file.xml.XMLTransducer;
import file.xml.XMLHelper;

public abstract class SingleNodeTransducer<T> implements XMLTransducer<T>{

	private String myTag;

	public SingleNodeTransducer(String tag){
		myTag = tag;
	}
	
	@Override
	public String getTag() {
		return myTag;
	}
	
	@Override
	public boolean matchesTag(String tag) {
		return myTag.equals(tag);
	}
	
	@Override
	public T fromStructureRoot(Element root) {
		String text = XMLHelper.containedText(root);
		return createInstance(text);
	}

	@Override
	public Element toXMLTree(Document doc, T structure) {
		Element e = XMLHelper.createElement(doc, getTag(), extractData(structure), null);
		return e;
	}

	public abstract Object extractData(T structure);

	public abstract T createInstance(String text);


}
