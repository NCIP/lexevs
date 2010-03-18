package edu.mayo.informatics.lexgrid.convert.validator;

import java.util.List;

import edu.mayo.informatics.lexgrid.convert.validator.error.LoadValidationError;

public interface Validator {

    public boolean isValidClassForValidator(Class<?> clazz);
    
    public List<LoadValidationError> validate(Object obj);
}
