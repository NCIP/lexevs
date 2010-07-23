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
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.codingSchemes.CodingScheme;

import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;

public interface CodeSystemLoadOperation {
	
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
	 * @return URN and Version of the loaded code system
	 * @throws LBException
	 */
	public abstract URNVersionPair[] load(URI source, URI metadata, URI manifest, String loaderName, Boolean stopOnErrors, Boolean async, Boolean overwriteMetadata, String versionTag, Boolean activate) throws LBException;
	
	/**
	 * Installs a code system (aka terminology) into the terminology service 
	 * for subsequent access by other service functions. This operation is used 
	 * for the initial install of the overall terminology structure itself. 
	 * This may include the full set of concepts, relationships and so on, or 
	 * some of these elements may be loaded using the Import Code System Revision 
	 * operation. 
	 * 
	 * @param codeSystem
	 * 				code system object to be loaded into the terminology service.
	 * @param metadata
	 *				(Optional) URI of the XML file containing custom code system meta data.
	 * 				loads additional data to be maintained and queried as
	 * 				terminology meta-information within the system.
	 * 				All tags and values are interpreted as simple text-based key/value
	 * 				pairs.
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
	 * @param versionTag 
	 * 				The tag (e.g "devel", "production", ...) to be set for the this code system
	 * @param activate 
	 * 				True: activates the code system after the load.
	 * @return URN and Version of the loaded code system
	 * @throws LBException
	 */
	public abstract URNVersionPair[] load(CodingScheme codeSystem, URI metadata, Boolean stopOnErrors, Boolean async, Boolean overwriteMetadata, String versionTag, Boolean activate) throws LBException;

	/**
	 * Loads custom code system meta data. This is to load additional data to be maintained and queried as
	 * terminology meta-information within the system.
	 * 
	 * @param codeSystemNameOrURI
	 * 				Code system name or URI.
	 * @param codeSystemVersionOrTag
	 * 				Code system version or tag.
	 * @param metadata
	 * 				URI of the XML file containing custom code system meta data.
	 * 				loads additional data to be maintained and queried as
	 * 				terminology meta-information within the system.
	 * 				All tags and values are interpreted as simple text-based key/value
	 * 				pairs.
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
	 * @return URN and Version of the code system
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
	
	/**
	 * Activates the loaded code system version. Only activated code system version will be available
	 * for access by other other terminology service functions.
	 * 
	 * @param codeSystemURI
	 * 				URI corresponding to the code system.
	 * @param codeSyatemVersion
	 * 				version of the code system.
	 * @return true if activated
	 * @throws LBException
	 */
	public boolean activateCodeSystem(String codeSystemURI, String codeSyatemVersion) throws LBException;
	
	/**
	 * Deactivates the loaded code system version. Deactivated code system version will not be available
	 * for access by other other terminology service functions.
	 * 
	 * @param codeSystemURI
	 * 				URI corresponding to the code system.
	 * @param codeSyatemVersion
	 * 				version of the code system.
	 * @return true if deactivated
	 * @throws LBException
	 */
	public boolean deactivateCodeSystem(String codeSystemURI, String codeSyatemVersion) throws LBException;
	
	public List<String> getSupportedLoaderNames() throws LBException;
}
