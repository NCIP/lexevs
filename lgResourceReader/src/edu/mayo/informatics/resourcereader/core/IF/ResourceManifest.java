
package edu.mayo.informatics.resourcereader.core.IF;

import java.net.URI;

/**
 * ResourceManifest interface
 * 
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public interface ResourceManifest {
    public void setManifestLocation(URI resource) throws ResourceException;

    public boolean isValidManifest() throws ResourceException;
}