
package edu.mayo.informatics.lexgrid.convert.validator;

import java.util.List;

import edu.mayo.informatics.lexgrid.convert.validator.error.LoadValidationError;

/**
 * The Interface Validator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface Validator {

    /**
     * Checks if is valid class for validator.
     * 
     * @param clazz the clazz
     * 
     * @return true, if is valid class for validator
     */
    public boolean isValidClassForValidator(Class<?> clazz);
    
    /**
     * Validate.
     * 
     * @param obj the obj
     * 
     * @return the list< load validation error>
     */
    public List<LoadValidationError> validate(Object obj);
}