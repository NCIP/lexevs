
package edu.mayo.informatics.lexgrid.convert.validator;

import java.util.ArrayList;
import java.util.List;

import edu.mayo.informatics.lexgrid.convert.validator.error.LoadValidationError;

/**
 * The Class AbstractValidator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractValidator<T> implements Validator {

    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.validator.Validator#validate(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    public List<LoadValidationError> validate(Object obj) {
        if(! this.isValidClassForValidator(obj.getClass())) {
            throw new RuntimeException("Validator cannot be applied to: " + obj.getClass().getName());
        }
        List<LoadValidationError> errors = new ArrayList<LoadValidationError>();
        
        doValidate( (T)obj, errors);
        
        return errors;
    }
    
    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.validator.Validator#isValidClassForValidator(java.lang.Class)
     */
    public boolean isValidClassForValidator(Class<?> clazz) {
        for(Class<?> validClazz : doGetValidClasses()) {
            if(validClazz.isAssignableFrom(clazz)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Do get valid classes.
     * 
     * @return the list< class<?>>
     */
    protected abstract List<Class<?>> doGetValidClasses();
    
    /**
     * Do validate.
     * 
     * @param object the object
     * @param errors the errors
     */
    public abstract void doValidate(T object, List<LoadValidationError> errors);

}