/*
 *  JFLAP - Formal Languages and Automata Package
 * 
 * 
 *  Susan H. Rodger
 *  Computer Science Department
 *  Duke University
 *  August 27, 2009

 *  Copyright (c) 2002-2009
 *  All rights reserved.

 *  JFLAP is open source software. Please see the LICENSE for terms.
 *
 */





package file.xml;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import debug.JFLAPDebug;


/**
 * This is an class for objects that serve as a go between from DOM to a
 * JFLAP object representing a structure (such as an automaton or grammar), and
 * back again.
 * 
 * @author Thomas Finley
 */

public abstract class StructureTransducer<T> extends BasicTransducer<T> {



	/* (non-Javadoc)
	 * @see file.xml.Transducer#fromStructureRoot(org.w3c.dom.Element, org.w3c.dom.Document)
	 */
	@Override
	public abstract T fromStructureRoot(Element root);

	/* (non-Javadoc)
	 * @see file.xml.Transducer#toXMLTree(org.w3c.dom.Document, T)
	 */
	@Override
	public Element toXMLTree(Document doc, T structure){
		Element root = creatRoot(doc, structure);
		return appendComponentsToRoot(doc, structure, root);
	}

	public abstract Element appendComponentsToRoot(Document doc, T structure, Element root);

	
	@Override
	public boolean matchesTag(String tag) {
		return getTag().equals(tag);
	}
	
	public Element creatRoot(Document doc, T structure) {
		
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put(STRUCTURE_TYPE_ATTR, getTag());
		// Create and add the <structure> element.
		Element structureElement = XMLHelper.createElement(doc, STRUCTURE_TAG, null,
				attributes);
		return structureElement;
	}
	
	
	public static String retrieveTypeTag(Element struct) {
		// Check for the type tag.
		String tag = struct.getAttribute(STRUCTURE_TYPE_ATTR);

		return tag;
	}
	
	/**
	 * Given a DOM document, this will return an appropriate instance of a
	 * transducer for the type of document. Note that the type of the structure
	 * should be specified with in the "type" tags.
	 * 
	 * @param document
	 *            the document to get the transducer for
	 * @return the correct transducer for this document
	 * @throws IllegalArgumentException
	 *             if the document does not map to a transducer, or if it does
	 *             not contain a "type" tag at all
	 */
	public static StructureTransducer getStructureTransducer(Element root) {
		String tag = retrieveTypeTag(root);
		return (StructureTransducer) TransducerFactory.getTransducerForTag(tag);
		
	}
	
	public static StructureTransducer getJFFStructureTransducer(Element root) {
		Element tag_elem = XMLHelper.getChildrenWithTag(root, STRUCTURE_TYPE_ATTR).get(0);
		String tag = XMLHelper.containedText(tag_elem);
		return (StructureTransducer) JFFTransducerFactory.getTransducerForTag(tag);
		
	}
}
