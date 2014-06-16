package file.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface XMLTransducer<T> extends XMLTags{

	/**
	 * Given a document, this will return the corresponding JFLAP structure
	 * encoded in the DOM document.
	 * @param root
	 *            the DOM document to decode
	 * @return a serializable object, as all JFLAP structures are encoded in
	 *         serializable objects
	 * @throws FileParseException
	 *             in the event of an error that may lead to undesirable
	 *             functionality
	 */
	public abstract T fromStructureRoot(Element root);

	/**
	 * Given a JFLAP structure, this will return the corresponding DOM encoding
	 * of the structure.
	 * @param doc TODO
	 * @param structure
	 *            the JFLAP structure to encode
	 * 
	 * @return a DOM document instance
	 */
	public abstract Element toXMLTree(Document doc, T structure);

	/**
	 * Returns the string encoding of the type this transducer decodes and
	 * encodes.
	 * 
	 * @return the type this transducer recognizes
	 */
	public abstract String getTag();

	
	public abstract boolean matchesTag(String tag);

}