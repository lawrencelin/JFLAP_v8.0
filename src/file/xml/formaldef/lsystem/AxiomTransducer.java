package file.xml.formaldef.lsystem;

import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import view.lsystem.helperclasses.Axiom;

import debug.JFLAPDebug;

import file.xml.XMLHelper;
import file.xml.XMLTransducer;

/**
 * Transducer specific to the axiom of an LSystem. 
 * 
 * @author Ian McMahon
 *
 */
public class AxiomTransducer implements XMLTransducer<Axiom> {
	private static final String AXIOM_TAG = "axiom";

	@Override
	public Axiom fromStructureRoot(Element root) {
		List<Element> children = XMLHelper.getChildrenWithTag(root, getTag());
		String axiom = "";

		if (children != null) {
			Element axiomNode = children.get(0);

			if (axiomNode != null) {
				axiom = axiomNode.getTextContent();
			}
		}
		return new Axiom(axiom);
	}

	@Override
	public String getTag() {
		return AXIOM_TAG;
	}

	@Override
	public boolean matchesTag(String tag) {
		return AXIOM_TAG.equals(tag);
	}


	@Override
	public Element toXMLTree(Document doc, Axiom structure) {
		Element e = XMLHelper.createElement(doc, getTag(), structure, null);
		return e;
	}

}
