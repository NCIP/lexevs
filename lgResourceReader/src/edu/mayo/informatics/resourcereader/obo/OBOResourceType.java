
package edu.mayo.informatics.resourcereader.obo;

import edu.mayo.informatics.resourcereader.core.IF.ResourceType;

/**
 * OBO resource Type
 * 
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class OBOResourceType extends ResourceType {
    private static OBOResourceType self = null;

    public static ResourceType getResourceType() {
        if (self == null)
            self = new OBOResourceType();
        return self;
    }

    public OBOResourceType() {
        super("OBO");
    }
}