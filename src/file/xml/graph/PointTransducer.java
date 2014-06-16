package file.xml.graph;

import java.awt.geom.Point2D;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import util.Point2DAdv;

import file.xml.BasicTransducer;
import file.xml.XMLHelper;

/**
 * Transducer for encoding of a Point2D as an (x, y) value, both of which are doubles.
 * 
 * @author Ian McMahon
 *
 */
public class PointTransducer extends BasicTransducer<Point2D>{

	@Override
	public Point2D fromStructureRoot(Element root) {
		Element x_ele = XMLHelper.getChildrenWithTag(root, X_TAG).get(0);
		Element y_ele = XMLHelper.getChildrenWithTag(root, Y_TAG).get(0);
		
		double x = Double.valueOf(XMLHelper.containedText(x_ele));
		double y = Double.valueOf(XMLHelper.containedText(y_ele));
		return new Point2DAdv(x, y);
	}

	@Override
	public Element toXMLTree(Document doc, Point2D item) {
		Element parent = XMLHelper.createElement(doc, getTag(), null, null);
		Element x = XMLHelper.createElement(doc, X_TAG, item.getX(), null);
		Element y = XMLHelper.createElement(doc, Y_TAG, item.getY(), null);
		
		parent.appendChild(x);
		parent.appendChild(y);
		return parent;
	}

	@Override
	public String getTag() {
		return POINT_TAG;
	}

}
