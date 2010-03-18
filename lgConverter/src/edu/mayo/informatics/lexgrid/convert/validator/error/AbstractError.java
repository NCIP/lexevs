package edu.mayo.informatics.lexgrid.convert.validator.error;

public abstract class AbstractError implements LoadValidationError {

    public String getErrorMessage() {
        StringBuffer sb = new StringBuffer();

        sb.append("\n============================================");
        sb.append("\nError Code: " + this.getErrorCode());
        sb.append("\nDescription: " + this.getErrorDescription() );
        sb.append("\n -- Caused By Object with Description: ");
        sb.append("\n --- " + this.getErrorObjectDescription());
        sb.append("\n============================================");
        
        return sb.toString();
    }
    
    protected abstract String getErrorObjectDescription();
}
