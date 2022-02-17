
package edu.mayo.informatics.resourcereader.obo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

import org.LexGrid.LexBIG.Utility.logging.CachingMessageDirectorIF;

import edu.mayo.informatics.resourcereader.core.StringUtils;
import edu.mayo.informatics.resourcereader.core.IF.ResourceEntity;
import edu.mayo.informatics.resourcereader.core.IF.ResourceException;

/**
 * The class stores the list of OBO relations
 * 
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class OBORelations extends OBOCollection {
    private Hashtable<String, OBORelation> relationsByIDs;
    private Hashtable<String, OBORelation> relationsByNames;

    public OBORelations(CachingMessageDirectorIF rLogger) {
        super(rLogger);
        relationsByIDs = new Hashtable<String, OBORelation>();
        relationsByNames = new Hashtable<String, OBORelation>();
        init();
    }

    public void init() {
        // populate built-in relations
        OBORelation rel = new OBORelation(logger);
        rel.id = OBOConstants.BLTNREL_ISA_ID;
        rel.name = OBOConstants.BLTNREL_ISA_NAME;
        rel.domain.add(OBOConstants.BLTNREL_ISA_DOMAIN);
        rel.range.add(OBOConstants.BLTNREL_ISA_RANGE);
        rel.definition = OBOConstants.BLTNREL_ISA_DEF;
        // rel.definitionSources.add(OBOConstants.BLTNREL_ISA_DEFSRC);
        // rel.inverseOf= OBOConstants.BLTNREL_ISA_INVERSEOF;
        try {
            addMember(rel);
        } catch (ResourceException e) {
            logger.warn(e.getMessage());
        }

        rel = new OBORelation(logger);
        rel.id = OBOConstants.BLTNREL_DISJFROM_ID;
        rel.name = OBOConstants.BLTNREL_DISJFROM_NAME;
        rel.domain.add(OBOConstants.BLTNREL_DISJFROM_DOMAIN);
        rel.range.add(OBOConstants.BLTNREL_DISJFROM_RANGE);
        rel.definition = OBOConstants.BLTNREL_DISJFROM_DEF;
        // rel.definitionSources.add(OBOConstants.BLTNREL_DISJFROM_DEFSRC);
        try {
            addMember(rel);
        } catch (ResourceException e) {
            logger.warn(e.getMessage());
        }

        rel = new OBORelation(logger);
        rel.id = OBOConstants.BLTNREL_UNIONOF_ID;
        rel.name = OBOConstants.BLTNREL_UNIONOF_NAME;
        rel.domain.add(OBOConstants.BLTNREL_UNIONOF_DOMAIN);
        rel.range.add(OBOConstants.BLTNREL_UNIONOF_RANGE);
        rel.definition = OBOConstants.BLTNREL_UNIONOF_DEF;
        // rel.definitionSources.add(OBOConstants.BLTNREL_UNIONOF_DEFSRC);
        try {
            addMember(rel);
        } catch (ResourceException e) {
            logger.warn(e.getMessage());
        }

        rel = new OBORelation(logger);
        rel.id = OBOConstants.BLTNREL_INTRSECOF_ID;
        rel.name = OBOConstants.BLTNREL_INTRSECOF_NAME;
        rel.domain.add(OBOConstants.BLTNREL_INTRSECOF_DOMAIN);
        rel.range.add(OBOConstants.BLTNREL_INTRSECOF_RANGE);
        rel.definition = OBOConstants.BLTNREL_INTRSECOF_DEF;
        // rel.definitionSources.add(OBOConstants.BLTNREL_INTRSECOF_DEFSRC);
        try {
            addMember(rel);
        } catch (ResourceException e) {
            logger.warn(e.getMessage());
        }

        rel = new OBORelation(logger);
        rel.id = OBOConstants.BLTNREL_INSTOF_ID;
        rel.name = OBOConstants.BLTNREL_INSTOF_NAME;
        rel.domain.add(OBOConstants.BLTNREL_INSTOF_DOMAIN);
        rel.range.add(OBOConstants.BLTNREL_INSTOF_RANGE);
        rel.definition = OBOConstants.BLTNREL_INSTOF_DEF;
        // rel.definitionSources.add(OBOConstants.BLTNREL_INSTOF_DEFSRC);
        try {
            addMember(rel);
        } catch (ResourceException e) {
            logger.warn(e.getMessage());
        }

        rel = new OBORelation(logger);
        rel.id = OBOConstants.BLTNREL_INVOF_ID;
        rel.name = OBOConstants.BLTNREL_INVOF_NAME;
        rel.domain.add(OBOConstants.BLTNREL_INVOF_DOMAIN);
        rel.range.add(OBOConstants.BLTNREL_INVOF_RANGE);
        rel.definition = OBOConstants.BLTNREL_INVOF_DEF;
        // rel.definitionSources.add(OBOConstants.BLTNREL_INVOF_DEFSRC);
        try {
            addMember(rel);
        } catch (ResourceException e) {
            logger.warn(e.getMessage());
        }
    }

    public void addMember(ResourceEntity relp) throws ResourceException {
        if ((relp != null) && (relp instanceof OBORelation)) {
            OBORelation rel = (OBORelation) relp;

            if (!StringUtils.isNull(rel.id)) {
                if (!relationsByIDs.containsKey(rel.id))
                    relationsByIDs.put(rel.id, rel);
                else {
                    if (logger != null) {
                        logger.info("Relation with ID:" + rel.id + " already exists!");
                    }
                }

                if (!relationsByNames.containsKey(rel.name))
                    relationsByNames.put(rel.name, rel);
                else {
                    if (logger != null) {
                        logger.info("Relation with name:" + rel.name + " already exists!");
                    }
                }
            }
        }
    }

    public void addMergeMember(OBORelation rel) throws ResourceException {
        if (rel != null) {
            if (!StringUtils.isNull(rel.id)) {
                if (!relationsByIDs.containsKey(rel.id))
                    relationsByIDs.put(rel.id, rel);
                else {
                    OBORelation existing_rel = getMemberById(rel.id);
                    existing_rel.merge(rel);
                }

                if (!relationsByNames.containsKey(rel.name))
                    relationsByNames.put(rel.name, rel);
               
            }
        }
    }

    public OBORelation getMemberById(String id) throws ResourceException {
        return relationsByIDs.get(id);
    }

    public OBORelation getMemberByName(String name) throws ResourceException {
        return relationsByNames.get(name);
    }

    public Collection<OBORelation> getAllMembers() {
        return relationsByIDs.values();
    }

    /**
     * Don't use this function as the rel.isUsed is not being set correctly
     * while the OBO file is being read.
     * 
     */
    protected Collection<OBORelation> getAllUsedRelationsDelete() {
        Collection<OBORelation> allUsedAbb = new ArrayList<OBORelation>();

        for (OBORelation rel : relationsByIDs.values())
            if (rel.isUsed)
                allUsedAbb.add(rel);

        return allUsedAbb;
    }

    public long getMembersCount() {
        return relationsByIDs.size();
    }

    public String toString() {
        return relationsByIDs.toString();
    }

}