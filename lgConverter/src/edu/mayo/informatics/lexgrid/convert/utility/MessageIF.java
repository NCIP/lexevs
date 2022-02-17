
package edu.mayo.informatics.lexgrid.convert.utility;

/**
 * <pre>
 * Title:        MessageIF.java
 * Description:  An interface to enable the SQL loading classes to pass messages back
 * about non-fatal issues.
 * </pre>
 *
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version 1.0 - cvs $Revision: 1.1 $ checked in on $Date: 2005/05/24 15:45:54 $
 */
public interface MessageIF
{
    public void message(String message);
    
    public void message(String message, Exception e);
}