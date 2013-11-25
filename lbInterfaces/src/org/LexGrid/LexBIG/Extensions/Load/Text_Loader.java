/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package org.LexGrid.LexBIG.Extensions.Load;

import java.net.URI;

import org.LexGrid.LexBIG.Exceptions.LBException;

/**
 * A loader for delimited text type files.
 * 
 * Text files come in one of two formats:
 * 
 * A) indented code/designation pairs B) indented code/designation/description
 * triples.
 * 
 * @author solbrigcvs
 * @version 1.0
 * @created 09-Feb-2006 10:22:07 PM
 */
public interface Text_Loader extends Loader {
    
    public static String STD_IN_URI = "std:in";
    
    public static final String name = "TextLoader";
    public static final String description = "This loader loads LexGrid Text files into the LexGrid database.";

	/**
	 * Load content from a candidate resource. This will also result in implicit
	 * generation of standard indices required by the LexBIG runtime.
	 * 
	 * An exception is raised if resources cannot be accessed or another load
	 * operation is already in progress.
	 * 
	 * @param source
	 *            URI corresponding to the text file.
	 * @param delimiter
	 *            Optional - defaults to tab the character used to delimit pair
	 *            or triple components and the nesting.
	 * @param readDoublesAsTriples
	 *            Force the converter to read a doubles file (name/description)
	 *            as a triples file (code/name/description) So it reads codes
	 *            and names instead of names and descriptions.
	 * @param stopOnErrors
	 *            True means stop if any load error is detected. False means
	 *            attempt to load what can be loaded if recoverable errors are
	 *            encountered.
	 * @param async
	 *            Flag controlling whether load occurs in the calling thread.  
	 *            If true, the load will occur in a separate asynchronous process.
	 *            If false, this method blocks until the load operation
	 *            completes or fails. Regardless of setting, the getStatus and
	 *            getLog calls are used to fetch results.
	 * @throws LBException
	 */
	public void load(URI source, Character delimiter,
			boolean readDoublesAsTriples, boolean stopOnErrors, boolean async)
			throws LBException;

	/**
	 * Validate content for a candidate resource without performing a load.
	 * 
	 * Returns without exception if validation succeeds.
	 * 
	 * @throws LBException
	 * 
	 * @param source
	 *            URI corresponding to the text file.
	 * @param delimiter
	 *            Optional - defaults to tab the character used to delimit pair
	 *            or triple components and the nesting.
	 * @param readDoublesAsTriples
	 *            Force the converter to read a doubles file (name/description)
	 *            as a triples file (code/name/description) So it reads codes
	 *            and names intead of names and descriptions
	 * @param validationLevel
	 *            Supported levels of validation include: 0 = Verify file syntax
	 *            against the indicated format.
	 * @throws LBException
	 */
	public void validate(URI source, Character delimiter,
			boolean readDoublesAsTriples, int validationLevel)
			throws LBException;

}