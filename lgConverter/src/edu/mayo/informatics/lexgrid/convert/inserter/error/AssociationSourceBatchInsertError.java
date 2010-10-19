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
package edu.mayo.informatics.lexgrid.convert.inserter.error;

import java.util.List;

import org.lexevs.dao.database.access.association.batch.AssociationSourceBatchInsertItem;

import edu.mayo.informatics.lexgrid.convert.validator.error.AbstractError;

/**
 * The Class AssociationSourceBatchInsertError.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class AssociationSourceBatchInsertError extends AbstractError{
    
    /** The ASSOCIATIO n_ sourc e_ batc h_ inser t_ erro r_ code. */
    public static String ASSOCIATION_SOURCE_BATCH_INSERT_ERROR_CODE = "ASSOCIATION-SOURCE-BATCH-INSERT-ERROR";
    
    /** The DESCRIPTION. */
    public static String DESCRIPTION = "An error has occured while inserting a batch of Association Sources.";
    
    /**
     * Instantiates a new association source batch insert error.
     * 
     * @param errorItem the error item
     */
    public AssociationSourceBatchInsertError(AssociationSourceBatchInsertErrorItem errorItem){
        super(errorItem);
    }

    public AssociationSourceBatchInsertError(Object errorObject, Exception errorException) {
        super(ASSOCIATION_SOURCE_BATCH_INSERT_ERROR_CODE, errorObject, errorException);
    }

    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.validator.error.AbstractError#getErrorObjectDescription()
     */
    @Override
    protected String getErrorObjectDescription() {
        StringBuffer sb = new StringBuffer();
        sb.append("CodingScheme Id: " + ((AssociationSourceBatchInsertErrorItem)this.getErrorObject()).getCodingSchemeId());
        sb.append("Batch Size: " + ((AssociationSourceBatchInsertErrorItem)this.getErrorObject()).getBatch().size());
        
        return sb.toString();
    }

    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.validator.error.LoadValidationError#getErrorCode()
     */
    public String getErrorCode() {
        return ASSOCIATION_SOURCE_BATCH_INSERT_ERROR_CODE;
    }

    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.validator.error.LoadValidationError#getErrorDescription()
     */
    public String getErrorDescription() {
        return DESCRIPTION;
    }

    /**
     * The Class AssociationSourceInsertErrorItem.
     * 
     * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
     */
    public static class AssociationSourceBatchInsertErrorItem {
        
        /** The coding scheme id. */
        public String codingSchemeId;
        
        /** The batch. */
        public List<AssociationSourceBatchInsertItem> batch;
        
        /**
         * Instantiates a new association source insert error item.
         * 
         * @param codingSchemeId the coding scheme id
         * @param batch the batch
         */
        public AssociationSourceBatchInsertErrorItem(String codingSchemeId, List<AssociationSourceBatchInsertItem> batch) {
            super();
            this.codingSchemeId = codingSchemeId;
            this.batch = batch;
        }
        
        /**
         * Gets the coding scheme id.
         * 
         * @return the coding scheme id
         */
        public String getCodingSchemeId() {
            return codingSchemeId;
        }
        
        /**
         * Sets the coding scheme id.
         * 
         * @param codingSchemeId the new coding scheme id
         */
        public void setCodingSchemeId(String codingSchemeId) {
            this.codingSchemeId = codingSchemeId;
        }
        
        /**
         * Gets the batch.
         * 
         * @return the batch
         */
        public List<AssociationSourceBatchInsertItem> getBatch() {
            return batch;
        }
        
        /**
         * Sets the batch.
         * 
         * @param batch the new batch
         */
        public void setBatch(List<AssociationSourceBatchInsertItem> batch) {
            this.batch = batch;
        }  
    }
}