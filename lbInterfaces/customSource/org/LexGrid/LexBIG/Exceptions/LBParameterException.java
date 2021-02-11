
package org.LexGrid.LexBIG.Exceptions;

/**
 * The exception to throw when invalid input is provided to a LexBIG service.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 * @created 27-Jan-2006 9:19:38 PM
 */
public class LBParameterException extends LBException
{
    String parameterName, parameterValue;

    public LBParameterException(String message, String parameterName, String parameterValue)
    {
        super(message +  " Parameter: " + parameterName + " Value: " + parameterValue);
        this.parameterName = parameterName;
        this.parameterValue = parameterValue;
    }

    public LBParameterException(String message, String parameterName)
    {
        super(message +  " Parameter: " + parameterName);
        this.parameterName = parameterName;
    }
    
    public LBParameterException(String message)
    {
        super(message);
    }

    public String getParameterName()
    {
        return this.parameterName;
    }

    public String getParameterValue()
    {
        return this.parameterValue;
    }
}