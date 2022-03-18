
package edu.mayo.informatics.lexgrid.convert.validator.error;

import edu.mayo.informatics.lexgrid.convert.validator.resolution.ErrorResolutionReport;

/**
 * The Interface ResolvedLoadValidationError.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface ResolvedLoadValidationError extends LoadValidationError {

    /**
     * Gets the error resolution report.
     * 
     * @return the error resolution report
     */
    public ErrorResolutionReport getErrorResolutionReport();
}