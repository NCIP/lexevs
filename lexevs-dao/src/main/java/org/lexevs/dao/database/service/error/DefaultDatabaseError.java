
package org.lexevs.dao.database.service.error;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.UUID;

/**
 * The Class DefaultDatabaseError.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DefaultDatabaseError implements DatabaseError {

	/** The error object. */
	private Object errorObject;
	
	/** The error exception. */
	private Exception errorException;
	
	/** The unique error id. */
	private String uniqueErrorId = UUID.randomUUID().toString();
	
	/** The error time. */
	private Date errorTime = new Date();
	
	/** The error code. */
	private String errorCode;
	
	/** The error description. */
	private String errorDescription;
    
	/** The debug. */
	private boolean debug = false;

	/**
	 * Instantiates a new default database error.
	 * 
	 * @param errorObject the error object
	 * @param errorException the error exception
	 * @param errorCode the error code
	 */
	public DefaultDatabaseError(String errorCode, Object errorObject, Exception errorException) {
		this(errorCode, errorObject, "An " + errorCode + " + Error Occured.", errorException);
	}
	
	/**
	 * Instantiates a new default database error.
	 * 
	 * @param errorCode the error code
	 * @param errorObject the error object
	 * @param errorDescription the error description
	 * @param errorException the error exception
	 */
	public DefaultDatabaseError(String errorCode, Object errorObject, String errorDescription, Exception errorException) {
		super();
		this.errorObject = errorObject;
		this.errorException = errorException;
		this.errorCode = errorCode;
	}
	
    /* (non-Javadoc)
     * @see org.lexevs.dao.database.service.error.DatabaseError#getErrorMessage()
     */
    public String getErrorMessage() {
        StringBuffer sb = new StringBuffer();

        sb.append("\n             *ERROR REPORT*");
        sb.append("\n");
        sb.append("\nError Code: " + this.getErrorCode());
        sb.append("\nUnique Error Id: " + this.getUniqueErrorId());
        sb.append("\nDescription: " + getErrorDescription() );
        sb.append("\n -- Caused By Object with Description: ");
        sb.append("\n --- " + getErrorObjectDescription());
        sb.append("\nException Message (if any):");
        sb.append("\n");
        if(getErrorException() != null) {
            sb.append(getErrorException().getMessage());
            if(debug) {
                sb.append("\n");
                sb.append("\n");
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                getErrorException().printStackTrace(pw);
                pw.flush();
                sw.flush();
                sb.append(sw.getBuffer());
                pw.close();
                try {
                    sw.close();
                } catch (IOException e) {
                    //
                }
            }
        }
        
        return sb.toString();
    }
    
    /**
     * Gets the error object description.
     * 
     * @return the error object description
     */
    protected String getErrorObjectDescription() {
    	return this.getErrorObject().toString();
    }
    
    /* (non-Javadoc)
     * @see org.lexevs.dao.database.service.error.DatabaseError#getErrorDescription()
     */
    public String getErrorDescription() {
    	return this.errorDescription;
    }
	
	/**
	 * Gets the unique error id.
	 * 
	 * @return the unique error id
	 */
	public String getUniqueErrorId() {
		return uniqueErrorId;
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.error.DatabaseError#getErrorObject()
	 */
	public Object getErrorObject() {
		return errorObject;
	}
	
	/**
	 * Sets the error object.
	 * 
	 * @param errorObject the new error object
	 */
	public void setErrorObject(Object errorObject) {
		this.errorObject = errorObject;
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.error.DatabaseError#getErrorException()
	 */
	public Exception getErrorException() {
		return errorException;
	}
	
	/**
	 * Sets the error exception.
	 * 
	 * @param errorException the new error exception
	 */
	public void setErrorException(Exception errorException) {
		this.errorException = errorException;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.error.DatabaseError#getErrorTime()
	 */
	@Override
	public Date getErrorTime() {
		return errorTime;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.error.DatabaseError#getErrorCode()
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * Checks if is debug.
	 * 
	 * @return true, if is debug
	 */
	public boolean isDebug() {
		return debug;
	}

	/**
	 * Sets the debug.
	 * 
	 * @param debug the new debug
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
}