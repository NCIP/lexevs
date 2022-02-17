
package edu.mayo.informatics.lexgrid.convert.directConversions.hl7.mif.vocabulary;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MifConceptRelationship implements Serializable {

    // Attributes from great great grandparent VocabularyModel object
    private String vmCombinedId;
    
    // Attributes from great grandparent CodeSystem object
    private String csCodeSystemId;
    
    // Attributes from grandparent CodeSystemVersion object
    private String csvReleaseDate;

    private String relationshipName;
    
    private String targetConceptCode;
    
    private String targetCodeSystemId;
    
    public String getVmCombinedId() {
        return vmCombinedId;
    }
    
    public void setVmCombinedId(String vmCombinedId) {
        this.vmCombinedId = vmCombinedId;
    }
    
    public String getCsCodeSystemId() {
        return csCodeSystemId;
    }
    
    public void setCsCodeSystemId(String csCodeSystemId) {
        this.csCodeSystemId = csCodeSystemId;
    }
    
    public String getCsvReleaseDate() {
        return csvReleaseDate;
    }
    
    public void setCsvReleaseDate(String csvReleaseDate) {
        this.csvReleaseDate = csvReleaseDate;
    }
    
    public String getRelationshipName() {
        return relationshipName;
    }
    
    public void setRelationshipName(String relationshipName) {
        this.relationshipName = relationshipName;
    }
    
    public String getTargetConceptCode() {
        return targetConceptCode;
    }
    
    public void setTargetConceptCode(String targetConceptCode) {
        this.targetConceptCode = targetConceptCode;
    }

    public String getTargetCodeSystemId() {
        return targetCodeSystemId;
    }

    public void setTargetCodeSystemId(String targetCodeSystemId) {
        this.targetCodeSystemId = targetCodeSystemId;
    }
    
}