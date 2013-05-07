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
package org.lexevs.cts2.admin.load;

import java.net.URI;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.valueSets.ValueSetDefinition;

import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;

/**
 * LexEVS CTS 2 Value Set Load Operation.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public interface ValueSetLoadOperation {
	
	/**
	 * Load the value set definition.
	 * 
	 * @param source location of the source file
	 * @param releaseURI Release URI the loaded contents belong to 
	 * @param loaderName Name of the loader to be used. Call getSupportedLoaderNames() 
	 * 			  to get list of loaders supported by this instance of LexEVS.
	 * @param stopOnErrors True means stop if any load error is detected. False means
	 *            attempt to load what can be loaded if recoverable errors are
	 *            encountered.
	 */
	public abstract URNVersionPair[] load(URI source, URI releaseURI, String loaderName, Boolean stopOnErrors) throws LBException;
	
	/**
	 * Load value set definition.
	 * 
	 * @param valueSetDefinition Value Set Definition object to be loaded
	 * @param releaseURI Release URI the loaded contents belong to
	 * @param stopOnErrors True means stop if any load error is detected. False means
	 *            attempt to load what can be loaded if recoverable errors are
	 *            encountered.
	 */
	public abstract String load(ValueSetDefinition valueSetDefinition, URI releaseURI, Boolean stopOnErrors) throws LBException;

	/**
	 * Returns list of Loader names supported by this LexEVS instance.
	 *  
	 * @return List of supported Loader names
	 * @throws LBException
	 */
    public List<String> getSupportedLoaderNames() throws LBException;
}