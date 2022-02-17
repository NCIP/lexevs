
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