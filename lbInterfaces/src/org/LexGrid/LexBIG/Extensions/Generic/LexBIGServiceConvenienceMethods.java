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
package org.LexGrid.LexBIG.Extensions.Generic;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList;
import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.annotations.LgClientSideSafe;
import org.LexGrid.naming.SupportedHierarchy;
import org.LexGrid.naming.SupportedProperty;

/**
 * Convenience methods to be implemented as a generic extension of the LexBIG
 * API.
 */
public interface LexBIGServiceConvenienceMethods extends GenericExtension {
	public enum HierarchyPathResolveOption {
		ALL, ONE, ONE_PER_HIERARCHY, ONE_PER_ROOT
	};

	/**
	 * @return LexBIGService
	 * 
	 *         Return the associated LexBIGService instance; lazy initialized as
	 *         required.
	 */
	@LgClientSideSafe
	LexBIGService getLexBIGService();

	/**
	 * Assign the associated LexBIGService instance.
	 * <p>
	 * Note: This method must be invoked by users of the distributed LexBIG API
	 * to set the service to an EVSApplicationService object, allowing client
	 * side implementations to use these convenience methods.
	 */
	@LgClientSideSafe
	void setLexBIGService(LexBIGService lbs);

	/**
	 * Create a CodedNodeSet from a set of concept codes in a coding scheme.
	 * 
	 * @param conceptCodes
	 *            The concept codes for included items.
	 * @param codingScheme
	 *            The local name or URN of the coding scheme.
	 * @param versionOrTag
	 *            The assigned tag/label or absolute version identifier of the
	 *            coding scheme.
	 * @return A CodedNodeSet representing the corresponding coded entries.
	 * @throws LBException
	 */
	CodedNodeSet createCodeNodeSet(String[] conceptCodes, String codingScheme,
			CodingSchemeVersionOrTag versionOrTag) throws LBException;

	/**
	 * Returns the entity description for the given code.
	 * 
	 * @param codingScheme
	 *            The local name or URN of the coding scheme.
	 * @param versionOrTag
	 *            The assigned tag/label or absolute version identifier of the
	 *            coding scheme.
	 * @param code
	 *            The code to resolve.
	 * @return The entity description associated with the code, or null if not
	 *         available.
	 * @throws LBException
	 */
	String getEntityDescription(String codingScheme,
			CodingSchemeVersionOrTag versionOrTag, String code)
			throws LBException;

	/**
	 * Returns the identifiers for hierarchical relationships available for
	 * navigation within a coding scheme. These identifiers can be submitted to
	 * the getHierarchyBroader() or getHierarchyNarrower() methods to navigate
	 * corresponding tree structures.
	 * <p>
	 * Possible return values are defined by the LexBIG model (see
	 * http://informatics.mayo.edu/LexGrid/downloads/LexGrid%20Model/
	 * schemas/2008/01/EAwebpublish/index.htm).
	 * 
	 * @param codingScheme
	 *            The local name or URN of the coding scheme.
	 * @param versionOrTag
	 *            The assigned tag/label or absolute version identifier of the
	 *            coding scheme.
	 * @return The array of identifiers; empty if no hierarchies are explicitly
	 *         defined (the ontology is 'flat').
	 * 
	 * @throws LBException
	 */
	String[] getHierarchyIDs(String codingScheme,
			CodingSchemeVersionOrTag versionOrTag) throws LBException;

	/**
	 * Returns all root nodes for the given hierarchy and coding scheme. Each
	 * root concept represents the conceptual start or narrowest point of a tree
	 * when visualizing the hierarchy.
	 * 
	 * @param codingScheme
	 *            The local name or URN of the coding scheme.
	 * @param versionOrTag
	 *            The assigned tag/label or absolute version identifier of the
	 *            coding scheme.
	 * @param hierarchyID
	 *            Identifies the type of hierarchy being traversed. Supported
	 *            values for the coding scheme are retrievable through the
	 *            getHierarchyIDs() method. If null, roots for all registered
	 *            hierarchies are returned.
	 * @return The collection of references to root nodes; empty if the given
	 *         hierarchy is not recognized or is unfulfilled by the given coding
	 *         scheme and version.
	 * @throws LBException
	 */
	ResolvedConceptReferenceList getHierarchyRoots(String codingScheme,
			CodingSchemeVersionOrTag versionOrTag, String hierarchyID)
			throws LBException;

	/**
	 * Returns all root nodes for the given hierarchy and coding scheme. Each
	 * root concept represents the conceptual start or narrowest point of a tree
	 * when visualizing the hierarchy.
	 * 
	 * @param codingScheme
	 *            The local name or URN of the coding scheme.
	 * @param versionOrTag
	 *            The assigned tag/label or absolute version identifier of the
	 *            coding scheme.
	 * @param hierarchyID
	 *            Identifies the type of hierarchy being traversed. Supported
	 *            values for the coding scheme are retrievable through the
	 *            getHierarchyIDs() method. If null, roots for all registered
	 *            hierarchies are returned.
	 * @param resolveConcepts
	 *            True to fill in all information for concepts identified as
	 *            part of the returned association; false to return only basic
	 *            references (e.g. code, coding scheme, and description). If
	 *            false, additional properties for referenced concepts can be
	 *            resolved on an item-by-item basis as controlled by the
	 *            application.
	 * @return The collection of references to root nodes; empty if the given
	 *         hierarchy is not recognized or is unfulfilled by the given coding
	 *         scheme and version.
	 * @throws LBException
	 */
	public ResolvedConceptReferenceList getHierarchyRoots(String codingScheme,
			CodingSchemeVersionOrTag versionOrTag, String hierarchyID,
			boolean resolveConcepts) throws LBException;

	/**
	 * Returns all concepts that can not be reached by traversing the hierarchy
	 * specified by the hierarchyId from the root.
	 * 
	 * @param codingScheme
	 *            The local name or URN of the coding scheme.
	 * @param versionOrTag
	 *            The assigned tag/label or absolute version identifier of the
	 *            coding scheme.
	 * @param hierarchyID
	 *            Identifies the type of hierarchy being traversed. Supported
	 *            values for the coding scheme are retrievable through the
	 *            getHierarchyIDs() method. If null, roots for all registered
	 *            hierarchies are returned.
	 * @return The collection orphaned concepts; empty if the given hierarchy is
	 *         not recognized or is unfulfilled by the given coding scheme and
	 *         version.
	 * @throws LBException
	 */
	ResolvedConceptReferenceList getHierarchyOrphanedConcepts(
			String codingScheme, CodingSchemeVersionOrTag versionOrTag,
			String hierarchyID) throws LBException;

	/**
	 * Returns all root nodes for the given hierarchy as a CodeNodeSet, which
	 * can be further restricted. Each root concept represents the conceptual
	 * start or narrowest point of a tree when visualizing the hierarchy.
	 * 
	 * @param codingScheme
	 *            The local name or URN of the coding scheme.
	 * @param versionOrTag
	 *            The assigned tag/label or absolute version identifier of the
	 *            coding scheme.
	 * @param hierarchyID
	 *            Identifies the type of hierarchy being traversed. Supported
	 *            values for the coding scheme are retrievable through the
	 *            getHierarchyIDs() method. If null, roots for all registered
	 *            hierarchies are returned.
	 * @return The collection of references to root nodes; empty if the given
	 *         hierarchy is not recognized or is unfulfilled by the given coding
	 *         scheme and version.
	 * @throws LBException
	 */
	CodedNodeSet getHierarchyRootSet(String codingScheme,
			CodingSchemeVersionOrTag versionOrTag, String hierarchyID)
			throws LBException;

	/**
	 * Return a representation of associations between a concept and its
	 * immediate descendants. The resolved association list represents the next
	 * branch of the hierarchy when visualized in a top (root) to bottom (leaf)
	 * representation. This method checks for the existence of a path to root
	 * and is equivalent to calling getHierarchyLevelNext with
	 * checkForHasHierarchyPathToRoot set to true.
	 * 
	 * @param codingScheme
	 *            The local name or URN of the coding scheme.
	 * @param versionOrTag
	 *            The assigned tag/label or absolute version identifier of the
	 *            coding scheme.
	 * @param hierarchyID
	 *            Identifies the type of hierarchy being traversed. Supported
	 *            values for the coding scheme are retrievable through the
	 *            getHierarchyIDs() method. If null, associations for all
	 *            registered hierarchies are navigated (less precise, but
	 *            improves performance).
	 * @param conceptCode
	 *            The starting point for resolution; not null.
	 * @param resolveConcepts
	 *            True to fill in all information for concepts identified as
	 *            part of the returned association; false to return only basic
	 *            references (e.g. code, coding scheme, and description). If
	 *            false, additional properties for referenced concepts can be
	 *            resolved on an item-by-item basis as controlled by the
	 *            application.
	 * @param associationQualifiers
	 *            Restrict to associations with specific qualifiers (e.g.
	 *            associations might be tagged with source-specific
	 *            information); null or empty to ignore qualifications.
	 * @return The list of associations and referenced concepts representing
	 *         immediate descendant within the hierarchy; empty if no items are
	 *         found.
	 * @throws LBException
	 */
	AssociationList getHierarchyLevelNext(String codingScheme,
			CodingSchemeVersionOrTag versionOrTag, String hierarchyID,
			String conceptCode, boolean resolveConcepts,
			NameAndValueList associationQualifiers) throws LBException;

	/**
	 * Return a representation of associations between a concept and its
	 * immediate descendants. The resolved association list represents the next
	 * branch of the hierarchy when visualized in a top (root) to bottom (leaf)
	 * representation. This method allows for a boolean flag
	 * checkForHasHierarchyPathToRoot to be passed in.
	 * 
	 * @param codingScheme
	 *            The local name or URN of the coding scheme.
	 * @param versionOrTag
	 *            The assigned tag/label or absolute version identifier of the
	 *            coding scheme.
	 * @param hierarchyID
	 *            Identifies the type of hierarchy being traversed. Supported
	 *            values for the coding scheme are retrievable through the
	 *            getHierarchyIDs() method. If null, associations for all
	 *            registered hierarchies are navigated (less precise, but
	 *            improves performance).
	 * @param conceptCode
	 *            The starting point for resolution; not null.
	 * @param resolveConcepts
	 *            True to fill in all information for concepts identified as
	 *            part of the returned association; false to return only basic
	 *            references (e.g. code, coding scheme, and description). If
	 *            false, additional properties for referenced concepts can be
	 *            resolved on an item-by-item basis as controlled by the
	 *            application.
	 * @param checkForHasHierarchyPathToRoot
	 *            True checks for the existence of a path from the concept to
	 *            the root. This check slows down the processing as hierarchy
	 *            checks have to be made. If no path is found, the method
	 *            returns an empty AssociationList
	 * @param associationQualifiers
	 *            Restrict to associations with specific qualifiers (e.g.
	 *            associations might be tagged with source-specific
	 *            information); null or empty to ignore qualifications.
	 * @return The list of associations and referenced concepts representing
	 *         immediate descendant within the hierarchy; empty if no items are
	 *         found.
	 * @throws LBException
	 */
	AssociationList getHierarchyLevelNext(String codingScheme,
			CodingSchemeVersionOrTag versionOrTag, String hierarchyID,
			String conceptCode, boolean resolveConcepts,
			boolean checkForHasHierarchyPathToRoot,
			NameAndValueList associationQualifiers) throws LBException;

	/**
	 * Return a representation of associations between a concept and its
	 * immediate ancestor(s). The resolved association list represents the
	 * previous level of the hierarchy when visualized in a top (root) to bottom
	 * (leaf) representation. This method checks for the existence of a path to
	 * root and is equivalent to calling getHierarchyLevelPrev with
	 * checkForHasHierarchyPathToRoot set to true. The check slows down the
	 * computation.
	 * 
	 * @param codingScheme
	 *            The local name or URN of the coding scheme.
	 * @param versionOrTag
	 *            The assigned tag/label or absolute version identifier of the
	 *            coding scheme.
	 * @param hierarchyID
	 *            Identifies the type of hierarchy being traversed. Supported
	 *            values for the coding scheme are retrievable through the
	 *            getHierarchyIDs() method. If null, associations for all
	 *            registered hierarchies are navigated (less precise, but
	 *            improves performance).
	 * @param conceptCode
	 *            The starting point for resolution; not null.
	 * @param resolveConcepts
	 *            True to fill in all information for concepts identified as
	 *            part of the returned association; false to return only basic
	 *            references (e.g. code, coding scheme, and description). If
	 *            false, additional properties for referenced concepts can be
	 *            resolved on an item-by-item basis as controlled by the
	 *            application.
	 * @param associationQualifiers
	 *            Restrict to associations with specific qualifiers (e.g.
	 *            associations might be tagged with source-specific
	 *            information); null or empty to ignore qualifications.
	 * @return The list of associations and referenced concepts representing the
	 *         immediate ancestor(s) within the hierarchy; empty if no items are
	 *         found.
	 * @throws LBException
	 */
	AssociationList getHierarchyLevelPrev(String codingScheme,
			CodingSchemeVersionOrTag versionOrTag, String hierarchyID,
			String conceptCode, boolean resolveConcepts,
			NameAndValueList associationQualifiers) throws LBException;

	/**
	 * Return a representation of associations between a concept and its
	 * immediate ancestor(s). The resolved association list represents the
	 * previous level of the hierarchy when visualized in a top (root) to bottom
	 * (leaf) representation. This method allows for a boolean flag
	 * checkForHasHierarchyPathToRoot to be passed in.
	 * 
	 * @param codingScheme
	 *            The local name or URN of the coding scheme.
	 * @param versionOrTag
	 *            The assigned tag/label or absolute version identifier of the
	 *            coding scheme.
	 * @param hierarchyID
	 *            Identifies the type of hierarchy being traversed. Supported
	 *            values for the coding scheme are retrievable through the
	 *            getHierarchyIDs() method. If null, associations for all
	 *            registered hierarchies are navigated (less precise, but
	 *            improves performance).
	 * @param conceptCode
	 *            The starting point for resolution; not null.
	 * @param resolveConcepts
	 *            True to fill in all information for concepts identified as
	 *            part of the returned association; false to return only basic
	 *            references (e.g. code, coding scheme, and description). If
	 *            false, additional properties for referenced concepts can be
	 *            resolved on an item-by-item basis as controlled by the
	 *            application.
	 * @param checkForHasHierarchyPathToRoot
	 *            True checks for the existence of a path from the concept to
	 *            the root. This check slows down the processing as hierarchy
	 *            checks have to be made.
	 * @param associationQualifiers
	 *            Restrict to associations with specific qualifiers (e.g.
	 *            associations might be tagged with source-specific
	 *            information); null or empty to ignore qualifications.
	 * @return The list of associations and referenced concepts representing the
	 *         immediate ancestor(s) within the hierarchy; empty if no items are
	 *         found.
	 * @throws LBException
	 */
	AssociationList getHierarchyLevelPrev(String codingScheme,
			CodingSchemeVersionOrTag versionOrTag, String hierarchyID,
			String conceptCode, boolean resolveConcepts,
			boolean checkForHasHierarchyPathToRoot,
			NameAndValueList associationQualifiers) throws LBException;

	/**
	 * Return a count of the number of concepts at the next level in the
	 * hierarchy.
	 * 
	 * @param codingScheme
	 *            The local name or URN of the coding scheme.
	 * @param versionOrTag
	 *            The assigned tag/label or absolute version identifier of the
	 *            coding scheme.
	 * @param hierarchyID
	 *            Identifies the type of hierarchy being traversed. Supported
	 *            values for the coding scheme are retrievable through the
	 *            getHierarchyIDs() method. If null, associations for all
	 *            registered hierarchies are navigated (less precise, but
	 *            improves performance).
	 * @param conceptRef
	 *            The starting point for resolution; not null.
	 * @return The number of concepts at the next level
	 * @throws LBException
	 */
	int getHierarchyLevelNextCount(String codingSchemeName,
			CodingSchemeVersionOrTag versionOrTag, String hierarchyID,
			ConceptReference conceptRef) throws LBException;

	/**
	 * Return a count of the number of concepts at the previous level in the
	 * hierarchy.
	 * 
	 * @param codingScheme
	 *            The local name or URN of the coding scheme.
	 * @param versionOrTag
	 *            The assigned tag/label or absolute version identifier of the
	 *            coding scheme.
	 * @param hierarchyID
	 *            Identifies the type of hierarchy being traversed. Supported
	 *            values for the coding scheme are retrievable through the
	 *            getHierarchyIDs() method. If null, associations for all
	 *            registered hierarchies are navigated (less precise, but
	 *            improves performance).
	 * @param conceptRef
	 *            The starting point for resolution; not null.
	 * @return The number of concepts at the previous level
	 * @throws LBException
	 */
	int getHierarchyLevelPrevCount(String codingSchemeName,
			CodingSchemeVersionOrTag versionOrTag, String hierarchyID,
			ConceptReference conceptRef) throws LBException;

	/**
	 * Returns a ConceptReferenceList of CountConceptReference that holds the
	 * count of concepts at the next level in the hierarchy.
	 * 
	 * @param codingScheme
	 *            The local name or URN of the coding scheme.
	 * @param versionOrTag
	 *            The assigned tag/label or absolute version identifier of the
	 *            coding scheme.
	 * @param hierarchyID
	 *            Identifies the type of hierarchy being traversed. Supported
	 *            values for the coding scheme are retrievable through the
	 *            getHierarchyIDs() method. If null, associations for all
	 *            registered hierarchies are navigated (less precise, but
	 *            improves performance).
	 * @param ConceptReferenceList
	 *            The list of concepts for which we want to find counts
	 * @return ConceptReferenceList of CountConceptReference that holds the
	 *         count
	 * @throws LBException
	 */
	public ConceptReferenceList getHierarchyLevelNextCount(
			String codingSchemeName, CodingSchemeVersionOrTag versionOrTag,
			String hierarchyID, ConceptReferenceList conceptCodes)
			throws LBException;

	/**
	 * Returns a ConceptReferenceList of CountConceptReference that holds the
	 * count of concepts at the previous level in the hierarchy.
	 * 
	 * @param codingScheme
	 *            The local name or URN of the coding scheme.
	 * @param versionOrTag
	 *            The assigned tag/label or absolute version identifier of the
	 *            coding scheme.
	 * @param hierarchyID
	 *            Identifies the type of hierarchy being traversed. Supported
	 *            values for the coding scheme are retrievable through the
	 *            getHierarchyIDs() method. If null, associations for all
	 *            registered hierarchies are navigated (less precise, but
	 *            improves performance).
	 * @param ConceptReferenceList
	 *            The list of concepts for which we want to find counts
	 * @return ConceptReferenceList of CountConceptReference that holds the
	 *         count
	 * @throws LBException
	 */
	public ConceptReferenceList getHierarchyLevelPrevCount(
			String codingSchemeName, CodingSchemeVersionOrTag versionOrTag,
			String hierarchyID, ConceptReferenceList conceptCodes)
			throws LBException;
	
	/**
	 * Return a representation of associations between a concept and
	 * hierarchical root node(s). The resolved association list represents the
	 * path within the hierarchy from traversed from bottom (leaf) to top
	 * (root).
	 * 
	 * @param codingScheme
	 *            The local name or URN of the coding scheme.
	 * @param versionOrTag
	 *            The assigned tag/label or absolute version identifier of the
	 *            coding scheme.
	 * @param hierarchyID
	 *            Identifies the type of hierarchy being traversed. Supported
	 *            values for the coding scheme are retrievable through the
	 *            getHierarchyIDs() method. If null, associations for all
	 *            registered hierarchies are navigated (less precise, but
	 *            improves performance).
	 * @param conceptCode
	 *            The starting point for resolution; not null.
	 * @param resolveConcepts
	 *            True to fill in all information for concepts identified as
	 *            part of the returned association; false to return only basic
	 *            references (e.g. code, coding scheme, and description). If
	 *            false, additional properties for referenced concepts can be
	 *            resolved on an item-by-item basis as controlled by the
	 *            application.
	 * @param pathResolveOption
	 *            It is possible that more than one path can exist between the
	 *            conceptCode and hierarchy roots by navigating different
	 *            intermediate nodes. This parameter allows the number of
	 *            returned paths to be constrained.
	 * @param associationQualifiers
	 *            Restrict to associations with specific qualifiers (e.g.
	 *            associations might be tagged with source-specific
	 *            information); null or empty to ignore qualifications.
	 * @return The list of associations and referenced concepts representing the
	 *         path to root node(s) within the hierarchy; empty if no items are
	 *         found. If not empty, each association in the initial list
	 *         represents a separate path to root for the given concept.
	 * @throws LBException
	 */
	
	AssociationList getHierarchyPathToRoot(String codingScheme,
			CodingSchemeVersionOrTag versionOrTag, String hierarchyID,
			String conceptCode, boolean resolveConcepts,
			HierarchyPathResolveOption pathResolveOption,
			NameAndValueList associationQualifiers) throws LBException;
	/**
	 * Return a representation of associations between a concept and
	 * hierarchical root node(s). The resolved association list represents the
	 * path within the hierarchy from traversed from bottom (leaf) to top
	 * (root).
	 * 
	 * @param codingScheme
	 *            The local name or URN of the coding scheme.
	 * @param versionOrTag
	 *            The assigned tag/label or absolute version identifier of the
	 *            coding scheme.
	 * @param hierarchyID
	 *            Identifies the type of hierarchy being traversed. Supported
	 *            values for the coding scheme are retrievable through the
	 *            getHierarchyIDs() method. If null, associations for all
	 *            registered hierarchies are navigated (less precise, but
	 *            improves performance).
	 * @param conceptCode
	 *            The starting point for resolution; not null.
	 * @param codeNamespace 
	 * @param resolveConcepts
	 *            True to fill in all information for concepts identified as
	 *            part of the returned association; false to return only basic
	 *            references (e.g. code, coding scheme, and description). If
	 *            false, additional properties for referenced concepts can be
	 *            resolved on an item-by-item basis as controlled by the
	 *            application.
	 * @param pathResolveOption
	 *            It is possible that more than one path can exist between the
	 *            conceptCode and hierarchy roots by navigating different
	 *            intermediate nodes. This parameter allows the number of
	 *            returned paths to be constrained.
	 * @param associationQualifiers
	 *            Restrict to associations with specific qualifiers (e.g.
	 *            associations might be tagged with source-specific
	 *            information); null or empty to ignore qualifications.
	 * @return The list of associations and referenced concepts representing the
	 *         path to root node(s) within the hierarchy; empty if no items are
	 *         found. If not empty, each association in the initial list
	 *         represents a separate path to root for the given concept.
	 * @throws LBException
	 */
	AssociationList getHierarchyPathToRoot(String codingScheme,
			CodingSchemeVersionOrTag versionOrTag, String hierarchyID,
			String conceptCode, String codeNamespace, boolean resolveConcepts,
			HierarchyPathResolveOption pathResolveOption,
			NameAndValueList associationQualifiers) throws LBException;
	/**
	 * Return the path/transitive closure path between two nodes.
	 * @param codingSchemeUri
	 * @param versionOrTag
	 * @param containerName
	 * @param associationName
	 * @param sourceCode
	 * @param sourceNS
	 * @param targetCode
	 * @param targetNS
	 * @return ResolvedConceptReference, which contains the path
	 * @throws LBParameterException
	 */
	ResolvedConceptReference getNodesPath(String codingSchemeUri,
			CodingSchemeVersionOrTag versionOrTag, String containerName,
			String associationName, String sourceCode, String sourceNS,
			String targetCode, String targetNS) throws LBParameterException;

	/**
	 * Return detailed rendering information (including coding scheme summary,
	 * version and status information, reference links, etc) for the given
	 * coding scheme; null if not available.
	 * 
	 * @param codingScheme
	 *            The local name or URN of the coding scheme.
	 * @param versionOrTag
	 *            The assigned tag/label or absolute version identifier of the
	 *            coding scheme.
	 * @throws LBException
	 */
	CodingSchemeRendering getRenderingDetail(String codingScheme,
			CodingSchemeVersionOrTag versionOrTag) throws LBException;

	/**
	 * Indicates if the given code is considered retired or inactive within
	 * context of the provided scheme.
	 * 
	 * @param conceptCode
	 *            The concept code to evaluate.
	 * @param codingScheme
	 *            The local name or URN of the coding scheme to query.
	 * @param versionOrTag
	 *            The assigned tag/label or absolute version identifier of the
	 *            coding scheme to query.
	 * @return true if retired; false otherwise
	 * @throws LBException
	 */
	boolean isCodeRetired(String conceptCode, String codingScheme,
			CodingSchemeVersionOrTag versionOrTag) throws LBException;

	/**
	 * Return the coding schemes who supported association matching with
	 * associationName. The search is performed only for loaded coding schemes.
	 * 
	 * @param associationName
	 *            Association name to search for. It is case sensitive.
	 * @return List of coding schemes who has supported association matching
	 *         with the value of associationName
	 * @throws LBException
	 */

	public CodingSchemeRenderingList getCodingSchemesWithSupportedAssociation(
			String associationName) throws LBException;

	/**
	 * Return the copyright text for the coding scheme.
	 * 
	 * @param codingScheme
	 *            The local name or URN of the coding scheme.
	 * @param versionOrTag
	 *            The assigned tag/label or absolute version identifier of the
	 *            coding scheme.
	 * @throws LBException
	 */
	public String getCodingSchemeCopyright(String codingScheme,
			CodingSchemeVersionOrTag versionOrTag) throws LBException;

	/**
	 * Return the Association Name given an Association Entity Code.
	 * 
	 * @param codingScheme
	 *            The local name or URN of the coding scheme.
	 * @param versionOrTag
	 *            The assigned tag/label or absolute version identifier of the
	 *            coding scheme.
	 * @param entityCode
	 *            The Association Entity Code.
	 * @return The Association Name.
	 * @throws LBException
	 */
	public String getAssociationNameFromAssociationCode(String codingScheme,
			CodingSchemeVersionOrTag versionOrTag, String entityCode)
			throws LBException;

	/**
	 * Return the Association Entity Code given an Association Name.
	 * 
	 * @param codingScheme
	 *            The local name or URN of the coding scheme.
	 * @param versionOrTag
	 *            The assigned tag/label or absolute version identifier of the
	 *            coding scheme.
	 * @param associationName
	 *            The Association Name.
	 * @return The Association Entity Code.
	 * @throws LBException
	 */
	public String getAssociationCodeFromAssociationName(String codingScheme,
			CodingSchemeVersionOrTag versionOrTag, String associationName)
			throws LBException;

	/**
	 * Return all the association forward name and reverse name for the coding
	 * scheme.
	 * 
	 * @param codingScheme
	 *            The local name or URN of the coding scheme.
	 * @param versionOrTag
	 *            The assigned tag/label or absolute version identifier of the
	 *            coding scheme.
	 * @throws LBException
	 */
	public String[] getAssociationForwardAndReverseNames(String codingScheme,
			CodingSchemeVersionOrTag versionOrTag) throws LBException;

	/**
	 * Return the forward name for the identified association.
	 * 
	 * @param association
	 *            Primary name of the association.
	 * @param codingScheme
	 *            The local name or URN of the coding scheme.
	 * @param versionOrTag
	 *            The assigned tag/label or absolute version identifier of the
	 *            coding scheme.
	 * @throws LBException
	 */
	public String getAssociationForwardName(String association,
			String codingScheme, CodingSchemeVersionOrTag versionOrTag)
			throws LBException;

	/**
	 * Return all the association forward name for the coding scheme.
	 * 
	 * @param codingScheme
	 *            The local name or URN of the coding scheme.
	 * @param versionOrTag
	 *            The assigned tag/label or absolute version identifier of the
	 *            coding scheme.
	 * @throws LBException
	 */
	public String[] getAssociationForwardNames(String codingScheme,
			CodingSchemeVersionOrTag versionOrTag) throws LBException;

	/**
	 * Return the reverse name for the identified association.
	 * 
	 * @param association
	 *            Basic (non-directional) name of the association.
	 * @param codingScheme
	 *            The local name or URN of the coding scheme.
	 * @param versionOrTag
	 *            The assigned tag/label or absolute version identifier of the
	 *            coding scheme.
	 * @throws LBException
	 */
	public String getAssociationReverseName(String association,
			String codingScheme, CodingSchemeVersionOrTag versionOrTag)
			throws LBException;

	/**
	 * Return all the association reverse name for the coding scheme.
	 * 
	 * @param codingScheme
	 *            The local name or URN of the coding scheme.
	 * @param versionOrTag
	 *            The assigned tag/label or absolute version identifier of the
	 *            coding scheme.
	 * @throws LBException
	 */
	public String[] getAssociationReverseNames(String codingScheme,
			CodingSchemeVersionOrTag versionOrTag) throws LBException;

	/**
	 * Return true if directionalName is the forward name of an association for
	 * the coding scheme.
	 * 
	 * @param codingScheme
	 *            The local name or URN of the coding scheme.
	 * @param versionOrTag
	 *            The assigned tag/label or absolute version identifier of the
	 *            coding scheme.
	 * @param directionalName
	 *            The directionalName string
	 * @throws LBException
	 */
	public boolean isForwardName(String codingScheme,
			CodingSchemeVersionOrTag versionOrTag, String directionalName)
			throws LBException;

	/**
	 * Return true if directionalName is the reverse name of an association for
	 * the coding scheme.
	 * 
	 * @param codingScheme
	 *            The local name or URN of the coding scheme.
	 * @param versionOrTag
	 *            The assigned tag/label or absolute version identifier of the
	 *            coding scheme.
	 * @param directionalName
	 *            The directionalName string
	 * @throws LBException
	 */
	public boolean isReverseName(String codingScheme,
			CodingSchemeVersionOrTag versionOrTag, String directionalName)
			throws LBException;

	/**
	 * Gets the association names that contain a directional name.
	 * 
	 * @param codingScheme
	 *            The local name or URN of the coding scheme.
	 * @param versionOrTag
	 *            The assigned tag/label or absolute version identifier of the
	 *            coding scheme.
	 * @param directionalName
	 *            The directionalName string
	 * @throws LBException
	 */
	public String[] getAssociationNameForDirectionalName(String codingScheme,
			CodingSchemeVersionOrTag versionOrTag, String directionalName)
			throws LBException;

	/**
	 * Return a representation of the association between the concepts with the
	 * given code in the reverse direction of the association in the specified
	 * relation containerName.
	 * 
	 * @param conceptCode
	 *            ConceptCode to lookup.
	 * @param relationContainerName
	 *            The relations container to query. If null, the native
	 *            relations container for the code system will be assumed.
	 * @param association
	 *            Local name of the specific relation/association to evaluate
	 *            ("hasSubtype", etc).
	 * @param codingScheme
	 *            The local name or URN of the coding scheme to query.
	 * @param versionOrTag
	 *            The assigned tag/label or absolute version identifier of the
	 *            coding scheme.
	 * @param buildReferencedEntries
	 *            true to resolve the full concept codes, false for skeletons
	 * @param associationQualifiers
	 *            Restrict to associations with specific qualifiers, for example
	 *            from a specific source
	 * @throws LBException
	 */
	Association getAssociationReverseOneLevel(String conceptCode,
			String relationContainerName, String association,
			String codingScheme, CodingSchemeVersionOrTag versionOrTag,
			boolean buildReferencedEntries,
			NameAndValueList associationQualifiers) throws LBException;

	/**
	 *Return a representation of the association between the concepts with the
	 * given code in the forward direction of the association in the specified
	 * relation containerName.
	 * 
	 * @param conceptCode
	 *            The conceptCode to lookup.
	 * @param relationContainerName
	 *            The relations container to query. If null, the native
	 *            relations container for the code system will be assumed.
	 * @param association
	 *            Local name of the specific relation/association to evaluate
	 *            ("hasSubtype", etc).
	 * @param codingScheme
	 *            The local name or URN of the coding scheme to query.
	 * @param versionOrTag
	 *            The assigned tag/label or absolute version identifier of the
	 *            coding scheme.
	 * @param buildReferencedEntries
	 *            true to resolve the full concept codes, false for skeletons
	 * @param associationQualifiers
	 *            Restrict to associations with specific qualifiers, for example
	 *            from a specific source
	 * @throws LBException
	 */
	Association getAssociationForwardOneLevel(String conceptCode,
			String relationContainerName, String association,
			String codingScheme, CodingSchemeVersionOrTag versionOrTag,
			boolean buildReferencedEntries,
			NameAndValueList associationQualifiers) throws LBException;

	/**
	 * Returns an array of hierarchies supported by the given coding scheme and
	 * matching the specified ID. If the ID is null, return all registered
	 * hierarchies (any ID).
	 * 
	 * @param codingScheme
	 * @param versionOrTag
	 * @param hierarchyId
	 * @return SupportedHierarchy
	 * @throws LBException
	 */

	public SupportedHierarchy[] getSupportedHierarchies(String codingScheme,
			CodingSchemeVersionOrTag versionOrTag, String hierarchyId)
			throws LBException;

	/**
	 * Returns an list of properties supported with propertyType of
	 * 'presentation'.
	 * 
	 * @param codingScheme
	 * @param versionOrTag
	 * @return SupportedProperty
	 * @throws LBException
	 */

	public List<SupportedProperty> getSupportedPropertiesOfTypePresentation(
			String codingScheme, CodingSchemeVersionOrTag versionOrTag)
			throws LBException;

	/**
	 * Returns an list of properties supported with propertyType of 'comment'.
	 * 
	 * @param codingScheme
	 * @param versionOrTag
	 * @return SupportedProperty
	 * @throws LBException
	 */

	public List<SupportedProperty> getSupportedPropertiesOfTypeComment(
			String codingScheme, CodingSchemeVersionOrTag versionOrTag)
			throws LBException;

	/**
	 * Returns an list of properties supported with propertyType of
	 * 'definition'.
	 * 
	 * @param codingScheme
	 * @param versionOrTag
	 * @return SupportedProperty
	 * @throws LBException
	 */

	public List<SupportedProperty> getSupportedPropertiesOfTypeDefinition(
			String codingScheme, CodingSchemeVersionOrTag versionOrTag)
			throws LBException;

	/**
	 * Returns an list of properties supported with propertyType of 'property'.
	 * 
	 * @param codingScheme
	 * @param versionOrTag
	 * @return SupportedProperty
	 * @throws LBException
	 */

	public List<SupportedProperty> getSupportedPropertiesOfTypeProperty(
			String codingScheme, CodingSchemeVersionOrTag versionOrTag)
			throws LBException;

	/**
	 * Add LuceneIndexes for the given list of concept.
	 * 
	 * @param codingSchemeName
	 * @param versionOrTag
	 * @param entityCodes
	 * @throws LBException
	 */
	public void addEntityLuceneIndexes(String codingSchemeName,
			CodingSchemeVersionOrTag versionOrTag, List<String> entityCodes)
			throws LBException;

	/**
	 * Remove LuceneIndexes for the given list of concept.
	 * 
	 * @param codingSchemeName
	 * @param versionOrTag
	 * @param entityCodes
	 * @throws LBException
	 */
	public void removeEntityLuceneIndexes(String codingSchemeName,
			CodingSchemeVersionOrTag versionOrTag, List<String> entityCodes)
			throws LBException;

	/**
	 * Modify LuceneIndexes for the given list of concept.
	 * 
	 * @param codingSchemeName
	 * @param versionOrTag
	 * @param entityCodes
	 * @throws LBException
	 */
	public void modifyEntityLuceneIndexes(String codingSchemeName,
			CodingSchemeVersionOrTag versionOrTag, List<String> entityCodes)
			throws LBException;
	
    /**
     * Gets the distinct namespaces of a code.
     * 
     * @param codingScheme the coding scheme
     * @param versionOrTag the version or tag
     * @param code the code
     * 
     * @return the distinct namespaces of code
     * 
     * @throws LBException the LB exception
     */
    public List<String> getDistinctNamespacesOfCode(
            String codingScheme,
            CodingSchemeVersionOrTag versionOrTag,
            final String code) throws LBException;
    
    /**
     * @param codingScheme the code system
     * @param csvt the version or tag
     * @param code the unique identifier in the code system
     * @param associationName the relationship to restrict this to
     * @param maxToReturn the maximum number of values to return
     * @return A list of concepts associated with this term via the indicated association
     * @throws LBInvocationException 
     * @throws LBParameterException
     * @throws LBException
     */
    public AssociatedConceptList getallIncomingConceptsForAssociation(String codingScheme, CodingSchemeVersionOrTag csvt,
            String code, String associationName, int maxToReturn) throws LBInvocationException, LBParameterException, LBException;
    
    /**
     * @param codingScheme the coding scheme
     * @param versionOrTag the version or tag
     * @param code the unique identifier in the code system
     * @param association the relationship to restrict this to
     * @return a list of all ancestor concepts for this term and relationship
     * @throws LBParameterException caused by an incorrect parameter for this method
     */
    public List<ResolvedConceptReference> getAncestorsInTransitiveClosure( String codingScheme,
            CodingSchemeVersionOrTag versionOrTag, final String code, final String association) throws LBParameterException;
    /**
     * @param codingScheme the coding scheme
     * @param versionOrTag the version or tag
     * @param code unique id in the code system
     * @param association relationship to restrict to
     * @return List of all concepts in the descendant path
     * @throws LBParameterException incorrect parameter will cause this
     */
    public List<ResolvedConceptReference> getDescendentsInTransitiveClosure( String codingScheme,
            CodingSchemeVersionOrTag versionOrTag, final String code, final String association) throws LBParameterException;
    
    /**
     * 
     * @param codingScheme the coding scheme focus of the search
     * @param versionOrTag coding scheme version
     * @param codes that are top nodes of a domain
     * @param association the relationship name that determines the domain
     * @param matchText text to match domain members
     * @return ResolvedConceptReferenceList of search results from discrete domain
     * @throws LBParameterException
     */
    public ResolvedConceptReferenceList searchDescendentsInTransitiveClosure( String codingScheme,
            CodingSchemeVersionOrTag versionOrTag, final List<String> codes, final String association, final String matchText, String matchAlgorithm,
            SearchDesignationOption searchOption,
            LocalNameList sources) throws LBParameterException;
    
    /**
     * 
     * @param codingScheme the coding scheme focus of the search
     * @param versionOrTag coding scheme version
     * @param codes that are top nodes of a domain
     * @param association the relationship name that determines the domain
     * @param matchText text to match domain members
     * @return ResolvedConceptReferenceList of search results from discrete domain
     * @throws LBParameterException
     */
    public ResolvedConceptReferenceList searchAscendentsInTransitiveClosure( String codingScheme,
            CodingSchemeVersionOrTag versionOrTag, final List<String> codes, final String association, final String matchText, String matchAlgorithm,
            SearchDesignationOption searchOption,
            LocalNameList sources) throws LBParameterException;
    
}