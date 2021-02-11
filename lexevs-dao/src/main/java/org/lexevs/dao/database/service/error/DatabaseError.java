
package org.lexevs.dao.database.service.error;

import java.util.Date;

/**
 * The Interface DatabaseError.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface DatabaseError {

	/** The UNKNOW_ERROR_CODE Constant. */
	public static String UNKNOWN_ERROR_CODE = "UNKNOWN_ERROR";

	/**
	 * Gets the error time.
	 * 
	 * @return the error time
	 */
	public Date getErrorTime();
	
    /**
     * Gets the error object.
     * 
     * @return the error object
     */
    public Object getErrorObject();
    
    /**
     * Gets the error exception.
     * 
     * @return the error exception
     */
    public Exception getErrorException();
    
    /**
     * Gets the unique error id.
     * 
     * @return the unique error id
     */
    public String getUniqueErrorId();
    
    /**
     * Gets the error description.
     * 
     * @return the error description
     */
    public String getErrorDescription();
    
    /**
     * Gets the error code.
     * 
     * @return the error code
     */
    public String getErrorCode();
    
    /**
     * Gets the error message.
     * 
     * @return the error message
     */
    public String getErrorMessage();
}