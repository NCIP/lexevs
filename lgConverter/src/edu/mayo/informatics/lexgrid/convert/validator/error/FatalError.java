
package edu.mayo.informatics.lexgrid.convert.validator.error;

/**
 * The Class FatalError.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class FatalError extends AbstractError {

    /** The FATA l_ erro r_ code. */
    public static String FATAL_ERROR_CODE = "FATAL-ERROR";
    
    /** The FATA l_ erro r_ description. */
    public static String FATAL_ERROR_DESCRIPTION = "A Fatal Error has occured. This is not recoverable.";
    
    /**
     * Instantiates a new fatal error.
     * 
     * @param errorObject the error object
     * @param exception the exception
     */
    public FatalError(Object errorObject, Exception exception){
        super(FATAL_ERROR_CODE, errorObject, exception);
    }
    
    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.validator.error.AbstractError#getErrorObjectDescription()
     */
    @Override
    protected String getErrorObjectDescription() {
        return this.getErrorException().toString();
    }

    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.validator.error.LoadValidationError#getErrorDescription()
     */
    public String getErrorDescription() {
        return FATAL_ERROR_DESCRIPTION;
    }
}