
package edu.mayo.informatics.lexgrid.convert.validator.processor;

import java.util.List;

import edu.mayo.informatics.lexgrid.convert.validator.Validator;
import edu.mayo.informatics.lexgrid.convert.validator.error.LoadValidationError;

/**
 * The Interface ValidationProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface ValidationProcessor<T> {

    /**
     * Validate.
     * 
     * @param object the object
     * 
     * @return the list< load validation error>
     */
    public List<LoadValidationError> validate(T object);
    
    /**
     * Adds the validator.
     * 
     * @param validator the validator
     */
    public void addValidator(Validator validator);
}