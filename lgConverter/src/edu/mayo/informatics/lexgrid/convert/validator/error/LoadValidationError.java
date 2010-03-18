package edu.mayo.informatics.lexgrid.convert.validator.error;

public interface LoadValidationError {
    
    public String getErrorMessage();
    
    public String getErrorCode();
    
    public String getErrorDescription();
    
    public Object getErrorObject();
}
