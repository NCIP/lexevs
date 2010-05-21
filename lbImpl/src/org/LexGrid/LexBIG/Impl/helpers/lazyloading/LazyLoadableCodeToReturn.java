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
package org.LexGrid.LexBIG.Impl.helpers.lazyloading;

import org.LexGrid.LexBIG.Impl.helpers.CodeToReturn;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;
import org.lexevs.dao.index.service.entity.EntityIndexService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.system.service.SystemResourceService;

/**
 * The Class LazyLoadableCodeToReturn.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class LazyLoadableCodeToReturn extends CodeToReturn {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 2847101693632620073L;

    /** The document id. */
    private int documentId;
    
    /** The internal code system name. */
    private String internalCodeSystemName;
    
    /** The internal version string. */
    private String internalVersionString;
    
    /** The is hydrated. */
    private boolean isHydrated = false;
    
    private EntityIndexService entityIndexService;
    
    private SystemResourceService systemResourceService;
    
    
    /**
     * Instantiates a new lazy loadable code to return.
     */
    public LazyLoadableCodeToReturn(){
        super();
    }
    
    /**
     * Instantiates a new lazy loadable code to return.
     * 
     * @param scoreDoc the score doc
     * @param internalCodeSystemName the internal code system name
     * @param internalVersionString the internal version string
     */
    public LazyLoadableCodeToReturn(ScoreDoc scoreDoc, String internalCodeSystemName, String internalVersionString){
        this(   internalCodeSystemName,
                internalVersionString,
                scoreDoc.score, 
                scoreDoc.doc);
    }
       
    /**
     * Instantiates a new lazy loadable code to return.
     * 
     * @param internalCodeSystemName the internal code system name
     * @param internalVersionString the internal version string
     * @param score the score
     * @param documentId the document id
     */
    public LazyLoadableCodeToReturn(
            String internalCodeSystemName, 
            String internalVersionString, 
            float score, 
            int documentId){
        super();
        this.setVersion(internalVersionString);
        this.setInternalCodeSystemName(internalCodeSystemName);
        this.setInternalVersionString(internalVersionString);
        this.setScore(score);
        this.documentId = documentId;  
        
        this.entityIndexService = LexEvsServiceLocator.getInstance().getIndexServiceManager().getEntityIndexService();
        this.systemResourceService = LexEvsServiceLocator.getInstance().getSystemResourceService();
    }
    
    /**
     * Hydrate.
     * 
     * @throws Exception the exception
     */
    public void hydrate() throws Exception{
         
        Document doc = buildDocument();
 
        String codeField = SQLTableConstants.TBLCOL_ENTITYCODE;
        
        this.setCode(doc.get(codeField));
        
        this.setEntityDescription(
                doc.get(SQLTableConstants.TBLCOL_ENTITYDESCRIPTION));
        this.setUri(
                doc.get("codingSchemeId"));
        
        this.setNamespace(
                doc.get(SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE));
        this.setEntityTypes(
                doc.getValues("entityType"));
        isHydrated = true;       
    }
    
    protected Document buildDocument() throws Exception {
        String uri = this.systemResourceService.getUriForUserCodingSchemeName(internalCodeSystemName);
        return entityIndexService.getDocumentById(
                uri, internalVersionString, documentId);
    }

    /**
     * Checks if is hydrated.
     * 
     * @return true, if is hydrated
     */
    public boolean isHydrated() {
        return isHydrated;
    } 
    
    /**
     * Gets the document id.
     * 
     * @return the document id
     */
    public int getDocumentId() {
        return documentId;
    }

    /**
     * Sets the document id.
     * 
     * @param documentId the new document id
     */
    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.helpers.CodeToReturn#setUri(java.lang.String)
     */
    @Override
    public void setUri(String uri) {
        super.setUri(uri);
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.helpers.CodeToReturn#setVersion(java.lang.String)
     */
    @Override
    public void setVersion(String version) {
        super.setVersion(version);
    } 
 
    /**
     * Gets the internal code system name.
     * 
     * @return the internal code system name
     */
    public String getInternalCodeSystemName() {
        return internalCodeSystemName;
    }

    /**
     * Sets the internal code system name.
     * 
     * @param internalCodeSystemName the new internal code system name
     */
    public void setInternalCodeSystemName(String internalCodeSystemName) {
        this.internalCodeSystemName = internalCodeSystemName;
    }

    /**
     * Gets the internal version string.
     * 
     * @return the internal version string
     */
    public String getInternalVersionString() {
        return internalVersionString;
    }

    /**
     * Sets the internal version string.
     * 
     * @param internalVersionString the new internal version string
     */
    public void setInternalVersionString(String internalVersionString) {
        this.internalVersionString = internalVersionString;
    }
}
