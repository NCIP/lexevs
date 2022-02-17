
package edu.mayo.informatics.lexgrid.convert.directConversions.hl7.mif.vocabulary;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MifConceptCode implements Serializable { 
    
    // Attributes from great great grandparent VocabularyModel object
    private String vmCombinedId;
    
    // Attributes from great grandparent CodeSystem object
    private String csCodeSystemId;
    
    // Attributes from grandparent CodeSystemVersion object
    private String csvReleaseDate;

    private String code;
    private String status;
    
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
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
}