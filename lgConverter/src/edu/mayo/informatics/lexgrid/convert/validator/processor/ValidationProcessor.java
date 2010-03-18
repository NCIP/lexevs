package edu.mayo.informatics.lexgrid.convert.validator.processor;

import java.util.List;

import edu.mayo.informatics.lexgrid.convert.validator.Validator;
import edu.mayo.informatics.lexgrid.convert.validator.error.LoadValidationError;

public interface ValidationProcessor<T> {

    public List<LoadValidationError> validate(T object);
    
    public void addValidator(Validator validator);
}
