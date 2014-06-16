package file.xml.graph;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import file.xml.BasicTransducer;
import file.xml.XMLHelper;

/**
 * Transducer for encoding the data (text) of each Note of an AutomatonEditorPanel to its locations on the panel.
 * 
 * @author Ian McMahon
 *
 */
public class NoteMapTransducer extends BasicTransducer<Map<Point2D, String>> {

	private PointTransducer subTrans = new PointTransducer();

	@Override
	public Map<Point2D, String> fromStructureRoot(Element root) {
		List<Element> list = XMLHelper.getChildrenWithTag(root, NOTE_TAG);
		Map<Point2D, String> map = new HashMap<Point2D, String>();

		for (int i = 0; i < list.size(); i++) {
			Element ele = list.get(i);
			Element p_ele = XMLHelper.getChildrenWithTag(ele, POINT_TAG).get(0);
			Element text_ele = XMLHelper.getChildrenWithTag(ele, VALUE_TAG).get(0);

			Point2D p = subTrans.fromStructureRoot(p_ele);
			String text = XMLHelper.containedText(text_ele);

			map.put(p, text);
		}
		return map;
	}

	@Override
	public Element toXMLTree(Document doc, Map<Point2D, String> noteMap) {
		Element root = XMLHelper.createElement(doc, getTag(), null, null);

		for (Point2D p : noteMap.keySet()) {
			String text = noteMap.get(p);

			Element note_elem = XMLHelper.createElement(doc, NOTE_TAG, null, null);
			note_elem.appendChild(XMLHelper.createElement(doc, VALUE_TAG, text,
					null));
			note_elem.appendChild(subTrans.toXMLTree(doc, p));

			root.appendChild(note_elem);
		}
		return root;
	}

	@Override
	public String getTag() {
		return NOTE_MAP_TAG;
	}

}
