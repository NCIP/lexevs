/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
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
package org.lexgrid.valuedomain;

import java.io.InputStream;
import java.io.Serializable;
import java.net.URI;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.LogEntry;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.emf.naming.Mappings;
import org.LexGrid.emf.valueDomains.ValueDomainDefinition;
import org.LexGrid.managedobj.RemoveException;
import org.lexgrid.valuedomain.dto.ResolvedValueDomainCodedNodeSet;
import org.lexgrid.valuedomain.dto.ResolvedValueDomainDefinition;

/**
 * Value Domain extension for LexGrid.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public interface LexEVSValueDomainServices extends Serializable {
	
	
	/**
	 * Loads supplied valueDomainDefinition object
	 * @param vddef value domain to load
	 * @param systemReleaseURI
	 * @param mappings - additional mappings passed from the value domain container
	 * @throws LBException
	 */
	public void loadValueDomain(ValueDomainDefinition vddef, String systemReleaseURI, Mappings mappings) throws LBException;
	
	/**
	 * Loads value domain using inputStream
	 * @param inputStream
	 * @param failOnAllErrors
	 * @throws Exception
	 */
	public void loadValueDomain(InputStream inputStream, boolean failOnAllErrors) throws LBException;
	
	/**
	 * Loads value domain by reading XML file location supplied
	 * @param xmlFileLocation XML file containing valueDomain definitions
	 * @param failOnAllErrors
	 * @throws Exception
	 */
	public void loadValueDomain(String xmlFileLocation, boolean failOnAllErrors) throws LBException;
	
	/**
	 * Perform validation of the candidate resource without loading data.  
	 * @param uri XML file containing valueDomain definitions
	 * @param validationLevel validate &lt;int&gt; 
	 *         Supported levels of validation include:
	 *         0 = Verify document is well-formed
	 *         1 = Verify document is valid
	 * @throws LBParameterException
	 */
	public void validate(URI uri, int validationLevel) throws LBParameterException;
	
	/**
     * Determine whether the supplied entity code is a valid entity code somewhere in the supplied value domain. This function is intended for
     * use with simple value domains that are drawn from a single coding scheme where most parameters can be defaulted
	 * 
	 * @param entityCode       - the entity code to search for.  If the value domain has a default coding scheme, this will become the namespace
	 *                           for the entity code.  If not, any matching entity code will pass.
	 * @param valueDomainURI   - the URI of the value domain to search
	 * @param versionTag       - the version or tag (e.g. "devel", "production", etc.) to be used for <i>all</i> of the coding schemes searched.
	 * @return coding scheme and version if the entityCode is valid, null otherwise
	 * @throws LBException
	 */
	public AbsoluteCodingSchemeVersionReference isEntityInDomain(
	        String entityCode, URI valueDomainURI, String versionTag)  throws LBException;

	/**
     * Determine whether the supplied entity code is valid in the suppled value domain, when reconciled against the supplied
     * set of coding scheme versions and/or version tags
	 * 
	 * @param entityCode           - the entity code to validate.
	 * @param entityCodeNamespace  - the URI of the entity code namespace.  If omitted, the default coding scheme namespace for the value domain
	 *                               will be used, if it is present.  Otherwise the first matching entity code, if any, will pass
	 * @param domainURI            - the URI of the value domain
	 * @param csVersionList        - a list of coding scheme URI's and versions to be used.  These will be used only if they are present in
	 *                               the service.  If absent, the most recent version will be used instead.
     * @param versionTag           - the tag (e.g "devel", "production", ...) to be used to reconcile coding schemes when more than one is present.
     *                               Note that non-tagged versions will be used if the tagged version is missing.
	 * @return The codingScheme URI and version of that asserts that the code is in the domain 
	 * @throws LBException
	 */
	public AbsoluteCodingSchemeVersionReference isEntityInDomain(
	        String entityCode, URI entityCodeNamespace, URI valueDomainURI, AbsoluteCodingSchemeVersionReferenceList csVersionList, String versionTag) 
	        throws LBException;

	/**
	 * Resolve a value domain using the supplied set of coding scheme versions.
	 * 
	 * @param valueDomainURI
	 * 			  value domain URI
	 * @param csVersionList
	 *            list of coding scheme versions to use in resolution. IF the
	 *            value domain uses a version that isn't mentioned in this list,
	 *            the resolve function will return the codingScheme and version 
	 *            that was used as a default for the resolution. 
	 * @param versionTag 
	 *            the tag (e.g. "devel", "production", ...) to be used to determine which coding scheme to be used
	 * @return Resolved Value Domain Definition
	 * @throws LBException
	 */
	public ResolvedValueDomainDefinition resolveValueDomain(URI valueDomainURI,
			AbsoluteCodingSchemeVersionReferenceList csVersionList, String versionTag) throws LBException;
	
	

	/**
	 * Check whether childValueDomainURI is a child of parentValueDomainURI.
	 * 
	 * @param childValueDomainURI
	 * 			child value domain URI
	 * @param parentValueDomainURI
	 * 			parent value domain URI
	 *  @param csVersionList
     *            list of coding scheme versions to use in resolution. IF the
     *            value domain uses a version that isn't mentioned in this list,
     *            the resolve function will return the codingScheme and version 
     *            that was used as a default for the resolution. 
     * @param versionTag 
     *            the tag (e.g. "devel", "production", ...) to be used to determine which coding scheme to be used
	 * @return YES, if all the elements of the child domain are in the parent domain 
	 *  		NO otherwise.
	 *  @throws LBException
	 */
	public boolean isSubDomain(URI childValueDomainURI, URI parentValueDomainURI,
	        AbsoluteCodingSchemeVersionReferenceList csVersionList, String versionTag) throws LBException;

	/**
	 * Returns value domain definition for supplied value domain URI.
	 * 
	 * @param valueDomainURI
	 * 			value domain URI
	 * @return value domain definition
	 * @throws LBException
	 */
	public ValueDomainDefinition getValueDomainDefinition(URI valueDomainURI) throws LBException;
	
	/**
	 * Export value domain definition to LexGrid cononical XML format.
	 * 
	 * @param valueDomainURI
	 * 			value domain URI
	 * @param xmlFolderLocation
	 * 			Location to save the definition
	 * @param overwrite
	 * 			True: to override the existing file.
	 * @param failOnAllErrors
	 * 			True: stops exporting if any error.
	 * @throws LBException
	 */
	public void exportValueDomainDefinition(URI valueDomainURI, String xmlFolderLocation, boolean overwrite, boolean failOnAllErrors) throws LBException;
	
	/**
	 * Return the URI's for the value domain definition(s) for the supplied
	 * domain name. If the name is null, returns everything. If the name is not
	 * null, returns the value domain(s) that have the assigned name. 
	 * 
	 * Note: plural because there is no guarantee of valueDomain uniqueness. If the name is the
	 * empty string "", returns all unnamed valueDomains.
	 * 
	 * @param valueDomainName
	 * @return value domain URI's
	 * @throws LBException
	 */
	public URI[] listValueDomains(String valueDomainName) throws LBException;

	/**
	 * Return the URI's of all unnamed value domain definition(s).
	 * 
	 * @return value domain URI's
	 * @throws LBException
	 */
	public URI[] getAllValueDomainsWithNoName()  throws LBException;
	
	
	/**
	 * Resolve the value domain definition, restricting the matching values to entities the match the supplied term and match algorithm.
	 * Behavior is the same as resolveValueDomain with the exception that a restricted set is returned
	 * @param term - text to match. Format is specific to the match algorithm
	 * @param matchAlgorithm - match algorithm to use.  Must be the name of a supported match algorithm
	 * @param valueDomainURI - value domain to resolve
	 * @param csVersionList  - list of coding schemes and versions to resolve against
	 * @param versionTag     - version tag to use for resolving coding schemes
	 * @return Resolution
	 * @throws LBException
	 */
	public ResolvedValueDomainCodedNodeSet getValueDomainEntitiesForTerm(String term, String matchAlgorithm, URI valueDomainURI,
            AbsoluteCodingSchemeVersionReferenceList csVersionList, String versionTag) throws LBException;

	
	
	/**
	 * Returns list of coding scheme summary that is referenced by the supplied
	 * value domain. 
	 * @param valueDomainURI
	 * @return coding scheme version reference list
	 * 
	 * @throws LBException
	 */
	public AbsoluteCodingSchemeVersionReferenceList getCodingSchemesInValueDomain(URI valueDomainURI) throws LBException;
	
	/**
	 * Determine if the supplied entity code is of type valueDomain in supplied
	 * coding scheme and, if it is, return the true, otherwise return false.
	 * 
	 * @param entityCode
	 * @param codingSchemeName
	 * @param csvt
	 * @return TRUE : If entityCode is of type valueDomain in supplied coding scheme, FALSE : otherwise
	 * 
	 * @throws LBException
	 */
	public boolean isDomain(String entityCode, String codingSchemeName, CodingSchemeVersionOrTag csvt) throws LBException;
	
	/**
	 * Removes supplied value domain definition from the system.
	 * 
	 * @param valueDomainURI URI of value domain to remove
	 * @throws LBException
	 * @throws RemoveException
	 */
	public void removeValueDomain(URI valueDomainURI) throws LBException, RemoveException ;
	
	/**
	 * Removes all value domain definitions from the system.
	 * 
	 * @throws LBException
	 * @throws RemoveException
	 */
	public void removeAllValueDomains() throws LBException, RemoveException ;
	
	/**
	 * Drops value domain tables only if there are no value domain and pick list entries.
	 * @throws LBException
	 * @throws RemoveException
	 */
	public void dropValueDomainTables() throws LBException, RemoveException ;
	
	public LogEntry[] getLogEntries();
}