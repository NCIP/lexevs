
package edu.mayo.informatics.resourcereader.core.IF;

import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;

/**
 * The minimum interface that must be implemented by any ResourceReader
 * 
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public interface ResourceReader {
    public void initResourceManifest(ResourceManifest manifest, LgMessageDirectorIF logger);

    public ResourceHeader getResourceHeader(boolean createNew);

    public ResourceContents getContents(boolean isLazy, boolean createNew);
}