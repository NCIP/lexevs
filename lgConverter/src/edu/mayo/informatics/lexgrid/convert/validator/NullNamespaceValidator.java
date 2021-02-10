
package edu.mayo.informatics.lexgrid.convert.validator;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.concepts.Entity;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.apache.commons.lang.StringUtils;

import edu.mayo.informatics.lexgrid.convert.validator.error.LoadValidationError;
import edu.mayo.informatics.lexgrid.convert.validator.error.NullNamespaceError;

/**
 * The Class NullNamespaceValidator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class NullNamespaceValidator extends AbstractValidator<Object> {
    
    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.validator.AbstractValidator#doGetValidClasses()
     */
    @Override
    protected List<Class<?>> doGetValidClasses() {
        List<Class<?>> clazzes = new ArrayList<Class<?>>();
        clazzes.add(Entity.class);
        clazzes.add(AssociationSource.class);
        clazzes.add(AssociationTarget.class);
        
        return clazzes;
    }

    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.validator.AbstractValidator#doValidate(java.lang.Object, java.util.List)
     */
    @Override
    public void doValidate(Object object, List<LoadValidationError> errors) {
        String namespace = null;

        if(object instanceof Entity) {
            namespace =((Entity)object).getEntityCodeNamespace();
        } else if(object instanceof AssociationSource) {
            namespace = ((AssociationSource)object).getSourceEntityCodeNamespace();
        } else if(object instanceof AssociationTarget) {
            namespace = ((AssociationTarget)object).getTargetEntityCodeNamespace();
        } 
        
        if(StringUtils.isBlank(namespace)) {
            errors.add(new NullNamespaceError(object));
        }
    }
}