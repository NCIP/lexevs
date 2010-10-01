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
package org.LexGrid.LexBIG.Impl.codedNodeSetOperations;

import java.util.Enumeration;

import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.codedNodeSetOperations.interfaces.Operation;
import org.LexGrid.LexBIG.Impl.codedNodeSetOperations.interfaces.Restriction;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.annotations.LgClientSideSafe;
import org.LexGrid.naming.SupportedContext;
import org.LexGrid.naming.SupportedProperty;
import org.LexGrid.naming.SupportedPropertyQualifier;
import org.LexGrid.naming.SupportedSource;
import org.LexGrid.naming.URIMap;
import org.lexevs.locator.LexEvsServiceLocator;

/**
 * Holder for the RestrictToProperties operation.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class RestrictToProperties implements Restriction, Operation {

    private static final long serialVersionUID = 7762452729573553393L;
    protected LocalNameList propertyList_;
    private LocalNameList sourceList_;
    private LocalNameList contextList_;
    private NameAndValueList qualifierList_;
    protected PropertyType[] propertyTypes_;

    @LgClientSideSafe
    public LocalNameList getPropertyList() {
        return this.propertyList_;
    }

    protected RestrictToProperties(LocalNameList sourceList, LocalNameList contextList, NameAndValueList qualifierList,
            String internalCodeSystemName, String internalVersionString) throws LBParameterException,
            LBInvocationException {
        try {
            if (sourceList != null) {
                Enumeration<? extends String> sources = sourceList.enumerateEntry();
                while (sources.hasMoreElements()) {
                    this.validateSupportedAttribute(
                            internalCodeSystemName, 
                            internalVersionString, 
                            sources.nextElement(), 
                            SupportedSource.class);
                }
            }
            sourceList_ = sourceList;

            if (qualifierList != null) {
                Enumeration<? extends NameAndValue> qualifiers = qualifierList.enumerateNameAndValue();
                while (qualifiers.hasMoreElements()) {
                    this.validateSupportedAttribute(
                            internalCodeSystemName, 
                            internalVersionString, 
                            qualifiers.nextElement().getName(), 
                            SupportedPropertyQualifier.class);
                }
            }
            qualifierList_ = qualifierList;

            if (contextList_ != null) {
                Enumeration<? extends String> contexts = contextList.enumerateEntry();
                while (contexts.hasMoreElements()) {
                    this.validateSupportedAttribute(
                            internalCodeSystemName, 
                            internalVersionString, 
                            contexts.nextElement(), 
                            SupportedContext.class);
                }
            }

            contextList_ = contextList;
        } catch (LBParameterException e) {
            throw e;
        } 
    }

    public RestrictToProperties(LocalNameList propertyList, PropertyType[] propertyTypes, LocalNameList sourceList,
            LocalNameList contextList, NameAndValueList qualifierList, String internalCodeSystemName,
            String internalVersionString) throws LBInvocationException, LBParameterException {
        this(sourceList, contextList, qualifierList, internalCodeSystemName, internalVersionString);
        try {

            if ((propertyList == null || propertyList.getEntryCount() == 0)
                    && (propertyTypes == null || propertyTypes.length == 0)) {
                throw new LBParameterException(
                        "At least one propertyList or one propertyType parameter must be supplied.",
                        "propertyList or propertyType");
            }

            if (propertyList != null) {
                Enumeration<? extends String> items = propertyList.enumerateEntry();
                while (items.hasMoreElements()) {
                    // this will throw the necessary exceptions
                    this.validateSupportedAttribute(
                            internalCodeSystemName, 
                            internalVersionString, 
                            items.nextElement(), 
                            SupportedProperty.class);
                }
                propertyList_ = propertyList;
            }

            propertyTypes_ = propertyTypes;
        } catch (LBParameterException e) {
            throw e;
        } 
    }
    
    protected void validateSupportedAttribute(
            String internalCodeSystemName, 
            String internalVersionString, 
            String localId, Class<? extends URIMap> supportedAttributeClass) throws LBParameterException {
        if(internalCodeSystemName != null && internalVersionString != null) {

            String uri = 
                LexEvsServiceLocator.getInstance().getSystemResourceService().getUriForUserCodingSchemeName(internalCodeSystemName, internalVersionString);

            boolean isValid = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().
            getCodingSchemeService().validatedSupportedAttribute(uri, internalVersionString, localId, supportedAttributeClass);

            if(!isValid) {
                throw new LBParameterException("Attribute with Id: " + localId + " is not valid, or is not registered as a " + supportedAttributeClass.getSimpleName());
            }
        }
    }

    /**
     * This constructor is for starting a bit set that matches all documents...
     */
    public RestrictToProperties()

    {
        propertyList_ = null;
    }

    /**
     * @return the contextList
     */
    @LgClientSideSafe
    public LocalNameList getContextList() {
        return this.contextList_;
    }

    /**
     * @return the qualifierList
     */
    @LgClientSideSafe
    public NameAndValueList getQualifierList() {
        return this.qualifierList_;
    }

    /**
     * @return the sourceList
     */
    @LgClientSideSafe
    public LocalNameList getSourceList() {
        return this.sourceList_;
    }

    @LgClientSideSafe
    public PropertyType[] getPropertyTypes() {
        return this.propertyTypes_;
    }
}