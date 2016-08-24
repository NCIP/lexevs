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
package org.LexGrid.LexBIG.Utility;

import java.io.Serializable;

import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.SortOption;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods;
import org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods.HierarchyPathResolveOption;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;

/**
 * Convenience methods that have been implemented against the LexBIG API. This
 * class doesn't contain any of the implementation - rather, it is a one stop
 * shop for all of the other convenience methods.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:kanjamala.pradip@mayo.edu">Pradip Kanjamala</A>
 * @author <A HREF="mailto:johnson.thomas@mayo.edu">Thomas Johnson</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
@SuppressWarnings("serial")
public class ConvenienceMethods implements Serializable {
    LexBIGServiceConvenienceMethods lbcm_;

    /**
     * Serialization constructor for remote interface only
     * 
     * @throws LBInvocationException
     * @throws LBParameterException
     * 
     */
    public ConvenienceMethods() throws LBParameterException, LBInvocationException {
        lbcm_ = (LexBIGServiceConvenienceMethods) LexBIGServiceImpl.defaultInstance().getGenericExtension(
                "LexBIGServiceConvenienceMethods");
    }

    public ConvenienceMethods(LexBIGService lbs) throws LBException {
        lbcm_ = (LexBIGServiceConvenienceMethods) lbs.getGenericExtension("LexBIGServiceConvenienceMethods");
        lbcm_.setLexBIGService(lbs);
    }

    /**
     * Add all associations in the source to the target list.
     */
    public static void addAll(AssociationList srcList, AssociationList tgtList) {
        if (srcList != null && tgtList != null)
            for (int i = 0; i < srcList.getAssociationCount(); i++)
                tgtList.addAssociation(srcList.getAssociation(i));
    }

    /**
     * Create a LocalNameList out of a set of Strings.
     */
    public static LocalNameList createLocalNameList(String[] entries) {
        return Constructors.createLocalNameList(entries);
    }

    /**
     * Create a "PRODUCTION" tag
     */
    public static CodingSchemeVersionOrTag createProductionTag() {
        return Constructors.createProductionTag();
    }

    /**
     * Create an AbsoluteCodingSchemeReference
     */
    public static AbsoluteCodingSchemeVersionReference createAbsoluteCodingSchemeVersionReference(String urn,
            String version) {
        return Constructors.createAbsoluteCodingSchemeVersionReference(urn, version);
    }

    /**
     * Create an AbsoluteCodingSchemeReference
     */
    public static AbsoluteCodingSchemeVersionReference createAbsoluteCodingSchemeVersionReference(
            CodingSchemeSummary summary) {
        return Constructors.createAbsoluteCodingSchemeVersionReference(summary);
    }

    /**
     * Create a LocalNameList from a single entry.
     */
    public static LocalNameList createLocalNameList(String entry) {
        return Constructors.createLocalNameList(entry);
    }

    /**
     * Create a NameAndValueList from a single concept name.
     */
    public static NameAndValueList createNameAndValueList(String name) {
        return Constructors.createNameAndValueList(name);
    }

    /**
     * Create a NameAndValueList from multiple names.
     */
    public static NameAndValueList createNameAndValueList(String[] names) {
        return Constructors.createNameAndValueList(names);
    }

    /**
     * Create a NameAndValue from a name / value combination.
     */
    public static NameAndValue createNameAndValue(String name, String value) {
        return Constructors.createNameAndValue(name, value);
    }

    /**
     * Create a NameAndValueList from multiple names / value combination.
     */
    public static NameAndValueList createNameAndValueList(String[] names, String value) {
        return Constructors.createNameAndValueList(names, value);
    }

    /**
     * Create a NameAndValue from a name / value combination.
     */
    public static NameAndValueList createNameAndValueList(String name, String value) {
        return Constructors.createNameAndValueList(name, value);
    }

    /**
     * Create a ConceptReference from a code / codesystem combination.
     */
    public static ConceptReference createConceptReference(String code, String codeSystem) {
        return Constructors.createConceptReference(code, codeSystem);
    }

    /**
     * Create a ConceptReferenceList from a single concept code.
     */
    public static ConceptReferenceList createConceptReferenceList(String code) {
        return Constructors.createConceptReferenceList(code);
    }

    /**
     * Create a conceptReferenceList from multiple codes.
     */
    public static ConceptReferenceList createConceptReferenceList(String[] codes) {
        return Constructors.createConceptReferenceList(codes);
    }

    /**
     * Create a ConceptReferenceList from a single concept / codesystem
     * combination.
     */
    public static ConceptReferenceList createConceptReferenceList(String code, String codeSystem) {
        return Constructors.createConceptReferenceList(code, codeSystem);
    }

    /**
     * Create a ConceptReferenceList from a set of codes. Assign the same code
     * system to each.
     */
    public static ConceptReferenceList createConceptReferenceList(String[] codes, String codeSystem) {
        return Constructors.createConceptReferenceList(codes, codeSystem);
    }

    public static SortOption createSortOption(String sortExtensionName, Boolean ascending) {
        return Constructors.createSortOption(sortExtensionName, ascending);
    }

    public static SortOptionList createSortOptionList(String[] sortExtensionNames, Boolean[] ascending)
            throws LBParameterException {
        return Constructors.createSortOptionList(sortExtensionNames, ascending);
    }

    public static SortOptionList createSortOptionList(String[] sortExtensionNames) {
        return Constructors.createSortOptionList(sortExtensionNames);
    }

    /**
     * Create a CodedNodeSet from a set of concept codes from a coding scheme.
     * 
     * @throws LBException
     */
    public CodedNodeSet createCodedNodeSet(String[] conceptCodes, String codingScheme,
            CodingSchemeVersionOrTag versionOrTag) throws LBException {
        return lbcm_.createCodeNodeSet(conceptCodes, codingScheme, versionOrTag);
    }

    /**
     * Returns a string which is generated by prepending "reverse_" to the
     * forward_name provided.
     * 
     * @param forward_name
     *            The forward_name of an association.
     * @return A String which is generated by prepending "reverse_" to the
     *         forward_name provided
     * 
     */
    public String generateReverseNameForAssociation(String forward_name) {
        if (forward_name != null && forward_name.trim().length() != 0) {
            return "reverse_" + forward_name;
        } else {
            return "";
        }
    }

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
    public CodingSchemeRendering getRenderingDetail(String codingScheme, CodingSchemeVersionOrTag versionOrTag)
            throws LBException {
        return lbcm_.getRenderingDetail(codingScheme, versionOrTag);
    }

    /**
     * Return copyright text for the given coding scheme; null if not available.
     * 
     * @param codingScheme
     *            The local name or URN of the coding scheme.
     * @param versionOrTag
     *            The assigned tag/label or absolute version identifier of the
     *            coding scheme.
     * @throws LBException
     */
    public String getCopyright(String codingScheme, CodingSchemeVersionOrTag versionOrTag) throws LBException {
        return lbcm_.getCodingSchemeCopyright(codingScheme, versionOrTag);
    }

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

    public CodingSchemeRenderingList getCodingSchemesWithSupportedAssociation(String associationName)
            throws LBException {
        return lbcm_.getCodingSchemesWithSupportedAssociation(associationName);
    }

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

    public String[] getAssociationForwardAndReverseNames(String codingScheme, CodingSchemeVersionOrTag versionOrTag)
            throws LBException {
        return lbcm_.getAssociationForwardAndReverseNames(codingScheme, versionOrTag);
    }

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
    public String[] getAssociationForwardNames(String codingScheme, CodingSchemeVersionOrTag versionOrTag)
            throws LBException {
        return lbcm_.getAssociationForwardNames(codingScheme, versionOrTag);
    }

    /**
     * Return all the association reverse name for the coding scheme
     * 
     * @param codingScheme
     *            The local name or URN of the coding scheme.
     * @param versionOrTag
     *            The assigned tag/label or absolute version identifier of the
     *            coding scheme.
     * @throws LBException
     */
    public String[] getAssociationReverseNames(String codingScheme, CodingSchemeVersionOrTag versionOrTag)
            throws LBException {
        return lbcm_.getAssociationReverseNames(codingScheme, versionOrTag);
    }

    /**
     * Returns the entity description for the given code.
     * @param codingScheme
     *            The local name or URN of the coding scheme.
     * @param versionOrTag
     *            The assigned tag/label or absolute version identifier of the
     *            coding scheme.
     * @param code
     *            The code to resolve.
     * @return
     *            The entity description associated with the code, or
     *            null if not available.
     * @throws LBException
     */
    public String getEntityDescription(String codingScheme, CodingSchemeVersionOrTag versionOrTag, String code) throws LBException {
        return lbcm_.getEntityDescription(codingScheme, versionOrTag, code);
    }

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
    public String[] getHierarchyIDs(String codingScheme, CodingSchemeVersionOrTag versionOrTag) throws LBException {
        return lbcm_.getHierarchyIDs(codingScheme, versionOrTag);
    }

    /**
     * Return a representation of associations between a concept and its
     * immediate descendants. The resolved association list represents the next
     * branch of the hierarchy when visualized in a top (root) to bottom (leaf)
     * representation.
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
     *         immediate descendants within the hierarchy; empty if no items are
     *         found.
     * @throws LBException
     */
    public AssociationList getHierarchyLevelNext(String conceptCode, String hierarchyID, String codingScheme,
            CodingSchemeVersionOrTag versionOrTag, boolean resolveConcepts, NameAndValueList associationQualifiers)
            throws LBException {
        return lbcm_.getHierarchyLevelNext(codingScheme, versionOrTag, hierarchyID, conceptCode, resolveConcepts,
                associationQualifiers);
    }

    /**
     * Return a representation of associations between a concept and its
     * immediate ancestor(s). The resolved association list represents the
     * previous level of the hierarchy when visualized in a top (root) to bottom
     * (leaf) representation.
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
    public AssociationList getHierarchyLevelPrev(String codingScheme, CodingSchemeVersionOrTag versionOrTag,
            String hierarchyID, String conceptCode, boolean resolveConcepts, NameAndValueList associationQualifiers)
            throws LBException {
        return lbcm_.getHierarchyLevelPrev(codingScheme, versionOrTag, hierarchyID, conceptCode, resolveConcepts,
                associationQualifiers);
    }

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
    public AssociationList getHierarchyPathToRoot(String codingScheme, CodingSchemeVersionOrTag versionOrTag,
            String hierarchyID, String conceptCode, boolean resolveConcepts,
            HierarchyPathResolveOption pathResolveOption, NameAndValueList associationQualifiers) throws LBException {
        return lbcm_.getHierarchyPathToRoot(codingScheme, versionOrTag, hierarchyID, conceptCode, resolveConcepts,
                pathResolveOption, associationQualifiers);
    }

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
    public ResolvedConceptReferenceList getHierarchyRoots(String codingScheme, CodingSchemeVersionOrTag versionOrTag,
            String hierarchyID) throws LBException {
        return lbcm_.getHierarchyRoots(codingScheme, versionOrTag, hierarchyID);
    }

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
    public CodedNodeSet getHierarchyRootSet(String codingScheme, CodingSchemeVersionOrTag versionOrTag,
            String hierarchyID) throws LBException {
        return lbcm_.getHierarchyRootSet(codingScheme, versionOrTag, hierarchyID);
    }

    /**
     * Return all of the concepts that come one level in the forward direction of the association when
     * traversing the graph from the concept code.
     * 
     * @param conceptCode
     *            conceptCode to lookup
     * @param relationContainerName
     *            The relationContainerName to look under (null for default)
     * @param association
     *            The association to look under ("hasSubtype", etc)
     * @param codingScheme
     *            The coding scheme to use.
     * @param buildReferencedEntries
     *            true to resolve the full concept codes, false for skeletons
     * @throws LBException
     * 
     */
    public Association getAssociationForwardOneLevel(String conceptCode, String relationContainerName, String association, String codingScheme,
            CodingSchemeVersionOrTag versionOrTag, boolean buildReferencedEntries,
            NameAndValueList associationQualifiers) throws LBException {
        return lbcm_.getAssociationForwardOneLevel(conceptCode, relationContainerName, association, codingScheme, versionOrTag,
                buildReferencedEntries, associationQualifiers);
    }

    /**
     * Return all of the concepts that are one level in the reverse direction of the association 
     * to the concept code in the graph. 
     * 
     * @param conceptCode
     *            conceptCode to lookup
     * @param relationContainerName
     *            The relationContainerName to look under (null for default)
     * @param association
     *            The association to look under ("hasSubtype", etc)
     * @param codingScheme
     *            The coding scheme to use.
     * @param buildReferencedEntries
     *            true to resolve the full concept codes, false for skeletons
     * @throws LBException
     */
    public Association getAssociationReverseOneLevel(String conceptCode, String relationContainerName, String association, String codingScheme,
            CodingSchemeVersionOrTag versionOrTag, boolean buildReferencedEntries,
            NameAndValueList associationQualifiers) throws LBException {
        return lbcm_.getAssociationReverseOneLevel(conceptCode, relationContainerName, association, codingScheme, versionOrTag,
                buildReferencedEntries, associationQualifiers);
    }

    /**
     * Return the retired status of a concept code. True if retired, false if
     * not retired. Throws a parameter exception if the code does not exist in
     * the code system.
     * 
     * @param code
     * @param codingSchemeName
     * @param versionOrTag
     * @return
     * @throws LBException
     */
    public boolean isCodeRetired(String code, String codingSchemeName, CodingSchemeVersionOrTag versionOrTag)
            throws LBException {
        return lbcm_.isCodeRetired(code, codingSchemeName, versionOrTag);
    }

    /**
     * Return true if directionalName is the forwardName of an association for
     * the coding scheme
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
    public boolean isForwardName(String codingScheme, CodingSchemeVersionOrTag versionOrTag, String directionalName)
            throws LBException {
        return lbcm_.isForwardName(codingScheme, versionOrTag, directionalName);
    }

    /**
     * Return true if directionalName is the reverseName of an association for
     * the coding scheme
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
    public boolean isReverseName(String codingScheme, CodingSchemeVersionOrTag versionOrTag, String directionalName)
            throws LBException {
        return lbcm_.isReverseName(codingScheme, versionOrTag, directionalName);
    }
}