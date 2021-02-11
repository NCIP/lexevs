
package edu.mayo.informatics.resourcereader.obo;

import java.util.Collection;

import org.LexGrid.LexBIG.Utility.logging.CachingMessageDirectorIF;

import edu.mayo.informatics.resourcereader.core.IF.ResourceCollection;
import edu.mayo.informatics.resourcereader.core.IF.ResourceEntity;
import edu.mayo.informatics.resourcereader.core.IF.ResourceException;
import edu.mayo.informatics.resourcereader.obo.exceptions.OBOMethodNotImplementedException;

/**
 * A OBO implementation of a ResourceCollection
 * 
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class OBOCollection extends OBO implements ResourceCollection {
    public OBOCollection(CachingMessageDirectorIF rLogger) {
        super(rLogger);
    }

    public void init() {
    }

    public void addMember(ResourceEntity entity) throws ResourceException {
        throw new OBOMethodNotImplementedException("addMember");
    }

    public ResourceEntity getMemberById(String id) throws ResourceException {
        throw new OBOMethodNotImplementedException("getMemberById");
    }

    public ResourceEntity getMemberByName(String name) throws ResourceException {
        throw new OBOMethodNotImplementedException("getMemberByName");
    }

    public ResourceEntity getMemberByProperty(Object obj) throws ResourceException {
        throw new OBOMethodNotImplementedException("getMemberByProperty");
    }

    public Collection getAllMembers() throws ResourceException {
        throw new OBOMethodNotImplementedException("getAllMembers");
    }

    public long getMembersCount() {
        return 0;
    }
}