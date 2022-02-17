
package edu.mayo.informatics.lexgrid.convert.directConversions.hl7.mif.vocabulary;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class MifCodeSystemVersion implements Serializable{

    // Attribute from containing grandparent VocabularyModel object
    private String vmCombinedId;
    
    // Attribute from containing parent CodeSystem object
    private String csCodeSystemId;
    
    // CodeSystemVersion attributes (XML element is releasedVersion):
    private String releaseDate;
//    private String publisherVersionId;
    private List<String> supportedLanguages;
    
    // Collection of Concept objects
    private List<MifConcept> concepts;

    public MifCodeSystemVersion() {
        super();
        supportedLanguages = new ArrayList<String>();
        concepts = new ArrayList<MifConcept>();
    }

    public String getVmCombinedId() {
        return vmCombinedId;
    }

    public void setVmCombinedId(String vmCombinedId) {
        this.vmCombinedId = vmCombinedId;
    }

    public String getCodeSystemId() {
        return csCodeSystemId;
    }

    public void setCodeSystemId(String csCodeSystemId) {
        this.csCodeSystemId = csCodeSystemId;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

//    public String getPublisherVersionId() {
//        return publisherVersionId;
//    }
//
//    public void setPublisherVersionId(String publisherVersionId) {
//        this.publisherVersionId = publisherVersionId;
//    }

    public List<String> getSupportedLanguages() {
        return supportedLanguages;
    }

    public void setSupportedLanguages(List<String> supportedLanguages) {
        this.supportedLanguages = supportedLanguages;
    }

    public List<MifConcept> getConcepts() {
        return concepts;
    }

    public void setConcepts(List<MifConcept> concepts) {
        this.concepts = concepts;    }
    
    
}