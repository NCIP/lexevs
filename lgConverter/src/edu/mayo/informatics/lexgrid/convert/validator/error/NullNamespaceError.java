package edu.mayo.informatics.lexgrid.convert.validator.error;

import org.LexGrid.concepts.Entity;

public class NullNamespaceError extends AbstractError {

    public static String NULL_NAMESPACE_CODE = "NULL-NAMESPACE";
    
    private Entity entity;
    
    public NullNamespaceError(Entity entity) {
        this.entity = entity;
    }
    
    public String getErrorCode() {
        return NULL_NAMESPACE_CODE;
    }

    public Object getErrorObject() {
        return entity;
    }
    public String getErrorDescription() {
        return "A namespace is required to insert an Entity";
    }

    @Override
    protected String getErrorObjectDescription() {
        return "Code: " + this.entity.getEntityCode();
    }
}
