package file.xml.formaldef.lsystem;

import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import view.lsystem.helperclasses.Parameter;
import view.lsystem.helperclasses.ParameterName;
import view.lsystem.helperclasses.ParameterValue;

import file.xml.BasicTransducer;
import file.xml.TransducerFactory;
import file.xml.XMLHelper;
import file.xml.XMLTransducer;

/**
 * Transducer specific to a Parameter of an LSystem.
 * 
 * @author Ian McMahon
 *
 */
public class ParameterTransducer extends BasicTransducer<Parameter> {

	@Override
	public String getTag() {
		return PARAMETER_TAG;
	}

	@Override
	public Parameter fromStructureRoot(Element root) {
		List<Element> eleChildren = XMLHelper.getElementChildren(root);
		ParameterName name = new ParameterName(null);
		ParameterValue value = new ParameterValue("");
		
		for (Element e : eleChildren) {
			String tag = e.getTagName();
			
			boolean isName = tag.equals(PARAMETER_NAME_TAG);
			boolean isValue = tag.equals(VALUE_TAG);
			if(isName || isValue){
				XMLTransducer trans = TransducerFactory.getTransducerForTag(tag);
				
				if(isName)
					name = (ParameterName) trans.fromStructureRoot(root);
				else
					value = (ParameterValue) trans.fromStructureRoot(root);
				
			}
		}
		return new Parameter(name, value);
	}

	@Override
	public Element toXMLTree(Document doc, Parameter param) {

		Element root = XMLHelper.createElement(doc, getTag(), null, null);

		XMLTransducer trans = new ParameterNameTransducer();
		root.appendChild(trans.toXMLTree(doc, param.getName()));

		trans = new ParameterValueTransducer();
		root.appendChild(trans.toXMLTree(doc, param.getValue()));

		return root;
	}

}
