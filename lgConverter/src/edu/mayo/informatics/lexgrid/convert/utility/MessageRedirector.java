
package edu.mayo.informatics.lexgrid.convert.utility;

/**
 * This class implements the MessageIF as defined in the lexgrid sql plugin - it
 * allows me to catch messages from inside of the sql load process and redirect
 * them to the messaging utilities I use here.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: 7198 $ checked in on $Date: 2008-02-15
 *          18:08:36 +0000 (Fri, 15 Feb 2008) $
 */
public class MessageRedirector implements MessageIF {
    private LgMessageDirectorIF director_;

    public MessageRedirector(LgMessageDirectorIF director) {
        director_ = director;
    }

    public void message(String message, Exception e) {
        director_.warn(message, e);
    }

    public void message(String message) {
        director_.info(message);
    }

}