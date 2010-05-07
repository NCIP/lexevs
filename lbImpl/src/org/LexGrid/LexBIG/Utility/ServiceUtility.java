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
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.naming.URIMap;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.system.service.SystemResourceService;
import org.springframework.util.CollectionUtils;

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
    
    public static void validateParameter(String codingSchemeUri, String codingSchemeVersion, LocalNameList list, Class<? extends URIMap> supportedAttributeClass) throws LBParameterException {
        if(list == null) {
            return;
        }
        for(String localName : list.getEntry()) {
            validateParameter(codingSchemeUri,codingSchemeVersion, localName, supportedAttributeClass);
        }
    }
    
    public static void validateParameter(String codingSchemeUri, String codingSchemeVersion, String localId, Class<? extends URIMap> supportedAttributeClass) throws LBParameterException {
        if(StringUtils.isBlank(localId)) {
            return;
        }
        CodingSchemeService codingSchemeService = 
            LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getCodingSchemeService();
        
        if(! codingSchemeService.validatedSupportedAttribute(codingSchemeUri, codingSchemeVersion, localId, supportedAttributeClass)) {
            throw new LBParameterException(localId + " is not a valid Parameter.");
        }
    }
}
