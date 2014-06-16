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


import java.io.*;
import java.util.Map;

import javax.swing.filechooser.FileFilter;
import javax.xml.parsers.*;



import model.automata.transducers.moore.MooreMachine;
import model.grammar.Grammar;

import org.w3c.dom.*;

import util.JFLAPConstants;

import debug.JFLAPDebug;

import file.Codec;
import file.DataException;
import file.EncodeException;
import file.FileJFLAPException;
import file.FileParseException;
import file.xml.*;



import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * This is the codec for reading and writing JFLAP structures as XML documents.
 * 
 * @author Thomas Finley, Henry Qin
 */

public class XMLCodec extends Codec {

	/**
	 * Determines which files this FileFilter will allow. We are only allowing files with extension XML jff or jflap.
	 * 
	 */
	@Override
	public boolean accept(File f){
		if (f.isDirectory()) return true;
		boolean b = false;
		for (String s: new String[]{JFLAPConstants.JFF_SUFFIX, JFLAPConstants.JFLAP_SUFFIX, ".jdef"})
			b = (b || f.getName().endsWith(s));
		return true;
	} 

	/**
	 * Given a file, this will return a JFLAP structure associated with that
	 * file.
	 * 
	 * @param file
	 *            the file to decode into a structure
	 * @return a JFLAP structure resulting from the interpretation of the file
	 * @throws FileParseException
	 *             if there was a problem reading the file
	 */
	@Override
	public Object decode(File file) {
		try {
			Document doc = XMLHelper.parse(file);
			XMLTransducer transducer;
			
			if(file.getName().endsWith(JFLAPConstants.JFF_SUFFIX)){
				transducer = getJFFRootTransducer(doc.getDocumentElement());
			}
			else
				transducer = getRootTransducer(doc.getDocumentElement());
			return transducer.fromStructureRoot(doc.getDocumentElement());
		} catch (Exception e) {
			if(e instanceof IOException)
				throw new FileJFLAPException("Could not open file to read!");
			if(e instanceof NullPointerException){
				e.printStackTrace();
				throw new FileJFLAPException("File is missing necessary values!");
			}
			e.printStackTrace();
			throw new FileJFLAPException("Could not parse XML!\n" + e.getMessage());
		} catch (ExceptionInInitializerError e) {
			// Hmm. That shouldn't be.
			System.err.println("STATIC INIT:");
			e.getException().printStackTrace();
			throw new FileParseException("Unexpected Error!");
		}
	}





	/**
	 * Given a structure, this will attempt to write the structure as a
	 * serialized object to a file.
	 * 
	 * @param structure
	 *            the structure to encode
	 * @param file
	 *            the file to save the structure to
	 * @param parameters
	 *            implementors have the option of accepting custom parameters in
	 *            the form of a map
	 * @return the file to which the structure was written
	 * @throws EncodeException
	 *             if there was a problem writing the file
	 */
	@Override
	public File encode(Object structure, File file, Map parameters) {
		Document doc = createBasicJFFDoc();
		try {
			XMLTransducer transducer = 
					TransducerFactory.getTransducerForStructure(structure);

			Element dom = transducer.toXMLTree(doc, structure);
			doc.appendChild(dom);
			XMLPrettier.makePretty(doc);
			Source s = new DOMSource(dom);
			Result r = new StreamResult(file);
			Transformer t = TransformerFactory.newInstance().newTransformer();
			t.transform(s, r);
			return file;
		} catch (IllegalArgumentException e) {
			throw new EncodeException(
					"No XML transducer available for this structure!");
		} catch (TransformerConfigurationException e) {
			throw new EncodeException("Could not open file to write!");
		} catch (TransformerException e) {
			throw new EncodeException("Could not open file to write!");
		}
	}

	private Document createBasicJFFDoc() {
		Document doc = XMLHelper.newDocument();
		
		Node versionTag = XMLHelper.createComment(doc, "Created with JFLAP "
				+ JFLAPConstants.VERSION + ".");
		doc.appendChild(versionTag);
		return doc;
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
	private StructureTransducer getRootTransducer(Element root) {
		// Check for the type tag.
//		NodeList structureNodes = root.getElementsByTagName(XMLTags.STRUCTURE_TAG);
//		
//		if (structureNodes.getLength() > 1)
//			throw new DataException("Multiple type nodes \n" +
//										"exist in this structure");
//		
//		Element n = (Element) structureNodes.item(0);
		
		
		return StructureTransducer.getStructureTransducer(root);
		
	}
	
	private StructureTransducer getJFFRootTransducer(Element root) {
		return StructureTransducer.getJFFStructureTransducer(root);
	}

	public static FileFilter getSaveFileFilter(){
		return new FileFilter() {

			@Override
			public String getDescription() {
				return "JFLAP "+ JFLAPConstants.VERSION + " files ("+JFLAPConstants.JFLAP_SUFFIX+")";
			}

			@Override
			public boolean accept(File f) {
				return f.getName().endsWith(JFLAPConstants.JFLAP_SUFFIX) || f.isDirectory();

			}
		};
	}
	
	public static FileFilter getOpenFileFilter(){
		return new FileFilter() {

			@Override
			public String getDescription() {
				return "JFLAP "+ JFLAPConstants.VERSION + " files ("+JFLAPConstants.JFLAP_SUFFIX+"/"+JFLAPConstants.JFF_SUFFIX+")";
			}

			@Override
			public boolean accept(File f) {
				String name = f.getName();
				return name.endsWith(JFLAPConstants.JFF_SUFFIX) || name.endsWith(JFLAPConstants.JFLAP_SUFFIX)|| f.isDirectory();

			}
		};
	}

	@Override
	public String getDescription() {
		return "A codec for XML/.jff/.jflap format files";
	}

	public static <T> T decode(File f, Class<T> class1) {
		return (T) new XMLCodec().decode(f);
	}

}


