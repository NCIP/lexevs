
package edu.mayo.informatics.lexgrid.convert.directConversions.hl7.mif.vocabulary;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
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
    private String xmlns; // xmlns attribute of the <vocabularyModel> element
    private String name;  // name attribute of the <vocabularyModel> element
    private String title; // title attribute of the <vocabularyModel> element
    private String schemaVersion;  // schemaVersion attribute of the <vocabularyModel> element
    private String combinedId;  // combinedId attribute of the <vocabularyModel>/<packageLocation> element
    private String defaultLanguage; // There is no corresponding element in XML file - parser defaults to "en" value

    // Collection below is the complete list of SupportedConceptRelationships found in the HL7 MIF Vocab XML file
    private HashMap<String, MifSupportedConceptRelationship> supportedConceptRelationshipsMap;
    // Collection below is the complete list of unique SupportedConceptProperty items found in the HL7 MIF Vocab XML file
    private HashMap<String, MifSupportedConceptProperty> supportedConceptPropertiesMap;

    // Collection of CodeSytem objects
    private List<MifCodeSystem> codeSystems;
    
    public MifVocabularyModel() {
        super();
        codeSystems = new ArrayList<MifCodeSystem>();
    }

    public String getXmlns() {
        return xmlns;
    }

    public void setXmlns(String xmlns) {
        this.xmlns = xmlns;
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

    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    public void setDefaultLanguage(String defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }

    public List<MifCodeSystem> getCodeSystems() {
        return codeSystems;
    }

    public void setCodeSystems(List<MifCodeSystem> codeSystems) {
        this.codeSystems = codeSystems;
    }

    public HashMap<String, MifSupportedConceptRelationship> getSupportedConceptRelationshipsMap() {
        return supportedConceptRelationshipsMap;
    }

    public void setSupportedConceptRelationshipsMap(
            HashMap<String, MifSupportedConceptRelationship> supportedConceptRelationshipsMap) {
        this.supportedConceptRelationshipsMap = supportedConceptRelationshipsMap;
    }
    public HashMap<String, MifSupportedConceptProperty> getSupportedConceptPropertiesMap() {
        return supportedConceptPropertiesMap;
    }

    public void setSupportedConceptPropertiesMap(HashMap<String, MifSupportedConceptProperty> supportedConceptPropertiesMap) {
        this.supportedConceptPropertiesMap = supportedConceptPropertiesMap;
    }

   
    
}