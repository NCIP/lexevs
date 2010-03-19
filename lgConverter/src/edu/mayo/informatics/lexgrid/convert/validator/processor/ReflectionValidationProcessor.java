package edu.mayo.informatics.lexgrid.convert.validator.processor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
import org.springframework.util.ClassUtils;

import edu.mayo.informatics.lexgrid.convert.validator.NullNamespaceValidator;
import edu.mayo.informatics.lexgrid.convert.validator.Validator;
import edu.mayo.informatics.lexgrid.convert.validator.error.LoadValidationError;
import edu.mayo.informatics.lexgrid.convert.validator.resolution.ErrorResolutionReport;
import edu.mayo.informatics.lexgrid.convert.validator.resolution.NullNamespaceResolver;

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
    
     public static void main(String[] args) {
        CodingScheme cs = new CodingScheme();
        cs.setCodingSchemeName("csName");
        cs.setEntities(new Entities());
        cs.getEntities().addEntity(new Entity());
        
        ValidationProcessor<Object> processor = new ReflectionValidationProcessor();
        processor.addValidator(new NullNamespaceValidator());
        List<LoadValidationError> errors = processor.validate(cs);
        System.out.println(errors.size());
        
        DefaultResolverProcessor resolverProcessor = new DefaultResolverProcessor();
        resolverProcessor.addResolver(new NullNamespaceResolver(cs));
        
        List<ErrorResolutionReport> reports = resolverProcessor.resolve(errors);
        for(ErrorResolutionReport report : reports) {
            System.out.println(report);
        }
        
        
        System.out.println(cs.getEntities().getEntity(0).getEntityCodeNamespace());
        
        
    }
}
