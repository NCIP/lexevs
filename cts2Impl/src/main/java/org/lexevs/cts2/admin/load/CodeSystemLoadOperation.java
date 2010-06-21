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
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Load.Loader;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.versions.Revision;

import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;

public interface CodeSystemLoadOperation extends Loader{
	
	/**
	 * Load the content and meta data.  If the async flag is true, return an URI that
	 * gives access to the load progress
	 * 
	 * @param source
	 * @param metadata
	 * @param manifest
	 * @param releaseURI
	 * @param loaderName
	 * @param stopOnErrors
	 * @param async
	 * @param overwriteMetadata
	 * @param versionTag - the tag (e.g "devel", "production", ...) to be set for the this code system
	 * @param activate - True: activates the code system after the load.
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
	 * Installs a code system (aka terminology) into the terminology service 
	 * for subsequent access by other service functions. This operation is used 
	 * for the initial install of the overall terminology structure itself. 
	 * This may include the full set of concepts, relationships and so on, or 
	 * some of these elements may be loaded using the Import Code System Revision 
	 * operation. The actual contents may be supplied by value or reference, 
	 * i.e. as a complete set of explicit content or as a reference to a location 
	 * where the content can be separately obtained for loading. 
	 * @return
	 * @throws 
	 */
	public int importCodeSystem() throws LBException;
	
	/**
	 * Installs either an entire new version or the necessary revision updates 
	 * for an already loaded code system (terminology) into the terminology server 
	 * repository (content included by value or by reference to a location). 
	 * Includes indicator as to whether intent is to replace whole code system or 
	 * just replace some elements (codes, associations etc).
	 * 
	 * @param codeSystemRevision
	 * @return
	 * @throws LBException
	 */
	public int importCodeSystemRevsion(Revision codeSystemRevision) throws LBException;
	
	public int importCodeSystemRevsion(String xmlFileLocation) throws LBException;
	
	public void changeCodeSystemStatus()throws LBException;	
	
	/**
	 * Validate resource without performing a load.
	 * 
	 * Returns without exception if validation succeeds.
	 *  
	 * @param source
	 *            URI corresponding to the source code system file.
	 * @param metatData
	 *            URI corresponding to the source meta data file.
	 * @param validationLevel
	 *            Supported levels of validation include: 0 = Verify top 10
	 *            lines are correct format. 1 = Verify entire file.
	 * @throws LBException
	 */
	public void validate(URI source, URI metaData, int validationLevel)
			throws LBException;	
}
