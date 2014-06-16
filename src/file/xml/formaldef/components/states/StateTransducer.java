package file.xml.formaldef.components.states;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import model.automata.State;
import file.xml.BasicTransducer;
import file.xml.XMLHelper;
import file.xml.XMLTransducer;

public class StateTransducer extends BasicTransducer<State> {

	@Override
	public State fromStructureRoot(Element root) {
		Element id_ele = XMLHelper.getChildrenWithTag(root, ID_TAG).get(0);
		Element name_ele = XMLHelper.getChildrenWithTag(root, NAME_TAG).get(0);
		
		int id = Integer.valueOf(XMLHelper.containedText(id_ele));
		String name = XMLHelper.containedText(name_ele);
		return new State(name,id);
	}

	@Override
	public Element toXMLTree(Document doc, State item) {
		Element parent = XMLHelper.createElement(doc, getTag(), null, null);
		Element id = XMLHelper.createElement(doc, ID_TAG, item.getID(), null);
		Element name = XMLHelper.createElement(doc, NAME_TAG, item.getName(), null);
		parent.appendChild(name);
		parent.appendChild(id);
		return parent;
	}

	@Override
	public String getTag() {
		return STATE_TAG;
	}

}
