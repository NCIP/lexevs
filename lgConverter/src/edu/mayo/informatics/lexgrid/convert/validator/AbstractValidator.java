package edu.mayo.informatics.lexgrid.convert.validator;

import java.util.ArrayList;
import java.util.List;

import edu.mayo.informatics.lexgrid.convert.validator.error.LoadValidationError;

public abstract class AbstractValidator<T> implements Validator {

    @SuppressWarnings("unchecked")
    public List<LoadValidationError> validate(Object obj) {
        List<LoadValidationError> errors = new ArrayList<LoadValidationError>();
        
        doValidate( (T)obj, errors);
        
        return errors;
    }
    
    public abstract void doValidate(T object, List<LoadValidationError> errors);

}
