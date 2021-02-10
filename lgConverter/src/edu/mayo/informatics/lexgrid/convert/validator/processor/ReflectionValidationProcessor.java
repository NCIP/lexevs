
package edu.mayo.informatics.lexgrid.convert.validator.processor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.Relations;
import org.springframework.util.ClassUtils;

import edu.mayo.informatics.lexgrid.convert.validator.NullNamespaceValidator;
import edu.mayo.informatics.lexgrid.convert.validator.Validator;
import edu.mayo.informatics.lexgrid.convert.validator.error.LoadValidationError;
import edu.mayo.informatics.lexgrid.convert.validator.error.ResolvedLoadValidationError;
import edu.mayo.informatics.lexgrid.convert.validator.resolution.NullNamespaceResolver;

/**
 * The Class ReflectionValidationProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ReflectionValidationProcessor<T> implements ValidationProcessor<T>{
    
    /** The validators. */
    private List<Validator> validators = new ArrayList<Validator>();
    
    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.validator.processor.ValidationProcessor#validate(java.lang.Object)
     */
    public List<LoadValidationError> validate(final Object obj) {
        
        List<LoadValidationError> errorList = new ArrayList<LoadValidationError>();
        
        if(obj.getClass().isPrimitive() ||
                obj.getClass().isEnum()) {
            return errorList;
        }
        
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
    
    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.validator.processor.ValidationProcessor#addValidator(edu.mayo.informatics.lexgrid.convert.validator.Validator)
     */
    public void addValidator(Validator validator) {
        validators.add(validator);
    }
    
     /**
      * The main method.
      * 
      * @param args the arguments
      */
     public static void main(String[] args) {
        CodingScheme cs = new CodingScheme();
        cs.setCodingSchemeName("csName");
        cs.setEntities(new Entities());
        cs.getEntities().addEntity(new Entity());
        
        cs.addRelations(new Relations());
        cs.getRelations(0).addAssociationPredicate(new AssociationPredicate());
        cs.getRelations(0).getAssociationPredicate(0).addSource(new AssociationSource());
        
        ValidationProcessor<Object> processor = new ReflectionValidationProcessor();
        processor.addValidator(new NullNamespaceValidator());
        List<LoadValidationError> errors = processor.validate(cs);
        System.out.println(errors.size());
        
        DefaultResolverProcessor resolverProcessor = new DefaultResolverProcessor();
        resolverProcessor.addResolver(new NullNamespaceResolver(cs));
        
        List<ResolvedLoadValidationError> resolvedErrors = resolverProcessor.resolve(errors);
        for(ResolvedLoadValidationError error : resolvedErrors) {
            System.out.println(error);
        }
        
        
        System.out.println(cs.getEntities().getEntity(0).getEntityCodeNamespace());
        
        
    }
}