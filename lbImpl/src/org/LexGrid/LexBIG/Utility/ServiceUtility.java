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

import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.SortDescription;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.SortContext;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Query.Filter;
import org.LexGrid.LexBIG.Impl.Extensions.ExtensionRegistryImpl;
import org.LexGrid.LexBIG.Impl.namespace.NamespaceHandler;
import org.LexGrid.LexBIG.Impl.pagedgraph.root.NullFocusRootsResolver;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.concepts.Entity;
import org.LexGrid.naming.URIMap;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry;
import org.lexevs.system.service.SystemResourceService;

/**
 * The Class ServiceUtility.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ServiceUtility {
    
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
    public static String getVersion(String codingScheme, CodingSchemeVersionOrTag tagOrVersion) throws LBParameterException {
        SystemResourceService systemResourceService = 
            LexEvsServiceLocator.getInstance().getSystemResourceService();
        
        String version = null;
        if (tagOrVersion == null || tagOrVersion.getVersion() == null || tagOrVersion.getVersion().length() == 0) {
            version = systemResourceService.getInternalVersionStringForTag(codingScheme,
                    (tagOrVersion == null ? null : tagOrVersion.getTag()));
        } else {
            version = tagOrVersion.getVersion();
        }
        
        return version;
    }
    
    public static void validateParameter(String codingSchemeNameOrUri, String codingSchemeVersion, LocalNameList list, Class<? extends URIMap> supportedAttributeClass) throws LBParameterException {
        if(list == null) {
            return;
        }
        for(String localName : list.getEntry()) {
            validateParameter(codingSchemeNameOrUri,codingSchemeVersion, localName, supportedAttributeClass);
        }
    }
    public static <T extends Throwable, O> O throwExceptionOrReturnDefault(T exception, O defaultReturnValue, boolean strict) throws T {
        if(strict) {
            throw exception;
        } else {
            return defaultReturnValue;
        }
    }
    
    public static void validateParameter(String codingSchemeNameOrUri, String codingSchemeVersion, String localId, Class<? extends URIMap> supportedAttributeClass) throws LBParameterException {
        if(StringUtils.isBlank(localId)) {
            return;
        }
        
        String codingSchemeUri = 
            LexEvsServiceLocator.getInstance().
            getSystemResourceService().
            getUriForUserCodingSchemeName(codingSchemeNameOrUri);
        
        CodingSchemeService codingSchemeService = 
            LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getCodingSchemeService();
        
        if(! codingSchemeService.validatedSupportedAttribute(codingSchemeUri, codingSchemeVersion, localId, supportedAttributeClass)) {
            throw new LBParameterException(localId + " is not a valid Parameter.");
        }
    }
    
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
    
    public static boolean isSortAlgorithmValid(String algorithm, SortContext context) {
        if (ExtensionRegistryImpl.instance().getSortExtension(algorithm) != null) {
            if (context != null) {
                SortDescription sd  = ExtensionRegistryImpl.instance().getSortExtension(algorithm);

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
    
    public static boolean passFilters(ResolvedConceptReference candidate, Filter[] filterOptions ){
        
        if(ArrayUtils.isEmpty(filterOptions)) {
            return true;
        }
        
        if(NullFocusRootsResolver.isRefRootOrTail(candidate)) {
            return true;
        }

        boolean pass = true;
        for(Filter filter : filterOptions) {
            pass = ( pass && filter.match(candidate) );
        }

        return pass;
    }
    
    public static <T extends ResolvedConceptReference> T resolveResolvedConceptReference(
            String uri, 
            String version, 
            LocalNameList propertyNames,
            PropertyType[] propertyTypes,
            NamespaceHandler namespaceHandler,
            T resolvedConceptReference) throws LBParameterException {
        if(resolvedConceptReference.getCodeNamespace() != null) {
            AbsoluteCodingSchemeVersionReference ref = 
                namespaceHandler.getCodingSchemeForNamespace(uri, version, resolvedConceptReference.getCodeNamespace());
        
            uri = ref.getCodingSchemeURN();
            version = ref.getCodingSchemeVersion();
        }
        
        return resolveResolvedConceptReference(
                uri, 
                version, 
                propertyNames, 
                propertyTypes, 
                resolvedConceptReference);
    }
    
    public static <T extends ResolvedConceptReference> T resolveResolvedConceptReference(
            String uri, 
            String version, 
            LocalNameList propertyNames,
            PropertyType[] propertyTypes,
            T resolvedConceptReference){
        Entity entity = LexEvsServiceLocator.getInstance().
            getDatabaseServiceManager().
            getEntityService().
            getEntity(uri, 
                    version, 
                    resolvedConceptReference.getCode(), 
                    resolvedConceptReference.getCodeNamespace(), 
                    DaoUtility.localNameListToString(propertyNames), 
                    DaoUtility.propertyTypeArrayToString(propertyTypes));
        
        resolvedConceptReference.setEntity(entity);
        
        return resolvedConceptReference;
    }
    
    public static String getSchemaVersionForCodingScheme(String codingSchemeName, CodingSchemeVersionOrTag versionOrTag) throws LBParameterException {

        String uri = LexEvsServiceLocator.getInstance().getSystemResourceService().getUriForUserCodingSchemeName(codingSchemeName);
        String version = ServiceUtility.getVersion(codingSchemeName, versionOrTag);

        Registry registry = LexEvsServiceLocator.getInstance().getRegistry();

        RegistryEntry entry = 
            registry.getCodingSchemeEntry(Constructors.createAbsoluteCodingSchemeVersionReference(uri, version));

        return entry.getDbSchemaVersion();
    }
}
