package file.xml.graph;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import file.xml.BasicTransducer;
import file.xml.XMLHelper;

/**
 * Transducer for encoding all states (by their ID number) to their given points (Point2D) as specified in a TransitionGraph.
 * @author Ian McMahon
 *
 */
public class StatePointMapTransducer extends
		BasicTransducer<Map<Integer, Point2D>> {
	
	private PointTransducer subTrans = new PointTransducer();

	@Override
	public Map<Integer, Point2D> fromStructureRoot(Element root) {
		List<Element> list = XMLHelper.getChildrenWithTag(root, STATE_POINT);
		Map<Integer, Point2D> map = new TreeMap<Integer, Point2D>();
		
		for (int i = 0; i < list.size(); i++){
			Element ele = list.get(i);
			Element id_ele = XMLHelper.getChildArray(ele, STATE_TAG).get(0);
			Element p_ele = XMLHelper.getChildArray(ele, POINT_TAG).get(0);
			
			Integer id = Integer.parseInt(XMLHelper.containedText(id_ele));
			Point2D current = subTrans.fromStructureRoot(p_ele);
			
			map.put(id, current);
		}
		return map;
	}

	@Override
	public Element toXMLTree(Document doc, Map<Integer, Point2D> structure) {
		Element root = XMLHelper.createElement(doc, STATE_POINT_MAP, null,
				null);

		for (Integer id : structure.keySet()) {
			Point2D value = structure.get(id);
			Element sPoint = XMLHelper.createElement(doc, STATE_POINT, null, null);
			
			sPoint.appendChild(XMLHelper.createElement(doc, STATE_TAG, id, null));
			sPoint.appendChild(subTrans.toXMLTree(doc, value));
			
			root.appendChild(sPoint);
		}
		return root;
	}

	@Override
	public String getTag() {
		return STATE_POINT_MAP;
	}

}
