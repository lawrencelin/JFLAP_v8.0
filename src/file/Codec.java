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

import javax.swing.filechooser.FileFilter;




/**
 * This object is both an encoder and decoder, and is useful as a file filter.
 */

public abstract class Codec extends FileFilter implements Encoder, Decoder {

	/**
	 * Returns an instance of a corresponding encoder. In many cases the
	 * returned will be <CODE>this</CODE>.
	 * 
	 * @return an encoder that encodes in the same format this decodes in, or
	 *         <CODE>null</CODE> if there is no such encoder
	 */
	public Encoder correspondingEncoder() {
		return this;
	}

}
