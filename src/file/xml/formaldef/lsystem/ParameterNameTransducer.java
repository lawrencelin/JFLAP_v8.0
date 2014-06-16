package file.xml.formaldef.lsystem;

import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import view.lsystem.helperclasses.ParameterName;

import debug.JFLAPDebug;

import file.xml.XMLHelper;
import file.xml.XMLTransducer;

/**
 * Transducer specific to the Parameter names of an LSystem.
 * 
 * @author Ian McMahon
 *
 */
public class ParameterNameTransducer implements XMLTransducer<ParameterName>{

	@Override
	public ParameterName fromStructureRoot(Element root) {
		List<Element> children = XMLHelper.getChildrenWithTag(root, getTag());
		String newName = null;
		
		if(children != null){
			Element name = children.get(0);
			if(name != null){
				newName = name.getTextContent();
			}
		}
		return new ParameterName(newName);
	}

	@Override
	public Element toXMLTree(Document doc, ParameterName structure) {
		Element e = XMLHelper.createElement(doc, getTag(), structure, null);
		return e;
	}

	@Override
	public String getTag() {
		return PARAMETER_NAME_TAG;
	}

	@Override
	public boolean matchesTag(String tag) {
		return tag == null ? false : tag.equals(getTag());
	}

}
