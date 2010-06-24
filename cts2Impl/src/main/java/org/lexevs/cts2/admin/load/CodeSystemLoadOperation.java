/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and Research
 * (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the triple-shield Mayo
 * logo are trademarks and service marks of MFMER.
 * 
 * Except as contained in the copyright notice above, or as used to identify
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License"); you
 * may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 * 
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.lexevs.cts2.admin.load;

import java.net.URI;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Load.Loader;
import org.LexGrid.codingSchemes.CodingScheme;

import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;

public interface CodeSystemLoadOperation extends Loader{
	
	/**
	 * Installs a code system (aka terminology) into the terminology service 
	 * for subsequent access by other service functions. This operation is used 
	 * for the initial install of the overall terminology structure itself. 
	 * This may include the full set of concepts, relationships and so on, or 
	 * some of these elements may be loaded using the Import Code System Revision 
	 * operation. 
	 * 
	 * @param source 
	 * 				URI corresponding to the code system file.
	 * @param metadata 
	 * 				(Optional) URI of the XML file containing custom code system meta data.
	 * 				loads additional data to be maintained and queried as
	 * 				terminology meta-information within the system.
	 * 				All tags and values are interpreted as simple text-based key/value
	 * 				pairs.
	 * @param manifest 
	 * 				(Optional) URI corresponding to the manifest file. 
	 * 				The LexGrid Manifest accommodates the need to supplement or
	 * 				override default information provided by the source.  More specifically,
	 * 				the manifest provides a means to customize the same code system metadata
	 * 				defined by the LexGrid model, since each element of the manifest extends
	 * 				directly from an element used to define the LexGrid coding scheme(aka code system) object.  
	 * 				Each extended element allows for the administrator to specify whether
	 * 				the manifest definition replaces or supplements original values provided
	 * 				in the terminology source.  Like the LexGrid Terminology model, the manifest
	 * 				is defined by a formal model mastered as XML Schema.
	 * @param releaseURI
	 * @param loaderName
	 * 				Loader to use for loading the code system. 
	 * 				LexEvsCTS2.getSupportedLoaders method returns all the loaders supported 
	 * 				by the service.
	 * 				For example, 'OBOLoader' could be used to load code system source that is in OBO format,
	 * 				'OWLLoader' for code system source in OWL format, etc.
	 * @param stopOnErrors
	 * 				True means stop if any load error is detected. False means
	 *            attempt to load what can be loaded if recoverable errors are
	 *            encountered.
	 * @param async
	 * 				Flag controlling whether load occurs in the calling thread.  
	 *            If true, the load will occur in a separate asynchronous process.
	 *            If false, this method blocks until the load operation
	 *            completes or fails. Regardless of setting, the getStatus and
	 *            getLog calls are used to fetch results.
	 * @param overwriteMetadata
	 * 				If true, existing meta data for the code system will be erased.
     *            If false, new meta data will be appended to existing meta data.
	 * @param versionTag - 
	 * 				The tag (e.g "devel", "production", ...) to be set for the this code system
	 * @param activate 
	 * 				True: activates the code system after the load.
	 */
	public abstract URNVersionPair[] load(URI source, URI metadata, URI manifest, URI releaseURI, String loaderName, Boolean stopOnErrors, Boolean async, Boolean overwriteMetadata, String versionTag, Boolean activate) throws LBException;
	
	/**
	 * Load the content and meta data.  If the async flag is true, return an URI that
	 * gives access to the load progress
	 * 
	 * @param codeSystem
	 * @param metadata
	 * @param manifest
	 * @param stopOnErrors
	 * @param async
	 * @param overwriteMetadata
	 * @param versionTag - the tag (e.g "devel", "production", ...) to be set for the this code system
	 * @param activate - True: activates the code system after the load.
	 */
	public abstract URNVersionPair[] load(CodingScheme codeSystem, URI metadata, URI manifest, Boolean stopOnErrors, Boolean async, Boolean overwriteMetadata, String versionTag, Boolean activate) throws LBException;

	/**
	 * Loads code system metadata.
	 * 
	 * @param codeSystemNameOrURI
	 * @param codeSystemVersionOrTag
	 * @param metadata
	 * @param stopOnErrors
	 * @param async
	 * @param overwriteMetadata
	 * @return
	 * @throws LBException
	 */
	public URNVersionPair applyMetadataToCodeSystem(String codeSystemNameOrURI, CodingSchemeVersionOrTag codeSystemVersionOrTag, URI metadata, Boolean stopOnErrors, Boolean async, Boolean overwriteMetadata) throws LBException;
	
	/**
	 * Installs either an entire new version or the necessary revision updates 
	 * for an already loaded code system (terminology) into the terminology server 
	 * repository (content included by value or by reference to a location). 
	 * Includes indicator as to whether intent is to replace whole code system or 
	 * just replace some elements (codes, associations etc).
	 * 
	 * @return
	 * @throws LBException
	 */
	public URNVersionPair loadCodeSystemRevsion() throws LBException;
	
	public boolean activateCodeSystem(String codeSystemURI, String codeSyatemVersion) throws LBException;
	
	public boolean deactivateCodeSystem(String codeSystemURI, String codeSyatemVersion) throws LBException;
	
	/**
	 * Validate resource without performing a load.
	 * 
	 * Returns without exception if validation succeeds.
	 *  
	 * @param source
	 *            URI corresponding to the source code system file.
	 * @param metatData
	 *            URI corresponding to the source meta data file.
	 * @param loaderName
	 * 				Loader to use for loading the code system. 
	 * 				LexEvsCTS2.getSupportedLoaders method returns all the loaders supported 
	 * 				by the service.
	 * 				For example, 'OBOLoader' could be used to load code system source that is in OBO format,
	 * 				'OWLLoader' for code system source in OWL format, etc.
	 * @param validationLevel
	 *            Supported levels of validation include: 0 = Verify top 10
	 *            lines are correct format. 1 = Verify entire file.
	 * @throws LBException
	 */
	public void validate(URI source, URI metaData, String loaderName, int validationLevel)
			throws LBException;
}
