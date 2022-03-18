
package edu.mayo.informatics.resourcereader.obo;

import java.util.Vector;

import org.LexGrid.LexBIG.Utility.logging.CachingMessageDirectorIF;

import edu.mayo.informatics.resourcereader.core.StringUtils;
import edu.mayo.informatics.resourcereader.core.IF.ResourceEntity;

/**
 * A commom class that is used to store common entity information
 * 
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public abstract class OBOEntity extends OBO implements ResourceEntity {
    public String id = null;
    public String prefix = null;
    public String name = null;
    public String namespace = null;
    public boolean isAnonymous = false;
    public boolean isObsolete = false;
    public String replacedBy = null;
    public String consider = null;
    public String comment = null;
    public String definition = null;
    

    public String created_by = null;
    public String creation_date = null;
    public Vector<OBODbxref> definitionSources = new Vector<OBODbxref>();
    public Vector<OBODbxref> dbXrefs = new Vector<OBODbxref>();
    public Vector<OBOSynonym> synonyms = new Vector<OBOSynonym>();
    public Vector<String> altIds = new Vector<String>();

    public OBOEntity() {
        super();
    }

    public OBOEntity(CachingMessageDirectorIF rLogger) {
        super(rLogger);
    }

    public Vector<String> getAltIds() {
        return altIds;
    }

    public void setAltIds(Vector<String> altIds) {
        this.altIds = altIds;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getConsider() {
        return consider;
    }

    public void setConsider(String consider) {
        this.consider = consider;
    }

    public Vector<OBODbxref> getDbXrefs() {
        return dbXrefs;
    }

    public void setDbXrefs(Vector<OBODbxref> dbXrefs) {
        this.dbXrefs = dbXrefs;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }
    
    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
    }

    public Vector<OBODbxref> getDefinitionSources() {
        return definitionSources;
    }

    public void setDefinitionSources(Vector<OBODbxref> definitionSources) {
        this.definitionSources = definitionSources;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isAnonymous() {
        return isAnonymous;
    }

    public void setAnonymous(boolean isAnonymous) {
        this.isAnonymous = isAnonymous;
    }

    public boolean isObsolete() {
        return isObsolete;
    }

    public void setObsolete(boolean isObsolete) {
        this.isObsolete = isObsolete;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getReplacedBy() {
        return replacedBy;
    }

    public void setReplacedBy(String replacedBy) {
        this.replacedBy = replacedBy;
    }

    public Vector<OBOSynonym> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(Vector<OBOSynonym> synonyms) {
        this.synonyms = synonyms;
    }

    /**
     * The merge operation would cause all the non null string data elements of
     * entity to overwrite their counterparts in the current instance. The
     * Vector data elements are merged. The boolean variables are overwritten
     * with the data counterpart in entity.
     * 
     * @param entity
     */
    public void merge(OBOEntity entity) {
        if (!StringUtils.isNull(entity.id)) {
            this.id = entity.id;
        }
        if (!StringUtils.isNull(entity.prefix)) {
            this.prefix = entity.prefix;
        }
        if (!StringUtils.isNull(entity.name)) {
            this.name = entity.name;
        }
        if (!StringUtils.isNull(entity.namespace)) {
            this.namespace = entity.namespace;
        }
        if (!StringUtils.isNull(entity.replacedBy)) {
            this.replacedBy = entity.replacedBy;
        }
        if (!StringUtils.isNull(entity.consider)) {
            this.consider = entity.consider;
        }
        if (!StringUtils.isNull(entity.comment)) {
            this.comment = entity.comment;
        }
        if (!StringUtils.isNull(entity.definition)) {
            this.definition = entity.definition;
        }
        if (!StringUtils.isNull(entity.created_by)) {
            this.created_by = entity.created_by;
        }
        if (!StringUtils.isNull(entity.creation_date)) {
            this.creation_date = entity.creation_date;
        }

        this.isAnonymous = entity.isAnonymous;
        this.isObsolete = entity.isObsolete;
        this.definitionSources.addAll(entity.definitionSources);
        this.dbXrefs.addAll(entity.dbXrefs);
        this.synonyms.addAll(entity.synonyms);
        this.altIds.addAll(entity.altIds);

    }
}