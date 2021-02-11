
package edu.mayo.informatics.lexgrid.convert.directConversions.hl7.mif.vocabulary;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MifConceptProperty implements Serializable {

    // Attributes from great great grandparent VocabularyModel object
    private String vmCombinedId;
    
    // Attributes from great grandparent CodeSystem object
    private String csCodeSystemId;
    
    // Attributes from grandparent CodeSystemVersion object
    private String csvReleaseDate;

    private String name;
    private String value;
    
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
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
       
}