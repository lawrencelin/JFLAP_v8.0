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





package file;

import java.io.File;
import java.io.Serializable;
import java.util.Map;


/**
 * This specifies the common interface for objects that parse documents and
 * produce a corresponding structure. Ideally the <CODE>toString</CODE> method
 * should be implemented with a brief description of the format of file this can
 * encode.
 * 
 * @author Thomas Finley
 */

public interface Encoder {
	/**
	 * Given a structure, this will attempt to write the structure to a file.
	 * This method should always return a file, or throw an
	 * {@link EncodeException} in the event of failure with a message detailing
	 * the nature of why the encoding failed.
	 * 
	 * @param structure
	 *            the structure to encode
	 * @param file
	 *            the file to save to
	 * @param parameters
	 *            implementors have the option of accepting custom parameters in
	 *            the form of a map
	 * @return the file to which the structure was written
	 * @throws EncodeException
	 *             if there was a problem writing the file
	 */
	public File encode(Object structure, File file, Map<String, Object> parameters);

}
