package file.xml.formaldef.components;

import java.util.List;

import model.formaldef.components.SetComponent;
import model.formaldef.components.SetSubComponent;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import debug.JFLAPDebug;

import util.Copyable;

import file.xml.StructureTransducer;
import file.xml.XMLHelper;
import file.xml.formaldef.components.states.StateSetTransducer;

public abstract class SetComponentTransducer<T extends SetSubComponent<T>> extends StructureTransducer<SetComponent<T>> {

	@Override
	public SetComponent<T> fromStructureRoot(Element root) {
		List<Element> list = XMLHelper.getChildrenWithTag(root,getSubNodeTag());
		SetComponent<T> comp = createEmptyComponent();
		for (int i = 0; i < list.size(); i++){
			comp.add(decodeSubNode((Element)list.get(i)));
		}
		return comp;
	}


	public abstract T decodeSubNode(Element item);

	public abstract SetComponent<T> createEmptyComponent();

	public abstract String getSubNodeTag();
	
	@Override
	public Element appendComponentsToRoot(Document doc,
			SetComponent<T> structure, Element root) {
		for (T item: structure){
			root.appendChild(createSubNode(doc, item));
		}
		return root;
	}


	public abstract Element createSubNode(Document doc, T item);


}
