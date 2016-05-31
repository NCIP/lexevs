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
package org.lexgrid.valuesets;

import java.io.InputStream;
import java.io.Serializable;
import java.net.URI;
import java.util.HashMap;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.LogEntry;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.AnonymousOption;
import org.LexGrid.naming.Mappings;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.lexgrid.valuesets.dto.ResolvedValueSetCodedNodeSet;
import org.lexgrid.valuesets.dto.ResolvedValueSetDefinition;

/**
 * Value Set Definition Services.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public interface LexEVSValueSetDefinitionServices extends Serializable {
	public static String RESOLVED_AGAINST_CODING_SCHEME_VERSION= "resolvedAgainstCodingSchemeVersion";
	public static String VERSION= "version";
	public static String GENERIC= "generic";
	public static String CS_NAME= "codingSchemeName";
	/**
	 * Loads supplied valueSetDefinition object
	 * @param vsdef value set definition to load
	 * @param systemReleaseURI
	 * @param mappings - additional mappings passed from the value set definition container
	 * @throws LBException
	 */
	public void loadValueSetDefinition(ValueSetDefinition vsdef, String systemReleaseURI, Mappings mappings) throws LBException;
	
	/**
	 * Loads value set definition by reading XML file location supplied
	 * @param xmlFileLocation XML file containing value set definitions
	 * @param failOnAllErrors
	 * @throws Exception
	 */
	public void loadValueSetDefinition(String xmlFileLocation, boolean failOnAllErrors) throws LBException;
	
	/**
	 * Perform validation of the candidate resource without loading data.  
	 * @param uri XML file containing value set definitions
	 * @param validationLevel validate &lt;int&gt; 
	 *         Supported levels of validation include:
	 *         0 = Verify document is well-formed
	 *         1 = Verify document is valid
	 * @throws LBParameterException
	 */
	public void validate(URI uri, int validationLevel) throws LBException;
	
	/**
     * Determine whether the supplied entity code is a valid entity code somewhere in the supplied value set definition. 
     * This function is intended for use with simple value set definition that are drawn from a single coding scheme 
     * where most parameters can be defaulted
	 * 
	 * @param entityCode       - the entity code to search for.  If the value set definition has a default coding scheme, 
	 * 							 this will become the namespace
	 *                           for the entity code.  If not, any matching entity code will pass.
	 * @param valueSetDefinitionURI   - the URI of the value set definition to search
	 * @param valueSetDefintionRevisionId - the version of the value set definition
	 * @param versionTag       - the version or tag (e.g. "devel", "production", etc.) to be used for <i>all</i> of the coding schemes searched.
	 * @return coding scheme and version if the entityCode is valid, null otherwise
	 * @throws LBException
	 */
	public AbsoluteCodingSchemeVersionReference isEntityInValueSet(
	        String entityCode, URI valueSetDefinitionURI, String valueSetDefinitionRevisionId, String versionTag)  throws LBException;

	/**
     * Determine whether the supplied entity code is valid in the suppled value set definition, when reconciled against the supplied
     * set of coding scheme versions and/or version tags
	 * 
	 * @param entityCode           - the entity code to validate.
	 * @param entityCodeNamespace  - the URI of the entity code namespace.  If omitted, the default coding scheme namespace for the value domain
	 *                               will be used, if it is present.  Otherwise the first matching entity code, if any, will pass
	 * @param valueSetDefinitionURI - the URI of the value set definitionn
	 * @param valueSetDefintionRevisionId - the version of the value set definition
	 * @param csVersionList        - a list of coding scheme URI's and versions to be used.  These will be used only if they are present in
	 *                               the service.  If absent, the most recent version will be used instead.
     * @param versionTag           - the tag (e.g "devel", "production", ...) to be used to reconcile coding schemes when more than one is present.
     *                               Note that non-tagged versions will be used if the tagged version is missing.
	 * @return The codingScheme URI and version of that asserts that the code is in the domain 
	 * @throws LBException
	 */
	public AbsoluteCodingSchemeVersionReference isEntityInValueSet(
	        String entityCode, URI entityCodeNamespace, URI valueSetDefinitionURI, String valueSetDefinitionRevisionId, AbsoluteCodingSchemeVersionReferenceList csVersionList, String versionTag) 
	        throws LBException;
	
	/**
     * Returns all the value set definition uris that contains supplied entity code.
	 * 
	 * @param entityCode           - the entity code to validate.
	 * @param entityCodeNamespace  - the URI of the entity code namespace.  If omitted, the default coding scheme namespace for the value domain
	 *                               will be used, if it is present.  Otherwise the first matching entity code, if any, will pass
	 * @param csVersionList        - a list of coding scheme URI's and versions to be used.  These will be used only if they are present in
	 *                               the service.  If absent, the most recent version will be used instead.
     * @param versionTag           - the tag (e.g "devel", "production", ...) to be used to reconcile coding schemes when more than one is present.
     *                               Note that non-tagged versions will be used if the tagged version is missing.
	 * @return The value set definition URIs 
	 * @throws LBException
	 */
	public List<String> listValueSetsWithEntityCode( 
			String entityCode, URI entityCodeNamespace, AbsoluteCodingSchemeVersionReferenceList 
			csVersionList, String versionTag) throws LBException;
	
	/**
	 * Returns unresolved CodedNodeSet populated using definition entries in the value set definition.
	 *   
	 * @param valueSetDefinitionURI - the URI of the value set definition
	 * @param valueSetDefintionRevisionId - the version of the value set definition
	 * @param csVersionList - a list of coding scheme URI's and versions to be used.  These will be used only if they are present in
	 *                               the service.  If absent, the most recent version will be used instead.
	 * @param csVersionTag - the tag (e.g "devel", "production", ...) to be used to reconcile coding schemes when more than one is present.
     *                               Note that non-tagged versions will be used if the tagged version is missing.
	 * @return 
	 * @throws LBException
	 */
	public ResolvedValueSetCodedNodeSet getCodedNodeSetForValueSetDefinition(
            URI valueSetDefinitionURI, String valueSetDefinitionRevisionId, 
            AbsoluteCodingSchemeVersionReferenceList csVersionList,
            String csVersionTag) throws LBException;
	
	/**
	 * Returns unresolved CodedNodeSet populated using definition entries in the value set definition.
	 *   
	 * @param valueSetDefinitionURI - the URI of the value set definition
	 * @param valueSetDefintionRevisionId - the version of the value set definition
	 * @param csVersionList - a list of coding scheme URI's and versions to be used.  These will be used only if they are present in
	 *                               the service.  If absent, the most recent version will be used instead.
	 * @param csVersionTag - the tag (e.g "devel", "production", ...) to be used to reconcile coding schemes when more than one is present.
     *                               Note that non-tagged versions will be used if the tagged version is missing.
     * @param AnonymousOption - Option to determine if anonymous classes should be returned.                                     
	 * @return 
	 * @throws LBException
	 */
	public ResolvedValueSetCodedNodeSet getCodedNodeSetForValueSetDefinition(
            URI valueSetDefinitionURI, String valueSetDefinitionRevisionId, 
            AbsoluteCodingSchemeVersionReferenceList csVersionList,
            String csVersionTag, AnonymousOption anonymousOption) throws LBException;
	
	/**
	 * Resolve a value set definition using the supplied set of coding scheme versions.
	 * 
	 * @param valueSetDefinitionURI
	 * 			  value set definition URI
	 * @param valueSetDefintionRevisionId - the version of the value set definition
	 * @param csVersionList
	 *            list of coding scheme versions to use in resolution. IF the
	 *            value set definition uses a version that isn't mentioned in this list,
	 *            the resolve function will return the codingScheme and version 
	 *            that was used as a default for the resolution. 
	 * @param versionTag 
	 *            the tag (e.g. "devel", "production", ...) to be used to determine which coding scheme to be used
	 * @param sortOptionList
	 *            List of sort options to apply during resolution. If supplied,
	 *            the sort algorithms will be applied in the order provided. Any
	 *            algorithms not valid to be applied in context of node set
	 *            iteration, as specified in the sort extension description,
	 *            will result in a parameter exception. Available algorithms can
	 *            be retrieved through the LexBIGService getSortExtensions()
	 *            method after being defined to the LexBIGServiceManager
	 *            extension registry.            
	 * @return Resolved Value Set Definition
	 * @throws LBException
	 */
	public ResolvedValueSetDefinition resolveValueSetDefinition(URI valueSetDefinitionURI, String valueSetDefinitionRevisionId, 
			AbsoluteCodingSchemeVersionReferenceList csVersionList, String versionTag, SortOptionList sortOptionList) throws LBException;
	
	/**
	 * Resolve a value set definition provided using the supplied set of coding scheme versions.
	 * 
	 * @param valueSetDefinition
	 * 			  value set definition object
	 * @param csVersionList
	 *            list of coding scheme versions to use in resolution. IF the
	 *            value set definition uses a version that isn't mentioned in this list,
	 *            the resolve function will return the codingScheme and version 
	 *            that was used as a default for the resolution. 
	 * @param versionTag 
	 *            the tag (e.g. "devel", "production", ...) to be used to determine which coding scheme to be used
	 * @param sortOptionList
	 *            List of sort options to apply during resolution. If supplied,
	 *            the sort algorithms will be applied in the order provided. Any
	 *            algorithms not valid to be applied in context of node set
	 *            iteration, as specified in the sort extension description,
	 *            will result in a parameter exception. Available algorithms can
	 *            be retrieved through the LexBIGService getSortExtensions()
	 *            method after being defined to the LexBIGServiceManager
	 *            extension registry. 
	 * @return Resolved Value Domain Definition
	 * @throws LBException
	 */
	public ResolvedValueSetDefinition resolveValueSetDefinition(ValueSetDefinition vsDef, 
			AbsoluteCodingSchemeVersionReferenceList csVersionList, String versionTag, SortOptionList sortOptionList) throws LBException;

	/**
	 * Resolve a value set definition provided using the supplied set of coding scheme versions. 
	 * This method also takes in list of ValueSetDefinitions (referencedVSDs) that are referenced by the ValueSetDefinition (vsDef).
	 * If referencedVSDs list is provided, these ValueSetDefinitions will be used to resolve vsDef.	
	 * 
	 * @param valueSetDefinition
	 * 			  value set definition object
	 * @param csVersionList
	 *            list of coding scheme versions to use in resolution. IF the
	 *            value set definition uses a version that isn't mentioned in this list,
	 *            the resolve function will return the codingScheme and version 
	 *            that was used as a default for the resolution. 
	 * @param versionTag 
	 *            the tag (e.g. "devel", "production", ...) to be used to determine which coding scheme to be used
	 * @param referencedVSDs
	 * 			  List of ValueSetDefinitions referenced by vsDef. If provided, these ValueSetDefinitions will be used to resolve vsDef.	
	 * @param sortOptionList
	 *            List of sort options to apply during resolution. If supplied,
	 *            the sort algorithms will be applied in the order provided. Any
	 *            algorithms not valid to be applied in context of node set
	 *            iteration, as specified in the sort extension description,
	 *            will result in a parameter exception. Available algorithms can
	 *            be retrieved through the LexBIGService getSortExtensions()
	 *            method after being defined to the LexBIGServiceManager
	 *            extension registry. 
	 * @return Resolved Value Domain Definition
	 * @throws LBException
	 */
	public ResolvedValueSetDefinition resolveValueSetDefinition(ValueSetDefinition vsDef, 
			AbsoluteCodingSchemeVersionReferenceList csVersionList, String versionTag, HashMap<String, ValueSetDefinition> referencedVSDs, SortOptionList sortOptionList) throws LBException;
	
	/**
	 * Check whether childValueDSetDefinitionURI is a child of parentValueSetDefinitionURI.
	 * 
	 * @param childValueSetDefinitionURI
	 * 			child value set definition URI
	 * @param parentValueSetDefinitionURI
	 * 			parent value set definition URI
	 *  @param csVersionList
     *            list of coding scheme versions to use in resolution. IF the
     *            value set definition uses a version that isn't mentioned in this list,
     *            the resolve function will return the codingScheme and version 
     *            that was used as a default for the resolution. 
     * @param versionTag 
     *            the tag (e.g. "devel", "production", ...) to be used to determine which coding scheme to be used
	 * @return YES, if all the elements of the child domain are in the parent domain 
	 *  		NO otherwise.
	 *  @throws LBException
	 */
	public boolean isSubSet(URI childValueSetDefinitionURI, URI parentValueSetDefinitionURI,
	        AbsoluteCodingSchemeVersionReferenceList csVersionList, String versionTag) throws LBException;

	/**
	 * Returns value set definition for supplied value set definition URI.
	 * 
	 * @param valueSetDefinitionURI
	 * 			value set definition URI
	 * @param valueSetDefintionRevisionId 
	 * 			the version of the value set definition
	 * @return value set definition
	 * @throws LBException
	 */
	public ValueSetDefinition getValueSetDefinition(URI valueSetDefinitionURI, String valueSetDefinitionRevisionId) throws LBException;
	
	/**
	 * Export value set definition to LexGrid canonical XML format.
	 * 
	 * @param valueSetDefinitionURI
	 * 			value set definition URI
	 * @param valueSetDefinitionRevisionId
	 * 			revision id of the value set definition to export
	 * @param xmlFullPathName
	 * 			Location to save the definition
	 * @param overwrite
	 * 			True: to override the existing file.
	 * @param failOnAllErrors
	 * 			True: stops exporting if any error.
	 * @throws LBException
	 */
	public void exportValueSetDefinition(URI valueSetDefinitionURI, String valueSetDefinitionRevisionId, String xmlFullPathName, boolean overwrite, boolean failOnAllErrors) throws LBException;
	
	/**
	 * Exports contents of Value Set Definition as Coding Scheme in LexGrid canonical XML format.
	 * 
	 * @param valueSetDefinitionURI
	 * 			value set definition URI
	 * @param valueSetDefinitionRevisionId
	 * 			value set definition revision id
	 * @param exportDestination
	 * 			Location to save the definition
	 * @param csVersionList 
	 * 			A list of coding scheme URI's and versions to be used.  These will be used only if they are present in
	 * 			the service.  If absent, the most recent version will be used instead.
	 * @param csVersionTag 
	 * 			the tag (e.g "devel", "production", ...) to be used to reconcile coding schemes when more than one is present.
     * @param overwrite
	 * 			True: to override the existing file.
	 * @param failOnAllErrors
	 * 			True: stops exporting if any error.
	 * @return URI of destination if successfully exported.
	 * @throws LBException
	 */
	public URI exportValueSetResolution(URI valueSetDefinitionURI, String valueSetDefinitionRevisionId,  
			URI exportDestination, AbsoluteCodingSchemeVersionReferenceList csVersionList,
            String csVersionTag, boolean overwrite, boolean failOnAllErrors) throws LBException;
	
	/**
	 * Resolves and Exports contents of Value Set Resolution as Coding Scheme in LexGrid canonical XML format.
	 * 
	 * @param valueSetDefinitionURI
	 * 			value set definition URI to be resolved
	 * @param valueSetDefinitionRevisionId
	 * 			value set definition revision id to be used
	 * @param csVersionList 
	 * 			A list of coding scheme URI's and versions to be used.  These will be used only if they are present in
	 * 			the service.  If absent, the most recent version will be used instead.
	 * @param csVersionTag 
	 * 			the tag (e.g "devel", "production", ...) to be used to reconcile coding schemes when more than one is present.
     * @param failOnAllErrors
	 * 			True: stops exporting if any error.
	 * @return InputStream Resolved contents of value set in LexGrid Coding Scheme XML format.
	 * @throws LBException
	 */
	public InputStream exportValueSetResolution(URI valueSetDefinitionURI, String valueSetDefinitionRevisionId,  
			AbsoluteCodingSchemeVersionReferenceList csVersionList, String csVersionTag, boolean failOnAllErrors) throws LBException;
	
	/**
	 * Resolve and Exports contents of supplied Value Set Definition object 
	 * as Coding Scheme in LexGrid canonical XML format.
	 * 
	 * @param valueSetDefinition
	 * 			  value set definition object
	 * @param referencedVSDs
	 * 			  List of ValueSetDefinitions referenced by valueSetDefinition. If provided, these ValueSetDefinitions will be used to resolve valueSetDefinition.	
	 * @param exportDestination
	 * 			Location to save the definition
	 * @param csVersionList 
	 * 			A list of coding scheme URI's and versions to be used.  These will be used only if they are present in
	 * 			the service.  If absent, the most recent version will be used instead.
	 * @param csVersionTag 
	 * 			the tag (e.g "devel", "production", ...) to be used to reconcile coding schemes when more than one is present.
     * @param overwrite
	 * 			True: to override the existing file.
	 * @param failOnAllErrors
	 * 			True: stops exporting if any error.
	 * @return URI of destination if successfully exported.
	 * @throws LBException
	 */
	public URI exportValueSetResolution(ValueSetDefinition valueSetDefinition, HashMap<String, ValueSetDefinition> referencedVSDs, 
			URI exportDestination, AbsoluteCodingSchemeVersionReferenceList csVersionList,
            String csVersionTag, boolean overwrite, boolean failOnAllErrors) throws LBException;
	
	/**
	 * Resolves and Exports contents of supplied Value Set Definition object as Coding Scheme in LexGrid canonical XML format.
	 * 
	 * @param valueSetDefinition
	 * 			  value set definition object
	 * @param referencedVSDs
	 * 			  List of ValueSetDefinitions referenced by valueSetDefinition. If provided, these ValueSetDefinitions will be used to resolve valueSetDefinition.	
	 * @param csVersionList 
	 * 			A list of coding scheme URI's and versions to be used.  These will be used only if they are present in
	 * 			the service.  If absent, the most recent version will be used instead.
	 * @param csVersionTag 
	 * 			the tag (e.g "devel", "production", ...) to be used to reconcile coding schemes when more than one is present.
     * @param failOnAllErrors
	 * 			True: stops exporting if any error.
	 * @return InputStream Resolved contents of value set in LexGrid Coding Scheme XML format.
	 * @throws LBException
	 */
	public InputStream exportValueSetResolution(ValueSetDefinition valueSetDefinition, HashMap<String, ValueSetDefinition> referencedVSDs, 
			AbsoluteCodingSchemeVersionReferenceList csVersionList, String csVersionTag, boolean failOnAllErrors) throws LBException;
	
	/**
	 * Exports Value Set Definition to StringBuffer in LexGrid XML format.
	 * 
	 * @param valueSetDefinitionURI
	 * 			Value Set Definition URI to be exported
	 * @param valueSetDefintionRevisionId 
	 * 			(Optional) The version of the value set definition
	 * @return StringBuffer containing value set definition in LexGrid XML format
	 * @throws LBException
	 */
	public StringBuffer exportValueSetDefinition(URI valueSetDefinitionURI, String valueSetDefinitionRevisionId) throws LBException;
	
	/**
	 * Exports supplied Value Set Definition to StringBuffer in LexGrid XML format.
	 * 
	 * @param valueSetDefinition
	 * 			Value Set Definition object to be exported to StringBuffer in LexGrid XML format
	 * @return StringBuffer containing value set definition in LexGrid XML format
	 * @throws LBException
	 */
	public StringBuffer exportValueSetDefinition(ValueSetDefinition valueSetDefinition) throws LBException;
	
	/**
	 * Return the URI's for the value set definition(s) for the supplied
	 * value set definition name. If the name is null, returns everything. If the name is not
	 * null, returns the value set definition(s) that have the assigned name. 
	 * 
	 * Note: plural because there is no guarantee of valueSetDefinition uniqueness. If the name is the
	 * empty string "", returns all unnamed valueSetDefinitions.
	 * 
	 * @param valueSetDefinitionName
	 * @return value domain URI's
	 * @throws LBException
	 */
	public List<String> listValueSetDefinitions(String valueSetDefinitionName) throws LBException;
	
	/**
	 * Lists all the value set definition URIs that are loaded in the system.
	 * 
	 * @return list of value set definition URIs
	 */
	public List<String> listValueSetDefinitionURIs();

	/**
	 * Return the URI's of all unnamed value set definition(s).
	 * 
	 * @return value set definition URI's
	 * @throws LBException
	 */
	public List<String> getAllValueSetDefinitionsWithNoName()  throws LBException;
	
	
	/**
	 * Resolve the value set definition, restricting the matching values to entities the match the supplied term and match algorithm.
	 * Behavior is the same as resolveValueSetDefinition with the exception that a restricted set is returned
	 * @param term - text to match. Format is specific to the match algorithm
	 * @param matchAlgorithm - match algorithm to use.  Must be the name of a supported match algorithm
	 * @param valueSetDefinitionURI - value set definition to resolve
	 * @param csVersionList  - list of coding schemes and versions to resolve against
	 * @param versionTag     - version tag to use for resolving coding schemes
	 * @return Resolution
	 * @throws LBException
	 */
	public ResolvedValueSetCodedNodeSet getValueSetDefinitionEntitiesForTerm(String term, String matchAlgorithm, URI valueSetDefinitionURI,
            AbsoluteCodingSchemeVersionReferenceList csVersionList, String versionTag) throws LBException;

	
	
	/**
	 * Returns list of coding scheme summary that is referenced by the supplied
	 * value set definition. 
	 * @param valueSetDefinitionURI
	 * @return coding scheme version reference list
	 * 
	 * @throws LBException
	 */
	public AbsoluteCodingSchemeVersionReferenceList getCodingSchemesInValueSetDefinition(URI valueSetDefinitionURI) throws LBException;
	
	/**
	 * Determine if the supplied entity code is of type valueSetDefinition in supplied
	 * coding scheme and, if it is, return the true, otherwise return false.
	 * 
	 * @param entityCode
	 * @param codingSchemeName
	 * @param csvt
	 * @return TRUE : If entityCode is of type valueSetDefinition in supplied coding scheme, FALSE : otherwise
	 * 
	 * @throws LBException
	 */
	public boolean isValueSetDefinition(String entityCode, String codingSchemeName, CodingSchemeVersionOrTag csvt) throws LBException;
	
	/**
	 * Removes supplied value set definition from the system.
	 * 
	 * @param valueSetDefinitionURI URI of value set definition to remove
	 * @throws LBException
	 */
	public void removeValueSetDefinition(URI valueSetDefinitionURI) throws LBException;
	
	public LogEntry[] getLogEntries();
	
	/**
	 * Returns list of Value Set Definition URIs that contain supplied SupportedAttribute Tag and Value.
	 * 
	 * @param supportedTag - Supported Attribute tag like CodingScheme, ConceptDomain, Source, Property etc.
	 * @param value - value of the supported attribute
	 * @param uri - uri of the supported attribute
	 * @return list of URIs
	 */
	public List<String> getValueSetDefinitionURIsForSupportedTagAndValue(String supportedTag, String value, String uri);
	
	/**
	 * Returns list of Value Set Definition URIs that references supplied coding scheme.
	 * 
	 * @param codingSchemename name of the coding scheme
	 * @parma codingschemeuri uri of coding scheme
	 * @return list of URIs
	 */
	public List<String> getValueSetDefinitionURIsWithCodingScheme(String codingSchemename, String codingSchemeURI);
	
	/**
	 * Returns list of Value Set Definition URIs that are bound to supplied concept domain.
	 * 
	 * @param conceptDomain name/id of the conceptDomain
	 * @param codingSchemeURI coding scheme URI to which the concept domain belongs to
	 * 
	 * @return list of URIs
	 */
	public List<String> getValueSetDefinitionURIsWithConceptDomain(String conceptDomain, String codingSchemeURI);
	
	/**
	 * Returns list of Value Set Definition URIs that can be used with in the supplied list of usage context.
	 * 
	 * @param usageContexts list of usage context
	 * @param codingSchemeURI coding scheme URI to which the concept domain belongs to
	 * 
	 * @return list of URIs
	 */
	public List<String> getValueSetDefinitionURIsWithUsageContext(List<String> usageContexts, String codingSchemeURI);
	
	/**
	 * Returns list of Value Set Definition URIs that are bound to supplied concept domain 
	 * and in supplied usage context.
	 * 
	 * @param conceptDomain name/id of the conceptDomain
	 * @param usageContexts list of usage context names/IDs
	 * @param codingSchemeURI coding scheme URI to which the concept domain belongs to
	 * 
	 * @return list of URIs
	 */
	public List<String> getValueSetDefinitionURIsWithConceptDomainAndUsageContext(String conceptDomain, List<String> usageContexts, String codingSchemeURI);
}