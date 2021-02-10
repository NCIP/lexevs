
package edu.mayo.informatics.resourcereader.core.impl;

import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.lexevs.logging.messaging.impl.CachingMessageDirectorImpl;
import org.lexevs.logging.messaging.impl.CommandLineMessageDirector;

import edu.mayo.informatics.resourcereader.core.IF.TopResource;

/**
 * The class provides for a single logger to log messages
 * 
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
@SuppressWarnings("static-access")
public class Resource implements TopResource {
    protected static LgMessageDirectorIF logger = new CachingMessageDirectorImpl(new CommandLineMessageDirector(
            "LexGridResourceReaderLog"));

    public static LgMessageDirectorIF getLogger() {
        return logger;
    }

    protected void setLogger(LgMessageDirectorIF logger) {
        if (logger != null)
            this.logger = logger;
    }
}