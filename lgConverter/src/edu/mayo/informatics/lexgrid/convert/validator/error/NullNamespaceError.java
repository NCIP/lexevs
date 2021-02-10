
package edu.mayo.informatics.lexgrid.convert.validator.error;

import org.LexGrid.concepts.Entity;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;

/**
 * The Class NullNamespaceError.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class NullNamespaceError extends AbstractError {

    /** The NUL l_ namespac e_ code. */
    public static String NULL_NAMESPACE_CODE = "NULL-NAMESPACE";
    
    /**
     * Instantiates a new null namespace error.
     * 
     * @param nullNamespaceObject the null namespace object
     */
    public NullNamespaceError(Object nullNamespaceObject) {
        super(nullNamespaceObject);
    }
    
    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.validator.error.LoadValidationError#getErrorCode()
     */
    public String getErrorCode() {
        return NULL_NAMESPACE_CODE;
    }

    
    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.validator.error.LoadValidationError#getErrorDescription()
     */
    public String getErrorDescription() {
        return "A namespace is required.";
    }

    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.validator.error.AbstractError#getErrorObjectDescription()
     */
    @Override
    protected String getErrorObjectDescription() {
        Object nullNamespaceObject = this.getErrorObject();
        if(nullNamespaceObject instanceof Entity) {
            return "Code: " + ((Entity)nullNamespaceObject).getEntityCode();
        } else if(nullNamespaceObject instanceof AssociationSource) {
            return "Code: " + ((AssociationSource)nullNamespaceObject).getSourceEntityCode();
        } else if(nullNamespaceObject instanceof AssociationTarget) {
            return "Code: " +  ((AssociationTarget)nullNamespaceObject).getTargetEntityCode();
        } else {
            return nullNamespaceObject.toString();
        }
    }
}