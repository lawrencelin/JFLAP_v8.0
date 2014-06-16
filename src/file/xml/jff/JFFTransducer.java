package file.xml.jff;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import file.xml.StructureTransducer;

public abstract class JFFTransducer<T> extends StructureTransducer<T> {

	@Override
	public Element appendComponentsToRoot(Document doc, T structure,
			Element root) {
		// This should never be called
		return null;
	}

}
