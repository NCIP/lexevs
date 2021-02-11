
package org.LexGrid.LexBIG.Exceptions;

/**
 * The exception to throw when invocation of a LexBIG service fails due to an
 * unexpected problem captured and logged for administrative action.  The logID
 * will contain information that the LexBIG admins can use to track down the
 * details of the internal error.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 * @created 27-Jan-2006 9:19:38 PM
 */
public class LBInvocationException extends LBException
{
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