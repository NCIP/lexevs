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

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.SortDescription;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.SortOption;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.SortContext;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Query.Filter;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.Extensions.ExtensionRegistryImpl;
import org.LexGrid.LexBIG.Impl.namespace.NamespaceHandler;
import org.LexGrid.LexBIG.Impl.pagedgraph.root.NullFocusRootsResolver;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.concepts.Entity;
import org.LexGrid.naming.URIMap;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry;
import org.lexevs.registry.service.Registry.ResourceType;
import org.lexevs.system.service.SystemResourceService;

/**
 * Utility ServiceUtility Class with various helper methods.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ServiceUtility {

    /**
     * Resolve concept reference.
     * 
     * @param conceptReference the concept reference
     * 
     * @return the entity
     * 
     * @throws LBParameterException the LB parameter exception
     */
    public static Entity resolveConceptReference(ResolvedConceptReference conceptReference) throws LBParameterException {
        String codingSchemeUri = conceptReference.getCodingSchemeURI();
        String version = conceptReference.getCodingSchemeVersion();

        if (codingSchemeUri == null) {
            return null;
        }

        if (version == null) {
            version = getVersion(codingSchemeUri, null);
        }

        Entity entity = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getEntityService().getEntity(
                codingSchemeUri, version, conceptReference.getCode(), conceptReference.getCodeNamespace());

        return entity;
    }

    /**
     * Gets the absolute coding scheme version reference.
     * 
     * @param codingScheme the coding scheme
     * @param tagOrVersion the tag or version
     * @param strict the strict
     * 
     * @return the absolute coding scheme version reference
     * 
     * @throws LBParameterException the LB parameter exception
     */
    public static AbsoluteCodingSchemeVersionReference getAbsoluteCodingSchemeVersionReference(String codingScheme,
            CodingSchemeVersionOrTag tagOrVersion, boolean strict) throws LBParameterException {
        String uri;
        String version;
        try {
            version = getVersion(codingScheme, tagOrVersion);
            
            uri = LexEvsServiceLocator.getInstance().getSystemResourceService().getUriForUserCodingSchemeName(
                    codingScheme, version);
        } catch (LBParameterException e) {
            if (strict) {
                throw e;
            } else {
                return null;
            }
        }

        return Constructors.createAbsoluteCodingSchemeVersionReference(uri, version);
    }

    /**
     * Gets the version.
     * 
     * @param codingScheme the coding scheme
     * @param tagOrVersion the tag or version
     * 
     * @return the version
     * 
     * @throws LBParameterException the LB parameter exception
     */
    public static String getVersion(String codingScheme, CodingSchemeVersionOrTag tagOrVersion)
            throws LBParameterException {
        SystemResourceService systemResourceService = LexEvsServiceLocator.getInstance().getSystemResourceService();

        String version = null;
        if (tagOrVersion == null || tagOrVersion.getVersion() == null || tagOrVersion.getVersion().length() == 0) {
            version = systemResourceService.getInternalVersionStringForTag(codingScheme, (tagOrVersion == null ? null
                    : tagOrVersion.getTag()));
        } else {
            version = tagOrVersion.getVersion();
        }

        return version;
    }

    /**
     * Gets the coding scheme name.
     * 
     * @param codingScheme the coding scheme
     * @param version the version
     * 
     * @return the coding scheme name
     * 
     * @throws LBParameterException the LB parameter exception
     */
    public static String getCodingSchemeName(String codingScheme, String version) throws LBParameterException {
        SystemResourceService systemResourceService = LexEvsServiceLocator.getInstance().getSystemResourceService();

        return systemResourceService.getInternalCodingSchemeNameForUserCodingSchemeName(codingScheme, version);
    }

    /**
     * Validate parameter.
     * 
     * @param codingSchemeNameOrUri the coding scheme name or uri
     * @param codingSchemeVersion the coding scheme version
     * @param list the list
     * @param supportedAttributeClass the supported attribute class
     * 
     * @throws LBParameterException the LB parameter exception
     */
    public static void validateParameter(String codingSchemeNameOrUri, String codingSchemeVersion, LocalNameList list,
            Class<? extends URIMap> supportedAttributeClass) throws LBParameterException {
        if (list == null) {
            return;
        }
        for (String localName : list.getEntry()) {
            validateParameter(codingSchemeNameOrUri, codingSchemeVersion, localName, supportedAttributeClass);
        }
    }

    /**
     * Throw exception or return default.
     * 
     * @param exception the exception
     * @param defaultReturnValue the default return value
     * @param strict the strict
     * 
     * @return the o
     * 
     * @throws T the T
     */
    public static <T extends Throwable, O> O throwExceptionOrReturnDefault(T exception, O defaultReturnValue,
            boolean strict) throws T {
        if (strict) {
            throw exception;
        } else {
            return defaultReturnValue;
        }
    }

    /**
     * Validate parameter.
     * 
     * @param codingSchemeNameOrUri the coding scheme name or uri
     * @param codingSchemeVersion the coding scheme version
     * @param localId the local id
     * @param supportedAttributeClass the supported attribute class
     * 
     * @throws LBParameterException the LB parameter exception
     */
    public static void validateParameter(String codingSchemeNameOrUri, String codingSchemeVersion, String localId,
            Class<? extends URIMap> supportedAttributeClass) throws LBParameterException {
        if (StringUtils.isBlank(localId)) {
            return;
        }

        String codingSchemeUri = LexEvsServiceLocator.getInstance().getSystemResourceService()
                .getUriForUserCodingSchemeName(codingSchemeNameOrUri, codingSchemeVersion);

        CodingSchemeService codingSchemeService = LexEvsServiceLocator.getInstance().getDatabaseServiceManager()
                .getCodingSchemeService();

        if (!codingSchemeService.validatedSupportedAttribute(codingSchemeUri, codingSchemeVersion, localId,
                supportedAttributeClass)) {
            throw new LBParameterException(localId + " is not a valid Parameter.");
        }
    }

    /**
     * Validate filters.
     * 
     * @param filterOptions the filter options
     * 
     * @return the filter[]
     * 
     * @throws LBParameterException the LB parameter exception
     */
    public static Filter[] validateFilters(LocalNameList filterOptions) throws LBParameterException {
        if (filterOptions != null && filterOptions.getEntryCount() > 0) {
            Filter[] temp = new Filter[filterOptions.getEntryCount()];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = ExtensionRegistryImpl.instance().getFilter(filterOptions.getEntry(i));
            }
            return temp;
        } else {
            return null;
        }
    }

    /**
     * Checks if is sort algorithm valid.
     * 
     * @param algorithm the algorithm
     * @param context the context
     * 
     * @return true, if is sort algorithm valid
     */
    public static boolean isSortAlgorithmValid(String algorithm, SortContext context) {
        if (ExtensionRegistryImpl.instance().getSortExtension(algorithm) != null) {
            if (context != null) {
                SortDescription sd = ExtensionRegistryImpl.instance().getSortExtension(algorithm);

                SortContext[] temp = sd.getRestrictToContext();
                for (int i = 0; i < temp.length; i++) {
                    if (temp[i].equals(context)) {
                        return true;
                    }
                }
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    /**
     * Pass filters.
     * 
     * @param candidate the candidate
     * @param filterOptions the filter options
     * 
     * @return true, if successful
     */
    public static boolean passFilters(ResolvedConceptReference candidate, Filter[] filterOptions) {

        if (ArrayUtils.isEmpty(filterOptions)) {
            return true;
        }

        if (NullFocusRootsResolver.isRefRootOrTail(candidate)) {
            return true;
        }

        boolean pass = true;
        for (Filter filter : filterOptions) {
            pass = (pass && filter.match(candidate));
        }

        return pass;
    }

    /**
     * Resolve resolved concept reference.
     * 
     * @param uri the uri
     * @param version the version
     * @param propertyNames the property names
     * @param propertyTypes the property types
     * @param namespaceHandler the namespace handler
     * @param resolvedConceptReference the resolved concept reference
     * 
     * @return the t
     * 
     * @throws LBParameterException the LB parameter exception
     */
    public static <T extends ResolvedConceptReference> T resolveResolvedConceptReference(String uri, String version,
            LocalNameList propertyNames, PropertyType[] propertyTypes, NamespaceHandler namespaceHandler,
            T resolvedConceptReference) throws LBParameterException {
        if (resolvedConceptReference.getCodeNamespace() != null) {
            AbsoluteCodingSchemeVersionReference ref = namespaceHandler.getCodingSchemeForNamespace(uri, version,
                    resolvedConceptReference.getCodeNamespace());

            uri = ref.getCodingSchemeURN();
            version = ref.getCodingSchemeVersion();
        }

        return resolveResolvedConceptReference(uri, version, propertyNames, propertyTypes, resolvedConceptReference);
    }

    /**
     * Resolve resolved concept reference.
     * 
     * @param uri the uri
     * @param version the version
     * @param propertyNames the property names
     * @param propertyTypes the property types
     * @param resolvedConceptReference the resolved concept reference
     * 
     * @return the t
     */
    public static <T extends ResolvedConceptReference> T resolveResolvedConceptReference(String uri, String version,
            LocalNameList propertyNames, PropertyType[] propertyTypes, T resolvedConceptReference) {
        Entity entity = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getEntityService().getEntity(
                uri, version, resolvedConceptReference.getCode(), resolvedConceptReference.getCodeNamespace(),
                DaoUtility.localNameListToString(propertyNames), DaoUtility.propertyTypeArrayToString(propertyTypes));

        resolvedConceptReference.setEntity(entity);

        return resolvedConceptReference;
    }

    /**
     * Gets the schema version for coding scheme.
     * 
     * @param codingSchemeName the coding scheme name
     * @param versionOrTag the version or tag
     * 
     * @return the schema version for coding scheme
     * 
     * @throws LBParameterException the LB parameter exception
     */
    public static String getSchemaVersionForCodingScheme(String codingSchemeName, CodingSchemeVersionOrTag versionOrTag)
            throws LBParameterException {
        String version = ServiceUtility.getVersion(codingSchemeName, versionOrTag);
        
        String uri = LexEvsServiceLocator.getInstance().getSystemResourceService().getUriForUserCodingSchemeName(
                codingSchemeName, version);  

        Registry registry = LexEvsServiceLocator.getInstance().getRegistry();

        RegistryEntry entry = registry.getCodingSchemeEntry(Constructors.createAbsoluteCodingSchemeVersionReference(
                uri, version));

        return entry.getDbSchemaVersion();
    }

    /**
     * Gets the entity.
     * 
     * @param codingSchemeUri the coding scheme uri
     * @param codingSchemeVersion the coding scheme version
     * @param entityCode the entity code
     * @param entityCodeNamespace the entity code namespace
     * 
     * @return the entity
     * 
     * @throws LBException the LB exception
     */
    public static Entity getEntity(String codingSchemeUri, String codingSchemeVersion, String entityCode,
            String entityCodeNamespace) throws LBException {
        CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
        versionOrTag.setVersion(codingSchemeVersion);

        LexBIGService lbsvc = LexBIGServiceImpl.defaultInstance();

        CodedNodeSet cns = lbsvc.getNodeSet(codingSchemeUri, versionOrTag, null);

        ResolvedConceptReferencesIterator iterator = cns.resolve(null, null, null, null, true);
        while (iterator.hasNext()) {
            ResolvedConceptReference conRef = iterator.next();
            if (conRef.getCode().equalsIgnoreCase(entityCode)
                    && conRef.getCodeNamespace().equalsIgnoreCase(entityCodeNamespace))
                return conRef.getEntity();

        }

        return null;

    }

    /**
     * Validate sort options.
     * 
     * @param sortOptions the sort options
     * 
     * @throws LBParameterException the LB parameter exception
     */
    public static void validateSortOptions(SortOptionList sortOptions) throws LBParameterException {
        if (sortOptions == null || sortOptions.getEntryCount() == 0) {
            return;
        }
        for (SortOption option : sortOptions.getEntry()) {
            SortDescription extension = ExtensionRegistryImpl.instance().getSortExtension(option.getExtensionName());
            if (extension == null) {
                throw new LBParameterException("Sort Option: " + option.getExtensionName()
                        + " is not a registered Sort Extension.");
            }

        }
    }
    
    /**
     * Checks if a given coding scheme is a supplement.
     * 
     * @param codingScheme the coding scheme
     * @param tagOrVersion the tag or version
     * 
     * @return true, if is supplement
     * 
     * @throws LBParameterException the LB parameter exception
     */
    public static boolean isSupplement(String codingScheme, CodingSchemeVersionOrTag tagOrVersion) throws LBParameterException {
        AbsoluteCodingSchemeVersionReference ref = 
            getAbsoluteCodingSchemeVersionReference(codingScheme, tagOrVersion, true);
        
        return isSupplement(ref.getCodingSchemeURN(), ref.getCodingSchemeVersion());
    }
    
    /**
     * Checks if a given coding scheme is a supplement.
     * 
     * @param uri the uri
     * @param version the version
     * 
     * @return true, if is supplement
     * 
     * @throws LBParameterException the LB parameter exception
     */
    public static boolean isSupplement(String uri, String version) throws LBParameterException {
        RegistryEntry entry = 
            LexEvsServiceLocator.getInstance().getRegistry().
            getCodingSchemeEntry(Constructors.createAbsoluteCodingSchemeVersionReference(uri, version));
        if(StringUtils.isNotBlank(entry.getSupplementsUri())
                &&
                StringUtils.isNotBlank(entry.getSupplementsVersion())){
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Gets the parent of a supplement.
     * 
     * @param uri the uri
     * @param version the version
     * 
     * @return the parent of supplement
     * 
     * @throws LBParameterException the LB parameter exception
     */
    public static AbsoluteCodingSchemeVersionReference getParentOfSupplement(
            String codingScheme, CodingSchemeVersionOrTag tagOrVersion) throws LBParameterException {
        AbsoluteCodingSchemeVersionReference ref = 
            getAbsoluteCodingSchemeVersionReference(codingScheme, tagOrVersion, true);
        
        return getParentOfSupplement(ref.getCodingSchemeURN(), ref.getCodingSchemeVersion());
    }
    
    /**
     * Gets the parent of a supplement.
     * 
     * @param uri the uri
     * @param version the version
     * 
     * @return the parent of supplement
     * 
     * @throws LBParameterException the LB parameter exception
     */
    public static AbsoluteCodingSchemeVersionReference getParentOfSupplement(String uri, String version) throws LBParameterException {
        RegistryEntry entry = 
            LexEvsServiceLocator.getInstance().getRegistry().
            getCodingSchemeEntry(Constructors.createAbsoluteCodingSchemeVersionReference(uri, version));
        if(StringUtils.isNotBlank(entry.getSupplementsUri())
                &&
                StringUtils.isNotBlank(entry.getSupplementsVersion())){
            return Constructors.createAbsoluteCodingSchemeVersionReference(
                    entry.getSupplementsUri(), 
                    entry.getSupplementsVersion());
        } else {
            throw new LBParameterException("URI: " + uri + " Version: " + version + " is not a Supplement of any Coding Scheme.");
        }  
    }
}
