package file.xml.graph;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import file.xml.BasicTransducer;
import file.xml.XMLHelper;

/**
 * Transducer for encoding edges (given as (x, y) Points where x is the from
 * State's ID and y is the to State's ID) to their actual location.
 * 
 * @author Ian McMahon
 * 
 */
public class ControlPointMapTransducer extends BasicTransducer<Map<Point, Point2D>> {

	private PointTransducer subTrans = new PointTransducer();

	@Override
	public Map<Point, Point2D> fromStructureRoot(Element root) {
		List<Element> list = XMLHelper.getChildrenWithTag(root, CTRL_POINT);
		Map<Point, Point2D> map = new HashMap<Point, Point2D>();

		for (int i = 0; i < list.size(); i++) {
			Element ele = list.get(i);
			
			Element from_ele = XMLHelper.getChildrenWithTag(ele, FROM_STATE).get(0);
			Element to_ele = XMLHelper.getChildrenWithTag(ele, TO_STATE).get(0);
			Element p_ele = XMLHelper.getChildrenWithTag(ele, POINT_TAG).get(0);

			int from = Integer.parseInt(XMLHelper.containedText(from_ele));
			int to = Integer.parseInt(XMLHelper.containedText(to_ele));
			Point2D current = subTrans.fromStructureRoot(p_ele);

			map.put(new Point(from, to), current);
		}
		return map;
	}

	@Override
	public Element toXMLTree(Document doc, Map<Point, Point2D> structure) {
		Element root = XMLHelper.createElement(doc, CTRL_POINT_MAP, null, null);

		for (Point fromTo : structure.keySet()) {
			Point2D value = structure.get(fromTo);

			Element ctrlPoint = XMLHelper.createElement(doc, CTRL_POINT, null, null);
			
			ctrlPoint.appendChild(XMLHelper.createElement(doc, FROM_STATE, fromTo.x, null));
			ctrlPoint.appendChild(XMLHelper.createElement(doc, TO_STATE, fromTo.y, null));
			ctrlPoint.appendChild(subTrans.toXMLTree(doc, value));

			root.appendChild(ctrlPoint);
		}
		return root;
	}

	@Override
	public String getTag() {
		return CTRL_POINT_MAP;
	}

}
