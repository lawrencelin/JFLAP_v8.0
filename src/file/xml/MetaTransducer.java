package file.xml;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import debug.JFLAPDebug;

public abstract class MetaTransducer<T> extends StructureTransducer<T> {

	@Override
	public T fromStructureRoot(Element root) {
		Object[] comps = toSubStructureList(root);
		return buildStructure(comps);
		
	}

	public Object[] toSubStructureList(Element root) {
		List<Element> list = XMLHelper.getChildArray(root, STRUCTURE_TAG);
		List<Object> subComp = new ArrayList<Object>();
		for(Element child: list){
			XMLTransducer t = 
					StructureTransducer.getStructureTransducer(child);
			subComp.add(t.fromStructureRoot(child));
		}
		return subComp.toArray();
	}

	public abstract T buildStructure(Object[] subComp);

	@Override
	public Element appendComponentsToRoot(Document doc, T structure, Element root) {
		Map<Object, XMLTransducer> transMap = createTransducerMap(structure);

		for(Entry<Object, XMLTransducer> e: transMap.entrySet()){
			root.appendChild(e.getValue().toXMLTree(doc, e.getKey()));
		}
		return root;
	};
	
	public Map<Object, XMLTransducer> createTransducerMap(T structure) {
		Map<Object, XMLTransducer> map = new HashMap<Object, XMLTransducer>();
		for (Object o: getConstituentComponents(structure)){
			XMLTransducer t = TransducerFactory.getTransducerForStructure(o);
			map.put(o, t);
		}
		return map;
	}

	public abstract Object[] getConstituentComponents(T structure);
	
	public static <T> T retrieveTarget(Class<T> target, Object ... population){
		for (Object o: population){
			if (target.equals(o.getClass()))
				return (T) o;
		}
		return null;
	}
	
}
