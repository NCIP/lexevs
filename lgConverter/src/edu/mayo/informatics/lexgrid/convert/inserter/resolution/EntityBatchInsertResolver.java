
package edu.mayo.informatics.lexgrid.convert.inserter.resolution;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.concepts.Entity;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.locator.LexEvsServiceLocator;

import edu.mayo.informatics.lexgrid.convert.inserter.error.EntityBatchInsertError;
import edu.mayo.informatics.lexgrid.convert.inserter.error.EntityBatchInsertError.EntityBatchInsertErrorItem;
import edu.mayo.informatics.lexgrid.convert.validator.resolution.AbstractResolver;
import edu.mayo.informatics.lexgrid.convert.validator.resolution.ErrorResolutionReport.ResolutionStatus;

public class EntityBatchInsertResolver extends AbstractResolver<EntityBatchInsertErrorItem>{

    public List<EntityExceptionPair> errors = new ArrayList<EntityExceptionPair>();
    
    @Override
    protected List<String> doGetValidErrorCodes() {
        return DaoUtility.createNonTypedList(EntityBatchInsertError.ENTITY_BATCH_INSERT_ERROR_CODE);
    }

    @Override
    public ResolutionStatus doResolveError(EntityBatchInsertErrorItem errorObject) {
    
        List<Entity> entities = errorObject.getBatch();
        for(Entity entity : entities) {
            try {
                LexEvsServiceLocator.getInstance().
                    getDatabaseServiceManager().
                        getEntityService().
                            insertEntity(errorObject.getCodingSchemeUri(), errorObject.getCodingSchemeVersion(), entity);
            } catch (Exception e) {
                errors.add(new EntityExceptionPair(entity, e));
            }            
        }
        
        if(this.errors.size() > 0 && this.errors.size() < errorObject.getBatch().size()) {
            return ResolutionStatus.PARTIALLY_RESOLVED;
        } 
        if(this.errors.size() == 0) {
            return ResolutionStatus.RESOLVED;
        }
        return ResolutionStatus.RESOLUTION_FAILED;
    }

    @Override
    public String getResolutionDetails() {
        StringBuffer sb = new StringBuffer();
        sb.append("\nAttempted to insert Entities individually.");
        sb.append("\n");
        if(this.errors.size() > 0) {
            sb.append("\nRemaining Entities in Error:");
            for(EntityExceptionPair pair : this.errors) {
                sb.append("\n - Entity Code: " + pair.getEntity().getEntityCode());
                sb.append("\n - Exception Message: ");
                sb.append("\n" + pair.getException().getCause().getMessage());
                sb.append("\n");
                sb.append("\n");
            }
        } else {
            sb.append("\nAll Entities individually inserted successfully.");
        }
        return sb.toString();
    }
    
    public static class EntityExceptionPair {
        private Entity entity;
        private Exception exception;
        public EntityExceptionPair(Entity entity, Exception exception) {
            super();
            this.entity = entity;
            this.exception = exception;
        }
        public Entity getEntity() {
            return entity;
        }
        public void setEntity(Entity entity) {
            this.entity = entity;
        }
        public Exception getException() {
            return exception;
        }
        public void setException(Exception exception) {
            this.exception = exception;
        } 
    }
}