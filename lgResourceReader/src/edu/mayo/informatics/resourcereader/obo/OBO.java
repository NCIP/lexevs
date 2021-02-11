
package edu.mayo.informatics.resourcereader.obo;

import org.LexGrid.LexBIG.Utility.logging.CachingMessageDirectorIF;
import org.lexevs.logging.messaging.impl.CachingMessageDirectorImpl;
import org.lexevs.logging.messaging.impl.CommandLineMessageDirector;

import edu.mayo.informatics.resourcereader.core.impl.Resource;

/**
 * Class OBO
 * 
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class OBO extends Resource {
    protected CachingMessageDirectorIF logger = null;

    public OBO() {
        setLogger(new CachingMessageDirectorImpl(new CommandLineMessageDirector("OBOReaderLoader")));
    }

    public OBO(CachingMessageDirectorIF logger) {
        setLogger(logger);
        this.logger = logger;
    }
}