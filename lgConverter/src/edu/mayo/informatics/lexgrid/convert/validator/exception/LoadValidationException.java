
package edu.mayo.informatics.lexgrid.convert.validator.exception;

import edu.mayo.informatics.lexgrid.convert.validator.error.LoadValidationError;

public class LoadValidationException extends Exception {

    private static final long serialVersionUID = 341075048952234243L;
    private LoadValidationError loadValidationError;
    
    public LoadValidationException(LoadValidationError loadValidationError){
        super();
        this.loadValidationError = loadValidationError;
    }

    public LoadValidationError getLoadValidationError() {
        return loadValidationError;
    }
}