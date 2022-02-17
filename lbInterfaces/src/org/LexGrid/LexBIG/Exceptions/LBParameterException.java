
package org.LexGrid.LexBIG.Exceptions;

/**
 * The exception to throw when invalid input is provided to a LexBIG service.
 */
public class LBParameterException extends LBException
{
	private static final long serialVersionUID = 5108177540505924442L;
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