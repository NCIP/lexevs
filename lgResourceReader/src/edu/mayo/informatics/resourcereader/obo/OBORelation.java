
package edu.mayo.informatics.resourcereader.obo;

import java.util.Vector;

import org.LexGrid.LexBIG.Utility.logging.CachingMessageDirectorIF;

import edu.mayo.informatics.resourcereader.core.StringUtils;

/**
 * The class stores the OBO Relation information
 * 
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class OBORelation extends OBOEntity {
    private Boolean isTransitive = null;
    private Boolean isSymmetric = null;
    private Boolean isAntiSymmetric = null;
    private Boolean isReflexive = null;
    private Boolean isCyclic = null;
    public String inverseOf = null;
    public Vector<String> domain = new Vector<String>();
    public Vector<String> range = new Vector<String>();

    // isUsed is set when a relationship is actually used by a concept/term.
    public boolean isUsed = false;

    public OBORelation(CachingMessageDirectorIF rLogger) {
        super(rLogger);
    }

    public boolean isReady() {
        return ((!StringUtils.isNull(id)) && (!StringUtils.isNull(name)));
    }

    public String printIt() {
        return "Relation:{" + "prefix: " + prefix + ", " + "id: " + id + ", " + "altIds: [" + altIds.toString() + "], "
                + "name: " + name + ", " + "namespace: " + namespace + ", " + "Anonymous: " + isAnonymous + ", "
                + "Transitive: " + isTransitive + ", " + "Symmetric: " + isSymmetric + ", " + "Reflexive: "
                + isReflexive + ", " + "Cyclic: " + isCyclic + ", " + "InverseOf: " + inverseOf + ", " + "isUsed: "
                + isUsed + ", " + "definition: " + definition + ", " + "definitionSrcs: ["
                + definitionSources.toString() + "], " + "comment: " + comment + ", " + "Synonyms: ["
                + synonyms.toString() + "], " + "dbXref: [" + dbXrefs.toString() + "], " + "domain: ["
                + domain.toString() + "], " + "range: [" + range.toString() + "} ";

    }

    public String toString() {
        return "Relation:{" + "prefix: " + prefix + ", " + "id: " + id + ", " + "altIds: [" + altIds.toString() + "], "
                + "name: " + name + ", " + "namespace: " + namespace + ", " + "Anonymous: " + isAnonymous + ", "
                + "Transitive: " + isTransitive + ", " + "Symmetric: " + isSymmetric + ", " + "Reflexive: "
                + isReflexive + ", " + "Cyclic: " + isCyclic + ", " + "InverseOf: " + inverseOf + ", " + "isUsed: "
                + isUsed + ", " + "definition: " + definition + ", " + "definitionSrcs: ["
                + definitionSources.toString() + "], " + "comment: " + comment + ", " + "Synonyms: ["
                + synonyms.toString() + "], " + "dbXref: [" + dbXrefs.toString() + "], " + "domain: ["
                + domain.toString() + "], " + "range: [" + range.toString() + "} \n";

    }

 
    public Boolean getIsTransitive() {
        return isTransitive;
    }

    public void setIsTransitive(Boolean isTransitive) {
        this.isTransitive = isTransitive;
    }

    public Boolean getIsSymmetric() {
        return isSymmetric;
    }

    public void setIsSymmetric(Boolean isSymmetric) {
        this.isSymmetric = isSymmetric;
    }

    public Boolean getIsAntiSymmetric() {
        return isAntiSymmetric;
    }

    public void setIsAntiSymmetric(Boolean isAntiSymmetric) {
        this.isAntiSymmetric = isAntiSymmetric;
    }

    public Boolean getIsReflexive() {
        return isReflexive;
    }

    public void setIsReflexive(Boolean isReflexive) {
        this.isReflexive = isReflexive;
    }

    public Boolean getIsCyclic() {
        return isCyclic;
    }

    public void setIsCyclic(Boolean isCyclic) {
        this.isCyclic = isCyclic;
    }

    public Vector<String> getDomain() {
        return domain;
    }

    public void setDomain(Vector<String> domain) {
        this.domain = domain;
    }

    public String getInverseOf() {
        return inverseOf;
    }

    public void setInverseOf(String inverseOf) {
        this.inverseOf = inverseOf;
    }


    public void setAntiSymmetric(boolean isAntiSymmetric) {
        this.isAntiSymmetric = isAntiSymmetric;
    }

 
    public void setCyclic(boolean isCyclic) {
        this.isCyclic = isCyclic;
    }

 

    public void setReflexive(boolean isReflexive) {
        this.isReflexive = isReflexive;
    }


    public void setSymmetric(boolean isSymmetric) {
        this.isSymmetric = isSymmetric;
    }


    public void setTransitive(boolean isTransitive) {
        this.isTransitive = isTransitive;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }

    public Vector<String> getRange() {
        return range;
    }

    public void setRange(Vector<String> range) {
        this.range = range;
    }

    public void merge(OBORelation relation) {
        super.merge(relation);
        if (relation.isTransitive != null) {
            this.isTransitive = relation.isTransitive;
        }
        if (relation.isSymmetric != null) {
            this.isSymmetric = relation.isSymmetric;
        }
        if (relation.isAntiSymmetric != null) {
            this.isAntiSymmetric = relation.isAntiSymmetric;
        }
        if (relation.isReflexive != null) {
            this.isReflexive = relation.isReflexive;
        }
        if (relation.isCyclic != null) {
            this.isCyclic = relation.isCyclic;
        }

        if (!StringUtils.isNull(relation.inverseOf)) {
            this.inverseOf = relation.inverseOf;
        }

        this.domain.addAll(relation.domain);
        this.range.addAll(relation.range);
    }

}