
package edu.mayo.informatics.resourcereader.obo;

import java.util.Collection;
import java.util.Hashtable;

import org.LexGrid.LexBIG.Utility.logging.CachingMessageDirectorIF;

import edu.mayo.informatics.resourcereader.core.StringUtils;
import edu.mayo.informatics.resourcereader.core.IF.ResourceEntity;
import edu.mayo.informatics.resourcereader.core.IF.ResourceException;

/**
 * This class stores the list of OBO Instances
 * 
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class OBOInstances extends OBOCollection {
    private Hashtable<String, OBOInstance> instancesByID = new Hashtable<String, OBOInstance>();

    public OBOInstances(CachingMessageDirectorIF rLogger) {
        super(rLogger);
    }

    public void addMember(ResourceEntity instancep) throws ResourceException {
        if ((instancep != null) && (instancep instanceof OBOInstance)) {
            OBOInstance instance = (OBOInstance) instancep;

            if (!StringUtils.isNull(instance.id)) {
                if (!instancesByID.containsKey(instance.id))
                    instancesByID.put(instance.id, instance);
            }
        }
    }

    public OBOInstance getMemberById(String id) throws ResourceException {
        return instancesByID.get(id);
    }

    public Collection<OBOInstance> getAllMembers() throws ResourceException {
        return instancesByID.values();
    }

    public long getMembersCount() {
        return instancesByID.size();
    }

    public String toString() {
        return instancesByID.toString();
    }
}