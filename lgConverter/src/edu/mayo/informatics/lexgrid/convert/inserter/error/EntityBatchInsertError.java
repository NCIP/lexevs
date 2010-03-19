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
package edu.mayo.informatics.lexgrid.convert.inserter.error;

import java.util.List;

import org.LexGrid.concepts.Entity;

import edu.mayo.informatics.lexgrid.convert.validator.error.AbstractError;

/**
 * The Class EntityBatchInsertError.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class EntityBatchInsertError extends AbstractError{
    
    /** The ENTIT y_ batc h_ inser t_ erro r_ code. */
    public static String ENTITY_BATCH_INSERT_ERROR_CODE = "ENTITY-BATCH-INSERT-ERROR";
    
    /** The DESCRIPTION. */
    public static String DESCRIPTION = "An error has occured while inserting a batch of Entities.";
    
    /**
     * Instantiates a new entity batch insert error.
     * 
     * @param errorItem the error item
     */
    public EntityBatchInsertError(EntityBatchInsertErrorItem errorItem){
        super(errorItem);
    }
    
    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.validator.error.AbstractError#getErrorObjectDescription()
     */
    @Override
    protected String getErrorObjectDescription() {
        StringBuffer sb = new StringBuffer();
        sb.append("CodingScheme Id: " + ((EntityBatchInsertErrorItem)this.getErrorObject()).getCodingSchemeId());
        sb.append("Batch Size: " + ((EntityBatchInsertErrorItem)this.getErrorObject()).getBatch().size());
        
        return sb.toString();
    }

    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.validator.error.LoadValidationError#getErrorCode()
     */
    public String getErrorCode() {
        return ENTITY_BATCH_INSERT_ERROR_CODE;
    }

    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.validator.error.LoadValidationError#getErrorDescription()
     */
    public String getErrorDescription() {
        return DESCRIPTION;
    }

    /**
     * The Class EntityBatchInsertErrorItem.
     * 
     * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
     */
    public static class EntityBatchInsertErrorItem {
        
        /** The coding scheme id. */
        public String codingSchemeId;
        
        /** The batch. */
        public List<Entity> batch;
        
        /**
         * Instantiates a new entity batch insert error item.
         * 
         * @param codingSchemeId the coding scheme id
         * @param batch the batch
         */
        public EntityBatchInsertErrorItem(String codingSchemeId, List<Entity> batch) {
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
        public List<Entity> getBatch() {
            return batch;
        }
        
        /**
         * Sets the batch.
         * 
         * @param batch the new batch
         */
        public void setBatch(List<Entity> batch) {
            this.batch = batch;
        }  
    }
}
