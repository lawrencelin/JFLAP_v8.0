package file.xml.formaldef.lsystem;

import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import view.lsystem.helperclasses.Parameter;
import view.lsystem.helperclasses.ParameterMap;
import view.lsystem.helperclasses.ParameterName;
import view.lsystem.helperclasses.ParameterValue;

import debug.JFLAPDebug;

import file.xml.XMLHelper;
import file.xml.XMLTransducer;

/**
 * Transducer specific to the Parameter Map of an LSystem.
 * 
 * @author Ian McMahon
 *
 */
public class ParameterMapTransducer implements XMLTransducer<ParameterMap>{
	private ParameterTransducer subTrans = new ParameterTransducer();
	
	
	@Override
	public String getTag() {
		return PARAMETER_MAP_TAG;
	}

	@Override
	public ParameterMap fromStructureRoot(Element root) {
		List<Element> list = XMLHelper.getChildrenWithTag(root, PARAMETER_TAG);
		ParameterMap parameters = new ParameterMap();
		
		for (int i = 0; i < list.size(); i++){
			Parameter current = subTrans.fromStructureRoot(list.get(i));
			ParameterName name = current.getName();
			ParameterValue value = current.getValue();
			
			if(name != null && name.toString() != null){
				if(value == null || value.toString() == null)
					value = new ParameterValue("");
				parameters.put(name, value);
			}
		}
		return parameters;
	}

	@Override
	public Element toXMLTree(Document doc, ParameterMap structure) {
		Element root = XMLHelper.createElement(doc, PARAMETER_MAP_TAG, null, null);
		
		for(String name : structure.keySet()){
			String value = structure.get(name);
			Parameter param = new Parameter(name, value);
			
			root.appendChild(subTrans.toXMLTree(doc, param));
		}
		return root;
	}

	@Override
	public boolean matchesTag(String tag) {
		return getTag().equals(tag);
	}

}
