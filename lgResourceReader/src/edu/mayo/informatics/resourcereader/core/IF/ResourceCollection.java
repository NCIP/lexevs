
package edu.mayo.informatics.resourcereader.core.IF;

import java.util.Collection;

/**
 * Resource Collection Interface
 * 
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public interface ResourceCollection {
    public void init();

    public void addMember(ResourceEntity entity) throws ResourceException;

    public ResourceEntity getMemberById(String id) throws ResourceException;

    public ResourceEntity getMemberByName(String name) throws ResourceException;

    public ResourceEntity getMemberByProperty(Object obj) throws ResourceException;

    public Collection getAllMembers() throws ResourceException;

    public long getMembersCount();
}