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
package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList;
import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.Extensions.ExtensionRegistryImpl;
import org.LexGrid.LexBIG.Impl.codedNodeGraphOperations.RestrictToAssociations;
import org.LexGrid.LexBIG.Impl.codedNodeGraphOperations.RestrictToSourceCodes;
import org.LexGrid.LexBIG.Impl.codedNodeGraphOperations.RestrictToTargetCodes;
import org.LexGrid.LexBIG.Impl.codedNodeGraphOperations.interfaces.Operation;
import org.LexGrid.LexBIG.Impl.dataAccess.SQLImplementedMethods;
import org.LexGrid.LexBIG.Impl.pagedgraph.query.DefaultGraphQueryBuilder;
import org.LexGrid.LexBIG.Impl.pagedgraph.query.GraphQueryBuilder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.ActiveOption;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.LexBIG.Utility.ObjectToString;
import org.LexGrid.LexBIG.Utility.ServiceUtility;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.LexGrid.annotations.LgAdminFunction;
import org.LexGrid.annotations.LgClientSideSafe;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.types.EntityTypes;
import org.LexGrid.commonTypes.types.PropertyTypes;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedAssociation;
import org.LexGrid.naming.SupportedHierarchy;
import org.LexGrid.naming.SupportedProperty;
import org.LexGrid.relations.AssociationEntity;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.map.LRUMap;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.access.association.AssociationDao;
import org.lexevs.dao.database.access.association.model.graphdb.GraphDbTriple;
import org.lexevs.dao.database.access.codednodegraph.CodedNodeGraphDao;
import org.lexevs.dao.database.access.codingscheme.CodingSchemeDao;
import org.lexevs.dao.database.access.entity.EntityDao;
import org.lexevs.dao.database.connection.SQLInterface;
import org.lexevs.dao.database.operation.transitivity.DefaultTransitivityBuilder;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.dao.database.service.codednodegraph.model.CountConceptReference;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService.DaoCallback;
import org.lexevs.exceptions.MissingResourceException;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.logging.LoggerFactory;
import org.lexevs.paging.AbstractPageableIterator;
import org.lexevs.system.ResourceManager;
import org.lexevs.system.service.SystemResourceService;

/**
 * Implementation of the server side convenience methods.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @author <A HREF="mailto:johnson.thomas@mayo.edu">Thomas Johnson</A>
 * @author <A HREF="mailto:scott.bauer@mayo.edu">Scott Bauer</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class LexBIGServiceConvenienceMethodsImpl implements LexBIGServiceConvenienceMethods {
    private static final long serialVersionUID = -1543872885685051947L;
    private final static String name_ = "LexBIGServiceConvenienceMethods";
    private final static String description_ = "Useful methods that are implemented by calling a combination of base service methods.";
    private final static String version_ = "1.0";
    private final static String provider_ = "MAYO";

    public LgLoggerIF getLogger() {
        return LoggerFactory.getLogger();
    }

    // Associated service ...
    private transient LexBIGService lbs_;

    // Caching ...
    private Map cache_codingSchemes_ = null;
    private Map cache_copyRights_ = null;
    private Map cache_hIDs_ = null;
    private Map cache_hRoots_ = null;
    private Map cache_hRootCodes_ = null;
    private Map cache_hPathToRootExists_ = null;

    private enum DirectionalName {
        FORWARD, REVERSE
    }

    public static void register() throws LBParameterException, LBException {
        ExtensionDescription temp = new ExtensionDescription();
        temp.setExtensionBaseClass(LexBIGServiceConvenienceMethodsImpl.class.getInterfaces()[0].getName());
        temp.setExtensionClass(LexBIGServiceConvenienceMethodsImpl.class.getName());
        temp.setDescription(description_);
        temp.setName(name_);
        temp.setVersion(version_);

        // Registered here as part of the impl to avoid the LexBig service
        // manager API. If writing an add-on extension, registration should be
        // performed through the proper interface.
        ExtensionRegistryImpl.instance().registerGenericExtension(temp);
    }

    @LgClientSideSafe
    public CodedNodeSet createCodeNodeSet(String[] conceptCodes, String codingScheme,
            CodingSchemeVersionOrTag versionOrTag) throws LBException {
        getLogger().logMethod(new Object[] { conceptCodes, codingScheme, versionOrTag });

        ConceptReferenceList crl = ConvenienceMethods.createConceptReferenceList(conceptCodes, codingScheme);
        CodedNodeSet cns = getLexBIGService().getCodingSchemeConcepts(codingScheme, versionOrTag);
        cns = cns.restrictToCodes(crl);
        return cns;
    }

    public Association getAssociationForwardOneLevel(String conceptCode, String relationContainerName,
            String association, String codingScheme, CodingSchemeVersionOrTag versionOrTag,
            boolean buildReferencedEntries, NameAndValueList associationQualifiers) throws LBException {
        getLogger().logMethod(
                new Object[] { conceptCode, relationContainerName, association, codingScheme, versionOrTag,
                        buildReferencedEntries });

        CodedNodeGraph cng = getLexBIGService().getNodeGraph(codingScheme, versionOrTag, relationContainerName);
        cng = cng.restrictToSourceCodes(createCodeNodeSet(new String[] { conceptCode }, codingScheme, versionOrTag));
        cng = cng.restrictToAssociations(ConvenienceMethods.createNameAndValueList(association), associationQualifiers);

        int buildRefEntries = -1;
        if (buildReferencedEntries) {
            buildRefEntries = 1;
        }
        ResolvedConceptReferenceList rcrl = cng.resolveAsList(ConvenienceMethods.createConceptReference(conceptCode,
                codingScheme), true, false, buildRefEntries, 1, null, null, null, 0);
        if (rcrl.getResolvedConceptReferenceCount() > 0) {
            AssociationList al = rcrl.getResolvedConceptReference(0).getSourceOf();
            if (al != null && al.getAssociationCount() > 0) {
                return al.getAssociation(0);
            }
        }
        return null;
    }

    public Association getAssociationReverseOneLevel(String conceptCode, String relationContainerName,
            String association, String codingScheme, CodingSchemeVersionOrTag versionOrTag,
            boolean buildReferencedEntries, NameAndValueList associationQualifiers) throws LBException {
        getLogger().logMethod(
                new Object[] { conceptCode, relationContainerName, association, codingScheme, versionOrTag,
                        buildReferencedEntries });

        CodedNodeGraph cng = getLexBIGService().getNodeGraph(codingScheme, versionOrTag, relationContainerName);
        cng = cng.restrictToTargetCodes(createCodeNodeSet(new String[] { conceptCode }, codingScheme, versionOrTag));
        cng = cng.restrictToAssociations(ConvenienceMethods.createNameAndValueList(association), associationQualifiers);

        int buildRefEntries = -1;
        if (buildReferencedEntries) {
            buildRefEntries = 1;
        }
        ResolvedConceptReferenceList rcrl = cng.resolveAsList(ConvenienceMethods.createConceptReference(conceptCode,
                codingScheme), false, true, buildRefEntries, 1, null, null, null, 0);
        if (rcrl.getResolvedConceptReferenceCount() > 0) {
            AssociationList al = rcrl.getResolvedConceptReference(0).getTargetOf();
            if (al != null && al.getAssociationCount() > 0) {
                return al.getAssociation(0);
            }
        }
        return null;
    }

    public boolean isCodeRetired(String code, String codingSchemeName, CodingSchemeVersionOrTag versionOrTag)
            throws LBException {
        getLogger().logMethod(new Object[] { code, codingSchemeName, versionOrTag });

        CodedNodeSet cns = getLexBIGService().getCodingSchemeConcepts(codingSchemeName, versionOrTag).restrictToCodes(
                ConvenienceMethods.createConceptReferenceList(new String[] { code }, codingSchemeName));
        cns = cns.restrictToStatus(ActiveOption.ACTIVE_ONLY, null);

        if (cns.isCodeInSet(ConvenienceMethods.createConceptReference(code, codingSchemeName))) {
            // if we found the concept when we were doing a activeOnly search,
            // then
            // it is not retired.
            return false;
        } else {
            // didn't find the concept. See if it exists when we allow retired
            // concepts.
            cns = getLexBIGService().getCodingSchemeConcepts(codingSchemeName, versionOrTag).restrictToCodes(
                    ConvenienceMethods.createConceptReferenceList(new String[] { code }, codingSchemeName));

            // if it exists now - then it is retired.
            if (cns.isCodeInSet(ConvenienceMethods.createConceptReference(code, codingSchemeName))) {
                return true;
            } else {
                // still not there - probably not a valid concept.
                throw new LBParameterException("The concept code '" + code + "' does not exist in the code system.",
                        "code", code);
            }
        }
    }

    @LgClientSideSafe
    public String getName() {
        getLogger().logMethod(new Object[] {});
        return name_;
    }

    @LgClientSideSafe
    public String getDescription() {
        getLogger().logMethod(new Object[] {});
        return description_;
    }

    @LgClientSideSafe
    public String getVersion() {
        getLogger().logMethod(new Object[] {});
        return version_;
    }

    @LgClientSideSafe
    public String getProvider() {
        getLogger().logMethod(new Object[] {});
        return provider_;
    }

    @LgClientSideSafe
    public CodingSchemeRendering getRenderingDetail(String codingScheme, CodingSchemeVersionOrTag versionOrTag)
            throws LBException {
        getLogger().logMethod(new Object[] { codingScheme, versionOrTag });

        CodingScheme cs = getCodingScheme(codingScheme, versionOrTag);

        CodingSchemeRendering[] csr = getLexBIGService().getSupportedCodingSchemes().getCodingSchemeRendering();

        for (int i = 0; i < csr.length; i++) {
            if (csr[i].getCodingSchemeSummary().getCodingSchemeURI().equals(cs.getCodingSchemeURI())
                    && csr[i].getCodingSchemeSummary().getRepresentsVersion().equals(cs.getRepresentsVersion())) {
                return csr[i];
            }
        }

        String id = getLogger().error("Did not match up a found coding scheme with the coding scheme rendering");
        throw new LBInvocationException("Unexpected internal error", id);
    }

    public CodingSchemeRenderingList getCodingSchemesWithSupportedAssociation(String associationName)
            throws LBException {
        CodingSchemeRenderingList resolvedCodingSchemes = new CodingSchemeRenderingList();

        if (associationName != null) {
            getLogger().logMethod(new Object[] { associationName });

            CodingSchemeRenderingList csrl = getLexBIGService().getSupportedCodingSchemes();
            CodingSchemeRendering[] csr = csrl.getCodingSchemeRendering();

            for (CodingSchemeRendering csRendering : csr) {

                CodingScheme cs = null;
                try {
                    cs = getCodingScheme(csRendering.getCodingSchemeSummary().getCodingSchemeURI(), null);
                } catch (UndeclaredThrowableException e) {
                    // GForge #15437 - skip over any Secured Coding Schemes that
                    // the client does not have access to, instead of throwing
                    // an Exception.
                    getLogger()
                            .info(
                                    "Skipping a Coding Scheme due to an 'UndeclaredThrowableException'. If"
                                            + " this is being run remotely, this means that a Secured Coding Scheme was skipped.");
                    continue;
                }

                SupportedAssociation[] suppAssocs = cs.getMappings().getSupportedAssociation();

                boolean found = false;
                for (int j = 0; (j < suppAssocs.length) && (!found); j++) {
                    if (associationName.equals(suppAssocs[j].getLocalId())) {
                        resolvedCodingSchemes.addCodingSchemeRendering(csRendering);
                        found = true;
                    }
                }
            }
            return resolvedCodingSchemes;
        }
        String id = getLogger().error("Did not match up a found coding scheme with the supported association");
        throw new LBInvocationException("Unexpected internal error", id);
    }

    public String getAssociationCodeFromAssociationName(String codingScheme, CodingSchemeVersionOrTag versionOrTag,
            String associationName) throws LBException {
        getLogger().logMethod(new Object[] { codingScheme, versionOrTag, associationName });
        CodingScheme resolvedCodingScheme = this.getCodingScheme(codingScheme, versionOrTag);

        for (SupportedAssociation foundSupportedAssociation : resolvedCodingScheme.getMappings()
                .getSupportedAssociation()) {
            if(this.useBackwardCompatibleMethods(codingScheme, versionOrTag)) {
                if (foundSupportedAssociation.getContent().equals(associationName)) {
                    return foundSupportedAssociation.getEntityCode();
                }
            } else {
                if (foundSupportedAssociation.getLocalId().equals(associationName)) {
                    return foundSupportedAssociation.getEntityCode();
                }
            }
        }

        throw new LBParameterException("Cound not find an EntityCode for AssociationName: " + associationName);
    }

    public String getAssociationNameFromAssociationCode(String codingScheme, CodingSchemeVersionOrTag versionOrTag,
            String entityCode) throws LBException {
        CodingScheme resolvedCodingScheme = this.getCodingScheme(codingScheme, versionOrTag);

        for (SupportedAssociation foundSupportedAssociation : resolvedCodingScheme.getMappings()
                .getSupportedAssociation()) {

            String foundEntityCode = foundSupportedAssociation.getEntityCode();
            if (foundEntityCode != null && foundEntityCode.equals(entityCode)) {
                if(this.useBackwardCompatibleMethods(codingScheme, versionOrTag)) {
                    return foundSupportedAssociation.getContent();
                } else {
                    return foundSupportedAssociation.getLocalId();
                }
            }
        }

        throw new LBParameterException("Cound not find an AssociationName for EntityCode: " + entityCode);
    }

    public String[] getAssociationNameForDirectionalName(String codingScheme, CodingSchemeVersionOrTag versionOrTag,
            String directionalName) throws LBException {
        List<String> returnList = new ArrayList<String>();

        CodingScheme resolvedCodingScheme = this.getCodingScheme(codingScheme, versionOrTag);

        for (SupportedAssociation supportedAssociation : resolvedCodingScheme.getMappings().getSupportedAssociation()) {

            String forwardName = this.doGetAssociationDirectionalName(codingScheme, versionOrTag, supportedAssociation
                    .getLocalId(), DirectionalName.FORWARD);

            // check the forward name
            if (directionalName.equals(forwardName)) {
                returnList.add(supportedAssociation.getLocalId());
            } else {
                // if not, check the reverse name
                String reverseName = this.doGetAssociationDirectionalName(codingScheme, versionOrTag,
                        supportedAssociation.getLocalId(), DirectionalName.REVERSE);
                if (directionalName.equals(reverseName)) {
                    returnList.add(supportedAssociation.getLocalId());
                }
            }
        }
        return returnList.toArray(new String[returnList.size()]);
    }

    public String getAssociationForwardName(String associationName, String codingScheme,
            CodingSchemeVersionOrTag versionOrTag) throws LBException {

        return doGetAssociationDirectionalName(codingScheme, versionOrTag, associationName, DirectionalName.FORWARD);
    }

    protected String doGetAssociationDirectionalName(String codingScheme, CodingSchemeVersionOrTag versionOrTag,
            String associationName, DirectionalName direction) throws LBException {
        
        CodingScheme resolvedCodingScheme = this.getCodingScheme(codingScheme, versionOrTag);

        SupportedAssociation supportedAssociation = null;

        for (SupportedAssociation foundSupportedAssociation : resolvedCodingScheme.getMappings()
                .getSupportedAssociation()) {
            if (foundSupportedAssociation.getLocalId().equals(associationName)) {
                supportedAssociation = foundSupportedAssociation;
                break;
            }
        }

        if (supportedAssociation == null) {
            throw new LBParameterException("No SupportedAssociation with name: " + associationName + " was found.");
        }
        
        if(this.useBackwardCompatibleMethods(codingScheme, versionOrTag)) {
            if(resolvedCodingScheme.getEntities() != null) {
                for(AssociationEntity ae : resolvedCodingScheme.getEntities().getAssociationEntity()) {
                    if(ae.getEntityCode().equals(supportedAssociation.getContent())){
                        if(direction.equals(DirectionalName.FORWARD)) {
                            return ae.getForwardName();
                        } else {
                            return ae.getReverseName();
                        }
                    }
                }
            } 
        }

        String containingCodingScheme = supportedAssociation.getCodingScheme();
        String containingEntityCode = supportedAssociation.getEntityCode();
        String containingEntityCodeNamespace = supportedAssociation.getEntityCodeNamespace();
        
        // if there's no entityCode, assume the AssociationName
        if (StringUtils.isBlank(containingEntityCode)) {
            containingEntityCode = supportedAssociation.getLocalId();
        }

        // if there's no codingSchemeName, assume local
        if (StringUtils.isBlank(containingCodingScheme)) {
            containingCodingScheme = resolvedCodingScheme.getCodingSchemeName();
        }

        // if there's no entityCodeNamespace, assume default
        if (StringUtils.isBlank(containingEntityCodeNamespace)) {
            containingEntityCodeNamespace = resolvedCodingScheme.getCodingSchemeName();
        }

        CodingSchemeVersionOrTag versionOrTagToUse = null;
        if (containingCodingScheme.equals(resolvedCodingScheme.getCodingSchemeName())) {
            versionOrTagToUse = versionOrTag;
        }

        CodedNodeSet cns = this.getLexBIGService().getNodeSet(containingCodingScheme, versionOrTagToUse,
                Constructors.createLocalNameList(EntityTypes.ASSOCIATION.toString()));

        cns = cns.restrictToCodes(Constructors.createConceptReferenceList(containingEntityCode,
                containingEntityCodeNamespace, containingCodingScheme));

        ResolvedConceptReferenceList list = cns.resolveToList(null, null, null, -1);

        if (list.getResolvedConceptReferenceCount() == 0) {
            return null;
        }

        AssociationEntity associationEntity = (AssociationEntity) list.getResolvedConceptReference(0).getEntity();

        if (direction.equals(DirectionalName.FORWARD)) {
            return associationEntity.getForwardName();
        } else {
            return associationEntity.getReverseName();
        }
    }

    protected String[] doGetAssociationNames(String codingScheme, CodingSchemeVersionOrTag versionOrTag,
            DirectionalName direction) throws LBException {

        List<String> names = new ArrayList<String>();

        CodingScheme cs = this.getCodingScheme(codingScheme, versionOrTag);

        for (SupportedAssociation assoc : cs.getMappings().getSupportedAssociation()) {
            String name = this.doGetAssociationDirectionalName(codingScheme, versionOrTag, assoc.getLocalId(),
                    direction);
            if (StringUtils.isNotBlank(name)) {
                names.add(name);
            }
        }
        return names.toArray(new String[names.size()]);
    }
    
    public String[] getAssociationForwardNames(String codingScheme, CodingSchemeVersionOrTag versionOrTag)
            throws LBException {
        return this.doGetAssociationNames(codingScheme, versionOrTag, DirectionalName.FORWARD);
    }

    public String getAssociationReverseName(String associationName, String codingScheme,
            CodingSchemeVersionOrTag versionOrTag) throws LBException {
        return doGetAssociationDirectionalName(codingScheme, versionOrTag, associationName, DirectionalName.REVERSE);
    }

    public String[] getAssociationReverseNames(String codingScheme, CodingSchemeVersionOrTag versionOrTag)
            throws LBException {
        return this.doGetAssociationNames(codingScheme, versionOrTag, DirectionalName.REVERSE);
    }

    public String[] getAssociationForwardAndReverseNames(String codingScheme, CodingSchemeVersionOrTag versionOrTag)
            throws LBException {
        String[] forward_names = getAssociationForwardNames(codingScheme, versionOrTag);
        String[] reverse_names = getAssociationReverseNames(codingScheme, versionOrTag);

        List<String> forward_and_reverse_names = new ArrayList<String>(forward_names.length + reverse_names.length);

        forward_and_reverse_names.addAll(Arrays.asList(forward_names));
        forward_and_reverse_names.addAll(Arrays.asList(reverse_names));
        return (String[]) forward_and_reverse_names.toArray(new String[forward_and_reverse_names.size()]);
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
        if (directionalName == null || directionalName.trim().length() == 0) {
            return false;
        }
        String[] forwardNames = this.getAssociationForwardNames(codingScheme, versionOrTag);
        for (String name : forwardNames) {
            if (directionalName.equals(name)) {
                return true;
            }
        }
        return false;
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
        if (directionalName == null || directionalName.trim().length() == 0) {
            return false;
        }
        String[] reverseNames = this.getAssociationReverseNames(codingScheme, versionOrTag);
        for (String name : reverseNames) {
            if (directionalName.equals(name)) {
                return true;
            }
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods
     * #getHierarchyIDs(java.lang.String,
     * org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)
     */
    @LgClientSideSafe
    public String[] getHierarchyIDs(String codingScheme, CodingSchemeVersionOrTag versionOrTag) throws LBException {

        Object[] params = new Object[] { codingScheme, versionOrTag };
        getLogger().logMethod(params);

        // Check and re-use cached value if present ...
        Object key = getCacheKey(params);
        Object val = null;
        if ((val = getCache_HIDs().get(key)) != null)
            return (String[]) val;

        // Resolve from coding scheme info ...
        Set<String> ids = new HashSet<String>();
        SupportedHierarchy[] sh = getSupportedHierarchies(codingScheme, versionOrTag);
        for (int i = 0; i < sh.length; i++)
            ids.add(sh[i].getLocalId());

        // Cache and return the new value ...
        String[] hier = ids.toArray(new String[ids.size()]);
        getCache_HIDs().put(key, hier);
        return hier;
    }

    public ConceptReferenceList getHierarchyLevelNextCount(String codingSchemeName,
            CodingSchemeVersionOrTag versionOrTag, String hierarchyID, ConceptReferenceList conceptCodes)
            throws LBException {
        getLogger().logMethod(new Object[] { codingSchemeName, versionOrTag, hierarchyID, conceptCodes });
        if(this.useBackwardCompatibleMethods(codingSchemeName, versionOrTag)) {
            return getHierarchyLevelNextCountBackwardCompatible(codingSchemeName, versionOrTag, hierarchyID, conceptCodes, true);
        } else {
            return getHierarchyLevelNextCount(codingSchemeName, versionOrTag, hierarchyID, conceptCodes, true);
        }
    }

    public ConceptReferenceList getHierarchyLevelPrevCount(String codingSchemeName,
            CodingSchemeVersionOrTag versionOrTag, String hierarchyID, ConceptReferenceList conceptCodes)
            throws LBException {
        getLogger().logMethod(new Object[] { codingSchemeName, versionOrTag, hierarchyID, conceptCodes });
        if(this.useBackwardCompatibleMethods(codingSchemeName, versionOrTag)) {
            return getHierarchyLevelNextCountBackwardCompatible(codingSchemeName, versionOrTag, hierarchyID, conceptCodes, false);
        } else {
            return getHierarchyLevelNextCount(codingSchemeName, versionOrTag, hierarchyID, conceptCodes, false);
        }
    }

    public int getHierarchyLevelNextCount(String codingSchemeName, CodingSchemeVersionOrTag versionOrTag,
            String hierarchyID, ConceptReference conceptRef) throws LBException {
        getLogger().logMethod(new Object[] { codingSchemeName, versionOrTag, hierarchyID, conceptRef });
        int count = 0;
        ConceptReferenceList crl = new ConceptReferenceList();
        crl.addConceptReference(conceptRef);
        ConceptReferenceList countRefList = getHierarchyLevelNextCount(codingSchemeName, versionOrTag, hierarchyID, crl);

        if (countRefList != null && countRefList.getConceptReferenceCount() > 0) {
            ConceptReference cr = (countRefList.getConceptReference())[0];
            if (cr instanceof CountConceptReference)
                count = ((CountConceptReference) cr).getChildCount();
        }
        return count;
    }

    public int getHierarchyLevelPrevCount(String codingSchemeName, CodingSchemeVersionOrTag versionOrTag,
            String hierarchyID, ConceptReference conceptRef) throws LBException {
        getLogger().logMethod(new Object[] { codingSchemeName, versionOrTag, hierarchyID, conceptRef });
        int count = 0;
        ConceptReferenceList crl = new ConceptReferenceList();
        crl.addConceptReference(conceptRef);
        ConceptReferenceList countRefList = getHierarchyLevelPrevCount(codingSchemeName, versionOrTag, hierarchyID, crl);
        if (countRefList != null && countRefList.getConceptReferenceCount() > 0) {
            ConceptReference cr = (countRefList.getConceptReference())[0];
            if (cr instanceof CountConceptReference)
                count = ((CountConceptReference) cr).getChildCount();
        }
        return count;
    }

    @SuppressWarnings("deprecation")
    protected ConceptReferenceList getHierarchyLevelNextCountBackwardCompatible(String codingSchemeName,
            CodingSchemeVersionOrTag versionOrTag, String hierarchyID, ConceptReferenceList conceptCodes,
            boolean forward) throws LBException {
        String internalCodingSchemeName = null;
        String version = null;

        if (versionOrTag == null) {
            version = ResourceManager.instance().getInternalVersionStringForTag(codingSchemeName, null);
        } else {
            version = versionOrTag.getVersion();
        }

        internalCodingSchemeName = ResourceManager.instance().getInternalCodingSchemeNameForUserCodingSchemeName(
                codingSchemeName, version);
        try {
            // If we are searching for the child count of concept C1 and C1 has
            // no children, it would not be in the list. Add C1 to the list with count 0,
            // indicating we couldn't find  a match in the database. 
            // It also takes care of the case where there is no hierarchy defined.
            HashMap<String, ConceptReference> lookup = new HashMap<String, ConceptReference>();
            ConceptReferenceList crl = new ConceptReferenceList();
            SupportedHierarchy[] shs = getSupportedHierarchies(codingSchemeName, versionOrTag, hierarchyID);
            //We have a hierarchy
            if (shs.length != 0) {
                SupportedHierarchy sh = shs[0];
                String assocs[] = sh.getAssociationNames();
                ArrayList<Operation> pendingOperations = new ArrayList<Operation>();

                RestrictToAssociations rta = new RestrictToAssociations(ConvenienceMethods
                        .createNameAndValueList(assocs), null);
                pendingOperations.add(rta);
                boolean resolveFwd = sh.isIsForwardNavigable();
                // If we want to get the prev level, we need to flip the resolve
                // direction
                if (!forward) {
                    resolveFwd = !resolveFwd;
                }
                if (resolveFwd) {
                    RestrictToSourceCodes rsc = new RestrictToSourceCodes(conceptCodes);
                    pendingOperations.add(rsc);
                } else {
                    RestrictToTargetCodes rtc = new RestrictToTargetCodes(conceptCodes);
                    pendingOperations.add(rtc);
                }

                SQLInterface si = ResourceManager.instance().getSQLInterface(internalCodingSchemeName, version);
                crl = SQLImplementedMethods.countQuery(si, pendingOperations, resolveFwd, internalCodingSchemeName,
                        version, null);

                for (ConceptReference cr : crl.getConceptReference()) {
                    lookup.put(cr.getConceptCode(), cr);
                }
            }
            // If we do not find the concept in the list, we assume it is
            // because it has no children.
            for (ConceptReference cr : conceptCodes.getConceptReference()) {
                if (!lookup.containsKey(cr.getConceptCode())) {
                    CountConceptReference ccr = new CountConceptReference(cr, 0);
                    crl.addConceptReference(ccr);
                }
            }
            // getLogger().debug("Time to execute getHierarchyLevelNextCount=" +
            // (System.currentTimeMillis() - startTime));
            return crl;

        } catch (MissingResourceException e) {
            throw new LBException(e.getMessage(), e);
        }

    }
    
    protected ConceptReferenceList getHierarchyLevelNextCount(String codingSchemeName,
            CodingSchemeVersionOrTag versionOrTag, String hierarchyID, final ConceptReferenceList conceptCodes,
            boolean forward) throws LBException {
        SystemResourceService systemResourceService = LexEvsServiceLocator.getInstance().getSystemResourceService();

        final String version = ServiceUtility.getVersion(codingSchemeName, versionOrTag);

        String internalCodingSchemeName = systemResourceService.getInternalCodingSchemeNameForUserCodingSchemeName(
                codingSchemeName, version);

        SupportedHierarchy[] shs = getSupportedHierarchies(codingSchemeName, versionOrTag, hierarchyID);
        SupportedHierarchy sh = shs[0];
        final String assocs[] = sh.getAssociationNames();
        ArrayList<Operation> pendingOperations = new ArrayList<Operation>();

        RestrictToAssociations rta = new RestrictToAssociations(ConvenienceMethods.createNameAndValueList(assocs), null);
        pendingOperations.add(rta);

        // If we want to get the prev level, we need to flip the resolve
        // direction
        final boolean resolveFwd = !sh.isIsForwardNavigable() ? !forward : forward;

        if (resolveFwd) {
            RestrictToSourceCodes rsc = new RestrictToSourceCodes(conceptCodes);
            pendingOperations.add(rsc);
        } else {
            RestrictToTargetCodes rtc = new RestrictToTargetCodes(conceptCodes);
            pendingOperations.add(rtc);
        }

        final String uri = systemResourceService.getUriForUserCodingSchemeName(internalCodingSchemeName, version);

        DaoCallbackService daoCallbackService = LexEvsServiceLocator.getInstance().getDatabaseServiceManager()
                .getDaoCallbackService();

        GraphQueryBuilder queryBuilder = new DefaultGraphQueryBuilder(uri, version);
        queryBuilder.restrictToAssociations(ConvenienceMethods.createNameAndValueList(assocs), null);

        if (resolveFwd) {
            queryBuilder.getQuery().setRestrictToSourceCodes(Arrays.asList(conceptCodes.getConceptReference()));

        } else {
            queryBuilder.getQuery().setRestrictToTargetCodes(Arrays.asList(conceptCodes.getConceptReference()));
        }

        List<CountConceptReference> conceptCountList = daoCallbackService
                .executeInDaoLayer(new DaoCallback<List<CountConceptReference>>() {

                    @Override
                    public List<CountConceptReference> execute(DaoManager daoManager) {
                        CodingSchemeDao csDao = daoManager.getCodingSchemeDao(uri, version);
                        CodedNodeGraphDao cngDao = daoManager.getCodedNodeGraphDao(uri, version);

                        String codingSchemeUid = csDao.getCodingSchemeUIdByUriAndVersion(uri, version);

                        if (resolveFwd) {
                            return cngDao.getCountConceptReferencesContainingSubject(codingSchemeUid, null, Arrays
                                    .asList(conceptCodes.getConceptReference()), Arrays.asList(assocs), null, null,
                                    null, null, null);
                        } else {
                            return cngDao.getCountConceptReferencesContainingObject(codingSchemeUid, null, Arrays
                                    .asList(conceptCodes.getConceptReference()), Arrays.asList(assocs), null, null,
                                    null, null, null);
                        }
                    }

                });

        ConceptReferenceList crl = new ConceptReferenceList();
        crl.setConceptReference(conceptCountList.toArray(new ConceptReference[conceptCountList.size()]));

        // If we are searching for the child count of concept C1 and C1 has no
        // children,
        // it would not be in the list. Add C1 to the list with count 0,
        // indicating we couldn't find
        // a match in the database.
        HashMap<String, ConceptReference> lookup = new HashMap<String, ConceptReference>();
        for (ConceptReference cr : crl.getConceptReference()) {
            lookup.put(cr.getConceptCode(), cr);
        }

        // If we do not find the concept in the list, we assume it is because it
        // has no children.
        for (ConceptReference cr : conceptCodes.getConceptReference()) {
            if (!lookup.containsKey(cr.getConceptCode())) {
                CountConceptReference ccr = new CountConceptReference(cr, 0);
                crl.addConceptReference(ccr);
            }
        }
        // getLogger().debug("Time to execute getHierarchyLevelNextCount=" +
        // (System.currentTimeMillis() - startTime));
        return crl;

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods
     * #getHierarchyLevelNext(java.lang.String,
     * org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag,
     * java.lang.String, java.lang.String, boolean, boolean,
     * org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList)
     */
    public AssociationList getHierarchyLevelNext(String codingScheme, CodingSchemeVersionOrTag versionOrTag,
            String hierarchyID, String conceptCode, boolean resolveConcepts, boolean checkForHasHierarchyPathToRoot,
            NameAndValueList assocQuals) throws LBException {
        getLogger().logMethod(new Object[] { conceptCode, hierarchyID, codingScheme, versionOrTag, resolveConcepts });

        AssociationList al = new AssociationList();

        // Group all possible associations to consider in forward and reverse
        // direction. Note the code must have a path back to the defined root
        // node for the associations to be considered.
        ArrayList<String> fwdAssoc = new ArrayList<String>();
        ArrayList<String> revAssoc = new ArrayList<String>();

        SupportedHierarchy[] shs = getSupportedHierarchies(codingScheme, versionOrTag, hierarchyID);
        for (SupportedHierarchy sh : shs) {
            boolean fwd = sh.getIsForwardNavigable();
            String[] assocNames = sh.getAssociationNames();
            String[] rootCodes = getHierarchyRootCodes(codingScheme, versionOrTag, hierarchyID);
            for (String rootCode : rootCodes) {
                if (hierarchyID == null
                        || !checkForHasHierarchyPathToRoot
                        || hasHierarchyPathToRoot(codingScheme, versionOrTag, assocNames, !fwd, conceptCode, rootCode,
                                assocQuals)) {
                    if (fwd)
                        fwdAssoc.addAll(Arrays.asList(assocNames));
                    else
                        revAssoc.addAll(Arrays.asList(assocNames));
                    break;
                }
            }
        }

        // Now resolve all associations in forward or reverse direction, and
        // add them to the returned association list.
        if (fwdAssoc.size() > 0)
            ConvenienceMethods.addAll(getHierarchyLevelNext(codingScheme, versionOrTag, (String[]) (fwdAssoc
                    .toArray(new String[fwdAssoc.size()])), true, conceptCode, resolveConcepts, assocQuals), al);
        if (revAssoc.size() > 0)
            ConvenienceMethods.addAll(getHierarchyLevelNext(codingScheme, versionOrTag, (String[]) (revAssoc
                    .toArray(new String[revAssoc.size()])), false, conceptCode, resolveConcepts, assocQuals), al);
        return al;

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods
     * #getHierarchyLevelNext(java.lang.String,
     * org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag,
     * java.lang.String, java.lang.String, boolean,
     * org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList)
     */
    public AssociationList getHierarchyLevelNext(String codingScheme, CodingSchemeVersionOrTag versionOrTag,
            String hierarchyID, String conceptCode, boolean resolveConcepts, NameAndValueList assocQuals)
            throws LBException {
        return getHierarchyLevelNext(codingScheme, versionOrTag, hierarchyID, conceptCode, resolveConcepts, true,
                assocQuals);

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods
     * #getHierarchyLevelPrev(java.lang.String,
     * org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag,
     * java.lang.String, java.lang.String, boolean, boolean,
     * org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList)
     */
    public AssociationList getHierarchyLevelPrev(String codingScheme, CodingSchemeVersionOrTag versionOrTag,
            String hierarchyID, String conceptCode, boolean resolveConcepts, boolean checkForHasHierarchyPathToRoot,
            NameAndValueList assocQuals) throws LBException {

        getLogger().logMethod(new Object[] { conceptCode, hierarchyID, codingScheme, versionOrTag, resolveConcepts });

        AssociationList al = new AssociationList();

        // Group all possible associations to consider in forward and reverse
        // direction. Note the code must have a path back to the defined root
        // node for the associations to be considered.
        ArrayList<String> fwdAssoc = new ArrayList<String>();
        ArrayList<String> revAssoc = new ArrayList<String>();

        SupportedHierarchy[] shs = getSupportedHierarchies(codingScheme, versionOrTag, hierarchyID);
        for (SupportedHierarchy sh : shs) {
            boolean fwd = sh.getIsForwardNavigable();
            String[] assocNames = sh.getAssociationNames();
            String[] rootCodes = getHierarchyRootCodes(codingScheme, versionOrTag, hierarchyID);
            for (String rootCode : rootCodes) {
                if (hierarchyID == null
                        || !checkForHasHierarchyPathToRoot
                        || hasHierarchyPathToRoot(codingScheme, versionOrTag, assocNames, !fwd, conceptCode, rootCode,
                                assocQuals)) {
                    if (fwd)
                        fwdAssoc.addAll(Arrays.asList(assocNames));
                    else
                        revAssoc.addAll(Arrays.asList(assocNames));
                    break;
                }
            }
        }

        // Now resolve all associations in forward or reverse direction, and
        // add them to the returned association list.
        if (fwdAssoc.size() > 0)
            ConvenienceMethods.addAll(getHierarchyLevelNext(codingScheme, versionOrTag, (String[]) (fwdAssoc
                    .toArray(new String[fwdAssoc.size()])), false, conceptCode, resolveConcepts, assocQuals), al);
        if (revAssoc.size() > 0)
            ConvenienceMethods.addAll(getHierarchyLevelNext(codingScheme, versionOrTag, (String[]) (revAssoc
                    .toArray(new String[revAssoc.size()])), true, conceptCode, resolveConcepts, assocQuals), al);
        return al;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods
     * #getHierarchyLevelPrev(java.lang.String,
     * org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag,
     * java.lang.String, java.lang.String, boolean,
     * org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList)
     */
    public AssociationList getHierarchyLevelPrev(String codingScheme, CodingSchemeVersionOrTag versionOrTag,
            String hierarchyID, String conceptCode, boolean resolveConcepts, NameAndValueList assocQuals)
            throws LBException {
        return getHierarchyLevelPrev(codingScheme, versionOrTag, hierarchyID, conceptCode, resolveConcepts, true,
                assocQuals);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods
     * #getHierarchyPathToRoot(java.lang.String,
     * org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag,
     * java.lang.String, java.lang.String, boolean,
     * org.LexGrid.LexBIG.Extensions
     * .Generic.LexBIGServiceConvenienceMethods.HierarchyPathResolveOption,
     * org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList)
     */
    public AssociationList getHierarchyPathToRoot(String codingScheme, CodingSchemeVersionOrTag versionOrTag,
            String hierarchyID, String conceptCode, boolean resolveConcepts,
            HierarchyPathResolveOption pathResolveOption, NameAndValueList assocQuals) throws LBException {

        getLogger().logMethod(
                new Object[] { codingScheme, versionOrTag, hierarchyID, conceptCode, resolveConcepts,
                        pathResolveOption, assocQuals });

        // Resolve matching hierarchies to evaluate, based on the provided ID
        // ...
        SupportedHierarchy[] sHiers = getSupportedHierarchies(codingScheme, versionOrTag, hierarchyID);
        if (sHiers.length == 0)
            throw new LBInvocationException("Unable to resolve hierarchy ID", getLogger().error("Hierarchy not found."));

        // Loop through each root and add matching associations to the result
        // ...
        AssociationList paths = new AssociationList();
        for (int i = 0; i < sHiers.length; i++) {
            SupportedHierarchy sh = sHiers[i];
            String[] rootCodes = getHierarchyRootCodes(codingScheme, versionOrTag, hierarchyID);
            for (String rootCode : rootCodes) {
                String[] assocNames = sh.getAssociationNames();
                boolean fwd = !sh.getIsForwardNavigable(); // Reverse to go to
                // root

                // Do we have a path for this root?
                if (assocNames.length >= 1
                        && hasHierarchyPathToRoot(codingScheme, versionOrTag, assocNames, fwd, conceptCode, rootCode,
                                assocQuals)) {
                    // Constrain number of paths resolved?
                    int maxToReturn = pathResolveOption.equals(HierarchyPathResolveOption.ALL) ? -1 : 1;

                    // Invoke primitive method that works in terms of
                    // associations and direction.
                    AssociationList toAdd = getHierarchyPathToRoot(codingScheme, versionOrTag, assocNames, fwd,
                            conceptCode, rootCode, resolveConcepts, assocQuals, maxToReturn);

                    // Add results for this root to the comprehensive result.
                    for (int j = 0; j < toAdd.getAssociationCount(); j++)
                        paths.addAssociation(toAdd.getAssociation(j));

                    // Exit loop if only getting one item or one per hierarchy.
                    // Otherwise continue processing the next root.
                    if (toAdd.getAssociationCount() > 0
                            && (pathResolveOption.equals(HierarchyPathResolveOption.ONE) || pathResolveOption
                                    .equals(HierarchyPathResolveOption.ONE_PER_HIERARCHY)))
                        break;
                }
            }
            // Exit loop if only getting one.
            // Otherwise continue processing the next hierarchy with the given
            // ID.
            if (paths.getAssociationCount() > 0 && pathResolveOption.equals(HierarchyPathResolveOption.ONE))
                break;
        }
        return paths;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods
     * #getHierarchyRoots(java.lang.String,
     * org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag,
     * java.lang.String)
     */
    @LgClientSideSafe
    public ResolvedConceptReferenceList getHierarchyRoots(String codingScheme, CodingSchemeVersionOrTag versionOrTag,
            String hierarchyID) throws LBException {

        Object[] params = new Object[] { codingScheme, versionOrTag, hierarchyID };
        getLogger().logMethod(params);

        // Check and re-use cached value if present ...
        Object key = getCacheKey(params);
        Object val = null;
        if ((val = getCache_HRoots().get(key)) != null)
            return (ResolvedConceptReferenceList) val;

        // Cache and return the new value ...
        val = getHierarchyRootSet(codingScheme, versionOrTag, hierarchyID).resolveToList(null, null, null, -1);
        getCache_HRoots().put(key, val);
        return (ResolvedConceptReferenceList) val;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods
     * #getHierarchyRoots(java.lang.String,
     * org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag,
     * java.lang.String)
     */
    @LgClientSideSafe
    public ResolvedConceptReferenceList getHierarchyRoots(String codingScheme, CodingSchemeVersionOrTag versionOrTag,
            String hierarchyID, boolean resolveConcepts) throws LBException {

        Object[] params = new Object[] { codingScheme, versionOrTag, hierarchyID, resolveConcepts };
        getLogger().logMethod(params);

        // Check and re-use cached value if present ...
        Object key = getCacheKey(params);
        Object val = null;
        if ((val = getCache_HRoots().get(key)) != null)
            return (ResolvedConceptReferenceList) val;

        // Cache and return the new value ...
        val = getHierarchyRootSet(codingScheme, versionOrTag, hierarchyID).resolveToList(null, null, null, null,
                resolveConcepts, -1);
        getCache_HRoots().put(key, val);
        return (ResolvedConceptReferenceList) val;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods
     * #getHierarchyRootSet(java.lang.String,
     * org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag,
     * java.lang.String)
     */
    @LgClientSideSafe
    public CodedNodeSet getHierarchyRootSet(String codingScheme, CodingSchemeVersionOrTag versionOrTag,
            String hierarchyID) throws LBException {

        getLogger().logMethod(new Object[] { codingScheme, versionOrTag, hierarchyID });

        // Start with an empty set ...
        CodedNodeSet cns = getLexBIGService().getCodingSchemeConcepts(codingScheme, versionOrTag);
        cns = cns.restrictToCodes(ConvenienceMethods.createConceptReferenceList(new String[] { "__INVALID" },
                codingScheme));

        // Iterate through supported hierarchies and union all participants ...
        SupportedHierarchy[] shs = getSupportedHierarchies(codingScheme, versionOrTag, hierarchyID);
        for (SupportedHierarchy sh : shs) {
            String code = sh.getRootCode();
            if (code.equals("@") || code.equals("@@")) {
                // Need to resolve first level; only want to return 'real'
                // concepts ...
                ConceptReference cr = new ConceptReference();
                cr.setCode(sh.getRootCode());
                
                CodedNodeGraph cng = getLexBIGService().getNodeGraph(codingScheme, versionOrTag, null);
                cng = cng.restrictToAssociations(ConvenienceMethods.createNameAndValueList(sh.getAssociationNames()),
                        null);
                try {
                    cns = cns.union(cng.toNodeList(cr, sh.getIsForwardNavigable(), !sh.getIsForwardNavigable(), 0, -1));
                } catch (Exception e) {
                    getLogger().error("Unable to resolve hierarchy root nodes.", e);
                }
            } else {
                // Root was a real concept; add it explicitly ...
                CodedNodeSet toCode = getLexBIGService().getCodingSchemeConcepts(codingScheme, versionOrTag);
                toCode.restrictToCodes(ConvenienceMethods.createConceptReferenceList(new String[] { sh.getRootCode() },
                        codingScheme));
                cns = cns.union(toCode);
            }
        }
        return cns;
    }

    /**
     * Primitive method to resolve and cache coding scheme representations.
     * 
     * @param codingScheme
     * @param versionOrTag
     * @return CodingScheme
     * @throws LBException
     */
    @LgClientSideSafe
    protected CodingScheme getCodingScheme(String codingScheme, CodingSchemeVersionOrTag versionOrTag)
            throws LBException {

        Object[] params = new Object[] { codingScheme, versionOrTag };

        // Check and re-use cached value if present ...
        Object key = getCacheKey(params);
        Object val = null;
        if ((val = getCache_CodingSchemes().get(key)) != null)
            return (CodingScheme) val;

        // Not found; resolve it ...
        val = getLexBIGService().resolveCodingScheme(codingScheme, versionOrTag);

        // Cache and return the new value ...
        getCache_CodingSchemes().put(key, val);
        return (CodingScheme) val;
    }

    /**
     * Primitive method to resolve and cache coding scheme copyRight text.
     * 
     * @param codingScheme
     * @param versionOrTag
     * @return CodingScheme
     * @throws LBException
     */
    @LgClientSideSafe
    public String getCodingSchemeCopyright(String codingScheme, CodingSchemeVersionOrTag versionOrTag)
            throws LBException {

        Object[] params = new Object[] { codingScheme, versionOrTag };
        getLogger().logMethod(params);

        // Check and re-use cached value if present ...
        Object key = getCacheKey(params);
        Object val = null;
        if ((val = getCache_CopyRights().get(key)) != null)
            return (String) val;

        // Not found; resolve it ...
        val = getLexBIGService().resolveCodingSchemeCopyright(codingScheme, versionOrTag);

        // Cache and return the new value ...
        getCache_CopyRights().put(key, val);
        return (String) val;
    }

    /**
     * Primitive method to retrieve next level of hierarchy based on association
     * names and directionality, but could ultimately be linked to several root
     * nodes based on supported hierarchy definitions.
     * <p>
     * Returns the list of concepts that match the given concept and association
     * names, empty if none are available.
     * 
     * @param codingScheme
     * @param versionOrTag
     * @param assocNames
     * @param fwd
     * @param conceptCode
     * @param resolveConcepts
     * @param assocQuals
     * @return AssociationList
     * @throws LBException
     */
    @LgClientSideSafe
    protected AssociationList getHierarchyLevelNext(String codingScheme, CodingSchemeVersionOrTag versionOrTag,
            String[] assocNames, boolean fwd, String conceptCode, boolean resolveConcepts, NameAndValueList assocQuals)
            throws LBException {

        getLogger().logMethod(
                new Object[] { codingScheme, versionOrTag, assocNames, fwd, conceptCode, resolveConcepts, assocQuals });

        AssociationList al = new AssociationList();
        for (String assoc : assocNames) {
            // Resolve the next nodes for every possible association ...
            ResolvedConceptReferenceList rcrl = getHierarchyNodesNext(codingScheme, versionOrTag,
                    new String[] { assoc }, fwd, conceptCode, resolveConcepts, assocQuals);

            // If found, append an entry to the association list ...
            if (rcrl.getResolvedConceptReferenceCount() > 0) {
                Association a = new Association();
                a.setAssociationName(assoc);
                a.setAssociatedConcepts(new AssociatedConceptList());
                a.setAssociationReference(ConvenienceMethods.createConceptReference(conceptCode, codingScheme));
                String dirName = fwd ? getAssociationForwardName(assoc, codingScheme, versionOrTag)
                        : getAssociationReverseName(assoc, codingScheme, versionOrTag);
                a.setDirectionalName(StringUtils.isNotBlank(dirName) ? dirName : "[R]" + assoc);

                // And add associated concepts for each resolved instance.
                // Adjust direction of returned assoc so that we are always
                // moving forward (returned value navigated by source
                // instead of target).
                for (int i = 0; i < rcrl.getResolvedConceptReferenceCount(); i++) {
                    ResolvedConceptReference rcr = rcrl.getResolvedConceptReference(i);
                    AssociationList dirAssocList = fwd ? rcr.getSourceOf() : rcr.getTargetOf();
                    if (dirAssocList == null)
                        continue;
                    for (int j = 0; j < dirAssocList.getAssociationCount(); j++) {
                        Association srcAssoc = dirAssocList.getAssociation(j);
                        AssociatedConceptList srcConceptList = srcAssoc.getAssociatedConcepts();
                        for (int k = 0; k < srcConceptList.getAssociatedConceptCount(); k++) {
                            AssociatedConcept ac = srcConceptList.getAssociatedConcept(k);
                            a.getAssociatedConcepts().addAssociatedConcept(ac);
                        }
                    }
                    if (a.getAssociatedConcepts().getAssociatedConceptCount() > 0)
                        al.addAssociation(a);
                }
            }
        }
        return al;
    }

    /**
     * Primitive method to retrieve next level of hierarchy based on association
     * names and directionality, but could ultimately be linked to several root
     * nodes based on supported hierarchy definitions.
     * <p>
     * Returns the list of concepts that match the given concept and association
     * names, empty if none are available.
     * 
     * @param codingScheme
     * @param versionOrTag
     * @param assocNames
     * @param fwd
     * @param conceptCode
     * @param assocQuals
     * @param resolveConcepts
     * @return ResolvedConceptReferenceList
     * @throws LBException
     */
    @LgClientSideSafe
    protected ResolvedConceptReferenceList getHierarchyNodesNext(String codingScheme,
            CodingSchemeVersionOrTag versionOrTag, String[] assocNames, boolean fwd, String conceptCode,
            boolean resolveConcepts, NameAndValueList assocQuals) throws LBException {

        getLogger().logMethod(
                new Object[] { codingScheme, versionOrTag, assocNames, fwd, conceptCode, resolveConcepts, assocQuals });

        CodedNodeGraph cng = getLexBIGService().getNodeGraph(codingScheme, versionOrTag, null);
        cng = cng.restrictToAssociations(ConvenienceMethods.createNameAndValueList(assocNames, codingScheme),
                assocQuals);

        // Return the concept list ...
        int rDepth = resolveConcepts ? 1 : -1;
        return cng.resolveAsList(ConvenienceMethods.createConceptReference(conceptCode, codingScheme), fwd, !fwd,
                rDepth, 1, null, null, null, null, -1);
    }

    /**
     * Returns a list of associations representing navigable paths between the
     * given concept and root node combination. It is assumed that the
     * associations generally narrow in the given direction, making this a
     * bounded problem.
     * <p>
     * This method handles caching. The primitive methods performing the actual
     * resolve are the single and multi-root getters.
     * <p>
     * Returns the resolved chain; empty if the association cannot be followed
     * from the concept to root code.
     * <p>
     * Note: For purposes of this method, hierarchical associations are assumed
     * to be transitive.
     * 
     * @param codingScheme
     * @param versionOrTag
     * @param assocNames
     * @param fwd
     * @param conceptCode
     * @param rootCode
     * @param resolveConcepts
     * @param assocQuals
     * @param maxToReturn
     * @return AssociationList
     * @throws LBException
     */
    @LgClientSideSafe
    protected AssociationList getHierarchyPathToRoot(String codingScheme, CodingSchemeVersionOrTag versionOrTag,
            String[] assocNames, boolean fwd, String conceptCode, String rootCode, boolean resolveConcepts,
            NameAndValueList assocQuals, int maxToReturn) throws LBException {

        getLogger().logMethod(
                new Object[] { codingScheme, versionOrTag, assocNames, fwd, conceptCode, rootCode, resolveConcepts,
                        assocQuals, maxToReturn });

        // Resolve paths using primitive methods ...
        AssociationList val = new AssociationList();
        if (assocNames.length >= 1)
            val = getHierarchyPathToRootPrim(codingScheme, versionOrTag, assocNames, fwd, conceptCode, rootCode,
                    resolveConcepts, assocQuals, maxToReturn);
        return val;
    }

    /**
     * Resolves all links representing navigable paths between the start and
     * stop codes for the given association, adding them to the returned
     * association list. This method assumes navigation occurs on a single
     * association axis.
     * <p>
     * Returns the resolved chain; empty if the association cannot be followed
     * from the concept to root code.
     * <p>
     * Note: For purposes of this method, hierarchical associations are assumed
     * to be transitive.
     * 
     * @param codingScheme
     * @param versionOrTag
     * @param assocName
     * @param fwd
     * @param conceptCode
     * @param rootCode
     * @param resolveConcepts
     * @param pathResolveOption
     * @param assocQuals
     * @param maxToReturn
     * @return AssociationList
     * @throws LBException
     */
    @LgClientSideSafe
    protected AssociationList getHierarchyPathToRootPrim(String codingScheme, CodingSchemeVersionOrTag versionOrTag,
            String[] assocNames, boolean fwd, String conceptCode, String rootCode, boolean resolveConcepts,
            NameAndValueList assocQuals, int maxToReturn) throws LBException {
        return this.getHierarchyPathToRootPrim(codingScheme, versionOrTag, assocNames, fwd, conceptCode, rootCode,
                resolveConcepts, assocQuals, maxToReturn, new ArrayList<String>());
    }

    @LgClientSideSafe
    protected AssociationList getHierarchyPathToRootPrim(String codingScheme, CodingSchemeVersionOrTag versionOrTag,
            String[] assocNames, boolean fwd, String conceptCode, String rootCode, boolean resolveConcepts,
            NameAndValueList assocQuals, int maxToReturn, List<String> codeChain) throws LBException {

        getLogger().logMethod(
                new Object[] { codingScheme, versionOrTag, assocNames, fwd, conceptCode, rootCode, resolveConcepts,
                        assocQuals, maxToReturn });
        int leftToReturn = maxToReturn > 0 ? maxToReturn : Integer.MAX_VALUE;

        // Create an association list to hold all defined paths,
        // and temporary map to reference associations by name.
        AssociationList assocList = new AssociationList();
        Map<String, Association> assocByName = new HashMap<String, Association>();

        // Resolve immediate neighbors (distance 1) for the given concept ...
        CodedNodeGraph cng = getLexBIGService().getNodeGraph(codingScheme, versionOrTag, null);
        cng = cng.restrictToAssociations(ConvenienceMethods.createNameAndValueList(assocNames, codingScheme),
                assocQuals);
        int rcDepth = resolveConcepts ? 1 : -1;
        ResolvedConceptReferenceList rcrl = cng.resolveAsList(ConvenienceMethods.createConceptReference(conceptCode,
                codingScheme), fwd, !fwd, rcDepth, 1, null, null, null, -1);

        // Run through each neighbor, recursing as necessary to check if
        // there is eventually an embedded match with the stop code.
        if (rcrl.getResolvedConceptReferenceCount() > 0) {

            // Evaluate each resolved neighbor for a match (does it match the
            // root?). If so, we are done. Add it to the temporary association
            // and move on to check the next neighbor for yet another possible
            // paths.
            for (int i = 0; i < rcrl.getResolvedConceptReferenceCount() && leftToReturn > 0; i++) {
                ResolvedConceptReference rcr = rcrl.getResolvedConceptReference(i);
                AssociationList candidates = fwd ? rcr.getSourceOf() : rcr.getTargetOf();
                if (candidates != null)
                    for (int j = 0; j < candidates.getAssociationCount() && leftToReturn > 0; j++) {

                        // Each associated concept represents a sub-branch to
                        // consider ,,,
                        Association candidate = candidates.getAssociation(j);
                        for (int k = 0; k < candidate.getAssociatedConcepts().getAssociatedConceptCount()
                                && leftToReturn > 0; k++) {
                            AssociatedConcept leafConcept = candidate.getAssociatedConcepts().getAssociatedConcept(k);
                            String leafCode = leafConcept.getConceptCode();

                            if (codeChain.contains(leafCode)) {
                                continue;
                            }

                            AssociatedConcept tempConcept = null;
                            if (rootCode.equals(leafCode)) {
                                // We have a direct match; add to the chain to
                                // be returned.
                                tempConcept = leafConcept;
                                leftToReturn--;

                            } else {
                                List<String> clonedList = deepCloneStringList(codeChain);
                                clonedList.add(leafCode);
                                // Not the stop node, but recurse to see if the
                                // leaf is the start of a sub-branch that
                                // eventually ends in the
                                // root. We keep navigation moving in the
                                // requested direction. This could
                                // be forward or reverse, but is always
                                // narrowing.
                                AssociationList subbranchAtLeaf = getHierarchyPathToRootPrim(codingScheme,
                                        versionOrTag, assocNames, fwd, leafCode, rootCode, resolveConcepts, assocQuals,
                                        maxToReturn, clonedList);

                                // If empty, recursion showed this chain is not
                                // terminated by the requested root code.
                                // Stop processing and continue in the loop.
                                if (subbranchAtLeaf.getAssociationCount() == 0) {
                                    continue;
                                }

                                // Yes, this is an intermediate node. Create a
                                // new associated concept so we can represent
                                // everything moving broader to narrower
                                // ("to root") in forward direction,
                                // and include the result of the recursive call
                                // as branch content.
                                tempConcept = leafConcept;
                                tempConcept.setSourceOf(subbranchAtLeaf);
                                leftToReturn--;
                            }

                            // Did we discover a root node or intermediate node
                            // on path to root?
                            if (tempConcept != null) {
                                // Do we have this path already defined for this
                                // association?
                                // If yes, this is just another concept
                                // reference to include.
                                // If no, create and register a new association
                                // to contain the path.
                                String assocName = candidate.getAssociationName();
                                Association a = assocByName.get(assocName);
                                if (a == null) {
                                    a = new Association();
                                    assocByName.put(assocName, a);
                                    a.setAssociationName(assocName);
                                    a.setAssociatedConcepts(new AssociatedConceptList());
                                    a.setAssociationReference(ConvenienceMethods.createConceptReference(conceptCode,
                                            codingScheme));
                                    String dirName = candidate.getDirectionalName();
                                    a.setDirectionalName(StringUtils.isNotBlank(dirName) ? dirName : (fwd ? assocName
                                            : "[R]" + assocName));
                                }
                                a.getAssociatedConcepts().addAssociatedConcept(tempConcept);
                            }
                        }
                    }
            }
            // Done processing this branch (or sub-branch) from the specified
            // code to root. Return all participating associations as a single
            // list ...
            for (Iterator<Association> it = assocByName.values().iterator(); it.hasNext();)
                assocList.addAssociation(it.next());
        }
        return assocList;
    }

    /**
     * Deep Clone a String List
     * 
     * @param list
     * @return the cloned list
     */
    private List<String> deepCloneStringList(List<String> list) {
        List<String> clonedList = new ArrayList<String>();
        for (String string : list) {
            clonedList.add(new String(string));
        }
        return clonedList;
    }

    /**
     * Return a basic array of strings containing root codes for the given
     * hierarchy ID. If present, artificial codes '@' and '@@' are pre-expanded.
     * If the hierarchy ID is null, root codes for all registered hierarchies
     * are returned.
     * 
     * @param codingScheme
     * @param versionOrTag
     * @param hierarchyID
     * @return String[]
     * @throws LBException
     */
    @LgClientSideSafe
    protected String[] getHierarchyRootCodes(String codingScheme, CodingSchemeVersionOrTag versionOrTag,
            String hierarchyID) throws LBException {

        Object[] params = new Object[] { codingScheme, versionOrTag, hierarchyID };
        getLogger().logMethod(params);

        // Check and re-use cached value if present ...
        Object key = getCacheKey(params);
        Object val = null;
        if ((val = getCache_HRootCodes().get(key)) != null)
            return (String[]) val;

        // No cache entry; figure it out ...
        CodedNodeSet cns = getHierarchyRootSet(codingScheme, versionOrTag, hierarchyID);
        ArrayList<String> codes = new ArrayList<String>();
        ResolvedConceptReferenceList codeList = cns.resolveToList(null, ConvenienceMethods
                .createLocalNameList("__INVALID"), null, -1);
        for (int i = 0; i < codeList.getResolvedConceptReferenceCount(); i++)
            codes.add(codeList.getResolvedConceptReference(i).getConceptCode());

        // Cache and return the new value ...
        val = codes.toArray(new String[codes.size()]);
        getCache_HRootCodes().put(key, val);
        return (String[]) val;
    }

    /**
     * Returns an array of hierarchies supported by the given coding scheme.
     * 
     * @param codingScheme
     * @param versionOrTag
     * @return SupportedHierarchy[]
     * @throws LBException
     */
    @LgClientSideSafe
    protected SupportedHierarchy[] getSupportedHierarchies(String codingScheme, CodingSchemeVersionOrTag versionOrTag)
            throws LBException {
        getLogger().logMethod(new Object[] { codingScheme, versionOrTag, });

        CodingScheme cs = getCodingScheme(codingScheme, versionOrTag);
        if (cs == null) {
            throw new LBResourceUnavailableException("Coding scheme not found.");
        }
        Mappings mappings = cs.getMappings();
        return mappings.getSupportedHierarchy();
    }

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
    @LgClientSideSafe
    public SupportedHierarchy[] getSupportedHierarchies(String codingScheme, CodingSchemeVersionOrTag versionOrTag,
            String hierarchyId) throws LBException {

        getLogger().logMethod(new Object[] { codingScheme, versionOrTag });

        // Retrieve all candidates ...
        SupportedHierarchy[] hierarchies = getSupportedHierarchies(codingScheme, versionOrTag);
        if (hierarchyId == null)
            return hierarchies;

        // ID was specified, filter out non-matches ...
        ArrayList<SupportedHierarchy> matches = new ArrayList<SupportedHierarchy>();
        for (SupportedHierarchy h : hierarchies)
            if (h.getLocalId().equals(hierarchyId))
                matches.add(h);

        // Return the matches
        return (SupportedHierarchy[]) matches.toArray(new SupportedHierarchy[matches.size()]);
    }

    /**
     * Returns an indication of whether a path exists between the given concept
     * and the set of root concepts for the given associations in the given
     * direction.
     * <p>
     * Note: For purposes of this method, hierarchical associations are assumed
     * to be transitive. When possible, transitive closure is used as a shortcut
     * to determine the answer.
     * 
     * @param codingScheme
     * @param versionOrTag
     * @param assocNames
     * @param fwd
     * @param conceptCode
     * @param rootCodes
     * @param assocQuals
     * @return boolean
     * @throws LBException
     */
    protected boolean hasHierarchyPathToRoot(String codingScheme, CodingSchemeVersionOrTag versionOrTag,
            String[] assocNames, boolean fwd, String conceptCode, String[] rootCodes, NameAndValueList assocQuals)
            throws LBException {

        Object[] params = new Object[] { codingScheme, versionOrTag, assocNames, fwd, conceptCode, rootCodes,
                assocQuals };
        getLogger().logMethod(params);
        for (String rootCode : rootCodes) {
            // Do we have a path for this root?
            if (assocNames.length >= 1
                    && hasHierarchyPathToRoot(codingScheme, versionOrTag, assocNames, fwd, conceptCode, rootCode,
                            assocQuals)) {

                return true;
            }
        }
        return false;

    }

    /**
     * Returns an indication of whether a path exists between the given concept
     * and root for the given associations in the given direction.
     * <p>
     * Note: For purposes of this method, hierarchical associations are assumed
     * to be transitive. When possible, transitive closure is used as a shortcut
     * to determine the answer.
     * 
     * @param codingScheme
     * @param versionOrTag
     * @param assocNames
     * @param fwd
     * @param conceptCode
     * @param rootCode
     * @param assocQuals
     * @return boolean
     * @throws LBException
     */
    protected boolean hasHierarchyPathToRoot(String codingScheme, CodingSchemeVersionOrTag versionOrTag,
            String[] assocNames, boolean fwd, String conceptCode, String rootCode, NameAndValueList assocQuals)
            throws LBException {

        Object[] params = new Object[] { codingScheme, versionOrTag, assocNames, fwd, conceptCode, rootCode, assocQuals };
        getLogger().logMethod(params);
        
        if(ServiceUtility.isSupplement(codingScheme, versionOrTag)) {
            return true;
        }

        // Check and re-use cached value if present ...
        Object key = getCacheKey(params);
        Object val = null;
        if ((val = getCache_HPathToRootExists().get(key)) != null)
            return (Boolean) val;

        // Nothing in cache. Evaluate now ...
        if (assocNames.length == 0)
            val = false;
        else
            val = (assocNames.length == 1) ? hasHierarchyPathToRootSingleAxis(codingScheme, versionOrTag,
                    assocNames[0], fwd, conceptCode, rootCode, assocQuals) : hasHierarchyPathToRootMultiAxis(
                    codingScheme, versionOrTag, assocNames, fwd, conceptCode, rootCode, assocQuals);

        // Cache and return the new value ...
        getCache_HPathToRootExists().put(key, val);
        return (Boolean) val;
    }

    /**
     * Returns an indication of whether a path exists between the given concept
     * and root for the given associations in the given direction.
     * <p>
     * Note: For purposes of this method, hierarchical associations are assumed
     * to be transitive. When possible, transitive closure is used as a shortcut
     * to determine the answer.
     * 
     * @param codingScheme
     * @param versionOrTag
     * @param assocName
     * @param fwd
     * @param conceptCode
     * @param rootCode
     * @param assocQuals
     * @return boolean
     * @throws LBException
     */
    protected boolean hasHierarchyPathToRootSingleAxis(String codingScheme, CodingSchemeVersionOrTag versionOrTag,
            String assocName, boolean fwd, String conceptCode, String rootCode, NameAndValueList assocQuals)
            throws LBException {

        getLogger().logMethod(new Object[] { codingScheme, versionOrTag, fwd, conceptCode, rootCode, assocQuals });

        String srcCode = fwd ? conceptCode : rootCode;
        String tgtCode = fwd ? rootCode : conceptCode;
        if (!srcCode.equals(tgtCode)) {
            CodedNodeGraph cng = getLexBIGService().getNodeGraph(codingScheme, versionOrTag, null);
            return cng.areCodesRelated(ConvenienceMethods.createNameAndValue(assocName, codingScheme),
                    ConvenienceMethods.createConceptReference(srcCode, codingScheme), ConvenienceMethods
                            .createConceptReference(tgtCode, codingScheme), false);
        }
        return true;
    }

    /**
     * Returns an indication of whether a path exists between the given concept
     * and root for the given associations in the given direction.
     * <p>
     * Note: For purposes of this method, hierarchical associations are assumed
     * to be transitive. When possible, transitive closure is used as a shortcut
     * to determine the answer.
     * 
     * @param codingScheme
     * @param versionOrTag
     * @param assocNames
     * @param fwd
     * @param conceptCode
     * @param rootCode
     * @param assocQuals
     * @return boolean
     * @throws LBException
     */
    protected boolean hasHierarchyPathToRootMultiAxis(String codingScheme, CodingSchemeVersionOrTag versionOrTag,
            String[] assocNames, boolean fwd, String conceptCode, String rootCode, NameAndValueList assocQuals)
            throws LBException {

        getLogger().logMethod(
                new Object[] { codingScheme, versionOrTag, assocNames, fwd, conceptCode, rootCode, assocQuals });
        if (conceptCode.equals(rootCode))
            return true;

        // Check single path shortcuts ...
        for (String assocName : assocNames)
            if (hasHierarchyPathToRootSingleAxis(codingScheme, versionOrTag, assocName, fwd, conceptCode, rootCode,
                    assocQuals))
                return true;

        // No luck; resolve a path ...
        AssociationList paths = getHierarchyPathToRoot(codingScheme, versionOrTag, assocNames, fwd, conceptCode,
                rootCode, false, assocQuals, 1);
        return paths.getAssociationCount() > 0;
    }

    /**
     * Assign the associated LexBIGService instance.
     * <p>
     * Note: This method must be invoked by users of the distributed LexBIG API
     * to set the service to an EVSApplicationService object, allowing client
     * side implementations to use these convenience methods.
     */
    @LgClientSideSafe(force=true)
    public void setLexBIGService(LexBIGService lbs) {
        lbs_ = lbs;
    }

    /**
     * Return the associated LexBIGService instance; lazy initialized as
     * required.
     */
    @LgClientSideSafe
    public LexBIGService getLexBIGService() {
        if (lbs_ == null)
            lbs_ = LexBIGServiceImpl.defaultInstance();
        return lbs_;
    }

    /**
     * Return a cache key based on the given items.
     * 
     * @param basis
     * @return Object
     */
    @LgClientSideSafe
    protected Object getCacheKey(Object[] basis) {
        StringBuffer sb = new StringBuffer(256);
        for (Object o : basis)
            sb.append("::").append(o instanceof Object[] ? getCacheKey((Object[]) o) : ObjectToString.toString(o));
        return DigestUtils.shaHex(sb.toString());
    }

    /**
     * Return the map used to cache codingScheme information, which maps from
     * key (derived from request parameters) to CodingScheme object.
     * <p>
     * Note: Methods requiring the cache should invoke this method rather than
     * directly referencing the class variable in order to allow lazy
     * initialization.
     */
    @LgClientSideSafe
    protected Map getCache_CopyRights() {
        if (cache_copyRights_ == null)
            cache_copyRights_ = Collections.synchronizedMap(new LRUMap(128));
        return cache_copyRights_;
    }

    /**
     * Return the map used to cache codingScheme copyRitght information, which
     * maps from key (derived from request parameters) to CopyRight String.
     * <p>
     * Note: Methods requiring the cache should invoke this method rather than
     * directly referencing the class variable in order to allow lazy
     * initialization.
     */
    @LgClientSideSafe
    protected Map getCache_CodingSchemes() {
        if (cache_codingSchemes_ == null)
            cache_codingSchemes_ = Collections.synchronizedMap(new LRUMap(16));
        return cache_codingSchemes_;
    }

    /**
     * Return the map used to cache hierarchy ID information, which maps from
     * key (derived from request parameters) to an array of hierarchy IDs
     * (String[]).
     * <p>
     * Note: Methods requiring the cache should invoke this method rather than
     * directly referencing the class variable in order to allow lazy
     * initialization.
     */
    @LgClientSideSafe
    protected Map getCache_HIDs() {
        if (cache_hIDs_ == null)
            cache_hIDs_ = Collections.synchronizedMap(new LRUMap(128));
        return cache_hIDs_;
    }

    /**
     * Return the map used to cache hierarchy root information, which maps from
     * key (derived from request parameters) to a ResolvedConceptReferenceList.
     * <p>
     * Note: Methods requiring the cache should invoke this method rather than
     * directly referencing the class variable in order to allow lazy
     * initialization.
     */
    @LgClientSideSafe
    protected Map getCache_HRoots() {
        if (cache_hRoots_ == null)
            cache_hRoots_ = Collections.synchronizedMap(new LRUMap(128));
        return cache_hRoots_;
    }

    /**
     * Return the map used to cache hierarchy root code information, which maps
     * from key (derived from request parameters) to an array of root codes
     * (String[]).
     * <p>
     * Note: Methods requiring the cache should invoke this method rather than
     * directly referencing the class variable in order to allow lazy
     * initialization.
     */
    @LgClientSideSafe
    protected Map getCache_HRootCodes() {
        if (cache_hRootCodes_ == null)
            cache_hRootCodes_ = Collections.synchronizedMap(new LRUMap(128));
        return cache_hRootCodes_;
    }

    /**
     * Return the map used to cache hierarchy root existence, which maps from
     * key (derived from request parameters) to a Boolean value.
     * <p>
     * Note: Methods requiring the cache should invoke this method rather than
     * directly referencing the class variable in order to allow lazy
     * initialization.
     */
    @LgClientSideSafe
    protected Map getCache_HPathToRootExists() {
        if (cache_hPathToRootExists_ == null)
            cache_hPathToRootExists_ = Collections.synchronizedMap(new LRUMap(2048));
        return cache_hPathToRootExists_;
    }

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
    @LgClientSideSafe
    public String getEntityDescription(String codingScheme, CodingSchemeVersionOrTag versionOrTag, String code)
            throws LBException {
        CodedNodeSet cns = getLexBIGService().getCodingSchemeConcepts(codingScheme, versionOrTag);
        cns = cns.restrictToCodes(Constructors.createConceptReferenceList(code));
        LocalNameList noopList = new LocalNameList();
        noopList.addEntry("_noop_");
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, noopList, null, 1);
        if (rcrl.getResolvedConceptReferenceCount() > 0) {
            EntityDescription desc = rcrl.getResolvedConceptReference(0).getEntityDescription();
            if (desc != null)
                return desc.getContent();
        }
        return null;
    }

    /**
     * Add LuceneIndexes for the given list of concepts.
     * 
     * @param codingSchemeName
     * @param versionOrTag
     * @param entityCode
     */
    @LgAdminFunction
    public void addEntityLuceneIndexes(String codingSchemeName, CodingSchemeVersionOrTag versionOrTag,
            List<String> entityCodes) throws LBException {
        throw new UnsupportedOperationException("This is handled in the Dao Layer, either as part"
                + " of the Entity Service itself, or as a Listener.");
    }

    /**
     * Remove LuceneIndexes for the given list of concepts.
     * 
     * @param codingSchemeName
     * @param versionOrTag
     * @param entityCode
     */
    @LgAdminFunction
    public void removeEntityLuceneIndexes(String codingSchemeName, CodingSchemeVersionOrTag versionOrTag,
            List<String> entityCodes) throws LBException {
        throw new UnsupportedOperationException("This is handled in the Dao Layer, either as part"
                + " of the Entity Service itself, or as a Listener.");
    }

    /**
     * modify LuceneIndexes for the given list of concepts.
     * 
     * @param codingSchemeName
     * @param versionOrTag
     * @param entityCode
     */
    @LgAdminFunction
    public void modifyEntityLuceneIndexes(String codingSchemeName, CodingSchemeVersionOrTag versionOrTag,
            List<String> entityCodes) throws LBException {
        throw new UnsupportedOperationException("This is handled in the Dao Layer, either as part"
                + " of the Entity Service itself, or as a Listener.");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods
     * #getHierarchyOrphanedConcepts(java.lang.String,
     * org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag,
     * java.lang.String)
     */
    public ResolvedConceptReferenceList getHierarchyOrphanedConcepts(String codingSchemeName,
            CodingSchemeVersionOrTag versionOrTag, String hierarchyID) throws LBException {
        // TODO Auto-generated method stub
        String internalCodingSchemeName = null;
        String version = null;

        if (versionOrTag == null) {
            version = ResourceManager.instance().getInternalVersionStringForTag(codingSchemeName, null);
        } else {
            version = versionOrTag.getVersion();
        }

        internalCodingSchemeName = ResourceManager.instance().getInternalCodingSchemeNameForUserCodingSchemeName(
                codingSchemeName, version);
        SupportedHierarchy[] shs = getSupportedHierarchies(codingSchemeName, versionOrTag, hierarchyID);
        SupportedHierarchy sh = shs[0];
        String assocs[] = sh.getAssociationNames();
        ArrayList<Operation> pendingOperations = new ArrayList<Operation>();

        RestrictToAssociations rta = new RestrictToAssociations(ConvenienceMethods.createNameAndValueList(assocs), null);
        pendingOperations.add(rta);
        boolean resolveFwd = sh.isIsForwardNavigable();
        // Get the root concepts of the ontology.
        ResolvedConceptReferenceList root_rcrl = getHierarchyRoots(codingSchemeName, versionOrTag, hierarchyID);
        ArrayList<String> root_codes = new ArrayList<String>();

        for (ResolvedConceptReference rcr : root_rcrl.getResolvedConceptReference()) {
            root_codes.add(rcr.getConceptCode());
        }
        ConceptReferenceList root_list = new ConceptReferenceList();
        root_list.setConceptReference(root_rcrl.getResolvedConceptReference());

        if (resolveFwd) {
            RestrictToSourceCodes rsc = new RestrictToSourceCodes(root_list);
            pendingOperations.add(rsc);
        } else {
            RestrictToTargetCodes rtc = new RestrictToTargetCodes(root_list);
            pendingOperations.add(rtc);
        }
        try {
            SQLInterface si = ResourceManager.instance().getSQLInterface(internalCodingSchemeName, version);
            ResolvedConceptReferenceList rcrl = SQLImplementedMethods.orphanedEntityQuery(si, pendingOperations,
                    resolveFwd, internalCodingSchemeName, version, null);
            // We are not done yet. We need to ensure that the root nodes are
            // excluded.
            for (ResolvedConceptReference rcr : rcrl.getResolvedConceptReference()) {
                if (root_codes.contains(rcr.getConceptCode())) {
                    rcrl.removeResolvedConceptReference(rcr);
                }
            }

            // If we have more than one association in the hierarchy. Check if
            // there are paths to root when we follow multiple associations
            if (assocs.length > 1) {
                for (ResolvedConceptReference rcr : rcrl.getResolvedConceptReference()) {
                    if (hasHierarchyPathToRoot(codingSchemeName, versionOrTag, assocs, !resolveFwd, rcr
                            .getConceptCode(), root_codes.toArray(new String[root_codes.size()]), null)) {
                        rcrl.removeResolvedConceptReference(rcr);
                    }
                }
            }

            // getLogger().debug("Time to execute getHierarchyLevelNextCount=" +
            // (System.currentTimeMillis() - startTime));
            return rcrl;

        } catch (MissingResourceException e) {
            throw new LBException(e.getMessage(), e);
        }

    }

    public List<SupportedProperty> getSupportedPropertiesOfTypeComment(String codingScheme,
            CodingSchemeVersionOrTag versionOrTag) throws LBException {
        String internalCodingSchemeName = null;
        String version = null;
        if (versionOrTag == null) {
            version = getSystemResourceService().getInternalVersionStringForTag(codingScheme, null);
        } else {
            version = versionOrTag.getVersion();
        }

        internalCodingSchemeName = getSystemResourceService().getInternalCodingSchemeNameForUserCodingSchemeName(
                codingScheme, version);

        return (List<SupportedProperty>) getCodingSchemeService().getSupportedPropertyForPropertyType(
                internalCodingSchemeName, version, PropertyTypes.COMMENT);
    }

    public List<SupportedProperty> getSupportedPropertiesOfTypeDefinition(String codingScheme,
            CodingSchemeVersionOrTag versionOrTag) throws LBException {
        String internalCodingSchemeName = null;
        String version = null;
        if (versionOrTag == null) {
            version = getSystemResourceService().getInternalVersionStringForTag(codingScheme, null);
        } else {
            version = versionOrTag.getVersion();
        }

        internalCodingSchemeName = getSystemResourceService().getInternalCodingSchemeNameForUserCodingSchemeName(
                codingScheme, version);

        return (List<SupportedProperty>) getCodingSchemeService().getSupportedPropertyForPropertyType(
                internalCodingSchemeName, version, PropertyTypes.DEFINITION);
    }

    public List<SupportedProperty> getSupportedPropertiesOfTypePresentation(String codingScheme,
            CodingSchemeVersionOrTag versionOrTag) throws LBException {
        String internalCodingSchemeName = null;
        String version = null;
        if (versionOrTag == null) {
            version = getSystemResourceService().getInternalVersionStringForTag(codingScheme, null);
        } else {
            version = versionOrTag.getVersion();
        }

        internalCodingSchemeName = getSystemResourceService().getUriForUserCodingSchemeName(codingScheme, version);

        return (List<SupportedProperty>) getCodingSchemeService().getSupportedPropertyForPropertyType(
                internalCodingSchemeName, version, PropertyTypes.PRESENTATION);
    }

    public List<SupportedProperty> getSupportedPropertiesOfTypeProperty(String codingScheme,
            CodingSchemeVersionOrTag versionOrTag) throws LBException {
        String internalCodingSchemeName = null;
        String version = null;
        if (versionOrTag == null) {
            version = getSystemResourceService().getInternalVersionStringForTag(codingScheme, null);
        } else {
            version = versionOrTag.getVersion();
        }

        internalCodingSchemeName = getSystemResourceService().getInternalCodingSchemeNameForUserCodingSchemeName(
                codingScheme, version);

        return (List<SupportedProperty>) getCodingSchemeService().getSupportedPropertyForPropertyType(
                internalCodingSchemeName, version, PropertyTypes.PROPERTY);
    }

    private CodingSchemeService getCodingSchemeService() {
        return LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getCodingSchemeService();
    }

    private SystemResourceService getSystemResourceService() {
        return LexEvsServiceLocator.getInstance().getSystemResourceService();
    }

    @Override
    public ResolvedConceptReference getNodesPath(final String codingSchemeUri, final CodingSchemeVersionOrTag versionOrTag, final String containerName,
            final String associationName, final String sourceCode, final String sourceNS, final String targetCode, final String targetNS) throws LBParameterException {

        final String version = ServiceUtility.getVersion(codingSchemeUri, versionOrTag);

        DaoCallbackService callbackService = 
            LexEvsServiceLocator.getInstance().
            getDatabaseServiceManager().getDaoCallbackService();

        String path = callbackService.executeInDaoLayer(new DaoCallback<String>(){
            @Override
            public String execute(DaoManager daoManager) {
                String csUid;
                String apUid;
                csUid = daoManager.getCodingSchemeDao(codingSchemeUri, version).getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);
                apUid = daoManager.getAssociationDao(codingSchemeUri, version).getAssociationPredicateUIdByContainerName(csUid, containerName, associationName);
                return daoManager.getAssociationDao(codingSchemeUri, version).getNodesPath(csUid, sourceCode, sourceNS, targetCode, targetNS, apUid);    
            }
        });
        
        if (StringUtils.isEmpty(path))
            return null;
        else {

            String[] nodes = path.split(DefaultTransitivityBuilder.PATH_DELIMITER);
            
            // root node
            String[] rootNode = nodes[0].split(DefaultTransitivityBuilder.CODE_NAMESPACE_DELIMITER);
            ResolvedConceptReference rootConRef = this.createConRef(rootNode, codingSchemeUri, version);

            // move on to the list
            ResolvedConceptReference previousConRef = rootConRef;
            for (int i = 1 ; i < nodes.length; i++) {
                String[] aNode = nodes[i].split(DefaultTransitivityBuilder.CODE_NAMESPACE_DELIMITER);
                AssociatedConcept curConRef = this.createConRef(aNode, codingSchemeUri, version);
                AssociationList assnList = new AssociationList();
                Association assn = new Association();
                assn.setAssociationName(associationName);
                AssociatedConceptList associatedConcepts = new AssociatedConceptList();
                associatedConcepts.addAssociatedConcept(curConRef);
                assn.setAssociatedConcepts(associatedConcepts);
                assnList.addAssociation(assn);
                previousConRef.setSourceOf(assnList);

                previousConRef = curConRef;
            }
            
            return rootConRef;
        }

    }
    
    public List<String> getDistinctNamespacesOfCode(
            String codingScheme,
            CodingSchemeVersionOrTag versionOrTag,
            final String code) throws LBException {
        
        AbsoluteCodingSchemeVersionReference ref = 
            ServiceUtility.getAbsoluteCodingSchemeVersionReference(codingScheme, versionOrTag, true);
        
        final String uri = ref.getCodingSchemeURN();
        final String version = ref.getCodingSchemeVersion();
       
        List<String> namespaces = 
            LexEvsServiceLocator.getInstance().
            getDatabaseServiceManager().
            getDaoCallbackService().
            executeInDaoLayer(new DaoCallback<List<String>>() {

                @Override
                public List<String> execute(DaoManager daoManager) {
                    String codingSchemeUid = daoManager.getCodingSchemeDao(uri, version).getCodingSchemeUIdByUriAndVersion(uri, version);

                    EntityDao entityDao = daoManager.getEntityDao(uri, version);

                    return entityDao.getDistinctEntityNamespacesFromCode(codingSchemeUid, code);
                }});
        
        return namespaces;
    }
    
    public List<ResolvedConceptReference> getAncestorsInTransitiveClosure( String codingScheme,
            CodingSchemeVersionOrTag versionOrTag, final String code, final String association) throws LBParameterException{
        AbsoluteCodingSchemeVersionReference ref = 
                ServiceUtility.getAbsoluteCodingSchemeVersionReference(codingScheme, versionOrTag, true);
            final String uri = ref.getCodingSchemeURN();
            final String version = ref.getCodingSchemeVersion();
            return getTransitiveClosure(code, association, uri, version, true);      
        }

    public List<ResolvedConceptReference> getDescendentsInTransitiveClosure( String codingScheme,
            CodingSchemeVersionOrTag versionOrTag, final String code, final String association) throws LBParameterException{
        AbsoluteCodingSchemeVersionReference ref = 
                ServiceUtility.getAbsoluteCodingSchemeVersionReference(codingScheme, versionOrTag, true);
            final String uri = ref.getCodingSchemeURN();
            final String version = ref.getCodingSchemeVersion();
            return getTransitiveClosure(code, association, uri, version, false);
        }
    
    private List<ResolvedConceptReference> getTransitiveClosure(final String code, final String associationName, final String uri,
            final String version, boolean ancestors) {
        DatabaseServiceManager databaseServiceManager = LexEvsServiceLocator.getInstance().getDatabaseServiceManager();
        ClosureIterator iterator = new ClosureIterator(databaseServiceManager, uri, version, code, associationName, ancestors);
        List<ResolvedConceptReference> refs = new ArrayList<ResolvedConceptReference>();
        while(iterator.hasNext()){
            GraphDbTriple triple = iterator.next();
            ResolvedConceptReference ref = new ResolvedConceptReference();
            if(ancestors == false){
            ref.setCode(triple.getSourceEntityCode());
            ref.setCodeNamespace(triple.getSourceEntityNamespace());
            ref.setCodingSchemeURI(triple.getSourceSchemeUri());
            ref.setCodingSchemeVersion(triple.getSourceSchemeVersion());
            ref.setEntityDescription(Constructors.createEntityDescription(triple.getSourceDescription()));
            }
            else{
                ref.setCode(triple.getTargetEntityCode());
                ref.setCodeNamespace(triple.getTargetEntityNamespace());
                ref.setCodingSchemeURI(triple.getTargetSchemeUri());
                ref.setCodingSchemeVersion(triple.getTargetSchemeVersion());
                ref.setEntityDescription(Constructors.createEntityDescription(triple.getTargetDescription()));
            }
            refs.add(ref);
        }
        return refs;
    }


    public AssociatedConceptList getallIncomingConceptsForAssociation(String codingScheme, CodingSchemeVersionOrTag csvt,
            String code, String associationName, int maxToReturn) throws LBInvocationException, LBParameterException, LBException{
        NameAndValueList nvList = Constructors.createNameAndValueList(associationName);
        ResolvedConceptReferenceList matches = lbs_.getNodeGraph(codingScheme, csvt, null).restrictToAssociations(nvList,
                null).resolveAsList(ConvenienceMethods.createConceptReference(code, codingScheme), false, true, 1, 1,
                new LocalNameList(), null, null, maxToReturn);
               ResolvedConceptReference ref =  matches.getResolvedConceptReference(0);
               AssociationList list = ref.getTargetOf();
              Association assoc =  list.getAssociation(0);
             AssociatedConceptList alist =  assoc.getAssociatedConcepts();
        return alist;
    }
    
    private boolean useBackwardCompatibleMethods(String codingScheme, CodingSchemeVersionOrTag versionOrTag) throws LBParameterException {
        String VERSION_17 = "1.7";
        String VERSION_18 = "1.8";
        
        String schemaVersion = 
            ServiceUtility.getSchemaVersionForCodingScheme(codingScheme, versionOrTag);
        
        return schemaVersion.equals(VERSION_17) || schemaVersion.equals(VERSION_18);
        
    }

    private AssociatedConcept createConRef(String[] node, String uri, String version) {
        AssociatedConcept conRef = new AssociatedConcept();
        conRef.setCode(node[0]);
        conRef.setCodeNamespace(node[1]);
        conRef.setCodingSchemeURI(uri);
        conRef.setCodingSchemeVersion(version);
        return conRef;
    }
    
    private class ClosureIterator extends AbstractPageableIterator<GraphDbTriple>{
        /**
         * 
         */
        private static final long serialVersionUID = 167913667057561717L;

        private static final int DEFAULT_PAGE_SIZE = 1000;
        
        private DatabaseServiceManager databaseServiceManager;
        private String codingSchemeUri;
        private String version;
        private String associationName;
        private String code;
        private boolean ancestors;

        public ClosureIterator(DatabaseServiceManager databaseServiceManager,
                String codingSchemeUri, String version, String associationName, String code, boolean ancestors) {
            this(databaseServiceManager, codingSchemeUri, version, code, DEFAULT_PAGE_SIZE, associationName, ancestors);
        }

        public ClosureIterator(DatabaseServiceManager databaseServiceManager,
                String codingSchemeUri, String version, String code,  int pageSize, String associationName, boolean ancestors) {

            super(pageSize);
            this.codingSchemeUri = codingSchemeUri;
            this.version = version;
            this.databaseServiceManager = databaseServiceManager;
            this.associationName = associationName;
            this.code = code;
            this.ancestors = ancestors;
        }
        
        private List<GraphDbTriple> getDescendentTriples(final int start){
        return databaseServiceManager.getDaoCallbackService()
                .executeInDaoLayer(new DaoCallback<List<GraphDbTriple>>() {

                    @Override
                    public List<GraphDbTriple> execute(DaoManager daoManager) {
                        String codingSchemeId = daoManager
                                .getCurrentCodingSchemeDao()
                                .getCodingSchemeUIdByUriAndVersion(
                                        codingSchemeUri, version);

                        return daoManager.getCurrentAssociationDao()
                                .getAllDescendantTriplesTrOfCodingScheme(
                                        codingSchemeId, associationName, code, start, DEFAULT_PAGE_SIZE);
                    }
                });
        }
        

        
        private List<GraphDbTriple> getAncestorTriples(final int start){
        return databaseServiceManager.getDaoCallbackService()
                .executeInDaoLayer(new DaoCallback<List<GraphDbTriple>>() {

                    @Override
                    public List<GraphDbTriple> execute(DaoManager daoManager) {
                        String codingSchemeId = daoManager
                                .getCurrentCodingSchemeDao()
                                .getCodingSchemeUIdByUriAndVersion(
                                        codingSchemeUri, version);

                        return daoManager.getCurrentAssociationDao()
                                .getAllAncestorTriplesTrOfCodingScheme(
                                        codingSchemeId, associationName, code, start, DEFAULT_PAGE_SIZE);
                    }
                });
        }
        
        @Override
        protected List<? extends GraphDbTriple> doPage(int currentPosition, int pageSize) {
            // TODO Auto-generated method stub
            if(ancestors == false){
                return getDescendentTriples(currentPosition);}
            else
            {  return getAncestorTriples(currentPosition);}
            
        }


    }
    
    
}