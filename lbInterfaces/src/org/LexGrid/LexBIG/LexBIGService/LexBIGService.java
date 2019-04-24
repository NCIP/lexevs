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
package org.LexGrid.LexBIG.LexBIGService;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Collections.ExtensionDescriptionList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.ModuleDescriptionList;
import org.LexGrid.LexBIG.DataModel.Collections.SortDescriptionList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.SortContext;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Extensions.Generic.GenericExtension;
import org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods.TerminologyServiceDesignation;
import org.LexGrid.LexBIG.Extensions.Query.Filter;
import org.LexGrid.LexBIG.Extensions.Query.Sort;
import org.LexGrid.LexBIG.History.HistoryService;
import org.LexGrid.codingSchemes.CodingScheme;

/**
 * This interface represents the core interface to a LexBIG service.
 */
public interface LexBIGService extends Serializable {

	/**
	 * Returns the version of when LexEVS.
	 * 
	 * @return String version representing the LexEVS version number.
	 */
	public String getLexEVSBuildVersion();
	
	/**
	 * Returns the timestamp of when LexEVS was built.
	 * 
	 * @return String timestamp representing the time LexEVS was built.
	 */
	public String getLexEVSBuildTimestamp();
	
	/**
	 * Returns the set of all concepts in the specified coding scheme.
	 * 
	 * @param codingScheme
	 *            The local name or URN of the coding scheme.
	 * @param versionOrTag
	 *            The assigned tag/label or absolute version identifier of the
	 *            coding scheme to query.
	 * @throws LBException
	 */
	CodedNodeSet getCodingSchemeConcepts(String codingScheme,
			CodingSchemeVersionOrTag versionOrTag)
			throws LBException;

	/**
	 * Returns the set of all (or all active) concepts in the specified coding
	 * scheme.
	 * 
	 * @param codingScheme
	 *            The local name or URN of the coding scheme.
	 * @param versionOrTag
	 *            The assigned tag/label or absolute version identifier of the
	 *            coding scheme to query.
	 * @param activeOnly
	 *            True means node set consists only of concepts that are active
	 *            in the given version. False means set represents both active
	 *            and inactive concepts.
	 * @throws LBException
	 * @deprecated
	 *            Replaced by use of base getCodingSchemeConcepts() method and
	 *            use of the ActiveStatus restriction.
	 */
	CodedNodeSet getCodingSchemeConcepts(String codingScheme,
			CodingSchemeVersionOrTag versionOrTag, boolean activeOnly)
			throws LBException;
	
	/**
	 * Returns an instance of the filter extension registered with the given name.
	 * 
	 * @param name
	 *            The extension name; not null.
	 * @return org.LexGrid.LexBIG.Extensions.Query.Filter
	 * @throws LBException
	 */
	Filter getFilter(String name) throws LBException;

	/**
	 * Returns a description of all registered extensions used to provide
	 * additional filtering of query results.
	 * 
	 * @return The list containing the description of extensions registered for
	 *         this category. Each description identifies a class of item implementing
	 *         org.LexGrid.LexBIG.Extensions.Query.Filter and providing a public
	 *         parameterless constructor.
	 */
	ExtensionDescriptionList getFilterExtensions();

	/**
	 * Returns an instance of the application-specific extension registered with
	 * the given name.
	 * 
	 * @param name
	 *            The extension name; not null.
	 * @return org.LexGrid.LexBIG.Extensions.Generic.GenericExtension
	 * @throws LBException
	 */
	GenericExtension getGenericExtension(String name) throws LBException;

	/**
	 * Returns a description of all registered extensions used to implement
	 * application-specific behavior that is centrally accessible from a
	 * LexBIGService.
	 * <p>
	 * Note that only generic extensions (base class GenericExtension) will
	 * be listed here. All other classes are retrievable at the appropriate
	 * interface point (filter, sort, etc).
	 * 
	 * @return The list containing the description of extensions registered for
	 *         this category. Each description identifies a class of item implementing
	 *         org.LexGrid.LexBIG.Extensions.Generic.GenericExtension and
	 *         providing a public parameterless constructor.
	 */
	ExtensionDescriptionList getGenericExtensions();

	/**
	 * Resolve a reference to the history api servicing the given coding scheme.
	 * 
	 * @param codingScheme
	 *            The local name or URN of the coding scheme.
	 * @return org.LexGrid.LexBIG.History.HistoryService
	 * @throws LBException
	 */
	HistoryService getHistoryService(String codingScheme)
			throws LBException;

	/**
	 * Return the last time that the content of this service was changed; null
	 * if no changes have occurred. Tag assignments do not count as service
	 * changes for this purpose.
	 * 
	 * @return java.util.Date
	 * @throws LBInvocationException
	 */
	Date getLastUpdateTime() throws LBInvocationException;

	/**
	 * Returns the full description of all supported match algorithms.
	 */
	ModuleDescriptionList getMatchAlgorithms();

	/**
	 * Returns the node graph as represented in the particular relationship set
	 * in the coding scheme.
	 * 
	 * @param codingScheme
	 *            The local name or URN of the coding scheme.
	 * @param versionOrTag
	 *            The assigned tag/label or absolute version identifier of the
	 *            coding scheme to query.
	 * @param relationContainerName
	 *            The name of the relations container to reference when generating
	 *            the graph. If omitted, all native relation containers for the code
	 *            system will be queried.  Note: a 'native' container contains a
	 *            set of associations defined by the coding scheme curators.
	 * @throws LBException
	 */
	CodedNodeGraph getNodeGraph(String codingScheme,
			CodingSchemeVersionOrTag versionOrTag, String relationContainerName)
			throws LBException;

	/**
	 * Returns the set of all entities in the specified coding scheme
	 * scheme of the given types.  This method can be used to resolve
	 * instances or other non-concept codes.
	 * 
	 * @param codingScheme
	 *            The local name or URN of the coding scheme.
	 * @param versionOrTag
	 *            The assigned tag/label or absolute version identifier of the
	 *            coding scheme to query.
	 * @param entityTypes
	 *            The list of entity types to resolve.  Possible values
	 *            include the LexGrid built-in types "concept" and "instance".
	 *            Additional supported types can be defined uniquely to a
	 *            coding scheme.  If null or empty, all entity types are
	 *            resolved.
	 * @throws LBException
	 */
	CodedNodeSet getNodeSet(String codingScheme,
			CodingSchemeVersionOrTag versionOrTag, LocalNameList entityTypes)
			throws LBException;

	/**
	 * Validate the credentials and return an interface to the LexBig service
	 * manager.
	 * 
	 * @param credentials
	 *            Credentials - type to be determined by the security system;
	 *            null if not applicable.
	 * @throws LBException
	 */
	LexBIGServiceManager getServiceManager(Object credentials)
			throws LBException;

	/**
	 * Return an interface to perform system-wide query over
	 * metadata for loaded code systems and providers.
	 * 
	 * @throws LBException
	 */
	LexBIGServiceMetadata getServiceMetadata()
			throws LBException;

	/**
	 * Returns an instance of the sort extension registered with the given name.
	 * 
	 * @param name
	 *            The extension name; not null.
	 * @return org.LexGrid.LexBIG.Extensions.Query.Sort
	 * @throws LBException
	 */
	Sort getSortAlgorithm(String name) throws LBException;

	/**
	 * Returns a description of all registered extensions used to provide
	 * additional sorting of query results in the given context.
	 * <p>
	 * Note: The returned list will include any 'built-in' sort algorithms in
	 * addition to extensions externally packaged and registered to the service.
	 * 
	 * @param context
	 *            A context defined by the SortContext class, or null to
	 *            indicate that all registered algorithms are to be returned.
	 * @return The list containing the description of extensions registered for
	 *         this category. Each description identifies a class of item implementing
	 *         org.LexGrid.LexBIG.Extensions.Query.Sort and providing a public
	 *         parameterless constructor.
	 */
	SortDescriptionList getSortAlgorithms(SortContext context);

	/**
	 * Return a list of coding schemes and versions that are supported by this
	 * service, along with their status.
	 * 
	 * @throws LBInvocationException
	 */
	CodingSchemeRenderingList getSupportedCodingSchemes()
			throws LBInvocationException;


	/**
	 * Return detailed coding scheme information given a specific tag or version
	 * identifier.
	 * 
	 * @param codingScheme
	 *            The local name or URN of the coding scheme.
	 * @param versionOrTag
	 *            The assigned tag/label or absolute version identifier of the
	 *            scheme to resolve.
	 * @throws LBException
	 */
	CodingScheme resolveCodingScheme(String codingScheme,
			CodingSchemeVersionOrTag versionOrTag) throws LBException;
	/**
	 * Return coding scheme copyright given a specific tag or version
	 * identifier.
	 * 
	 * @param codingScheme
	 *            The local name or URN of the coding scheme.
	 * @param versionOrTag
	 *            The assigned tag/label or absolute version identifier of the
	 *            scheme to resolve.
	 * @throws LBException
	 */
	
	String resolveCodingSchemeCopyright(String codingScheme,
			CodingSchemeVersionOrTag versionOrTag) throws LBException;

	/**
	 * Return a list of minimal coding scheme objects of the resolved value set
	 * type, to be fully resolved by user later using resolveCodingScheme() method
	 * This method is temporarily using the dbName and dbURI columns in the registry
	 * Model changes for implementation will update the registry schema and model
	 * as necessary
	 * 
	 * @throws LBInvocationException
	 */
    List<CodingScheme> getMinimalResolvedVSCodingSchemes() 
			  throws LBInvocationException;
    
    List<CodingScheme> getRegularResolvedVSCodingSchemes();
    
    List<CodingScheme> getSourceAssertedResolvedVSCodingSchemes();

	TerminologyServiceDesignation getTerminologyServiceObjectType(String uri);

}