package edu.mayo.informatics.lexgrid.convert.validator;

import java.util.List;

import org.LexGrid.concepts.Entity;
import org.apache.commons.lang.StringUtils;

import edu.mayo.informatics.lexgrid.convert.validator.error.LoadValidationError;
import edu.mayo.informatics.lexgrid.convert.validator.error.NullNamespaceError;

public class NullNamespaceValidator extends AbstractValidator<Entity> {

    public boolean isValidClassForValidator(Class<?> clazz) {
        return clazz.isAssignableFrom(Entity.class);
    }

    @Override
    public void doValidate(Entity object, List<LoadValidationError> errors) {
        String namespace = object.getEntityCodeNamespace();
        if(StringUtils.isBlank(namespace)) {
            errors.add(new NullNamespaceError(object));
        }
    }
}
