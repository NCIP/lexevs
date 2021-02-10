
package edu.mayo.informatics.resourcereader.obo;

import java.util.Collection;

import org.LexGrid.LexBIG.Utility.logging.CachingMessageDirectorIF;

import edu.mayo.informatics.resourcereader.core.IF.ResourceContents;
import edu.mayo.informatics.resourcereader.core.IF.ResourceException;

/**
 * The class that is used to store the OBO Contents
 * 
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 * @author <A HREF="mailto:kanjamala.pradip@mayo.edu">Pradip Kanjamala</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class OBOContents extends OBO implements ResourceContents {
    private OBOAbbreviations sourceAbbreviations = null;
    private OBORelations relations = null;
    private OBOTerms terms = null;
    private OBOInstances instances = null;

    public OBOContents(CachingMessageDirectorIF rLogger) {
        super(rLogger);
        init(rLogger);
    }

    public void init(CachingMessageDirectorIF rLogger) {
        sourceAbbreviations = new OBOAbbreviations(rLogger);
        relations = new OBORelations(rLogger);
        terms = new OBOTerms(rLogger);
        instances = new OBOInstances(rLogger);
    }

    /**
     * The function returns a Collection of OBOTerm
     */
    public Collection<OBOTerm> getConcepts() {
        if (terms != null) {
            return terms.getAllMembers();
        }

        return null;
    }

    /**
     * The function returns a Collection of OBORelation
     */
    public Collection<OBORelation> getRelations() {
        if (relations != null)
            return relations.getAllMembers();

        return null;
    }

    /**
     * The function returns a Collection of OBOInstance
     */
    public Collection<OBOInstance> getInstances() {
        if (instances != null) {
            try {
                return instances.getAllMembers();
            } catch (ResourceException e) {

                logger.error("Error returning a Collection of OBOInstances", e);
            }
        }

        return null;
    }

    public OBOAbbreviations getOBOAbbreviations() {
        return sourceAbbreviations;
    }

    public void setOBOAbbreviations(OBOAbbreviations sourceAbbreviations) {
        this.sourceAbbreviations = sourceAbbreviations;
    }

    public OBORelations getOBORelations() {
        return relations;
    }

    public void setOBORelations(OBORelations relations) {
        this.relations = relations;
    }

    public OBOTerms getOBOTerms() {
        return terms;
    }

    public void setOBOTerms(OBOTerms terms) {
        this.terms = terms;
    }

    public OBOInstances getOBOInstances() {
        return instances;
    }

    public void setOBOInstances(OBOInstances instances) {
        this.instances = instances;
    }

    public String toString() {
        String temp = "Terms= \n" + terms.toString() + "\n" + "Relations=\n" + relations + "\n" + "Instances=\n"
                + instances + "\n" + "sourceAbbreviations=\n" + sourceAbbreviations;
        return temp;
    }
}