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
import org.lexevs.exceptions.UnexpectedInternalError;
import org.lexevs.system.ResourceManager;

/**
 * The implementation of the history service against the custom UMLS history
 * file / table.
 * 
 * @author <A>Satyabodha Rao</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class UMLSHistoryServiceImpl implements HistoryService {
    private static final long serialVersionUID = 5666320190009376793L;
    private String urn_ = null;

    /**
     * This constructor is only here for Apache Axis to work correctly. It
     * should not be used by anyone.
     */
    public UMLSHistoryServiceImpl() {

    }

    public UMLSHistoryServiceImpl(String urn) throws LBParameterException {
        ResourceManager rm = ResourceManager.instance();
        try {
            urn_ = urn;
            // make sure we have one for it
            rm.getSQLInterfaceForHistory(urn_);

        } catch (LBParameterException e) {
            throw e;
        }

    }

    public NCIChangeEventList getAncestors(ConceptReference conceptReference) throws LBParameterException,
            LBInvocationException {
        try {
            return UMLSHistorySQLQueries.getAncestors(urn_, conceptReference);
        } catch (UnexpectedInternalError e) {
            throw new LBInvocationException("There was an unexpected internal error", e.getLogId());
        }
    }

    public SystemReleaseList getBaselines(Date releasedAfter, Date releasedBefore) throws LBParameterException,
            LBInvocationException {
        try {
            return UMLSHistorySQLQueries.getBaseLines(urn_, releasedAfter, releasedBefore);
        } catch (UnexpectedInternalError e) {
            throw new LBInvocationException("There was an unexpected internal error", e.getLogId());
        }
    }

    public CodingSchemeVersionList getConceptChangeVersions(ConceptReference conceptReference, Date beginDate,
            Date endDate) throws LBParameterException, LBInvocationException {
        try {
            return UMLSHistorySQLQueries.getConceptChangeVersions(urn_, conceptReference, beginDate, endDate);
        } catch (UnexpectedInternalError e) {
            throw new LBInvocationException("There was an unexpected internal error", e.getLogId());
        }
    }

    public CodingSchemeVersion getConceptCreationVersion(ConceptReference conceptReference)
            throws LBParameterException, LBInvocationException {
        try {
            return UMLSHistorySQLQueries.getConceptCreationVersion(urn_, conceptReference);
        } catch (UnexpectedInternalError e) {
            throw new LBInvocationException("There was an unexpected internal error", e.getLogId());
        }
    }

    public NCIChangeEventList getDescendants(ConceptReference conceptReference) throws LBParameterException,
            LBInvocationException {
        try {
            return UMLSHistorySQLQueries.getDescendants(urn_, conceptReference);
        } catch (UnexpectedInternalError e) {
            throw new LBInvocationException("There was an unexpected internal error", e.getLogId());
        }
    }

    public SystemRelease getEarliestBaseline() throws LBInvocationException {
        try {
            return UMLSHistorySQLQueries.getEarliestBaseLine(urn_);
        } catch (UnexpectedInternalError e) {
            throw new LBInvocationException("There was an unexpected internal error", e.getLogId());
        }
    }

    public NCIChangeEventList getEditActionList(ConceptReference conceptReference,
            CodingSchemeVersion codingSchemeVersion) throws LBParameterException, LBInvocationException {
        try {
            return UMLSHistorySQLQueries.getEditActionList(urn_, conceptReference, codingSchemeVersion);
        } catch (UnexpectedInternalError e) {
            throw new LBInvocationException("There was an unexpected internal error", e.getLogId());
        }
    }

    public NCIChangeEventList getEditActionList(ConceptReference conceptReference, Date beginDate, Date endDate)
            throws LBParameterException, LBInvocationException {
        try {
            return UMLSHistorySQLQueries.getEditActionList(urn_, conceptReference, beginDate, endDate);
        } catch (UnexpectedInternalError e) {
            throw new LBInvocationException("There was an unexpected internal error", e.getLogId());
        }
    }

    public NCIChangeEventList getEditActionList(ConceptReference conceptReference, URI releaseURN)
            throws LBParameterException, LBInvocationException {
        try {
            return UMLSHistorySQLQueries.getEditActionList(urn_, conceptReference, releaseURN);
        } catch (UnexpectedInternalError e) {
            throw new LBInvocationException("There was an unexpected internal error", e.getLogId());
        }
    }

    public SystemRelease getLatestBaseline() throws LBInvocationException {
        try {
            return UMLSHistorySQLQueries.getLatestBaseLine(urn_);
        } catch (UnexpectedInternalError e) {
            throw new LBInvocationException("There was an unexpected internal error", e.getLogId());
        }
    }

    public SystemReleaseDetail getSystemRelease(URI releaseURN) throws LBParameterException, LBInvocationException {
        try {
            return UMLSHistorySQLQueries.getSystemRelease(urn_, releaseURN);
        } catch (UnexpectedInternalError e) {
            throw new LBInvocationException("There was an unexpected internal error", e.getLogId());
        }
    }

    @Override
    public List<String> getCodeListforVersion(String currentVersion) {
        throw new UnsupportedOperationException("Not supported for RRF based terminologies");  
    }
}