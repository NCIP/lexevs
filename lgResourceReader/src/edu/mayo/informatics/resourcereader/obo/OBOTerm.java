
package edu.mayo.informatics.resourcereader.obo;

import java.util.Hashtable;
import java.util.Vector;

import org.LexGrid.LexBIG.Utility.logging.CachingMessageDirectorIF;

import edu.mayo.informatics.resourcereader.core.StringUtils;

/**
 * The class stores information about a OBO Term
 * 
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class OBOTerm extends OBOEntity {
    public Vector<String> subset = new Vector<String>();

    private Hashtable<String, Vector<String>> relationships = null;

    public OBOTerm(CachingMessageDirectorIF rLogger) {
        super(rLogger);
    }

    public boolean isReady() {
        return ((!StringUtils.isNull(id)) && (!StringUtils.isNull(name)));
    }

    public Hashtable<String, Vector<String>> getRelationships() {
        return relationships;
    }

    public void addRelationship(String relation, String target) {
        // TODO: Process Trailing modifiers like cardinality etc.
        try {
            if ((StringUtils.isNull(relation)) || (StringUtils.isNull(target)))
                return;

            if (relationships == null)
                relationships = new Hashtable<String, Vector<String>>();

            if (!StringUtils.isNull(target)) {
                Vector<String> existingTargets = relationships.get(relation);
                ;

                if (existingTargets == null)
                    existingTargets = new Vector<String>();

                if (!existingTargets.contains(target)) {
                    existingTargets.add(target);
                } else {
                    logger.warn("Duplicate target found for term[" + id + "]: '" + relation + "' with value=" + target);
                }
                relationships.put(relation, existingTargets);
            }
        } catch (Exception e) {
            if (logger != null) {
                logger.warn("Failed to add relationship for term[" + id + "," + name + "]: '" + relation
                        + "' with value=" + target);
            }
        }
    }

    public String toString() {
        return "Term:{" + "prefix: " + prefix + ", " + "id: " + id + ", " + "altIds: " + altIds.toString() + ", "
                + "name: " + name + ", " + "namespace: " + namespace + ", " + "Anonymous: " + isAnonymous + ", "
                + "isObsolete: " + isObsolete + ", " + "definition: " + definition + ", " + "definitionSrcs: ["
                + definitionSources.toString() + "], " + "comment: " + comment + ", " + "Synonyms: "
                + synonyms.toString() + ", " + "dbXref: " + dbXrefs.toString() + ", " + "subset: " + subset.toString()
                + ", " + "replacedBy: " + replacedBy + ", " + "consider: " + consider + "} \n";

    }

    public String printIt() {
        return "Term:{" + "prefix: " + prefix + ", " + "id: " + id + ", " + "name: " + name + ", " + "namespace: "
                + namespace + ", " + "Anonymous: " + isAnonymous + ", " + "isObsolete: " + isObsolete + "} \n";

    }

    public Vector<String> getSubset() {
        return subset;
    }

    public void setSubset(Vector<String> subset) {
        this.subset = subset;
    }
}