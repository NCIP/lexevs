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
package org.lexgrid.valuesets.helper;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.ActiveOption;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ServiceUtility;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedAssociation;
import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.naming.SupportedNamespace;
import org.LexGrid.proxy.CastorProxy;
import org.LexGrid.valueSets.DefinitionEntry;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.castor.xml.XMLProperties;
import org.exolab.castor.xml.MarshalListener;
import org.exolab.castor.xml.Marshaller;
import org.lexevs.dao.database.service.valuesets.ValueSetDefinitionService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.registry.service.Registry.KnownTags;
import org.lexevs.system.service.SystemResourceService;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.dto.ResolvedValueSetCodedNodeSet;
import org.lexgrid.valuesets.helper.compiler.FileSystemCachingValueSetDefinitionCompilerDecorator;
import org.lexgrid.valuesets.helper.compiler.ValueSetDefinitionCompiler;

import com.ibatis.common.io.ReaderInputStream;

import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.constants.LexGridConstants;
import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.listeners.LexGridMarshalListener;
import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.listeners.StreamingLexGridMarshalListener;

/**
 * Helper class for Value Set Definition functions.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public class VSDServiceHelper {
	@SuppressWarnings("unused")
	private static Logger log = LogManager.getLogger("convert.SQL");

	private ValueSetDefinitionCompiler valueSetDefinitionCompiler;

	// The maximum number of nodes to cache in the process of searching for leaf
	// nodes of a graph
	private int maxLeafCacheSize = 10000;

	private LexBIGService lbs_;
	private SystemResourceService rm_;

	private ValueSetDefinitionService vsds_ = LexEvsServiceLocator
			.getInstance().getDatabaseServiceManager()
			.getValueSetDefinitionService();

	/**
	 * Constructor
	 * 
	 * @param sqlServer
	 *            - SQL Server URI
	 * @param sqlDriver
	 *            - SQL driver name
	 * @param sqlUsername
	 *            - logon user name
	 * @param sqlPassword
	 *            - logon password
	 * @param tablePrefix
	 *            - prefix to use (?)
	 * @param failOnAllErrors
	 *            - true means fail on any load, false means try
	 * @param messages
	 *            - message director
	 * @throws LBParameterException
	 * @throws LBInvocationException
	 */
	public VSDServiceHelper(boolean failOnAllErrors,
			LgMessageDirectorIF messages) throws LBParameterException,
			LBInvocationException {
		this.rm_ = LexEvsServiceLocator.getInstance()
				.getSystemResourceService();
	}

	/**
	 * Return the local identifier of the coding scheme name associated with the
	 * supplied namespace name in the context of the supplied mapping.
	 * Comparison is case insensitive.
	 * 
	 * @param maps
	 *            Mappings to use for transformation
	 * @param namespaceName
	 *            name to map
	 * @return local id of coding scheme if there is one, else null
	 */
	public String getCodingSchemeNameForNamespaceName(Mappings maps,
			String namespaceName) {
		if (!StringUtils.isEmpty(namespaceName)) {
			if (maps != null && maps.getSupportedNamespace() != null) {
				Iterator<SupportedNamespace> sni = maps
						.getSupportedNamespaceAsReference().iterator();
				while (sni.hasNext()) {
					SupportedNamespace sns = sni.next();
					if (sns.getLocalId().equalsIgnoreCase(namespaceName))
						return sns.getEquivalentCodingScheme();
				}
			}
		}
		return null;
	}

	/**
	 * Return the URI that corresponds to the supplied coding scheme name.
	 * Comparison is case insensitive.
	 * 
	 * @param maps
	 *            - Mappings that contain the name to URI maps
	 * @param codingSchemeName
	 *            - local identifier of the coding scheme
	 * @return - URI or, if missing, the coding scheme name (surrogate)
	 */
	public static String getURIForCodingSchemeName(Mappings maps,
			String codingSchemeName) {
		if (maps != null && maps.getSupportedCodingScheme() != null) {
			ListIterator<SupportedCodingScheme> scsi = maps
					.getSupportedCodingSchemeAsReference().listIterator();
			while (scsi.hasNext()) {
				SupportedCodingScheme scs = scsi.next();
				if (scs.getLocalId().equalsIgnoreCase(codingSchemeName))
					return scs.getUri();
			}
		}
		return codingSchemeName;
	}

	/**
	 * Return the coding scheme URI that corresponds to the supplied
	 * entityCodeNamespace. Comparison is case insensitive.
	 * 
	 * @param maps
	 *            - Mappings that contain the name to URI maps
	 * @param entityCodeNamespace
	 *            - local identifier of the entityCodeNamespace
	 * @return - URI or, if missing, the coding scheme name (surrogate)
	 */
	public static String getCodingSchemeURIForEntityCodeNamespace(
			Mappings maps, String entityCodeNamespace) {
		if (maps != null && maps.getSupportedNamespace() != null) {
			ListIterator<SupportedNamespace> snsi = maps
					.getSupportedNamespaceAsReference().listIterator();
			while (snsi.hasNext()) {
				SupportedNamespace sns = snsi.next();
				if (sns.getLocalId().equalsIgnoreCase(entityCodeNamespace)) {
					if (sns.getEquivalentCodingScheme() != null) {
						return getURIForCodingSchemeName(maps,
								sns.getEquivalentCodingScheme());
					}
				}
			}
		}

		return entityCodeNamespace;
	}

	/**
	 * Return the URI that corresponds to the supplied association name. This
	 * function will use <i>either</i> the local association identifier or the
	 * value text. Comparison is case insensitive.
	 * 
	 * @param maps
	 *            - Mappings that contain the name to URI maps
	 * @param associationName
	 *            - local identifier of the coding scheme
	 * @return - URI or, if missing, the association name (surrogate)
	 */
	public String getURIForAssociationName(Mappings maps, String associationName) {
		if (maps != null && maps.getSupportedAssociation() != null) {
			ListIterator<SupportedAssociation> sai = maps
					.getSupportedAssociationAsReference().listIterator();
			while (sai.hasNext()) {
				SupportedAssociation sa = sai.next();
				if (sa.getLocalId().equalsIgnoreCase(associationName)
						|| sa.getContent().equalsIgnoreCase(associationName))
					return sa.getUri();
			}
		}
		return associationName;
	}

	/**
	 * Return a string representation the URI's of all of the coding schemes
	 * used in the supplied value domain
	 * 
	 * @param vdDef
	 *            supplied value domain
	 * @return List of unique URIs. Returned as strings because we aren't all
	 *         that picky about the syntax
	 * @throws LBException
	 * @throws URISyntaxException
	 */
	public HashSet<String> getCodingSchemeURIs(ValueSetDefinition vdDef)
			throws LBException {
		HashSet<String> csRefs = new HashSet<String>();

		if (vdDef != null && vdDef.getDefinitionEntry() != null) {
			// Always add the default coding scheme, even if it isn't used
			if (!StringUtils.isEmpty(vdDef.getDefaultCodingScheme()))
				csRefs.add(getURIForCodingSchemeName(vdDef.getMappings(),
						vdDef.getDefaultCodingScheme()));

			// Iterate over all of the individual definitions
			Iterator<DefinitionEntry> deIter = vdDef
					.getDefinitionEntryAsReference().iterator();
			while (deIter.hasNext()) {
				DefinitionEntry de = deIter.next();
				String csName = null;
				if (de.getCodingSchemeReference() != null) {
					csName = de.getCodingSchemeReference().getCodingScheme();
				} else if (de.getEntityReference() != null) {
					String entityNamespaceName = de.getEntityReference()
							.getEntityCodeNamespace();
					if (!StringUtils.isEmpty(entityNamespaceName)) {
						csName = getCodingSchemeNameForNamespaceName(
								vdDef.getMappings(), entityNamespaceName);
					}
				} else if (de.getPropertyReference() != null) {
					csName = de.getPropertyReference().getCodingScheme();
				} else if (de.getValueSetDefinitionReference() != null) {
					try {
						csRefs.addAll(getCodingSchemeURIs(vsds_
								.getValueSetDefinitionByUri(new URI(de
										.getValueSetDefinitionReference()
										.getValueSetDefinitionURI()))));
					} catch (URISyntaxException e) {
						// TODO Decide what to do here - the value domain URI
						// isn't valid?
						e.printStackTrace();
					}
				} else {
					assert false : "Invalid value domain definition";
				}
				if (!StringUtils.isEmpty(csName)
						&& !StringUtils.equals(csName,
								vdDef.getDefaultCodingScheme())) {
					String csURI = getURIForCodingSchemeName(
							vdDef.getMappings(), csName);
					if (!StringUtils.isEmpty(csURI))
						csRefs.add(csURI);
				}
			}
		}
		return csRefs;
	}

	/**
	 * Return a list of all the versions of the supplied coding scheme URI or
	 * local identifier that are supported by the service
	 * 
	 * @param codingSchemeNameOrURI
	 *            - URI to return versions for or return all URI's if null
	 * @return AbsoluteCodingSchemeVersionReferenceList list of codingScheme and
	 *         version. Names are transformed to URI's for the return
	 * @throws LBException
	 */
	public AbsoluteCodingSchemeVersionReferenceList getAbsoluteCodingSchemeVersionReference(
			String codingSchemeNameOrURI) throws LBException {
		AbsoluteCodingSchemeVersionReferenceList acsvrList = new AbsoluteCodingSchemeVersionReferenceList();
		CodingSchemeRendering[] csrList = getLexBIGService()
				.getSupportedCodingSchemes().getCodingSchemeRendering();

		for (CodingSchemeRendering csr : csrList) {
			if (StringUtils.isEmpty(codingSchemeNameOrURI)
					|| csr.getCodingSchemeSummary().getCodingSchemeURI()
							.equalsIgnoreCase(codingSchemeNameOrURI)
					|| csr.getCodingSchemeSummary().getLocalName()
							.equalsIgnoreCase(codingSchemeNameOrURI)) {
				AbsoluteCodingSchemeVersionReference acsvr = new AbsoluteCodingSchemeVersionReference();
				acsvr.setCodingSchemeURN(csr.getCodingSchemeSummary()
						.getCodingSchemeURI());
				acsvr.setCodingSchemeVersion(csr.getCodingSchemeSummary()
						.getRepresentsVersion());
				acsvrList.addAbsoluteCodingSchemeVersionReference(acsvr);
			}
		}
		return acsvrList;
	}

	/**
	 * Checks if the supplied codingScheme and version is loaded.
	 * 
	 * @param codingSchemeName
	 * @param version
	 * @return True; if given codingScheme/version is loader, otherwise, False.
	 * @throws LBException
	 */
	protected boolean isCodingSchemeVersionLoaded(String codingSchemeName,
			String version) throws LBException {

		String csName = codingSchemeName;

		if (StringUtils.isEmpty(version)) {
			// check if supplied version for the coding scheme is loaded
			csName = rm_.getInternalCodingSchemeNameForUserCodingSchemeName(
					codingSchemeName, version);
			if (StringUtils.isNotEmpty(csName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Resolve a value domain definition.
	 * 
	 * @param vdd
	 *            - the value domain definition to be resolved
	 * @param acsvl
	 *            - a list of coding scheme URI's and versions to be used in the
	 *            resolution.
	 * @param versionTag
	 *            - a tag (e.g. "production", "test", etc. used to any coding
	 *            schemes not in asvl If a coding scheme does not appear in the
	 *            asvl list the resolution will be as follows: 1) If the service
	 *            supports a single active version of the coding scheme it will
	 *            be used. 2) If there is more than one version the one that
	 *            uses the supplied versionTag will be used 3) If the versionTag
	 *            isn't supplied, or if none of the versions matches it, then
	 *            the one marked "production" will be used 4) If there isn't one
	 *            marked production, then the "latest" will be used
	 * @param referencedVSDs
	 *            - List of ValueSetDefinitions referenced by vsDef. If
	 *            provided, these ValueSetDefinitions will be used to resolve
	 *            vsDef.
	 * @return ResolvedValueSetCodedNodeSet
	 * @throws LBException
	 */
	public ResolvedValueSetCodedNodeSet getResolvedCodedNodeSetForValueSet(
			ValueSetDefinition vdd,
			AbsoluteCodingSchemeVersionReferenceList csVersionsToUse,
			String versionTag,
			HashMap<String, ValueSetDefinition> referencedVSDs)
			throws LBException {
		ResolvedValueSetCodedNodeSet rval = new ResolvedValueSetCodedNodeSet();

		// Remove anything from the resolution list that isn't available in the
		// coding scheme and turn into map from URI to Version
		// TODO What should the behavior be if it is in the list but not
		// supported by the service? Warning?
		HashMap<String, String> refVersions = pruneVersionList(csVersionsToUse);

		rval.setCodedNodeSet(getValueSetDefinitionCompiler()
				.compileValueSetDefinition(vdd, refVersions, versionTag,
						referencedVSDs));
		rval.setCodingSchemeVersionRefList(new AbsoluteCodingSchemeVersionReferenceList());

		// Transfer the list of used versions
		Iterator<String> versionURLs = refVersions.keySet().iterator();
		while (versionURLs.hasNext()) {
			String versionURL = versionURLs.next();
			rval.getCodingSchemeVersionRefList()
					.addAbsoluteCodingSchemeVersionReference(
							Constructors
									.createAbsoluteCodingSchemeVersionReference(
											versionURL,
											refVersions.get(versionURL)));
		}
		return rval;
	}

	/**
	 * Go over the supplied coding scheme version reference list and remove any
	 * entries that aren't supported by the service. If a coding scheme URI
	 * appears more than once in the list, only the first entry will be used.
	 * 
	 * @param suppliedVersions
	 *            - a list of "suggested" versions to use
	 * @return A list of unique coding scheme URIs and the corresponding
	 *         versions
	 */
	public HashMap<String, String> pruneVersionList(
			AbsoluteCodingSchemeVersionReferenceList suppliedCsVersions)
			throws LBException {
		HashMap<String, String> prunedList = new HashMap<String, String>();
		if (suppliedCsVersions != null) {
			// List of all coding scheme versions that are supported in the
			// service
			AbsoluteCodingSchemeVersionReferenceList serviceCsVersions = getAbsoluteCodingSchemeVersionReference(null);
			for (AbsoluteCodingSchemeVersionReference suppliedVer : suppliedCsVersions
					.getAbsoluteCodingSchemeVersionReference()) {

				String externalVersionId = rm_.getUriForUserCodingSchemeName(
						suppliedVer.getCodingSchemeURN(),
						suppliedVer.getCodingSchemeVersion());

				// TODO - implement a content equality operator so we can use
				// "contains" vs. an inner iterator
				for (AbsoluteCodingSchemeVersionReference serviceVer : serviceCsVersions
						.getAbsoluteCodingSchemeVersionReference()) {
					if (StringUtils.equalsIgnoreCase(externalVersionId,
							serviceVer.getCodingSchemeURN())
							&& StringUtils.equalsIgnoreCase(
									suppliedVer.getCodingSchemeVersion(),
									serviceVer.getCodingSchemeVersion())) {
						if (!prunedList.containsKey(externalVersionId))
							prunedList.put(externalVersionId,
									suppliedVer.getCodingSchemeVersion());
						else {
							// TODO Should we report an error here? A duplicate
							// URN could result in inconsistent behavior.
						}
						break;
					}
				}
			}
		}
		return prunedList;
	}

	/**
	 * Set the maximum cache size to be used when traversing graphs looking for
	 * leaf nodes.
	 * 
	 * @param newSize
	 *            New max size
	 */
	public void setMaxLeafCacheSize(int newSize) {
		maxLeafCacheSize = newSize;
	}

	/**
	 * Return the maximum cache size for traversing graphs looking for leaf
	 * nodes
	 */
	public int getMaxLeafCacheSize() {
		return maxLeafCacheSize;
	}

	/**
	 * Record a persistent link to the LexBIG service for use by this and other
	 * classes
	 * 
	 * @param lbs
	 */
	public void setLexBIGService(LexBIGService lbs) {
		this.lbs_ = lbs;
	}

	/**
	 * Return a persistent link to the LexBIG service
	 * 
	 * @return
	 */
	public LexBIGService getLexBIGService() {
		if (lbs_ == null)
			lbs_ = LexBIGServiceImpl.defaultInstance();
		return lbs_;
	}

	/*
	 * ==========================================================================
	 * ======================================== Helper Functions
	 * ================
	 * ==========================================================
	 * ========================================
	 */

	/**
	 * Return the absolute reference for the supplied csName. Add the entry to
	 * the refVersions if it isn't there
	 * 
	 * @param csName
	 *            - the local identifier of the coding scheme to be resolved
	 * @param maps
	 *            - mappings that contain local ids to URIs
	 * @param versionTag
	 *            - the version tag to use if there is more than one version in
	 *            the service
	 * @param refVersions
	 *            - a list of URI/version pairs that are already resolved
	 * @return the URI/Version to use or null if none can be found
	 * @throws LBException
	 */
	public AbsoluteCodingSchemeVersionReference resolveCSVersion(String csName,
			Mappings maps, String versionTag,
			HashMap<String, String> refVersions) throws LBException {
		String csURI = getURIForCodingSchemeName(maps, csName);
		if (!StringUtils.isEmpty(csURI)) {
			// If it is already in the list, use it
			if (refVersions.containsKey(csURI))
				return Constructors.createAbsoluteCodingSchemeVersionReference(
						csURI, refVersions.get(csURI));

			// If it is a named version, try to resolve it
			if (!StringUtils.isEmpty(versionTag)) {
				String tagVersion = rm_.getInternalVersionStringForTag(csURI,
						versionTag);
				if (!StringUtils.isEmpty(tagVersion)){
					refVersions.put(csURI, tagVersion);
					return Constructors
							.createAbsoluteCodingSchemeVersionReference(csURI,
									tagVersion);
				}
			}

			// Default to the named version - KnownTags.PRODUCTION, if it exists
			String tagVersion = null;
            try{
				tagVersion = rm_.getInternalVersionStringForTag(csURI,
            		KnownTags.PRODUCTION.toString());
				if (!StringUtils.isEmpty(tagVersion)){
	            	// Add the constructed AbsoluteCodingSchemeVersionReference to the refVersions
	            	refVersions.put(csURI, tagVersion);
	            	
	                return Constructors
	                        .createAbsoluteCodingSchemeVersionReference(csURI,
	                                tagVersion);
				}
            }
			catch (LBParameterException e) {
				// continue on
			} 
			// Take whatever is most appropriate from the service
			AbsoluteCodingSchemeVersionReferenceList serviceCsVersions = getAbsoluteCodingSchemeVersionReference(csURI);

			if (serviceCsVersions == null)
				return null;

			if (refVersions != null && refVersions.size() > 0) {
				for (AbsoluteCodingSchemeVersionReference serviceCsVersion : serviceCsVersions
						.getAbsoluteCodingSchemeVersionReference()) {
					if (refVersions.containsValue(serviceCsVersion
							.getCodingSchemeVersion()))
						return serviceCsVersion;
				}
			}

			if (serviceCsVersions
					.getAbsoluteCodingSchemeVersionReferenceCount() > 0) {
				refVersions.put(csURI, serviceCsVersions
						.getAbsoluteCodingSchemeVersionReference(0)
						.getCodingSchemeVersion());
				return serviceCsVersions
						.getAbsoluteCodingSchemeVersionReference(0);
			}
		}
		return null;

	}

	/**
	 * Construct a (hopefully) unique key from a concept reference
	 * 
	 * @param cr
	 *            concept reference
	 * @return unique key
	 */
	protected String constructKey(ConceptReference cr) {
		return (StringUtils.isEmpty(cr.getCodeNamespace()) ? cr
				.getCodingSchemeName() : cr.getCodeNamespace())
				+ ":"
				+ cr.getCode();
	}

	/**
	 * Determine whether two concept references refer to the same thing <b>in
	 * the context of a single coding scheme!</b>
	 * 
	 * @param r1
	 *            - first concept reference
	 * @param r2
	 *            - second concept reference
	 * @return true if the references are the same within the context of a
	 *         coding scheme
	 */
	protected boolean equalReferences(ConceptReference r1, ConceptReference r2) {
		return constructKey(r1).equals(constructKey(r2));
	}

	/**
	 * Convert a concept reference list into a coded node set in the context of
	 * a particular value set definition
	 * 
	 * @param crl
	 *            - list to convert
	 * @param vdd
	 *            - context to do the conversion in
	 * @param refVersions
	 *            - set of already resolved versions (may have new versions
	 *            added)
	 * @param versionTag
	 *            - the versionTag to use if more than one version of the coding
	 *            scheme exists
	 * @return corresponding coded node set
	 * @throws LBException
	 */
	protected CodedNodeSet conceptReferenceListToCodedNodeSet(
			ConceptReferenceList crl, ValueSetDefinition vdd,
			HashMap<String, String> refVersions, String versionTag)
			throws LBException {
		HashMap<String, ConceptReferenceList> csConcepts = new HashMap<String, ConceptReferenceList>();
		Iterator<? extends ConceptReference> crli = crl
				.iterateConceptReference();

		CodedNodeSet mergedNodeSet = null;

		// Split the list among the target coding schemes
		while (crli.hasNext()) {
			ConceptReference cr = crli.next();
			String crCs = StringUtils.isEmpty(cr.getCodeNamespace()) ? cr
					.getCodingSchemeName()
					: getCodingSchemeNameForNamespaceName(vdd.getMappings(),
							cr.getCodeNamespace());
			if (!csConcepts.containsKey(crCs))
				csConcepts.put(crCs, new ConceptReferenceList());
			csConcepts.get(crCs).addConceptReference(cr);
		}

		// Create a coded node set for each coding scheme and union it
		Iterator<String> csNames = csConcepts.keySet().iterator();
		while (csNames.hasNext()) {
			String csName = csNames.next();
			CodedNodeSet csNodes = getNodeSetForCodingScheme(vdd, csName,
					refVersions, versionTag);
			if (csNodes != null) {
				if (mergedNodeSet == null)
					mergedNodeSet = csNodes.restrictToCodes(csConcepts
							.get(csName));
				else
					mergedNodeSet = mergedNodeSet.union(csNodes
							.restrictToCodes(csConcepts.get(csName)));
			} else {
				// TODO - do we want to say anything about the fact that we
				// aren't resolving all of the association contents?
			}
		}
		return mergedNodeSet;
	}

	/**
	 * Return the coded node set that represents all of the concept codes in the
	 * referenced coding scheme
	 * 
	 * @param vdd
	 *            - containing value set definition
	 * @param csName
	 *            - local name of coding scheme within the value domain
	 * @param refVersions
	 *            - map from coding scheme URI to versions. A new node will be
	 *            added to this list if the coding scheme isn't already there
	 * @param versionTag
	 *            - default version or tag
	 * @return coded node set that corresponds to this node or null if none
	 *         available
	 * @throws LBException
	 */
	protected CodedNodeSet getNodeSetForCodingScheme(ValueSetDefinition vdd,
			String csName, HashMap<String, String> refVersions,
			String versionTag) throws LBException {

		if (StringUtils.isEmpty(csName))
			csName = vdd.getDefaultCodingScheme();
		if (!StringUtils.isEmpty(csName)) {
			AbsoluteCodingSchemeVersionReference resVersion = resolveCSVersion(
					csName, vdd.getMappings(), versionTag, refVersions);
			CodingSchemeVersionOrTag verOrTag = new CodingSchemeVersionOrTag();
			verOrTag.setVersion(resVersion.getCodingSchemeVersion());
			return getLexBIGService().getCodingSchemeConcepts(
					resVersion.getCodingSchemeURN(), verOrTag)
					.restrictToStatus(ActiveOption.ACTIVE_ONLY, null);
		}
		return null;
	}

	public ValueSetDefinitionCompiler getValueSetDefinitionCompiler() {
		if (this.valueSetDefinitionCompiler == null) {
			this.valueSetDefinitionCompiler = this
					.doCreateValueSetDefinitionCompiler();
		}
		return this.valueSetDefinitionCompiler;
	}

	protected ValueSetDefinitionCompiler doCreateValueSetDefinitionCompiler() {
		return new FileSystemCachingValueSetDefinitionCompilerDecorator(
				new DefaultCompiler(this), this);
	}

	/**
	 * Generate LexGrid Coding Scheme object for the Value Set Resolution
	 * 
	 * @param vsd
	 *            Value Set Definition
	 * @param rvscns
	 *            ResolvedValueSetCodedNodeSet for the VSD
	 * @return Serialized Input Stream
	 * @throws LBException
	 */

	public InputStream exportValueSetResolutionDataToWriter(
			ValueSetDefinition vsd, ResolvedValueSetCodedNodeSet rvscns,
			LgMessageDirectorIF messager) throws LBException {
		String codingSchemeUri = vsd.getValueSetDefinitionURI();
		String codingSchemeVersion = vsd.getEntryState() == null ? "UNASSIGNED"
				: vsd.getEntryState().getContainingRevision();

		String codingSchemeName = StringUtils.isEmpty(vsd
				.getValueSetDefinitionName()) ? codingSchemeUri : vsd
				.getValueSetDefinitionName();


		CodingScheme cs = null;

		cs = new CodingScheme();


		cs.setCodingSchemeURI(codingSchemeUri);
		cs.setRepresentsVersion(codingSchemeVersion);
		if (vsd.getEffectiveDate() != null)
			cs.setEffectiveDate(vsd.getEffectiveDate());
		if (vsd.getExpirationDate() != null)
			cs.setExpirationDate(vsd.getExpirationDate());
		cs.setEntryState(vsd.getEntryState());
		cs.setFormalName(codingSchemeName);
		cs.setCodingSchemeName(truncateDefNameforCodingSchemeName(codingSchemeName));
		cs.setIsActive(vsd.getIsActive());
		cs.setMappings(vsd.getMappings());
		cs.setOwner(vsd.getOwner());
		if (vsd.getProperties()!= null) {
		    cs.setProperties(vsd.getProperties());
		} else {
			cs.setProperties(new  org.LexGrid.commonTypes.Properties());
		}
		cs.setSource(vsd.getSource());
		cs.setStatus(vsd.getStatus());

		for (AbsoluteCodingSchemeVersionReference acsvr : rvscns
				.getCodingSchemeVersionRefList()
				.getAbsoluteCodingSchemeVersionReference()) {
			Property prop = new Property();
			prop.setPropertyType(LexEVSValueSetDefinitionServices.GENERIC);
			prop.setPropertyName(LexEVSValueSetDefinitionServices.RESOLVED_AGAINST_CODING_SCHEME_VERSION);
			Text txt = new Text();
			txt.setContent(acsvr.getCodingSchemeURN());
			prop.setValue(txt);
			PropertyQualifier pq = createPropertyQualifier(
					LexEVSValueSetDefinitionServices.VERSION, acsvr.getCodingSchemeVersion());
			prop.getPropertyQualifierAsReference().add(pq);
			String csSourceName= ServiceUtility.getCodingSchemeName(acsvr.getCodingSchemeURN(), acsvr.getCodingSchemeVersion());
			if( csSourceName != null){
				PropertyQualifier pQual = createPropertyQualifier(LexEVSValueSetDefinitionServices.CS_NAME, csSourceName);
				prop.getPropertyQualifierAsReference().add(pQual);
			}
			cs.getProperties().addProperty(prop);
		}

		Entities entities = new Entities();
		Entity entity = new Entity();
		entity.setEntityCode(LexGridConstants.MR_FLAG);
		entities.addEntity(entity);
		cs.setEntities(entities);

		return marshalToXml(cs, null, rvscns.getCodedNodeSet(), 5, true, false,
				messager);
	}

	private String truncateDefNameforCodingSchemeName(String name){
		if (StringUtils.isNotEmpty(name) && name.length() > 50) {
			name = name.substring(0, 49);
		}
		return name;
	}
	private PropertyQualifier createPropertyQualifier(String name,  String value){
		PropertyQualifier pq = new PropertyQualifier();
		pq.setPropertyQualifierName(name);
		Text pqtxt = new Text();
		pqtxt.setContent(value);
		pq.setValue(pqtxt);
		return pq;
	}
	private String getSupportedCodingSchemeNameForURI(CodingScheme cs, String URI){
		for(SupportedCodingScheme scs: cs.getMappings().getSupportedCodingScheme()){
			if(scs.getUri().equals(URI)){
				return scs.getLocalId();
			}
		}
		return null;
	}
	/** The namespace cognizant marshaller. */
	private LexEVSMarshaller ns_marshaller;
	{
		ns_marshaller = new LexEVSMarshaller();
		ns_marshaller.setProperty(XMLProperties.USE_INDENTATION, "true");
		ns_marshaller.setMarshalAsDocument(true);
		ns_marshaller.setMarshalExtendedType(false);
		ns_marshaller.setSuppressNamespaces(false);
		ns_marshaller.setSchemaLocation(LexGridConstants.lgSchemaLocation); // mct
		ns_marshaller.setSupressXMLDeclaration(true);
		ns_marshaller.setSuppressXSIType(false);
		ns_marshaller.setValidation(true);
		// ns_marshaller.setEncoding("UTF-8");
		ns_marshaller.setProperty(XMLProperties.PROXY_INTERFACES,
				CastorProxy.class.getCanonicalName());
		ns_marshaller.setNamespaceMapping("lgBuiltin",
				LexGridConstants.lgBuiltin);
		ns_marshaller
				.setNamespaceMapping("lgCommon", LexGridConstants.lgCommon);
		ns_marshaller.setNamespaceMapping("lgCon", LexGridConstants.lgCon);
		ns_marshaller.setNamespaceMapping("lgCS", LexGridConstants.lgCS);
		ns_marshaller
				.setNamespaceMapping("lgNaming", LexGridConstants.lgNaming);
		ns_marshaller.setNamespaceMapping("lgRel", LexGridConstants.lgRel);
		ns_marshaller.setNamespaceMapping("lgVD", LexGridConstants.lgVD);
		ns_marshaller.setNamespaceMapping("lgVer", LexGridConstants.lgVer);
		ns_marshaller.setNamespaceMapping("xsi", LexGridConstants.lgXSI); // mct
		ns_marshaller.setInternalContext(ns_marshaller.getInternalContext());

	}

	/**
	 * This method exports the data in LexGRID XML format to a PipedWriter which
	 * can be read using Reader as it writes.
	 * 
	 * @throws LBException
	 */
	private InputStream marshalToXml(final Object obj,
			final CodedNodeGraph cng, final CodedNodeSet cns,
			final int pageSize, final boolean useStreaming,
			final boolean validate, final LgMessageDirectorIF messager)
			throws LBException {
		final PipedReader in = new PipedReader();
		final Marshaller marshaller = ns_marshaller;
		try {
			new Thread(new Runnable() {
				PipedWriter out = new PipedWriter(in);

				public void run() {
					try {
						MarshalListener listener = null;
						if (useStreaming == true) {
							listener = new StreamingLexGridMarshalListener(
									marshaller, cng, cns, pageSize, messager);
						} else {
							listener = new LexGridMarshalListener(marshaller,
									cng, cns, pageSize);
						}

						marshaller.setValidation(validate);
						marshaller.setMarshalListener(listener);
						marshaller.setWriter(out);
						marshaller.marshal(obj);
						out.close(); // close the writer after the marshaling
										// job done
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			}).start();
		} catch (IOException e) {
			messager.error("Problem marshalling value set resolution : "
					+ e.getMessage());
			throw new LBException("Problem marshalling value set resolution : "
					+ e.getMessage());
		}

		InputStream is = null;
		if (in != null)
			is = new ReaderInputStream(in);

		return is;
	}
}