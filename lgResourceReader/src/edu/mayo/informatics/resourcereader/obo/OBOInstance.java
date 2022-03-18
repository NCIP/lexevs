
package edu.mayo.informatics.resourcereader.obo;

import java.util.Vector;

import org.LexGrid.LexBIG.Utility.logging.CachingMessageDirectorIF;

import edu.mayo.informatics.resourcereader.core.StringUtils;

/**
 * This class stores the OBO Instance information
 * 
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class OBOInstance extends OBOEntity {
    public String instanceOfTermId = null;
    public String instanceOfTermPrefix = null;
    public Vector<String> properties = new Vector<String>();

    public OBOInstance(CachingMessageDirectorIF rLogger) {
        super(rLogger);
    }

    public boolean isReady() {
        return ((!StringUtils.isNull(id)) && (!StringUtils.isNull(name)) && (!StringUtils.isNull(instanceOfTermId)));
    }

    public void addProperty(String str) {
        if (!StringUtils.isNull(str))
            properties.add(str);
    }

    public String printIt() {
        return "Instance:{" + "prefix: " + prefix + ", " + "id: " + id + ", " + "altIds: [" + altIds.toString() + "], "
                + "name: " + name + ", " + "namespace: " + namespace + ", " + "Anonymous: " + isAnonymous + ", "
                + "isObsolete: " + isObsolete + ", " + "Instanceof(prefix:id): (" + instanceOfTermPrefix + ", "
                + instanceOfTermId + "), " + "comment: " + comment + ", " + "Synonyms: [" + synonyms.toString() + "], "
                + "dbXref: [" + dbXrefs.toString() + "], " + "replacedBy: " + replacedBy + ", " + "consider: "
                + consider + "} ";

    }

    public String toString() {
        return "Instance:{" + "prefix: " + prefix + ", " + "id: " + id + ", " + "name: " + name + ", " + "namespace: "
                + namespace + ", " + "Instanceof(prefix:id): (" + instanceOfTermPrefix + ", " + instanceOfTermId
                + "), " + "Anonymous: " + isAnonymous + ", " + "isObsolete: " + isObsolete + "}\n ";

    }
}