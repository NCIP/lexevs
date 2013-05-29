package edu.mayo.informatics.lexgrid.convert.directConversions.hl7.mif.vocabulary;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represent information for the HL7 VocabularyModel object as represented in the 
 * HL7's coremif XML file.  This object contains all other vocabulary related objects as 
 * composition/aggregation type relationships.  The start XML element for this object is
 * "<vocabularyModel>".
 *  
 * @author m046445
 *
 */
@SuppressWarnings("serial")
public class MifVocabularyModel implements Serializable {
    
    // VocabularyModel object attributes:
    private String name;  // name attribute of the <vocabularyModel> element
    private String title; // title attribute of the <vocabularyModel> element
    private String schemaVersion;  // schemaVersion attribute of the <vocabularyModel> element
    private String combinedId;  // combinedId attribute of the <vocabularyModel>/<packageLocation> element
    
    // Collection of CodeSytem objects
    private List<MifCodeSystem> codeSystems;
    
    public MifVocabularyModel() {
        super();
        codeSystems = new ArrayList<MifCodeSystem>();
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSchemaVersion() {
        return schemaVersion;
    }
    
    public void setSchemaVersion(String schemaVersion) {
        this.schemaVersion = schemaVersion;
    }
    
    public String getCombinedId() {
        return combinedId;
    }
    
    public void setCombinedId(String combinedId) {
        this.combinedId = combinedId;
    }

    public List<MifCodeSystem> getCodeSystems() {
        return codeSystems;
    }

    public void setCodeSystems(List<MifCodeSystem> codeSystems) {
        this.codeSystems = codeSystems;
    }
   
    
}
