
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
  
    public EntityBatchInsertError(Object errorObject, Exception errorException) {
        super(ENTITY_BATCH_INSERT_ERROR_CODE, errorObject, errorException);
    }

    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.validator.error.AbstractError#getErrorObjectDescription()
     */
    @Override
    protected String getErrorObjectDescription() {
        StringBuffer sb = new StringBuffer();
        sb.append("\n");
        sb.append("\nCodingScheme Id: " + ((EntityBatchInsertErrorItem)this.getErrorObject()).getCodingSchemeId());
        sb.append("\nBatch Size: " + ((EntityBatchInsertErrorItem)this.getErrorObject()).getBatch().size());
        sb.append("\n");
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
        
        public String codingSchemeUri;
        
        public String codingSchemeVersion;
        
        /**
         * Instantiates a new entity batch insert error item.
         * 
         * @param codingSchemeId the coding scheme id
         * @param batch the batch
         */
        public EntityBatchInsertErrorItem(String codingSchemeUri, String codingSchemeVersion, String codingSchemeId, List<Entity> batch) {
            super();
            this.codingSchemeId = codingSchemeId;
            this.batch = batch;
            this.codingSchemeUri = codingSchemeUri;
            this.codingSchemeVersion = codingSchemeVersion;
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

        public String getCodingSchemeUri() {
            return codingSchemeUri;
        }

        public void setCodingSchemeUri(String codingSchemeUri) {
            this.codingSchemeUri = codingSchemeUri;
        }

        public String getCodingSchemeVersion() {
            return codingSchemeVersion;
        }

        public void setCodingSchemeVersion(String codingSchemeVersion) {
            this.codingSchemeVersion = codingSchemeVersion;
        }  
    }
}