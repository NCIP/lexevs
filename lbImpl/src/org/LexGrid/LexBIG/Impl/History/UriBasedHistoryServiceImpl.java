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
package org.LexGrid.LexBIG.Impl.History;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeVersionList;
import org.LexGrid.LexBIG.DataModel.Collections.NCIChangeEventList;
import org.LexGrid.LexBIG.DataModel.Collections.SystemReleaseList;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.SystemReleaseDetail;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.History.HistoryService;
import org.LexGrid.versions.CodingSchemeVersion;
import org.LexGrid.versions.SystemRelease;
import org.lexevs.dao.database.service.ncihistory.NciHistoryService;
import org.lexevs.locator.LexEvsServiceLocator;

/**
 * The Class UriBasedHistoryServiceImpl.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class UriBasedHistoryServiceImpl implements HistoryService {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 524036401675315235L;
    
    /** The date format. */
    private DateFormat dateFormat = NciHistoryService.dateFormat;

    /** The uri. */
    private String uri;
    
    /**
     * USED ONLY BY PROXIES -- DON'T USE THIS CONSTRUCTOR!
     * 
     * Instantiates a new uri based history service impl.
     * 
     * @throws LBParameterException the LB parameter exception
     */
    public UriBasedHistoryServiceImpl() throws LBParameterException {
        super();
    }

    /**
     * Instantiates a new uri based history service impl.
     * 
     * @param codingSchemeUri the coding scheme uri
     * 
     * @throws LBParameterException the LB parameter exception
     */
    public UriBasedHistoryServiceImpl(String codingSchemeUri) throws LBParameterException {
        uri = codingSchemeUri;  
    }

    /**
     * Gets the nci history service.
     * 
     * @return the nci history service
     */
    private NciHistoryService getNciHistoryService() {
        return LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getNciHistoryService();
    }
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.History.HistoryService#getAncestors(org.LexGrid.LexBIG.DataModel.Core.ConceptReference)
     */
    @Override
    public NCIChangeEventList getAncestors(ConceptReference conceptReference) throws LBParameterException,
            LBInvocationException {
       return getNciHistoryService().getAncestors(uri, conceptReference);
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.History.HistoryService#getBaselines(java.util.Date, java.util.Date)
     */
    @Override
    public SystemReleaseList getBaselines(Date releasedAfter, Date releasedBefore) throws LBParameterException,
            LBInvocationException {
        return this.getNciHistoryService().getBaseLines(uri, releasedAfter, releasedBefore);
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.History.HistoryService#getConceptChangeVersions(org.LexGrid.LexBIG.DataModel.Core.ConceptReference, java.util.Date, java.util.Date)
     */
    @Override
    public CodingSchemeVersionList getConceptChangeVersions(ConceptReference conceptReference, Date beginDate,
            Date endDate) throws LBParameterException, LBInvocationException {
        return this.getNciHistoryService().getConceptChangeVersions(uri, conceptReference, beginDate, endDate);
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.History.HistoryService#getConceptCreationVersion(org.LexGrid.LexBIG.DataModel.Core.ConceptReference)
     */
    @Override
    public CodingSchemeVersion getConceptCreationVersion(ConceptReference conceptReference)
            throws LBParameterException, LBInvocationException {
        CodingSchemeVersion creationVersion = this.getNciHistoryService().getConceptCreationVersion(uri, conceptReference);
        if(creationVersion == null) {
            throw new LBParameterException("No results found for parameter:", "conceptReference");
        }
        
        return creationVersion;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.History.HistoryService#getDescendants(org.LexGrid.LexBIG.DataModel.Core.ConceptReference)
     */
    @Override
    public NCIChangeEventList getDescendants(ConceptReference conceptReference) throws LBParameterException,
            LBInvocationException {
        return getNciHistoryService().getDescendants(uri, conceptReference);
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.History.HistoryService#getEarliestBaseline()
     */
    @Override
    public SystemRelease getEarliestBaseline() throws LBInvocationException {
        return getNciHistoryService().getEarliestBaseLine(uri);
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.History.HistoryService#getEditActionList(org.LexGrid.LexBIG.DataModel.Core.ConceptReference, org.LexGrid.versions.CodingSchemeVersion)
     */
    @Override
    public NCIChangeEventList getEditActionList(ConceptReference conceptReference,
            CodingSchemeVersion codingSchemeVersion) throws LBParameterException, LBInvocationException {
        if(codingSchemeVersion.getVersion() != null) {
            try {
                return getNciHistoryService().getEditActionList(uri, conceptReference, this.dateFormat.parse(codingSchemeVersion.getVersion()));
            } catch (ParseException e) {
                throw new LBParameterException(e.getMessage());
            }
        } else {
            try {
                return getNciHistoryService().getEditActionList(uri, conceptReference, new URI(codingSchemeVersion.getReleaseURN()));
            } catch (URISyntaxException e) {
                throw new LBParameterException(e.getMessage());
            }
        }
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.History.HistoryService#getEditActionList(org.LexGrid.LexBIG.DataModel.Core.ConceptReference, java.util.Date, java.util.Date)
     */
    @Override
    public NCIChangeEventList getEditActionList(ConceptReference conceptReference, Date beginDate, Date endDate)
            throws LBParameterException, LBInvocationException {
        return getNciHistoryService().getEditActionList(uri, conceptReference, beginDate, endDate);
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.History.HistoryService#getEditActionList(org.LexGrid.LexBIG.DataModel.Core.ConceptReference, java.net.URI)
     */
    @Override
    public NCIChangeEventList getEditActionList(ConceptReference conceptReference, URI releaseURN)
            throws LBParameterException, LBInvocationException {
        return getNciHistoryService().getEditActionList(uri, conceptReference, releaseURN);
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.History.HistoryService#getLatestBaseline()
     */
    @Override
    public SystemRelease getLatestBaseline() throws LBInvocationException {
        return getNciHistoryService().getLatestBaseLine(uri);
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.History.HistoryService#getSystemRelease(java.net.URI)
     */
    @Override
    public SystemReleaseDetail getSystemRelease(URI releaseURN) throws LBParameterException, LBInvocationException {
        return getNciHistoryService().getSystemRelease(uri, releaseURN);
    }

    @Override
    public List<String> getCodeListforVersion(String currentVersion) {
        return getNciHistoryService().getCodeListForVersion(currentVersion, uri);
    }

}