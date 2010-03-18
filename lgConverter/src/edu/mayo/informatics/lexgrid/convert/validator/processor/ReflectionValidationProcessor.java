package edu.mayo.informatics.lexgrid.convert.validator.processor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.util.ClassUtils;

import edu.mayo.informatics.lexgrid.convert.validator.Validator;
import edu.mayo.informatics.lexgrid.convert.validator.error.LoadValidationError;

public class ReflectionValidationProcessor implements ValidationProcessor<Object>{
    
    private List<Validator> validators = new ArrayList<Validator>();
    
    public List<LoadValidationError> validate(final Object obj) {
        List<LoadValidationError> errorList = new ArrayList<LoadValidationError>();
        
        for(Validator validator : validators) {
            if(validator.isValidClassForValidator(obj.getClass())) {
                errorList.addAll( validator.validate(obj) );
            }
        }

        for(Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if(! ClassUtils.isPrimitiveOrWrapper(field.getType()) &&
                    ! ClassUtils.isPrimitiveArray(field.getType()) &&
                    ! ClassUtils.isPrimitiveWrapperArray(field.getType())){
                Object fieldVal;
                try {
                    fieldVal = field.get(obj);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } 
                if(fieldVal != null) {
                    if(fieldVal instanceof Collection<?>) {
                        for(Object collectionObj : (Collection<?>)fieldVal) {
                            errorList.addAll( validate(collectionObj) );
                        }
                    } else {
                        errorList.addAll( validate(fieldVal) );
                    }
                }
            }
        }
        return errorList;
    }
    
    public void addValidator(Validator validator) {
        validators.add(validator);
    }
}
