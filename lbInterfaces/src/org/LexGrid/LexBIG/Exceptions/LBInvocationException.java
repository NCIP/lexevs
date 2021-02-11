
package org.LexGrid.LexBIG.Exceptions;

/**
 * The exception to throw when invocation of a LexBIG service fails due to an
 * unexpected problem captured and logged for administrative action.  The logID
 * will contain information that the LexBIG admins can use to track down the
 * details of the internal error.
 */
public class LBInvocationException extends LBException
{
	private static final long serialVersionUID = 3030886891325779414L;
	private String logId;

    public LBInvocationException(String message, String logId)
    {
        super(message + " - LogID Reference " + logId);
        this.logId = logId;
    }

    public String getLogId()
    {
        return this.logId;
    }

}